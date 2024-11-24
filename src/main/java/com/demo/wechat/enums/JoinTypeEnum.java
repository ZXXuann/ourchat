package com.demo.wechat.enums;

import com.demo.wechat.utils.StringTools;

/**
 * @Author ZXX
 * @Date 2024/11/19 20:30
 */
public enum JoinTypeEnum {
    APPLY("申请",0),
        AUTHORIZE("授权",1),
            DENY("拒绝",2),
                REJECT("驳回",3),
    JOIN("加入",4);
    private String description;
    private Integer type;
    private JoinTypeEnum(String description,Integer type){
        this.description=description;
        this.type=type;
    }
    public static JoinTypeEnum getByName(String name){
        try{
            if(StringTools.isEmpty(name)){
                return null;
            }
            //获取名称对应的枚举对象
            return JoinTypeEnum.valueOf(name.toUpperCase());
        }catch (Exception e){
            return null;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
