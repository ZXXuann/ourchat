package com.demo.wechat.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Author ZXX
 * @Date 2024/11/17 21:17
 */
@Slf4j
@Configuration
public class RedisConfig<V> {
    @Value("${spring.redis.host:}")
    private String redisHost;
    @Value("${spring.redis.port:}")
    private Integer redisPort;
    @Bean(name="redissonClient",destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        try{
            Config config=new Config();
            config.useSingleServer().setAddress("redis://"+redisHost+":"+redisPort);
            RedissonClient redissonClient= Redisson.create(config);
            return redissonClient;
        }catch (Exception e){
            log.info("redis配置错误，请检查redis配置");
        }
        return null;
    }

    @Bean("redisTemplate")
    public RedisTemplate<String,V> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,V> template=new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //设置key的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        //设置value的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        //设置hash的key的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置hash的value的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        return template;
    }
}
