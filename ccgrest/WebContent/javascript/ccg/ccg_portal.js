// global variable



Ext.onReady(function(){
	// some initialization functions
	Ext.create('Ext.container.Viewport',{
		layout:'border',
		items:[{
			region:'north',			
			xtype:"ccg-toolbar",
			cls:'ccg-app-header'				
		},
		{ 
			region:"west",
		     split:true,
		     collapsible:true,
			items:[
			   	ccg.ui.doclist,
			    ccg.ui.relateddoclist 
			]
		},
		{
			title:'Main',
			region:'center',
			layout:'hbox',
			 frame: true,
			defaults: {		        
		        bodyPadding: 10
		    },
			items:[
			       	{			
			       		xtype:'panel',
			       		title:'Content Panel',
			       		id:'contentpanel',
			       		height:500,
			       		style: {
			             color: '#66b3ff'			             
			       		},
			       		flex:1,
			       		autoScroll: true
			       	},
			       	ccg.ui.metapanel,
			       	{
			       		width:'20px'
			       	}
			       	]
				
		},
		{
			title:'category',
			region:'east',
			   split:true,
			     collapsible:true,
			items:[
			       ccg.ui.doccategory			       		       			        
			]
				
		}
		]
	});
});

