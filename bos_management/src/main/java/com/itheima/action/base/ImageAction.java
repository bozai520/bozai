package com.itheima.action.base;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.domain.take_delivery.Promotion;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImageAction extends ActionSupport implements ModelDriven<Promotion> {

	//模型驱动
	private Promotion promotion = new Promotion();
	@Override
	public Promotion getModel() {
		// TODO Auto-generated method stub
		return promotion;
	}
	
	
	//上传图片
	private File imageFile;
	private String imageFileFileName;
	private String imageFileContentType;
	
	/**
	 * 上传图片
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	@Action(value="image_upload",results={@Result(name="success",type="json")})
	public String uploadImage() throws IOException{
		
		//获取到绝对路径
		//获取到的是项目名称
		//获取到绝对路径
		String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
		System.out.println("我是绝对路径：：：："+realPath);
		String contextPath = ServletActionContext.getRequest().getContextPath()+"/upload/";		
		System.out.println("我是相对路径"+contextPath);
		
		//生成随机的文件名称
		String uuid = UUID.randomUUID().toString();
		
		String ext = imageFileFileName.substring(imageFileFileName.lastIndexOf("."));
		
		//生成的文件名：
		String filename = uuid + ext;
		
		//获取到保存路径
		String savePath = realPath +"/"+ filename;
		
		//或者直接获取到File对象
		File file = new File(realPath, filename);
		
		//文件的上传保存
		FileUtils.copyFile(imageFile, file);
		
		//通知浏览器文件上传成功
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return SUCCESS;
	}
	
	
	//提供getter和setter方法
	public File getImageFile() {
		return imageFile;
	}
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}
	public String getImageFileFileName() {
		return imageFileFileName;
	}
	public void setImageFileFileName(String imageFileFileName) {
		this.imageFileFileName = imageFileFileName;
	}
	public String getImageFileContentType() {
		return imageFileContentType;
	}
	public void setImageFileContentType(String imageFileContentType) {
		this.imageFileContentType = imageFileContentType;
	}
	
	

}
