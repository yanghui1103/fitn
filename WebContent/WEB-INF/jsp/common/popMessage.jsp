<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.bw.fit.common.util.*" pageEncoding="UTF-8"%><%@ include
	file="/include.inc.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<!--jquery右下角pop弹窗start -->
	<script type="text/javascript"> 
	  $(function(){
			if(is_pop == '1'){ // 如果启用即时信息通知
		  	var is_pop = '<%=PropertiesUtil.getValueByKey("system.is_open_popmessage")%>' ;
		    var source=new EventSource("<%=basePath%>messageInteractive.jsp");
			source.onmessage=function(event)
			  {     
					setTimeout(popAlert(event),10000) ;
				 
			  };  
			}
	  });
	  function popAlert(event){
		  var json = JSON.parse(event.data); 
			if(json.res =='2'){
				var pop=new Pop(json.msg ,
						json.url ,
						json.vip_message);
			}
	  }
</script>
	<div id="pop" style="display: none;">
		<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

#pop {
	background: #fff;
	width: 260px;
	border: 1px solid #e0e0e0;
	font-size: 12px;
	position: fixed;
	right: 5px;
	bottom: 35px;
}

#popHead {
	line-height: 32px;
	background: #e7eff0;
	border-bottom: 1px solid #e0e0e0;
	position: relative;
	font-size: 12px;
	padding: 0 0 0 10px;
}

#popHead h2 {
	font-size: 14px;
	color: #666;
	line-height: 32px;
	height: 32px;
}

#popHead #popClose {
	position: absolute;
	right: 10px;
	top: 1px;
}

#popHead a#popClose:hover {
	color: #f00;
	cursor: pointer;
}

#popContent {
	padding: 5px 10px;
}

#popTitle a {
	line-height: 24px;
	font-size: 14px;
	font-family: '微软雅黑';
	color: #333;
	font-weight: bold;
	text-decoration: none;
}

#popTitle a:hover {
	color: #f60;
}

#popIntro {
	text-indent: 24px;
	line-height: 160%;
	margin: 5px 0;
	color: #666;
}

#popMore {
	text-align: right;
	border-top: 1px dotted #ccc;
	line-height: 24px;
	margin: 8px 0 0 0;
}

#popMore a {
	color: #f60;
}

#popMore a:hover {
	color: #f00;
}
</style>
		<div id="popHead">
			<a id="popClose" title="关闭">关闭</a>
			<h2>消息提醒</h2>
		</div>
		<div id="popContent">
			<dl>
				<dt id="popTitle">
					<a href="r4" target="navTab" rel="pop">这里是参数</a>
				</dt>
				<dd id="popIntro">这里是内容简介</dd>
			</dl>
		</div>
	</div>
</body>
</html>