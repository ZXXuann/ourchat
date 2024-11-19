package com.demo.wechat.service;


import java.util.List;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.InfoBeauty;
import com.demo.wechat.entity.query.InfoBeautyQuery;
/**
 * @Description:  Service
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
public interface InfoBeautyService{

	/**
 	 * 根据条件查询列表
 	 */
	List<InfoBeauty> findListByParam(InfoBeautyQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(InfoBeautyQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<InfoBeauty> findListByPage(InfoBeautyQuery query);

	/**
 	 * 新增
 	 */
	Integer add(InfoBeauty bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<InfoBeauty> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<InfoBeauty> listBean);

	/**
 	 * 根据 Id 查询
 	 */
	InfoBeauty getInfoBeautyById(Integer id);

	/**
 	 * 根据 Id 更新
 	 */
	Integer updateInfoBeautyById(InfoBeauty bean, Integer id); 

	/**
 	 * 根据 Id 删除
 	 */
	Integer deleteInfoBeautyById(Integer id);

	/**
 	 * 根据 UserId 查询
 	 */
	InfoBeauty getInfoBeautyByUserId(String userId);

	/**
 	 * 根据 UserId 更新
 	 */
	Integer updateInfoBeautyByUserId(InfoBeauty bean, String userId); 

	/**
 	 * 根据 UserId 删除
 	 */
	Integer deleteInfoBeautyByUserId(String userId);

	/**
 	 * 根据 Email 查询
 	 */
	InfoBeauty getInfoBeautyByEmail(String email);

	/**
 	 * 根据 Email 更新
 	 */
	Integer updateInfoBeautyByEmail(InfoBeauty bean, String email); 

	/**
 	 * 根据 Email 删除
 	 */
	Integer deleteInfoBeautyByEmail(String email);
}