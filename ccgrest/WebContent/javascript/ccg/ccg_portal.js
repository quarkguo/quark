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
			   	ccg.ui.doclist
			]
		},
		{
			region:'center',
			layout:'border',
			frame: true,
						
			defaults: {		        
		        bodyPadding: 10
		    },
			items:[{			
				region:'north',
				   split:true,				   
				   xtype:'tabpanel',
				     collapsible:true,
				     activeTab:0,
				items:[
				       ccg.ui.doccategory,
				       ccg.ui.relateddoclist 
				],
				tools:[
						 {
			    			type: 'search', // this doesn't appear to work, probably I need to use a valid class
			    			tooltip: 'Search Related Content',
			    			handler: function() 
			    			{
			    				ccg.ui.contentsearchPanel.show();
			    			}
						}
						]
					},
			       	{			
			       		xtype:'tabpanel',
			       		region:'north',
			       		title:'Article Content',
			       		id:'contenttabpanel',
			       		activeTab:0,
			       		style: {
			             color: '#66b3ff'			             
			       		},
			       	//	flex:1,
			       		height:"98%",
			       		autoScroll: true,
			       		items:[
			       		    {
			       		    	title:'PDF content',
			       		    	bodyBorder: true,
			       		    	border:true,
			       		    	html:'<iframe name="pdfcontent" id="pdfcontent" width=99% height=95% border=0 />'
			       		    },
			       		   {
			       		       title:'TEXT content',
			       			   xtype:'panel',
			       			   id:'contentpanel',
			       			   bodyBorder: true,
			       			   border:true,
			       			   frame:true,
			       			   html:' test 1'
			       		   }			       		   
			       		]
			       	}
			       	]
				
		}		
		]
	});
});

