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
		return (treeNode.click != false);
	}
	

	function sss(){
		alert("sssddd");
	}
	function tackOff(fdid,obj,model){ 
		 ajaxTodo("alloctAuthority/"+model+"/"+fdid+"/"+obj.value+"/"+obj.checked); 
	}
	
	function onClick(event, treeId, treeNode, clickFlag) { 
		if((treeNode.isParent)   ){return ;}
		
		$("#temp_str4",navTab.getCurrentPanel()).val(treeNode.id) ;
		
		var fdid = $("#fdid",navTab.getCurrentPanel()).val();
		var url = "system/getEltCheckedOfRole/"+fdid+"/"+treeNode.id ; 
		$("#menuListR",navTab.getCurrentPanel()).empty();
		$("#operationListR",navTab.getCurrentPanel()).empty();
		$("#elementListR",navTab.getCurrentPanel()).empty();
		$("#attListR",navTab.getCurrentPanel()).empty();
		ajaxTodo(ctx+url,function(data){
			if(data.hasMenu=="1"){ // 角色有这个菜单的话
				var obj = $('<label><input type=checkbox onchange=tackOff("'+fdid+'",this,"menu")  name=temp_arr1 value='+treeNode.id+' checked=checked />'+treeNode.name+'</label>');
				$("#menuListR",navTab.getCurrentPanel()).append(obj); 
			}else{
				var obj = $('<label><input type=checkbox onchange=tackOff("'+fdid+'",this,"menu")  name=temp_arr1 value='+treeNode.id+'  />'+treeNode.name+'</label>');
				$("#menuListR",navTab.getCurrentPanel()).append(obj); 
			}
			if(data.res=="2"){
				var list_operation = data.list_operation ; 
				for(var i=0;i<list_operation.length;i++){ 
					if("1"==list_operation[i].checked){
						var obj = $('<label><input type=checkbox onchange=tackOff("'+fdid+'",this,"operation")  name=temp_arr2 value='+list_operation[i].id+' checked=checked />'+list_operation[i].name+'</label>');
						$("#operationListR",navTab.getCurrentPanel()).append(obj); 
					}else{
						var obj = $('<label><input type=checkbox onchange=tackOff("'+fdid+'",this,"operation")  name=temp_arr2 value='+list_operation[i].id+'  />'+list_operation[i].name+'</label>');
						$("#operationListR",navTab.getCurrentPanel()).append(obj); 
					}
				} // 功能结束
				

				var list_elements = data.list_elements ; 
				for(var i=0;i<list_elements.length;i++){
					if("1"==list_elements[i].checked){
						var obj = $('<label><input type=checkbox onchange=tackOff("'+fdid+'",this,"element")  name=temp_arr3 value='+list_elements[i].id+' checked=checked />'+list_elements[i].name+'</label>');
						$("#elementListR",navTab.getCurrentPanel()).append(obj); 
					}else{
						var obj = $('<label><input type=checkbox onchange=tackOff("'+fdid+'",this,"element")  name=temp_arr3 value='+list_elements[i].id+'  />'+list_elements[i].name+'</label>');
						$("#elementListR",navTab.getCurrentPanel()).append(obj); 
					}
				} //页面元素

				var list_att = data.list_att ; 
				for(var i=0;i<list_att.length;i++){
					if("1"==list_att[i].checked){
						var obj = $('<label><input type=checkbox onchange=tackOff("'+fdid+'",this,"att")  name=temp_arr4 value='+list_att[i].id+' checked=checked />'+list_att[i].name+'</label>');
						$("#attListR",navTab.getCurrentPanel()).append(obj); 
					}else{
						var obj = $('<label><input type=checkbox onchange=tackOff("'+fdid+'",this,"att")  name=temp_arr4 value='+list_att[i].id+'  />'+list_att[i].name+'</label>');
						$("#attListR",navTab.getCurrentPanel()).append(obj); 
					}
				} //附件
			}
		});
	}		
	function getAllChildrenNodes(treeNode,result){
	      if (treeNode.isParent) {
	        var childrenNodes = treeNode.children;
	        if (childrenNodes) {
	            for (var i = 0; i < childrenNodes.length; i++) {
	                result += ',' + childrenNodes[i].id;
	                result = getChildNodes(childrenNodes[i], result);
	            }
	        }
	    }
	    return result;
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