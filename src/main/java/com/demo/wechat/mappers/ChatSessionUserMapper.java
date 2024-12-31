package com.demo.wechat.mappers;

import com.demo.wechat.entity.po.ChatSessionUser;
import com.demo.wechat.entity.query.ChatSessionUserQuery;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 会话用户 Mapper
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
public interface ChatSessionUserMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 UserIdAndContactId 查询
 	 */
	T selectByUserIdAndContactId(@Param("userId")String userId, @Param("contactId")String contactId);

	/**
 	 * 根据 UserIdAndContactId 更新
 	 */
	Integer updateByUserIdAndContactId(@Param("bean") T t, @Param("userId")String userId, @Param("contactId")String contactId); 

	/**
 	 * 根据 UserIdAndContactId 删除
 	 */
	Integer deleteByUserIdAndContactId(@Param("userId")String userId, @Param("contactId")String contactId);

	Integer updateByParam(@Param("updateInfo")ChatSessionUser updateInfo, @Param("chatSessionUserQuery")ChatSessionUserQuery chatSessionUserQuery);
}