package com.demo.wechat.service.impl;


import java.util.List;

import com.demo.wechat.entity.dto.MessageSendDto;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.ChatSessionQuery;
import com.demo.wechat.entity.query.SimplePage;
import com.demo.wechat.entity.query.UserContactQuery;
import com.demo.wechat.enums.MessageTypeEnum;
import com.demo.wechat.enums.PageSize;
import com.demo.wechat.enums.UserContactStatusEnum;
import com.demo.wechat.enums.UserContactTypeEnum;
import com.demo.wechat.mappers.ChatSessionUserMapper;
import com.demo.wechat.mappers.UserContactMapper;
import com.demo.wechat.service.ChatSessionUserService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.ChatSessionUser;
import com.demo.wechat.entity.query.ChatSessionUserQuery;
import com.demo.wechat.websocket.MessageHandler;
import org.apache.catalina.User;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
 * @Description: 会话用户 业务接口实现
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
@Service("ChatSessionUserMapper")
public class ChatSessionUserServiceImpl implements ChatSessionUserService{

	@Resource
	private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;
	@Resource
	private MessageHandler messageHandler;
	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;
	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<ChatSessionUser> findListByParam(ChatSessionUserQuery query) {
		return this.chatSessionUserMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(ChatSessionUserQuery query) {
		return this.chatSessionUserMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<ChatSessionUser> findListByPage(ChatSessionUserQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<ChatSessionUser> list = this.findListByParam(query);
		PaginationResultVO<ChatSessionUser> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(ChatSessionUser bean) {
		return this.chatSessionUserMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<ChatSessionUser> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.chatSessionUserMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<ChatSessionUser> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.chatSessionUserMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 UserIdAndContactId 查询
 	 */
	@Override
	public ChatSessionUser getChatSessionUserByUserIdAndContactId(String userId, String contactId) {
		return this.chatSessionUserMapper.selectByUserIdAndContactId(userId, contactId);}

	/**
 	 * 根据 UserIdAndContactId 更新
 	 */
	@Override
	public Integer updateChatSessionUserByUserIdAndContactId(ChatSessionUser bean, String userId, String contactId) {
		return this.chatSessionUserMapper.updateByUserIdAndContactId(bean, userId, contactId);}

	/**
 	 * 根据 UserIdAndContactId 删除
 	 */
	@Override
	public Integer deleteChatSessionUserByUserIdAndContactId(String userId, String contactId) {
		return this.chatSessionUserMapper.deleteByUserIdAndContactId(userId, contactId);}

	@Override
	public void updateRedundancyInfo(String contactName, String contactId) {
		ChatSessionUser updateInfo=new ChatSessionUser();
		updateInfo.setContactName(contactName);
		ChatSessionUserQuery chatSessionUserQuery=new ChatSessionUserQuery();
		chatSessionUserQuery.setContactId(contactId);
		this.chatSessionUserMapper.updateByParam(updateInfo,chatSessionUserQuery);

		UserContactTypeEnum contactTypeEnum=UserContactTypeEnum.getByPrefix(contactId);
		if(contactTypeEnum==UserContactTypeEnum.GROUP){
			MessageSendDto messageSendDto=new MessageSendDto();
			messageSendDto.setContactType(UserContactTypeEnum.getByPrefix(contactId).getType());
			messageSendDto.setContactId(contactId);
			messageSendDto.setExtendData(contactName);
			messageSendDto.setMessageType(MessageTypeEnum.CONTACT_NAME_UPDATE.getType());
			messageHandler.sendMessage(messageSendDto);
		}else{
			UserContactQuery userContactQuery=new UserContactQuery();
			userContactQuery.setContactType(UserContactTypeEnum.USER.getType());
			userContactQuery.setContactId(contactId);
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			List<UserContact> userContactList=userContactMapper.selectList(userContactQuery);
			for(UserContact userContact:userContactList){
				MessageSendDto messageSendDto=new MessageSendDto();
				messageSendDto.setContactType(UserContactTypeEnum.getByPrefix(contactId).getType());
				messageSendDto.setContactId(userContact.getUserId());
				messageSendDto.setExtendData(contactName);
				messageSendDto.setMessageType(MessageTypeEnum.CONTACT_NAME_UPDATE.getType());
				messageSendDto.setSendUserId(contactId);
				messageSendDto.setSendUserNickName(contactName);
				messageHandler.sendMessage(messageSendDto);
			}
		}

	}
}