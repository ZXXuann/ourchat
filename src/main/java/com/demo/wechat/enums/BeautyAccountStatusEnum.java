package com.demo.wechat.enums;

/**
 * @Author ZXX
 * @Date 2024/11/18 16:58
 */
public enum BeautyAccountStatusEnum {
    NO_USE(0,"未使用"),
    USED(1,"已使用");
    private Integer status;
    private String desc;
    BeautyAccountStatusEnum(Integer status,String desc){
        this.status=status;
        this.desc=desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static BeautyAccountStatusEnum getByStatus(Integer status){
        for(BeautyAccountStatusEnum base:BeautyAccountStatusEnum.values()){
            if(base.getStatus().equals(status)){
                return base;
            }
        }
        return null;
    }
}
