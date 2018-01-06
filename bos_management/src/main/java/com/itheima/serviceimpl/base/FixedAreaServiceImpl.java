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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.FixedAreaDao;
import com.itheima.domain.base.FixedArea;
import com.itheima.service.base.FixedAreaService;
//添加一个注解
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	//注册一个FixedAreaDao的对象
	@Autowired
	private FixedAreaDao fixedAreaDao;
	
	
	/**
	 * 添加一个定区的信息
	 */
	@Override
	public void AddFixedArea(FixedArea fixedArea) {
		fixedAreaDao.save(fixedArea);
	}

	/**
	 * 定区信息的全查询--带分页
	 */
	@Override
	public Page<FixedArea> findAll(final FixedArea fixedArea,Pageable pageable) {
		
		//创建Specification对象
		Specification<FixedArea> spec = new Specification<FixedArea>() {
			List<Predicate> list = new ArrayList<Predicate>();
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				if(StringUtils.isNoneBlank(fixedArea.getId())){
					Predicate p1 = cb.equal(root.get("id").as(String.class), fixedArea.getId());
					list.add(p1);
				}
				if(StringUtils.isNoneBlank(fixedArea.getCompany())){
					Predicate p2 = cb.equal(root.get("company").as(String.class), fixedArea.getCompany());
					list.add(p2);
				}
				
				//自分区的查询条件
				//if(StringUtils.isNoneBlank(fixedArea.getId())){
				//	Predicate p1 = cb.equal(root.get("id").as(String.class), fixedArea.getId());
				//}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		Page<FixedArea> page = fixedAreaDao.findAll(spec,pageable);
		return page;
	}
	
	
	/**
	 * 数据全查询，不带分页
	 */
	@Override
	public List<FixedArea> queryAll() {
		List<FixedArea> list = fixedAreaDao.findAll();
		return list;
	}
	
	/**
	 * 根据Id查询 FixedArea对象
	 * @param fixedArea
	 * @return
	 */
	@Override
	public FixedArea findByFixedArea(FixedArea fixedArea) {
		FixedArea queryFixedArea = fixedAreaDao.findOne(fixedArea.getId());
		return queryFixedArea;
	}

}
