package com.demo.wechat.controller;


import java.util.List;
import com.demo.wechat.service.InfoBeautyService;
import com.demo.wechat.entity.vo.ResponseVO;
import com.demo.wechat.entity.po.InfoBeauty;
import com.demo.wechat.entity.query.InfoBeautyQuery;
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
@RequestMapping("/infoBeauty")
public class InfoBeautyController extends ABaseController{

	@Resource
	private InfoBeautyService infoBeautyService;

	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(InfoBeautyQuery query) {
		return getSuccessResponseVO(infoBeautyService.findListByPage(query));
	}

	/**
 	 * 新增
 	 */
	@RequestMapping("/add")
	public ResponseVO add(InfoBeauty bean) {
		Integer result = this.infoBeautyService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增
 	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<InfoBeauty> listBean) {
		this.infoBeautyService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增或修改
 	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<InfoBeauty> listBean) {
		this.infoBeautyService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 根据 Id 查询
 	 */
	@RequestMapping("/getInfoBeautyById")
	public ResponseVO getInfoBeautyById(Integer id) {
		return getSuccessResponseVO(infoBeautyService.getInfoBeautyById(id));}

	/**
 	 * 根据 Id 更新
 	 */
	@RequestMapping("/updateInfoBeautyById")
	public ResponseVO updateInfoBeautyById(InfoBeauty bean, Integer id) {
		this.infoBeautyService.updateInfoBeautyById(bean, id);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Id 删除
 	 */
	@RequestMapping("/deleteInfoBeautyById")
	public ResponseVO deleteInfoBeautyById(Integer id) {
		this.infoBeautyService.deleteInfoBeautyById(id);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 UserId 查询
 	 */
	@RequestMapping("/getInfoBeautyByUserId")
	public ResponseVO getInfoBeautyByUserId(String userId) {
		return getSuccessResponseVO(infoBeautyService.getInfoBeautyByUserId(userId));}

	/**
 	 * 根据 UserId 更新
 	 */
	@RequestMapping("/updateInfoBeautyByUserId")
	public ResponseVO updateInfoBeautyByUserId(InfoBeauty bean, String userId) {
		this.infoBeautyService.updateInfoBeautyByUserId(bean, userId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 UserId 删除
 	 */
	@RequestMapping("/deleteInfoBeautyByUserId")
	public ResponseVO deleteInfoBeautyByUserId(String userId) {
		this.infoBeautyService.deleteInfoBeautyByUserId(userId);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Email 查询
 	 */
	@RequestMapping("/getInfoBeautyByEmail")
	public ResponseVO getInfoBeautyByEmail(String email) {
		return getSuccessResponseVO(infoBeautyService.getInfoBeautyByEmail(email));}

	/**
 	 * 根据 Email 更新
 	 */
	@RequestMapping("/updateInfoBeautyByEmail")
	public ResponseVO updateInfoBeautyByEmail(InfoBeauty bean, String email) {
		this.infoBeautyService.updateInfoBeautyByEmail(bean, email);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Email 删除
 	 */
	@RequestMapping("/deleteInfoBeautyByEmail")
	public ResponseVO deleteInfoBeautyByEmail(String email) {
		this.infoBeautyService.deleteInfoBeautyByEmail(email);
		return getSuccessResponseVO(null);
}
}