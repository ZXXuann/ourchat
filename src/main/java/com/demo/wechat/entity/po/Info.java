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
 * @Date: 2024/11/17 16:43:43
 */
public class Info implements Serializable {
	/**
 	 * 用户ID
 	 */
	private String userId;

	/**
 	 * 昵称
 	 */
	private String nickName;

	/**
 	 * 用户邮箱
 	 */
	private String email;

	/**
 	 * 性别 0：女 1：男
 	 */
	private Integer sex;

	/**
 	 * 密码
 	 */
	private String password;

	/**
 	 * 个性签名
 	 */
	private String personalSignature;

	/**
 	 * 状态
 	 */
	@JsonIgnore
	private Integer status;

	/**
 	 * 添加方式:0直接加入 1别人加的
 	 */
	private Integer joinType;

	/**
 	 * 地区
 	 */
	private String areaName;

	/**
 	 * 地区编码
 	 */
	private String areaCode;

	/**
 	 * 创建时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
 	 * 上次登录时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	/**
 	 * 最后离开时间
 	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastOffTime;


	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getSex() {
		return sex;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPersonalSignature(String personalSignature) {
		this.personalSignature = personalSignature;
	}

	public String getPersonalSignature() {
		return personalSignature;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}

	public Integer getJoinType() {
		return joinType;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastOffTime(Date lastOffTime) {
		this.lastOffTime = lastOffTime;
	}

	public Date getLastOffTime() {
		return lastOffTime;
	}
	@Override
	public String toString() {
		return "用户ID:" + (userId == null ? "空" : userId) + "," + 
				"昵称:" + (nickName == null ? "空" : nickName) + "," + 
				"用户邮箱:" + (email == null ? "空" : email) + "," + 
				"性别 0：女 1：男:" + (sex == null ? "空" : sex) + "," + 
				"密码:" + (password == null ? "空" : password) + "," + 
				"个性签名:" + (personalSignature == null ? "空" : personalSignature) + "," + 
				"状态:" + (status == null ? "空" : status) + "," + 
				"添加方式:0直接加入 1别人加的:" + (joinType == null ? "空" : joinType) + "," + 
				"地区:" + (areaName == null ? "空" : areaName) + "," + 
				"地区编码:" + (areaCode == null ? "空" : areaCode) + "," + 
				"创建时间:" + (createTime == null ? "空" : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "," + 
				"上次登录时间:" + (lastLoginTime == null ? "空" : DateUtils.format(lastLoginTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "," + 
				"最后离开时间:" + (lastOffTime == null ? "空" : DateUtils.format(lastOffTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
		}
}