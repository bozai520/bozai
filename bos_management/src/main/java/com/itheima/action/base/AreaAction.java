package com.itheima.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import javassist.expr.NewArray;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONArray;
import com.itheima.domain.base.Area;
import com.itheima.service.base.AreaService;
import com.itheima.utils.base.PinYin4jUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends ActionSupport implements ModelDriven<Area>{

	//模型驱动
	private Area area = new  Area();
	@Override
	public Area getModel() {
		// TODO Auto-generated method stub
		return area;
	}
	
	//注入宇哥AreaService的对象 
	@Autowired
	private AreaService areaService;
	
	
	//文件上上传需要的三个参数
	private File file;
	private String fileFileName;
	private String fileContentType;
	
	
	/**
	 * 添加区域信息
	 * @return
	 */
	@Action(value="save_area",results={@Result(name="success",location="./pages/base/area.html",type="redirect")})
	public String addArea(){
		areaService.addArea(area);
		return "success";
	}
	
	
	/**
	 * 区域信息的全查询--带分页的查询
	 * 
	 * @return
	 */
	//接受页面传输过来的两个数据
	private int page;
	private int rows;
	
	@Action(value="findAll_area",results={@Result(name="success",type="json")})
	public String findAll(){
		
		
		 Page<Area> p = areaService.findAll(area, page, rows);
		 
		 //对查出的数据进行处理
		 Map<String,Object> map = new HashMap<String, Object>();
		 
		 map.put("total", p.getTotalElements());
		 map.put("rows", p.getContent());
		
		//将数据压栈
		ActionContext.getContext().getValueStack().push(map);
		return "success";
	}
	
	/**
	 * 区域信息--不带分页的全查询
	 * @return
	 */
	@Action(value="queryAll_area",results={@Result(name="success",type="json")})
	public String queryAll(){
		List<Area> list = areaService.queryAll();
		
		//直接将数据压栈
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	
	/**
	 * 文件上传操作
	 * 
	 */
	@Action(value="areaFile_upload")
	public void uploadFile(){
			
		//创建一个List集合
		List<Area> list = new ArrayList<Area>();	
		Workbook workbook = null;
		try {
			
			if(fileFileName.endsWith(".xls")){
				workbook = new HSSFWorkbook(new FileInputStream(file));
			}else if(fileFileName.endsWith(".xlsx")){
				workbook = new HSSFWorkbook(new FileInputStream(file));
			}
			//第一步创建以工作簿,加载文件：针对xls文件的上传
			//第二步：获取到sheet页
			Sheet sheet = workbook.getSheetAt(0);
			//第三步：遍历sheet获取到row
			for (int i = 1; i < sheet.getLastRowNum(); i++) {
				
				Row row = sheet.getRow(i);
				
				String id = row.getCell(0).getStringCellValue();
				String province = row.getCell(1).getStringCellValue();
				
				//生成省份的简码
				String[] str = PinYin4jUtils.getHeadByString(province);
				String shortcode = "";
				//拼接
				for (int j = 0; j < str.length; j++) {
					shortcode+=str[j];
				}
				String city = row.getCell(2).getStringCellValue();
				
				//生成市的简码
				String citycode = PinYin4jUtils.hanziToPinyin(city);
				
				String district = row.getCell(3).getStringCellValue();
				String postcode = row.getCell(4).getStringCellValue();
				
				Area area = new Area(id, province, city, district, citycode, shortcode);
				list.add(area);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		areaService.saveBath(list);
	};
	
	

	/**
	 * 数据导出操作
	 * @throws IOException 
	 */
	@Action(value="area_export")
	public String doExport(){
		
		//区域信息不分页的全查询,获取到全部的区域信息
		List<Area> list = areaService.queryAll();
		//将数据分为多个sheet页
		int pageSize = 30;
		int totalCount = list.size();
		
		//获取到数据导出时所分出的sheet页数
		int countPage = (totalCount%pageSize)==0 ? totalCount/pageSize : (totalCount/pageSize)+1;
		//实现数据的导出
		//第一步：创建一个空的工作簿,
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		
		for (int i = 0; i < countPage; i++) {
				//第二步：创建一个sheet页
				HSSFSheet sheet = workbook.createSheet("区域数据"+i);
				
				
				//每个sheet页的第一行时一个标题栏
				HSSFRow row = sheet.createRow(0);
				
				row.createCell(0).setCellValue("区域编号");
				row.createCell(1).setCellValue("省份");
				row.createCell(2).setCellValue("城市");
				row.createCell(3).setCellValue("区域");
				row.createCell(4).setCellValue("邮编");
				
				for (int j = i*pageSize; j < ((i+1)*pageSize); j++) {
					
					if(j>=list.size()){
						break;
					}
					
					HSSFRow row1 = sheet.createRow(sheet.getLastRowNum()+1);
					
					row1.createCell(0).setCellValue(list.get(j).getId());
					row1.createCell(1).setCellValue(list.get(j).getProvince());
					row1.createCell(2).setCellValue(list.get(j).getCity());
					row1.createCell(3).setCellValue(list.get(j).getDistrict());
					row1.createCell(4).setCellValue(list.get(j).getPostcode());
				}
			}
		
			try {
				//将数据Exxcel文件写到前台去，提供下载的方式
				String filename = "Area.xls";
				
				//获取到一个输出流::::::一个流两个头
				
				//获取到一个流
				ServletOutputStream os = ServletActionContext.getResponse().getOutputStream();
				
				//设置两个头文件
				//根据文件名称获取大mimeType的类型
				String mimeType = ServletActionContext.getServletContext().getMimeType(filename);
				
				//设置mimeType的类型：设置响应打开文件的格式
				ServletActionContext.getResponse().setContentType(mimeType);	
				
				//设置另外一个头文件
				ServletActionContext.getResponse().setHeader("content-disposition", "attachment;filename="+filename);
				
				workbook.write(os);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return NONE;
	}
	
	/**
	 * 获取到所有的省份
	 * 
	 * @return
	 */
	@Action(value="queryAll_areaProvince",results={@Result(name="success",type="json")})
	public String queryProvince(){
		List<String> list = areaService.findProvince();
		List<Map<String,String>> lst = new ArrayList<Map<String,String>>();
		//对数据处理
		for (String pro : list) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("province", pro);
			lst.add(map);
		}
		
		//数据压栈操作
		ActionContext.getContext().getValueStack().push(lst);
		return SUCCESS;
	}
	
	/**
	 * 获取到所有的城市
	 * 
	 * @return
	 */
	@Action(value="queryAll_areaCity",results={@Result(name="success",type="json")})
	public String queryCity(){
		
		
		try {
			//接受请求参数
			String str = ServletActionContext.getRequest().getParameter("province");
			String province = new String(str.getBytes("ISO-8859-1"),"UTF-8");
			
			List<String> list = areaService.findCity(province);
			List<Map<String,String>> lst = new ArrayList<Map<String,String>>();
			//对数据处理
			for (String city : list) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("city", city);
				lst.add(map);
			}
			
			//数据压栈操作
			ActionContext.getContext().getValueStack().push(lst);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return SUCCESS;
	}
	
	/**
	 * 获取到所有的区域
	 * 
	 * @return
	 */
	@Action(value="queryAll_areaDistrict",results={@Result(name="success",type="json")})
	public String queryDistrict(){
		
		try {
			//接受请求参数
			String str = ServletActionContext.getRequest().getParameter("province");
			String province = new String(str.getBytes("ISO-8859-1"),"UTF-8");
			
			String str1 = ServletActionContext.getRequest().getParameter("city");
			String city = new String(str1.getBytes("ISO-8859-1"),"UTF-8");
			
			List<String> list = areaService.findDistrict(province,city);
			List<Map<String,String>> lst = new ArrayList<Map<String,String>>();
			//对数据处理
			for (String district : list) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("district", district);
				lst.add(map);
			}
			
			//数据压栈操作
			ActionContext.getContext().getValueStack().push(lst);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	
		
	//为属性添加setter方法
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

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
