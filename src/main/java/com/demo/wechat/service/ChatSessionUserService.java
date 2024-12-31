package com.demo.wechat.service;


import java.util.List;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.ChatSessionUser;
import com.demo.wechat.entity.query.ChatSessionUserQuery;
/**
 * @Description: 会话用户 Service
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
public interface ChatSessionUserService{

	/**
 	 * 根据条件查询列表
 	 */
	List<ChatSessionUser> findListByParam(ChatSessionUserQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(ChatSessionUserQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<ChatSessionUser> findListByPage(ChatSessionUserQuery query);

	/**
 	 * 新增
 	 */
	Integer add(ChatSessionUser bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<ChatSessionUser> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<ChatSessionUser> listBean);

	/**
 	 * 根据 UserIdAndContactId 查询
 	 */
	ChatSessionUser getChatSessionUserByUserIdAndContactId(String userId, String contactId);

	/**
 	 * 根据 UserIdAndContactId 更新
 	 */
	Integer updateChatSessionUserByUserIdAndContactId(ChatSessionUser bean, String userId, String contactId); 

	/**
 	 * 根据 UserIdAndContactId 删除
 	 */
	Integer deleteChatSessionUserByUserIdAndContactId(String userId, String contactId);
	void updateRedundancyInfo(String contactname,String contactId);
}