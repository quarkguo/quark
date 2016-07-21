// ccg_admin.js
// admin page for ccg portal

Ext.onReady(function(){
	// some initialization functions
	Ext.create('Ext.container.Viewport',{
		layout:'border',
		xtype:'ccgadminwin',
		items:[{
			region:'north',			
			xtype:"ccg-admin-toolbar",
			cls:'ccg-app-header'				
		},
		{
			region:'center',
			layout:'hbox',
			 frame: true,
			defaults: {		        
		        bodyPadding: 2
		    },
			items:[
			       {
			    	   xtype:'panel',
			    	 //  title:'left',
			    	   id:'adminleft',
			    	   width: 300,			    	 
			    	   items:[
			    	          ccg.ui.grouplistpanel
			    	   ]
			       },   	
			       {
			       		width:'2px'
			       	},
			       	{			
			       		xtype:'panel',
		       		   id:'adminmain',
			       		height:500,		
			       		layout:'hbox',
			       		style: {
			             color: '#66b3ff'			             
			       		},
			       		flex:1,
			       		autoScroll: true,
			       		items:[
			       		       	ccg.ui.groupmemberpanel,
			       		        {
						       		width:'10px'
						       	},
			       		       	ccg.ui.docaccesspanel 
			       		       ]
			       	},
			       	{
			       		width:'2px'
			       	}
			       	,
			       	{
						region:'east',
						width:'24%',						
						xtype:'panel',
						items:[
						       ccg.ui.alluserlistpanel
						       ]
										
					}
			       	]
		}
		]
	});
});


ccg.data.allGroupStore = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: 'rest/admin/usergroup/all'
    },
    autoLoad:false,
    root: {
        text: 'Group List:',        
        expanded: true
    },
    listeners: {
        
    }
});

ccg.data.groupMemberStore = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: ''
    },
    root: {
        text: 'Group Members:',        
        expanded: true
    }
});

ccg.data.groupMemberStore1 = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
    proxy: {
        type: 'ajax',
        url: ''
    },
    root: {
        text: 'Members:',        
        expanded: true
    }
});

ccg.data.alluserlist = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: 'rest/admin/user/all'
    },
    root: {
        text: 'All Users:',        
        expanded: true
    }
});

ccg.data.groupAccessStore = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: ''
    },
    root: {
        text: 'Document Access:',        
        expanded: true
    }
});

ccg.ui.grouplistpanel =Ext.create('Ext.tree.Panel', {
	curgroupid:'',
    store: ccg.data.allGroupStore,
    width: 300,
    height: 480,
    title: 'All Groups',
    useArrows: true,
    frame:true,
    listeners:{
    itemclick: function(s,r) {           
   	 if(r.data.leaf)
   	 {
   		 // load the member panel
   		 console.log(r.data);
   		
   		ccg.data.groupMemberStore.load({url:"rest/admin/userGroupMembers/"+r.data.groupId});
   		//ccg.data.groupMemberStore1.load({url:"rest/admin/userGroupMembers/"+r.data.groupId});
   		 // load the document access panel
   		ccg.data.groupAccessStore.load({url:"rest/admin/userGroupArticles/"+r.data.groupId});
   		ccg.ui.grouplistpanel.curgroupid=r.data.groupId;
   		// load user panel
   		//ccg.data.assignuserstore.load({url:"rest/admin/userGroupNotMembers/"+r.data.groupId});
   		 
   	 }
    }
    }
});

