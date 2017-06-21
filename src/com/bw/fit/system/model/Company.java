package com.bw.fit.system.model;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

public class Company {
	@NotEmpty(message="组织名称不能为空")
	private String company_name;
	private String company_address; 
	private String company_type_id ;  
	private String parent_company_id;
	@Min(value=0,message="序号不得小于零")
	private int company_order;
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_address() {
		return company_address;
	}
	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}
	public String getCompany_type_id() {
		return company_type_id;
	}
	public void setCompany_type_id(String company_type_id) {
		this.company_type_id = company_type_id;
	}
	public String getParent_company_id() {
		return parent_company_id;
	}
	public void setParent_company_id(String parent_company_id) {
		this.parent_company_id = parent_company_id;
	}
	public int getCompany_order() {
		return company_order;
	}
	public void setCompany_order(int company_order) {
		this.company_order = company_order;
	}
	
	
	
	
}
