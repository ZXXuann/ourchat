package com.demo.wechat.enums;

import com.demo.wechat.utils.StringTools;

/**
 * @Author ZXX
 * @Date 2024/11/19 20:39
 */
public enum SexEnum {
    MALE("男",0),
    FEMALE("女",1);
    private String description;
    private Integer sex;
    private SexEnum(String description,Integer sex){
        this.description=description;
        this.sex=sex;
    }
    public SexEnum getByName(String name){
        try{
            if(StringTools.isEmpty(name)){
                return null;
            }
            //获取名称对应的枚举对象
            return SexEnum.valueOf(name.toUpperCase());
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
