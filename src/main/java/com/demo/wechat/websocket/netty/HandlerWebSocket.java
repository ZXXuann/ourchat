package com.demo.wechat.websocket.netty;

import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.utils.StringTools;
import com.demo.wechat.websocket.ChannelContextUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author ZXX
 * @Date 2024/12/22 20:02
 */
//用于处理WebSocket协议中文本帧（TextWebSocketFrame），这个类主要用来处理WebSocket的连接建立、断开以及接收消息的事件.
    //TextWebSocketFrame是WebSocket中的文本数据帧，用于传输字符串消息
@Slf4j
@Component
@ChannelHandler.Sharable
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //这个方法会在通道激活时触发，通常表示一个新的连接以及建立
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private ChannelContextUtils channelContextUtils;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有新的连接加入...");
    }
    //这个方法会在通道关闭或者断开时触发，通常表示一个客户端断开连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("有连接断开...");
        channelContextUtils.removeContext(ctx.channel());
    }
    //这个方法用于处理接受到的入站信息，其中TextWebSocketFrame代表WebSocket的文本信息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //ctx用于获取当前通道的上下文，并可以通过它访问当前通道的信息或执行I/O操作。
        //textWebSocketFrame这是接受到的WebSocket文本帧，包含一个text()方法，可以获取帧中的文本消息内容。
        Channel channel= ctx.channel();
        //通过channel.id()作为Attributekey传入进去，获取到一个Attribute
        Attribute<String> attribute=channel.attr(AttributeKey.valueOf(channel.id().toString()));
        //获取userid
        String userId=attribute.get();
        log.info("收到userId{}消息{}",userId,textWebSocketFrame.text());
        redisComponent.saveHeartBeat(userId);
//        channelContextUtils.send2Group(textWebSocketFrame.text());
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,Object evt) throws Exception{
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            //HandshakeComplete事件表示WebSocket握手完成
            WebSocketServerProtocolHandler.HandshakeComplete complete=(WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url=complete.requestUri();
            log.info("url:{}",url);
            String token=getToken(url);
            if(token==null){
                ctx.channel().close();
                return;
            }
            TokenUserInfoDto tokenUserInfoDto=redisComponent.getTokenUserInfoDto(token);
            if(null==tokenUserInfoDto){
                ctx.channel().close();
                return;
            }
            //如果token验证通过，则将当前连接的 Channel 与 tokenUserInfoDto 中的用户 ID 绑定
            channelContextUtils.addContext(tokenUserInfoDto.getUserId(), ctx.channel());
        }
    }
    private String getToken(String url){
        if(StringTools.isEmpty(url)||url.indexOf("?")==-1){
            return null;
        }
        String[] queryParams=url.split("\\?");
        if(queryParams.length<2){
            return null;
        }
        String [] params=queryParams[1].split("=");
        if(params.length<2){
            return null;
        }
        return params[1];
    }
}
