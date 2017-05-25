package com.bw.fit.common.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

public class LogUser {

	private String user_id ;
	@NotEmpty(message="{user.login.cd}")
	private String user_cd ;
	private String user_name;
	@NotEmpty(message="{user.login.pwd}")
	private String passwd;
	private String pwd_mm;
	private String ip; 
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getPwd_mm() {
		return pwd_mm;
	}
	public void setPwd_mm(String pwd_mm) {
		this.pwd_mm = pwd_mm;
	}
	public String getUser_cd() {
		return user_cd;
	}
	public void setUser_cd(String user_cd) {
		this.user_cd = user_cd;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	
}
