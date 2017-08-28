// 系统默认分隔符 
var system_delimiter = '-';

function openwindow(url, name, iWidth, iHeight) {
	var url; // 转向网页的地址;
	var name; // 网页名称，可为空;
	var iWidth; // 弹出窗口的宽度;
	var iHeight; // 弹出窗口的高度;
	var iTop = (window.screen.availHeight - 30 - iHeight) / 2; // 获得窗口的垂直位置;
	var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; // 获得窗口的水平位置;
	var re = window
			.showModalDialog(
					url,
					name,
					'height='
							+ iHeight
							+ ',,innerHeight='
							+ iHeight
							+ ',width='
							+ iWidth
							+ ',innerWidth='
							+ iWidth
							+ ',top='
							+ iTop
							+ ',left='
							+ iLeft
							+ ',toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no');
	return re;
}
// 所有提交，查询等操作按钮走的JS校验
function checkStaffFuncButton(button_id, obj) {
	// 按钮的ID -- button_id
	// （1） 该处后面改造，从登录的session中获取该员工的域权限，按后与按钮ID核对是否可以操作

	return true;
}
// 将空值替换为-9
function replaceNullValToNumber9(val) {
	if (val == '' || val == undefined || val == 'null' || val == null) {
		val = '-9';
	}
	return val;
}
// 将-9替换为空
function replaceF9ValToNull(val) {
	if (parseInt(val) == -9) {
		val = '';
	}
	return val;
}
// 将-9替换为不详
function replaceF9ValToUnknown(val) {
	if (parseInt(val) == -9) {
		val = '不详';
	}
	return val;
}
function clrThisPage() {
	window.location.reload();
}

function replaceNullValToNumberPlus1(val) {
	if (val == '' || val == undefined || val == 'null' || val == null) {
		val = '-1';
	}
	return val;
}
function replaceNullValToOtherVal(val, toVal) {
	if (val == '' || val == undefined || val == 'null' || val == null) {
		val = toVal;
	}
	return val;
}
function clrPage() {
	window.location.reload();
}
// 返回处理结果insert update delete 等
function alertUpOrInOver(ttDom, plusMes) {
	var res = ttDom.selectSingleNode('./root/res').text;
	var msg = ttDom.selectSingleNode('./root/msg').text;
	if (plusMes == '' || plusMes == undefined || plusMes == null) {
		alert(msg);
	} else {
		alert(msg + ":" + plusMes);
	}

	window.location.reload();
	return;
}

// 统一提示
function alertMsgBoxByXmlData(resultXml) {
	var res = resultXml.selectSingleNode('./root/res').text;
	var msg = resultXml.selectSingleNode('./root/msg').text;
	if (res == '1') {
		alertMsg.error(msg);
	} else if (res == '2') {
		alertMsg.correct(msg);
	} else if (res == '3') {
		alertMsg.info(msg);
	} else {
		alertMsg.warn(msg);
	}
}

function alertToUserMsg(json) {
	var res = json.res;
	var msg = json.msg;
	if (res == '0') {
		$.pdialog.open("login_dialog.html", "dlglogin1", "登录");
	} else if (res == '1') {
		alertMsg.error(msg);
	} else if (res == '2') {
		alertMsg.correct(msg);
	} else if (res == '3') {
		alertMsg.info(msg);
	}
}

function alertToPageMsg(json) {
	var res = json.res;
	var msg = json.msg;
	if (res == '0') {
		$.pdialog.open("login_dialog.html", "dlglogin1", "登录");
	} else if (res == '1') {
		alertMsg.error(msg);
	} else if (res == '2') {
		alertMsg.correct(msg);
	} else if (res == '3') {
		alertMsg.info(msg);
	}
}

