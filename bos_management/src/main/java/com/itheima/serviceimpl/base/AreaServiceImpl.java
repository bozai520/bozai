package com.itheima.serviceimpl.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.AreaDao;
import com.itheima.domain.base.Area;
import com.itheima.service.base.AreaService;
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	//注入一个AreaDao对象
	@Autowired
	private AreaDao areaDao;
	
	/**
	 * 区域信息的添加
	 */
	@Override
	public void addArea(Area area) {
		areaDao.save(area);
	}

	
	/**
	 * 区域信息的全查询：：：：带分页
	 */
	@Override
	public Page<Area> findAll(final Area area,int page,int rows) {
				//封装参数
				Pageable pageable = new PageRequest(page-1, rows);
				
				//构建第一个Spec
				Specification<Area> spec = new Specification<Area>() {
					
					@Override
					public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query,
							CriteriaBuilder cb) {
						
						List<Predicate> list = new ArrayList<Predicate>();
						
						//在这里进行数据条件的封装
						if(StringUtils.isNotBlank(area.getProvince())){
							Predicate p1 = cb.like(root.get("province").as(String.class), "%"+area.getProvince()+"%");
							
							list.add(p1);
						}
						
						if(StringUtils.isNotBlank(area.getCity())){
							Predicate p2 = cb.like(root.get("city").as(String.class),"%"+area.getCity()+"%" );
							list.add(p2);
						}
						
						if(StringUtils.isNotBlank(area.getDistrict())){
							Predicate p3 = cb.like(root.get("district").as(String.class), "%"+area.getDistrict()+"%");
							list.add(p3);
						}
						return cb.and(list.toArray(new Predicate[0]));
					}
				};
				
				//条件查询到额条件封装好，调用业务逻辑
				Page<Area> pages = areaDao.findAll(spec, pageable);
				
				return pages;
	}
	
	/**
	 * 导入的数据批量添加到数据库中
	 */
	@Override
	public void saveBath(List<Area> list) {
		areaDao.save(list);
	}

	
	/**
	 * 对区域信息进行全查询，不带分页
	 */
	@Override
	public List<Area> queryAll() {
		List<Area> list = areaDao.findAll();
		return list;
	}


	@Override
	public List<String> findProvince() {
		//查询到所有的省份，并且不重复
		List<String> list = areaDao.queryProvince();
		return list;
	}


	@Override
	public List<String> findCity(String province) {
		
		List<String> list = areaDao.queryCity(province);
		return list;
	}


	@Override
	public List<String> findDistrict(String province, String city) {
		List<String> list = areaDao.queryDistrict(province,city);
		return list;
	}
	
	/**
	 * 根据省市的参数查询Area的对象
	 * @param area
	 * @return
	 */
	@Override
	public Area findByArea(Area area) {
		Area queryArea = areaDao.queryByQuYu(area.getProvince(),area.getCity(),area.getDistrict());
		return queryArea;
	}
}
