package com.demo.wechat.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.enums.UserContactTypeEnum;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Author ZXX
 * @Date 2024/11/18 14:09
 */

public class StringTools {
    public static String getUserId(){
        return UserContactTypeEnum.USER.getPrefix() + getRandomNumber(Constants.LENGTH_11);
    }
    public static String getGroupId(){
        return UserContactTypeEnum.GROUP.getPrefix()+getRandomNumber(Constants.LENGTH_11);
    }
    public static String getRandomNumber(Integer count){
        return RandomUtil.randomNumbers(count);
    }
    public static String getRandomString(Integer count){
        return RandomUtil.randomString(count);
    }
    public static boolean isEmpty(String content){
        return StrUtil.hasBlank(content);
    }
    public static String encodeMd5(String msg){
        MD5 md5=MD5.create();
        String md5Hex =md5.digestHex(msg);
        return md5Hex;
    }
    public static String cleanHtmlTag(String content){
        if(isEmpty(content)){
            return content;
        }
        content=content.replace("<","&lt;");
        content=content.replace("\r\n","<br>");
        content=content.replace("\n","<br>");
        return content;
    }
    public static final String getChatSessionId4User(String[] userIds){
        Arrays.sort(userIds);
        return encodeMd5(StringUtils.joinWithSerialComma(Arrays.stream(userIds).toList()));
    }
    public static final String getChatSessionId4Group(String groupId){
        return encodeMd5(groupId);
    }
    public static String getFileSuffix(String filename){
        return filename.substring(filename.lastIndexOf("."));
    }
    public static boolean isNumber(String str){
        String checkNumber="^[0-9]+$";
        if(null==str){
            return false;
        }
        if(!str.matches(checkNumber)){
            return false;
        }
        return true;
    }
}
