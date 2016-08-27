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
			layout:'border',
			 frame: true,
			defaults: {		        
		        bodyPadding: 10
		    },
			items:[{			
				region:'north',
				   split:true,
				     collapsible:true,
				items:[
				       ccg.ui.doccategory			       		       			        
				]
					
			},
			       	{			
			       		xtype:'panel',
			       		region:'north',
			       		title:'Content Panel',
			       		id:'contentpanel',			       	
			       		style: {
			             color: '#66b3ff'			             
			       		},
			       	//	flex:1,
			       		height:"98%",
			       		autoScroll: true
			       	}
			       	]
				
		}		
		]
	});
});

