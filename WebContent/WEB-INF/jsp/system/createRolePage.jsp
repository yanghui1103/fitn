<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.bw.fit.common.model.*"
	pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
$("button",navTab.getCurrentPanel()).click(function(){
	dwzConfirmFormToBack("是否确认新建角色?",function(){
		$('#temp_str1', navTab.getCurrentPanel()).val($('#topIds', navTab.getCurrentPanel()).val());
		$('#temp_str2', navTab.getCurrentPanel()).val($('#topIds2', navTab.getCurrentPanel()).val());
		$("#roleFm",navTab.getCurrentPanel()).submit();
	},function(){}); 
});
</script>
</head>
<body> 
	<div class="pageContent">
		<form id="roleFm"   method=post 
			action="<%=basePath %>system/createRole?navTabId=page102&callbackType=closeCurrent"
			class="pageForm required-validate"
			onsubmit="return validateCallback(this,navTabAjaxDone);">
			<div class="pageFormContent" layoutH="56">
				<p>
					<label>角色名称：</label> <input name="role_name" class="required" minlength="2"  
						type="text"   size="30" maxlength=30 />
				</p>
				<p>
					<label>父角色：</label>
					<select name="parent_id" class="combox">
						<option selected value="">请选择</option>
						<c:forEach var="item" items="${myRoles }">
							<option value="${item.fdid }">${item.role_name }</option>
						</c:forEach>
					</select>
				</p>
			</div>
			<input name="fdid" value="${uuid}" type="hidden" />
			<div class="formBar" id="panelBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="button">保存</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>
</body>
</html>