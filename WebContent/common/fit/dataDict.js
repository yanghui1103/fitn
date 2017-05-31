function dataDictList(event, treeId, treeNode){
	
	var gotoHref = $("#gotoHref",navTab.getCurrentPanel());
	gotoHref.attr("href","companyList/-9");
	gotoHref.trigger("click");
}