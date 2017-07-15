package com.bw.fit.leave.service;

import org.json.simple.JSONObject;

import com.bw.fit.common.model.RbackException;
import com.bw.fit.leave.model.LeaveModel;

public interface LeaveService {

	public void createLeaveForm(LeaveModel m) throws RbackException;
}
