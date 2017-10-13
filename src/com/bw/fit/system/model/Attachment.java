package com.bw.fit.system.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.bw.fit.common.model.BaseModel;

public class Attachment extends BaseModel implements Serializable{
	private static final long serialVersionUID = 74458L;
	private String file_name;
	@NotEmpty(message="文件名不得为空")
	private String before_name;
	private String foreign_id;
	private String path ;
	private double file_size ; 
	private List<MultipartFile> MultipartFiles ;
	private List<File> files ;
	private String create_company_id ;
	
	
	public String getCreate_company_id() {
		return create_company_id;
	}
	public void setCreate_company_id(String create_company_id) {
		this.create_company_id = create_company_id;
	}
	public String getForeign_id() {
		return foreign_id;
	}
	public void setForeign_id(String foreign_id) {
		this.foreign_id = foreign_id;
	}
	
	public List<MultipartFile> getMultipartFiles() {
		return MultipartFiles;
	}
	public void setMultipartFiles(List<MultipartFile> multipartFiles) {
		MultipartFiles = multipartFiles;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getBefore_name() {
		return before_name;
	}
	public void setBefore_name(String before_name) {
		this.before_name = before_name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public double getFile_size() {
		return file_size;
	}
	public void setFile_size(double file_size) {
		this.file_size = file_size;
	}
	
	
	
}
