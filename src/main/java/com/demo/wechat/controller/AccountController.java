package com.demo.wechat.controller;

import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.SysSettingDto;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.Info;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.vo.SysSettingVO;
import com.demo.wechat.entity.vo.UserInfoVO;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.redis.RedisUtils;
import com.demo.wechat.service.InfoService;
import com.demo.wechat.utils.CopyTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author ZXX
 * @Date 2024/11/17 20:55
 */
@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{
    private static final Logger logger= LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private InfoService infoService;
    @Autowired
    private RedisComponent redisComponent;

    /**
     * 获取验证码
     *  生成验证码-》base64
     * 生成token
     * base64和token-》前端
     * @return
     */
    @RequestMapping("/checkCode")
    public ResponseVO checkCode(){
        //生成code
        ArithmeticCaptcha captcha=new ArithmeticCaptcha(100,43);
        //获取code的字符串
        String code=captcha.text();
        //返回唯一标识
        String checkCodeKey= UUID.randomUUID().toString();
        //存入redis
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey,code,Constants.REDIS_TIME_1MIN*2);
        //把验证码转为base64
        String checkCodeBase64=captcha.toBase64();
        //用来存结果
        Map<String,String> result=new HashMap<>();
        //将验证码存放在MAP当中，返回出去
        result.put("checkCode",checkCodeBase64);
        result.put("checkCodeKey",checkCodeKey);
        return getSuccessResponseVO(result);
    }

    /**
     * 注册，需要把验证码的token带回来
     * @param checkCodeKey
     * @param email
     * @param password
     * @param nickname
     * @param checkCode
     * @return
     */
    @RequestMapping("/register")
    public ResponseVO register(@NotEmpty String checkCodeKey,
                               @NotEmpty @Email String email,
                               @NotEmpty String password,
                               @NotEmpty String nickname,
                               @NotEmpty String checkCode){
        try{
            if(!checkCode.equalsIgnoreCase((String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey))){
                throw new BusinessException("图片验证码不正确");
            }
            infoService.register(email,nickname,password);
        }finally {
            redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey);
        }
        return getSuccessResponseVO(null);
    }

    /**
     * 登录，也需要把验证码token带回来，这样可以验证验证码的正确性
     * @param checkCodeKey
     * @param email
     * @param password
     * @param checkCode
     * @return
     */
    @RequestMapping("/login")
    public ResponseVO login(@NotEmpty String checkCodeKey,
                            @NotEmpty @Email String email,
                            @NotEmpty String password,
                            @NotEmpty String checkCode){
        try{
            if(!checkCode.equalsIgnoreCase((String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey))){
                throw new BusinessException("图片验证码不正确");
            }
            UserInfoVO userInfoVO= infoService.login(email,password);
            return getSuccessResponseVO(userInfoVO);
        }finally {
            redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey);
        }
    }

    /**
     * 获取系统设置
     * @return
     */
    @RequestMapping("/getSysSetting")
    public ResponseVO getSysSetting(){
        return getSuccessResponseVO(redisComponent.getSysSetting());
    }

    /**
     * 设置系统设置
     * @param sysSettingVO
     * @return
     */
    @GlobalInterceptor
    @RequestMapping("/setSysSetting")
    public ResponseVO setSysSetting(@NotNull SysSettingVO sysSettingVO){
        if(null==sysSettingVO){
            throw new BusinessException("系统设置传参失败，请查证后再提交");
        }
        SysSettingDto sysSettingDto=CopyTools.copy(sysSettingVO,SysSettingDto.class);
        redisComponent.setSysSetting(sysSettingDto);
        return getSuccessResponseVO(null);
    }
}
