package com.demo.wechat.controller;


import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.Info;
import com.demo.wechat.entity.query.InfoQuery;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.vo.UserInfoVO;
import com.demo.wechat.service.InfoService;
import com.demo.wechat.utils.CopyTools;
import com.demo.wechat.utils.StringTools;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.List;

/**
 * @Description:  Controller
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
@RestController
@RequestMapping("/info")
public class AdminInfoController extends ABaseController{

	@Resource
	private InfoService infoService;
	@RequestMapping("/loadUser")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO loadUser(InfoQuery userInfoQuery){
		userInfoQuery.setOrderBy("create_time desc");
		PaginationResultVO resultVO=infoService.findListByPage(userInfoQuery);
		return getSuccessResponseVO(resultVO);
	}
	@RequestMapping("/updateUserStatus")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO updateUserStatus(@NotNull Integer status,@NotEmpty String userId){
		infoService.updateUserStatus(status,userId);
		return getSuccessResponseVO(null);
	}
	@RequestMapping("/forceOffLine")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO forceOffLine(@NotNull String userId){
		infoService.forceOffLine(userId);
		return getSuccessResponseVO(null);
	}
}