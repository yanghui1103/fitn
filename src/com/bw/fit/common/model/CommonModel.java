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
	
	private String menu_name;
	private String menu_path;
	private String menu_url; 
	private String menu_level ;
	private String iscontrol ;
	private String default_action ;
	private String params ;
	
	
	
	
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getDefault_action() {
		return default_action;
	}
	public void setDefault_action(String default_action) {
		this.default_action = default_action;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public String getMenu_path() {
		if(null==menu_path||"".equals(menu_path))
			return "-9";
		return menu_path;
	}
	public void setMenu_path(String menu_path) {
		this.menu_path = menu_path;
	}
	public String getMenu_url() {
		return menu_url;
	}
	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}
	public String getMenu_level() {
		return menu_level;
	}
	public void setMenu_level(String menu_level) {
		this.menu_level = menu_level;
	}
	public String getIscontrol() {
		return iscontrol;
	}
	public void setIscontrol(String iscontrol) {
		this.iscontrol = iscontrol;
	}
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
