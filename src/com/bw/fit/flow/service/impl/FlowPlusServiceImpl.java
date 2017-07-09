package com.bw.fit.flow.service.impl;

import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import com.bw.fit.flow.service.FlowPlusService;

public class FlowPlusServiceImpl implements FlowPlusService {
	@Autowired
	private TaskService taskService ;
	@Autowired
	private RuntimeService runtimeService ;
	
	@Override
	public void createStartNodeVars(Map<String, Object> vars) {
		// TODO Auto-generated method stub 
	}

}
