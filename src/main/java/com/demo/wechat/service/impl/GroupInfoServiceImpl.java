package com.demo.wechat.service.impl;


import java.io.File;
import java.util.Date;
import java.util.List;

import com.demo.wechat.entity.config.AppConfig;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.MessageSendDto;
import com.demo.wechat.entity.dto.SysSettingDto;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.*;
import com.demo.wechat.entity.query.*;
import com.demo.wechat.enums.*;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.mappers.*;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.service.ChatMessageService;
import com.demo.wechat.service.ChatSessionUserService;
import com.demo.wechat.service.GroupInfoService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.service.UserContactService;
import com.demo.wechat.utils.CopyTools;
import com.demo.wechat.utils.StringTools;
import com.demo.wechat.websocket.ChannelContextUtils;
import com.demo.wechat.websocket.MessageHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.swing.*;

/**
 * @Description:  业务接口实现
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
@Service("GroupInfoMapper")
public class GroupInfoServiceImpl implements GroupInfoService{

	@Resource
	private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;
	@Resource
	private RedisComponent redisComponent;
	@Resource
	private UserContactMapper userContactMapper;
	@Resource
	private AppConfig appConfig;
	@Resource
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;
	@Resource
	private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;
	@Resource
	private ChatMessageMapper<ChatMessage,ChatMessageQuery> chatMessageMapper;
	@Resource
	private MessageHandler messageHandler;
	@Resource
	private ChannelContextUtils channelContextUtils;
	@Resource
	private ChatSessionUserService chatSessionUserService;
	@Resource
	private UserContactService userContactService;
	@Resource
	private InfoMapper<Info,InfoQuery> userInfoMapper;
	@Resource
	@Lazy
	private GroupInfoService groupInfoService;
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void dissolutionGroup(String groupOwnerId, String groupId) {
		GroupInfo dbInfo=this.groupInfoMapper.selectByGroupId(groupId);
		//判断传入的群主信息id是否一致
		if(null==dbInfo||!dbInfo.getGroupOwnerId().equals(groupOwnerId)){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//删除群组
		GroupInfo updateInfo=new GroupInfo();
		updateInfo.setStatus(GroupStatusEnum.DISSOLUTION.getStatus());
		this.groupInfoMapper.updateByGroupId(updateInfo,groupId);
		//更新联系人的信息
		UserContactQuery userContactQuery=new UserContactQuery();
		userContactQuery.setContactId(groupId);
		userContactQuery.setContactType(UserContactTypeEnum.GROUP.getType());
		UserContact updatedUserContact=new UserContact();
		updatedUserContact.setStatus(UserContactStatusEnum.DEL.getStatus());
		this.userContactMapper.updateByParam(updatedUserContact,userContactQuery);

		List<UserContact> userContactList=this.userContactMapper.selectList(userContactQuery);
		for(UserContact userContact:userContactList){
			redisComponent.removeUserContact(userContact.getUserId(),userContact.getContactId());
		}
		String sessionId=StringTools.getChatSessionId4Group(groupId);
		Date curDate=new Date();
		String messageContent=MessageTypeEnum.DISSOLUTION_GROUP.getInitMessage();
		ChatSession chatSession=new ChatSession();
		chatSession.setLastMessage(messageContent);
		chatSession.setLastReceiveTime(curDate.getTime());
		chatSessionMapper.updateBySessionId(chatSession,sessionId);

		ChatMessage chatMessage=new ChatMessage();
		chatMessage.setSessionId(sessionId);
		chatMessage.setSendTime(curDate.getTime());
		chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
		chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
		chatMessage.setMessageType(MessageTypeEnum.DISSOLUTION_GROUP.getType());
		chatMessage.setContactId(groupId);
		chatMessage.setMessageContent(messageContent);
		chatMessageMapper.insert(chatMessage);
		MessageSendDto messageSendDto=CopyTools.copy(chatMessage,MessageSendDto.class);
		messageHandler.sendMessage(messageSendDto);
		//TODO 移除群员
		//TODO 发消息 1.更新回话信息 2.记录群信息 3.发送解散通知信息
	}

	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<GroupInfo> findListByParam(GroupInfoQuery query) {
		return this.groupInfoMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(GroupInfoQuery query) {
		return this.groupInfoMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<GroupInfo> list = this.findListByParam(query);
		PaginationResultVO<GroupInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(GroupInfo bean) {
		return this.groupInfoMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<GroupInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.groupInfoMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<GroupInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.groupInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 GroupId 查询
 	 */
	@Override
	public GroupInfo getGroupInfoByGroupId(String groupId) {
		return this.groupInfoMapper.selectByGroupId(groupId);}

	/**
 	 * 根据 GroupId 更新
 	 */
	@Override
	public Integer updateGroupInfoByGroupId(GroupInfo bean, String groupId) {
		return this.groupInfoMapper.updateByGroupId(bean, groupId);}

	/**
 	 * 根据 GroupId 删除
 	 */
	@Override
	public Integer deleteGroupInfoByGroupId(String groupId) {
		return this.groupInfoMapper.deleteByGroupId(groupId);}

	@Override
	public void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) {
		Date currentDate=new Date();
		if(StringTools.isEmpty(groupInfo.getGroupId())){
			//查询群主的群聊数量
			GroupInfoQuery groupInfoQuery=new GroupInfoQuery();
			groupInfoQuery.setGroupOwnerId((groupInfo.getGroupOwnerId()));
			Integer count=this.groupInfoMapper.selectCount(groupInfoQuery);
			SysSettingDto sysSettingDto=redisComponent.getSysSetting();
			if(count>=sysSettingDto.getMaxGroupCount()){
				throw new BusinessException("最多能创建"+sysSettingDto.getMaxGroupCount()+"个群聊");
			}
			//查询图片是否为空
			if(null==avatarFile){
				throw new BusinessException("图片不能为空");
			}
			///保存当前的群聊信息
			groupInfo.setCreateTime(currentDate);
			groupInfo.setGroupId(StringTools.getGroupId());
			this.groupInfoMapper.insert(groupInfo);
			//将群组添加为联系人
			UserContact userContact=new UserContact();
			userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			userContact.setContactType(UserContactTypeEnum.GROUP.getType());
			userContact.setContactId(groupInfo.getGroupId());
			userContact.setUserId(groupInfo.getGroupOwnerId());
			userContact.setCreateTime(currentDate);
			userContact.setLastUpdateTime(currentDate);
			this.userContactMapper.insert(userContact);

			//创建会话
			String sessionId=StringTools.getChatSessionId4Group(groupInfo.getGroupId());
			ChatSession chatSession=new ChatSession();
			chatSession.setSessionId(sessionId);
			chatSession.setLastMessage(MessageTypeEnum.GROUP_CREATE.getInitMessage());
			chatSession.setLastReceiveTime(currentDate.getTime());
			this.chatSessionMapper.insertOrUpdate(chatSession);

			ChatSessionUser chatSessionUser=new ChatSessionUser();
			chatSessionUser.setUserId(groupInfo.getGroupOwnerId());
			chatSessionUser.setContactId(groupInfo.getGroupId());
			chatSessionUser.setContactName(groupInfo.getGroupName());
			chatSessionUser.setSessionId(sessionId);
			this.chatSessionUserMapper.insert(chatSessionUser);

			//创建消息
			ChatMessage chatMessage=new ChatMessage();
			chatMessage.setSessionId(sessionId);
			chatMessage.setMessageType(MessageTypeEnum.GROUP_CREATE.getType());
			chatMessage.setMessageContent(MessageTypeEnum.GROUP_CREATE.getInitMessage());
			chatMessage.setSendTime(currentDate.getTime());
			chatMessage.setContactId(groupInfo.getGroupId());
			chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
			chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
			chatMessageMapper.insert(chatMessage);
			//将群组添加到联系人
			redisComponent.addUserContact(groupInfo.getGroupOwnerId(),groupInfo.getGroupId());
			//将联系人通道添加到群组通道
			channelContextUtils.addUser2Group(groupInfo.getGroupOwnerId(),groupInfo.getGroupId());

			//发送ws消息
			chatSessionUser.setLastMessage(MessageTypeEnum.GROUP_CREATE.getInitMessage());
			chatSessionUser.setLastReceiveTime(String.valueOf(currentDate.getTime()));
			chatSessionUser.setMemberCount(1);

			MessageSendDto messageSendDto= CopyTools.copy(chatMessage,MessageSendDto.class);
			messageSendDto.setExtendData(chatSessionUser);
			messageSendDto.setLastMessage(chatSessionUser.getLastMessage());
			messageHandler.sendMessage(messageSendDto);
		}else{
			GroupInfo dbInfo=this.groupInfoMapper.selectByGroupId(groupInfo.getGroupId());
			if(!dbInfo.getGroupOwnerId().equals(groupInfo.getGroupOwnerId())){
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			this.groupInfoMapper.updateByGroupId(groupInfo,groupInfo.getGroupId());
			//更新相关表的冗余信息
			String contactNameUpdate=null;
			if(!dbInfo.getGroupName().equals(groupInfo.getGroupName())){
				contactNameUpdate=groupInfo.getGroupName();
			}
			if(contactNameUpdate==null){
				return;
			}
			chatSessionUserService.updateRedundancyInfo(contactNameUpdate,groupInfo.getGroupId());

		}
		if(null==avatarFile){
			return;
		}
		//获取存储文件的路径
		String baseFolder=appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
		File targetFileFolder=new File(baseFolder+Constants.FILE_FOLDER_AVATAR_NAME);
		if(!targetFileFolder.exists()){
			targetFileFolder.mkdirs();
		}
	}

	@Override
	public void addOrRemoveGroupUser(TokenUserInfoDto tokenUserInfoDto, String groupId, String contactIds, Integer opType) {
		GroupInfo groupInfo=groupInfoMapper.selectByGroupId(groupId);
		if(null==groupInfo||!groupInfo.getGroupOwnerId().equals(tokenUserInfoDto.getUserId())){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		String[] contactIdList=contactIds.split(",");
		for(String contactId:contactIdList){
			if(Constants.ZERO.equals(opType)){
				groupInfoService.leaveGroup(contactId,groupId,MessageTypeEnum.REMOVE_GROUP);
			}else {
				userContactService.addContact(contactId,null,groupId,UserContactTypeEnum.GROUP.getType(), null);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void leaveGroup(String userId, String groupId, MessageTypeEnum messageTypeEnum) {
		GroupInfo groupInfo=groupInfoMapper.selectByGroupId(groupId);
		if(groupInfo==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if(userId.equals(groupInfo.getGroupOwnerId())){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		Integer count=userContactMapper.deleteByUserIdAndContactId(userId,groupId);
		if(count==0){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		Info userInfo=userInfoMapper.selectByUserId(userId);

		String sessionId=StringTools.getChatSessionId4Group(groupId);
		Date curDate=new Date();
		String messageContent=String.format(messageTypeEnum.getInitMessage(),userInfo.getNickName());

		ChatSession chatSession=new ChatSession();
		chatSession.setLastMessage(messageContent);
		chatSession.setLastReceiveTime(curDate.getTime());
		chatSessionMapper.updateBySessionId(chatSession,sessionId);

		ChatMessage chatMessage=new ChatMessage();
		chatMessage.setSessionId(sessionId);
		chatMessage.setSendTime(curDate.getTime());
		chatMessage.setContactType(UserContactTypeEnum.GROUP.getType());
		chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
		chatMessage.setMessageType(messageTypeEnum.getType());
		chatMessage.setContactId(groupId);
		chatMessage.setMessageContent(messageContent);
		chatMessageMapper.insert(chatMessage);

		UserContactQuery userContactQuery=new UserContactQuery();
		userContactQuery.setContactId(groupId);
		userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		Integer memberCount=this.userContactMapper.selectCount(userContactQuery);
		MessageSendDto messageSendDto=CopyTools.copy(chatMessage,MessageSendDto.class);
		messageSendDto.setExtendData(userId);
		messageSendDto.setMemberCount(memberCount);
		messageHandler.sendMessage(messageSendDto);
	}
}