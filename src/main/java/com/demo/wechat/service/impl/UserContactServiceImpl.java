package com.demo.wechat.service.impl;


import java.util.Date;
import java.util.List;

import cn.hutool.system.UserInfo;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.dto.UserContactSearchResultDto;
import com.demo.wechat.entity.po.GroupInfo;
import com.demo.wechat.entity.po.Info;
import com.demo.wechat.entity.po.UserContactApply;
import com.demo.wechat.entity.query.SimplePage;
import com.demo.wechat.enums.*;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.mappers.GroupInfoMapper;
import com.demo.wechat.mappers.InfoMapper;
import com.demo.wechat.mappers.UserContactApplyMapper;
import com.demo.wechat.mappers.UserContactMapper;
import com.demo.wechat.service.UserContactService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.UserContactQuery;
import com.demo.wechat.utils.CopyTools;
import com.demo.wechat.utils.StringTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
 * @Description:  业务接口实现
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
@Service("UserContactMapper")
public class UserContactServiceImpl implements UserContactService{

	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;
	@Resource
	private InfoMapper userInfoMapper;
	@Resource
	private GroupInfoMapper groupInfoMapper;
	@Resource
	private UserContactApplyMapper userContactApplyMapper;
	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<UserContact> findListByParam(UserContactQuery query) {
		return this.userContactMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(UserContactQuery query) {
		return this.userContactMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<UserContact> findListByPage(UserContactQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserContact> list = this.findListByParam(query);
		PaginationResultVO<UserContact> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(UserContact bean) {
		return this.userContactMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<UserContact> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userContactMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<UserContact> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.userContactMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 UserIdAndContactId 查询
 	 */
	@Override
	public UserContact getUserContactByUserIdAndContactId(String userId, String contactId) {
		return this.userContactMapper.selectByUserIdAndContactId(userId, contactId);}

	/**
 	 * 根据 UserIdAndContactId 更新
 	 */
	@Override
	public Integer updateUserContactByUserIdAndContactId(UserContact bean, String userId, String contactId) {
		return this.userContactMapper.updateByUserIdAndContactId(bean, userId, contactId);}

	/**
 	 * 根据 UserIdAndContactId 删除
 	 */
	@Override
	public Integer deleteUserContactByUserIdAndContactId(String userId, String contactId) {
		return this.userContactMapper.deleteByUserIdAndContactId(userId, contactId);}

	@Override
	public UserContactSearchResultDto searchContact(String userId, String contactId) {
		UserContactTypeEnum typeEnum=UserContactTypeEnum.getByPrefix(contactId);
		if(typeEnum==null){
			return null;
		}
		//查询
		UserContactSearchResultDto resultDto=new UserContactSearchResultDto();
		switch(typeEnum){
			case USER:
				UserInfo userInfo= (UserInfo) userInfoMapper.selectByUserId(contactId);
				if(userInfo==null) return null;
				//将查询到的玩意塞入dto
				resultDto= CopyTools.copy(userInfo,UserContactSearchResultDto.class);
				break;
			case GROUP:
				GroupInfo groupInfo= (GroupInfo) groupInfoMapper.selectByGroupId(contactId);
				if(groupInfo==null) return null;
				resultDto=CopyTools.copy(groupInfo,UserContactSearchResultDto.class);
				break;
		}
		resultDto.setContactType(typeEnum.toString());
		resultDto.setContactId(contactId);
		if(userId.equals(contactId)){
			resultDto.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			return resultDto;
		}

		UserContact userContact=this.userContactMapper.selectByUserIdAndContactId(userId,contactId);
		resultDto.setStatus(userContact==null?null:userContact.getStatus());
		return resultDto;
	}
	//实现申请添加好友或者加入群聊的逻辑
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer applyAdd(TokenUserInfoDto tokenUserInfoDto, String contactId, String applyInfo) {
		//联系人类型判断
		UserContactTypeEnum typeEnum=UserContactTypeEnum.getByPrefix(contactId);
		//判断类型是用户还是群组
		if(null==typeEnum){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//设置申请信息
		String applyUserId=tokenUserInfoDto.getUserId();
		//默认申请信息
		applyInfo= StringTools.isEmpty(applyInfo)?String.format(Constants.APPLY_INFO_TEMPLATE,tokenUserInfoDto.getNickName()):applyInfo;
		Long curTime= System.currentTimeMillis();
		Date nowTime=new Date();
		Integer joinType=null;
		String receiveUserId=contactId;
		//查询对方好友（群组）是否已经添加，如果已经拉黑则无法添加
		UserContact userContact=userContactMapper.selectByUserIdAndContactId(applyUserId,contactId);
		if(userContact!=null&&UserContactStatusEnum.BLACKLIST_BE.getStatus().equals(userContact.getStatus())){
			throw new BusinessException("对方已经拉黑你，无法添加");
		}
		//判断是否为申请群组
		if(UserContactTypeEnum.GROUP==typeEnum){
			//获取群组信息
			GroupInfo groupInfo= (GroupInfo) groupInfoMapper.selectByGroupId(contactId);
			//群组消息是否为空 并且 群组是否已解散
			if(groupInfo==null|| GroupStatusEnum.DISSOLUTION.getStatus().equals(groupInfo.getStatus())){
				throw new BusinessException("群聊不存在或已解散");
			}
			//设置接收人
			//获取加入类型（是否需要审核）
			receiveUserId =groupInfo.getGroupOwnerId();
			joinType=groupInfo.getJoinType();
		}else{
			//如果不是申请群组，而是加好友
			//那么先查询User
			Info userInfo= (Info) userInfoMapper.selectByUserId(contactId);
			//为空则抛出异常
			if(userInfo==null){
				throw new BusinessException("申请添加的人不存在");
			}
			//获取加入的类型（是否需要审核）
			joinType=userInfo.getJoinType();
		}
		//若是直接可以加入则添加好友
		if(JoinTypeEnum.JOIN.getType().equals(joinType)){

			//双向设定
			//我方设定
				UserContact userContact1=new UserContact();
				//设置添加人
				userContact1.setUserId(tokenUserInfoDto.getUserId());
				//设置被添加人
				userContact1.setContactId(contactId);
				//设置创建的时间
				userContact1.setCreateTime(nowTime);
				//设置状态
				userContact1.setStatus(UserContactStatusEnum.FRIEND.getStatus());
				//设置更新时间
				userContact1.setLastUpdateTime(nowTime);
			//他放设定
				UserContact userContact2=new UserContact();
				userContact2.setUserId(contactId);
				userContact2.setContactId(tokenUserInfoDto.getUserId());
				userContact2.setCreateTime(nowTime);
				userContact2.setStatus(UserContactStatusEnum.FRIEND.getStatus());
				userContact2.setLastUpdateTime(nowTime);
				//更新数据库
				userContactMapper.insert(userContact2);
				userContactMapper.insert(userContact1);
			//	WS发送信息
			//TODO 添加联系人
			return joinType;
		}
		//查看是否有相似的申请
		UserContactApply dbApply= (UserContactApply) this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId,receiveUserId,contactId);
		//若无，则插入信息
		if(dbApply==null){
			UserContactApply contactApply=new UserContactApply();
			contactApply.setApplyUserId(applyUserId);
			contactApply.setContactId(contactId);
			contactApply.setReceiveUserId(receiveUserId);
			contactApply.setLastApplyTime(curTime);
			//待审核状态
			contactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
			contactApply.setApplyInfo(applyInfo);
			this.userContactApplyMapper.insert(contactApply);
		}else{
			//更新记录
			UserContactApply contactApply=new UserContactApply();
			contactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
			contactApply.setLastApplyTime(curTime);
			contactApply.setApplyInfo(applyInfo);
			this.userContactApplyMapper.updateByApplyId(contactApply,dbApply.getApplyId());
		}
		//若是申请为空或者为INIT状态，则发送WS
		if(dbApply==null||UserContactApplyStatusEnum.INIT.getStatus().equals(dbApply.getStatus())){
			//TODO 发送WS消息
		}
		return joinType;
	}
}