package com.demo.wechat.enums;

/**
 * @Author ZXX
 * @Date 2024/12/26 11:51
 */
public enum MessageStatusEnum {
    SENDING(0,"发送中"),
        SENDED(1,"已发送");
    private Integer status;
    private String desc;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    MessageStatusEnum(Integer status, String desc){
        this.status=status;
        this.desc=desc;
    }
    public static MessageStatusEnum getByStatus(Integer status){
        for(MessageStatusEnum item:MessageStatusEnum.values()){
            if(item.getStatus().equals(status)){
                return item;
            }
        }
        return null;
    }
}
