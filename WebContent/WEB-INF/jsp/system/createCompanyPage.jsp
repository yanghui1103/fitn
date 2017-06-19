<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="pageContent">
		<form method=post
			action="system/createCompany?callbackType=closeCurrent"
			class="pageForm required-validate"
			onsubmit="return validateCallback(this,navTabAjaxDone);">
			<div class="pageFormContent" layoutH="56">
				<p>
					<label>机构名称：</label> <input name="company_name" class="required"
						type="text"   size="30" maxlength=30 />
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
					<label>上级机构：</label><input type="text" class="required"
						style="float: left" readonly name="orgLookup.orgName" value=""
						suggestFields="orgNum,orgName" lookupGroup="orgLookup" /> <a
						style="float: left" class="btnLook"
						href="gotoIFramePage/system/selectObjByTreePage"
						lookupGroup="orgLookup"></a> <input type="hidden"
						class="orgReqCss" readonly="readonly" name="orgLookup.id" />
				</p>
				<p>
					<label>序号：</label> <input name="company_name" class="required digits"
						type="text"  size="30" maxlength=3 />
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