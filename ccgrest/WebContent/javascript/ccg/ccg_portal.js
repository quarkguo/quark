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
			   	ccg.ui.doclist
			]
		},
		{
			title:'Content',
			region:'center',
			items:[
			       	
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

