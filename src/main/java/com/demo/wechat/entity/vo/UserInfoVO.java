package com.demo.wechat.entity.vo;

/**
 * @Author ZXX
 * @Date 2024/11/19 16:59
 */
public class UserInfoVO {
    private String userId;
    private String nickName;
    private Integer sex;
    private Integer joinType;
    private String personalSignature;
    private String areaCode;
    private String areaName;
    private String token;
    private Boolean admin;
    private Integer contactStatus;

    public UserInfoVO() {
    }

    public UserInfoVO(String userId, String nickName, Integer sex, Integer joinType, String personalSignature, String areaCode, String areaName, String token, Boolean admin, Integer contactStatus) {
        this.userId = userId;
        this.nickName = nickName;
        this.sex = sex;
        this.joinType = joinType;
        this.personalSignature = personalSignature;
        this.areaCode = areaCode;
        this.areaName = areaName;
        this.token = token;
        this.admin = admin;
        this.contactStatus = contactStatus;
    }

    @Override
    public String toString() {
        return "UserInfoVO{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", joinType=" + joinType +
                ", personalSignature='" + personalSignature + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", areaName='" + areaName + '\'' +
                ", token='" + token + '\'' +
                ", admin=" + admin +
                ", contactStatus=" + contactStatus +
                '}';
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Integer getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(Integer contactStatus) {
        this.contactStatus = contactStatus;
    }
}
