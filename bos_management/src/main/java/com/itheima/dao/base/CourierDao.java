package com.itheima.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.base.Courier;

public interface CourierDao extends JpaRepository<Courier, Integer> {

	
	@Query("update Courier set deltag = 1 where id = ?")
	@Modifying
	public void delBatch(int id);

	
	@Query("update Courier set deltag = null where id = ?")
	@Modifying
	public void doRestore(int id);
	
}
