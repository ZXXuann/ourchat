package com.demo.wechat.controller;


import java.util.List;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.system.UserInfo;
import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.dto.UserContactSearchResultDto;
import com.demo.wechat.entity.po.Info;
import com.demo.wechat.entity.query.UserContactApplyQuery;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.vo.UserInfoVO;
import com.demo.wechat.enums.PageSize;
import com.demo.wechat.enums.ResponseCodeEnum;
import com.demo.wechat.enums.UserContactStatusEnum;
import com.demo.wechat.enums.UserContactTypeEnum;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.service.InfoService;
import com.demo.wechat.service.UserContactApplyService;
import com.demo.wechat.service.UserContactService;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.UserContactQuery;
import com.demo.wechat.utils.CopyTools;
import org.apache.catalina.User;
import org.apache.el.parser.Token;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
	@RequestMapping("/addContact2BlackList")
	@GlobalInterceptor
	public ResponseVO addContact2BlackList(HttpServletRequest request,@NotNull String contactId){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		userContactService.removeUserContact(tokenUserInfoDto.getUserId(),contactId,UserContactStatusEnum.BLACKLIST);
		return getSuccessResponseVO(null);
	}
	@RequestMapping("/delContact")
	@GlobalInterceptor
	public ResponseVO delContact(HttpServletRequest request,@NotNull String contactId){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		userContactService.removeUserContact(tokenUserInfoDto.getUserId(),contactId,UserContactStatusEnum.DEL);
		return getSuccessResponseVO(null);
	}

	/**
	 * 获取联系人的信息 未必是好友
	 * @param request
	 * @param contactId
	 * @return
	 */
	@RequestMapping("/getContactInfo")
	@GlobalInterceptor
	public ResponseVO getContactInfo(HttpServletRequest request,@NotNull String contactId){
		//通过redis以及token获取用户信息
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		//通过传入的联系人ID查询用户信息
		Info userInfo=userInfoService.getInfoByUserId(contactId);
		//将传过来的userInfo转为VO实体
		UserInfoVO userInfoVO=CopyTools.copy(userInfo, UserInfoVO.class);
		//设置联系人的状态（先设置为非好友）
		userInfoVO.setContactStatus(UserContactStatusEnum.NOT_FRIEND.getStatus());
		UserContact userContact=userContactService.getUserContactByUserIdAndContactId(tokenUserInfoDto.getUserId(),contactId);
		if(userContact!=null){
			userInfoVO.setContactStatus(UserContactStatusEnum.FRIEND.getStatus());
		}
		return getSuccessResponseVO(userInfoVO);

	}

	/**
	 * 获取联系人的信息 曾经是好友
	 * @param request
	 * @param contactId
	 * @return
	 */
	@RequestMapping("/getContactUserInfo")
	@GlobalInterceptor
	public ResponseVO getContactUserInfo(HttpServletRequest request,@NotNull String contactId){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		UserContact userContact=userContactService.getUserContactByUserIdAndContactId(tokenUserInfoDto.getUserId(),contactId);
		boolean temp=ArrayUtil.contains(new Integer[]{
			UserContactStatusEnum.FRIEND.getStatus(),
				UserContactStatusEnum.DEL_BE.getStatus(),
				UserContactStatusEnum.BLACKLIST_BE.getStatus()
		},userContact.getStatus());
		if(null==userContact||!temp){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		Info userInfo=userInfoService.getInfoByUserId(contactId);
		UserInfoVO userInfoVO= CopyTools.copy(userInfo,UserInfoVO.class);
		return getSuccessResponseVO(userInfoVO);
	}
	@RequestMapping("/loadContact")
	@GlobalInterceptor
	public ResponseVO loadContact(HttpServletRequest request,@NotNull Integer contactType){
		//获取联系人（根据ContactType来获取群或者人）
		UserContactTypeEnum contactTypeEnum=UserContactTypeEnum.getByStatus(contactType);
		if(null==contactTypeEnum){
			throw new BusinessException("获取失败");
		}
		//获取用户登录的状态信息
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		//通过用户ID以及联系人的类型来查询联系人（群或者组）
		UserContactQuery contactQuery=new UserContactQuery();
		contactQuery.setUserId(tokenUserInfoDto.getUserId());
		contactQuery.setContactType(contactTypeEnum.getType());
		if(UserContactTypeEnum.USER==contactTypeEnum){
			//如果查询的是人，则连用户表查询
			contactQuery.setQueryContactUserInfo(true);
		}else if(UserContactTypeEnum.GROUP==contactTypeEnum){
			//如果查询的是组，则连组表查询
			contactQuery.setQueryGroupInfo(true);
			//需排除非自己的组
			contactQuery.setExcludeMyGroup(true);
		}
		contactQuery.setOrderBy("last_update_time desc");
		contactQuery.setStatusArray(
				new Integer[]{
						UserContactStatusEnum.FRIEND.getStatus(),
						UserContactStatusEnum.DEL_BE.getStatus(),
						UserContactStatusEnum.BLACKLIST_BE.getStatus()
				}
		);
		List<UserContact> contactList=userContactService.findListByParam(contactQuery);
		return getSuccessResponseVO(contactList);
	}
	@RequestMapping("/dealWithApply")
	@GlobalInterceptor
	public ResponseVO dealWithApply(HttpServletRequest request, @NotNull Integer applyId,@NotNull Integer status){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		this.userContactApplyService.dealWithApply(tokenUserInfoDto.getUserId(),applyId,status);
		return getSuccessResponseVO(null);
	}
	@RequestMapping("/loadApply")
	@GlobalInterceptor
	public ResponseVO loadApply(HttpServletRequest request,Integer pageNo){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		UserContactApplyQuery applyQuery=new UserContactApplyQuery();
		applyQuery.setOrderBy("last_apply_time desc");
		applyQuery.setReceiveUserId(tokenUserInfoDto.getUserId());
		applyQuery.setPageNo(pageNo);
		applyQuery.setPageSize(PageSize.SIZE15.getSize());
		applyQuery.setQueryContactInfo(true);
		PaginationResultVO resultVO=userContactApplyService.findListByPage(applyQuery);
		return getSuccessResponseVO(resultVO);
	}
	@RequestMapping("/applyAdd")
	@GlobalInterceptor
	public ResponseVO applyAdd(HttpServletRequest request,@NotEmpty String contactId,String applyInfo){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		Integer joinType=userContactApplyService.applyAdd(tokenUserInfoDto,contactId,applyInfo);
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