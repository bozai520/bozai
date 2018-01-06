package com.itheima.service.base;

import org.springframework.data.domain.Page;

import com.itheima.domain.base.Courier;

public interface CourierService {
	
	//添加一个快递员
	public void addCourier(Courier courier);
	
	
	//快递员信息的全查询
	public Page<Courier> findAll(int page,int rows);

	//数据批量逻辑删除
	public void delBatch(String[] arr);

	//数据批量还原数据
	public void doRestore(String[] arr);
}