function alertToUserMsg(json) {
	var res = json.res;
	var msg = json.msg;
	if (res == '0') {
		$.pdialog.open("login_dialog.html", "dlglogin1", "登录");
	} else if (res == '1') {
		alertMsg.error(msg); 
	} else if (res == '2') {
		alertMsg.correct(msg); 
	} else if (res == '3') {
		alertMsg.warn(msg); 
	}
} 
function alertToUserMsg(json, deal) {
	var res = json.res;
	var msg = json.msg;
	if (res == '0') {
		$.pdialog.open("login_dialog.html", "dlglogin1", "登录");
	} else if (res == '1') {
		alertMsg.error(msg);
		deal(json);
	} else if (res == '2') {
		alertMsg.correct(msg);
		deal(json);
	} else if (res == '3') {
		alertMsg.warn(msg);
		deal(json);
	}
}
//文件上传
function fileUpload(action) {
	$.ajaxFileUpload({
		url : "attachment_upload_one" ,// 用于文件上传的服务器端请求地址
		secureuri : false,// 一般设置为false
		fileElementId : 'file',// 文件上传空间的id属性 <input type="file" id="file" // name="file" />
		dataType : 'json',// 返回值类型 一般设置为json
		success : function(data, status) // 服务器成功响应处理函数
		{
			alert(data.msg);  
		},
		error : function(data, status, e)// 服务器响应失败处理函数
		{
			alertMsg.error(e);
		}
	}) 
}

function ajaxFileUploadV1(foregin_id,func) { 
	/*
	 * $("#loading") .ajaxStart(function(){ $(this).show(); })//开始上传文件时显示一个图片
	 * .ajaxComplete(function(){ $(this).hide(); });//文件上传完成将图片隐藏起来
	 */
	$.ajaxFileUpload({
		url : 'attachment_upload_multi/'+foregin_id,// 用于文件上传的服务器端请求地址
		secureuri : false,// 一般设置为false
		fileElementId : 'file',// 文件上传空间的id属性 <input type="file" id="file" // name="file" />
		dataType : 'json',// 返回值类型 一般设置为json
		success : function(data, status) // 服务器成功响应处理函数
		{
			func();
					
		},
		error : function(data, status, e)// 服务器响应失败处理函数
		{ 
			alert(e);
		}
	}); 
}


// 文件上传
function ajaxFileUpload(foregin_id) {
	/*
	 * $("#loading") .ajaxStart(function(){ $(this).show(); })//开始上传文件时显示一个图片
	 * .ajaxComplete(function(){ $(this).hide(); });//文件上传完成将图片隐藏起来
	 */
	$.ajaxFileUpload({
		url : 'attachment_upload_multi/'+foregin_id,// 用于文件上传的服务器端请求地址
		secureuri : false,// 一般设置为false
		fileElementId : 'file',// 文件上传空间的id属性 <input type="file" id="file" // name="file" />
		dataType : 'html',// 返回值类型 一般设置为json
//		success : function(data, status) // 服务器成功响应处理函数
//		{
//					alert(data);  
//		},
//		error : function(data, status, e)// 服务器响应失败处理函数
//		{
//			alert(e);
//		}
	}); 

}
function dealUploadList(data) {
	if (data.res == '2') {
		alertMsg.correct("文件上传成功");
	} else {
		alertMsg.error(data.msg);
	}
}
// 请选择选项
function addPleaseCheck(control) {
	// 添加---请选择---
	var option = document.createElement("<option value='-9'></option>");
	option.appendChild(document.createTextNode("--请选择--"));
	control.appendChild(option);
}

