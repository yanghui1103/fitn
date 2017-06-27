<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript">  
$(function(){
	renderAuthorityOperateBtnAll($("#panelBar",navTab.getCurrentPanel()),"getOperationsByMenuId","101",false,"panelBar"); 
});

</script>

<c:import url="../_frag/pager/pagerForm.jsp"></c:import>
<form method="post" rel="pagerForm" action="<%=basePath %>system/userList/all" onsubmit="return navTabSearch(this)">
<div class="pageHeader">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>关键词：</label>
				<input type="text"  value="${param.keyWords}"     name="keyWords" />
			</li> 
		</ul>
		<div class="subBar">
			<ul>						
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">查询</button></div></div></li>
			</ul>
		</div> 
	</div>
</div>
</form>
<div class="pageContent">
	<div class="panelBar" id="panelBar">
	</div>
	<table class="table" width="100%" layoutH="133">
		<thead>
			<tr>
				<th width="10">序号</th>
				<th width="100">名称</th> 
				<th width="100">登录帐号</th> 
				<th width="100">联系电话</th> 
				<th width="100">岗位</th> 
				<th width="100">所属公司</th> 
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${userList}" varStatus="s">
			<tr target="id" rel="${item.fdid}">
				<td>${s.index+1}</td>
				<td>${item.staff_name}</td>
				<td>${item.staff_number}</td> 
				<td>${item.phone}</td>  
				<td>${item.postion_name}</td>  
				<td>${item.company_name}</td> 
			</tr>
		</c:forEach>
		</tbody>
	</table>

	<c:import url="../_frag/pager/panelBar.jsp"></c:import>
</div>