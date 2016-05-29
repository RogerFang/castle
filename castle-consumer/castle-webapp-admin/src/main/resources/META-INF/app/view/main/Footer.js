Ext.define("app.view.main.Footer", {
	extend : "Ext.toolbar.Toolbar",
	alias : "widget.mainfooter",
	items : [ "->", {
		html : "&copy;广州当凌信息科技有限公司",
		xtype : "label"
	}, "->" ]
});