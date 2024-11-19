package com.demo.wechat.service.impl;


import java.util.List;
import com.demo.wechat.entity.query.SimplePage;
import com.demo.wechat.enums.PageSize;
import com.demo.wechat.mappers.InfoBeautyMapper;
import com.demo.wechat.service.InfoBeautyService;
import com.demo.wechat.entity.vo.PaginationResultVO;
import com.demo.wechat.entity.po.InfoBeauty;
import com.demo.wechat.entity.query.InfoBeautyQuery;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
 * @Description:  业务接口实现
 * @Author: false
 * @Date: 2024/11/17 16:43:43
 */
@Service("InfoBeautyMapper")
public class InfoBeautyServiceImpl implements InfoBeautyService{

	@Resource
	private InfoBeautyMapper<InfoBeauty, InfoBeautyQuery> infoBeautyMapper;

	/**
 	 * 根据条件查询列表
 	 */
	@Override
	public List<InfoBeauty> findListByParam(InfoBeautyQuery query) {
		return this.infoBeautyMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	@Override
	public Integer findCountByParam(InfoBeautyQuery query) {
		return this.infoBeautyMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	@Override
	public PaginationResultVO<InfoBeauty> findListByPage(InfoBeautyQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<InfoBeauty> list = this.findListByParam(query);
		PaginationResultVO<InfoBeauty> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	@Override
	public Integer add(InfoBeauty bean) {
		return this.infoBeautyMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	@Override
	public Integer addBatch(List<InfoBeauty> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.infoBeautyMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	@Override
	public Integer addOrUpdateBatch(List<InfoBeauty> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.infoBeautyMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 Id 查询
 	 */
	@Override
	public InfoBeauty getInfoBeautyById(Integer id) {
		return this.infoBeautyMapper.selectById(id);}

	/**
 	 * 根据 Id 更新
 	 */
	@Override
	public Integer updateInfoBeautyById(InfoBeauty bean, Integer id) {
		return this.infoBeautyMapper.updateById(bean, id);}

	/**
 	 * 根据 Id 删除
 	 */
	@Override
	public Integer deleteInfoBeautyById(Integer id) {
		return this.infoBeautyMapper.deleteById(id);}

	/**
 	 * 根据 UserId 查询
 	 */
	@Override
	public InfoBeauty getInfoBeautyByUserId(String userId) {
		return this.infoBeautyMapper.selectByUserId(userId);}

	/**
 	 * 根据 UserId 更新
 	 */
	@Override
	public Integer updateInfoBeautyByUserId(InfoBeauty bean, String userId) {
		return this.infoBeautyMapper.updateByUserId(bean, userId);}

	/**
 	 * 根据 UserId 删除
 	 */
	@Override
	public Integer deleteInfoBeautyByUserId(String userId) {
		return this.infoBeautyMapper.deleteByUserId(userId);}

	/**
 	 * 根据 Email 查询
 	 */
	@Override
	public InfoBeauty getInfoBeautyByEmail(String email) {
		return this.infoBeautyMapper.selectByEmail(email);}

	/**
 	 * 根据 Email 更新
 	 */
	@Override
	public Integer updateInfoBeautyByEmail(InfoBeauty bean, String email) {
		return this.infoBeautyMapper.updateByEmail(bean, email);}

	/**
 	 * 根据 Email 删除
 	 */
	@Override
	public Integer deleteInfoBeautyByEmail(String email) {
		return this.infoBeautyMapper.deleteByEmail(email);}
}