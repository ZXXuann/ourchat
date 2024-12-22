package com.demo.wechat.websocket.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author ZXX
 * @Date 2024/12/22 19:48
 */
@Slf4j
public class HandlerHeartBeat extends ChannelDuplexHandler {
    //userEventTriggered方法是Netty中处理用户自定义事件的方法。
    //IdleStateEvent是用来通知连接在一定时间内没有读写操作的事件，通常用于心跳检测或者空闲超时检测
    //其具有如下三种状态：
    //READER_IDLE:读取空闲状态，表示在一定时间内没有接收到任何数据
    //WRITER_IDLE:写空闲状态，表示在一定时间内没有发送任何数据
    //ALL_IDLE:所有空闲状态，表示在一定时间内没有进行任何读取和写入操作
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent e=(IdleStateEvent) evt;
            if(e.state()==IdleState.READER_IDLE){
                log.info("心跳超时");
                ctx.close();
            }else if(e.state()==IdleState.WRITER_IDLE){
                //如果链接在一段时间没有发送数据给客户端，那么向
                ctx.writeAndFlush("heart");
            }
        }
    }
}
