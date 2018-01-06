package com.itheima.service.base;

import java.util.List;

import com.itheima.domain.base.Standard;

public interface StandardService {
	
	//添加标准
	public void addStandard(Standard standard);
	
	//根据Id进行数据的查询
	public Standard findById(Integer id);
//	
//	//修改Standard数据
//	public void updateStandard(Standard standard);
//	
	//查询所有的数据
	public List<Standard> findAll();
}
