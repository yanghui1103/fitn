package com.bw.fit.flow.service;

import java.util.*;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import java.awt.image.*;
import java.io.IOException;
import java.io.InputStream;
public interface FlowCoreService {

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
    
    public BufferedImage getProcessDiagramByDefId(String defId) throws IOException;/**获取流程图**/
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
    public void createTaskAssignee(String taskId,String userId);/**设置任务的受理人，这个人处理代办****/

    public void createAttachment(String taskId,String processinstanceId,String name,String descp,String type,String url);// 上传附件
    public void createAttachment(String taskId,String processinstanceId,String name,String descp,String type,String url,InputStream is);
    public void deleteAttachment(String attachmentId);
    public List<Attachment> getAttachmentsOfProccesInstance(String processInstanceId);
    public List<Attachment> getAttachmentsOfTheTask(String taskId);
    
    
    public void createTaskComment(String taskId,String processInstanceId,String message); // 增加评论
    public List<Comment> getCommentOfTheTask(String taskId);
    public List<Comment> getCommentOfProcessInstance(String instanceId);
    
    public void completeTask();
    public void completeTask(String taskId,Map<String,Object> vars);
    
    
}
