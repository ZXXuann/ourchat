package com.demo.wechat.service;


import java.util.List;

import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.dto.UserContactSearchResultDto;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.UserContactQuery;
/**
 * @Description:  Service
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
public interface UserContactService{

	/**
 	 * 根据条件查询列表
 	 */
	List<UserContact> findListByParam(UserContactQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(UserContactQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<UserContact> findListByPage(UserContactQuery query);

	/**
 	 * 新增
 	 */
	Integer add(UserContact bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<UserContact> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<UserContact> listBean);

	/**
 	 * 根据 UserIdAndContactId 查询
 	 */
	UserContact getUserContactByUserIdAndContactId(String userId, String contactId);

	/**
 	 * 根据 UserIdAndContactId 更新
 	 */
	Integer updateUserContactByUserIdAndContactId(UserContact bean, String userId, String contactId); 

	/**
 	 * 根据 UserIdAndContactId 删除
 	 */
	Integer deleteUserContactByUserIdAndContactId(String userId, String contactId);
	UserContactSearchResultDto searchContact(String userId, String contactId);
	Integer applyAdd(TokenUserInfoDto tokenUserInfoDto,String contactId,String applyInfo);

}