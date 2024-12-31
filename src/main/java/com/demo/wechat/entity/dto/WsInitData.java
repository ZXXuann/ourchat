package com.demo.wechat.entity.dto;

import com.demo.wechat.entity.po.ChatMessage;
import com.demo.wechat.entity.po.ChatSession;
import com.demo.wechat.entity.po.ChatSessionUser;

import java.util.List;

/**
 * @Author ZXX
 * @Date 2024/12/25 12:11
 */
public class WsInitData {
    private List<ChatSessionUser>  chatSessionUserList;
    private List<ChatMessage> chatMessageList;
    private Integer applyCount;

    public List<ChatSessionUser> getChatSessionUserList() {
        return chatSessionUserList;
    }

    public void setChatSessionUserList(List<ChatSessionUser> chatSessionUserList) {
        this.chatSessionUserList = chatSessionUserList;
    }

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    public Integer getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }

    public WsInitData(List<ChatSessionUser> chatSessionUserList, List<ChatMessage> chatMessageList, Integer applyCount) {
        this.chatSessionUserList = chatSessionUserList;
        this.chatMessageList = chatMessageList;
        this.applyCount = applyCount;
    }

    public WsInitData() {
    }
}
