package com.itheima.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

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

import com.itheima.domain.base.Area;
import com.itheima.domain.base.FixedArea;
import com.itheima.domain.base.SubArea;
import com.itheima.service.base.AreaService;
import com.itheima.service.base.FixedAreaService;
import com.itheima.service.base.SubAreaService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
//action的注入
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SubAreaAction extends ActionSupport implements ModelDriven<SubArea> {

	//模型驱动
	private SubArea subArea = new SubArea();
	@Override
	public SubArea getModel() {
		return subArea;
	}
	
	//注入一个SubAreaService的服务对象
	@Autowired
	private SubAreaService subAreaService;
	
	@Autowired 
	private AreaService areaService;
	
	@Autowired
	private FixedAreaService fixedAreaService;
	
	
	//导入文件需要三个成员变量
	private File file;
	private String fileFileName;
	
	//EasyUI自带的分页参数的传递
	private int page;
	private int rows;
	
	
	
	/**
	 * 添加分区的相关信息
	 * @return
	 */
	@Action(value="save_subArea",results={@Result(name="success",type="redirect",location="./pages/base/sub_area.html")})
	public String saveSubArea(){
		//获取到区域对象,根据前台页面传送过来的省市区查询该区域的对象
		Area queryArea = areaService.findByArea(subArea.getArea());
		System.out.println("queryArea"+queryArea+"**********************");
		
		//获取到当前的定区的对象
		System.out.println("***********************************");
		System.out.println(subArea.getFixedArea().getId());
		System.out.println("***********************************");
		
		FixedArea queryFixedArea = fixedAreaService.findByFixedArea(subArea.getFixedArea());
		
		subArea.setFixedArea(queryFixedArea);
		
		subArea.setArea(queryArea);
		
		subAreaService.addSubArea(subArea);
		return SUCCESS;
	}
	
	
	/**
	 * 文件导入
	 */
	@Action(value="subArea_import")
	public void doImport(){
		
		//文件导入使用poi来进行文件的导入
		Workbook workbook = null;
		
		//创建一个List集合用来封装数据
		List<SubArea> list = new ArrayList<SubArea>();
		
		//判断和限制上传文件的格式
		try {
			if(fileFileName.endsWith(".xls")){
				workbook = new HSSFWorkbook(new FileInputStream(file));
			}else if(fileFileName.endsWith(".xlsx")){
				workbook = new XSSFWorkbook(new FileInputStream(file));
			}
			//获取到sheet页,当前的测试数据只有一个sheet页
			Sheet sheet = workbook.getSheetAt(0);
			
			//获取到sheet页中的行数
			int count = sheet.getLastRowNum();
			
			//遍历获取数据
			for (int i = 1; i <= count; i++) {
				Row row = sheet.getRow(i);
				//分区编号	定区编码	区域编码	关键字	起始号	结束号	单双号	位置信息

				String id = row.getCell(0).getStringCellValue();
				String fixedAreaId = row.getCell(1).getStringCellValue();
				//封装一个对象
				FixedArea fixedArea = new FixedArea();
				fixedArea.setId(fixedAreaId);
				
				String areaId = row.getCell(2).getStringCellValue();
				//再疯转一个对象
				Area area = new Area();
				area.setId(areaId);
				String keyWords = row.getCell(3).getStringCellValue();
				String startNum = row.getCell(4).getStringCellValue();
				String endNum = row.getCell(5).getStringCellValue();
				Character single = row.getCell(6).getStringCellValue().charAt(0);//单双号是Character
				String assistKeyWords = row.getCell(7).getStringCellValue();
				
				
				
				SubArea subArea = new SubArea(id, startNum, endNum, single, keyWords, assistKeyWords, area, fixedArea);
				list.add(subArea);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//调用业务：将数据保存到数据库中
		subAreaService.addSubAreaBath(list);
		
	}
	
	/**
	 * 分区数据的导出
	 */
	@Action(value="subArea_export")
	public String doExport(){
		
		//导出之前需要查询所有的分区数据
		List<SubArea> list = subAreaService.queryAll();
		
		//获取到数据的总记录
		int count = list.size();
		
		//每页数据显示的记录数
		int pageSize = 30;
		
		//获取到总共的页数
		int countPage = (count%pageSize) == 0 ? (count/pageSize) :((count/pageSize)+1);
		//实现数据的导出
		//第一步创建Excel的工作簿
		Workbook workbook = new HSSFWorkbook();
		
		//创建sheet页
		for (int i = 0; i < countPage; i++) {
			//创建sheet页
			Sheet sheet = workbook.createSheet("分区数据"+i);
			
			Row row = sheet.createRow(0);
			//分区编号	定区编码	区域编码	关键字	起始号	结束号	单双号	位置信息
			//设置标题行
			row.createCell(0).setCellValue("分区编号");
			row.createCell(1).setCellValue("定区编码");
			row.createCell(2).setCellValue("区域编码");
			row.createCell(3).setCellValue("关键字");
			row.createCell(4).setCellValue("起始号");
			row.createCell(5).setCellValue("结束号");
			row.createCell(6).setCellValue("单双号");
			row.createCell(7).setCellValue("位置信息");
			
			for (int j = i*pageSize; j < ((i+1)*pageSize); j++) {
				
				if(j>=count){
					break;
				}
			
				//创建数据的数据行
				Row row1 = sheet.createRow(sheet.getLastRowNum()+1);
				
				//给每一行的每一个cell写入数据
				row1.createCell(0).setCellValue(list.get(j).getId());
				row1.createCell(1).setCellValue(list.get(j).getFixedArea().getId());
				row1.createCell(2).setCellValue(list.get(j).getArea().getId());
				row1.createCell(3).setCellValue(list.get(j).getKeyWords());
				row1.createCell(4).setCellValue(list.get(j).getStartNum());
				row1.createCell(5).setCellValue(list.get(j).getEndNum());
				row1.createCell(6).setCellValue(list.get(j).getSingle());
				row1.createCell(7).setCellValue(list.get(j).getAssistKeyWords());
			}
		}
		
		
		try {
			//将数据写会到前台下载
			//一个流两个头
			String filename = "subArea.xls";
			
			//获取到一个MimeType
			String mimeType = ServletActionContext.getServletContext().getMimeType(filename);
			
			//获取到一个流
			ServletOutputStream os = ServletActionContext.getResponse().getOutputStream();
			
			//设置第一个头
			ServletActionContext.getResponse().setContentType(mimeType);
			
			//设置第二个头
			ServletActionContext.getResponse().setHeader("content-disposition", "attachment;filename="+filename);
			
			workbook.write(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NONE;
	}
	
	
	/**
	 * 数据的全查询带分页
	 * @return
	 */
	@Action(value="subArea_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		//调用业务逻辑
		Page<SubArea> p = subAreaService.findAll(subArea,page,rows);
		
		//参数的后处理
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", p.getTotalElements());
		map.put("rows", p.getContent());
		
		//数据的压栈操作
		ActionContext.getContext().getValueStack().push(map);
		
		return SUCCESS;
	}
	/**
	 * 不带分页的数据全查询
	 * @return
	 */
	@Action(value="queryAll_subArea",results={@Result(name="success",type="json")})
	public String queryAll(){
		List<SubArea> list = subAreaService.queryAll();
		
		//数据压栈处理
		ActionContext.getContext().getValueStack().push(list);
		
		return SUCCESS;
	}
	
	
	
	
	
	
	//提供setter方法
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
