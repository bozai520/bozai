package com.itheima.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.domain.base.Standard;
import com.itheima.service.base.StandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

	//模型驱动
	private Standard standard = new Standard();
	@Override
	public Standard getModel() {
		// TODO Auto-generated method stub
		return standard;
	}
	
	//注入一个Standardservice对象
	@Autowired 
	private StandardService standardService;
	
	//添加准则
	@Action(value="save_standard",results={@Result(name="success",location="./pages/base/standard.html",type="redirect")})
	public String addStandard(){
		standardService.addStandard(standard);
		return "success";
	}
	
	

	@Action(value="findAll_standard",results=@Result(name="success",type="json"))
	public String findAll(){
			List<Standard> list = standardService.findAll();
			
			//将数据压栈
			ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}

}
