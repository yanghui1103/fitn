package com.bw.fit.leave.service.impl;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.RbackException;
import com.bw.fit.leave.model.LeaveModel;
import com.bw.fit.leave.service.LeaveService;
@Service
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private CommonDao commonDao;
	@Override
	public void createLeaveForm(LeaveModel m) throws RbackException {
		// TODO Auto-generated method stub
		commonDao.insert("leave.createLeaveForm", m); 
	}

}
