package com.demo.wechat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:  Mapper
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
public interface InfoBeautyMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 Id 查询
 	 */
	T selectById(@Param("id")Integer id);

	/**
 	 * 根据 Id 更新
 	 */
	Integer updateById(@Param("bean") T t, @Param("id")Integer id); 

	/**
 	 * 根据 Id 删除
 	 */
	Integer deleteById(@Param("id")Integer id);

	/**
 	 * 根据 UserId 查询
 	 */
	T selectByUserId(@Param("userId")String userId);

	/**
 	 * 根据 UserId 更新
 	 */
	Integer updateByUserId(@Param("bean") T t, @Param("userId")String userId); 

	/**
 	 * 根据 UserId 删除
 	 */
	Integer deleteByUserId(@Param("userId")String userId);

	/**
 	 * 根据 Email 查询
 	 */
	T selectByEmail(@Param("email")String email);

	/**
 	 * 根据 Email 更新
 	 */
	Integer updateByEmail(@Param("bean") T t, @Param("email")String email); 

	/**
 	 * 根据 Email 删除
 	 */
	Integer deleteByEmail(@Param("email")String email);

}