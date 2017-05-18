package com.bw.fit.flow.service;

import java.util.*;

public interface FlowCoreService {

	/** 
     * 驳回流程 
     * @param taskId  当前任务ID 
     * @param activityId   驳回节点ID 
     * @param variables  流程存储参数 
     * @throws Exception 
     */  
    public void rollBack(String taskId, String backActivityId,Map<String, Object> variables) throws Exception;
}
