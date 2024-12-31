package com.demo.wechat.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.ArrayUtil;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.MessageSendDto;
import com.demo.wechat.entity.dto.SysSettingDto;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.GroupInfo;
import com.demo.wechat.entity.po.Info;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.*;
import com.demo.wechat.enums.*;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.mappers.GroupInfoMapper;
import com.demo.wechat.mappers.InfoMapper;
import com.demo.wechat.mappers.UserContactApplyMapper;
import com.demo.wechat.mappers.UserContactMapper;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.service.UserContactApplyService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.UserContactApply;
import com.demo.wechat.service.UserContactService;
import com.demo.wechat.utils.StringTools;
import com.demo.wechat.websocket.MessageHandler;
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
	private UserContactMapper<UserContact,UserContactQuery> userContactMapper;
	@Resource
	private RedisComponent redisComponent;
	@Resource
	private MessageHandler messageHandler;
	@Resource
	private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;
	@Resource
	private UserContactService userContactService;
	@Resource
	private InfoMapper<Info, InfoQuery> userInfoMapper;
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
		UserContact userContact= (UserContact) userContactMapper.selectByUserIdAndContactId(applyUserId,contactId);
//		UserContactStatusEnum.BLACKLIST_BE.getStatus().equals(userContact.getStatus())
		if(userContact!=null&& ArrayUtil.contains(new Integer[]{
				UserContactStatusEnum.BLACKLIST_BE.getStatus(),
				UserContactStatusEnum.BLACKLIST.getStatus()
		},userContact.getStatus())){
			throw new BusinessException("对方已经拉黑你，无法添加");
		}
		//判断是否为申请群组
		if(UserContactTypeEnum.GROUP==typeEnum){
			//获取群组信息
			GroupInfo groupInfo= (GroupInfo) groupInfoMapper.selectByGroupId(contactId);
			//群组消息是否为空 并且 群组是否已解散
//			GroupStatusEnum.DISSOLUTION.getStatus().equals(groupInfo.getStatus())
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
			userContactService.addContact(applyUserId,receiveUserId,contactId,typeEnum.getType(),applyInfo);
//			//双向设定
//			//我方设定
//				UserContact userContact1=new UserContact();
//				//设置添加人
//				userContact1.setUserId(tokenUserInfoDto.getUserId());
//				//设置被添加人
//				userContact1.setContactId(contactId);
//				//设置创建的时间
//				userContact1.setCreateTime(nowTime);
//				//设置状态
//				userContact1.setStatus(UserContactStatusEnum.FRIEND.getStatus());
//				//设置更新时间
//				userContact1.setLastUpdateTime(nowTime);
//			//他放设定
//				UserContact userContact2=new UserContact();
//				userContact2.setUserId(contactId);
//				userContact2.setContactId(tokenUserInfoDto.getUserId());
//				userContact2.setCreateTime(nowTime);
//				userContact2.setStatus(UserContactStatusEnum.FRIEND.getStatus());
//				userContact2.setLastUpdateTime(nowTime);
//				//更新数据库
//				userContactMapper.insert(userContact2);
//				userContactMapper.insert(userContact1);
//			//	WS发送信息
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
			MessageSendDto messageSendDto=new MessageSendDto();
			messageSendDto.setMessageType(MessageTypeEnum.CONTACT_APPLY.getType());
			messageSendDto.setContactId(receiveUserId);
			messageHandler.sendMessage(messageSendDto);
		}
		return joinType;
	}



}