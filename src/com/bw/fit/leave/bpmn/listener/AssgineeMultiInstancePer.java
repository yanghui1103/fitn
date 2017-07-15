package com.bw.fit.leave.bpmn.listener;

import java.util.Arrays;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;


public class AssgineeMultiInstancePer implements JavaDelegate {  
    @Override  
    public void execute(DelegateExecution execution) throws Exception {  
        System.out.println("设置会签环节的人员.");  
        execution.setVariable("persons", Arrays.asList("p121", "p122", "p123", "p124"));  
    }  

}  