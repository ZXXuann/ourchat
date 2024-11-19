package com.demo.wechat.redis;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author ZXX
 * @Date 2024/11/17 21:35
 */
@Component("redisUtils")
public class RedisUtils<T> {
    @Autowired
    private RedisTemplate<String,T> redisTemplate;
    private static  final Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    public void delete(String... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }
    public T get(String key){
        return key==null?null:redisTemplate.opsForValue().get(key);
    }
    public boolean set(String key, T value){
        try{
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            logger.error("设置redisKey:{},value:{}失败",key,value);
            return false;
        }
    }
    public boolean renew(String key,long newTime){
        try {
            if(newTime >0){
                //检查键是否存在
                if(redisTemplate.hasKey(key)){
                    redisTemplate.expire(key,newTime,TimeUnit.SECONDS);
                    return true;
                }else{
                    logger.error("续期失败，redisKey::{}不存在",key);
                    return false;
                }
            }else{
                logger.error("续期失败，redisKey::{}不存在",key);
                return false;
            }
        }catch (Exception e){
            logger.error("续期失败，redisKey::{}不存在",key);
            return false;
        }

    }
    public boolean setex(String key,T value,long time){
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        }catch (Exception e){
            logger.error("设置redisKey::{},value:{}失败",key,value);
            return false;
        }
    }

    public boolean expire(String key,long time){
        try{
            if(time>0){
                redisTemplate.expire(key,time,TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            logger.error("设置超时时间失败:{}",key);
            return false;
        }
    }
    public List<T> getQueueList(String key){
        return  redisTemplate.opsForList().range(key,0,-1);
    }
    public boolean lpush(String key,T value,long time){
        try{
            redisTemplate.opsForList().leftPush(key,value);
            return true;
        }catch (Exception e){
            logger.error("设置超时时间失败:{}",key);
            return false;
        }
    }
    public long remove(String key,Object value){
        try{
            Long remove=redisTemplate.opsForList().remove(key,1,value);
            return remove;
        }catch (Exception e){
            logger.error("删除失败:{}",key);
            return 0;
        }
    }
    public boolean lpushAll(String key,List<T> values,long time){
        try {
            redisTemplate.opsForList().leftPushAll(key,values);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            logger.error("添加失败:{}",key);
            return false;
        }
    }
}
