package com.demo.wechat.entity.query;

import java.util.Date;


/**
 * @Description: 
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
public class InfoQuery extends BaseQuery {
	/**
 	 * 用户ID 查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 * 昵称 查询对象
 	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
 	 * 用户邮箱 查询对象
 	 */
	private String email;

	private String emailFuzzy;

	/**
 	 * 性别 0：女 1：男 查询对象
 	 */
	private Integer sex;

	/**
 	 * 密码 查询对象
 	 */
	private String password;

	private String passwordFuzzy;

	/**
 	 * 个性签名 查询对象
 	 */
	private String personalSignature;

	private String personalSignatureFuzzy;

	/**
 	 * 状态 查询对象
 	 */
	private Integer status;

	/**
 	 * 添加方式:0直接加入 1别人加的 查询对象
 	 */
	private Integer joinType;

	/**
 	 * 地区 查询对象
 	 */
	private String areaName;

	private String areaNameFuzzy;

	/**
 	 * 地区编码 查询对象
 	 */
	private String areaCode;

	private String areaCodeFuzzy;

	/**
 	 * 创建时间 查询对象
 	 */
	private Date createTime;

	private String createTimeStart;
	private String createTimeEnd;
	/**
 	 * 上次登录时间 查询对象
 	 */
	private Date lastLoginTime;

	private String lastLoginTimeStart;
	private String lastLoginTimeEnd;
	/**
 	 * 最后离开时间 查询对象
 	 */
	private Date lastOffTime;

	private String lastOffTimeStart;
	private String lastOffTimeEnd;

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

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setNickNameFuzzy(String nickNameFuzzy) {
		this.nickNameFuzzy = nickNameFuzzy;
	}

	public String getNickNameFuzzy() {
		return nickNameFuzzy;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy() {
		return emailFuzzy;
	}

	public void setPasswordFuzzy(String passwordFuzzy) {
		this.passwordFuzzy = passwordFuzzy;
	}

	public String getPasswordFuzzy() {
		return passwordFuzzy;
	}

	public void setPersonalSignatureFuzzy(String personalSignatureFuzzy) {
		this.personalSignatureFuzzy = personalSignatureFuzzy;
	}

	public String getPersonalSignatureFuzzy() {
		return personalSignatureFuzzy;
	}

	public void setAreaNameFuzzy(String areaNameFuzzy) {
		this.areaNameFuzzy = areaNameFuzzy;
	}

	public String getAreaNameFuzzy() {
		return areaNameFuzzy;
	}

	public void setAreaCodeFuzzy(String areaCodeFuzzy) {
		this.areaCodeFuzzy = areaCodeFuzzy;
	}

	public String getAreaCodeFuzzy() {
		return areaCodeFuzzy;
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

	public void setLastLoginTimeStart(String lastLoginTimeStart) {
		this.lastLoginTimeStart = lastLoginTimeStart;
	}

	public String getLastLoginTimeStart() {
		return lastLoginTimeStart;
	}

	public void setLastLoginTimeEnd(String lastLoginTimeEnd) {
		this.lastLoginTimeEnd = lastLoginTimeEnd;
	}

	public String getLastLoginTimeEnd() {
		return lastLoginTimeEnd;
	}

	public void setLastOffTimeStart(String lastOffTimeStart) {
		this.lastOffTimeStart = lastOffTimeStart;
	}

	public String getLastOffTimeStart() {
		return lastOffTimeStart;
	}

	public void setLastOffTimeEnd(String lastOffTimeEnd) {
		this.lastOffTimeEnd = lastOffTimeEnd;
	}

	public String getLastOffTimeEnd() {
		return lastOffTimeEnd;
	}
}