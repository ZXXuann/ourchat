package com.demo.wechat.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.enums.UserContactTypeEnum;
import org.springframework.stereotype.Component;

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

}
