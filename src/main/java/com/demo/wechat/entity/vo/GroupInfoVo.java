package com.demo.wechat.entity.vo;

import com.demo.wechat.entity.po.GroupInfo;
import com.demo.wechat.entity.po.UserContact;

import java.util.List;

/**
 * @Author ZXX
 * @Date 2024/11/23 16:59
 */
public class GroupInfoVo {
    private GroupInfo groupInfo;
    private List<UserContact> userContactList;

    public GroupInfoVo() {
    }

    public GroupInfoVo(GroupInfo groupInfo, List<UserContact> userContactList) {
        this.groupInfo = groupInfo;
        this.userContactList = userContactList;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public List<UserContact> getUserContactList() {
        return userContactList;
    }

    public void setUserContactList(List<UserContact> userContactList) {
        this.userContactList = userContactList;
    }
}
