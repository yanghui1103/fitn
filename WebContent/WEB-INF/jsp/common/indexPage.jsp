<%@ page language="java" contentType="text/html; charset=UTF-8"   import="com.bw.fit.common.model.LogUser,com.bw.fit.common.util.*"
  isELIgnored="false"   pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/" ;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8" /> 
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
<title><%=PropertiesUtil.getValueByKey("system.full_name") %></title>
<script src="<%=basePath%>common/js/jquery-1.7.2.min.js"
	type="text/javascript"></script>
<link href="<%=basePath%>themes/default/style.css" rel="stylesheet"
	type="text/css" media="screen" />
<link href="<%=basePath%>themes/css/core.css" rel="stylesheet"
	type="text/css" media="screen" />
<link href="<%=basePath%>themes/css/print.css" rel="stylesheet"
	type="text/css" media="print" />
<link href="<%=basePath%>common/uploadify/css/uploadify.css"
	rel="stylesheet" type="text/css" media="screen" />
	<!--[if IE]>
<link href="<%=basePath%>themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->
<script src="<%=basePath%>common/js/speedup.js" type="text/javascript"></script>
 
<script src="<%=basePath%>common/js/jquery.cookie.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/jquery.validate.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/jquery.bgiframe.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/xheditor/xheditor-1.2.2.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/xheditor/xheditor_lang/zh-cn.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/uploadify/scripts/jquery.uploadify.js"
	type="text/javascript"></script>

<script src="<%=basePath%>common/js/dwz.core.js" type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.util.date.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.validate.method.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.barDrag.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.drag.js" type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.tree.js" type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.accordion.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.ui.js" type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.theme.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.switchEnv.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.alertMsg.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.contextmenu.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.navTab.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.tab.js" type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.resize.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.dialog.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.dialogDrag.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.sortDrag.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.cssTable.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.stable.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.taskBar.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/ajaxfileupload.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.ajax.js" type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.pagination.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.database.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.datepicker.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.effects.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.panel.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.checkbox.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.history.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.combox.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.file.js" type="text/javascript"></script>
<script src="<%=basePath%>common/js/dwz.print.js"
	type="text/javascript"></script>
<script src="<%=basePath%>common/js/misc.js" type="text/javascript"></script>
<!-- 可以用dwz.min.js替换前面全部dwz.*.js (注意：替换时下面dwz.regional.zh.js还需要引入)
<script src="<%=basePath%>common/js/dwz.min.js" type="text/javascript"></script> 
<script src="<%=basePath%>common/js/dwz.regional.zh.js" type="text/javascript"></script>
-->
<script type="text/javascript"
	src="<%=basePath%>common/fit/dwz_common.js"></script>
<script type="text/javascript"
	src="<%=basePath%>common/js/json2.js"></script>
<script type="text/javascript"
	src="<%=basePath%>common/fit/homePage.js"></script>
<script type="text/javascript">
	$(function() {
		var path = $("#basepath").val() ; 
		generateTree($("#pwr").val(),path); 	  
		initDwzPage(path); 
	});
</script>

</head>

<body >
	
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<a class="logo3" >标志</a>
				<ul class="nav">
					<li><a id="zhanghao">您好,<%=((LogUser)session.getAttribute("LogUser")).getUser_name() %></a></li> 
					<li><a id="company"><%=((LogUser)session.getAttribute("LogUser")).getCompany_name() %></a></li>
					<li><a href="javascript:window.location.reload();"><span>刷新</span></a></li>
					<li><a href="gotoIFramePage/system/changePwdPage" target=dialog><span>设置密码</span></a></li>
					<li><a href="logout">退出</a></li>
				</ul> 
			</div>

			<!-- navMenu -->
			
		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse"><div></div></div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse"><h2>主菜单</h2><div>收缩</div></div>
				<div id ="leftMenu" class="accordion" fillSpace="sidebar">
				</div>
			</div>
		</div>
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent"> 
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">我的主页</span></span></a></li>
						</ul>
					</div>
				</div> 
				<!-- style="overflow-y:scroll" -->
				<div class="navTab-panel tabsPageContent layoutBox"  >  
					<div class="page unitBox"><jsp:include page="/WEB-INF/jsp/system/mainPage.jsp"></jsp:include>
						<div >							
						<div class="pageFormContent" layoutH="80" style="margin-right:230px">
						</div>
						<div> <input
								type=hidden id="pwr"
								value=${menuTreeJson} type="hidden" />
						<input type=hidden  id="basepath"  value=<%=basePath %> />
						<input id="basePathOfSys" value="<%=basePath %>" type="hidden"/>
						</div> 
					</div>
				</div>
			</div> 
		</div>
	</div>
	</div> 
</body>
</html>