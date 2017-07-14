package com.bw.fit.leave.service;

import org.json.simple.JSONObject;

import com.bw.fit.leave.model.LeaveModel;

public interface LeaveService {

	public JSONObject createLeaveForm(LeaveModel m);
}
