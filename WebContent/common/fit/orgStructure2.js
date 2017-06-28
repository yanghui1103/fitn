 
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
		var log, className = "dark";
		function beforeClick(treeId, treeNode, clickFlag) {
			className = (className === "dark" ? "":"dark");
			showLog("[ "+getTime()+" beforeClick ]&nbsp;&nbsp;" + treeNode.name );
			return (treeNode.click != false);
		}
		function onClick(event, treeId, treeNode, clickFlag) { 
			$("#keyWords",$.pdialog.getCurrent()).val("");
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
	$.pdialog.resizeDialog({
		style : {
			height : 543,
			width : 750 
		}
	}, $.pdialog.getCurrent(), ""); 
	
	
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
		var selectMulti = $("#selectMulti",$.pdialog.getCurrent()).val() ;
		var len = $("#right_ul li",$.pdialog.getCurrent()).length ;  
		var fdidv = $this.attr("data-id");
		var dname = $this.attr("data-name") ;
		var remark = $this.attr("remark") ;
		if("false"==selectMulti && len==0 ){	 
			$("#right_ul",$.pdialog.getCurrent()).empty();
			$("#right_ul",$.pdialog.getCurrent()).append($('<li class="active" ondblclick="javascript:this.remove()" data-id="'+fdidv+'" value="'+fdidv+'" data-name="'+dname+'" remark="'+remark+'">'+dname+'</li>')) ;
			createTempRelation();
		}else if("false"==selectMulti && len > 0 ){		 
			$("#right_ul",$.pdialog.getCurrent()).empty();
			$("#right_ul",$.pdialog.getCurrent()).append($('<li class="active" ondblclick="javascript:this.remove()"  data-id="'+fdidv+'" value="'+fdidv+'" data-name="'+dname+'" remark="'+remark+'">'+dname+'</li>')) ;
			createTempRelation();
		}else {
			$("#right_ul",$.pdialog.getCurrent()).append($('<li class="active" ondblclick="javascript:this.remove()"  data-id="'+fdidv+'" value="'+fdidv+'" data-name="'+dname+'" remark="'+remark+'">'+dname+'</li>')) ;
		}
	});
 
});

function returnSelected(){
	var  array = createTempRelation(); 
	$.bringBack({ids:array[0], names:array[1]});
}

function createTempRelation(){
	var ids = "";
	var names = "";
	var length = $("#right_ul li",$.pdialog.getCurrent()).length ;
	for(var i=0;i<length;i++){
		ids = ids + $("#right_ul li",$.pdialog.getCurrent()).eq(i).attr("data-id") + "-" ;
		names = names + $("#right_ul li",$.pdialog.getCurrent()).eq(i).attr("data-name") + "-" ;
	}
	var e = $(".elementId",$.pdialog.getCurrent()).attr("data-fdid") ;

	if(length>0){
		ajaxTodo($("#basePathOfSys").val()+ "system/insertTempRelation/"+ $("#uuid",$.pdialog.getCurrent()).val() +"/"+ids+"/"+e);
	}
	var array = new Array(2);
	array[0] = ids ;
	array[1] = names ;
	return array ;
}