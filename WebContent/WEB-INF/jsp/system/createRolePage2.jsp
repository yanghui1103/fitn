<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.bw.fit.common.model.*" pageEncoding="UTF-8"%><%@ include
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=basePath%>common/zTree/css/zTreeStyle/zTreeStyle.css"
	rel="stylesheet" type="text/css" media="screen" />
<script type="text/javascript"
	src="<%=basePath%>common/zTree/js/jquery.ztree.core-3.0.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>common/zTree/js/jquery.ztree.excheck-3.0.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>common/zTree/js/jquery.ztree.exedit-3.0.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>common/js/json2.js"></script>
<script type="text/javascript">
	$("button", navTab.getCurrentPanel()).click(function() {
		dwzConfirmFormToBack("是否确认新建角色?", function() {
			$("#roleFm", navTab.getCurrentPanel()).submit();
		}, function() {
		});
	});
</script>
</head>
<body>
	<div class="pageContent">
		<form id="roleFm" method=post
			action="<%=basePath%>system/createRole?navTabId=page102&callbackType=closeCurrent"
			class="pageForm required-validate"
			onsubmit="return validateCallback(this,navTabAjaxDone);">
			<div class="pageFormContent" layoutH="56">
				<p>
					<label>角色名称：</label> <input name="role_name" class="required"
						minlength="2" type="text" size="30" maxlength=30 />
				</p>
				<p>
					<label>父角色：</label> <select name="parent_id" class="combox">
						<option selected value="">请选择</option>
						<c:forEach var="item" items="${myRoles }">
							<option value="${item.fdid }">${item.role_name }</option>
						</c:forEach>
					</select>
				</p>

				<ul id="treeDemo" class="ztree"></ul>
			</div>
			<input name="fdid" value="${uuid}" type="hidden" /> <input
				id="treeJson" value="${treeJson}" type="hidden" />
			<div class="formBar" id="panelBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="button">保存</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>
	
<script type="text/javascript" >
<!--
var setting = {
		data : {
			key : {
				title : "t"
			},
			simpleData : {
				enable : true
			}
		},
		callback : {
			onMouseDown : onMouseDown
		},
		view : {
			fontCss : getFontCss
		}
	}; 
	alert($("#treeJson",navTab.getCurrentPanel()).length);
	var data = JSON.parse($("#treeJson",navTab.getCurrentPanel()).val());
	var zNodes = data.list;

	function focusKey(e) {
		if (key.hasClass("empty")) {
			key.removeClass("empty");
		}
	}
	function blurKey(e) {
		if (key.get(0).value === "") {
			key.addClass("empty");
		}
	}
	var lastValue = "", nodeList = [], fontCss = {};
	function clickRadio(e) {
		lastValue = "";
		searchNode(e);
	}
	function searchNode(e) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		if (!$("#getNodesByFilter").attr("checked")) {
			var value = $.trim(key.get(0).value);
			var keyType = "";
			if ($("#name").attr("checked")) {
				keyType = "name";
			} else if ($("#level").attr("checked")) {
				keyType = "level";
				value = parseInt(value);
			} else if ($("#id").attr("checked")) {
				keyType = "id";
				value = parseInt(value);
			}
			if (key.hasClass("empty")) {
				value = "";
			}
			if (lastValue === value)
				return;
			lastValue = value;
			if (value === "")
				return;
			updateNodes(false);

			if ($("#getNodeByParam").attr("checked")) {
				var node = zTree.getNodeByParam(keyType, value);
				if (node === null) {
					nodeList = [];
				} else {
					nodeList = [ node ];
				}
			} else if ($("#getNodesByParam").attr("checked")) {
				nodeList = zTree.getNodesByParam(keyType, value);
			} else if ($("#getNodesByParamFuzzy").attr("checked")) {
				nodeList = zTree.getNodesByParamFuzzy(keyType, value);
			}
		} else {
			updateNodes(false);
			nodeList = zTree.getNodesByFilter(filter);
		}
		updateNodes(true);

	}
	function updateNodes(highlight) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		for (var i = 0, l = nodeList.length; i < l; i++) {
			nodeList[i].highlight = highlight;
			zTree.updateNode(nodeList[i]);
		}
	}

	function onMouseDown(event, treeId, treeNode) {
		dataDictList(event, treeId, treeNode);
	}
	function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {
			color : "#A60000",
			"font-weight" : "bold"
		} : {
			color : "#333",
			"font-weight" : "normal"
		};
	}
	function filter(node) {
		return !node.isParent && node.isFirstNode;
	}

	var key;
	$(document)
			.ready(
					function() {
						alert("sssddd");
						$.fn.zTree.init($("#treeDemo"), setting, zNodes);
						key = $("#key");
						key.bind("focus", focusKey).bind("blur", blurKey).bind(
								"propertychange", searchNode).bind("input",
								searchNode);
						$("#name").bind("change", clickRadio);
						$("#level").bind("change", clickRadio);
						$("#id").bind("change", clickRadio);
						$("#getNodeByParam").bind("change", clickRadio);
						$("#getNodesByParam").bind("change", clickRadio);
						$("#getNodesByParamFuzzy").bind("change", clickRadio);
						$("#getNodesByFilter").bind("change", clickRadio);
					});

-->
</script>
</body>
</html>