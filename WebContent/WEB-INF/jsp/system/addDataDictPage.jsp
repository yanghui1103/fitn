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
</HEAD>
<BODY> 
	<div class="pageContent">
		<form    method=post 
			action="<%=basePath %>system/addDataDict?navTabId=page200&callbackType=closeCurrent"
			class="pageForm required-validate"
			onsubmit="return validateCallback(this,navTabAjaxDone);">
			<div class="pageFormContent" layoutH="56">
				<p>
					<label>父节点名称：</label> <input  
						type="text" readonly  value="${model.dict_name }"  size="30"  />
				</p> 
				<p>
					<label>字典名称：</label> <input  type="text" name="dict_name"  class="required"
						  size="30"  />
				</p>  
				<p>
					<label>字典值：</label> <input type="text" name="dict_value"     class="required"
						type="text"   size="30"  />
				</p> 
				<p>
					<label>序号：</label> <input   name="num"     class="required"
						type="text"    size="30"  />
				</p> 
				<p>
					<label>可增加：</label> 
					<select name=can_add class="combox required">
						<option selected value="">请选择</option>
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
				</p>
				<p>
					<label>可修改：</label> 
					<select name=can_edit class="combox required">
						<option selected value="">请选择</option>
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
				</p>
				<p>
					<label>可删除：</label> 
					<select name=can_del class="combox required">
						<option selected value="">请选择</option>
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
				</p>
				<input value = "${item_id }" type="hidden" name="parent_id"  />
			</div>
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
</BODY>
</HTML>