package com.itheima.serviceimpl.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.CustomerDao;
import com.itheima.domain.base.ActiveStateCustomer;
import com.itheima.domain.base.Customer;
import com.itheima.service.base.CustomerService;
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	//注入一个CustomerDao对象
	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public List<Customer> findNoAssociateFixedArea() {
		List<Customer> list = customerDao.findByFixedAreaIdIsNull();
		return list;
	}

	@Override
	public List<Customer> findHasAssociateFixedArea(String fixedAreaId) {
		List<Customer> list = customerDao.findByFixedAreaId(fixedAreaId);
		return list;
	}

	@Override
	public void updateNoAssociateFixedAreaTOHasAssociateFixedArea(
			String customerIdStr, String fixedAreaId) {
		
		if(StringUtils.isBlank(customerIdStr)){
			return;
		}
		
		//获取到所用的用户的Id
		String[] arr = customerIdStr.split(",");
		for (String str : arr) {
			//获取到关联到定区的用户Id
			int id = Integer.parseInt(str);
			
			//调用业务，对这些用户的信息进行修改
			customerDao.updateCustomer(fixedAreaId,id);
		}
		
	}

	@Override
	public void addCustomer(Customer customer) {
		customerDao.save(customer);
	}

	@Override
	public ActiveStateCustomer activeCustomer(String telephone) {
		ActiveStateCustomer activeStateCustomer = new ActiveStateCustomer();
		String msg="";
		//激活之前，需要判断该用户是否已经被激活过
		
		System.out.println("我是CRM系统的电话"+telephone);
		
		
		Customer customer = customerDao.findByTelephone(telephone);
		
		System.out.println("***************");
		System.out.println(customer);
		System.out.println("***************");
		if(customer!=null){
			if(customer.getType()==null){
				//需要激活
				customer.setType(1);
				 msg = "激活成功";
			}else{
				msg = "激活失败,不能重复激活";
			}
		}		
		System.out.println(msg);
		System.out.println(customer);
		activeStateCustomer.setMsg(msg);
		return activeStateCustomer;
	}


}
