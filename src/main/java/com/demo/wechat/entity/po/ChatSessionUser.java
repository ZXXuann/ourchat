package com.demo.wechat.entity.po;

import com.demo.wechat.enums.UserContactTypeEnum;

import java.io.Serializable;


/**
 * @Description: 会话用户
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
public class ChatSessionUser implements Serializable {
	/**
 	 * 用户ID
 	 */
	private String userId;

	/**
 	 * 联系人ID
 	 */
	private String contactId;

	/**
 	 * 会话ID
 	 */
	private String sessionId;

	/**
 	 * 联系人名称
 	 */
	private String contactName;
	private String lastMessage;
	private String lastReceiveTime;
	private Integer memberCount;
	private Integer contactType;

	public ChatSessionUser() {
	}

	public ChatSessionUser(String userId, String contactId, String sessionId, String contactName, String lastMessage, String lastReceiveTime, Integer memberCount, Integer contactType) {
		this.userId = userId;
		this.contactId = contactId;
		this.sessionId = sessionId;
		this.contactName = contactName;
		this.lastMessage = lastMessage;
		this.lastReceiveTime = lastReceiveTime;
		this.memberCount = memberCount;
		this.contactType = contactType;
	}

	public Integer getContactType() {
		return UserContactTypeEnum.getByPrefix(contactId).getType();
	}

	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}

	public Integer getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setLastReceiveTime(String lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactName() {
		return contactName;
	}
	@Override
	public String toString() {
		return "用户ID:" + (userId == null ? "空" : userId) + "," + 
				"联系人ID:" + (contactId == null ? "空" : contactId) + "," + 
				"会话ID:" + (sessionId == null ? "空" : sessionId) + "," + 
				"联系人名称:" + (contactName == null ? "空" : contactName);
		}
}