package com.demo.wechat.controller;


import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.GroupInfo;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.GroupInfoQuery;
import com.demo.wechat.entity.query.UserContactQuery;
import com.demo.wechat.entity.vo.GroupInfoVo;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.enums.GroupStatusEnum;
import com.demo.wechat.enums.ResponseCodeEnum;
import com.demo.wechat.enums.UserContactStatusEnum;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.service.GroupInfoService;
import com.demo.wechat.service.UserContactService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description:  Controller
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
@RestController
@RequestMapping("/admin")
@Validated
public class AdminGroupController extends ABaseController{

	@Resource
	private GroupInfoService groupInfoService;
	@RequestMapping("/loadGroup")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO loadGroup(GroupInfoQuery query){
		query.setOrderBy("create_time desc");
		query.setQueryMemberCount(true);
		query.setQueryGroupOwnerName(true);
		PaginationResultVO resultVO=groupInfoService.findListByPage(query);
		return getSuccessResponseVO(resultVO);
	}
	@RequestMapping("/dissolutionGroup")
	@GlobalInterceptor(checkAdmin = true)
	public ResponseVO dissolutionGroup(@NotEmpty String groupId){
		GroupInfo groupInfo=groupInfoService.getGroupInfoByGroupId(groupId);
		if(null==groupInfo){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		groupInfoService.dissolutionGroup(groupInfo.getGroupOwnerId(),groupId);
		return getSuccessResponseVO(null);
	}
}