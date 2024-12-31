package com.demo.wechat.websocket.netty;

import com.demo.wechat.entity.config.AppConfig;
import com.demo.wechat.utils.StringTools;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author ZXX
 * @Date 2024/12/22 19:36
 */
@Slf4j
@Component
public class NettyWebSocketStarter implements Runnable{

    private static EventLoopGroup bossGroup=new NioEventLoopGroup(1);
    private static EventLoopGroup workGroup=new NioEventLoopGroup();
    @Resource
    private HandlerWebSocket handlerWebSocket;
    @Resource
    private AppConfig appConfig;
    @PreDestroy
    public void close(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
//    @Async
    //Async的作用：异步执行任务，当你希望某个方法执行不阻塞当前线程时。
//    public void startNetty(){
//
//    }
//    public static void main(String[] args) {
//
//    }

    @Override
    public void run() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //对http协议的支持，使用http的编码器和解码器
                            ChannelPipeline pipeline=socketChannel.pipeline();
                            //HttpServerCodec是一个组合处理器 由两部分组成
                            //httpRequestDecoder:负责解码客户端发送的Http请求，将Http请求的字节流（如TCP流）解析成HttpRequest对象。
                            //httpResponseEncoder：负责编码服务端的Http响应，将httpResponse对象转换为字节流并发送给客户端
                            pipeline.addLast(new HttpServerCodec());
                            //聚合阶码 httpRequest/httpContent/lastHttpContent到fullHttpRequest
                            //保证接收的http请求的完整性
                            //HttpObjectAggregator是用于聚合HTTP请求或响应的处理，它会将多个HTTP消息段（请求头，请求体，请求体的最后一部分）
                            //合并成一个完整的FullHttpRequest或FullHttpResponse对象
                            //参数设置了聚合消息的最大大小为64KB，若请求太大，netty会抛出异常
                            //LastHttpContent 通常是一个空的 HttpContent，但是它标志着请求体的结束。
                            //如果客户端发送一个文件上传请求，传输过程中的最后一块数据会是 LastHttpContent，它表示文件传输完成。
                            pipeline.addLast(new HttpObjectAggregator(64*1024));
                            //心跳
                            //long readerIdleTime ,long writerIdleTime,long allIdleTime,TimeUnit unit
                            //readerIdleTime 读超时事件 未接收到客户端的信息
                            //writerIdleTime 写超时事件 未发送到客户端的消息
                            //allIdleTime 所有类型的超时事件
                            pipeline.addLast(new IdleStateHandler(60,0,0, TimeUnit.SECONDS));
                            pipeline.addLast(new HandlerHeartBeat());
                            //将http协议升级为ws协议，对websocket支持
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws",null,true,64*1012,true,true,10000L));

                            pipeline.addLast(handlerWebSocket);
                        }
                    });
            Integer wsPort=appConfig.getWsPort();
            String wsPortStr=System.getProperty("ws.port");
            if(!StringTools.isEmpty(wsPortStr)){
                wsPort=Integer.parseInt(wsPortStr);
            }
            ChannelFuture channelFuture = serverBootstrap.bind(wsPort).sync();
            log.info("netty启动成功");
            // channelFuture.channel().closeFuture().sync();是等待ServerChannel关闭，也就是等待连接的通道关闭
            channelFuture.channel().closeFuture().sync();

        }catch (InterruptedException e){
            log.error("netty启动失败",e);
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
