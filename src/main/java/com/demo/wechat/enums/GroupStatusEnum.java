package com.demo.wechat.enums;

/**
 * @Author ZXX
 * @Date 2024/11/23 19:08
 */
public enum GroupStatusEnum {
    NORMAL(1,"正常"),
    DISSOLUTION(0,"解散");
    private Integer status;
    private String desc;
    private GroupStatusEnum(Integer status,String desc){
        this.status=status;
        this.desc=desc;
    }

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

    public static GroupStatusEnum getByStatus(Integer status){
        for(GroupStatusEnum temp:GroupStatusEnum.values()){
            if(temp.getStatus().equals(status)){
                return temp;
            }
        }
        return null;
    }
}
