package com.demo.wechat;

import com.demo.wechat.redis.RedisUtils;
import com.demo.wechat.websocket.netty.NettyWebSocketStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author ZXX
 * @Date 2024/12/23 10:11
 */
@Slf4j
@Component("initRun")
public class InitRun implements ApplicationRunner {
    //ApplicationRunner这个是Springboot的接口，用于在应用启动后执行特定操作
    //CommandLineRunner也是用于在应用启动后执行特定操作
    //CommandLineRunner 参数类型是 String[] args,获取选项参数（--开头）以及获取非选项参数均需要手动解析
    //例如 --mode debug files1 files 2
    //CLR获取的就是 --mode debug 、files1、files2
    //而AR获取的是 mode:debug files1 files2
    //System.out.println("Mode: " + args.getOptionValues("mode").get(0));
    //CommandLineRunner类似
    @Resource
    private DataSource dataSource;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private NettyWebSocketStarter nettyWebSocketStarter;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        try{
            dataSource.getConnection();
            redisUtils.get("test");
//            nettyWebSocketStarter.startNetty();
            new Thread(nettyWebSocketStarter).start();
            log.info("服务启动成功");
        }catch (SQLException e){
            log.error("数据配置出错，请检查数据库配置");
        }catch (RedisConnectionFailureException e){
            log.error("redis配置错误，请检查redis配置");
        }catch (Exception e){
            log.error("服务启动失败",e);
        }
    }
}
