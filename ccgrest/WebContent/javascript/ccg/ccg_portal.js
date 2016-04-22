// global variable
var ccg={};
ccg.ui={};
ccg.data={};

Ext.define("com.ccg.portalheader",{
	extend:"Ext.Container",
	
	xtype:"ccg-portal-header",
	title:document.title,
	cls:'app-header',
	height:48,
	layout:{
		type:'hbox',
		align:'middle'
	},
	items:[{
		xtype:'component',
		cls:'ccg-logo'},
		{
			xtype:'component',
			cls:'app-header-title',
			html:document.title,
			flex:1
		}
	]	
});

Ext.onReady(function(){
	// some initialization functions
	Ext.create('Ext.container.Viewport',{
		layout:'border',
		items:[{
			region:'north',
			xtype:"ccg-portal-header",
			id:'ccg-app-header'
		},
		{ 
			region:"west",
			collapse:true,
			title:'Menu',
			width:200,
			split:true,
			layout:{
				type:'accordion',
				animate:true,
				collapseFirst:true
			},
			items:[{
				title:'Content Ingestion',
				scrollable:true,
			},
			{
				title:'Content Management',
				scrollable:true,
			},
			{
				title:'Content Builder',
				scrollable:true,
			},
			{
				title:'System Admin',
				scrollable:true,
			}
			]
		},
		{
			title:'main',
			region:'center'
			
		}
		
		]
	});
});

