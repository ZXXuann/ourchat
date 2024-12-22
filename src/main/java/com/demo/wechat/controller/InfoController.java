package com.demo.wechat.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cn.hutool.system.UserInfo;
import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.vo.UserInfoVO;
import com.demo.wechat.service.InfoService;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.po.Info;
import com.demo.wechat.entity.query.InfoQuery;
import com.demo.wechat.utils.CopyTools;
import com.demo.wechat.utils.StringTools;
import org.apache.el.parser.Token;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @Description:  Controller
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
@RestController
@RequestMapping("/info")
public class InfoController extends ABaseController{

	@Resource
	private InfoService infoService;
	@RequestMapping("/logout")
	@GlobalInterceptor
	public ResponseVO logout(HttpServletRequest request){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		//TODO 退出登录 关闭WS连接
		return getSuccessResponseVO(null);
	}
	@RequestMapping("/updatePassword")
	@GlobalInterceptor
	public ResponseVO updatePassword(HttpServletRequest request,
									 @NotEmpty @Pattern(regexp = Constants.REGEX_PASSWORD) String password) throws IOException{
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		Info userInfo=new Info();
		userInfo.setPassword(StringTools.encodeMd5(password));
		this.infoService.updateInfoByUserId(userInfo,tokenUserInfoDto.getUserId());
		//TODO 强制退出，重新登录
		return getSuccessResponseVO(null);
	}
	@RequestMapping("/saveUserInfo")
	@GlobalInterceptor
	public ResponseVO saveUserInfo(HttpServletRequest request,
								   Info userInfo,
								   MultipartFile avatarFile,
								   MultipartFile avatarCover) throws IOException{
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		userInfo.setUserId(tokenUserInfoDto.getUserId());
		userInfo.setPassword(null);
		userInfo.setStatus(null);
		userInfo.setCreateTime(null);
		userInfo.setLastLoginTime(null);
		this.infoService.updateUserInfo(userInfo,avatarFile,avatarCover);
		return getUserInfo(request);
	}
	@RequestMapping("/getUserInfo")
	@GlobalInterceptor
	public ResponseVO getUserInfo(HttpServletRequest request){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		Info userInfo=infoService.getInfoByUserId(tokenUserInfoDto.getUserId());
		UserInfoVO userInfoVO= CopyTools.copy(userInfo, UserInfoVO.class);
		userInfoVO.setAdmin(tokenUserInfoDto.getAdmin());
		return getSuccessResponseVO(null);
	}

	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(InfoQuery query) {
		return getSuccessResponseVO(infoService.findListByPage(query));
	}

	/**
 	 * 新增
 	 */
	@RequestMapping("/add")
	public ResponseVO add(Info bean) {
		Integer result = this.infoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增
 	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<Info> listBean) {
		this.infoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增或修改
 	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<Info> listBean) {
		this.infoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 根据 UserId 查询
 	 */
	@RequestMapping("/getInfoByUserId")
	public ResponseVO getInfoByUserId(String userId) {
		return getSuccessResponseVO(infoService.getInfoByUserId(userId));}

	/**
 	 * 根据 UserId 更新
 	 */
	@RequestMapping("/updateInfoByUserId")
	public ResponseVO updateInfoByUserId(Info bean, String userId) {
		this.infoService.updateInfoByUserId(bean, userId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 UserId 删除
 	 */
	@RequestMapping("/deleteInfoByUserId")
	public ResponseVO deleteInfoByUserId(String userId) {
		this.infoService.deleteInfoByUserId(userId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Email 查询
 	 */
	@RequestMapping("/getInfoByEmail")
	public ResponseVO getInfoByEmail(String email) {
		return getSuccessResponseVO(infoService.getInfoByEmail(email));}

	/**
 	 * 根据 Email 更新
 	 */
	@RequestMapping("/updateInfoByEmail")
	public ResponseVO updateInfoByEmail(Info bean, String email) {
		this.infoService.updateInfoByEmail(bean, email);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Email 删除
 	 */
	@RequestMapping("/deleteInfoByEmail")
	public ResponseVO deleteInfoByEmail(String email) {
		this.infoService.deleteInfoByEmail(email);
		return getSuccessResponseVO(null);
}
}