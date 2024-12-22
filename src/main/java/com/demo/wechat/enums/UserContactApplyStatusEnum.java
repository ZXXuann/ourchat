package com.demo.wechat.enums;

import com.demo.wechat.entity.query.UserContactQuery;
import com.demo.wechat.utils.StringTools;

/**
 * @Author ZXX
 * @Date 2024/11/23 20:17
 */
public enum UserContactApplyStatusEnum {
    INIT(0,"待处理"),
    PASS(1,"已同意"),
    REJECT(2,"已拒绝"),
    BLACKLIST(3,"已拉黑");
    private Integer status;
    private String desc;
    private UserContactApplyStatusEnum(Integer status,String desc){
        this.status=status;
        this.desc=desc;
    }
    public UserContactApplyStatusEnum getByName(String name){

        try {
            if (StringTools.isEmpty(name)) {
                return null;
            }
            return UserContactApplyStatusEnum.valueOf(name.toUpperCase());
        }catch (IllegalArgumentException e){
            return null;
        }

    }
    public static UserContactApplyStatusEnum getByStatus(Integer status){
        for(UserContactApplyStatusEnum temp:UserContactApplyStatusEnum.values()){
            if(temp.getStatus().equals(status)){
                return temp;
            }
        }
        return null;
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
}
