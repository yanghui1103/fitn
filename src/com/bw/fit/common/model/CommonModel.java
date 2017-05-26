package com.bw.fit.common.model;
import static com.bw.fit.common.util.PubFun.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonModel extends BaseModel implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private String staff_number;
	private String staff_name;
	private String password;
	private String passwd;
	private String phone;
	private String state ;
	private String address;

	private String postion_name;
	private String desp;
	private String role_name;
	private String parent_id ;
	
	
	
	
	
	
	public String getPostion_name() {
		return postion_name;
	}
	public void setPostion_name(String postion_name) {
		this.postion_name = postion_name;
	}
	public String getDesp() {
		return desp;
	}
	public void setDesp(String desp) {
		this.desp = desp;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
