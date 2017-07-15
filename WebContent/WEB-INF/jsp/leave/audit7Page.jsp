<%@ page language="java" contentType="text/html; charset=UTF-8"
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
</head>
<body>
	<div class="pageContent">
		<form  method=post
			action="<%=basePath %>system/leave/auditLeave7?navTabId=page202&callbackType=closeCurrent"
			class="pageForm required-validate"
			onsubmit="return validateCallback(this,navTabAjaxDone);">
			<div class="pageFormContent" layoutH="56">
				<p>
					<label>理由7：</label> <input name="leave_reason" value="${model.leave_reason }" class="required"
						type="text" size="30" maxlength=30 />
				</p>
				<p>
					<label>天数7：</label> <input name="days" type="digits" value="${model.days }" class="required"
						size="30" maxlength=30 />
				</p> 
			</div> 
			<input name="fdid" value="${model.fdid }"
				type="hidden" />
			<div class="formBar" id="panelBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button>保存</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>
</body>
</html>