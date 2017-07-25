/**
 * 创建新的角色
 */
$(function(){
	 
});

function changeParentRole(obj){
	var parent_role_id = obj.value ;
	ajaxTodo("system/getMenuTreeOfRole/"+parent_role_id,function(data){
		alert(data.res);
	});
}