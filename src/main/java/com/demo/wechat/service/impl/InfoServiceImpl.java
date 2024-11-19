package com.demo.wechat.service.impl;


import java.beans.Transient;
import java.util.*;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.system.UserInfo;
import com.demo.wechat.entity.config.AppConfig;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.InfoBeauty;
import com.demo.wechat.entity.query.InfoBeautyQuery;
import com.demo.wechat.entity.query.SimplePage;
import com.demo.wechat.entity.vo.UserInfoVO;
import com.demo.wechat.enums.JoinTypeEnum;
import com.demo.wechat.enums.PageSize;
import com.demo.wechat.enums.SexEnum;
import com.demo.wechat.enums.UserStatusEnum;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.mappers.InfoBeautyMapper;
import com.demo.wechat.mappers.InfoMapper;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.service.InfoService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.Info;
import com.demo.wechat.entity.query.InfoQuery;
import com.demo.wechat.utils.CopyTools;
import com.demo.wechat.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:  业务接口实现
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
@Service("InfoMapper")
public class InfoServiceImpl implements InfoService{
	@Autowired
	private AppConfig appConfig;
	@Autowired
	private InfoMapper<Info, InfoQuery> infoMapper;
	@Autowired
	private InfoBeautyMapper<InfoBeauty, InfoBeautyQuery> infoBeautyMapper;
	@Autowired
	private RedisComponent redisComponent;
	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<Info> findListByParam(InfoQuery query) {
		return this.infoMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(InfoQuery query) {
		return this.infoMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<Info> findListByPage(InfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<Info> list = this.findListByParam(query);
		PaginationResultVO<Info> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(Info bean) {
		return this.infoMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<Info> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.infoMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<Info> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.infoMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 UserId 查询
 	 */
	@Override
	public Info getInfoByUserId(String userId) {
		return this.infoMapper.selectByUserId(userId);}

	/**
 	 * 根据 UserId 更新
 	 */
	@Override
	public Integer updateInfoByUserId(Info bean, String userId) {
		return this.infoMapper.updateByUserId(bean, userId);}

	/**
 	 * 根据 UserId 删除
 	 */
	@Override
	public Integer deleteInfoByUserId(String userId) {
		return this.infoMapper.deleteByUserId(userId);}

	/**
 	 * 根据 Email 查询
 	 */
	@Override
	public Info getInfoByEmail(String email) {
		return this.infoMapper.selectByEmail(email);}

	/**
 	 * 根据 Email 更新
 	 */
	@Override
	public Integer updateInfoByEmail(Info bean, String email) {
		return this.infoMapper.updateByEmail(bean, email);}

	/**
 	 * 根据 Email 删除
 	 */
	@Override
	public Integer deleteInfoByEmail(String email) {
		return this.infoMapper.deleteByEmail(email);}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void register(String email, String nickname, String password) {
//		Map<String,Object> result=new HashMap<>();
		Info info = this.infoMapper.selectByEmail(email);
		String userId=null;
		if(null==info){
			userId= StringTools.getUserId();
			info=new Info();
		}else{
			throw new BusinessException("邮箱已存在");
		}
		//存入数据库----------------------------------
		Date currentDate=new Date();
		info.setUserId(userId);
		info.setEmail(email);
		info.setNickName(nickname);
		info.setPassword(password);
		info.setSex(SexEnum.MALE.getSex());
		info.setAreaName("中国");
		info.setAreaCode("86");
		info.setCreateTime(currentDate);
		info.setStatus(UserStatusEnum.ENABLE.getStatus());
		info.setJoinType(JoinTypeEnum.APPLY.getType());
		info.setLastLoginTime(currentDate);
		this.add(info);
		//----------------------------------
	}
	public UserInfoVO login(String email, String password){
		//根据email获取用户信息
		Info info=this.infoMapper.selectByEmail(email);
		//前置判断条件
		if(info==null){
			throw new BusinessException("账户不存在");
		}
		if(!info.getPassword().equals(password)){
			throw new BusinessException("密码错误");
		}
		if(UserStatusEnum.DISABLE.equals(info.getStatus())){
			throw new BusinessException("账号已被封禁");
		}
		//将info转换为TOKENUSEERINFODTO
		TokenUserInfoDto tokenUserInfoDto=getTokenUserInfoDto(info);
		//心跳
		Long lastHeartBeat=redisComponent.getUserHeartBeat(info.getUserId());
		if(lastHeartBeat!=null){
			throw new BusinessException("此账号已在别处登录，请退出后再登录");
		}
		//建立心跳
		if(!redisComponent.setUserHeartBeat(info.getUserId())){
			throw new BusinessException("建立心跳失败");
		};
		//保存登录信息到redis
		String token=StringTools.encodeMd5(tokenUserInfoDto.getUserId()+StringTools.getRandomString(Constants.LENGTH_20));

		//保存token到TOKENUSERINFODTO
		tokenUserInfoDto.setToken(token);
		//将TOKENUSERINFODTO暂存到redis，方便下次使用
		redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

		//将userinfo信息转为VO的格式，返回给前端
		UserInfoVO userInfoVO= CopyTools.copy(tokenUserInfoDto, UserInfoVO.class);
		return userInfoVO;
//		userInfoVO.setToken(tokenUserInfoDto.getToken());
//		userInfoVO.setAdmin(tokenUserInfoDto.getAdmin());
//		return getTokenUserInfoDto(info);
	}
	private TokenUserInfoDto getTokenUserInfoDto(Info info){
		TokenUserInfoDto tokenUserInfoDto=new TokenUserInfoDto();
		tokenUserInfoDto.setUserId(info.getUserId());
		tokenUserInfoDto.setNickName(info.getNickName());
		String adminEmails= appConfig.getAdminEmails();
		if(!StringTools.isEmpty(adminEmails)&& ArrayUtil.contains(adminEmails.split(","),info.getEmail())){
			tokenUserInfoDto.setAdmin(true);
		}else{
			tokenUserInfoDto.setAdmin(false);
		}
		return tokenUserInfoDto;
	}
	public static void main(String[] args) {
		for(int i=0;i<10;i++){
			System.out.println(StringTools.getUserId());
		}
	}
}