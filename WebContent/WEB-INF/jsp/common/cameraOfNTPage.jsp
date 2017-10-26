<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="Decoder.BASE64Encoder" pageEncoding="UTF-8"%><%@ include
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
<script type="text/javascript"
	src="<%=basePath%>common/photo/lib/jquery.mousewheel.pack.js?v=3.1.3"></script>
<!-- Add fancyBox main JS and CSS files -->
<script type="text/javascript"
	src="<%=basePath%>common/photo/source/jquery.fancybox.pack.js?v=2.1.5"></script>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>common/photo/source/jquery.fancybox.css?v=2.1.5"
	media="screen" />
<!-- Add Button helper (this is optional) -->
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>common/photo/source/helpers/jquery.fancybox-buttons.css?v=1.0.5" />
<script type="text/javascript"
	src="<%=basePath%>common/photo/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>
<!-- Add Thumbnail helper (this is optional) -->
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>common/photo/source/helpers/jquery.fancybox-thumbs.css?v=1.0.7" />
<script type="text/javascript"
	src="<%=basePath%>common/photo/source/helpers/jquery.fancybox-thumbs.js?v=1.0.7"></script>
<!-- Add Media helper (this is optional) -->
<script type="text/javascript"
	src="<%=basePath%>common/photo/source/helpers/jquery.fancybox-media.js?v=1.0.6"></script>

<style type="text/css">
.div-a {
	float: left;
	width: 50%;
	border: 0px solid #000;
}

.div-b {
	float: left;
	width: 50%;
	height: 100%;
	border: 0px solid #000;
}
</style>

