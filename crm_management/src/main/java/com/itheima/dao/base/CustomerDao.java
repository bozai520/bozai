package com.itheima.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.base.Customer;

public interface CustomerDao extends JpaRepository<Customer, Integer>,JpaSpecificationExecutor<Customer> {

	/**
	 * 获取到未关联到定区的用户
	 * @return
	 */
	public List<Customer> findByFixedAreaIdIsNull();

	
	/**
	 * 获取到当前定区关联的所有用户
	 * @return
	 */
	public List<Customer> findByFixedAreaId(String fixedAreaId);

	@Query(value="update T_CUSTOMER set C_Fixed_AREA_ID = ? where c_id = ?",nativeQuery=true)
	@Modifying
	public void updateCustomer(String fixedAreaId,int id);

	/**
	 * 根据电话查询用户信息
	 * @return
	 */
	public Customer findByTelephone(String telephone);

	
	
//	@Query(value="update T_CUSTOMER set c_type = 1 where telephone = ?",nativeQuery=true)
//	@Modifying
//	public void updateCustomerSetType(String telephone);

}
