package com.bw.fit.flow.service;

import java.util.*;

import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.json.simple.JSONObject;

import com.bw.fit.common.model.RbackException;

import java.awt.image.*;
import java.io.IOException;
import java.io.InputStream;
public interface FlowCoreService {

	/***部署资源***/
	public Deployment deployFlowResource(String resourcePath);
	/***启动流程***/
	public ProcessInstance startProcessInstanceByKey(String key,Map<String,Object> vars);
	/** 
     * 驳回流程 
     * @param taskId  当前任务ID 
     * @param activityId   驳回节点ID 
     * @param variables  流程存储参数 
     * @throws Exception 
     */  
    public void rollBack(String taskId, String backActivityId,Map<String, Object> variables) throws Exception;
    
    public void suspendProcessDefinitionById(String defId);/***挂起流程定义，流程就不能被启动***/
    public void suspendProcessDefinitionByKey(String defKey);
    public void activateProcessDefinitionById(String defId);/**激活流程定义**/
    public void activateProcessDefinitionByKey(String defKey);
    
    public void addCandidateStarterGroup(String defId,String groupId);/**这个用户组可以启动流程，创建流程实例**/
    public void addCandidateStarterUser(String defId,String userId);
    public List<ProcessDefinition> getCanStartableByUser(String userId); 
    
    public BufferedImage getProcessDiagramByDefId(String defId) throws Exception;/**获取流程图**/
    public void deleteDeploymentCasCade(String defId,boolean b) throws Exception; /**删除部署资源及相关联全部数据**/

    public void createTask(); // 新建任务
    public void createTask(String taskId);
    public void deleteTaskCascade(String taskId,boolean b);// 删除任务
    public void deleteTaskCascade(Collection<String> taskIds,boolean cascade);
    
    public void createTaskGroupRelation(String taskId,String groupId); // 创建组与任务之间的关联关系，绑定候选关系
    public void createTaskUserRelation(String taskId,String userId);	// 创建人与任务之间的关联关系，绑定候选关系
    public void deleteGroupTaskRelation(String taskId,String groupId);/**删除用户组与任务关系**/
    public void deleteUserTaskRelation(String taskId,String userId,String type);

    public List<Task> getTasksOfTheGroup(String groupId); // 这个用户组待处理的任务
    public List<Task> getTasksOfTheUser(String userId);  /***此用户拥有的代办任务**/

    public void createTaskOwner(String taskId,String userId);/***重点：设置任务的持有人（非受理人）***/
    public void createTaskAssignee(String taskId,String userId);/**设置任务的受理人，这个人处理代办(转办)****/

    public void createAttachment(String taskId,String processinstanceId,String name,String descp,String type,String url);// 上传附件
    public void createAttachment(String taskId,String processinstanceId,String name,String descp,String type,String url,InputStream is);
    public void deleteAttachment(String attachmentId);
    public List<Attachment> getAttachmentsOfProccesInstance(String processInstanceId);
    public List<Attachment> getAttachmentsOfTheTask(String taskId);
    
    
    public void createTaskComment(String taskId,String processInstanceId,String message); // 增加评论
    public List<Comment> getCommentOfTheTask(String taskId);
    public List<Comment> getCommentOfProcessInstance(String instanceId);
    
    public void completeTask(String taskId,Map<String,Object> vars);// 完成任务
    
    public void startProcessByPdId(String processDefiniedId);/**启动流程**/
    public void startProcessByPdId(String processDefiniedId,Map<String,Object> vars);
    public void startProcessByPdId(String processDefiniedId,String bussiness_key);
    public void startProcessByPdId(String processDefiniedId,String bussiness_key,Map<String,Object> vars);
    
    public void signalProcess(String exeId);/**触发流程继续往下走**/
    public void signalProcess(String exeId,Map<String,Object> vars);/**这个参数值将在整个流程中有效**/
    
    public void suspendProcessInstanceByPiId(String processInstanceId); //挂起流程实例
    public boolean isProcessSuspend(String processInstanceId); //实例是否挂起
    public boolean isProcessEnd(String processInstanceId); // 实例是否结束
    public void activateProcessInstanceByPiId(String processInstanceId);// 激活流程实例
    public void deleteProcessInstance(String piid,String reason);  //删除流程实例
    
    public void rollBackProcess(String currentTaskId) throws Exception;
    
    public boolean isJonitTaskCompleted(ActivityExecution execution) ;
    /**这个任务的候选人和直接参与人，候选人是组用户**/
    public List<HistoricIdentityLink> getDealersOfTheTask(String taskId);
    /**判断任务是不是多例任务会审/会签任务   true 为多例任务***/
    public boolean getMultiInstanceTask(String taskId);
    /***获取一个用户的组任务，个人代办任务并集***/
    public List<Task> getCurrentTasksOfUser(String userId);
    /***获取用户参与过的历史任务,finished:true 只查询已经完成的历史任务，false为全部历史任务***/
    public List<HistoricTaskInstance> getUserhistoryTaskInstance(String userId,boolean finished);
    /***根据流程实例id查询历史的任务列表***/
    public List<HistoricTaskInstance> getHistoryTaskInstance(String processInstanceId);
    /**根据流程定义ID,节点Code,查询出，这个节点上处理人们**/
    public String getThisNodeDealers(String flowDefiniedId,String nodeCode);
    /***认领任务**/
    public void cliamTaskToUser(String taskId,String userId);
    /***把这个待办委托给别人***/
    public void delegateTask(String taskId,String userId);
    /****委托人处理待办****/
    public void resolveTask(String taskId);
    public void resolveTask(String taskId,Map<String,Object> vars);
    /***如果所有者，当前处理者是不是同一个用户ID,(用于委托办理，如果这俩者不同，那就需要使用完成委托的事情)****/
    public boolean isOwnerAssigneeSameUser(Task task);
    
}
