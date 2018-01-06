package com.itheima.serviceimpl.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.StandardDao;
import com.itheima.domain.base.Standard;
import com.itheima.service.base.StandardService;
@Service
@Transactional
public class StandardServiceImpl implements StandardService {

	//注入一个Standard的dao对象
	@Autowired
	private StandardDao standardDao;
	
	@Override
	public void addStandard(Standard standard) {
		
		standardDao.save(standard);
	}

	@Override
	public Standard findById(Integer id) {
		
		return standardDao.findOne(id);
	}

//	@Override
//	public void updateStandard(Standard standard) {
//		standardDao.updateStandard(standard.getName(),standard.getMinWeight(),
//				standard.getMaxWeight(),standard.getMinLength(),standard.getMaxLength(),standard.getId());
//		
//	}

	@Override
	public List<Standard> findAll() {
		List<Standard> list = standardDao.findAll();
		return list;
	}

}
