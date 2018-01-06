package com.itheima.serviceimpl.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
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

import com.itheima.dao.base.SubAreaDao;
import com.itheima.domain.base.Area;
import com.itheima.domain.base.SubArea;
import com.itheima.service.base.SubAreaService;
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

	//注入一个SubArea的对象
	@Autowired
	private SubAreaDao subAreaDao;
	
	@Override
	public void addSubAreaBath(List<SubArea> list) {
		subAreaDao.save(list);
	}

	/**
	 * 带分页的全查询
	 */
	@Override
	public Page<SubArea> findAll(final SubArea subArea, int page, int rows) {
		//创建一个条件分页查询的条件
		Specification<SubArea> spec = new Specification<SubArea>() {
			
			@Override
			public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				
				List<Predicate> list = new ArrayList<Predicate>();
				if(StringUtils.isNoneBlank(subArea.getKeyWords())){
					Predicate p1 = cb.like(root.get("keyWords").as(String.class), "%"+subArea.getKeyWords()+"%");
					list.add(p1);
				}
				//子节点的获取
				if(subArea.getArea() !=null){
					Area area = subArea.getArea();
					root.get("area").as(String.class);
					
					if(StringUtils.isNoneBlank(subArea.getArea().getProvince())){
						Predicate p2 = cb.like(root.get("area").get("province").as(String.class),"%"+subArea.getArea().getProvince()+"%");
						list.add(p2);//(root.<String> get("clazz").<String> get("name")
									//root.<String> get("name")
									//root.get("name").as(String.class)
									//root.<String> get("area").<String> get("province")
									//root.get("area").as(String.class).get("province")
					}
					if(StringUtils.isNoneBlank(subArea.getArea().getCity())){
						Predicate p3 = cb.like(root.get("area").get("city").as(String.class),"%"+subArea.getArea().getCity()+"%");
						list.add(p3);
					}
					if(StringUtils.isNoneBlank(subArea.getArea().getDistrict())){
						Predicate p4 = cb.like(root.get("area").get("district").as(String.class),"%"+subArea.getArea().getDistrict()+"%");
						list.add(p4);
					}
				}
				//获取到另外一个子节点
				if(subArea.getFixedArea() !=null){
				
					if(StringUtils.isNoneBlank(subArea.getArea().getId())){
						Predicate p5 = cb.equal(root.get("fixedArea").get("id").as(String.class), subArea.getArea().getId());
						list.add(p5);//(root.<String> get("fixedAreaRoot").<String> get("id")
						//root.get("area").get("province").as(String.class)
					}
				
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
			
		};
		
		//封装参数
		Pageable pageable = new PageRequest(page-1, rows);
		Page<SubArea> p = subAreaDao.findAll(spec,pageable);
		return p;
	}
	
	/**
	 * 不带分页的数据全查询
	 */
	@Override
	public List<SubArea> queryAll() {
		List<SubArea> list = subAreaDao.findAll();
		return list;
	}
	
	/**
	 * 添加分区的信息
	 * @param subArea
	 */
	@Override
	public void addSubArea(SubArea subArea) {
		subAreaDao.save(subArea);
	}
	
	/**
	 * 获取到当前表中的最大索引值
	 * @return
	 */
	@Override
	public String getLastRowID() {
		List<String> list = subAreaDao.getLastRowID();
		int max = list.size();
		String rowID = list.get(max-1);
		return rowID;
	}
}
