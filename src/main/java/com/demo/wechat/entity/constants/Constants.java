package com.demo.wechat.entity.constants;

import com.demo.wechat.enums.UserContactTypeEnum;

/**
 * @Author ZXX
 * @Date 2024/11/17 22:04
 */
public class Constants {
    public static String REDIS_KEY_CHECK_CODE="wechat:checkcode:";
    public static final Integer REDIS_TIME_1MIN=60;
    public static final Integer LENGTH_11=11;
    public static final Integer LENGTH_20=20;
    public static final String REDIS_KEY_WS_USER_HEART_BEAT="wechat:ws:user:heartbeat:";
    public static final String REDIS_KEY_WS_TOKEN="wechat:ws:token:";
    public static final Integer REDIS_KEY_EXPIRES_DAY=REDIS_TIME_1MIN*60*24;
    public static final String REDIS_KEY_WS_TOKEN_USERID="wechat:ws:token:userid:";
    public static final String ROBOT_UID= UserContactTypeEnum.USER.getPrefix()+"robot";
    public static final String REDIS_KEY_SYS_SETTING="wechat:syssetting:";
    public static final String FILE_FOLDER_FILE="/file";
    public static final String FILE_FOLDER_AVATAR_NAME="/avatar";
    public static final String APPLY_INFO_TEMPLATE="您好";
    public static final String IMAGE_SUFFIX="/head_image";
    public static final String COVER_IMAGE_SUFFIX="/cover_image";
    public static final String REGEX_PASSWORD="^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$$^&*_]{8,18}$";
    public static final Integer REDIS_KEY_EXPIRES_HEART_BEAT=6;
    //用户联系人列表
    public static final String REDIS_KEY_USER_CONTACT="wechat:ws:user:contact";
    public static final Long MILLISSECONDS_3DAYS_AGO=3*24*60*60*1000L;
    public static final String[] IMAGE_SUFFIX_LIST=new String[]{".jpeg",".jpg",".png",".gif",".bmp",".webp"};
    public static final String[] VIDEO_SUFFIX_LIST=new String[]{".mp4",".avi",".rmvb",".mkv",".mov"};
    public static final Long FILE_SIZE_MB=1024*1024L;
    public static final Integer ZERO=0;
    public static final Integer ONE=1;
}
