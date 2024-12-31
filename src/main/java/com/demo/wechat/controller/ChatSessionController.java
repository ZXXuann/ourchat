package com.demo.wechat.controller;


import java.util.List;
import com.demo.wechat.service.ChatSessionService;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.po.ChatSession;
import com.demo.wechat.entity.query.ChatSessionQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
/**
 * @Description: 会话信息 Controller
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
@RestController
@RequestMapping("/chatSession")
public class ChatSessionController extends ABaseController{

	@Resource
	private ChatSessionService chatSessionService;

	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ChatSessionQuery query) {
		return getSuccessResponseVO(chatSessionService.findListByPage(query));
	}

	/**
 	 * 新增
 	 */
	@RequestMapping("/add")
	public ResponseVO add(ChatSession bean) {
		Integer result = this.chatSessionService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增
 	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ChatSession> listBean) {
		this.chatSessionService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增或修改
 	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ChatSession> listBean) {
		this.chatSessionService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 根据 SessionId 查询
 	 */
	@RequestMapping("/getChatSessionBySessionId")
	public ResponseVO getChatSessionBySessionId(String sessionId) {
		return getSuccessResponseVO(chatSessionService.getChatSessionBySessionId(sessionId));}

	/**
 	 * 根据 SessionId 更新
 	 */
	@RequestMapping("/updateChatSessionBySessionId")
	public ResponseVO updateChatSessionBySessionId(ChatSession bean, String sessionId) {
		this.chatSessionService.updateChatSessionBySessionId(bean, sessionId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 SessionId 删除
 	 */
	@RequestMapping("/deleteChatSessionBySessionId")
	public ResponseVO deleteChatSessionBySessionId(String sessionId) {
		this.chatSessionService.deleteChatSessionBySessionId(sessionId);
		return getSuccessResponseVO(null);
}
}