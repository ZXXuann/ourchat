package com.demo.wechat.service.impl;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.system.UserInfo;
import com.demo.wechat.annotation.GlobalInterceptor;
import com.demo.wechat.entity.config.AppConfig;
import com.demo.wechat.entity.constants.Constants;
import com.demo.wechat.entity.dto.MessageSendDto;
import com.demo.wechat.entity.dto.TokenUserInfoDto;
import com.demo.wechat.entity.po.InfoBeauty;
import com.demo.wechat.entity.po.UserContact;
import com.demo.wechat.entity.query.InfoBeautyQuery;
import com.demo.wechat.entity.query.SimplePage;
import com.demo.wechat.entity.query.UserContactQuery;
import com.demo.wechat.entity.vo.UserInfoVO;
import com.demo.wechat.enums.*;
import com.demo.wechat.exception.BusinessException;
import com.demo.wechat.mappers.InfoBeautyMapper;
import com.demo.wechat.mappers.InfoMapper;
import com.demo.wechat.mappers.UserContactMapper;
import com.demo.wechat.redis.RedisComponent;
import com.demo.wechat.service.ChatSessionUserService;
import com.demo.wechat.service.InfoService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.Info;
import com.demo.wechat.entity.query.InfoQuery;
import com.demo.wechat.service.UserContactService;
import com.demo.wechat.utils.CopyTools;
import com.demo.wechat.utils.StringTools;
import com.demo.wechat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	@Autowired
	private UserContactMapper userContactMapper;
	@Autowired
	private UserContactService userContactService;
	@Autowired
	private ChatSessionUserService chatSessionUserService;
	@Autowired
	private MessageHandler messageHandler;
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
		info.setPassword(BCrypt.hashpw(password));
		info.setSex(SexEnum.MALE.getSex());
		info.setAreaName("中国");
		info.setAreaCode("86");
		info.setCreateTime(currentDate);
		info.setStatus(UserStatusEnum.ENABLE.getStatus());
		info.setJoinType(JoinTypeEnum.APPLY.getType());
		info.setLastLoginTime(currentDate);
		this.add(info);
		//----------------------------------
		userContactService.addContact4Robot(userId);
	}
	public UserInfoVO login(String email, String password){
		//根据email获取用户信息
		Info info=this.infoMapper.selectByEmail(email);
		//前置判断条件
		if(info==null){
			throw new BusinessException("账户不存在");
		}
//		if(!info.getPassword().equals(password)){
//			throw new BusinessException("密码错误");
//		}
		if(!BCrypt.checkpw(password,info.getPassword())){
			throw new BusinessException("密码错误");
		}
		if(UserStatusEnum.DISABLE.equals(info.getStatus())){
			throw new BusinessException("账号已被封禁");
		}
		//查询联系人
		UserContactQuery contactQuery=new UserContactQuery();
		contactQuery.setUserId(info.getUserId());
		contactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		List<UserContact> contactList=userContactMapper.selectList(contactQuery);
		List<String> contactIdList=contactList.stream().map(item->item.getContactId()).collect(Collectors.toList());
		//往redis中存放联系人列表
		redisComponent.cleanUserContact(info.getUserId());
		if(!contactList.isEmpty())
			redisComponent.addUserContactBatch(info.getUserId(), contactIdList);

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

	@Override
	@Transactional(rollbackFor = Exception.class)
	@GlobalInterceptor
	public void updateUserInfo(Info userInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {
		if(avatarFile!=null&&avatarCover!=null){
			String baseFolder=appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE;
			File targetFileFolder=new File(baseFolder+Constants.FILE_FOLDER_AVATAR_NAME);
			if(!targetFileFolder.exists()){
				targetFileFolder.mkdirs();
			}
			String filePath=targetFileFolder.getPath()+"/"+userInfo.getUserId()+Constants.IMAGE_SUFFIX;
			//存储图像
			avatarFile.transferTo(new File(filePath));
			//存储缩略图
			avatarCover.transferTo(new File(filePath+Constants.COVER_IMAGE_SUFFIX));
		}
		//
		Info dbInfo=this.infoMapper.selectByUserId(userInfo.getUserId());
		this.infoMapper.updateByUserId(userInfo, userInfo.getUserId());
		String contactNameUpdate=null;
		if(!dbInfo.getNickName().equals(userInfo.getNickName())){
			contactNameUpdate=userInfo.getNickName();
		}
		//更新会话信息中的昵称消息
		if(contactNameUpdate==null){
			return;
		}
		//更新token中的昵称
		TokenUserInfoDto tokenUserInfoDto=redisComponent.getTokenUserInfoDtoByUserId(userInfo.getUserId());
		tokenUserInfoDto.setNickName(contactNameUpdate);
		redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

		chatSessionUserService.updateRedundancyInfo(contactNameUpdate,userInfo.getUserId());
	}

	@Override
	public void updateUserStatus(Integer status, String userId) {
		UserStatusEnum userStatusEnum=UserStatusEnum.getByStatus(status);
		if(userStatusEnum==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		Info userInfo=new Info();
		userInfo.setStatus(userStatusEnum.getStatus());
		this.infoMapper.updateByUserId(userInfo,userId);
	}

	@Override
	public void forceOffLine(String userId) {
		MessageSendDto sendDto=new MessageSendDto();
		sendDto.setContactType(UserContactTypeEnum.USER.getType());
		sendDto.setMessageType(MessageTypeEnum.FORCE_OFF_LINE.getType());
		sendDto.setContactId(userId);
		messageHandler.sendMessage(sendDto);
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