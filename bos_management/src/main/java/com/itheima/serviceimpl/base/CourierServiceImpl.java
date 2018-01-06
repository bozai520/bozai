package com.itheima.serviceimpl.base;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.CourierDao;
import com.itheima.domain.base.Courier;
import com.itheima.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	//注入一个用户Dao的对象
	@Autowired
	private CourierDao courierDao;
	
	/**
	 * 添加一个快递 
	 */
	@Override
	public void addCourier(Courier courier) {
		courierDao.save(courier);
	}

	@Override
	public Page<Courier> findAll(int page, int rows) {
		
		//数据的封装
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Courier> findAll = courierDao.findAll(pageable);
		return findAll;
	}

	@Override
	public void delBatch(String[] arr) {
		for (String str : arr) {
			int id = Integer.parseInt(str);
			courierDao.delBatch(id);
		}
	}

	@Override
	public void doRestore(String[] arr) {
		for (String str : arr) {
			int id = Integer.parseInt(str);
			courierDao.doRestore(id);
		}
		
	}

}
