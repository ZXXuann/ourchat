package com.demo.wechat.entity.query;



import java.util.Date;


/**
 * @Description: 
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
public class UserContactQuery extends BaseQuery {
	/**
 	 * 用户ID 查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 * 联系人ID或者群组ID 查询对象
 	 */
	private String contactId;

	private String contactIdFuzzy;

	/**
 	 * 联系人类型 0好友 1群组 查询对象
 	 */
	private Integer contactType;
	/**
 	 * 创建时间 查询对象
 	 */
	private Date createTime;

	private String createTimeStart;
	private String createTimeEnd;
	private Boolean queryUserInfo;
	private Boolean queryGroupInfo;
	private Boolean queryContactUserInfo;
	private Boolean excludeMyGroup;
	private Integer[] statusArray;

	public Integer[] getStatusArray() {
		return statusArray;
	}

	public void setStatusArray(Integer[] statusArray) {
		this.statusArray = statusArray;
	}

	public Boolean getExcludeMyGroup() {
		return excludeMyGroup;
	}

	public void setExcludeMyGroup(Boolean excludeMyGroup) {
		this.excludeMyGroup = excludeMyGroup;
	}

	public Boolean getQueryGroupInfo() {
		return queryGroupInfo;
	}

	public void setQueryGroupInfo(Boolean queryGroupInfo) {
		this.queryGroupInfo = queryGroupInfo;
	}

	public Boolean getQueryContactUserInfo() {
		return queryContactUserInfo;
	}

	public void setQueryContactUserInfo(Boolean queryContactUserInfo) {
		this.queryContactUserInfo = queryContactUserInfo;
	}

	public Boolean getQueryUserInfo() {
		return queryUserInfo;
	}

	public void setQueryUserInfo(Boolean queryUserInfo) {
		this.queryUserInfo = queryUserInfo;
	}

	/**
 	 * 状态 0非好友 1好友 2已删除 3被好友删除 4已拉黑好友 5被好友拉黑 查询对象
 	 */
	private Integer status;

	/**
 	 * 最后更新时间 查询对象
 	 */
	private Date lastUpdateTime;

	private String lastUpdateTimeStart;
	private String lastUpdateTimeEnd;

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

	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}

	public Integer getContactType() {
		return contactType;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setContactIdFuzzy(String contactIdFuzzy) {
		this.contactIdFuzzy = contactIdFuzzy;
	}

	public String getContactIdFuzzy() {
		return contactIdFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setLastUpdateTimeStart(String lastUpdateTimeStart) {
		this.lastUpdateTimeStart = lastUpdateTimeStart;
	}

	public String getLastUpdateTimeStart() {
		return lastUpdateTimeStart;
	}

	public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) {
		this.lastUpdateTimeEnd = lastUpdateTimeEnd;
	}

	public String getLastUpdateTimeEnd() {
		return lastUpdateTimeEnd;
	}
}