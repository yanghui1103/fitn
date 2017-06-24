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
<meta http-equiv="Content-Type"
	content="application/x-www-form-urlencoded; charset=UTF-8">
<script type="text/javascript">
	// $("button",navTab.getCurrentPanel()).click(function(){
	// 	dwzConfirmFormToBack("是否确认新建组织?",function(){
	// 		$("#createCompanyFm",navTab.getCurrentPanel()).submit();
	// 	},function(){}); 
	// });
</script>
</head>
<body>
	<div class="pageContent">
		<form id="createCompanyFm" name="company" method=post
			action="<%=basePath%>system/createCompany?callbackType=closeCurrent"
			class="pageForm required-validate"
			onsubmit="return validateCallback(this,navTabAjaxDone);">
			<div class="pageFormContent" layoutH="56">
				<p>
					<label>机构名称：</label> <input name="company_name" class="required"
						type="text" size="30" maxlength=30 />
				</p>
				<p>
					<label>机构地址：</label> <input name="company_address" type="text"
						size="30" maxlength=30 />
				</p>
				<p>
					<label>机构类型：</label> <select id="company_type" name="company_type"
						class=combox>
						<option selected value="-9">请选择</option>
						<c:forEach var="item" items="${OrgTypeList}" varStatus="s">
							<option value="${item.dict_value}">${item.dict_name}</option>
						</c:forEach>
					</select>
				</p>
				<p>
					<label>上级机构：</label><input type="text" style="float: left" readonly
						 /> <a style="float:left" class="btnLook"
						href="javascript:openSysAddressBook()"
						></a>  
				</p>
				<p>
					<label>序号：</label> <input name="company_order"
						class="required digits" type="text" size="30" maxlength=3 />
				</p>
			</div>
			<div class="formBar" id="panelBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button>保存</button>
								<button type=button onclick='toUploadPic()'>打开</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>
</body>
</html>