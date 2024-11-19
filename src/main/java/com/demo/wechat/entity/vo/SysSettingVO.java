package com.demo.wechat.entity.vo;

import com.demo.wechat.entity.constants.Constants;

import java.io.Serializable;

/**
 * @Author ZXX
 * @Date 2024/11/19 21:25
 */
public class SysSettingVO implements Serializable {
    //最大的群数组
    private Integer maxGroupCount=5;
    //群组最大人数
    private Integer maxGroupMemberCount=500;
    //图片大小
    private Integer maxImageSize=2;
    //视频大小
    private Integer maxVideoSize=5;
    //文件大小
    private Integer maxFileSize=5;
    //机器人ID
    private String robotUid= Constants.ROBOT_UID;
    //机器人昵称
    private String robotNickName="LittleWe";
    //欢迎语
    private String robotWelcome="欢迎使用WeChat";

    public SysSettingVO() {
    }

    public SysSettingVO(Integer maxGroupCount, Integer maxGroupMemberCount, Integer maxImageSize, Integer maxVideoSize, Integer maxFileSize, String robotUid, String robotNickName, String robotWelcome) {
        this.maxGroupCount = maxGroupCount;
        this.maxGroupMemberCount = maxGroupMemberCount;
        this.maxImageSize = maxImageSize;
        this.maxVideoSize = maxVideoSize;
        this.maxFileSize = maxFileSize;
        this.robotUid = robotUid;
        this.robotNickName = robotNickName;
        this.robotWelcome = robotWelcome;
    }

    public Integer getMaxGroupCount() {
        return maxGroupCount;
    }

    public void setMaxGroupCount(Integer maxGroupCount) {
        this.maxGroupCount = maxGroupCount;
    }

    public Integer getMaxGroupMemberCount() {
        return maxGroupMemberCount;
    }

    public void setMaxGroupMemberCount(Integer maxGroupMemberCount) {
        this.maxGroupMemberCount = maxGroupMemberCount;
    }

    public Integer getMaxImageSize() {
        return maxImageSize;
    }

    public void setMaxImageSize(Integer maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    public Integer getMaxVideoSize() {
        return maxVideoSize;
    }

    public void setMaxVideoSize(Integer maxVideoSize) {
        this.maxVideoSize = maxVideoSize;
    }

    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getRobotUid() {
        return robotUid;
    }

    public void setRobotUid(String robotUid) {
        this.robotUid = robotUid;
    }

    public String getRobotNickName() {
        return robotNickName;
    }

    public void setRobotNickName(String robotNickName) {
        this.robotNickName = robotNickName;
    }

    public String getRobotWelcome() {
        return robotWelcome;
    }

    public void setRobotWelcome(String robotWelcome) {
        this.robotWelcome = robotWelcome;
    }

    @Override
    public String toString() {
        return "SysSettingVO{" +
                "maxGroupCount=" + maxGroupCount +
                ", maxGroupMemberCount=" + maxGroupMemberCount +
                ", maxImageSize=" + maxImageSize +
                ", maxVideoSize=" + maxVideoSize +
                ", maxFileSize=" + maxFileSize +
                ", robotUid='" + robotUid + '\'' +
                ", robotNickName='" + robotNickName + '\'' +
                ", robotWelcome='" + robotWelcome + '\'' +
                '}';
    }
}
