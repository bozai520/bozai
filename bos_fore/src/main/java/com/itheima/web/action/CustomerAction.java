package com.itheima.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.itheima.domain.base.ActiveStateCustomer;
import com.itheima.domain.base.Customer;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressWarnings("serial")
public class CustomerAction extends ActionSupport implements
		ModelDriven<Customer> {
	
	
	//注入一个模板对象
	@Autowired
	private JmsTemplate jmsQueueTemplate;

	// 注入一个模型驱动
	private Customer customer = new Customer();

	@Override
	public Customer getModel() {
		return customer;
	}

	// 注入一个redis缓存对象的模板
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 短信验证码发送
	 * 
	 */
	@Action(value = "customer_sendSms")
	public void sendNote() {
		// 没有注册账号，只好模拟衣蛾发送的验证码
		String checkCode = RandomStringUtils.randomNumeric(4);
		// 将获取到的验证码存储在sesion中
		ServletActionContext.getRequest().getSession()
				.setAttribute("checkCode", checkCode);
		System.out.println("发送的短信验证码为：：：：：" + checkCode);
	}

	// 获取到前台的短信验证码
	private String checkCode;

	/**
	 * 注册用户信息
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value = "resign_customer", results = { @Result(name = "success", location = "./signup-success.html", type = "redirect") })
	public String resignCustomer() throws IOException {
		// 接受表单的参数验证码
		String checkCode = ServletActionContext.getRequest().getParameter(
				"checkCode");
		// 其他参数直接通过模型驱动获取

		// 验证短信验证是否正确
		// 1.获取到session中存储到的短信验证码
		String code = (String) ServletActionContext.getRequest().getSession()
				.getAttribute("checkCode");
		// 2.判断两个验证码是否相同
		if (!code.equalsIgnoreCase(checkCode)) {
			ServletActionContext.getResponse().getWriter().print("验证码错误");
			return NONE;
		}

		// 调用远程访问服务，对数据进行添加操作
		WebClient
				.create("http://localhost:9998/crm_management/service/customerService/customer/addCustomer")
				.type(MediaType.APPLICATION_JSON).put(customer);
		// 在进行保存操作之后进行实现页面发送激活邮件
		String activeCode = RandomStringUtils.randomNumeric(8);

		// 在激活码产生将激活码储存在redires缓冲中
		// 如何将激活码存储到redis缓存中、调用业务逻辑将激活码和用户的手机号保存到redis缓存中
		String telephone = customer.getTelephone();
		redisTemplate.opsForValue().set(telephone, activeCode, 24,
				TimeUnit.HOURS);
			
		final String url = "aaa@123.com";
		String activeUrl = "http://localhost:9997/bos_fore/customer_activeCustomer.action";
		final String context = "尊敬的用户您好，请在24小时以内进行账号的邮箱绑定</br><a href='" + activeUrl
				+ "?activeCode=" + activeCode + "&telephone="
				+ customer.getTelephone() + "'>速运快递邮箱绑定</a>";
		/**
		 * ********************************************************
		 * MQ实现发送 邮件
		 * ********************************************************
		 */
		jmsQueueTemplate.send("bos_em", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("url", url);
				mapMessage.setString("context", context);
				return mapMessage;
			}
		});
		return SUCCESS;
	}

	/**
	 * 激活账号信息,通过邮件连接发送过来的数据进行激活
	 * 
	 * @return
	 */
	@Action(value = "customer_activeCustomer", results = { @Result(name = "success", location = "./activeSuccess.html", type = "redirect"),
			@Result(name="input",location="./activeError.html",type="redirect")})
	public String activeCustomer() {
		// 当客户点击后发送请求过来后，首先判断在redis缓存中查找是否存在
		// 获取到连接的参数
		// 手机号
		String tel = ServletActionContext.getRequest().getParameter("telephone");
		// 激活码
		String checkCode = ServletActionContext.getRequest().getParameter("activeCode");
		// 根据上面的参数获取到当前缓存中是否存在这样对应的一对数据
		// 在redis中进行数据查询
		 String code = redisTemplate.opsForValue().get(tel);
		System.out.println(code);
		 System.out.println(tel);
		 ActiveStateCustomer stateCustomer = new ActiveStateCustomer();
		  //判断是否一致 if(StringUtils.isNoneBlank(checkCode)){
		  if(checkCode.equals(code)){ 
			  //如果相等则说明存在这样的一个用户的tel和验证码 //向Customer发请求
			  stateCustomer = WebClient.create("http://localhost:9998/crm_management/service/customerService/customer/activeCustomer?telephone="+tel).accept(MediaType.APPLICATION_JSON).get(ActiveStateCustomer.class); 
		  }else{
			  stateCustomer.setMsg("激活失败，激活码可能已经过期");
		  }
		return SUCCESS;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

}