function createParamJsonCommon(paramArray) {
	var obj = new Object();
	obj.param1 = paramArray[0] == undefined ? "-9" : paramArray[0];
	obj.param2 = paramArray[1] == undefined ? "-9" : paramArray[1];
	obj.param3 = paramArray[2] == undefined ? "-9" : paramArray[2];
	obj.param4 = paramArray[3] == undefined ? "-9" : paramArray[3];
	obj.param5 = paramArray[4] == undefined ? "-9" : paramArray[4];
	obj.param6 = paramArray[5] == undefined ? "-9" : paramArray[5];
	obj.param7 = paramArray[6] == undefined ? "-9" : paramArray[6];
	obj.param8 = paramArray[7] == undefined ? "-9" : paramArray[7];
	obj.param9 = paramArray[8] == undefined ? "-9" : paramArray[8];
	obj.param10 = paramArray[9] == undefined ? "-9" : paramArray[9];
	obj.param11 = paramArray[10] == undefined ? "-9" : paramArray[10];
	obj.param12 = paramArray[11] == undefined ? "-9" : paramArray[11];
	obj.param13 = paramArray[12] == undefined ? "-9" : paramArray[12];
	obj.param14 = paramArray[13] == undefined ? "-9" : paramArray[13];
	obj.param15 = paramArray[14] == undefined ? "-9" : paramArray[14];
	obj.param16 = paramArray[15] == undefined ? "-9" : paramArray[15];
	obj.param17 = paramArray[16] == undefined ? "-9" : paramArray[16];
	obj.param18 = paramArray[17] == undefined ? "-9" : paramArray[17];
	obj.param19 = paramArray[18] == undefined ? "-9" : paramArray[18];
	obj.param20 = paramArray[19] == undefined ? "-9" : paramArray[19];
	obj.param21 = paramArray[20] == undefined ? "-9" : paramArray[20];
	obj.param22 = paramArray[21] == undefined ? "-9" : paramArray[21];
	obj.param23 = paramArray[22] == undefined ? "-9" : paramArray[22];
	obj.param24 = paramArray[23] == undefined ? "-9" : paramArray[23];
	obj.param25 = paramArray[24] == undefined ? "-9" : paramArray[24];
	obj.param26 = paramArray[25] == undefined ? "-9" : paramArray[25];
	obj.param27 = paramArray[26] == undefined ? "-9" : paramArray[26];
	obj.param28 = paramArray[27] == undefined ? "-9" : paramArray[27];
	obj.param29 = paramArray[28] == undefined ? "-9" : paramArray[28];
	obj.param30 = paramArray[29] == undefined ? "-9" : paramArray[29];
	obj.param31 = paramArray[30] == undefined ? "-9" : paramArray[30];
	obj.param32 = paramArray[31] == undefined ? "-9" : paramArray[31];
	obj.param33 = paramArray[32] == undefined ? "-9" : paramArray[32];
	obj.param34 = paramArray[33] == undefined ? "-9" : paramArray[33];
	obj.param35 = paramArray[34] == undefined ? "-9" : paramArray[34];
	obj.param36 = paramArray[35] == undefined ? "-9" : paramArray[35];
	obj.param37 = paramArray[36] == undefined ? "-9" : paramArray[36];
	obj.param38 = paramArray[37] == undefined ? "-9" : paramArray[37];
	obj.param39 = paramArray[38] == undefined ? "-9" : paramArray[38];
	obj.param40 = paramArray[39] == undefined ? "-9" : paramArray[39];
	obj.param41 = paramArray[40] == undefined ? "-9" : paramArray[40];
	obj.param42 = paramArray[41] == undefined ? "-9" : paramArray[41];
	obj.param43 = paramArray[42] == undefined ? "-9" : paramArray[42];
	obj.param44 = paramArray[43] == undefined ? "-9" : paramArray[43];
	obj.param45 = paramArray[44] == undefined ? "-9" : paramArray[44];
	obj.param46 = paramArray[45] == undefined ? "-9" : paramArray[45];
	obj.param47 = paramArray[46] == undefined ? "-9" : paramArray[46];
	obj.param48 = paramArray[47] == undefined ? "-9" : paramArray[47];
	obj.param49 = paramArray[48] == undefined ? "-9" : paramArray[48];
	obj.param50 = paramArray[49] == undefined ? "-9" : paramArray[49];
	obj.param51 = paramArray[50] == undefined ? "-9" : paramArray[50];
	obj.param52 = paramArray[51] == undefined ? "-9" : paramArray[51];
	obj.param53 = paramArray[52] == undefined ? "-9" : paramArray[52];
	obj.param54 = paramArray[53] == undefined ? "-9" : paramArray[53];
	obj.param55 = paramArray[54] == undefined ? "-9" : paramArray[54];
	obj.param56 = paramArray[55] == undefined ? "-9" : paramArray[55];
	obj.param57 = paramArray[56] == undefined ? "-9" : paramArray[56];
	obj.param58 = paramArray[57] == undefined ? "-9" : paramArray[57];
	obj.param59 = paramArray[58] == undefined ? "-9" : paramArray[58];
	obj.param60 = paramArray[59] == undefined ? "-9" : paramArray[59];
	var lst = [];
	var json = {};
	lst.push(obj);
	json['content'] = lst;
	return json;
}
// ****************************************************************
// Description: sInputString 为输入字符串，iType为类型，分别为
// 0 - 去除前后空格; 1 - 去左边空格; 2 - 去右边空格
// ****************************************************************
function cTrim(sInputString, iType) {
	var sTmpStr = ' '
	var i = -1
	if (iType == 0 || iType == 1) {
		while (sTmpStr == ' ') {
			++i
			sTmpStr = sInputString.substr(i, 1)
		}
		sInputString = sInputString.substring(i)
	}
	if (iType == 0 || iType == 2) {
		sTmpStr = ' '
		i = sInputString.length
		while (sTmpStr == ' ') {
			--i
			sTmpStr = sInputString.substr(i, 1)
		}
		sInputString = sInputString.substring(0, i + 1)
	}
	return sInputString
}

