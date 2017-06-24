<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.container {
	min-height:380px;
	min-width:50%;
	overflow:hidden;
}
.left_c {
	box-sizing:border-box;
	width:30%;
	float:left;
	height:502px;
	overflow:scroll;
	white-space:nowrap;
}
.right_c {
	box-sizing:border-box;
	width:70%;
	float:left;
	border:1px solid #ddd;
	background-color: #fff;
	font-size:14px
}
.search{
	margin:10px;
	overflow:hidden;
}
.search h2 {
	float:left;
	width:10%;
	line-height:28px;
}
.search_center {
	float:left;
	width:70%;
}
.search_center .search_input {
	display:block;
	width:90%;
	height:26px;
	line-height:26px;
	padding: 0 10px;
}
.search_center label {
	display:inline-block;
	margin: 5px 10px 0 0;
	line-height:26px;
}
.search_center label input {
	float:left;
	margin:6px 5px 0 0;
}
.search_btn {
	float:left;
	width:20%;
	height:28px;
	line-height:28px;
}
.main {
	border-top:1px solid #ddd;
	border-bottom:1px solid #ddd;
	overflow:hidden;
	height:420px;
}
.left {
	box-sizing:border-box;
	float:left;
	width:40%;
	overflow: scroll;
	height:280px;
}
.left h2 {
	font-size:14px;
	line-height:26px;
	font-weight:bold;
	border-bottom:1px solid #ddd;
	background-color:#f5f5f5;
	padding-left:10px;
}
.left li {
	position:relative;
	line-height:22px;
	padding:0 10px;
	white-space:nowrap;
}
.left li.active, .left li.active:hover {
	background-color: #7cc5e5;
	border-color:#b8d0d6;
}
.left li:hover {
	background-color: #f5f5f5;
	border-color:#ddd;
}

.middle {
	float:left;
	box-sizing:border-box;
	width:20%;
	text-align:center;
	border-left:1px solid #ddd;
	border-right:1px solid #ddd;
	border-bottom:1px solid #ddd;
	min-height:280px;
}
.middle button {
	width:70%;
	margin:20px auto 0;
}
.btn {
	float:left;
	height:30px;
	width:100%;
	text-align:center;
	margin:10px 0 0;
}
.ms {
	box-sizing:border-box;
	float:left;
	width:90%;
	border:1px solid #ddd;
	padding:10px;
	margin:10px 5%;
	line-height:24px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="left_c">
			<li class="active">杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
		</div>
		<div class="right_c">
			<div class="search">
				<h2>关键字</h2>
				<div class="search_center">
					<input class="search_input" type="text" placeholder="输入关键字">
					<label><input type="checkbox">机构</label>
					<label><input type="checkbox">机构</label>
					<label><input type="checkbox">机构</label>
					<label><input type="checkbox">机构</label>
				</div>
				<button class="search_btn">搜索</button>
			</div>
			<div class="main">
				<div class="left">
					<h2>待选列表</h2>
					<ul>
						<li class="active">杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管杨慧-辅助系统你哦管杨慧-辅助系统你哦管杨慧-辅助系统你哦管杨慧-辅助系统你哦管杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
					</ul>
				</div>
				<div class="middle">
					<button>添加</button>
					<button>添加</button>
					<button>添加</button>
					<button>添加</button>
				</div>
				<div class="left right">
					<h2>已选列表</h2>
					<ul>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
						<li>杨慧-辅助系统你哦管</li>
					</ul>
				</div>
				
				<div class="btn">
					<button>确认</button>
					<button>确认</button>
				</div>
				<div class="ms">
					辅助系统你哦管辅助系统你哦管辅助系统你哦管辅助系统你哦管辅助系统你哦管辅助系统你哦管辅助系统你哦管辅助系统你哦管辅助系统你哦管
				</div>
			</div>
		</div>
	</div>
</html>
</body>
</html>