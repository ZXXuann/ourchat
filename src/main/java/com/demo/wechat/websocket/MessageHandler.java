package com.demo.wechat.websocket;

import cn.hutool.json.JSONUtil;
import com.demo.wechat.entity.dto.MessageSendDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Author ZXX
 * @Date 2024/12/26 16:24
 */
@Slf4j
@Component("messageHandler")
public class MessageHandler {
    private static final String MESSAGE_TOPIC="message.topic";
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ChannelContextUtils channelContextUtils;
    @PostConstruct
    public void lisMessage(){
        RTopic rTopic=redissonClient.getTopic(MESSAGE_TOPIC);
        rTopic.addListener(MessageSendDto.class,(MessageSendDto,sendDto)->{
            log.info("收到广播消息：{}",JSONUtil.toJsonStr(sendDto));
            channelContextUtils.sendMessage(sendDto);
        });
    }
    public void sendMessage(MessageSendDto sendDto){
        RTopic rTopic=redissonClient.getTopic(MESSAGE_TOPIC);
        rTopic.publish(sendDto);
    }
}
