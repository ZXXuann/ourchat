package com.demo.wechat.entity.po;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @Description: 
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
public class UserContactApply implements Serializable {
	/**
 	 * 申请的ID
 	 */
	private Integer applyId;

	/**
 	 * 申请人ID
 	 */
	private String applyUserId;

	/**
 	 * 接收人ID
 	 */
	private String receiveUserId;

	/**
 	 * 联系人类型 0好友 1群组
 	 */
	private Integer contactType;

	/**
 	 * 联系人群组ID

 	 */
	private String contactId;

	/**
 	 * 最后申请时间
 	 */
	private Long lastApplyTime;

	/**
 	 * 状态 0待处理 1已同意 2已拒绝 3已拉黑
 	 */
	@JsonIgnore
	private Integer status;

	/**
 	 * 申请信息
 	 */
	private String applyInfo;


	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}

	public Integer getApplyId() {
		return applyId;
	}

	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getApplyUserId() {
		return applyUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}

	public Integer getContactType() {
		return contactType;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setLastApplyTime(Long lastApplyTime) {
		this.lastApplyTime = lastApplyTime;
	}

	public Long getLastApplyTime() {
		return lastApplyTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setApplyInfo(String applyInfo) {
		this.applyInfo = applyInfo;
	}

	public String getApplyInfo() {
		return applyInfo;
	}
	@Override
	public String toString() {
		return "申请的ID:" + (applyId == null ? "空" : applyId) + "," + 
				"申请人ID:" + (applyUserId == null ? "空" : applyUserId) + "," + 
				"接收人ID:" + (receiveUserId == null ? "空" : receiveUserId) + "," + 
				"联系人类型 0好友 1群组:" + (contactType == null ? "空" : contactType) + "," + 
				"联系人群组ID:" + (contactId == null ? "空" : contactId) + "," +
				"最后申请时间:" + (lastApplyTime == null ? "空" : lastApplyTime) + "," + 
				"状态 0待处理 1已同意 2已拒绝 3已拉黑:" + (status == null ? "空" : status) + "," + 
				"申请信息:" + (applyInfo == null ? "空" : applyInfo);
		}
}