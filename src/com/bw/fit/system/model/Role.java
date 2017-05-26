package com.bw.fit.system.model;

import com.bw.fit.common.model.BaseModel;

public class Role extends BaseModel{

	private String role_name;
	private String parent_id ;
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
	
	
}
