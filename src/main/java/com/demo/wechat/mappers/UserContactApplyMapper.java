package com.demo.wechat.mappers;

import com.demo.wechat.entity.po.UserContactApply;
import com.demo.wechat.entity.query.UserContactApplyQuery;
import com.demo.wechat.entity.query.UserContactQuery;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:  Mapper
 * @Author: false
 * @Date: 2024/11/22 21:01:52
 */
public interface UserContactApplyMapper<T, P> extends BaseMapper {
	T selectByApplyUserIdAndReceiveUserIdAndContactId(@Param("applyUserId") String applyUserId,
													  @Param("receiveUserId") String receiveUserId,
													  @Param("contactId") String contactId);

	/**
 	 * 根据 ApplyId 查询
 	 */
	T selectByApplyId(@Param("applyId")Integer applyId);

	/**
 	 * 根据 ApplyId 更新
 	 */
	Integer updateByApplyId(@Param("bean") T t, @Param("applyId")Integer applyId);
	Integer updateByParam(@Param("updateInfo") UserContactApply updateInfo,@Param("applyQuery") UserContactApplyQuery applyQuery);

	/**
 	 * 根据 ApplyId 删除
 	 */
	Integer deleteByApplyId(@Param("applyId")Integer applyId);

}