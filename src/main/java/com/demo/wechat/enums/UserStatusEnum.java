package com.demo.wechat.enums;

/**
 * @Author ZXX
 * @Date 2024/11/18 17:08
 */
public enum UserStatusEnum {
    DISABLE(0,"禁用"),
    ENABLE(1,"启用");
    private Integer status;
    private String desc;
    UserStatusEnum(Integer status,String desc){
        this.status=status;
        this.desc=desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
    public static UserStatusEnum getByStatus(Integer status){
        for(UserStatusEnum use:UserStatusEnum.values()){
            if(use.getStatus().equals(status)){
                return use;
            }
        }
        return null;
    }
}
