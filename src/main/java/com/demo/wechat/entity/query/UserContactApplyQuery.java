package com.demo.wechat.entity.query;



/**
 * @Description: 
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
public class UserContactApplyQuery extends BaseQuery {
	/**
 	 * 申请的ID 查询对象
 	 */
	private Integer applyId;

	/**
 	 * 申请人ID 查询对象
 	 */
	private String applyUserId;

	private String applyUserIdFuzzy;

	/**
 	 * 接收人ID 查询对象
 	 */
	private String receiveUserId;

	private String receiveUserIdFuzzy;

	/**
 	 * 联系人类型 0好友 1群组 查询对象
 	 */
	private Integer contactType;

	/**
 	 * 联系人群组ID
 查询对象
 	 */
	private String contactId;

	private String contactIdFuzzy;

	/**
 	 * 最后申请时间 查询对象
 	 */
	private Long lastApplyTime;

	/**
 	 * 状态 0待处理 1已同意 2已拒绝 3已拉黑 查询对象
 	 */
	private Integer status;

	/**
 	 * 申请信息 查询对象
 	 */
	private String applyInfo;

	private String applyInfoFuzzy;


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

	public void setApplyUserIdFuzzy(String applyUserIdFuzzy) {
		this.applyUserIdFuzzy = applyUserIdFuzzy;
	}

	public String getApplyUserIdFuzzy() {
		return applyUserIdFuzzy;
	}

	public void setReceiveUserIdFuzzy(String receiveUserIdFuzzy) {
		this.receiveUserIdFuzzy = receiveUserIdFuzzy;
	}

	public String getReceiveUserIdFuzzy() {
		return receiveUserIdFuzzy;
	}

	public void setContactIdFuzzy(String contactIdFuzzy) {
		this.contactIdFuzzy = contactIdFuzzy;
	}

	public String getContactIdFuzzy() {
		return contactIdFuzzy;
	}

	public void setApplyInfoFuzzy(String applyInfoFuzzy) {
		this.applyInfoFuzzy = applyInfoFuzzy;
	}

	public String getApplyInfoFuzzy() {
		return applyInfoFuzzy;
	}
}