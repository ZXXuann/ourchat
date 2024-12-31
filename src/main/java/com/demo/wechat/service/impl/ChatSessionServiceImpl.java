package com.demo.wechat.service.impl;


import java.util.List;
import com.demo.wechat.entity.query.SimplePage;
import com.demo.wechat.enums.PageSize;
import com.demo.wechat.mappers.ChatSessionMapper;
import com.demo.wechat.service.ChatSessionService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.ChatSession;
import com.demo.wechat.entity.query.ChatSessionQuery;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
 * @Description: 会话信息 业务接口实现
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
@Service("ChatSessionMapper")
public class ChatSessionServiceImpl implements ChatSessionService{

	@Resource
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<ChatSession> findListByParam(ChatSessionQuery query) {
		return this.chatSessionMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(ChatSessionQuery query) {
		return this.chatSessionMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<ChatSession> findListByPage(ChatSessionQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<ChatSession> list = this.findListByParam(query);
		PaginationResultVO<ChatSession> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(ChatSession bean) {
		return this.chatSessionMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<ChatSession> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.chatSessionMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<ChatSession> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.chatSessionMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 SessionId 查询
 	 */
	@Override
	public ChatSession getChatSessionBySessionId(String sessionId) {
		return this.chatSessionMapper.selectBySessionId(sessionId);}

	/**
 	 * 根据 SessionId 更新
 	 */
	@Override
	public Integer updateChatSessionBySessionId(ChatSession bean, String sessionId) {
		return this.chatSessionMapper.updateBySessionId(bean, sessionId);}

	/**
 	 * 根据 SessionId 删除
 	 */
	@Override
	public Integer deleteChatSessionBySessionId(String sessionId) {
		return this.chatSessionMapper.deleteBySessionId(sessionId);}
}