ccg.ui.groupmemberpanel =Ext.create('Ext.tree.Panel', {
    store: ccg.data.groupMemberStore,
    title: 'Group Members',
    width: '48%',
    height: 480,
    useArrows: true,
    frame:true,
    tools: [
     {
            type: 'plus', // this doesn't appear to work, probably I need to use a valid class
            tooltip: 'Add Group Member',
            handler: function() {
             //   console.log('TODO: Add project');
                //console.log(ccg.ui.contentsearchPanel);
            	ccg.ui.assignuserpanel.getForm().reset();
                ccg.ui.assignuserpanel.getForm().setValues({groupID:ccg.ui.grouplistpanel.curgroupid});    
                
                ccg.data.assignuserstore.load({url:"rest/admin/userGroupNotMembers/"+ccg.ui.grouplistpanel.curgroupid});
                
                ccg.ui.assignuserpanel.show();
            }
        },
        {
        	type: 'minus',// this doesn't appear to work, probably I need to use a valid class
        	handler: function(){
        		ccg.ui.removeuserpanel.getForm().reset();
        		ccg.ui.removeuserpanel.getForm().setValues({groupID:ccg.ui.grouplistpanel.curgroupid});
        		ccg.ui.removeuserpanel.show();
        		ccg.data.groupMemberStore1.proxy.url="rest/admin/userGroupMembers/"+ccg.ui.grouplistpanel.curgroupid;
        		ccg.data.groupMemberStore1.load({url:"rest/admin/userGroupMembers/"+ccg.ui.grouplistpanel.curgroupid});
        		//ccg.data.groupMemberStore1.load({url:"rest/admin/userGroupMembers/"+ccg.ui.grouplistpanel.curgroupid});
        	}
        }
     ]
});
ccg.ui.docaccesspanel =Ext.create('Ext.tree.Panel', {
    store: ccg.data.groupAccessStore,
    title: 'Document Access',
    width: '48%',
    height: 480,
    useArrows: true,
    frame:true,
    tools: [
     {
            type: 'plus', // this doesn't appear to work, probably I need to use a valid class
            tooltip: 'Assign Doc Access',
            handler: function() {
                console.log('TODO: Add project');
              //  console.log(ccg.ui.contentsearchPanel);
                ccg.ui.addDocAccessPanel.getForm().reset();
                ccg.ui.addDocAccessPanel.getForm().setValues({groupID:ccg.ui.grouplistpanel.curgroupid});
                ccg.data.groupnewdocStore.proxy.url="rest/admin/userGroupArticleCandidate/"+ccg.ui.grouplistpanel.curgroupid;
                ccg.data.groupnewdocStore.load({url:"rest/admin/userGroupArticleCandidate/"+ccg.ui.grouplistpanel.curgroupid});
                ccg.ui.addDocAccessPanel.show();
            }
        },
        {
        	type: 'minus' ,// this doesn't appear to work, probably I need to use a valid class
            handler: function(){
            	//ccg.ui.removeuserpanel.show();
            	ccg.ui.removeDocAccessPanel.getForm().reset();
            	ccg.ui.removeDocAccessPanel.getForm().setValues({groupID:ccg.ui.grouplistpanel.curgroupid});
            	ccg.data.removeDocStore.proxy.url="rest/admin/userGroupArticles/"+ccg.ui.grouplistpanel.curgroupid;
            	ccg.data.removeDocStore.load({url:"rest/admin/userGroupArticles/"+ccg.ui.grouplistpanel.curgroupid});
            	ccg.ui.removeDocAccessPanel.show();
            }
        }
        ]
});

ccg.ui.alluserlistpanel =Ext.create('Ext.tree.Panel', {
    store: ccg.data.alluserlist,
    title: 'All Users',
    height: 480,
    useArrows: true,
    frame:true,
    tools: [
            {
        	type: 'minus' ,// this doesn't appear to work, probably I need to use a valid class
            handler: function(){
            	var nodeary=ccg.ui.alluserlistpanel.getSelectionModel().getSelection();
            	if(nodeary.length>0)
            	{
            	  var ele=nodeary[0];
            	  if(ele.data.userID)
            	  {
            		  Ext.Ajax.request({
   	                   url: "rest/admin/removeUser/"+ele.data.userID,
   	                   method: 'POST',   	                  
   	                   success: function(response, opts) {
   	                	ccg.data.alluserlist.load();
   	                   },
   	                   failure: function(response, opts) {
   	                      console.log('server-side failure with status code ' + response.status);   	                    
   	                   }
   	                });
            	  }
            	}            		
            }
           }
    ]
});

