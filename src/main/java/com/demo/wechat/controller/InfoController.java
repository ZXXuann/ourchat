package com.demo.wechat.controller;


import java.util.List;
import com.demo.wechat.service.InfoService;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.po.Info;
import com.demo.wechat.entity.query.InfoQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
/**
 * @Description:  Controller
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
@RestController
@RequestMapping("/info")
public class InfoController extends ABaseController{

	@Resource
	private InfoService infoService;

	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(InfoQuery query) {
		return getSuccessResponseVO(infoService.findListByPage(query));
	}

	/**
 	 * 新增
 	 */
	@RequestMapping("/add")
	public ResponseVO add(Info bean) {
		Integer result = this.infoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增
 	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<Info> listBean) {
		this.infoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增或修改
 	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<Info> listBean) {
		this.infoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 根据 UserId 查询
 	 */
	@RequestMapping("/getInfoByUserId")
	public ResponseVO getInfoByUserId(String userId) {
		return getSuccessResponseVO(infoService.getInfoByUserId(userId));}

	/**
 	 * 根据 UserId 更新
 	 */
	@RequestMapping("/updateInfoByUserId")
	public ResponseVO updateInfoByUserId(Info bean, String userId) {
		this.infoService.updateInfoByUserId(bean, userId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 UserId 删除
 	 */
	@RequestMapping("/deleteInfoByUserId")
	public ResponseVO deleteInfoByUserId(String userId) {
		this.infoService.deleteInfoByUserId(userId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Email 查询
 	 */
	@RequestMapping("/getInfoByEmail")
	public ResponseVO getInfoByEmail(String email) {
		return getSuccessResponseVO(infoService.getInfoByEmail(email));}

	/**
 	 * 根据 Email 更新
 	 */
	@RequestMapping("/updateInfoByEmail")
	public ResponseVO updateInfoByEmail(Info bean, String email) {
		this.infoService.updateInfoByEmail(bean, email);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Email 删除
 	 */
	@RequestMapping("/deleteInfoByEmail")
	public ResponseVO deleteInfoByEmail(String email) {
		this.infoService.deleteInfoByEmail(email);
		return getSuccessResponseVO(null);
}
}