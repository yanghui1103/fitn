package com.bw.fit.leave.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.bw.fit.common.util.PubFun.*;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;  
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
import com.bw.fit.common.model.RbackException;
import com.bw.fit.common.util.AjaxBackResult;
import com.bw.fit.common.util.PropertiesUtil;
import com.bw.fit.flow.model.CommonTransferModel; 
import com.bw.fit.flow.model.TodoTask;
import com.bw.fit.flow.service.FlowCoreService;
import com.bw.fit.flow.service.impl.ActivitiUtil;
import com.bw.fit.flow.service.impl.JumpBeforeNodeCmd;
import com.bw.fit.leave.model.LeaveModel;
import com.bw.fit.leave.service.LeaveService;
import com.bw.fit.system.service.SystemService;

@RequestMapping("system/leave")
@Controller
public class LeaveController {
	@Autowired
	private SystemService systemService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FlowCoreService flowCoreService ;
	@Autowired
	private LeaveService leaveService ;
	@Autowired
	private FormService formService ;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private ProcessEngine processEngine; 
	@Autowired
	private RuntimeService runtimeService ;
	
	@RequestMapping("leaveFlowListPage/{params}")
	public String leaveFlowListPage(Model model){
	
		
		List<Task> list = taskService.createTaskQuery().processInstanceId("72501").list();
		List<TodoTask> ls = new ArrayList<>();
		for(Task t:list){
			TodoTask tt = new TodoTask();
			List<HistoricIdentityLink> lst = flowCoreService.getDealersOfTheTask(t.getId());
			StringBuffer users = new StringBuffer();
				for(HistoricIdentityLink ht:lst){
					users.append(ht.getUserId());
				}
			tt.setAssignee(users.toString());
			LeaveModel m = ((LeaveModel)taskService.getVariable(t.getId(), "formModel"));
			tt.setTask_title(m.getLeave_reason()); 			
			tt.setUrl(PropertiesUtil.getValueByKey(t.getTaskDefinitionKey()));
			tt.setFdid(t.getId());
			ls.add(tt);
		}
		model.addAttribute("todolist", ls);
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
		
		//Deployment d = flowCoreService.deployFlowResource("com/bw/fit/leave/bpmn/leaveFlow2.bpmn");
		
		Map<String,Object> vars = new HashMap<>();
		c.setFdid(getUUID());
		LogUser u  =(LogUser)session.getAttribute("LogUser");
		c.setLogUser(u);
		vars.put("user1", "p1");
		vars.put("users2", "p2,p23,p11");
		vars.put("user3", "p3");
		vars.put("user4", "p4"); 
		vars.put("LogUser", u);		

		md.setFdid(c.getFdid());
		systemService.fillCommonField(md, session, false);
		vars.put("formModel", md);
 
		
		ProcessInstance pi = flowCoreService.startProcessInstanceByKey("leaveFlow2", vars);
		try {
			leaveService.createLeaveForm(md);
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(pi==null){
			json.put("res", "1");
			json.put("msg", "发生错误");
			return a.returnAjaxBack(json);
		}
		json.put("res", "2");
		json.put("msg", "成功");
		return a.returnAjaxBack(json); 
	}
	@RequestMapping("editLeavePage/{id}")
	public String editLeavePage(){
		return "leave/editLeavePage";
	}

	@RequestMapping("auditPage/{id}")
	public String auditPage(){
		return "leave/auditPage";
	}
	@RequestMapping("audit3Page/{id}")
	public String audit3Page(){
		return "leave/audit3Page";
	}
	@RequestMapping("audit4Page/{id}")
	public String audit4Page(){
		return "leave/audit4Page";
	}
	@RequestMapping("audit5Page/{id}")
	public String audit5Page(){
		return "leave/audit5Page";
	}
	@RequestMapping("audit7Page/{id}")
	public String audit7Page(){
		return "leave/audit7Page";
	}
	
	
	/***组长审批 **/
	@RequestMapping("updateLeave")
	public ModelAndView updateLeave(){
		JSONObject json = new JSONObject();
		CommonTransferModel c = new CommonTransferModel();
		AjaxBackResult a = new AjaxBackResult();
		List<Task> list = taskService.createTaskQuery().taskAssignee("p1").list();
		for(Task t:list){

			ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(t.getProcessInstanceId()).singleResult();  
	        //当前实例的执行到哪个节点  
	        String activitiId = execution.getActivityId();  

			taskService.setVariable(t.getId(), "activitiId", activitiId);
			
			flowCoreService.completeTask(t.getId(), null);
			System.out.println(t.getId());
		}
		json.put("res", "2");
		json.put("msg", "成功");
		return a.returnAjaxBack(json);
	}
	@RequestMapping("auditLeave12")
	public ModelAndView auditLeave12(){
		JSONObject json = new JSONObject();
		CommonTransferModel c = new CommonTransferModel();
		AjaxBackResult a = new AjaxBackResult();
		List<Task> list = taskService.createTaskQuery().taskCandidateUser("p23").list();
		for(Task t:list){
			//taskService.setVariable(t.getId(), "days", 12);
			//flowCoreService.cliamTaskToUser(t.getId(), "p23");
			
			Execution exe = runtimeService.createExecutionQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
			
			String activitiId = "usertask1";// taskService.getVariable(t.getId(), "activitiId").toString();
			TaskServiceImpl taskServiceImpl=(TaskServiceImpl)taskService;  
			taskServiceImpl.getCommandExecutor().execute(new JumpBeforeNodeCmd(exe.getId(),activitiId));  

			
			//flowCoreService.completeTask(t.getId(),null);
			System.out.println(t.getId());
		}
		json.put("res", "2");
		json.put("msg", "成功");
		return a.returnAjaxBack(json);
	}
	

	@RequestMapping("auditLeave1")
	public ModelAndView auditLeave1(){
		JSONObject json = new JSONObject();
		CommonTransferModel c = new CommonTransferModel();
		AjaxBackResult a = new AjaxBackResult();
		List<Task> list = taskService.createTaskQuery().taskCandidateUser("p23").list();
		for(Task t:list){
			taskService.setVariable(t.getId(), "days", 12);
			flowCoreService.cliamTaskToUser(t.getId(), "p23");
			
			flowCoreService.completeTask(t.getId(),null);
			System.out.println(t.getId());
		}
		json.put("res", "2");
		json.put("msg", "成功");
		return a.returnAjaxBack(json);
	}
	@RequestMapping("auditLeave3")
	public ModelAndView auditLeave3() throws Exception{ // 副科长审核
		JSONObject json = new JSONObject();
		CommonTransferModel c = new CommonTransferModel();
		AjaxBackResult a = new AjaxBackResult();
		List<Task> list = taskService.createTaskQuery().taskAssignee("p3").list();
		for(Task t:list){
			//ActivitiUtil.taskRollBack(t.getId());
			flowCoreService.completeTask(t.getId(), null);
			System.out.println(t.getId());
		}
		json.put("res", "2");
		json.put("msg", "成功");
		return a.returnAjaxBack(json);
	}
	
	@RequestMapping("auditLeave4")
	public ModelAndView auditLeave4() throws Exception{ // kezhang审核
		JSONObject json = new JSONObject();
		CommonTransferModel c = new CommonTransferModel();
		AjaxBackResult a = new AjaxBackResult();
		List<Task> list = taskService.createTaskQuery().taskAssignee("p4").list();
		for(Task t:list){

//			Execution exe = runtimeService.createExecutionQuery().processInstanceId(t.getProcessInstanceId()).singleResult();
//			
//			String activitiId = "usertask1";// taskService.getVariable(t.getId(), "activitiId").toString();
//			TaskServiceImpl taskServiceImpl=(TaskServiceImpl)taskService;  
//			taskServiceImpl.getCommandExecutor().execute(new JumpBeforeNodeCmd(exe.getId(),activitiId));  
			flowCoreService.completeTask(t.getId(), null);
			System.out.println(t.getId());
		}
		json.put("res", "2");
		json.put("msg", "成功");
		return a.returnAjaxBack(json);
	}
	@RequestMapping("auditLeave7")
	public ModelAndView auditLeave7() throws Exception{ // 测试审核
		JSONObject json = new JSONObject();
		CommonTransferModel c = new CommonTransferModel();
		AjaxBackResult a = new AjaxBackResult();
		List<Task> list = taskService.createTaskQuery().taskAssignee("p7").list();
		for(Task t:list){
			flowCoreService.completeTask(t.getId(), null);
			System.out.println(t.getId());
		}
		json.put("res", "2");
		json.put("msg", "成功");
		return a.returnAjaxBack(json);
	}
	
	
}
