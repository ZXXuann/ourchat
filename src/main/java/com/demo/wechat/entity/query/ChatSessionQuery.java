package com.demo.wechat.entity.query;



/**
 * @Description: 会话信息
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
public class ChatSessionQuery extends BaseQuery {
	/**
 	 * 会话ID 查询对象
 	 */
	private String sessionId;
	private String sessionIdFuzzy;
	/**
 	 * 最后接受的消息 查询对象
 	 */
	private String lastMessage;
	private String lastMessageFuzzy;
	/**
 	 * 最后接受消息时间豪秒 查询对象
 	 */
	private Long lastReceiveTime;
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastReceiveTime(Long lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	public Long getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setSessionIdFuzzy(String sessionIdFuzzy) {
		this.sessionIdFuzzy = sessionIdFuzzy;
	}

	public String getSessionIdFuzzy() {
		return sessionIdFuzzy;
	}

	public void setLastMessageFuzzy(String lastMessageFuzzy) {
		this.lastMessageFuzzy = lastMessageFuzzy;
	}

	public String getLastMessageFuzzy() {
		return lastMessageFuzzy;
	}
}