package com.bw.fit.flow.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.image.BufferedImage;

import com.bw.fit.flow.service.FlowCoreService;

public class FlowCoreServiceImpl implements FlowCoreService {
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;

	@Override
	public void rollBack(String taskId, String backActivityId,
			Map<String, Object> variables) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspendProcessDefinitionById(String defId) {
		// TODO Auto-generated method stub
		repositoryService.suspendProcessDefinitionById(defId);
	}

	@Override
	public void suspendProcessDefinitionByKey(String defKey) {
		// TODO Auto-generated method stub
		repositoryService.suspendProcessDefinitionByKey(defKey);
	}

	@Override
	public void activateProcessDefinitionById(String defId) {
		// TODO Auto-generated method stub
		repositoryService.activateProcessDefinitionById(defId);
	}

	@Override
	public void activateProcessDefinitionByKey(String defKey) {
		// TODO Auto-generated method stub
		repositoryService.activateProcessDefinitionByKey(defKey);
	}

	@Override
	public void addCandidateStarterGroup(String defId, String groupId) {
		// TODO Auto-generated method stub
		repositoryService.addCandidateStarterGroup(defId, groupId);
	}

	@Override
	public void addCandidateStarterUser(String defId, String userId) {
		// TODO Auto-generated method stub
		repositoryService.addCandidateStarterUser(defId, userId);
	}

	@Override
	public List<ProcessDefinition> getCanStartableByUser(String userId) {
		// TODO Auto-generated method stub
		List<ProcessDefinition> list = repositoryService
				.createProcessDefinitionQuery().startableByUser(userId).list();
		return list;
	}

	@Override
	public BufferedImage getProcessDiagramByDefId(String defId)
			throws IOException {
		// TODO Auto-generated method stub
		InputStream is = repositoryService.getProcessDiagram(defId);
		java.awt.image.BufferedImage image = ImageIO.read(is);
		return image;
	}

	@Override
	public void deleteDeploymentCasCade(String defId, boolean b)
			throws Exception {
		// TODO Auto-generated method stub
		repositoryService.deleteDeployment(defId, b);
	}

	@Override
	public void createTask() {
		// TODO Auto-generated method stub
		taskService.newTask();
	}

	@Override
	public void createTask(String taskId) {
		// TODO Auto-generated method stub
		taskService.newTask(taskId); // 必须保证这个id不存在，否则会主键冲突
	}

	@Override
	public void deleteTaskCascade(String taskId, boolean b) {
		// TODO Auto-generated method stub
		taskService.deleteTask(taskId, b);
	}

	@Override
	public void deleteTaskCascade(Collection<String> taskIds, boolean cascade) {
		// TODO Auto-generated method stub
		taskService.deleteTasks(taskIds, cascade);
	}

	@Override
	public void createTaskGroupRelation(String taskId, String groupId) {
		// TODO Auto-generated method stub
		taskService.addCandidateGroup(taskId, groupId);
	}

	@Override
	public void createTaskUserRelation(String taskId, String userId) {
		// TODO Auto-generated method stub
		taskService.addCandidateUser(taskId, userId);
	}

	@Override
	public List<Task> getTasksOfTheGroup(String groupId) {
		// TODO Auto-generated method stub
		List<Task> list = taskService.createTaskQuery()
				.taskCandidateGroup(groupId).list();
		return list;
	}

	@Override
	public List<Task> getTasksOfTheUser(String userId) {
		// TODO Auto-generated method stub
		List<Task> list = taskService.createTaskQuery()
				.taskCandidateUser(userId).list();
		return list;
	}

	@Override
	public void createTaskOwner(String taskId, String userId) {
		// TODO Auto-generated method stub
		taskService.setOwner(taskId, userId);
	}

	@Override
	public void deleteGroupTaskRelation(String taskId, String groupId) {
		// TODO Auto-generated method stub
		taskService.deleteCandidateGroup(taskId, groupId);
	}

	@Override
	public void createTaskAssignee(String taskId, String userId) {
		// TODO Auto-generated method stub
		taskService.setAssignee(taskId, userId);
	}

	@Override
	public void deleteUserTaskRelation(String taskId, String userId, String type) {
		// TODO Auto-generated method stub
		/**
		 * IdentityLinkType.ASSIGNEE或OWNER 将会act_ru_task 表中owner ,assignee 都会置空
		 * IdentityLinkType.CANDIDATE 则是删除用户权限数据，只是把act_ru_identitylink那条记录删除
		 * **/
		taskService.deleteUserIdentityLink(taskId, userId, type);
		taskService.setVariable(taskId, userId, type);
	}

	@Override
	public void createAttachment(String taskId, String processinstanceId,
			String name, String descp, String type, String url) {
		// TODO Auto-generated method stub
		taskService.createAttachment(type, taskId, processinstanceId, name,
				descp, url);
	}

	@Override
	public void createAttachment(String taskId, String processinstanceId,
			String name, String descp, String type, String url, InputStream is) {
		// TODO Auto-generated method stub
		taskService.createAttachment(type, taskId, processinstanceId, name,
				descp, is);
	}

	@Override
	public void deleteAttachment(String attachmentId) {
		// TODO Auto-generated method stub
		taskService.deleteAttachment(attachmentId);
	}

	@Override
	public List<Attachment> getAttachmentsOfProccesInstance(
			String processInstanceId) {
		// TODO Auto-generated method stub
		return taskService.getProcessInstanceAttachments(processInstanceId);
	}

