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
	width: 400px;
	height: 150px;
	overflow: auto;
	border: 1px solid #E5E5E5;
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<div class="pageContent" style="margin: 0 10px" layoutH="50">
		<form id="form1" enctype="multipart/form-data">
			<input id="uploadify" type="file" name="files"
				uploaderOption="{
			swf:ctx+'common/uploadify/scripts/uploadify.swf',
			uploader:ctx+'system/attachment_upload_multi/e4',
			formData:formdata,
			buttonText:'请选择文件',
			fileSizeLimit:'100MB',
			fileTypeDesc:'*.jpg;*.jpeg;*.gif;*.png;*.jar;',
			fileTypeExts:'*.jpg;*.jpeg;*.gif;*.png;*.jar;',
			auto:true,
			multi:mul,
			height:30,
			width:90,
			method : 'post',  
			removeCompleted : false 
		}" />
			<input type="hidden" value='${foreign_id }' id="foreign_id" /> <input
				type="hidden" value='${multi }' id="multi" />
		</form>
	</div>
	<script type="text/javascript">
		var form = document.getElementById("form1");
		var formdata = new FormData(form);
		var mul = document.getElementById('multi').value;
		var foreign_id = document.getElementById('foreign_id').value; 
	</script>
</body>
</html>