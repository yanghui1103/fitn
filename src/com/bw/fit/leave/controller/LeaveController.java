package com.bw.fit.leave.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.flow.service.FlowCoreService;
import com.bw.fit.system.service.SystemService;

@RequestMapping("system/leave")
@Controller
public class LeaveController {
	@Autowired
	private SystemService systemService;
	@Autowired
	private FlowCoreService flowCoreService ;
	@Autowired
	private CommonDao commonDao;

	@RequestMapping("leaveFlowListPage/{params}")
	public String leaveFlowListPage(){
		
		return "leave/leaveFlowListPage";
	}
	
	@RequestMapping("openCreateLeavelPage")
	public String openCreateLeavelPage(){
		
		return "leave/createLeavePage";
	}
}
