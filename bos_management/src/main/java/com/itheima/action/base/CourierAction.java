package com.itheima.action.base;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.itheima.domain.base.Courier;
import com.itheima.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{

	//模型驱动
	private Courier courier = new Courier();
	@Override
	public Courier getModel() {
		return courier;
	}
	
	//属性驱动
	private int page;
	private int rows;
	
	//注入一个CourierService的对象
	@Autowired
	private CourierService  courierService;
	
	
	
	/**
	 * 添加快递员信息
	 * @return
	 */
	@Action(value="add_courier",results={@Result(name="success",location="./pages/base/courier.html",type="redirect")})
	public String addCourier(){
		courierService.addCourier(courier);
		return "success";
	}
	
	/**
	 * 快递员信息的全查询
	 */
	//用户信息的全查询：前台页面会传过来两个参数：page  rows;接受参数
	@Action(value="findAll_courier",results={@Result(name="success",type="json")})
	public String findAll(){
		
		//获取到参数之后调用业务逻辑，实现页面数据的分页全查询
		Page<Courier> p = courierService.findAll(page,rows);
		
		//对数据进行处理
		Map<String,Object> map = new HashMap<String, Object>();
		
		map.put("total", p.getTotalElements());
		map.put("rows",p.getContent());
		
		//最后将数据压栈
		ActionContext.getContext().getValueStack().push(map);
		
		return "success";
	}
	
	
	
	
	
	//批量逻辑删除数据/或者还原
	@Action(value="courier_delBatch",results = {@Result(name="success",location="./pages/base/courier.html",type="redirect")})
	public String delBatch(){
		//接受批量删除的数据
	 String[] arr = ServletActionContext.getRequest().getParameter("ids").split(",");
	 
	 String flag = ServletActionContext.getRequest().getParameter("flag");
	 
	 if("true".equals(flag)){
		 //调用还原
		 courierService.doRestore(arr);
	 }else if("false".equals(flag)){
		 //调用逻辑删除
		 courierService.delBatch(arr);
	 }
	 
	 //调用业务将这些数据的对象进行逻辑删除
	 
	return "success";
	}

	
	
	
	
	
	
	
	
	
	//提供成员变量的setter方法
	public void setPage(int page) {
		this.page = page;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	
	

}
