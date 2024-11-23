package com.demo.wechat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:  Mapper
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
public interface UserContactApplyMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 ApplyId 查询
 	 */
	T selectByApplyId(@Param("applyId")Integer applyId);

	/**
 	 * 根据 ApplyId 更新
 	 */
	Integer updateByApplyId(@Param("bean") T t, @Param("applyId")Integer applyId); 

	/**
 	 * 根据 ApplyId 删除
 	 */
	Integer deleteByApplyId(@Param("applyId")Integer applyId);

}