	@Override
	public List<Attachment> getAttachmentsOfTheTask(String taskId) {
		// TODO Auto-generated method stub
		return taskService.getTaskAttachments(taskId);
	}

	@Override
	public void createTaskComment(String taskId, String processInstanceId,
			String message) {
		// TODO Auto-generated method stub
		taskService.addComment(taskId, processInstanceId, message);
	}

	@Override
	public List<Comment> getCommentOfTheTask(String taskId) {
		// TODO Auto-generated method stub
		return taskService.getTaskComments(taskId);
	}

	@Override
	public List<Comment> getCommentOfProcessInstance(String instanceId) {
		// TODO Auto-generated method stub
		return taskService.getProcessInstanceComments(instanceId);
	}

	@Override
	public void completeTask() {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeTask(String taskId, Map<String, Object> vars) {
		// TODO Auto-generated method stub
		taskService.complete(taskId, vars);
		/** 将vars里这些参数传入下一个环节，且可以利用这个决定流程走向 **/
	}

	@Override
	public void startProcessByPdId(String processDefiniedId) {
		// TODO Auto-generated method stub
		runtimeService.startProcessInstanceById(processDefiniedId);
	}

	@Override
	public void startProcessByPdId(String processDefiniedId,
			Map<String, Object> vars) {
		// TODO Auto-generated method stub
		runtimeService.startProcessInstanceById(processDefiniedId, vars);
	}

	@Override
	public void startProcessByPdId(String processDefiniedId,
			String bussiness_key) {
		// TODO Auto-generated method stub
		runtimeService.startProcessInstanceById(processDefiniedId,
				bussiness_key);
	}

	@Override
	public void startProcessByPdId(String processDefiniedId,
			String bussiness_key, Map<String, Object> vars) {
		// TODO Auto-generated method stub
		runtimeService.startProcessInstanceById(processDefiniedId,
				bussiness_key, vars);
	}

	@Override
	public void signalProcess(String exeId) {
		// TODO Auto-generated method stub
		runtimeService.signal(exeId);
	}

	@Override
	public void signalProcess(String exeId, Map<String, Object> vars) {
		// TODO Auto-generated method stub
		runtimeService.signal(exeId, vars);
	}

	@Override
	public void suspendProcessInstanceByPiId(String processInstanceId) {
		// TODO Auto-generated method stub
		runtimeService.suspendProcessInstanceById(processInstanceId);
	}

	@Override
	public void activateProcessInstanceByPiId(String processInstanceId) {
		// TODO Auto-generated method stub
		runtimeService.activateProcessInstanceById(processInstanceId);
	}

	@Override
	public boolean isProcessSuspend(String processInstanceId) {
		// TODO Auto-generated method stub
		Execution exe = runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId).singleResult();

		return exe.isSuspended();
	}

	@Override
	public boolean isProcessEnd(String processInstanceId) {
		// TODO Auto-generated method stub
		Execution exe = runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId).singleResult();

		return exe.isEnded();
	}

	@Override
	public void deleteProcessInstance(String piid, String reason) {
		// TODO Auto-generated method stub
		runtimeService.deleteProcessInstance(piid, reason);
	}

	@Override
	public void rollBackProcess(String currentTaskId) throws Exception {
		/*** 驳回 ****/
		try {
			Map<String, Object> variables;
			// 取得当前任务
			HistoricTaskInstance currTask = historyService
					.createHistoricTaskInstanceQuery().taskId(currentTaskId)
					.singleResult();
			// 取得流程实例
			ProcessInstance instance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(currTask.getProcessInstanceId())
					.singleResult();
			if (instance == null) {
				// 流程结束
			}
			variables = instance.getProcessVariables();
			// 取得流程定义
			ProcessDefinitionEntity definition = (ProcessDefinitionEntity) (processEngine
					.getRepositoryService().getProcessDefinition(currTask
					.getProcessDefinitionId()));

			if (definition == null) {
				// log.error("流程定义未找到");
				return;
			}
			// 取得上一步活动
			ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
					.findActivity(currTask.getTaskDefinitionKey());
			List<PvmTransition> nextTransitionList = currActivity
					.getIncomingTransitions();
			// 清除当前活动的出口
			List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
			List<PvmTransition> pvmTransitionList = currActivity
					.getOutgoingTransitions();
			for (PvmTransition pvmTransition : pvmTransitionList) {
				oriPvmTransitionList.add(pvmTransition);
			}
			pvmTransitionList.clear();

			// 建立新出口
			List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
			for (PvmTransition nextTransition : nextTransitionList) {
				PvmActivity nextActivity = nextTransition.getSource();
				ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition)
						.findActivity(nextActivity.getId());
				TransitionImpl newTransition = currActivity
						.createOutgoingTransition();
				newTransition.setDestination(nextActivityImpl);
				newTransitions.add(newTransition);
			}
			// 完成任务
			List<Task> tasks = taskService.createTaskQuery()
					.processInstanceId(instance.getId())
					.taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
			for (Task task : tasks) {
				taskService.complete(task.getId(), variables);
				historyService.deleteHistoricTaskInstance(task.getId());
			}
			// 恢复方向
			for (TransitionImpl transitionImpl : newTransitions) {
				currActivity.getOutgoingTransitions().remove(transitionImpl);
			}
			for (PvmTransition pvmTransition : oriPvmTransitionList) {
				pvmTransitionList.add(pvmTransition);
			}

			return;
		} catch (Exception e) {
			return;
		}
	}

	@Override
	public boolean isJonitTaskCompleted(ActivityExecution execution) {
		return false;
	}
}
