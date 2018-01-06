package com.itheima.service.base;

import java.util.List;

import org.springframework.data.domain.Page;

import com.itheima.domain.base.Area;
import com.itheima.domain.base.FixedArea;
import com.itheima.domain.base.SubArea;

public interface SubAreaService {

	/**
	 * 将导入数据保存到数据库中
	 * 
	 * @param list
	 */
	public void addSubAreaBath(List<SubArea> list);

	/**
	 * 带分页的全查询
	 * 
	 * @param subArea
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<SubArea> findAll(SubArea subArea, int page, int rows);

	/**
	 * 不带分页的数据全查询
	 * 
	 * @return
	 */
	public List<SubArea> queryAll();
	
	/**
	 * 添加分区的信息
	 * @param subArea
	 */
	public void addSubArea(SubArea subArea);
	
	/**
	 * 获取到当前表中的最大索引值
	 * @return
	 */
	public String getLastRowID();

}