<script language="JavaScript">
	function SetRotateWeb(angle) {
		testocx.setRotate(angle);
		SealCapBox.disabled = 0;
		MessageCtrl.value = "旋转成功。";
	}

	function PandaCapPlay() {
		testocx.StopPreview();
		testocx.PandaCapturePlay();
		setRotate90.disabled = 1;
		setRotate270.disabled = 1;
		SealCapBox.disabled = 0;
		ConfigBox.disabled = 0;
		MessageCtrl.value = "操作成功。";
	}

	function jsStartPreviewProc() {
		try {
			testocx.StopPreview();
			ret = testocx.PandaCapturePlay();
			testocx.ManualToAuto();
		} catch (e) {
			window.alert(e); //"加载DLL错误！");
		}
		if (ret == 0) {
			//ok
			setRotate90.disabled = 0;
			setRotate270.disabled = 0;
			SealCapBox.disabled = 0;
			ConfigBox.disabled = 0;
			MessageCtrl.value = "操作成功。";
		} else {
			SelectBox.disabled = 0;
			setRotate90.disabled = 1;
			setRotate270.disabled = 1;
			SealCapBox.disabled = 0; //1;
			PreviewBox.disabled = 0; //0;
			ConfigBox.disabled = 1;
			MessageCtrl.value = "操作失败。";
		}
	}

	function Merge() {
		try {
			ret = testocx.MergeImage();
		} catch (e) {
			window.alert(e); //"加载DLL错误！");
		}

		txtTraceBox.disabled = 0;
		MessageCtrl.value = "返回值:" + ret;
		if (ret == 0) {
			MessageCtrl.value = "操作成功。";
			FileNameCtrl.value = testocx.cPicName;
			var sel = document.getElementById('s3');
			for (var x = sel.length - 1; x >= 0; x--) {
				sel.options[x].removeNode(true);
			}
			var format = document.getElementById('s4').value;
			var inum = testocx.RefreshImgList(format);
			var cstr = "";
			for (i = 0; i < inum; i++) {
				cstr = testocx.GetImgNameByIdx(i);
				if (cstr != "") {
					var opt = document.createElement("OPTION");
					sel.options.add(opt);
					opt.innerText = cstr;
					opt.value = i;
				}
			}
		} else {
			MessageCtrl.value = "操作失败。";
		}
	}

	function SendSealWeb() {
		if (txtTrace.value == "") {
			MessageCtrl.value = " 请先输入URL！";
			return;
		}

		if (checkbox1.checked == true) {
			testocx.bUploadDel = true;
		} else {
			testocx.bUploadDel = false;
		}

		try {
			txtTraceBox.disabled = 1;
			ret = testocx.SendSeal(txtTrace.value);
		} catch (e) {
			window.alert(e); //"加载DLL错误！");
		}

		txtTraceBox.disabled = 0;
		if (ret == 1) {
			var sel = document.getElementById('s3');
			for (var x = sel.length - 1; x >= 0; x--) {
				sel.options[x].removeNode(true);
			}
			var format = document.getElementById('s4').value;
			var inum = testocx.RefreshImgList(format);
			var cstr = "";
			for (i = 0; i < inum; i++) {
				cstr = testocx.GetImgNameByIdx(i);
				if (cstr != "") {
					var opt = document.createElement("OPTION");
					sel.options.add(opt);
					opt.innerText = cstr;
					opt.value = i;
				}
			}
			FileNameCtrl.value = "本地文件名:" + testocx.cPicName;
			MessageCtrl.value = "操作成功。";
		} else {
			MessageCtrl.value = "操作失败。";
		}
	}

	function JsRefreshDevList() {
		testocx.WriteLog = true;
		var sel = document.getElementById('s1');
		var inum = testocx.RefreshDevList();
		var i = 0;
		var cstr = "";
		for (i = 0; i < inum; i++) {
			cstr = testocx.GetDevNameByIdx(i);
			if (cstr != "") {
				var opt = document.createElement("OPTION");
				sel.options.add(opt);
				opt.innerText = cstr;
				opt.value = i;
				if (cstr == "主摄像头(2M)") {
					opt.selected = true;
					testocx.SetDevNameSel(i);
				}
			}
		}
	}

	function JsSelDevName() {

		MessageCtrl.value = "";

		try {
			var ret = 0;
			var i;
			testocx.StopPreview();
			i = document.getElementById('s1').value;
			ret = testocx.SetDevNameSel(i);
			var sel = document.getElementById('s2');
			for (var x = 0; x < sel.length; x++) {
				sel.options[x].removeNode(true);
			}
			JsRefreshResolution();
			jsStartPreviewProc();
		} catch (e) {
			window.alert(e); //"加载DLL错误！");
		}
	}

	function JsSelResolution() {

		MessageCtrl.value = "";

		try {
			var ret = 0;
			var i;

			testocx.StopPreview();

			i = document.getElementById('s2').value;
			ret = testocx.SetResolution(i);

		} catch (e) {
			window.alert(e); //"加载DLL错误！");
		}
	}

	function JsRefreshResolution() {
		var sel = $('#s2');
		var i = $('#s1').val();
		var inum = testocx.RefreshResolution(i);
		var cstr = "";
		for (i = 0; i < inum; i++) {
			cstr = testocx.GetResolutionByIdx(i);
			if (cstr != "") {
				var opt = document.createElement("OPTION");
				sel.append(opt);
				opt.innerText = cstr;
				opt.value = i;
			}
		}
	}

	function ConfigCamera() {

		ret = testocx.ConfigCameraFilter();
	}

	function AddWaterMark() {
		ret = testocx.AddWaterMark();
	}

	function RecordVideo() {
		ret = testocx.VideoRecord("D:\\");
	}

	var ImgIdx = -1;
	function JsSelImgName() {
		ImgIdx = document.getElementById('s3').value;
	}

	function jsDeleteImg() {
		if (ImgIdx >= 0)
			ret = testocx.DeleteImg(ImgIdx);
		var sel = document.getElementById('s3');
		for (var x = sel.length - 1; x >= 0; x--) {
			sel.options[x].removeNode(true);
		}
		var format = document.getElementById('s4').value;
		var inum = testocx.RefreshImgList(format);
		var cstr = "";
		for (i = 0; i < inum; i++) {
			cstr = testocx.GetImgNameByIdx(i);
			if (cstr != "") {
				var opt = document.createElement("OPTION");
				sel.options.add(opt);
				opt.innerText = cstr;
				opt.value = i;
			}
		}
	}

	function JsSelFormat() {
		var sel = document.getElementById('s3');
		for (var x = sel.length - 1; x >= 0; x--) {
			sel.options[x].removeNode(true);
		}
		var format = document.getElementById('s4').value;
		var inum = testocx.RefreshImgList("format");
		var cstr = "";
		for (i = 0; i < inum; i++) {
			cstr = testocx.GetImgNameByIdx(i);
			if (cstr != "") {
				var opt = document.createElement("OPTION");
				sel.options.add(opt);
				opt.innerText = cstr;
				opt.value = i;
			}
		}
	}

	function jsScreenCut() {
		testocx.ScreenCut();
	}

	function jsMtoA() {
		testocx.ManualToAuto();
	}

	function jsDisCrop() {
		testocx.DisableCrop();
	}

	function ImgToBase64() {
		testocx.markdown = true;
		ret = testocx.GetBase64String(testocx.cPicName);
		if (ret == true) {
			MessageCtrl.value = "转换成功，字符串保存在Temp目录的日志文件SealCapNtWebLog.txt中。";
		}
	}

	function jsDeleteImgByName() {
		ret = testocx.DeleteImgByName(DeleteImgName.value);
		if (ret == 0) {
			MessageCtrl.value = "删除成功。";
		} else {
			MessageCtrl.value = "文件不存在。";
		}
	}

	function jsSelLocalImg() {
		ret = testocx.SelectLocalImgs();
		var sel = document.getElementById('s6');
		for (var x = sel.length - 1; x >= 0; x--) {
			sel.options[x].removeNode(true);
		}
		var inum = testocx.RefreshLocalImg();
		var cstr = "";
		for (i = 0; i < inum; i++) {
			cstr = testocx.GetLocalImgName(i);
			if (cstr != "") {
				var opt = document.createElement("OPTION");
				sel.options.add(opt);
				opt.innerText = cstr;
				opt.value = i;
			}
		}
	}

	var LocalImgIdx = -1;
	function JsLocalImgName() {
		LocalImgIdx = document.getElementById('s6').value;
	}

	function jsUploadLocalImg() {
		if (txtTrace.value == "") {
			MessageCtrl.value = " 请先输入URL！";
			return;
		}
		testocx.bShowMsgBox = false;
		ret = testocx.UploadLocalImg(txtTrace.value);
		MessageCtrl.value = "函数返回值：" + ret + "  服务器返回:" + testocx.HttpResponse;
	}

	function jsRemoveLocalImg() {
		if (LocalImgIdx >= 0)
			ret = testocx.RemoveLocalImg(LocalImgIdx);
		var sel = document.getElementById('s6');
		for (var x = sel.length - 1; x >= 0; x--) {
			sel.options[x].removeNode(true);
		}
		var inum = testocx.RefreshLocalImg();
		var cstr = "";
		for (i = 0; i < inum; i++) {
			cstr = testocx.GetLocalImgName(i);
			if (cstr != "") {
				var opt = document.createElement("OPTION");
				sel.options.add(opt);
				opt.innerText = cstr;
				opt.value = i;
			}
		}
	}
	 

	function SealCapWeb() {
		FileNameCtrl.value = "";
		if ($("#checkbox").checked == true) {
			MergeBox.disabled = 0;
			ret = testocx.IsMerge();
		} else {
			MergeBox.disabled = 1;
		}
		testocx.customSavePath = customTrace.value;
		testocx.customImgName = customName.value;
		try {
			var format = document.getElementById('s4').value;
			var DPI = document.getElementById('s5').value;
			testocx.pDPI = DPI;
			testocx.nColorSpace = 0;
			testocx.nRotateAngle = 0;
			ret = testocx.SealCap(format);
			var sel = document.getElementById('s3');
			for (var x = sel.length - 1; x >= 0; x--) {
				sel.options[x].removeNode(true);
			}
			var inum = testocx.RefreshImgList(format);
			var cstr = "";
			for (i = 0; i < inum; i++) {
				cstr = testocx.GetImgNameByIdx(i);
				if (cstr != "") {
					var opt = document.createElement("OPTION");
					sel.options.add(opt);
					opt.innerText = cstr;
					opt.value = i;
				}
			}
		} catch (e) {
			window.alert(e); //"加载DLL错误！");
		}

		txtTraceBox.disabled = 0;
		MessageCtrl.value = "返回值:" + ret;
		if (ret == 0) {
			VerifyBox.disabled = 0;
			AddWater.disabled = 0;
			MessageCtrl.value = "操作成功。";
			var txt = testocx.cPicName;
			FileNameCtrl.value = txt;
			$('input[type="file"]').attr("value", txt);
			// 		var WshShell=new ActiveXObject("WScript.Shell");      
			// 		document.getElementById('file').focus(); 
			// 		alert(txt);
			// 		WshShell.SendKeys(txt); 
			testocx.markdown = false; // false的话，日志里就不存base64编码了。
			ret = testocx.GetBase64String(testocx.cPicName); 
			$("#photo_bs64", navTab.getCurrentPanel()).val(testocx.Img2Base64);
			var fid = ($("#foreign_id", navTab.getCurrentPanel()).val());
			var bs64 = ($("#photo_bs64", navTab.getCurrentPanel()).val());  
 			
			var data2 = $('#ntfm', navTab.getCurrentPanel()).serializeArray();   
			var path = $("#basePathOfSys").val()+"system/createPhotoAttment" ; 
			$.ajax({ 
				  url:path,
				  type: "POST",
				  data : data2,
				  success:function(result){
					  if("1"==result.res){
						  alertMsg.error(result.msg);
						  return ;
					  }
					  
					  var $tr = $("<tr>");
					  $tr.addClass("hover");
					  
						 
					  var $td1 = $("<td>"+result.file_name+"</td>");
					  var fdid = "'"+ result.fdid +"'";

					  if("0" == ${isReadOnly}){
					  	var $td2 = $("<td><button type=button onclick=\"delAttachment(this,"+fdid+");\">删除</button>"
					  	+"<button type=button onclick=\"lookPhotoAtt('"+result.file_name+"');\">预览</button>"+"</td>");
					  }else{
						  	var $td2 = $("<td><div class=gallery><a href="+$("#basePathOfSys").val()+"common/attachmentDir/"+ result.file_name + "   data-fancybox-group=gallery   class=fancybox>浏览</a></td>");
					  }
					  $td1.appendTo($tr) ;
					  $td2.appendTo($tr) ;
					  $('#photo_list', navTab.getCurrentPanel()).append($tr); 
				  }
				  }); 
		} else {
			MessageCtrl.value = "操作失败。";
		}

	}

	function delAttachment(obj,fdid){
		var $this = $(obj);
		ajaxTodo($("#basePathOfSys").val()+"system/deleteAttahment/"+fdid,function(data){
			if(data.res =="2"){
				$this.parents("tr").remove();
			}else{
				alertMsg.error(data.msg);
			}			
		});
	}
	function lookPhotoAtt(fileName){
		$.pdialog.open($("#basePathOfSys").val()+"common/attachmentDir/4779e0e29ccf400c8b8686519dab2fc0.JPG"
				, "d555", "title");


	}
	$(function() {
		if("0" == ${isReadOnly}){
			JsRefreshDevList();
			JsRefreshResolution();
			alertMsg.info("摄像头启动中...");
			jsStartPreviewProc();
		}
	});
