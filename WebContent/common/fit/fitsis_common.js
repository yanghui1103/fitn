 
function takeCustomValueByOther(control,action,isSelect,array){ 
	// 0：放置的是ITYPE   1：放置的是STAFFID
	var eJson = createParamJsonCommon(array);
	eJson = JSON.stringify(eJson); 
	var args = {
		"context" : eJson
	};
	baidu.post(action, args, function(data) {  
		if(data.res=="1"){return ;}
		var json = data.list ;
		// 请选择
		if(isSelect == '1'){
			control.append("<option value='-9'>---请选择---</option>");
		}    
		for(var i=0;i< json.length ;i++){
			control.append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");
		}
	},  'json');
 }

/***
 * 计算人员类型
 */
function getPersonTypeName(age,gender){
	var array = new Array();
	if(gender =='0'){
		if(age >=40 && age <=45){
			array[0] = '101';array[1]="距退休年龄超5年人员";
			return array ;
		}else if(age >45 && age <=50){
			array[0] = '102';array[1]="距退休年龄不足5年人员";
			return array ;
		}
	}else  {
			if(age >=50 && age <=55){
				array[0] = '101';array[1]="距退休年龄超5年人员";
				return array ;
			}else if(age >55 && age <=60){
				array[0] = '102';array[1]="距退休年龄不足5年人员";
				return array ;
			}
		}
	   array[0]="-9";array[1]="年龄不符合，高校毕业一年未就业毕业生除外";
	   return array ;
}
 
// 获取可以享受的最长月数(无不断情况)
function getMaxMonthsByCard(card_id,gender,zhousui,p_type){
	var ling_yue = card_id.substr(10,2) ;
	ling_yue = parseInt(ling_yue);
	if(gender =="0" &&p_type =="101" ){
		return "36" ;
	}else if(gender =="0" &&p_type =="102" ){
		return (50 - zhousui +1) *12 +ling_yue ;
	}else if(gender =="1" &&p_type =="101" ){
		return "36" ;
	}else if(gender =="0" &&p_type =="102" ){
		return (60 - zhousui +1) *12+ling_yue ;
	}else {
		return "0";
	}
}