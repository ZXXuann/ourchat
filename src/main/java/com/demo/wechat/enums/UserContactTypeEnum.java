package com.demo.wechat.enums;

import com.demo.wechat.utils.StringTools;
import com.mysql.cj.util.StringUtils;

/**
 * @Author ZXX
 * @Date 2024/11/18 14:21
 */

public enum UserContactTypeEnum {
    USER(0,"U","好友"),
    GROUP(1,"G","群");
    private Integer type;
    private String prefix;
    private String desc;
    UserContactTypeEnum(Integer type,String prefix,String desc){
        this.type=type;
        this.prefix=prefix;
        this.desc=desc;
    }

    public Integer getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDesc() {
        return desc;
    }
    public static UserContactTypeEnum getByName(String name){

        try{
            if(StringTools.isEmpty(name)){
                return null;
            }
            //获取名称对应的枚举对象
            return UserContactTypeEnum.valueOf(name.toUpperCase());
        }catch (Exception e){
            return null;
        }
    }
    public static UserContactTypeEnum getByPrefix(String prefix){
        try{
            if(StringTools.isEmpty(prefix)){
                return null;
            }
            prefix=prefix.substring(0,1);
            //遍历所有的枚举对象，查看哪个符合当前枚举，返回出去
            for(UserContactTypeEnum typeEnum:UserContactTypeEnum.values()){
                if(typeEnum.getPrefix().equals(prefix)){
                    return typeEnum;
                }
            }
            return null;
        }catch(Exception e){
            return null;
        }
    }
}
