package com.demo.wechat.controller;


import java.util.List;

import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.dto.UserContactSearchResultDto;
import com.demo.wechat.service.InfoService;
import com.demo.wechat.service.UserContactApplyService;
import com.demo.wechat.service.UserContactService;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.UserContactQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @Description:  Controller
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
@RestController
@RequestMapping("/userContact")
public class UserContactController extends ABaseController{

	@Resource
	private UserContactService userContactService;
	@Resource
	private InfoService userInfoService;
	@Resource
	private UserContactApplyService userContactApplyService;
	@RequestMapping("/applyAdd")
	@GlobalInterceptor
	public ResponseVO applyAdd(HttpServletRequest request,@NotEmpty String contactId,String applyInfo){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		Integer joinType=userContactService.applyAdd(tokenUserInfoDto,contactId,applyInfo);
		return getSuccessResponseVO(joinType);
	}

	@RequestMapping("/search")
	@GlobalInterceptor
	public ResponseVO search(HttpServletRequest request, @NotEmpty String contactId){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		UserContactSearchResultDto resultDto=userContactService.searchContact(tokenUserInfoDto.getUserId(),contactId);
		return getSuccessResponseVO(resultDto);
	}
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(UserContactQuery query) {
		return getSuccessResponseVO(userContactService.findListByPage(query));
	}

	/**
 	 * 新增
 	 */
	@RequestMapping("/add")
	public ResponseVO add(UserContact bean) {
		Integer result = this.userContactService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增
 	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<UserContact> listBean) {
		this.userContactService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增或修改
 	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<UserContact> listBean) {
		this.userContactService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 根据 UserIdAndContactId 查询
 	 */
	@RequestMapping("/getUserContactByUserIdAndContactId")
	public ResponseVO getUserContactByUserIdAndContactId(String userId, String contactId) {
		return getSuccessResponseVO(userContactService.getUserContactByUserIdAndContactId(userId, contactId));}

	/**
 	 * 根据 UserIdAndContactId 更新
 	 */
	@RequestMapping("/updateUserContactByUserIdAndContactId")
	public ResponseVO updateUserContactByUserIdAndContactId(UserContact bean, String userId, String contactId) {
		this.userContactService.updateUserContactByUserIdAndContactId(bean, userId, contactId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 UserIdAndContactId 删除
 	 */
	@RequestMapping("/deleteUserContactByUserIdAndContactId")
	public ResponseVO deleteUserContactByUserIdAndContactId(String userId, String contactId) {
		this.userContactService.deleteUserContactByUserIdAndContactId(userId, contactId);
		return getSuccessResponseVO(null);
}
}