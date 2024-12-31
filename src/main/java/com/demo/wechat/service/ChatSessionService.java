package com.demo.wechat.service;


import java.util.List;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.ChatSession;
import com.demo.wechat.entity.query.ChatSessionQuery;
/**
 * @Description: 会话信息 Service
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
public interface ChatSessionService{

	/**
 	 * 根据条件查询列表
 	 */
	List<ChatSession> findListByParam(ChatSessionQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(ChatSessionQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<ChatSession> findListByPage(ChatSessionQuery query);

	/**
 	 * 新增
 	 */
	Integer add(ChatSession bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<ChatSession> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<ChatSession> listBean);

	/**
 	 * 根据 SessionId 查询
 	 */
	ChatSession getChatSessionBySessionId(String sessionId);

	/**
 	 * 根据 SessionId 更新
 	 */
	Integer updateChatSessionBySessionId(ChatSession bean, String sessionId); 

	/**
 	 * 根据 SessionId 删除
 	 */
	Integer deleteChatSessionBySessionId(String sessionId);
}