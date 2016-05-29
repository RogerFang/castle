Ext.define("app.view.main.Header", {
	extend : "Ext.toolbar.Toolbar",
	alias : "widget.mainheader",
	defaults : {
		xtype : "button"
	},
	items : [ {
		xtype : "label",
		text : "后台管理系统",
		style : "font-size: 14;font-weight: 800"
	}, "->", {
		text : "用户",
		iconCls : "fa fa-user",
		menu : [ {
			text : "我的资料",
			iconCls : "fa fa-info-circle"
		}, "-", {
			text : "退出",
			iconCls : "fa fa-sign-out",
			handler : "onSignOut"
		} ]
	} ]
});