package com.bw.fit.leave.model;

import java.io.Serializable;

import com.bw.fit.common.model.BaseModel;

public class LeaveModel  extends BaseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private String leave_reason ;
	private int days ;
	public String getLeave_reason() {
		return leave_reason;
	}
	public void setLeave_reason(String leave_reason) {
		this.leave_reason = leave_reason;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	
}