function getTotalPageCnt(nodeslength, len) {
	if (len >= nodeslength) {
		return 1;
	}
	var ye = nodeslength % len;
	if (ye == 0) {
		return nodeslength / len;
	} else {
		return Math.floor(nodeslength / len) + 1;
	}
}

loadXML = function(xmlFile) {
	var xmlDoc = null;
	// 判断浏览器的类型
	// 支持IE浏览器
	if (!window.DOMParser && window.ActiveXObject) {
		var xmlDomVersions = [ 'MSXML.2.DOMDocument.6.0',
				'MSXML.2.DOMDocument.3.0', 'Microsoft.XMLDOM' ];
		for (var i = 0; i < xmlDomVersions.length; i++) {
			try {
				xmlDoc = new ActiveXObject(xmlDomVersions[i]);
				break;
			} catch (e) {
			}
		}
	}
	// 支持Mozilla浏览器
	else if (document.implementation && document.implementation.createDocument) {
		try {
			/*
			 * document.implementation.createDocument('','',null); 方法的三个参数说明
			 * 第一个参数是包含文档所使用的命名空间URI的字符串； 第二个参数是包含文档根元素名称的字符串；
			 * 第三个参数是要创建的文档类型（也称为doctype）
			 */
			xmlDoc = document.implementation.createDocument('', '', null);
		} catch (e) {
		}
	} else {
		return null;
	}

	if (xmlDoc != null) {
		xmlDoc.async = false;
		xmlDoc.load(xmlFile);
	}
	return xmlDoc;
}

function isNullCheck(obj) {

	if (obj == '' || obj == null || obj == 'undefined' || obj == '-1'
			|| obj == '-9') {
		return true;
	}
	return false;
}

// // Ajax 文件下载
// $.download = function(url, data, method){ // 获得url和data
// if( url && data ){
// // data 是 string 或者 array/object
// data = typeof data == 'string' ? data : jQuery.param(data); // 把参数组装成 form的
// input
// var inputs = '';
// jQuery.each(data.split('&'), function(){
// var pair = this.split('=');
// inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
// }); // request发送请求
// jQuery('<form action="'+ url +'" method="'+ (method||'post')
// +'">'+inputs+'</form>')
// .appendTo('body').submit().remove();
// };
// } ;

