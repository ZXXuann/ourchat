package com.demo.wechat.service;


import java.util.List;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.GroupInfo;
import com.demo.wechat.entity.query.GroupInfoQuery;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:  Service
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
public interface GroupInfoService{

	/**
 	 * 根据条件查询列表
 	 */
	List<GroupInfo> findListByParam(GroupInfoQuery query);

	/**
 	 * 根据条件查询数量
 	 */
	Integer findCountByParam(GroupInfoQuery query);

	/**
 	 * 分页查询
 	 */
	PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery query);

	/**
 	 * 新增
 	 */
	Integer add(GroupInfo bean);

	/**
 	 * 批量新增
 	 */
	Integer addBatch(List<GroupInfo> listBean);

	/**
 	 * 批量新增或修改
 	 */
	Integer addOrUpdateBatch(List<GroupInfo> listBean);

	/**
 	 * 根据 GroupId 查询
 	 */
	GroupInfo getGroupInfoByGroupId(String groupId);

	/**
 	 * 根据 GroupId 更新
 	 */
	Integer updateGroupInfoByGroupId(GroupInfo bean, String groupId); 

	/**
 	 * 根据 GroupId 删除
 	 */
	Integer deleteGroupInfoByGroupId(String groupId);

	void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile,MultipartFile avatarCover);
}