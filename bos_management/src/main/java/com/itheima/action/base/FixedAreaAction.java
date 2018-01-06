package com.itheima.action.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.domain.base.Customer;
import com.itheima.domain.base.FixedArea;
import com.itheima.service.base.FixedAreaService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

//添加注解
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends ActionSupport implements
		ModelDriven<FixedArea> {

	// 模型驱动
	private FixedArea fixedArea = new FixedArea();

	@Override
	public FixedArea getModel() {
		return fixedArea;
	}

	// 接受EasyUI的分页传递给后台的参数：：属性驱动
	private int page;
	private int rows;

	// 注册一个FixedAreaService的对象
	@Autowired
	private FixedAreaService fixedAreaService;

	/**
	 * 添加一个定区的信息
	 */
	@Action(value = "save_fixed_area", results = { @Result(name = "success", location = "./pages/base/fixed_area.html", type = "redirect") })
	public String addFixedArea() {
		fixedAreaService.AddFixedArea(fixedArea);
		return SUCCESS;
	}

	/**
	 * 定区信息的全查询+家条件
	 * 
	 * @return
	 */
	@Action(value = "findAll_fixed_area", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		// 获取到前台传过来的参数page rows
		// 参数封装
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<FixedArea> p = fixedAreaService.findAll(fixedArea, pageable);

		// 参数后处理
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("total", p.getTotalElements());
		map.put("rows", p.getContent());

		// 参数压栈
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}

	/**
	 * 定区域的全查询--不带分页的
	 * 
	 * @return
	 */
	@Action(value = "queryAll_fixedArea", results = { @Result(name = "success", type = "json") })
	public String queryAll() {
		List<FixedArea> list = fixedAreaService.queryAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}

	/**
	 * 获取到位关联的用户信息
	 */
	@Action(value="getNoAssociateCustomer",results={@Result(name="success",type="json")})
	public String getNoAssociateCustomer(){
		Collection<? extends Customer> list = WebClient.create("http://localhost:9998/crm_management/service/customerService/noAssociateFixedArea").accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		
		//将数据压栈
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	/**
	 * 获取到当前区域已关联的用户信息
	 * @return
	 */
	@Action(value = "getHasAssociateCustomer", results = { @Result(name = "success", type = "json") })
	public String getHasAssociateCustomer() {
		String fixedAreaId = ServletActionContext.getRequest().getParameter("id");
		Collection<? extends Customer> list = WebClient.create("http://localhost:9998/crm_management/service/customerService/hasAssociateFixedArea?fixedAreaId="+fixedAreaId).accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		
		//将数据压栈
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	@Action(value="decidedzone_assigncustomerstodecidedzone",results={@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
	public String updateNoAssociateToHasAssociateCustomer(){
		//首先接受页面表单传输过来的数据
		HttpServletRequest request = ServletActionContext.getRequest();
		String[] customerIds = request.getParameterValues("customerIds");
		String customerIdStr = StringUtils.join(customerIds, ",");
		System.out.println("*******************************************8");
		System.out.println("customerIdStr"+customerIdStr);
		System.out.println("*******************************************8");
		//获取fixedAreaId
		String fixedAreaId = fixedArea.getId();
		System.out.println("fixedAreaId"+fixedAreaId);
		WebClient.create("http://localhost:9998/crm_management/service/customerService/updateNoAssociateFixedAreaTOHasAssociateFixedArea/"+customerIdStr+"/"+fixedAreaId).post(NONE);
		return  SUCCESS;
	}
	
	
	
	

	// 提供gettet和setter方法
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}
