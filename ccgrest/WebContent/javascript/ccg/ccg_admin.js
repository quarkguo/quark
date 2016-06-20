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
	ccg.ui.loadUserAdmin();
});

ccg.data.userlist={};
ccg.ui.loadUserAdmin=function()
{
	// load userList json
	Ext.Ajax.request({
		   url: 'json/userlist.json',
		   success: function(response, opts) {
		      var o = Ext.decode(response.responseText);
		      ccg.data.userlist=o;
		      //console.log(ccg.data.userlist);
		   },
		   failure: function(response, opts) {
		      console.log('server-side failure with status code ' + response.status);
		   }
		});
	// render user accordion
	
};

ccg.data.allGroupStore = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: 'json/grouplist.json'
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
        url: 'json/userlist.json'
    },
    root: {
        text: 'Group Members:',        
        expanded: true
    }
});

ccg.data.alluserlist = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: 'json/userlist.json'
    },
    root: {
        text: 'All Users:',        
        expanded: true
    }
});

ccg.data.groupAccessStore = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: 'json/docaccesslist.json'
    },
    root: {
        text: 'Document Access:',        
        expanded: true
    }
});

ccg.ui.grouplistpanel =Ext.create('Ext.tree.Panel', {
    store: ccg.data.allGroupStore,
    width: 300,
    height: 480,
    title: 'All Groups',
    useArrows: true,
    frame:true
    
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
                console.log('TODO: Add project');
                console.log(ccg.ui.contentsearchPanel);
                ccg.ui.contentsearchPanel.show();
            }
        },
        {
        	type: 'minus' // this doesn't appear to work, probably I need to use a valid class
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
            tooltip: 'Add Group Member',
            handler: function() {
                console.log('TODO: Add project');
                console.log(ccg.ui.contentsearchPanel);
                ccg.ui.contentsearchPanel.show();
            }
        },
        {
        	type: 'minus' // this doesn't appear to work, probably I need to use a valid class
        }
        ]
});

ccg.ui.alluserlistpanel =Ext.create('Ext.tree.Panel', {
    store: ccg.data.alluserlist,
    title: 'All Users',
    height: 480,
    useArrows: true,
    frame:true
    
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
            name: 'useremail'
        },
        {
            fieldLabel: 'Name:',
            name: 'name'
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
        	/*
            var form = this.up('form').getForm();
            if (form.isValid()) {
               // making ajax calls
               var urlstr="rest/article/metadata";
               console.log(urlstr);
               Ext.Ajax.request({
                   url: urlstr,
                   method: 'POST',
                   jsonData: form.getValues(),
                   success: function(response, opts) {
                      var obj = Ext.decode(response.responseText);
                      console.log(obj);
                   },
                   failure: function(response, opts) {
                      console.log('server-side failure with status code ' + response.status);
                   }
                });
            }
            else
            {
            	alert("invalid data!");
            }
            */
        	ccg.ui.newuserpanel.hide();
        }
    }]
});
