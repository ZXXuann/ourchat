package com.demo.wechat.controller;


import java.util.List;
import com.demo.wechat.service.ChatMessageService;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.po.ChatMessage;
import com.demo.wechat.entity.query.ChatMessageQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
/**
 * @Description: 聊天消息表 Controller
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
@RestController
@RequestMapping("/chatMessage")
public class ChatMessageController extends ABaseController{

	@Resource
	private ChatMessageService chatMessageService;

	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ChatMessageQuery query) {
		return getSuccessResponseVO(chatMessageService.findListByPage(query));
	}

	/**
 	 * 新增
 	 */
	@RequestMapping("/add")
	public ResponseVO add(ChatMessage bean) {
		Integer result = this.chatMessageService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增
 	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ChatMessage> listBean) {
		this.chatMessageService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增或修改
 	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ChatMessage> listBean) {
		this.chatMessageService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 根据 MessageId 查询
 	 */
	@RequestMapping("/getChatMessageByMessageId")
	public ResponseVO getChatMessageByMessageId(Long messageId) {
		return getSuccessResponseVO(chatMessageService.getChatMessageByMessageId(messageId));}

	/**
 	 * 根据 MessageId 更新
 	 */
	@RequestMapping("/updateChatMessageByMessageId")
	public ResponseVO updateChatMessageByMessageId(ChatMessage bean, Long messageId) {
		this.chatMessageService.updateChatMessageByMessageId(bean, messageId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 MessageId 删除
 	 */
	@RequestMapping("/deleteChatMessageByMessageId")
	public ResponseVO deleteChatMessageByMessageId(Long messageId) {
		this.chatMessageService.deleteChatMessageByMessageId(messageId);
		return getSuccessResponseVO(null);
}
}