ccg.ui.newuserpanel=Ext.create('Ext.form.Panel', {  
    title: 'Create New User', 
    width: 300,
    bodyPadding: 10,
    defaultType: 'textfield',
    frame: true,
    id:'newuser',
    bodyBorder: true, 
    floating: true,
    closable : true,
    draggable: true,
    items: [
            {
            	xtype:"panel",
            	html:'<img src="images/user-icon.jpg" width=120 height=100 align="right" />'
            },
            {
            fieldLabel: 'User name (Email)',
            name: 'useremail',
            required:true
        }
        
    ],
    listeners:{
    	beforeclose:function(win) {
    		 ccg.ui.newuserpanel.hide();
        	 return false; 
        }
    },
    buttons: [{
        text: 'Create User',
        handler: function () {
        	
            var form = this.up('form').getForm();
            if (form.isValid()) {
               // making ajax calls
               var urlstr="rest/admin/createUser";
               console.log(form.getValues());
               Ext.Ajax.request({
                   url: urlstr,
                   method: 'POST',
                   jsonData: form.getValues(),
                   success: function(response, opts) {
                	   	ccg.data.alluserlist.load();
                		ccg.ui.newuserpanel.hide();
                   },
                   failure: function(response, opts) {
                	   alert("Could not completed your request. Please check data.")
                      console.log('server-side failure with status code ' + response.status);
                   }
                });
            }
            else
            {
            	alert("invalid data!");
            }            
        	//ccg.ui.newuserpanel.hide();
        }
    }]
});

ccg.data.assignuserstore=Ext.create("Ext.data.Store",{
	fields: [ 'userID', 'text' ],
	proxy: {
        type: 'ajax',
        url: ''
    }
});
ccg.ui.assignuserpanel=Ext.create('Ext.form.Panel', {
	 title: 'Assign User to the Group', 
	    width: 320,
	    height: 240,
	    bodyPadding: 10,
	    defaultType: 'textfield',
	    frame: true,
	    id:'assignuser',
	    bodyBorder: true, 
	    floating: true,
	    closable : true,
	    draggable: true,
	    items:[	          
	           {
	        	 fieldLabel:'Group ID',
	        	 name:'groupID',
	        	 editable:false,
	           	 fieldStyle:'color:#ccc'
	           },
	           {
	        	   fieldLabel:'Users',
	        	   xtype: 'tagfield',	        	   
	        	   name:'usernames',
	        	   multiselect:true,
	        	   store: ccg.data.assignuserstore	        	   
	           }
	    ],
	    listeners:{
	    	beforeclose:function(win) {
	    		ccg.ui.assignuserpanel.hide();
	        	 return false; 
	        }
	    },
	    displayField: 'text',
	    buttons: [{
	        text: 'Add Users',
	        handler: function () {
	        	var form=this.up('form').getForm();
	        	var url="rest/admin/addUserToGroup";
	        	Ext.Ajax.request({
	                   url: url,
	                   method: 'POST',
	                   jsonData: form.getValues(),
	                   success: function(response, opts) {
	                    console.log(response.responseText);   
	                	ccg.data.groupMemberStore.load({url:"rest/admin/userGroupMembers/"+ccg.ui.grouplistpanel.curgroupid});
	                    ccg.ui.assignuserpanel.hide();
	                   },
	                   failure: function(response, opts) {
	                      console.log('server-side failure with status code ' + response.status);
	                      alert("Update Error!!");
	                   }
	                });
	        }
	    }]
	      
});

ccg.ui.removeuserpanel=Ext.create('Ext.form.Panel', {
	 title: 'Remove User From the Group', 
	    width: 320,
	    height: 240,
	    bodyPadding: 10,
	    defaultType: 'textfield',
	    frame: true,
	    id:'removeuser',
	    bodyBorder: true, 
	    floating: true,
	    closable : true,
	    draggable: true,
	    items:[	          
	           {
	        	 fieldLabel:'Group ID',
	        	 name:'groupID',
	        	 editable:false,
	           	 fieldStyle:'color:#ccc'
	           },
	           {
	        	   fieldLabel:'Users',
	        	   xtype: 'tagfield',	        	   
	        	   name:'usernames',
	        	   multiselect:true,
	        	   store:ccg.data.groupMemberStore1	        	   
	           }
	    ],
	    listeners:{
	    	beforeclose:function(win) {
	    		ccg.ui.removeuserpanel.hide();
	        	 return false; 
	        }
	    },
	    displayField: 'text',
	    buttons: [{
	        text: 'Remove Users',
	        handler: function () {
	        	var form=this.up('form').getForm();
	        	var url="rest/admin/removeUserFromGroup";
	        	Ext.Ajax.request({
	                   url: url,
	                   method: 'POST',
	                   jsonData: form.getValues(),
	                   success: function(response, opts) {
	                    console.log(response.responseText);   
	                	ccg.data.groupMemberStore.load({url:"rest/admin/userGroupMembers/"+ccg.ui.grouplistpanel.curgroupid});
	                	ccg.ui.removeuserpanel.hide();
	                   },
	                   failure: function(response, opts) {
	                      console.log('server-side failure with status code ' + response.status);
	                      alert("Update Error!!");
	                   }
	                });
	        }
	    }]
	      
});

