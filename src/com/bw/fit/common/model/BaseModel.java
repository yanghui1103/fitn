package com.bw.fit.common.model;

import static com.bw.fit.common.util.PubFun.getUUID;

public class BaseModel {

	private String fdid = getUUID();
	private String keyWords="-9";	
	private String start_date="1900-01-01" ;
	private String end_date="3900-12-12" ;
	private String create_time ;
	private String version_time ;
	private String creator;
	private String creator_name;
	private String create_company_id;
	private String create_company_name;
	public String getFdid() {
		return fdid;
	}
	public void setFdid(String fdid) {
		this.fdid = fdid;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getVersion_time() {
		return version_time;
	}
	public void setVersion_time(String version_time) {
		this.version_time = version_time;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreator_name() {
		return creator_name;
	}
	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}
	public String getCreate_company_id() {
		return create_company_id;
	}
	public void setCreate_company_id(String create_company_id) {
		this.create_company_id = create_company_id;
	}
	public String getCreate_company_name() {
		return create_company_name;
	}
	public void setCreate_company_name(String create_company_name) {
		this.create_company_name = create_company_name;
	}	
	
	
}