// 删除表头以外的行
function removeTableData(obj, cnt) {
	obj.find("tr").remove();
}
// 创建一个30长度的数组Json,并做ajax请求
function createJsonAndAjax(action, arr, ff, dataFormat) {
	var eJson = createParamJsonCommon(arr);
	eJson = JSON.stringify(eJson);
	var args = {
		"context" : eJson
	};
	baidu.post(action, args, function(data) {
		ff(data)
	}, dataFormat);
}
function checkFormValidate(x) {
	// 把这些Obj-Array拿出来，做校验
	for (var i = 0; i < x.length; i++) {
		var obj = $("input[name='" + x[i].name + "']");
		var return_array = checkThisCellValidate(obj);
		obj.css("background-color", "white");
		if (return_array[0] == "1") {
			obj.focus();
			obj.css("background-color", "red");
			alert(return_array[1]);
			return false;
		}
	}
	return true;
}
// 创建一个30长度的数组Json,并做post请求，百度请求也可以适应全类型浏览器
function createJsonAndPost2Java(action, form, dealAfter, dataFormat, async_val) {
	var x = form.serializeArray();
	var check_result = checkForm(x);
	if (check_result == "1") {
		return;
	}  
	var args = x ;  
	$.ajax({
		url : action,
		type : "POST",
		async : async_val, // false:同步请求,同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
		data : args,
		dataType : dataFormat,
		success : function(data, textStatus, jqXHR) {
				dealAfter(data);
		},
		error : function(jqXHR, textStatus, errorMsg) {
			alertMsg.error(errorMsg);
		}
	});
	

	var ajaxbg = $("#background,#progressBar");
	ajaxbg.hide();
}
function createJsonAndGet2Java(action, form, dealAfter, dataFormat, async_val) {
	var x = form.serializeArray();
	var check_result = checkForm(x);
	if (check_result == "1") {
		return;
	}  
	var args = x ;  
	$.ajax({
		url : action,
		type : "GET",
		async : async_val, // false:同步请求,同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
		data : args,
		dataType : dataFormat,
		success : function(data, textStatus, jqXHR) {
			dealAfter(data);
		},
		error : function(jqXHR, textStatus, errorMsg) {
			alertMsg.error(errorMsg);
		}
	});
}
function createJsonAndAjaxRptAjax(action, arr, ff, dataFormat) {
	var eJson = createParamJsonRpt(arr);
	eJson = JSON.stringify(eJson);
	var args = {
		"context" : eJson
	};
	baidu.ajax({
		url : action,
		type : "POST",
		data : args,
		async : false,
		success : function(data) {
			ff(data)
		}
	});
}

function renderBtnsToDiv(obj, action, array) {
	var eJson = createParamJsonCommon(array);
	eJson = JSON.stringify(eJson);
	var args = {
		"context" : eJson
	};
	$.ajax({
		url : action,
		type : "POST",
		data : args,
		dataType : 'html',
		async : false,
		success : function(data) {
			obj.html(data);
		}
	});

}
function initPageSelectList(obj, pageNum, obj2, tatol, thisPageNo) {
	obj.find("option").remove();
	for (var i = 0; i < pageNum; i++) {
		var j = i + 1;
		if (thisPageNo == i) {
			obj.append("<option selected value='" + i + "'>" + j + "</option>");
		} else {
			obj.append("<option value='" + i + "'>" + j + "</option>");
		}
	}
	obj2.text(tatol);
	if (thisPageNo > 0) {
		obj.val(thisPageNo);
	}
}


function renderTypeDataListV2(control, action, isSelect, array,selected_item) {
	var args = {
		"type" : array[0]
	};
	$.post(action, args, function(data, status) {
		// 请选择
		if (status == 'success') {
			var json = data.list; 
			if (isSelect == '1') {
				control.append("<option value='-9'>请选择</option>");
			}
			for (var i = 0; i < json.length; i++) {
				if(selected_item == json[i].item_id){
					var $option = $("<option>").attr("value", json[i].item_id);
					$option.attr("selected",true) ;
				}else{
					var $option = $("<option>").attr("value", json[i].item_id);
				}
				$option.text(json[i].item_name);
				control.append($option);
			}
			control.addClass("combox"); 
			control.combox();  
		} // end success
	}, 'JSON');
}