ccg.data.groupnewdocStore = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: ''
    },
    root: {
        text: 'Documents:',        
        expanded: true
    }
});

ccg.ui.addDocAccessPanel=Ext.create('Ext.form.Panel', {
	 title: 'Grant Document Access', 
	    width: 510,
	    height: 240,
	    bodyPadding: 10,
	    defaultType: 'textfield',
	    frame: true,
	    id:'grantdocaccess',
	    bodyBorder: true, 
	    floating: true,
	    closable : true,
	    draggable: true,
	    items:[	          
	           {
	        	 fieldLabel:'Group ID',
	        	 name:'groupID',
	        	 editable:false,
	           	 fieldStyle:'color:#ccc'
	           },
	           {
	        	   fieldLabel:'documents',
	        	   xtype: 'combo',	      
	        	   width:460,
	        	   name:'documentID',	 
	        	   displayField: 'text',
	        	   valueField: 'articleID',
	        	   store: ccg.data.groupnewdocStore	        	   
	           }
	    ],
	    listeners:{
	    	beforeclose:function(win) {
	    		ccg.ui.addDocAccessPanel.hide();
	        	 return false; 
	        }
	    },
	    displayField: 'text',
	    buttons: [{
	        text: 'Assign Docs',
	        handler: function () {
	        	var form=this.up('form').getForm();
	        	console.log(form.getValues());
	        	var url="rest/admin/addDocToGroup";
	        	
	        	Ext.Ajax.request({
	                   url: url,
	                   method: 'POST',
	                   jsonData: form.getValues(),
	                   success: function(response, opts) {
	                    console.log(response.responseText);
	                	ccg.data.groupAccessStore.load({url:"rest/admin/userGroupArticles/"+ccg.ui.grouplistpanel.curgroupid});	                    
	                	ccg.ui.addDocAccessPanel.hide();
	                   },
	                   failure: function(response, opts) {
	                      console.log('server-side failure with status code ' + response.status);
	                      alert("Update Error!!");
	                   }
	                });
	                
	        }
	    }]
	      
});

ccg.data.removeDocStore = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: ''
    },
    root: {
        text: 'Documents:',        
        expanded: true
    }
});

ccg.ui.removeDocAccessPanel=Ext.create('Ext.form.Panel', {
	 title: 'Remove Document Access', 
	    width: 510,
	    height: 240,
	    bodyPadding: 10,
	    defaultType: 'textfield',
	    frame: true,
	    id:'removedocaccess',
	    bodyBorder: true, 
	    floating: true,
	    closable : true,
	    draggable: true,
	    items:[	          
	           {
	        	 fieldLabel:'Group ID',
	        	 name:'groupID',
	        	 editable:false,
	           	 fieldStyle:'color:#ccc'
	           },
	           {
	        	   fieldLabel:'documents',
	        	   xtype: 'combo',	      
	        	   width:460,
	        	   name:'documentID',	 
	        	   displayField: 'text',
	        	   valueField: 'articleID',
	        	   store: ccg.data.removeDocStore	        	   
	           }
	    ],
	    listeners:{
	    	beforeclose:function(win) {
	    		ccg.ui.removeDocAccessPanel.hide();
	        	 return false; 
	        }
	    },
	    displayField: 'text',
	    buttons: [{
	        text: 'Romove Docs',
	        handler: function () {
	        	var form=this.up('form').getForm();
	        	console.log(form.getValues());
	        	var url="rest/admin/removeDocFromGroup";
	        	
	        	Ext.Ajax.request({
	                   url: url,
	                   method: 'POST',
	                   jsonData: form.getValues(),
	                   success: function(response, opts) {
	                    console.log(response.responseText);
	                	ccg.data.groupAccessStore.load({url:"rest/admin/userGroupArticles/"+ccg.ui.grouplistpanel.curgroupid});	                    
	                	ccg.ui.removeDocAccessPanel.hide();
	                   },
	                   failure: function(response, opts) {
	                      console.log('server-side failure with status code ' + response.status);
	                      alert("Update Error!!");
	                   }
	                });
	                
	        }
	    }]
	      
});