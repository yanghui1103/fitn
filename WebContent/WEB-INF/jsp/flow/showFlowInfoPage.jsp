<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.bw.fit.common.util.*"
	pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
$(function(){
	$("#flowForm").load(<%=basePath%>+"");
	
});
</script>
</head>
<body>
	<div class="pageContent">
		<form   method=post
			action="<%=basePath%>flowController/auditFlowForm?navTabId=page100&callbackType=closeCurrent"
			class="pageForm required-validate"
			onsubmit="return validateCallback(this,navTabAjaxDone);">
			<div class="pageFormContent" layoutH="56" id="flowForm">				 
			</div>
			<div class="formBar" id="panelBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type=button>保存</button> 
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>
</body>
</html>