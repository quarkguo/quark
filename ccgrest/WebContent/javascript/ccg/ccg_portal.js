// global variable



Ext.onReady(function(){
	// some initialization functions
	Ext.create('Ext.container.Viewport',{
		layout:'border',
		items:[{
			region:'north',			
			xtype:"ccg-toolbar"
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
			       		title:'Content',
			       		flex:1
			       			
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

