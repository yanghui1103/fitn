<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=basePath%>common/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet"
	type="text/css" media="screen" />
<script type="text/javascript" src="<%=basePath%>common/zTree/js/jquery.ztree.core-3.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>common/zTree/js/jquery.ztree.excheck-3.0.min.js"></script>	
<script type="text/javascript" src="<%=basePath%>common/zTree/js/jquery.ztree.exedit-3.0.min.js"></script>		
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	 	<SCRIPT type="text/javascript">
		<!--
		var setting = {
			data: {
				key: {
					title: "t"
				},
				simpleData: {
					enable: true
				}				
			},
			view: {
				fontCss: getFontCss
			}
		};
		var data = JSON.parse($("#ztreeJson").val());
		var zNodes = data.list ;

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
				if (lastValue === value) return;
				lastValue = value;
				if (value === "") return;
				updateNodes(false);

				if ($("#getNodeByParam").attr("checked")) {
					var node = zTree.getNodeByParam(keyType, value);
					if (node === null) {
						nodeList = [];
					} else {
						nodeList = [node];
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
			for( var i=0, l=nodeList.length; i<l; i++) {
				nodeList[i].highlight = highlight;
				zTree.updateNode(nodeList[i]);
			}
		}
		function getFontCss(treeId, treeNode) {
			return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
		}
		function filter(node) {
			return !node.isParent && node.isFirstNode;
		}

		var key;
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			key = $("#key");
			key.bind("focus", focusKey)
			.bind("blur", blurKey)
			.bind("propertychange", searchNode)
			.bind("input", searchNode);
			$("#name").bind("change", clickRadio);
			$("#level").bind("change", clickRadio);
			$("#id").bind("change", clickRadio);
			$("#getNodeByParam").bind("change", clickRadio);
			$("#getNodesByParam").bind("change", clickRadio);
			$("#getNodesByParamFuzzy").bind("change", clickRadio);
			$("#getNodesByFilter").bind("change", clickRadio);
		});
		//-->
	</SCRIPT>
	<style type="text/css">
	.div{ border:3px solid #000;padding:4px} 
	</style>
</HEAD>

<BODY>
<input   type="hidden" id="ztreeJson"  value=${dataDictTreeJson} />
<div style=" margin-left: 2px;margin-top: 2px;">
<div layoutH="56" style="float:left; display:block; overflow:auto; width:30%; border:solid 1px #CCC; line-height:21px; background:#fff">	
			<ul id="treeDemo" class="ztree" ></ul>
				</div>	 
</div>				
</BODY>
</HTML>