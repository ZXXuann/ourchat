package com.demo.wechat.controller;


import java.util.List;

import cn.hutool.http.server.HttpServerRequest;
import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.service.GroupInfoService;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.po.GroupInfo;
import com.demo.wechat.entity.query.GroupInfoQuery;
import com.demo.wechat.service.UserContactService;
import com.demo.wechat.service.impl.UserContactStatusEnum;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Description:  Controller
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
@RestController
@RequestMapping("/groupInfo")
@Validated
public class GroupInfoController extends ABaseController{

	@Resource
	private GroupInfoService groupInfoService;
	@Resource
	private UserContactService userContactService;
	@RequestMapping("/getGroupInfo4Chat")
	public ResponseVO getGroupInfo4Chat(HttpServletRequest request,
										@NotEmpty String groupId){

	}
	private GroupInfo getGroupDetailCommon(HttpServletRequest request,
										   @NotEmpty String groupId){
		//获取Token里面的用户信息
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		//通过用户ID和群ID查询当前组信息是否跟当前用户ID有关系
		UserContact userContact=this.userContactService.getUserContactByUserIdAndContactId(tokenUserInfoDto.getUserId(), groupId);
		//判断一下是不是还是好友关系（有无退群）
		if(null==userContact||!UserContactStatusEnum.FRIEND.getStatus().equals(userContact.getStatus())){
			throw new BusinessException("群聊不存在");
		}
		//获取该组对应的群信息
		GroupInfo groupInfo=this.groupInfoService.getGroupInfoByGroupId(groupId);
		if(null==groupInfo||!GroupStatusEnum.NORMAL.getStatus().equals(groupInfo.getStatus())){
			throw new BusinessException("群聊不存在或已解散");
		}
		return groupInfo;

	}
	@RequestMapping("/loadMyGroup")
	@GlobalInterceptor
	public ResponseVO loadMyGroup(HttpServletRequest request){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		GroupInfoQuery groupInfoQuery=new GroupInfoQuery();
		groupInfoQuery.setGroupOwnerId(tokenUserInfoDto.getUserId());
		groupInfoQuery.setOrderBy("create_time desc");
		List<GroupInfo> groupInfoList=this.groupInfoService.findListByParam(groupInfoQuery);
		return getSuccessResponseVO(groupInfoList);
	}
	@RequestMapping("/saveGroup")
	@GlobalInterceptor
	public ResponseVO saveGroup(HttpServletRequest request,
								String groupId,
								@NotEmpty String groupName,
								String groupNotice,
								@NotNull Integer joinType,
								MultipartFile avatarFile,
								MultipartFile avatarCover){
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfo(request);
		GroupInfo groupInfo=new GroupInfo();
		groupInfo.setGroupId(groupId);
		groupInfo.setGroupOwnerId(tokenUserInfoDto.getUserId());
		groupInfo.setGroupName(groupName);
		groupInfo.setGroupNotice(groupNotice);
		groupInfo.setJoinType(joinType);
		this.groupInfoService.saveGroup(groupInfo,avatarFile,avatarCover);
		return getSuccessResponseVO(null);
	}

	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(GroupInfoQuery query) {
		return getSuccessResponseVO(groupInfoService.findListByPage(query));
	}

	/**
 	 * 新增
 	 */
	@RequestMapping("/add")
	public ResponseVO add(GroupInfo bean) {
		Integer result = this.groupInfoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增
 	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<GroupInfo> listBean) {
		this.groupInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增或修改
 	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<GroupInfo> listBean) {
		this.groupInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 根据 GroupId 查询
 	 */
	@RequestMapping("/getGroupInfoByGroupId")
	public ResponseVO getGroupInfoByGroupId(String groupId) {
		return getSuccessResponseVO(groupInfoService.getGroupInfoByGroupId(groupId));}

	/**
 	 * 根据 GroupId 更新
 	 */
	@RequestMapping("/updateGroupInfoByGroupId")
	public ResponseVO updateGroupInfoByGroupId(GroupInfo bean, String groupId) {
		this.groupInfoService.updateGroupInfoByGroupId(bean, groupId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 GroupId 删除
 	 */
	@RequestMapping("/deleteGroupInfoByGroupId")
	public ResponseVO deleteGroupInfoByGroupId(String groupId) {
		this.groupInfoService.deleteGroupInfoByGroupId(groupId);
		return getSuccessResponseVO(null);
}
}