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
			       		//title:'Article Content',
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
			       		    }
			       		    /*,
			       		   {
			       		       title:'Text Content',
			       			   xtype:'panel',
			       			   id:'contentpanel',
			       			   bodyBorder: true,
			       			   border:true,
			       			   frame:true,
			       			   html:' test 1'
			       		   }	*/		       		   
			       		]
			       	}
			       	],
			       	tools:[
			       	       {
							xtype:'panel',
							align:'left',							
							id:'navipanel',
							layout:'hbox',
							hidden:true,
							items:[							    
							]
							
			       	       },
			       	       /*
			       	       	{xtype:'tbfill'},
			       	        {xtype:'tbfill'},
			       	        {xtype:'tbfill'},*/			       	        
							 {
				    			type: 'search', // this doesn't appear to work, probably I need to use a valid class
				    			tooltip: 'Search Related Content',
				    			handler: function() 
				    			{
				    				ccg.ui.contentsearchPanel.show();
				    			}
							},
							   
							   { xtype: 'tbspacer' },
							{
								type: 'gear', // this doesn't appear to work, probably I need to use a valid class
								tooltip: 'Document Meta Data',
								region:'center',
								handler: function() {
									ccg.ui.metapanel.show();
								}
							},
							{type:'prev',
								id:'previcon',
								page:-1,
								hidden:true,
								handler: function(){
									var p=Ext.getCmp("previcon").page;
									var aID=Ext.getCmp("previcon").articleID;
									var key=Ext.getCmp("previcon").key;
									console.log(p);
									if(p>0)
									{
									  p=p-1;
									  url='rest/article/'+aID+'/'+p+'/download'
									  var pdfPanel=document.getElementById('pdfcontent');
									  pdfPanel.src=url;
									  Ext.getCmp("previcon").page=p;									  
									}
										
									
								 }
								}
							,
							{
								type:'next',
								id:'nexticon',							
								hidden:true,
								handler: function(){
									var p=Ext.getCmp("previcon").page;
									var aID=Ext.getCmp("previcon").articleID;
									var key=Ext.getCmp("previcon").key;
									console.log(p);
									if(p>0)
									{
									  p=p+1;
									  url='rest/article/'+aID+'/'+p+'/download'
									  var pdfPanel=document.getElementById('pdfcontent');
									  pdfPanel.src=url;
									  Ext.getCmp("previcon").page=p;									  
									}
										
									
								 }
							}
							
							]
				
		}		
		]
	});
});

