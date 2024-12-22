package com.demo.wechat.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.demo.wechat.entity.dto.SysSettingDto;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.SimplePage;
import com.demo.wechat.entity.query.UserContactQuery;
import com.demo.wechat.enums.*;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.mappers.UserContactApplyMapper;
import com.demo.wechat.mappers.UserContactMapper;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.service.UserContactApplyService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.UserContactApply;
import com.demo.wechat.entity.query.UserContactApplyQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
 * @Description:  业务接口实现
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
@Service("UserContactApplyMapper")
public class UserContactApplyServiceImpl implements UserContactApplyService{

	@Resource
	private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;
	@Resource
	private UserContactMapper userContactMapper;
	@Resource
	private RedisComponent redisComponent;
	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<UserContactApply> findListByParam(UserContactApplyQuery query) {
		return this.userContactApplyMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(UserContactApplyQuery query) {
		return this.userContactApplyMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserContactApply> list = this.findListByParam(query);
		PaginationResultVO<UserContactApply> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(UserContactApply bean) {
		return this.userContactApplyMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<UserContactApply> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userContactApplyMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<UserContactApply> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userContactApplyMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 ApplyId 查询
 	 */
	@Override
	public UserContactApply getUserContactApplyByApplyId(Integer applyId) {
		return this.userContactApplyMapper.selectByApplyId(applyId);}

	/**
 	 * 根据 ApplyId 更新
 	 */
	@Override
	public Integer updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId) {
		return this.userContactApplyMapper.updateByApplyId(bean, applyId);}

	/**
 	 * 根据 ApplyId 删除
 	 */
	@Override
	public Integer deleteUserContactApplyByApplyId(Integer applyId) {
		return this.userContactApplyMapper.deleteByApplyId(applyId);}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void dealWithApply(String userId, Integer applyId, Integer status) {
		Long curTime=System.currentTimeMillis();
		UserContactApplyStatusEnum statusEnum=UserContactApplyStatusEnum.getByStatus(status);
		//前端传进来的东西不可能是未处理的，现在就是要处理未处理的东西，把它变成已经处理的东西，所以status必须是未处理外的东西
		if(statusEnum==null||UserContactApplyStatusEnum.INIT==statusEnum){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//查询这一个申请的记录是否不存在
		UserContactApply applyInfo=this.userContactApplyMapper.selectByApplyId(applyId);
		if(applyInfo==null||!userId.equals(applyInfo.getReceiveUserId())){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//更新
		UserContactApply updateInfo=new UserContactApply();
		updateInfo.setStatus(statusEnum.getStatus());
		updateInfo.setLastApplyTime(curTime);
		//查询
		UserContactApplyQuery applyQuery=new UserContactApplyQuery();
		applyQuery.setApplyId(applyId);
		applyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
		Integer count=userContactApplyMapper.updateByParam(updateInfo,applyQuery);
		//update xxx set status=1 where xxx,status=0
		//防止多重修改，防止并发修改 实际场景：例如某张优惠券只能被使用一次，但是多线程可能被多次使用，此时就该这么写
		if(count==0){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if(UserContactApplyStatusEnum.PASS.getStatus().equals(status)){
			//添加联系人

			return;
		}
		//用户如果设置为黑名单
		if(UserContactApplyStatusEnum.BLACKLIST==statusEnum){
			Date nowTime=new Date();
			UserContact userContact=new UserContact();
			userContact.setLastUpdateTime(nowTime);
			userContact.setCreateTime(nowTime);
			userContact.setStatus(UserContactStatusEnum.BLACKLIST_BE_FIRST.getStatus());
			userContact.setContactId(applyInfo.getContactId());
			userContact.setContactType(applyInfo.getContactType());
			userContact.setUserId(applyInfo.getApplyUserId());
			userContactMapper.insertOrUpdate(userContact);
		}
	}


}