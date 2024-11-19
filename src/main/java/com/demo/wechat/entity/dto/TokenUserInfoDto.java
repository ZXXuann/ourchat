package com.demo.wechat.entity.dto;

import java.io.Serializable;

/**
 * @Author ZXX
 * @Date 2024/11/18 17:24
 */
public class TokenUserInfoDto implements Serializable {
   private String token;
   private String userId;
   private String nickName;
   private Boolean admin;

    @Override
    public String toString() {
        return "TokenUserInfoDto{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", admin=" + admin +
                '}';
    }

    public TokenUserInfoDto(){}
    public TokenUserInfoDto(String token, String userId, String nickName, Boolean admin) {
        this.token = token;
        this.userId = userId;
        this.nickName = nickName;
        this.admin = admin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
