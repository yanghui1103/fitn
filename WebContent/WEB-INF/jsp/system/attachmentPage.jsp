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
function uploadFile(obj, type) {  
	  
    $.ajaxFileUpload({  
        url : "http://localhost:8081/ws2/servlet/fileUpload",  
        secureuri : false,// 一般设置为false  
        fileElementId : "fileUpload"+type,// 文件上传表单的id <input type="file" id="fileUpload" name="file" />  
        dataType : 'json',// 返回值类型 一般设置为json  
        data: {'type': type, "type2":2},            
        success : function(data) // 服务器成功响应处理函数  
        {  
                },  
        error : function(data)// 服务器响应失败处理函数  
        {  
            console.log("服务器异常");  
        }  
    });  
    return false;  
}  
</script>
<div class="pageContent" style="margin: 0 0 0 0" heigth=100% scoll=yes>

	<div class="div-a">

		<form action="upload.do" method="post" enctype="multipart/form-data">
			<input type="file" name="file"  multiple=true onchange ="uploadFile(this,1)"  />  
		</form>
	</div>
	<input id="foreign_id" value="${foreign_id }" type="hidden" />
	<input id="multi" value="${multi }" type="hidden" />
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