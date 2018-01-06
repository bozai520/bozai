package com.itheima.service.base;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;






import com.itheima.domain.base.ActiveStateCustomer;
import com.itheima.domain.base.Customer;

/**
 * 客户信息的的服务
 * @author Administrator
 *
 */
@Produces("*/*")
public interface CustomerService {
	
	/**
	 * 获取到当前没有关联到定区的用户
	 * @return
	 */
	@GET
	@Path("/noAssociateFixedArea")
	@Produces({"application/json","application/xml"})
	public List<Customer> findNoAssociateFixedArea();
	
	
	/**
	 * 获取到已经被关联到该定区的用户信息
	 * @return
	 */
	@GET
	@Path("/hasAssociateFixedArea/")
	@Produces({"application/json","application/xml"})
	public List<Customer> findHasAssociateFixedArea(@QueryParam("fixedAreaId") String fixedAreaId);
	
	/**
	 * 将前台传输过来的关联到的用户信息更新到数据库中
	 * 
	 * @param customerIdStr
	 * @param fixedAreaId
	 */
	@POST
	@Path("/updateNoAssociateFixedAreaTOHasAssociateFixedArea/{customerIdStr}/{fixedAreaId}")
	public void updateNoAssociateFixedAreaTOHasAssociateFixedArea(
		@PathParam("customerIdStr")	String customerIdStr,@PathParam("fixedAreaId") String fixedAreaId);
	
	/**
	 * 注册用户信息
	 * @param customer
	 */
	@PUT
	@Path("/customer/addCustomer")
	@Consumes("application/json")
	public void addCustomer(Customer customer);
	
	/**
	 * 激活用户信息
	 * @return
	 */
	@GET
	@Path("/customer/activeCustomer")
	@Produces("application/json")
	public ActiveStateCustomer activeCustomer(@QueryParam("telephone") String telephone);
}
