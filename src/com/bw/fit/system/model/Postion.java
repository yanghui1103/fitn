package com.bw.fit.system.model;

import java.io.Serializable;

import com.bw.fit.common.model.BaseModel;

public class Postion extends BaseModel implements Serializable{

	private static final long serialVersionUID = 11L;
	private String postion_name;
	private String desp;
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
	
}
