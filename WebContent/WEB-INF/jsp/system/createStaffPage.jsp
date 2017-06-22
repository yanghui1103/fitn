<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="application/x-www-form-urlencoded; charset=UTF-8">
<script type="text/javascript">
$("button",navTab.getCurrentPanel()).click(function(){
	dwzConfirmFormToBack("是否确认新建组织?",function(){
		$("#createStaffFm",navTab.getCurrentPanel()).submit();
	},function(){}); 
});
</script>
</head>
<body>
	<div class="pageContent">
		<form id="createStaffFm" name="company" method=post
			action="<%=basePath %>system/createStaff?callbackType=closeCurrent"
			class="pageForm required-validate"
			onsubmit="return validateCallback(this,navTabAjaxDone);">
			<div class="pageFormContent" layoutH="56">
				<p>
					<label>用户姓名：</label> <input name="staff_name" class="required" minlength="2" maxlength=10
						type="text"   size="30" maxlength=30 />
				</p>
				<p>
					<label>登录帐号：</label> <input name="staff_number" class="required alphanumeric" minlength="2" maxlength=10
						type="text"   size="30" maxlength=30 />
				</p>
				<p>
					<label>性别：</label> 
					<select name=gender class="combox required">
						<option selected value="-9">请选择</option>
						<option value="1">男</option>
						<option value="0">女</option>
					</select>
				</p>
				<p>
					<label>联系电话：</label> <input name="phone"  class="phone"
						type="text"   size="30" maxlength=11 minlength="11" />
				</p>
				<p>
					<label>所属组织：</label><input type="text"   class="required"
						style="float: left" readonly name="orgLookup.orgName" value=""
						suggestFields="orgNum,orgName" lookupGroup="orgLookup" /> <a
						style="float: left" class="btnLook"
						href="gotoIFramePage/system/selectObjByTreePage"
						lookupGroup="orgLookup"></a> <input type="hidden"
						class="orgReqCss" readonly="readonly" name="orgLookup.id" /> <input type="hidden"
						  name="parent_company_id" id="parent_company_id" />
				</p>
				<p>
					<label>角色：</label><input type="text"   class="required"
						style="float: left" readonly name="orgLookup.orgName" value=""
						suggestFields="orgNum,orgName" lookupGroup="orgLookup" /> <a
						style="float: left" class="btnLook"
						href="gotoIFramePage/system/selectObjByTreePage"
						lookupGroup="orgLookup"></a> <input type="hidden"
						class="orgReqCss" readonly="readonly" name="orgLookup.id" /> <input type="hidden"
						  name="parent_company_id" id="parent_company_id" />
				</p>
				<p>
					<label>用户组：</label><input type="text"   
						style="float: left" readonly name="orgLookup.orgName" value=""
						suggestFields="orgNum,orgName" lookupGroup="orgLookup" /> <a
						style="float: left" class="btnLook"
						href="gotoIFramePage/system/selectObjByTreePage"
						lookupGroup="orgLookup"></a> <input type="hidden"
						class="orgReqCss" readonly="readonly" name="orgLookup.id" /> <input type="hidden"
						  name="parent_company_id" id="parent_company_id" />
				</p>
				<p>
					<label>岗位：</label><input type="text"   class="required"
						style="float: left" readonly name="orgLookup.orgName" value=""
						suggestFields="orgNum,orgName" lookupGroup="orgLookup" /> <a
						style="float: left" class="btnLook"
						href="gotoIFramePage/system/selectObjByTreePage"
						lookupGroup="orgLookup"></a> <input type="hidden"
						class="orgReqCss" readonly="readonly" name="orgLookup.id" /> <input type="hidden"
						  name="parent_company_id" id="parent_company_id" />
				</p>
			</div>
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