package com.demo.wechat.controller;


import java.util.List;
import com.demo.wechat.service.ChatSessionUserService;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.po.ChatSessionUser;
import com.demo.wechat.entity.query.ChatSessionUserQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
/**
 * @Description: 会话用户 Controller
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
@RestController
@RequestMapping("/chatSessionUser")
public class ChatSessionUserController extends ABaseController{

	@Resource
	private ChatSessionUserService chatSessionUserService;

	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ChatSessionUserQuery query) {
		return getSuccessResponseVO(chatSessionUserService.findListByPage(query));
	}

	/**
 	 * 新增
 	 */
	@RequestMapping("/add")
	public ResponseVO add(ChatSessionUser bean) {
		Integer result = this.chatSessionUserService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增
 	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ChatSessionUser> listBean) {
		this.chatSessionUserService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增或修改
 	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ChatSessionUser> listBean) {
		this.chatSessionUserService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 根据 UserIdAndContactId 查询
 	 */
	@RequestMapping("/getChatSessionUserByUserIdAndContactId")
	public ResponseVO getChatSessionUserByUserIdAndContactId(String userId, String contactId) {
		return getSuccessResponseVO(chatSessionUserService.getChatSessionUserByUserIdAndContactId(userId, contactId));}

	/**
 	 * 根据 UserIdAndContactId 更新
 	 */
	@RequestMapping("/updateChatSessionUserByUserIdAndContactId")
	public ResponseVO updateChatSessionUserByUserIdAndContactId(ChatSessionUser bean, String userId, String contactId) {
		this.chatSessionUserService.updateChatSessionUserByUserIdAndContactId(bean, userId, contactId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 UserIdAndContactId 删除
 	 */
	@RequestMapping("/deleteChatSessionUserByUserIdAndContactId")
	public ResponseVO deleteChatSessionUserByUserIdAndContactId(String userId, String contactId) {
		this.chatSessionUserService.deleteChatSessionUserByUserIdAndContactId(userId, contactId);
		return getSuccessResponseVO(null);
}
}