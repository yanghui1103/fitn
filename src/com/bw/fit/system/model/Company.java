package com.bw.fit.system.model;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

public class Company {
	@NotEmpty(message="组织名称不能为空")
	private String company_name;
	private String company_address; 
	private String company_type_id ; 
	@NotEmpty(message="父组织不能为空")
	private String parent_company_id;
	@Min(value=0,message="序号不得小于零")
	private int company_order;
}
