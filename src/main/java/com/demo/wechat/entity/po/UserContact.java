package com.demo.wechat.entity.po;

import java.io.Serializable;

import com.demo.wechat.enums.DateTimePatternEnum;
import com.demo.wechat.utils.DateUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @Description: 
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
public class UserContact implements Serializable {
	/**
 	 * 用户ID
 	 */
	private String userId;

	/**
 	 * 联系人ID或者群组ID
 	 */
	private String contactId;

	/**
 	 * 联系人类型 0好友 1群组
 	 */
	private Integer contactType;

	/**
 	 * 创建时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
 	 * 状态 0非好友 1好友 2已删除 3被好友删除 4已拉黑好友 5被好友拉黑
 	 */
	@JsonIgnore
	private Integer status;

	/**
 	 * 最后更新时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;


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
	@Override
	public String toString() {
		return "用户ID:" + (userId == null ? "空" : userId) + "," + 
				"联系人ID或者群组ID:" + (contactId == null ? "空" : contactId) + "," + 
				"联系人类型 0好友 1群组:" + (contactType == null ? "空" : contactType) + "," + 
				"创建时间:" + (createTime == null ? "空" : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "," + 
				"状态 0非好友 1好友 2已删除 3被好友删除 4已拉黑好友 5被好友拉黑:" + (status == null ? "空" : status) + "," + 
				"最后更新时间:" + (lastUpdateTime == null ? "空" : DateUtils.format(lastUpdateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
		}
}