function dataDictList(event, treeId, treeNode){ 
	var gotoHref = $("#gotoHref",navTab.getCurrentPanel());
	gotoHref.attr("href","dictlist/" + treeNode.id);
	gotoHref.trigger("click");
}