Ext.define("app.view.main.WestNav", {
	extend : "Ext.panel.Panel",
	alias : "widget.mainwestnav",
	layout : {
		type : "accordion",
		titleCollapse : false,
		animate : true
	},
	defaults : {
		xtype : "treepanel"
	},
	items : [ {
		title : "aaabb",
		rootVisible : false,
		listeners : {
			itemclick : 'onMenuTreeItemClick'
		}
	}, {
		title : "cccddd",
		rootVisible : false,
		listeners : {
			itemclick : 'onMenuTreeItemClick'
		}
	} ]
});