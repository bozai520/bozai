package com.itheima.dao.base;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.base.Area;

public interface AreaDao extends JpaRepository<Area, String>,JpaSpecificationExecutor<Area>{

	@Query(value="select distinct(c_province) from t_area",nativeQuery=true)
	public List<String> queryProvince();

	@Query(value="select distinct(c_city) from t_area where c_province = ?",nativeQuery=true)
	public List<String> queryCity(String province);

	@Query(value="select c_district from t_area where c_province = ? and c_city = ?",nativeQuery=true)
	public List<String> queryDistrict(String province, String city);

	@Query(value="select * from t_area where c_province = ? and c_city = ? and c_district = ?",nativeQuery=true)
	public Area queryByQuYu(String province, String city, String district);


	
}
