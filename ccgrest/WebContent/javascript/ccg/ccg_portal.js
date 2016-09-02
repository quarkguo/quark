// global variable



Ext.onReady(function(){
	// some initialization functions
	ccg.initUploadFilePanel();
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
		     height:'100%',
		     width:"60%",
			items:[
			   	ccg.ui.doclist,
			   	{			
					region:'north',
					   split:true,				   
					   xtype:'tabpanel',				   
					   id:'categorytabpanel',
					     collapsible:true,
					     activeTab:0,
					items:[
					       ccg.ui.doccategory
					       //ccg.ui.relateddoclist 
					]
					
						}
			]
		},
		{
			region:'center',
			layout:'border',
			frame: true,
						
			defaults: {		        
		        bodyPadding: 10
		    },
			items:[
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
			       		    	title:'Original Content',
			       		    	bodyBorder: true,
			       		    	border:true,
			       		    	html:'<iframe name="pdfcontent" id="pdfcontent" width=100% height=100% border=0 />'
			       		    },
			       		   {
			       		       title:'Text Content',
			       			   xtype:'panel',
			       			   id:'contentpanel',
			       			   bodyBorder: true,
			       			   border:true,
			       			   frame:true,
			       			   html:' test 1'
			       		   }			       		   
			       		]
			       	}
			       	],
			       	tools:[
							 {
				    			type: 'search', // this doesn't appear to work, probably I need to use a valid class
				    			tooltip: 'Search Related Content',
				    			handler: function() 
				    			{
				    				ccg.ui.contentsearchPanel.show();
				    			}
							},
							{
								type: 'gear', // this doesn't appear to work, probably I need to use a valid class
								tooltip: 'Document Meta Data',
								handler: function() {
									ccg.ui.metapanel.show();
								}
							}
							]
				
		}		
		]
	});
});

