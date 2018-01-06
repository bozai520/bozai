package com.itheima.dao.base;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.base.Standard;

public interface StandardDao extends JpaRepository<Standard, Integer> {
		
	
	//修改数据
//	@Query(value="update Standard set name=? and minWeight=? and maxWeight=? and minLength=? and maxLength=? where id=?")
//	@Modifying
//	public void updateStandard(String name,Integer minWeight,Integer maxWeight,Integer minLength,Integer maxLength,Integer id);
}
