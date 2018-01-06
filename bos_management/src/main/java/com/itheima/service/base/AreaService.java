package com.itheima.service.base;

import java.util.List;

import org.springframework.data.domain.Page;

import com.itheima.domain.base.Area;

public interface AreaService {
	
	
	/**
	 * 添加一个区域信息
	 * @param area
	 */
	public void addArea(Area area);
	
	/**
	 * 区域信息的分页全查询
	 * @return
	 */
	public Page<Area> findAll(Area area,int page,int rows);
	
	
	/**
	 * 上传数据保存到数据库
	 * @param list
	 */
	public void saveBath(List<Area> list);

	
	/**
	 * 区域信息的全查询
	 */
	public List<Area> queryAll();
	
	/**
	 * 查询所有的省份
	 * @return
	 */
	public List<String> findProvince();

	/**
	 * 查询所有的城市
	 * @return
	 */
	public List<String> findCity(String province);

	
	/**
	 * 查询所有的区域
	 * @return
	 */
	public List<String> findDistrict(String province, String city);
	
	/**
	 * 根据省市的参数查询Area的对象
	 * @param area
	 * @return
	 */
	public Area findByArea(Area area);

	
	

	
	

}
