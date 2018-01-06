package com.itheima.domain.base;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ActiveStateCustomer")
public class ActiveStateCustomer {

	//激活状态反馈
	private String msg;
	
	public ActiveStateCustomer() {
		// TODO Auto-generated constructor stub
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ActiveStateCustomer [msg=" + msg + "]";
	}
	
	
	
	
	
}
