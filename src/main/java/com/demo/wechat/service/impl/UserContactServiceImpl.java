package com.demo.wechat.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.system.UserInfo;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.MessageSendDto;
import com.demo.wechat.entity.dto.SysSettingDto;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.dto.UserContactSearchResultDto;
import com.demo.wechat.entity.po.*;
import com.demo.wechat.entity.query.*;
import com.demo.wechat.enums.*;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.mappers.*;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.service.UserContactApplyService;
import com.demo.wechat.service.UserContactService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.utils.CopyTools;
import com.demo.wechat.utils.StringTools;
import com.demo.wechat.websocket.ChannelContextUtils;
import com.demo.wechat.websocket.MessageHandler;
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
//	@Resource
//	private UserContactApplyMapper userContactApplyMapper;
//	@Resource
//	private UserContactApplyService userContactApplyService;
	@Resource
	private RedisComponent redisComponent;
	@Resource
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;
	@Resource
	private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;
	@Resource
	private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;
	@Resource
	private ChannelContextUtils channelContextUtils;
	@Resource
	private MessageHandler messageHandler;
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void removeUserContact(String userId, String contactId, UserContactStatusEnum statusEnum) {
		//从自身的好友中移除好友
		UserContact userContact=new UserContact();
		userContact.setStatus(statusEnum.getStatus());
		userContactMapper.updateByUserIdAndContactId(userContact,userId,contactId);
		//从好友移除自身
		UserContact friendContact=new UserContact();
		if(UserContactStatusEnum.DEL==statusEnum){
			friendContact.setStatus(UserContactStatusEnum.DEL_BE.getStatus());
		} else if (UserContactStatusEnum.BLACKLIST==statusEnum) {
			friendContact.setStatus(UserContactStatusEnum.BLACKLIST_BE.getStatus());
		}
		userContactMapper.updateByUserIdAndContactId(friendContact,contactId,userId);
		//从我的好友列表缓存中删除好友
		redisComponent.removeUserContact(contactId,userId);
		//从好友列表缓存中删除自身
		redisComponent.removeUserContact(userId,contactId);


	}
	/**
	 * 添加好友
	 * @param applyUserId 申请人
	 * @param receiveUserId 接收申请的人
	 * @param contactId 群的ID或者接收申请人的ID
	 * @param contactType 当前这一条申请的类型 群还是人？
	 * @param applyInfo 申请的消息
	 */
	@Override
	public void addContact(String applyUserId, String receiveUserId, String contactId, Integer contactType, String applyInfo) {
		//获取群聊人数(若是群聊）
		if(UserContactTypeEnum.GROUP.getType().equals(contactType)){
			UserContactQuery userContactQuery=new UserContactQuery();
			//接收申请人的ID，也就是在谁的列表
			userContactQuery.setContactId(contactId);
			//状态调为好友状态
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			//查询人数
			Integer count=userContactMapper.selectCount(userContactQuery);
			if(count>=redisComponent.getSysSetting().getMaxGroupMemberCount()){
				throw new BusinessException("成员已满，无法加入");
			}
		}
		Date curDate=new Date();
		List<UserContact> contactList=new ArrayList<>();
		//申请人添加对方
		UserContact userContact=new UserContact();
		userContact.setUserId(applyUserId);
		userContact.setContactId(contactId);
		userContact.setContactType(contactType);
		userContact.setCreateTime(curDate);
		userContact.setLastUpdateTime(curDate);
		userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		contactList.add(userContact);
		//如果是申请好友，接受人添加申请人，群组不用添加对方为好友
		if(UserContactTypeEnum.USER.getType().equals(contactType)){
			userContact=new UserContact();
			userContact.setUserId(receiveUserId);
			userContact.setContactId(applyUserId);
			userContact.setContactType(contactType);
			userContact.setCreateTime(curDate);
			userContact.setLastUpdateTime(curDate);
			userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			contactList.add(userContact);
		}
		userContactMapper.insertOrUpdateBatch(contactList);
		if(UserContactTypeEnum.USER.getType().equals(contactType)){
			//往redis添加联系人
			redisComponent.addUserContact(receiveUserId,applyUserId);
		}
		redisComponent.addUserContact(applyUserId,contactId);
		//创建会话
		String sessionId=null;
		if(UserContactTypeEnum.USER.getType().equals(contactType)){
			sessionId=StringTools.getChatSessionId4User(new String[]{applyUserId,contactId});
		}else{
			sessionId=StringTools.getChatSessionId4Group(contactId);
		}

		List<ChatSessionUser> chatSessionUserList=new ArrayList<>();
		if(UserContactTypeEnum.USER.getType().equals(contactType)){
			ChatSession chatSession=new ChatSession();
			chatSession.setSessionId(sessionId);
			chatSession.setLastReceiveTime(curDate.getTime());
			this.chatSessionMapper.insertOrUpdate(chatSession);

			//申请人session
			ChatSessionUser applySessionUser=new ChatSessionUser();
			applySessionUser.setUserId(applyUserId);
			applySessionUser.setContactId(contactId);
			applySessionUser.setSessionId(sessionId);
			Info contactUser= (Info) this.userInfoMapper.selectByUserId(contactId);
			applySessionUser.setContactName(contactUser.getNickName());
			chatSessionUserList.add(applySessionUser);

			//接收人session
			ChatSessionUser contactSessionUser=new ChatSessionUser();
			contactSessionUser.setUserId(contactId);
			contactSessionUser.setContactName(applyUserId);
			contactSessionUser.setSessionId(sessionId);
			Info applyUserInfo= (Info) this.userInfoMapper.selectByUserId(applyUserId);
			contactSessionUser.setContactName(applyUserInfo.getNickName());
			chatSessionUserList.add(applySessionUser);
			this.chatSessionUserMapper.insertOrUpdateBatch(chatSessionUserList);

			//记录消息表
			ChatMessage chatMessage=new ChatMessage();
			chatMessage.setSessionId(sessionId);
			chatMessage.setMessageType(MessageTypeEnum.ADD_FRIEND.getType());
			chatMessage.setMessageContent(applyInfo);
			chatMessage.setSendUserId(applyUserId);
			chatMessage.setSendUserNickName(applyUserInfo.getNickName());
			chatMessage.setSendTime(curDate.getTime());
			chatMessage.setContactId(contactId);
			chatMessage.setContactType(UserContactTypeEnum.USER.getType());
			chatMessageMapper.insert(chatMessage);

			MessageSendDto messageSendDto=CopyTools.copy(chatMessage,MessageSendDto.class);
			//发送给接受还有申请的人
			messageHandler.sendMessage(messageSendDto);

			//发送给申请人，发送人就是接收人，联系人就是申请人
			messageSendDto.setMessageType(MessageTypeEnum.ADD_FRIEND_SELF.getType());
			messageSendDto.setContactId(applyUserId);
			messageSendDto.setExtendData(contactUser);
			messageHandler.sendMessage(messageSendDto);
		}else{
			//加入群组
			ChatSessionUser chatSessionUser=new ChatSessionUser();
			chatSessionUser.setUserId(applyUserId);
			chatSessionUser.setContactId(contactId);
			GroupInfo groupInfo= (GroupInfo) this.groupInfoMapper.selectByGroupId(contactId);
			chatSessionUser.setContactName(groupInfo.getGroupName());
			chatSessionUser.setSessionId(sessionId);
			this.chatSessionUserMapper.insertOrUpdate(chatSessionUser);

			Info applyUserInfo= (Info) this.userInfoMapper.selectByUserId(applyUserId);
			String sendMessage=String.format(MessageTypeEnum.ADD_GROUP.getInitMessage(),applyUserInfo.getNickName());
			//增加session信息
			ChatSession chatSession=new ChatSession();
			chatSession.setSessionId(sessionId);
			chatSession.setLastReceiveTime(curDate.getTime());
			chatSession.setLastMessage(sendMessage);
			this.chatSessionMapper.insertOrUpdate(chatSession);

			//增加聊天消息
			ChatMessage chatMessage=new ChatMessage();
			chatMessage.setSessionId(sessionId);
			chatMessage.setMessageType(MessageTypeEnum.ADD_GROUP.getType());
			chatMessage.setMessageContent(sendMessage);
			chatMessage.setSendTime(curDate.getTime());
			chatMessage.setContactId(contactId);
			chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
			chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
			this.chatMessageMapper.insert(chatMessage);

			//将群组添加到联系人
			redisComponent.addUserContact(applyUserId, groupInfo.getGroupId());
			//将联系人通道添加到群组通道
			channelContextUtils.addUser2Group(applyUserId,groupInfo.getGroupId());

			//发送群消息
			MessageSendDto messageSendDto=CopyTools.copy(chatMessage,MessageSendDto.class);
			messageSendDto.setContactId(contactId);
			//获取群人数量
			UserContactQuery userContactQuery=new UserContactQuery();
			userContactQuery.setContactId(contactId);
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			Integer memberCount=this.userContactMapper.selectCount(userContactQuery);
			messageSendDto.setMemberCount(memberCount);
			messageSendDto.setContactName(groupInfo.getGroupName());
			//发消息
			messageHandler.sendMessage(messageSendDto);
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addContact4Robot(String userId) {
		Date curDate=new Date();
		SysSettingDto sysSettingDto= redisComponent.getSysSetting();
		String contactId=sysSettingDto.getRobotUid();
		String contactName=sysSettingDto.getRobotNickName();
		String sendMessage=sysSettingDto.getRobotWelcome();
		sendMessage=StringTools.cleanHtmlTag(sendMessage);
		//增加机器人好友
		UserContact userContact=new UserContact();
		userContact.setUserId(userId);
		userContact.setContactId(contactId);
		userContact.setContactType(UserContactTypeEnum.USER.getType());
		userContact.setCreateTime(curDate);
		userContact.setLastUpdateTime(curDate);
		userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		userContactMapper.insert(userContact);
		//增加会话信息
		String sessionId=StringTools.getChatSessionId4User(new String[]{contactId,userId});
		ChatSession chatSession=new ChatSession();
		chatSession.setLastMessage(sendMessage);
		chatSession.setSessionId(sessionId);
		chatSession.setLastReceiveTime(curDate.getTime());
		this.chatSessionMapper.insert(chatSession);
		//增加聊天信息
		ChatMessage chatMessage=new ChatMessage();
		chatMessage.setSessionId(sessionId);
		chatMessage.setMessageType(MessageTypeEnum.CHAT.getType());
		chatMessage.setMessageContent(sendMessage);
		chatMessage.setSendUserId(contactId);
		chatMessage.setSendUserNickName(contactName);
		chatMessage.setSendTime(curDate.getTime());
		chatMessage.setContactId(userId);
		chatMessage.setContactType(UserContactTypeEnum.USER.getType());
		chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
		chatMessageMapper.insert(chatMessage);
	}
}