<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript"
	src="<%=basePath%>common/fit/dictItemPage.js"></script>
<%-- <c:import url="../_frag/pager/pagerForm.jsp"></c:import> --%>
<%-- <form method="post" rel="pagerForm" action="<%=basePath %>system/dictlist/${id}" onsubmit="return navTabSearch(this)"> --%>
<!-- </form> -->
<script type="text/javascript">
<!--
$(function() {
	renderAuthorityOperateBtnAll($("#panelBar", navTab.getCurrentPanel()),
			"getOperationsByMenuId", "200", false, "panelBar");
});
//-->
</script>
<div class="pageContent">
	<div class="panelBar" id="panelBar">
	</div>
	<table class="table" width="100%" layoutH="133">
		<thead>
			<tr>
				<th>名称</th> 
				<th>编码</th> 
				<th>序号</th> 
				<th>可增加</th> 
				<th>可编辑</th> 
				<th>可删除</th> 
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${itemList}" varStatus="s">
			<tr target="item_id" rel="${item.fdid}">
				<td>${item.dict_name}</td>
				<td>${item.dict_value}</td> 
				<td>${item.num}</td> 
				<c:choose>
					<c:when test="${item.can_add!='0'}">
						<td>是</td> 
					</c:when>
					<c:otherwise><td>否</td></c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${item.can_edit!='0'}">
						<td>是</td> 
					</c:when>
					<c:otherwise><td>否</td></c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${item.can_del!='0'}">
						<td>是</td> 
					</c:when>
					<c:otherwise><td>否</td></c:otherwise>
				</c:choose>
			</tr>
		</c:forEach>
		</tbody>
	</table>

<%-- 	<c:import url="../_frag/pager/panelBar.jsp"></c:import> --%>
</div>