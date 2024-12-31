package com.demo.wechat.enums;

import com.demo.wechat.utils.StringTools;

/**
 * @Author ZXX
 * @Date 2024/12/25 15:15
 */
public enum MessageTypeEnum {
    INIT(0,"","连接ws获取信息"),
    ADD_FRIEND(1,"","添加好友打招呼信息"),
    CHAT(2,"","普通聊天信息"),
    GROUP_CREATE(3,"群组已经创建好，可以和好友一起畅聊了","群创建成功"),
    CONTACT_APPLY(4,"","好友申请"),
    MEDIA_CHAT(5,"","媒体文件"),
    FILE_UPLOAD(6,"","文件上传完成"),
    FORCE_OFF_LINE(7,"","强制下线"),
    DISSOLUTION_GROUP(8,"群聊已解散","解散群聊"),
    ADD_GROUP(9,"%s加入了群组","加入群聊"),
    CONTACT_NAME_UPDATE(10,"","更新昵称"),
    LEAVE_GROUP(11,"%s退出了群聊","退出群聊"),
    REMOVE_GROUP(12,"%s被管理员移出了群聊","被管理员移出了群聊"),
    ADD_FRIEND_SELF(13,"","添加好友打招呼消息");
    private Integer type;
    private String initMessage;
    private String desc;

    MessageTypeEnum(Integer type, String initMessage, String desc) {
        this.type = type;
        this.initMessage = initMessage;
        this.desc = desc;
    }
    public static MessageTypeEnum getByName(String name){
        try {
            if (StringTools.isEmpty(name)) {
                return null;
            }
            return MessageTypeEnum.valueOf(name);
        }catch (Exception e){
            return null;
        }
    }
    public static MessageTypeEnum getByType(Integer type){
        for(MessageTypeEnum item:MessageTypeEnum.values()){
            if(item.getType().equals(type)){
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInitMessage() {
        return initMessage;
    }

    public void setInitMessage(String initMessage) {
        this.initMessage = initMessage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
