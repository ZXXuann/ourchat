package com.demo.wechat.entity.po;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @Description: 
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
public class InfoBeauty implements Serializable {
	/**
 	 * id
 	 */
	private Integer id;

	/**
 	 * 邮箱
 	 */
	private String email;

	/**
 	 * 用户id
 	 */
	private String userId;

	/**
 	 * 0未使用 1已使用
 	 */
	@JsonIgnore
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
	@Override
	public String toString() {
		return "id:" + (id == null ? "空" : id) + "," + 
				"邮箱:" + (email == null ? "空" : email) + "," + 
				"用户id:" + (userId == null ? "空" : userId) + "," + 
				"0未使用 1已使用:" + (status == null ? "空" : status);
		}
}