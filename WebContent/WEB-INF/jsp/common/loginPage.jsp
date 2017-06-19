<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.bw.fit.common.util.*"
    pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
	session=request.getSession(false);
	if(session!=null)session.invalidate();
%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=PropertiesUtil.getValueByKey("system.full_name") %></title>
<link href="<%=basePath %>themes/css/login.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<div id="login">
		<div id="login_header">
			<h1 class="login_logo">
				<a href="http://demo.dwzjs.com"><img src="<%=basePath %>themes/default/images/login_logo.gif" /></a>
			</h1>
			<div class="login_headerContent">
				<div class="navList"> 
				</div>
				<h2 class="login_title"><img src="<%=basePath %>themes/default/images/login_title.png" /></h2>
			</div>
		</div>
		<div id="login_content">
			<div class="loginForm">
				<form action="<%=basePath%>system/login" method="post">
					<p>
						<label>账号：</label>
						<input type="text" name="user_cd" size="20" class="login_input" />
					</p>
					<p>
						<label>密码：</label>
						<input type="password" name="passwd" size="20" class="login_input" />
					</p> 
					<c:if test="${errorMsg !=null}">
						<p><font color=red>${errorMsg}</font></p>
					</c:if>
					<div class="login_bar">
						<input class="sub" type="submit" value=" " />
					</div>
				</form>
			</div>
			<div class="login_banner"><img src="<%=basePath %>themes/default/images/login_banner.jpg" /></div>
			<div class="login_main">
				<ul class="helpList">
					<li><a href="#"></a></li>
					<li><a href="#"></a></li>
					<li><a href="#"></a></li>
					<li><a href="#"></a></li>
				</ul>
				<div class="login_inner">
					<p></p>
					<p></p>
					<p></p>
				</div>
			</div>
		</div>
		<div id="login_footer">
			Copyright &copy; 2009 伊泰准东铁路有限责任公司. All Rights Reserved.
		</div>
	</div>
</body>
</html>