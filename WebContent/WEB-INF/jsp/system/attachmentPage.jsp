<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<script type="text/javascript">
	$(function() {
		renderAuthorityOperateBtnAll($("#panelBar", navTab.getCurrentPanel()),
				"getOperationsByMenuId", "100", false, "panelBar");
	});
	function uploadFile(obj) {
		$.ajaxFileUpload({
			url : ctx + "system/doUpload/111",
			secureuri : false,// 一般设置为false  
			fileElementId : "fileUpload",// 文件上传表单的id <input type="file" id="fileUpload" name="file" />  
			dataType : 'json',// 返回值类型 一般设置为json  
			data : {
				'type' : null
			},
			success : function(data) // 服务器成功响应处理函数  
			{
				alert(data);
			},
			error : function(data)// 服务器响应失败处理函数  
			{
				alert("seerr");
				console.log("服务器异常");
			}
		});
		return false;
	}

	function uploadFile2(obj) {
		alert('222');
		$("#upFm").submit();
	}
</script>
<div class="pageHeader">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<form action="<%=basePath%>system/doUpload/111" id="upFm"
					method="post" enctype="multipart/form-data"
					onsubmit="return navTabSearch(this)">
					<input type="file" name="file" multiple=true id="fileUpload"
						onchange="uploadFile2(this)" />
				</form>
			</li>
		</ul>

		<div class="subBar">
			<ul>
				<li></li>
			</ul>
		</div>
	</div>
</div>
<c:import url="../_frag/pager/pagerForm.jsp"></c:import>
<form method="post" rel="pagerForm"
	action="<%=basePath%>system/attachmentList"
	onsubmit="return navTabSearch(this)"></form>
<div class="pageContent">
	<div class="panelBar" id="panelBar"></div>
	<table class="table" width="100%" layoutH="133">
		<thead>
			<tr>
				<th width="120">名称</th>
				<th width="60">大小</th>
				<th width="100">上传人</th>
				<th width="100">上传时间</th>
				<th width="50">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${attList}" varStatus="s">
				<tr target="id" rel="${item.fdid}">
					<td>${item.company_name}</td>
					<td>${item.company_type_name}</td>
					<td>${item.parent_company_name}</td>
					<td>${item.company_address}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<c:import url="../_frag/pager/panelDialogBar.jsp"></c:import>
</div>