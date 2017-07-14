package com.bw.fit.leave.controller;

import java.util.HashMap;

import static com.bw.fit.common.util.PubFun.*;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.common.util.AjaxBackResult;
import com.bw.fit.flow.model.CommonTransferModel; 
import com.bw.fit.flow.service.FlowCoreService;
import com.bw.fit.leave.model.LeaveModel;
import com.bw.fit.leave.service.LeaveService;
import com.bw.fit.system.service.SystemService;

@RequestMapping("system/leave")
@Controller
public class LeaveController {
	@Autowired
	private SystemService systemService;
	@Autowired
	private FlowCoreService flowCoreService ;
	@Autowired
	private LeaveService leaveService ;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private ProcessEngine processEngine;

	@RequestMapping("leaveFlowListPage/{params}")
	public String leaveFlowListPage(){
		
		return "leave/leaveFlowListPage";
	}
	
	@RequestMapping("openCreateLeavelPage")
	public String openCreateLeavelPage(){
		
		return "leave/createLeavePage";
	}
	
	@RequestMapping( value = "createLeave", method = RequestMethod.POST)
	public ModelAndView createLeave(@ModelAttribute LeaveModel md,HttpSession session,Model model){
		JSONObject json = new JSONObject();
		CommonTransferModel c = new CommonTransferModel();
		AjaxBackResult a = new AjaxBackResult();
		
		// Deployment d = flowCoreService.deployFlowResource("com/bw/fit/leave/bpmn/leaveFlow.bpmn");
		
		Map<String,Object> vars = new HashMap<>();
		c.setFdid(getUUID());
		LogUser u  =(LogUser)session.getAttribute("LogUser");
		c.setLogUser(u);
		vars.put("user1", "p1");
		vars.put("users2", "p2,p23,p11");
		vars.put("user3", "p3");
		vars.put("user4", "p4");
		vars.put("persons", "p133,p134,p135,p136");
		vars.put("LogUser", u);		

		md.setFdid(c.getFdid());
		systemService.fillCommonField(md, session, false);
		vars.put("formModel", md);
		
		ProcessInstance pi = flowCoreService.startProcessInstanceByKey("leaveFlow", vars);
		leaveService.createLeaveForm(md);
		if(pi==null){
			json.put("res", "1");
			json.put("msg", "发生错误");
			return a.returnAjaxBack(json);
		}
		json.put("res", "2");
		json.put("msg", "成功");
		return a.returnAjaxBack(json);
	}
}
