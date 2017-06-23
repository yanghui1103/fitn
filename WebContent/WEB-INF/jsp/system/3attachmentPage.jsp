<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="/Tools/Excel/ImportExcel" method="POST" enctype="multipart/form-data" onsubmit="return  fileCallback(this,dialogAjaxDone)" >
    <div class="pageFormContent" layouth="56">
        <input type="text" name="filePath" id="filePath" readonly="readonly" style="width:190px;height: 22px "/>&nbsp; 
        <input type="button" id="btnSelect" value="选择文件"  class="fontColor"/>&nbsp;   
        <input type="button" id="btnDownload" value="下载模板" class="fontColor"/> 
        <input type="file" name="files" id="files"  style="display: none"/>
  </div>
    <div class="formBar">
        <ul>
            <li>
                <div class="button">
                    <div class="buttonContent">
                        <button type="submit">导入</button>        
                    </div>
                </div>
            </li>
            <li>
                <div class="button">
                    <div class="buttonContent">
                        <button type="button" class="close">关闭</button></div>
                </div>
            </li>
        </ul>
    </div>
</form>  
</body>
</html>