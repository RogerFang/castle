Ext.define("app.view.main.Main", {
	extend : "Ext.container.Viewport",
	xtype : "app-main",
	layout : "border",
	requires : [ "app.view.main.MainController", "app.view.main.MainModel", "app.view.main.Header", "app.view.main.Footer", "app.view.main.WestNav" ],
	controller : "main",
	viewModel : "main",
	items : [ {
		region : "north",
		xtype : "mainheader"
	}, {
		region : "south",
		xtype : "mainfooter"
	}, {
		region : "west",
		xtype : "mainwestnav",
		title : "导航栏",
		collapsible : true,
		split : true,
		width : 200
	}, {
		region : "center",
		xtype : "panel",
		title : "中部"
	} ]
});