function renderTypeDataList(control, action, isSelect, array) {
	var args = {
		"type" : array[0]
	};
	$.post(action, args, function(data, status) {
		// 请选择
		if (status == 'success') {
			var json = data.list; 
			if (isSelect == '1') {
				control.append("<option value='-9'>请选择</option>");
			}
			for (var i = 0; i < json.length; i++) {
				var $option = $("<option>").attr("value", json[i].item_id);
				$option.text(json[i].item_name);
				control.append($option);
			}
			//control.addClass("combox"); 
			//control.combox();  
		} // end success
	}, 'JSON');
}

function checkThisCellValidate(obj) {
	var css = obj.attr("class");
	var str = obj.val();
	var array = new Array();
	array[0] = "2";
	array[1] = "-9";
	if (css == undefined) {
		return array;
	}
	if (css.indexOf("required") != '-1') {
		if (str == "" || str == null || str == undefined || str == "-1"
				|| str == "-9") {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，为必输项";
			return array;
		}
	}
	if (css.indexOf("int") != '-1') {
		var reg = new RegExp("^[0-9]*$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入正整数";
			return array;
		}
	}
	if (css.indexOf("double2") != '-1') {
		var reg = new RegExp("^[0-9]+(.[0-9]{2})?$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入2位小数点正数";
			return array;
		}
	}
	if (css.indexOf("double3") != '-1') {
		var reg = new RegExp("^[0-9]+(.[0-9]{3})?$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入3位小数点正数";
			return array;
		}
	}
	if (css.indexOf("number") != '-1') {
		if (isNaN(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入数字";
			return array;
		}
	}
	if (css.indexOf("numberAndChar") != '-1') {
		var reg = new RegExp("^[A-Za-z0-9]+$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入数字与字母混合";
			return array;
		}
	}
	if (css.indexOf("charSmall") != '-1') {
		var reg = new RegExp("^[a-z]+$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入小写字母";
			return array;
		}
	}
	if (css.indexOf("charBig") != '-1') {
		var reg = new RegExp("^[A-Z]+$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入大写字母";
			return array;
		}
	}
	if (css.indexOf("chinese") != '-1') {
		var reg = new RegExp("^[\u4e00-\u9fa5],{0,}$");
		for (var k = 0; k < str.length; k++) {
			if (!reg.test(str.charAt(k))) {
				array[0] = "1";
				array[1] = obj.attr("ename") + "，应输入汉字";
				return array;
			}
		}
	}
	if (css.indexOf("email") != '-1') {
		var reg = new RegExp("^\w+[-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入Email";
			return array;
		}
	}
	if (css.indexOf("http") != '-1') {
		var reg = new RegExp(
				"^http://([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$ ；^[a-zA-z]+://(w+(-w+)*)(.(w+(-w+)*))*(?S*)?$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入网页地址";
			return array;
		}
	}
	if (css.indexOf("phone") != '-1') {
		var reg = new RegExp("^(\(\d{3,4}\)|\d{3,4}-)?\d{7,8}$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入电话号码";
			return array;
		}
	}
	if (css.indexOf("card") != '-1') {
		var reg = new RegExp("^\d{15}|\d{}18$");
		if (!reg.test(str)) {
			array[0] = "1";
			array[1] = obj.attr("ename") + "，应输入身份证号码";
			return array;
		}
	}

	return array;
}

function createJsonAndAjaxNew(action, array, dealAfter, dataFormat, async_val) {
	var eJson = createParamJsonCommon(array);
	eJson = JSON.stringify(eJson);
	var args = {
		"context" : eJson
	};
	var ajaxbg = $("#background,#progressBar");
	ajaxbg.hide();
	ajaxbg.show();
	$.ajax({
		url : action,
		type : "POST",
		data : args,
		dataType : dataFormat,
		async : async_val,
		success : function(data, textStatus, xhr) {
			ajaxbg.hide();
			dealAfter(data);
		},
		error : function(data) {
			ajaxbg.hide();
			alertMsg.error("Ajax调用异常,或请重试!");
		}
	});
}


function ajaxRequestToJavaByMapArgs(action, args, dealAfter, dataFormat, async_val) {
	var ajaxbg = $("#background,#progressBar");
	ajaxbg.hide();
	ajaxbg.show();
	$.ajax({
		url : action,
		type : "POST",
		data : args,
		dataType : dataFormat,
		async : async_val,
		success : function(data, textStatus, xhr) {
			ajaxbg.hide();
			dealAfter(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			ajaxbg.hide();
//			alert(jqXHR.responseText);
//            alert(jqXHR.status);
//            alert(jqXHR.readyState);
//            alert(jqXHR.statusText); 
//            alert(textStatus);
//            alert(errorThrown);
			alertMsg.error("Ajax调用异常,或请重试!");
		}
	});
}

function testAjax(action, form, dealAfter, dataFormat) {
	var x = form.serializeArray();
	var check_result = checkForm(x);
	if (check_result == "1") {
		return;
	}
	var eJson = JSON.stringify(x);
	var args = {
		"context" : eJson
	};
	$.ajax({
		url : action,
		type : "POST",
		data : args,
		dataType : dataFormat,
		async : false,
		success : function(data) {
			dealAfter(data)
		}
	});
}

function checkForm(x) {
	var val = "2";
	for (var i = 0; i < x.length; i++) {
		var obj = $("input[name='" + x[i].name + "']");
		var return_array = checkThisCellValidate(obj);
		obj.css("background-color", "white");
		if (return_array[0] == "1") {
			obj.focus();
			obj.css("background-color", "yellow");
			alertMsg.info(return_array[1]);
			val = "1";
			break;
		}
		return_array = null;
	}
	return val;
}

function isStartEndDate(startDate, endDate) {
	if (startDate.length > 0 && endDate.length > 0) {
		var startDateTemp = startDate.split(" ");
		var endDateTemp = endDate.split(" ");
		var arrStartDate = startDateTemp[0].split("-");
		var arrEndDate = endDateTemp[0].split("-");
		var arrStartTime = startDateTemp[1].split(":");
		var arrEndTime = endDateTemp[1].split(":");
		var allStartDate = new Date(arrStartDate[0], arrStartDate[1],
				arrStartDate[2], arrStartTime[0], arrStartTime[1],
				arrStartTime[2]);
		var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2],
				arrEndTime[0], arrEndTime[1], arrEndTime[2]);
		if (allStartDate.getTime() > allEndDate.getTime()) {
			return false;
		}
	}
	return true;
} 
//根据操作前缀编码抽取该用户拥有的操作权限按钮
function renderAuthorityOperateBtnAll(obj, action, code, ansy, barCss) {
	ajaxTodo(action+"/"+code,function(data){
		var htmlString = ""; 
		if (data.res != '2') {
		} else {
			if (barCss == "panelBar") {
				var list = data.list;
				htmlString = "<ul class=toolBar>";
				for (var i = 0; i < list.length; i++) {
					if ("" == list[i].operate_address
							|| list[i].operate_address == undefined
							|| "-9" == list[i].operate_address) {
						htmlString = htmlString + "<li><a class="
								+ list[i].operate_css
								+ "   target="
								+ list[i].operate_target
								+ "><span id="
								+ list[i].operate_code + ">"
								+ list[i].operate_name
								+ "</span></a></li>";
					} else {
						htmlString = htmlString + "<li><a class="
								+ list[i].operate_css
								+ " href='"
								+  list[i].operate_address
								+ "'  rel='"+list[i].operate_code+list[i].menuId+"'  target="
								+ list[i].operate_target
								+ "><span id="
								+ list[i].operate_code + ">"
								+ list[i].operate_name
								+ "</span></a></li>";
					}
				}
				htmlString = htmlString + "</ul>";
				obj.html(htmlString);
			} else if (barCss == "formBar") {
				var list = data.list;
				htmlString = "<ul>";
				for (var i = 0; i < list.length; i++) {
					htmlString = htmlString
							+ "<li><div class=buttonActive><div class=buttonContent><button id="
							+ list[i].operate_code
							+ " type='"+list[i].button_type+"'>"
							+ list[i].operate_name
							+ "</button></div></div></li>";
				}
				htmlString = htmlString + "</ul>";
				obj.html(htmlString);
			}
		}

		obj.initUI();
	}); 
}

