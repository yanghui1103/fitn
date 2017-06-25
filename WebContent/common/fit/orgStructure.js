﻿ 
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

		var zNodes = $("#orgTreeJSON",$.pdialog.getCurrent()).val();
		zNodes = JSON.parse(zNodes);
		zNodes = zNodes.list ;
//		var zNodes =[
//			{ id:1, pId:0, name:"普通的父节点", t:"我很普通，随便点我吧", open:true},
//			{ id:11, pId:1, name:"叶子节点 - 1", t:"我很普通，随便点我吧"},
//			{ id:12, pId:1, name:"叶子节点 - 2", t:"我很普通，随便点我吧"},
//			{ id:13, pId:1, name:"叶子节点 - 3", t:"我很普通，随便点我吧我很普通，随便点我吧我很普通，随便点我吧我很普通，随便点我吧我很普通，随便点我吧我很普通，随便点我吧我很普通，随便点我吧"},
//			{ id:2, pId:0, name:"NB的父节点", t:"点我可以，但是不能点我的子节点，有本事点一个你试试看？", open:true},
//			{ id:21, pId:2, name:"叶子节点2 - 1", t:"你哪个单位的？敢随便点我？小心点儿..", click:false},
//			{ id:22, pId:2, name:"叶子节点2 - 2", t:"我有老爸罩着呢，点击我的小心点儿..", click:false},
//			{ id:23, pId:2, name:"叶子节点2 - 3", t:"好歹我也是个领导，别普通群众就来点击我..", click:false},
//			{ id:3, pId:0, name:"郁闷的父节点", t:"别点我，我好害怕...我的子节点随便点吧...", open:true, click:false },
//			{ id:31, pId:3, name:"叶子节点3 - 1", t:"唉，随便点我吧"},
//			{ id:32, pId:3, name:"叶子节点3 - 2", t:"唉，随便点我吧"},
//			{ id:33, pId:3, name:"叶子节点3 - 3", t:"唉，随便点我吧"}
//		];
		
		var log, className = "dark";
		function beforeClick(treeId, treeNode, clickFlag) {
			className = (className === "dark" ? "":"dark");
			showLog("[ "+getTime()+" beforeClick ]&nbsp;&nbsp;" + treeNode.name );
			return (treeNode.click != false);
		}
		function onClick(event, treeId, treeNode, clickFlag) { 
			$("#temp_str2",$.pdialog.getCurrent()).val(treeNode.id);
			$(".search_btn",$.pdialog.getCurrent()).trigger("click");
			//showLog("[ "+getTime()+" onClick ]&nbsp;&nbsp;clickFlag = " + clickFlag + " (" + (clickFlag===1 ? "普通选中": (clickFlag===0 ? "<b>取消选中</b>" : "<b>追加选中</b>")) + ")");
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
			$.fn.zTree.init($("#orgStrutsTree",$.pdialog.getCurrent()), setting, zNodes);
		}); 
		
function closeP(){
	$.pdialog.close($.pdialog.getCurrent()); 
}		


$(function(){
	$("li",$.pdialog.getCurrent()).click(function(){
		var $this = $(this);
		var remark = $this.attr("remark") ;
		remark = (remark==''||remark==undefined)?'无描述':remark;
		$("#userMiaoshu",$.pdialog.getCurrent()).text(remark);
		$("li",$.pdialog.getCurrent()).removeClass();
		$this.addClass("active");		
	});
	$("#left_d li",$.pdialog.getCurrent()).dblclick(function(){ 
		var $this = $(this);  
		$("#right_ul",$.pdialog.getCurrent()).append($('<li class="active" ondblclick="javascript:this.remove()" value="'+$this.val()+'" data-name="'+$this.attr("data-name")+'" remark="'+$this.attr("remark")+'">'+$this.attr("data-name")+'</li>')) ;
	});
 
});