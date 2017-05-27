<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=basePath %>common/fit/mainPage.js"></script>
<style>
.textone {
        overflow: hidden;
        text-overflow: hidden;
        display: -webkit-box;
        line-height: 25px;    
        max-height: 25px;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical; 
    }
.div-a {
	float: left;
	width: 70%;
	border: 0px solid #000
}

.div-b {
	float: left;
	width: 30%;
	border: 0px solid #000
}
 
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="panel  div-a" minH="100" defH="180">
		<h1>
			安全问题（ <a href="gotoIframePage/question/fileIssueListPage"
				target="navTab"><u>更多</u></a>）
		</h1>
		<div>
			<table class="list"   width="100%"   id="table1" style="table-layout:fixed;">
				<thead>
					<tr>
						<th width="60%">问题</th>
						<th width="30%">责任部门</th>
						<th width="10%">整改限期</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach var="item" items="${issueList}" varStatus="s">
						<tr target="itemId" rel="${item.fdid}">
							<td><a style="color: black; font-size: 12.4px;"
								target="navTab" rel="pag01"
								href="issue/openOldDetail/ ${item.fdid}">${item.temp_str3}(${item.issue_address})</a></td>
							<td>${item.issue_org}</td>
							<td>${item.issue_end_date}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>


	<div class="panel  div-b " minH="100" defH="180">
		<h1>待办</h1>
		<div>
			<table class="list" width="100%">
				<thead>
					<tr>
						<th>主题</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach var="item" items="${toDoList}" varStatus="s">
						<tr target="itemId" rel="${item.fdid}">
							<td><a style="color: black; font-size: 12.4px;"
								target="navTab" rel="pag01" href="${item.temp_str1}">${item.temp_str3}(${item.issue_address})</a></td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<!--  over-->

	<div class="panel  div-a" minH="100" defH="330">
		<h1>
			逾期问题（ <a href="gotoIframePage/question/oldIssueListPage"
				target="navTab"><u>更多</u></a>）
		</h1>
		<div>
			<table class="list"  width="100%"  id="table1">
				<thead>
					<tr>
						<th width="60%">问题</th>
						<th width="30%">责任部门</th>
						<th width="10%">整改限期</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach var="item" items="${oldIssueList}" varStatus="s">
						<tr target="itemId" rel="${item.fdid}">
							<c:choose>
								<c:when test="${item.temp_int1 >-8 && item.temp_int1 <=0}">
									<td>
									<a style="font-size: 12.5px;"
										target="navTab" rel="pag01"
										href="issue/openOldDetail/ ${item.fdid}"><font color=#BF8F00>${item.temp_str3}(${item.issue_address})</font></a>
								</c:when>
								<c:when test="${item.temp_int1 > 0}">
									<td>
									<a style="font-size: 12.5px;"
										target="navTab" rel="pag01"
										href="issue/openOldDetail/ ${item.fdid}"><font color=red>${item.temp_str3}(${item.issue_address})</font></a>
									</td>
								</c:when>
								<c:otherwise>
									<td><a style="font-size: 12.5px;"
										target="navTab" rel="pag01"
										href="issue/openOldDetail/ ${item.fdid}">${item.temp_str3}(${item.issue_address})</a></td>
								</c:otherwise>
							</c:choose>

							<td>${item.issue_org}</td>
							<td>${item.issue_end_date}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div class="panel  div-b" minH="100" defH="330">
		<h1>
			督办列表（ <a href="gotoIframePage/supervise/superviseListPage"
				target="navTab"><u>更多</u></a>）
		</h1>
		<div>
			<table class="list" width=100% >
				<thead>
					<tr>
						<th width="80%">主题</th> 
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${superviseslist}" varStatus="s">
						<tr target="superviseId" rel="${item.fdid}">
							<td><a style="color: black; font-size: 12.4px;"
								href="supervise/detailSuperviseInfo/${item.fdid}"
								target="navTab" rel="superviseDetail">${item.supervise_title}</a></td>
							
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	
</body>
</html>