function renderTableList(obj, action, array, render_func) {
	createJsonAndAjaxNew(
			action,
			array,
			function(data) {
				if (data.res != '2') {
					obj.empty();
					alertToUserMsg(data);
					$("#totalCount", navTab.getCurrentPanel()).html('0');
					$("#paginationBar", navTab.getCurrentPanel()).empty();
				} else {
					$("#totalCount", navTab.getCurrentPanel()).html(
							data.totalCount);
					var html = "<select id=pageNumber onchange=getListDataByPageNum(this.value) >";
					for (var i = 1; i <= data.pageNumShown; i++) {
						html = html + "<option value=" + i + ">" + i
								+ "</option>";
					}
					html = html + "</select>";
					$("#paginationBar", navTab.getCurrentPanel()).empty();
					$("#paginationBar", navTab.getCurrentPanel()).append(html);
					render_func(obj, data.list);
				}
			}, 'JSON', false);
}
 

function getCleanString(s){
	if("-9"==s)
		return "";
	return s ;
}
// 获取UUID
//8 character ID (base=2)
//uuid(8, 2) // "01001010"
// 8 character ID (base=10)
//uuid(8, 10) // "47473046"
// 8 character ID (base=16)
//uuid(8, 16) // "098F4D35"
function getUUID(len, radix) {
	  var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
	  var uuid = [], i;
	  radix = radix || chars.length;
	 
	  if (len) {
	   // Compact form
	   for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
	  } else {
	   // rfc4122, version 4 form
	   var r;
	 
	   // rfc4122 requires these characters
	   uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
	   uuid[14] = '4';
	 
	   // Fill in random data. At i==19 set the high bits of clock sequence as
	   // per rfc4122, sec. 4.1.5
	   for (i = 0; i < 36; i++) {
	    if (!uuid[i]) {
	     r = 0 | Math.random()*16;
	     uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
	    }
	   }
	  }
	 
	  return uuid.join('');
	}


