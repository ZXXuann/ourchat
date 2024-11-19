package com.demo.wechat.entity.query;



/**
 * @Description: 
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
public class InfoBeautyQuery extends BaseQuery {
	/**
 	 * id 查询对象
 	 */
	private Integer id;

	/**
 	 * 邮箱 查询对象
 	 */
	private String email;

	private String emailFuzzy;

	/**
 	 * 用户id 查询对象
 	 */
	private String userId;

	private String userIdFuzzy;

	/**
 	 * 0未使用 1已使用 查询对象
 	 */
	private Integer status;


	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy() {
		return emailFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}
}