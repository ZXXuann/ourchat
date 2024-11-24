package com.demo.wechat.enums;

/**
 * @Author ZXX
 * @Date 2024/11/23 13:47
 */
public enum UserContactStatusEnum {
    NOT_FRIEND(0,"非好友"),
    FRIEND(1,"好友"),
    DEL(2,"已删除好友"),
    DEL_BE(3,"被好友删除"),
    BLACKLIST(4,"已拉黑好友"),
    BLACKLIST_BE(5,"被好友拉黑");
    private Integer status;
    private String desc;
    private UserContactStatusEnum(Integer status,String desc){
        this.status=status;
        this.desc=desc;
    }

    @Override
    public String toString() {
        return "UserContactStatusEnum{" +
                "status=" + status +
                ", desc='" + desc + '\'' +
                '}';
    }
    public static UserContactStatusEnum getByStatus(Integer status){
        for(UserContactStatusEnum temp:UserContactStatusEnum.values()){
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