function dwzConfirmFormToBack(msg,success,cancel){
	alertMsg.confirm(msg, {
		 okCall: function(){
			 success();
		 },
		 cancelCall : function() {cancel();}
		});	
} 


//系统地址本
//objtype 11111 五位数  用户，用户组，岗位，角色，组织
function openSysAddressBook(uuid,objtype,multi,callBack) {
	var op2 = {width:600,height:543,mask:eval("true"),maxable:eval("false"),mixable:eval("false"),minable:eval("false"),resizable:eval("false"),drawable:eval("true") };
	$.pdialog.open($("#basePathOfSys").val()+"/system/openSysAddressBook/0/"+ objtype +"/"+multi+"/"+ uuid,
			"sysAddressBookDialog", "地址本", op2);
	$.pdialog.resizeDialog({
		style : {
			height : 543,
			width : 750 
		}
	}, $.pdialog.getCurrent(), ""); 
}

/***
* 获取文件上传时候
* 多选，服务器接收方法
****/
function getUploaderOption(foreign_id,multi,server_function,file_type){
	return "{swf:ctx+'common/uploadify/scripts/uploadify.swf',uploader:'"+ ctx + server_function + foreign_id + "', "+
			" formData:{}, "+
			" buttonText:'请选择文件', "+
			" fileSizeLimit:'100MB', "+
			" fileTypeDesc:'"+file_type+"', "+
			" fileTypeExts:'"+file_type+"', "+
			" auto:true, "+
			" multi:"+multi+", "+
			" height:30, "+
			" width:90, "+
			" method : 'post',  "+
			" removeCompleted : false  }";
}