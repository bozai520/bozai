package com.itheima.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.base.Area;
import com.itheima.domain.base.FixedArea;
import com.itheima.domain.base.SubArea;

public interface SubAreaDao extends JpaRepository<SubArea, String>,JpaSpecificationExecutor<SubArea> {
	
	@Query(value="select c_id from t_sub_area",nativeQuery=true)
	public List<String> getLastRowID();


}