</script>
</head>
<body class="dfg">
	<div class="div-a">
		<c:if test="${isReadOnly=='0' }">
			<div>
				<object classid="clsid:1C68DF21-EFEC-4623-85E5-0C369B95F15E"
					width=600 height=500 hspace="3" vspace="3" id="testocx"
					codebase="<%=basePath%>common/ocx/SealCapNtWeb.cab#version=1,7,0,0">
				</object>
			</div>

			<p>
				<label>请选择摄像头：</label> <select name="SelectBox" size="1" id="s1"
					onChange="JsSelDevName()">
				</select> <input type="button" name="PreviewBox" value="打开摄像头"
					onClick="jsStartPreviewProc()"> <input name="SealCapBox"
					type="button" onClick="SealCapWeb()" value="拍摄并上传 " />
			</p>
			<div style='display: none'>
				<input id=FileNameCtrl name=txtControl2 readonly
					style="WIDTH: 600; height: 19" size="18">
				<p>
					<input type="button" name="setRotate90" value="顺时针方向旋转90°" disabled
						onClick="SetRotateWeb(90)"> <input type="button"
						name="setRotate270" value="逆时针方向旋转90°" disabled
						onClick="SetRotateWeb(360-90)"> <input type="button"
						name="ConfigBox" value="设置视频参数" disabled onClick="ConfigCamera()">
					<input type="checkbox" name="checkbox" id="checkbox"
						value="checkbox"> <input type="button" name="MergeBox"
						value="合并图片" disabled onClick="Merge()"> <input
						name="VerifyBox" type="button" disabled id="Verify"
						onClick="SendSealWeb()" value="HTTP上传">
				<p>
					自定义图片保存路径:<input id=customTrace name=customTraceBox
						style="WIDTH: 500; height: 19" size="20" maxlength="100">
					自定义图片名称(不含后缀):<input id=customName name=customNameBox
						style="WIDTH: 100; height: 19" size="20" maxlength="100">
				<p>
					请选择分辨率： <select name="SelectResolutionBox" size="1" id="s2"
						onChange="JsSelResolution()">
					</select> 请输入URL: <input id=txtTrace name=txtTraceBox
						style="WIDTH: 500; height: 19" size="20" maxlength="100">
					<input type="checkbox" name="checkbox1" value="checkbox1">
					<input type="button" name="Record" value="  录像  "
						onClick="RecordVideo()"> <input type="button"
						name="AddWater" value="添加水印" disabled onClick="AddWaterMark()">
				</p>
				<p>
					操作返回信息: <input id=MessageCtrl name=txtControl1 readonly
						style="WIDTH: 300; height: 19" size="20">
				</p>
				<p>
					图像文件名称: <input type="button" name="ToBase64" value="  转Base64字符串  "
						onClick="ImgToBase64()">
				</p>
				<p>
					拍照文件列表: <select name="SelectImgBox" size="5" id="s3"
						style="overflow: scroll" onChange="JsSelImgName()">
				</p>
				<p>
					操作返回信息: <input id=MessageCtrl name=txtControl1 readonly
						style="WIDTH: 300; height: 19" size="20">
				</p>
				请选择图片输出格式： <select name="SelectFormatBox" size="1" id="s4"
					onChange="JsSelFormat()">
					<option value="jpg" selected="selected">jpg</option>
					<option value="bmp">bmp</option>
					<option value="tif">tif</option>
					<option value="gif">gif</option>
				</select> 请选择图片输出DPI： <select name="SelectDPIBox" size="1" id="s5">
					<option value=0>默认值</option>
					<option value=72>72</option>
					<option value=200 selected="selected">200</option>
					<option value=300>300</option>
				</select>
			</div>
		</c:if>
		<form id="ntfm" method=post>
			<input type="hidden" id="photo_bs64" name="temp_str1" /> <input
				type="hidden" id="foreign_id" value="${foreignId }"
				name="foreign_id" />
		</form>
	</div>
	<div class="pageContent div-b">
		<div class="panelBar" id="panelBar"></div>
		<table class="table" width="100%" layoutH="133">
			<thead>
				<tr>
					<th width="50%">名称</th>
					<th width="50%">操作</th>
				</tr>
			</thead>
			<tbody id="photo_list">
			</tbody>
		</table>
	</div>
</body>
</html>
