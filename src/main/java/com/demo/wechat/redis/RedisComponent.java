package com.demo.wechat.redis;

import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.SysSettingDto;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.vo.SysSettingVO;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author ZXX
 * @Date 2024/11/19 15:34
 */
@Component("redisComponent")
public class RedisComponent {
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 获取心跳
     * @param userId 用户的ID
     * @Return 返回获取的心跳记录
     */
    public Long getUserHeartBeat(String userId){
        return (Long) redisUtils.get(Constants.REDIS_KEY_WS_USER_HEART_BEAT+userId);
    }

    /**
     * 设置心跳
     * @param tokenUserInfoDto
     */
    public Boolean setUserHeartBeat(String userId){
        return redisUtils.setex(Constants.REDIS_KEY_WS_USER_HEART_BEAT+userId,userId,60);
    }
    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto){
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN+tokenUserInfoDto.getToken(),tokenUserInfoDto,Constants.REDIS_KEY_EXPIRES_DAY);
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN_USERID+tokenUserInfoDto.getUserId(),tokenUserInfoDto.getToken(),Constants.REDIS_KEY_EXPIRES_DAY);
    }

    /**
     * 获取超管配置
     * @return
     */
    public SysSettingDto getSysSetting(){
        SysSettingDto sysSettingDto= (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
//        sysSettingDto=sysSettingDto==null?new SysSettingDto():sysSettingDto;
        if(null==sysSettingDto){
            sysSettingDto= new SysSettingDto();
            redisUtils.setex(Constants.REDIS_KEY_SYS_SETTING,sysSettingDto,Constants.REDIS_TIME_1MIN*60*24*10);
            return sysSettingDto;
        }
        redisUtils.renew(Constants.REDIS_KEY_SYS_SETTING,Constants.REDIS_TIME_1MIN*60*24*10);
        return sysSettingDto;
    }

    /**
     * 设置超管配置
     */
    public void setSysSetting(SysSettingDto sysSettingDto){
        redisUtils.setex(Constants.REDIS_KEY_SYS_SETTING,sysSettingDto,Constants.REDIS_TIME_1MIN*60*24*10);
    }
}
