package com.demo.wechat.service;


import java.util.List;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.UserContactApply;
import com.demo.wechat.entity.query.UserContactApplyQuery;
/**
 * @Description:  Service
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
public interface UserContactApplyService{

	/**
 	 * 根据条件查询列表
 	 */
	List<UserContactApply> findListByParam(UserContactApplyQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(UserContactApplyQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery query);

	/**
 	 * 新增
 	 */
	Integer add(UserContactApply bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<UserContactApply> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<UserContactApply> listBean);

	/**
 	 * 根据 ApplyId 查询
 	 */
	UserContactApply getUserContactApplyByApplyId(Integer applyId);

	/**
 	 * 根据 ApplyId 更新
 	 */
	Integer updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId); 

	/**
 	 * 根据 ApplyId 删除
 	 */
	Integer deleteUserContactApplyByApplyId(Integer applyId);
	void dealWithApply(String userId,Integer applyId,Integer status);

}