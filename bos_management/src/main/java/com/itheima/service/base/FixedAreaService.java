package com.itheima.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.domain.base.FixedArea;

public interface FixedAreaService {
	
	/**
	 * 添加一个定区信息
	 * @param fixedArea
	 */
	public void AddFixedArea(FixedArea fixedArea);
	
	/**
	 * 定区信息的全查询--带分页效果
	 * @return
	 */
	public Page<FixedArea> findAll(FixedArea fixedArea,Pageable pageable);
	
	/**
	 * 定区信息的全查询--无分页的数据
	 * @return
	 */
	public List<FixedArea> queryAll();
	
	/**
	 * 根据Id查询 FixedArea对象
	 * @param fixedArea
	 * @return
	 */
	public FixedArea findByFixedArea(FixedArea fixedArea);
}
