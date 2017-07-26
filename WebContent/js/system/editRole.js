/**
 * 创建新的角色
 */ 

	var setting = {
		data: {
			key: {
				title:"t"
			},
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeClick: beforeClick,
			onClick: onClick
		}
	};
	var ztreeJson = $("#ztreeJson",navTab.getCurrentPanel()).val(); 
	var zNodes = "" ;
	ztreeJson = JSON.parse(ztreeJson); 
	if(ztreeJson.res =="2"){
		zNodes = ztreeJson.list;
	}
	 

	var log, className = "dark";
	function beforeClick(treeId, treeNode, clickFlag) {
		className = (className === "dark" ? "":"dark");
		showLog("[ "+getTime()+" beforeClick ]&nbsp;&nbsp;" + treeNode.name );
		return (treeNode.click != false);
	}
	function onClick(event, treeId, treeNode, clickFlag) {
		var fdid = $("#fdid",navTab.getCurrentPanel()).val();
		ajaxTodo("system/getEltCheckedOfRole/"+fdid+"/"+treeNode.id,function(data){
			if(data.res=="2"){
//				var obj = $('<label><input type=radio name=r1 value='+treeNode.id+' checked=checked />'+treeNode.name+'</label>');
//				$("#menuListR",navTab.getCurrentPanel()).append(obj);
				alert("okok");
			}
		});
	}		
	function showLog(str) {
		if (!log) log = $("#log");
		log.append("<li class='"+className+"'>"+str+"</li>");
		if(log.children("li").length > 8) {
			log.get(0).removeChild(log.children("li")[0]);
		}
	}
	function getTime() {
		var now= new Date(),
		h=now.getHours(),
		m=now.getMinutes(),
		s=now.getSeconds();
		return (h+":"+m+":"+s);
	}

	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	}); 

function changeParentRole(obj){
	var parent_role_id = obj.value ;
	ajaxTodo("system/getMenuTreeOfRole/"+parent_role_id,function(data){
		alert(data.res);
	});
}