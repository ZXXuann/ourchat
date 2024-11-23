package com.demo.wechat.aspect;

import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.enums.ResponseCodeEnum;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.redis.RedisUtils;
import com.demo.wechat.utils.StringTools;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static com.mysql.cj.conf.PropertyKey.logger;

/**
 * @Author ZXX
 * @Date 2024/11/22 21:29
 */
@Aspect
@Component("globalOperationAspect")
public class GlobalOperationAspect {
    @Autowired
    private RedisUtils redisUtils;
    private static final Logger logger= LoggerFactory.getLogger(GlobalOperationAspect.class);
    @Before("@annotation(com.demo.wechat.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint point){
        try {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if (interceptor == null) {
                return;
            }
            if (interceptor.checkAdmin() || interceptor.checkLogin()) {
                checkLogin(interceptor.checkAdmin());
            }
        }catch (BusinessException e){
            logger.error("全局拦截异常",e);
            throw e;
        }catch (Exception e){
            logger.error("全局拦截异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }catch (Throwable e){
            logger.error("全局拦截异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }

    private void checkLogin(Boolean checkAdmin){
        ServletRequestAttributes attrs=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request= attrs.getRequest();
        String token=request.getHeader("token");
        TokenUserInfoDto tokenUserInfoDto=(TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN+token);
        if(StringTools.isEmpty(token)){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        if(tokenUserInfoDto==null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }

        if(checkAdmin&&!tokenUserInfoDto.getAdmin()){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
    }

}
