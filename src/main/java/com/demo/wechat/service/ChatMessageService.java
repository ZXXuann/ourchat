package com.demo.wechat.service;


import java.io.File;
import java.util.List;

import com.demo.wechat.entity.dto.MessageSendDto;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.ChatMessage;
import com.demo.wechat.entity.query.ChatMessageQuery;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 聊天消息表 Service
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
public interface ChatMessageService{

	/**
 	 * 根据条件查询列表
 	 */
	List<ChatMessage> findListByParam(ChatMessageQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(ChatMessageQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<ChatMessage> findListByPage(ChatMessageQuery query);

	/**
 	 * 新增
 	 */
	Integer add(ChatMessage bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<ChatMessage> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<ChatMessage> listBean);

	/**
 	 * 根据 MessageId 查询
 	 */
	ChatMessage getChatMessageByMessageId(Long messageId);

	/**
 	 * 根据 MessageId 更新
 	 */
	Integer updateChatMessageByMessageId(ChatMessage bean, Long messageId); 

	/**
 	 * 根据 MessageId 删除
 	 */
	Integer deleteChatMessageByMessageId(Long messageId);
	MessageSendDto saveMessage(ChatMessage chatMessage, TokenUserInfoDto tokenUserInfoDto);
	void saveMessageFile(String userId, Long messageId, MultipartFile file,MultipartFile cover);
	File downloadFile(TokenUserInfoDto userInfoDto,Long fileId,Boolean showCover);
}