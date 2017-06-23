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
<style type="text/css" media="screen">
.my-uploadify-button {
	background: none;
	border: none;
	text-shadow: none;
	border-radius: 0;
}
.uploadify:hover .my-uploadify-button {
	background: none;
	border: none;
}
.fileQueue {
	width: 300px;
	height: 100px;
	overflow: auto;
	border: 1px solid #E5E5E5;
	margin-bottom: 10px;
}
.div-a {
	margin-top: 10px;
	float: left;
	width: 70%;
	border: 0px solid #000;
	float: left;
}
.div-b {
	margin-top: 10px;
	float: left;
	width: 30%;
	border: 0px solid #000;
}
</style>
<script type="text/javascript">
function upload(callback){
	var files = $("#files").attr("value");
	var uploadUrl = $("#uploadUrl").attr("value");
	var url = uploadUrl+"&files="+escape(encodeURIComponent(files));
	$("#files").attr("value",""); 
	var $callback = callback || navTabAjaxDone;
	if (! $.isFunction($callback)) $callback = eval('(' + callback + ')');
	$.ajax({
		type:'GET',
		url:url,
		dataType:"json",
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		cache: false,
		success: $callback,
		error: DWZ.ajaxError
	});
}
</script>
<div class="pageContent" style="margin: 0 0 0 0" heigth=100% scoll=yes>

	<div class="div-a">
		<div id="fileQueue" class="fileQueue"></div>
	</div>
	<div class="div-b">
		<input id="testFileInput2" type="file" name="image2"
			uploaderOption="{
			swf:'<%=basePath%>common/uploadify/scripts/uploadify.swf',
			uploader:'<%=basePath%>system/attachment_upload_multi/'+$('#foreign_id',$.pdialog.getCurrent()).val(),
			formData:{PHPSESSID:'xxx', ajax:1},
			queueID:'fileQueue',
			buttonImage:'<%=basePath%>common/uploadify/img/add.jpg',
			buttonClass:'my-uploadify-button',
			width:102,
			auto:false
		}" />

		<input type="image" src="<%=basePath%>common/uploadify/img/upload.jpg"
			onclick="$('#testFileInput2').uploadify('upload', '*');" /> <input
			type="image" src="<%=basePath%>common/uploadify/img/cancel.jpg"
			onclick="$('#testFileInput2').uploadify('cancel', '*');" /> 
		
		<input id="uploadUrl" type="hidden" value ="${uploadUrl }" name="uploadUrl" />
	<input id="files" type="hidden" value ="" name="files" />
	<input type="image" src="<%=basePath%>common/uploadify/img/submit.jpg" onclick="upload();" /> 
	</div>
	<input id="foreign_id" value=${foreign_id } type="hidden" />
	<div class="divider"></div>
	<div class="pageContent">
		<table class="table" width="100%">
			<thead>
				<tr>
					<th width="5%">序号</th>
					<th width="20%">文件名称</th>
					<th width="20%">文件大小</th>
					<th width="20%">上传者</th>
					<th width="20%">上传时间</th>
					<th width="15%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${attList}" varStatus="s">
					<tr target="item_id" rel="${item.fdid}">
						<td>${s.index+1}</td>
						<td>${item.before_name}</td>
						<td>${item.file_size}</td>
						<td>${item.creator_name}</td>
						<td>${item.create_time}</td>
						<td><a title="删除" target="ajaxTodo"
							href="demo/common/ajaxDone.html?id=xxx" class="btnDel">删除</a> <a
							title="下载" target="dialog"
							href="<%=basePath%>${item.path}/${item.file_name}"
							class="btnEdit">下载</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
</body>
</html>