package com.demo.wechat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 聊天消息表 Mapper
 * @Author: false
 * @Date: 2024/12/23 16:49:28
 */
public interface ChatMessageMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 MessageId 查询
 	 */
	T selectByMessageId(@Param("messageId")Long messageId);

	/**
 	 * 根据 MessageId 更新
 	 */
	Integer updateByMessageId(@Param("bean") T t, @Param("messageId")Long messageId); 

	/**
 	 * 根据 MessageId 删除
 	 */
	Integer deleteByMessageId(@Param("messageId")Long messageId);

	Integer updateByParam(@Param("updateInfo") T t,@Param("chatMessageQuery")P p);
}