// ccg_admin.js
// admin page for ccg portal

Ext.onReady(function(){
	// some initialization functions
	Ext.create('Ext.container.Viewport',{
		layout:'border',
		items:[{
			region:'north',			
			xtype:"ccg-admin-toolbar",
			cls:'ccg-app-header'				
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
		       		   id:'adminmain',
			       		height:500,
			       		style: {
			             color: '#66b3ff'			             
			       		},
			       		flex:1,
			       		autoScroll: true
			       	},
			       	{
			       		width:'10px'
			       	}
			       	]
				
		}
		]
	});
});


