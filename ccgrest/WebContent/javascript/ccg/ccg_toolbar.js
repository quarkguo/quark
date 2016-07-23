// here we define the tool bar
var ccg={};
ccg.ui={};
ccg.data={};

ccg.data.docliststore = Ext.create('Ext.data.TreeStore', {
        proxy: {
            type: 'ajax',
            url: 'rest/articleList'
        },
        root: {
            text: 'Document List:',
            id: 'src',
            expanded: true
        }
    });

ccg.ui.loadDocCategory=function(arcID)
{
	 ccg.ui.doccategory.getRootNode().removeAll();
     
     var urlstr="rest/article/"+arcID+"/category";
     console.log(urlstr);         
   //  ccg.data.doccategorystore.removeAll();
     ccg.data.doccategorystore.load({url:urlstr});
     // here load meta data panel
     var metaurl="rest/article/"+arcID+"/metadata";
     console.log(metaurl);
     Ext.Ajax.request({
    	 url: metaurl,
    	 method:"GET",
    	 success: function(response, opts) {
    		 var jdata = Ext.decode(response.responseText);
    		 console.log(jdata);
    		 console.log( ccg.ui.metapanel.getForm());
    		 jdata.articleId=arcID;
    		 ccg.ui.metapanel.getForm().reset();
    		 ccg.ui.metapanel.getForm().setValues(jdata);
    	 },
    	 failure: function(response, opts) {
    		 alert("load data error!!");
    	 }
     });
    

};
 ccg.ui.doclist =Ext.create('Ext.tree.Panel', {
     store: ccg.data.docliststore,
     height: 360,
     width:320,
     title: 'Document List',     
     useArrows: true,
     listeners: {
         itemclick: function(s,r) {           
        	 if(r.data.leaf)
        	 {
        		 // here load category panel
        		 var arcID=r.data.articleID;
        		 ccg.ui.loadDocCategory(arcID);
        	 }
        	 
         }
     },
     dockedItems: [{
         xtype: 'toolbar',
         items: []
     }]
 });


Ext.define("com.ccg.portalheader",{
	extend:"Ext.Container",
	
	xtype:"ccg-portal-header",
	title:document.title,
	cls:'ccg-app-header',
	height:48,
	layout:{
		type:'hbox',
		align:'middle'
	},
	items:[{
			xtype:'component',
			cls:'ccg-logo'
		},
		{
			xtype:'component',
			cls:'ccg-header-title',
			html:document.title,
			flex:1
		}
		
	]	
});


Ext.define('com.ccg.toolbar',{
		  extend:"Ext.toolbar.Toolbar",
		  style: 'background-image: -webkit-linear-gradient(top,#2ba4c4, #2589a3)',
		  //style:'backgrond-color:green',
		  height:48,
		  layout:{
				type:'hbox',
				align:'middle'
			},
		  title:document.title,	
		  xtype:"ccg-toolbar",		
		  items:[
		         {
		        	 xtype:'component',
		        	 cls:'ccg-logo'
		         },
		         {
		        	 xtype:'component',		        	 
		        	 html:document.title,
		        	 cls:'.ccg-header-title',
		        	 flex:1
		         },
		    {
                    text: 'Content Ingestion',
                    iconCls: 'edit',                    
                    displayText: 'Content Ingestion',
                    handler: function(){
                    	console.log('upload file....');
                    	console.log(ccg.ui.uploadfilepanel);
                    	ccg.ui.uploadfilepanel.show();
                    	
                    }
		    },
		    {
                    text: 'Content Management',
                    iconCls: 'file',                    
                    displayText: 'Content Management'                  	 
		    }
		    ,
		    {
                    text: 'Content Builder',
                    iconCls: 'file',                    
                    displayText: 'Content Management'                  	 
		    }
		    ,
		    {
                    text: 'Admin',
                    iconCls: 'file',                    
                    displayText: 'Content Management'                  	 
		    },
		    {
                text: 'Profile',
                iconCls: 'file',     
                handler: function() {
                    console.log('TODO: Add project');
                    console.log(ccg.ui.contentsearchPanel);
                    ccg.ui.userprofilepanel.show();
                    // here we need to pull all content
                    var userprofileurl="rest/user/profile";
                    Ext.Ajax.request({
                   	 url: userprofileurl,
                   	 method:"GET",
                   	 success: function(response, opts) {
                   		 var jdata = Ext.decode(response.responseText);
                   		 console.log(jdata);
                   		ccg.ui.userprofilepanel.getForm().reset();
                   		ccg.ui.userprofilepanel.getForm().setValues(jdata);
                   	 },
                   	 failure: function(response, opts) {
                   		 alert("load data error!!");
                   	 }
                    });
                }
                                  	
		    },
		    {
                text: 'Reset Password',
                iconCls: 'file',     
                handler: function() {
                    var userprofileurl="rest/user/profile";
                    Ext.Ajax.request({
                   	 url: userprofileurl,
                   	 method:"GET",
                   	 success: function(response, opts) {
                   		 var jdata = Ext.decode(response.responseText);
                   		 console.log(jdata);
                   		ccg.ui.passwordresetpanel.getForm().reset();
                   		ccg.ui.passwordresetpanel.getForm().setValues(jdata);
                   	 },
                   	 failure: function(response, opts) {
                   		 alert("load data error!!");
                   	 }
                    });
                    ccg.ui.passwordresetpanel.show();
                    //ccg.ui.passwordresetpanel.getForm().setValues({useremail:"ccg"});
                }
                                  	
		    },
		    {
                text: 'Logout',
                iconCls: 'file',     
                handler: function() {                    
                    //console.log(ccg.ui.contentsearchPanel);
                	 Ext.Ajax.request({
                       	 url: 'rest/user/logout',
                       	 method:"GET",
                       	 success: function(response, opts) {
                       		 window.location="ccgportal.html";
                       		 //window.refresh();
                       	 }
                	 });
                }
		    }
		  ]
	});

Ext.define('com.ccg.admintoolbar',{
	  extend:"Ext.toolbar.Toolbar",
	 // style: 'background-image: -webkit-linear-gradient(top,#2ba4c4, #2589a3)',
	  //style:'backgrond-color:green',
	  height:48,
	  layout:{
			type:'hbox',
			align:'middle'
		},
	  title:document.title,	
	  xtype:"ccg-admin-toolbar",		
	  items:[
	         {
	        	 xtype:'component',
	        	 cls:'ccg-logo'
	         },
	         {
	        	 xtype:'component',		        	 
	        	 html:document.title,
	        	 cls:'.ccg-header-title',
	        	 flex:1
	         },
	   
	    {
          text: 'New User',
          iconCls: 'file',
          handler: function() {
              ccg.ui.newuserpanel.show();
          }
	    },
	    {
	    	text:'New Group',
	    	iconCls:'file'	
	    }
	    
	  ]
});
