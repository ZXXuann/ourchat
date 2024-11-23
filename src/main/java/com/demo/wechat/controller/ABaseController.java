package com.demo.wechat.controller;

import cn.hutool.http.server.HttpServerRequest;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.vo.ResponseVO;;

import com.demo.wechat.enums.ResponseCodeEnum;
import com.demo.wechat.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;;import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 信息返回状态
 * @Author: KunSpireUp
 * @Date: 3/27/2024 12:24 AM
 */
public class ABaseController {

	protected static final String STATUS_SUCCESS = "success";

	protected static final String STATUS_ERROR = "error";
	@Autowired
	private RedisUtils redisUtils;

	protected <T> ResponseVO getSuccessResponseVO(T t) {
		ResponseVO<T> responseVO = new ResponseVO<>();
		responseVO.setStatus(STATUS_SUCCESS);
		responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
		responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
		responseVO.setData(t);
		return responseVO;
	}
	protected TokenUserInfoDto getTokenUserInfo(HttpServletRequest request){
		String token=request.getHeader("token");
		TokenUserInfoDto tokenUserInfoDto=(TokenUserInfoDto)redisUtils.get(Constants.REDIS_KEY_WS_TOKEN+token);
		return tokenUserInfoDto;
	}
}
