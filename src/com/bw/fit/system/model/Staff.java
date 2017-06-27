package com.bw.fit.system.model;

import org.hibernate.validator.constraints.NotEmpty;

import com.bw.fit.common.model.BaseModel;
import com.bw.fit.common.util.PropertiesUtil;

public class Staff extends BaseModel{

	@NotEmpty(message="登录帐号不得为空")
	private String staff_number;
	@NotEmpty(message="用户姓名不得为空")
	private String staff_name;
	private String password; // 密文
	private String passwd = PropertiesUtil.getValueByKey("system.passwd");  // 默认的明码密码
	private String phone;
	private String state ;
	private String address;
	private String company_id ;
	private String company_name; 
	private String role_id ;
	private String staff_group_id ;
	private String postion_id ;
	
	
	
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getStaff_group_id() {
		return staff_group_id;
	}
	public void setStaff_group_id(String staff_group_id) {
		this.staff_group_id = staff_group_id;
	}
	public String getPostion_id() {
		return postion_id;
	}
	public void setPostion_id(String postion_id) {
		this.postion_id = postion_id;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getStaff_number() {
		return staff_number;
	}
	public void setStaff_number(String staff_number) {
		this.staff_number = staff_number;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
