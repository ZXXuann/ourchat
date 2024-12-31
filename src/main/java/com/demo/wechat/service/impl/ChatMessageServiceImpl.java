package com.demo.wechat.service.impl;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.ArrayUtil;
import com.demo.wechat.entity.config.AppConfig;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.MessageSendDto;
import com.demo.wechat.entity.dto.SysSettingDto;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.ChatSession;
import com.demo.wechat.entity.po.ChatSessionUser;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.*;
import com.demo.wechat.enums.*;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.mappers.ChatMessageMapper;
import com.demo.wechat.mappers.ChatSessionMapper;
import com.demo.wechat.mappers.ChatSessionUserMapper;
import com.demo.wechat.mappers.UserContactMapper;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.service.ChatMessageService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.ChatMessage;
import com.demo.wechat.utils.CopyTools;
import com.demo.wechat.utils.DateUtils;
import com.demo.wechat.utils.StringTools;
import com.demo.wechat.websocket.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.redisson.config.Config;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
/**
 * @Description: 聊天消息表 业务接口实现
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
@Service("ChatMessageMapper")
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService{

	@Resource
	private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;
	@Resource
	private RedisComponent redisComponent;
	@Resource
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;
	@Resource
	private MessageHandler messageHandler;
	@Resource
	private AppConfig appConfig;
	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;
	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<ChatMessage> findListByParam(ChatMessageQuery query) {
		return this.chatMessageMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(ChatMessageQuery query) {
		return this.chatMessageMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<ChatMessage> findListByPage(ChatMessageQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<ChatMessage> list = this.findListByParam(query);
		PaginationResultVO<ChatMessage> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(ChatMessage bean) {
		return this.chatMessageMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<ChatMessage> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.chatMessageMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<ChatMessage> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.chatMessageMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 MessageId 查询
 	 */
	@Override
	public ChatMessage getChatMessageByMessageId(Long messageId) {
		return this.chatMessageMapper.selectByMessageId(messageId);}

	/**
 	 * 根据 MessageId 更新
 	 */
	@Override
	public Integer updateChatMessageByMessageId(ChatMessage bean, Long messageId) {
		return this.chatMessageMapper.updateByMessageId(bean, messageId);}

	/**
 	 * 根据 MessageId 删除
 	 */
	@Override
	public Integer deleteChatMessageByMessageId(Long messageId) {
		return this.chatMessageMapper.deleteByMessageId(messageId);}

	@Override
	public MessageSendDto saveMessage(ChatMessage chatMessage, TokenUserInfoDto tokenUserInfoDto) {
		//	不是机器人回复，判断好友状态
		if(!Constants.ROBOT_UID.equals(tokenUserInfoDto.getUserId())){
			List<String> contactList=redisComponent.getUserContactList(tokenUserInfoDto.getUserId());
			if(!contactList.contains(chatMessage.getContactId())){
				UserContactTypeEnum userContactTypeEnum=UserContactTypeEnum.getByPrefix(chatMessage.getContactId());
				if(UserContactTypeEnum.USER==userContactTypeEnum){
					throw new BusinessException(ResponseCodeEnum.CODE_902);
				}else{
					throw new BusinessException(ResponseCodeEnum.CODE_903);
				}
			}
		}
		String sessionId=null;
		String sendUserId=tokenUserInfoDto.getUserId();
		String contactId=chatMessage.getContactId();
		UserContactTypeEnum contactTypeEnum=UserContactTypeEnum.getByPrefix(contactId);
		//联系人类型
		if(UserContactTypeEnum.USER==contactTypeEnum){
			sessionId= StringTools.getChatSessionId4User(new String[]{sendUserId,contactId});
		}else{
			sessionId=StringTools.getChatSessionId4Group(contactId);
		}
		chatMessage.setSessionId(sessionId);

		Long curTime=System.currentTimeMillis();
		chatMessage.setSendTime(curTime);

		MessageTypeEnum messageTypeEnum=MessageTypeEnum.getByType(chatMessage.getMessageType());
		if(null==messageTypeEnum||!ArrayUtil.contains(new Integer[]{MessageTypeEnum.CHAT.getType(),
				MessageTypeEnum.MEDIA_CHAT.getType()},chatMessage)){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		Integer status=MessageTypeEnum.MEDIA_CHAT==messageTypeEnum? MessageStatusEnum.SENDED.getStatus():MessageStatusEnum.SENDING.getStatus();
		chatMessage.setStatus(status);

		String messageContent=StringTools.cleanHtmlTag(chatMessage.getMessageContent());
		chatMessage.setMessageContent(messageContent);

		//更新会话
		ChatSession chatSession=new ChatSession();
		chatSession.setLastMessage(messageContent);
		if(UserContactTypeEnum.GROUP==contactTypeEnum){
			chatSession.setLastMessage(tokenUserInfoDto.getNickName()+":"+messageContent);
		}
		chatSession.setLastReceiveTime(curTime);
		chatSessionMapper.updateBySessionId(chatSession,sessionId);

		//记录消息
		chatMessage.setSendUserId(sendUserId);
		chatMessage.setSendUserNickName(tokenUserInfoDto.getNickName());
		chatMessage.setContactType(contactTypeEnum.getType());
		chatMessageMapper.insert(chatMessage);
		MessageSendDto messageSendDto= CopyTools.copy(chatMessage,MessageSendDto.class);

		if(Constants.ROBOT_UID.equals(contactId)){
			SysSettingDto sysSettingDto= redisComponent.getSysSetting();
			TokenUserInfoDto robot=new TokenUserInfoDto();
			robot.setUserId(sysSettingDto.getRobotUid());
			robot.setNickName(sysSettingDto.getRobotNickName());
			ChatMessage robotChatMessage=new ChatMessage();
			robotChatMessage.setContactId(sendUserId);
			//可以对阶ai，实现聊天
			robotChatMessage.setMessageContent("我只是一个机器人无法识别你的消息");
			robotChatMessage.setMessageType(MessageTypeEnum.CHAT.getType());
			saveMessage(robotChatMessage,robot);
		}else{
			messageHandler.sendMessage(messageSendDto);
		}
		return messageSendDto;

	}

	@Override
	public void saveMessageFile(String userId, Long messageId, MultipartFile file, MultipartFile cover) {
		ChatMessage chatMessage=chatMessageMapper.selectByMessageId(messageId);
		if(chatMessage==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if(!chatMessage.getSendUserId().equals(userId)){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		SysSettingDto sysSettingDto= redisComponent.getSysSetting();
		String fileSuffix=StringTools.getFileSuffix(file.getOriginalFilename());
		if(!StringTools.isEmpty(fileSuffix)
			&&ArrayUtil.contains(Constants.IMAGE_SUFFIX_LIST,fileSuffix.toUpperCase())
			&&file.getSize()>sysSettingDto.getMaxImageSize()* Constants.FILE_SIZE_MB){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		} else if (!StringTools.isEmpty(fileSuffix)
			&&ArrayUtil.contains(Constants.VIDEO_SUFFIX_LIST,fileSuffix.toUpperCase())
			&&file.getSize()>sysSettingDto.getMaxImageSize()*Constants.FILE_SIZE_MB) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}else if(!StringTools.isEmpty(fileSuffix)
			&&!ArrayUtil.contains(Constants.VIDEO_SUFFIX_LIST,fileSuffix.toUpperCase())
			&&!ArrayUtil.contains(Constants.IMAGE_SUFFIX_LIST,fileSuffix.toUpperCase())
			&&file.getSize()>sysSettingDto.getMaxFileSize()*Constants.FILE_SIZE_MB){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		String fileName=file.getOriginalFilename();
		String fileExtName=StringTools.getFileSuffix(fileName);
		String fileRealName=messageId+fileExtName;
		String month= DateUtils.format(new Date(chatMessage.getSendTime()),DateTimePatternEnum.YYYYMM.getPattern());
		File folder =new File(appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+month);
		if(!folder.exists()){
			folder.mkdirs();
		}
		File uploadFile=new File(folder.getPath()+"/"+fileRealName);
		try{
			file.transferTo(uploadFile);
			cover.transferTo(new File(uploadFile.getPath()+Constants.COVER_IMAGE_SUFFIX));
		}catch (IOException e){
			log.error("上传文件失败",e);
			throw new BusinessException("文件上传失败");
		}
		ChatMessage uploadInfo=new ChatMessage();
		uploadInfo.setStatus(MessageStatusEnum.SENDED.getStatus());
		ChatMessageQuery messageQuery=new ChatMessageQuery();
		messageQuery.setMessageId(messageId);
		chatMessageMapper.updateByParam(uploadInfo,messageQuery);

		MessageSendDto messageSendDto=new MessageSendDto();
		messageSendDto.setStatus(MessageStatusEnum.SENDED.getStatus());
		messageSendDto.setMessageId(messageId);
		messageSendDto.setMessageType(MessageTypeEnum.FILE_UPLOAD.getType());
		messageSendDto.setContactId(chatMessage.getContactId());
		messageHandler.sendMessage(messageSendDto);
	}

	@Override
	public File downloadFile(TokenUserInfoDto userInfoDto, Long messageId, Boolean showCover) {
		ChatMessage message=chatMessageMapper.selectByMessageId(messageId);
		String contactId=message.getContactId();
		UserContactTypeEnum contactTypeEnum=UserContactTypeEnum.getByPrefix(contactId);
		if(UserContactTypeEnum.USER==contactTypeEnum&&!userInfoDto.getUserId().equals(message.getContactId())){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if(UserContactTypeEnum.GROUP==contactTypeEnum){
			UserContactQuery userContactQuery=new UserContactQuery();
			userContactQuery.setUserId(userInfoDto.getUserId());
			userContactQuery.setContactType(UserContactTypeEnum.GROUP.getType());
			userContactQuery.setContactId(contactId);
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			Integer contactCount= userContactMapper.selectCount(userContactQuery);
			if(contactCount==0){
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
		}
		String month=DateUtils.format(new Date(message.getSendTime()),DateTimePatternEnum.YYYYMM.getPattern());
		File folder=new File(appConfig.getProjectFolder()+ Constants.FILE_FOLDER_FILE+month);
		if(!folder.exists()){
			folder.mkdirs();
		}
		String fileName=message.getFileName();
		String fileExtName=StringTools.getFileSuffix(fileName);
		String fileRealName=messageId+fileExtName;
		if(showCover!=null&&showCover){
			fileExtName=fileRealName+Constants.COVER_IMAGE_SUFFIX;
		}
		File file=new File(folder.getPath()+"/"+fileRealName);
		if(!file.exists()){
			log.info("文件不存在",messageId);
			throw new BusinessException(ResponseCodeEnum.CODE_602);
		}
		return file;
	}
}