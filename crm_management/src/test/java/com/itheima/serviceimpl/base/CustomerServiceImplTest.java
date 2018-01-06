package com.itheima.serviceimpl.base;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itheima.domain.base.Customer;
import com.itheima.service.base.CustomerService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerServiceImplTest {

	//注入一个对象
	@Autowired
	private CustomerService cs;
	
	@Test
	public void testFindNoAssociateFixedArea() {
		List<Customer> list = cs.findNoAssociateFixedArea();
		for (Customer customer : list) {
			System.out.println(customer);
		}
	}

	@Test
	public void testFindHasAssociateFixedArea() {
		List<Customer> list = cs.findHasAssociateFixedArea("dq001");
		for (Customer customer : list) {
			System.out.println(customer);
		}
	}

	@Test
	public void testUpdateNoAssociateFixedAreaTOHasAssociateFixedArea() {
		cs.updateNoAssociateFixedAreaTOHasAssociateFixedArea("2,3", "dq002");
	}

}
