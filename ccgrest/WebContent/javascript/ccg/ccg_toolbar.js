// here we define the tool bar
var ccg={};
ccg.ui={};
ccg.data={};
ccg.data.currentArticle={};
ccg.article={};
ccg.article.pattern={};
ccg.data.pattern=[];
ccg.data.licensemessage = false;
ccg.currentProject={projectId:0, projectName:'My Project'}

contextMenu = new Ext.menu.Menu({
    items: [{
        id: 'add_to_current_project',
        text: 'Add to current project'
    }],
    listeners: {
        click: function(item) {
        	console.log(this.article);
        	
        	Ext.Msg.confirm("Confirmation", "Add article: <b>" + this.article.articleTitle + '</b> <br /> to Project:<b>' + ccg.currentProject.projectName + '</b>?', function(btnText){
        		if(btnText === "no"){
        			
        		}else if(btnText === "yes"){
                	var urlstr = 'rest/project/addArticle';
                	var postdata = {'article': this.article, 'project':ccg.currentProject };
                		
                    Ext.Ajax.request({
                        url: urlstr,
                        method: 'POST',
                        jsonData: postdata,
                        success: function(response, opts) {
                           //var obj = Ext.decode(response.responseText);
                           console.log(response);
                           //window.location.reload();
                        },
                        failure: function(response, opts) {
                           console.log('server-side failure with status code ' + response.status);
                        }
                     });        			
        		}     		
        	}, this);
        }
    }
});

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
     ccg.data.doccategorystore.on("load",
    		 function (eopts)
    		 {
    	 		ccg.ui.doccategory.expandAll();
    		 }
     );
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
     
	// load pdf file to main panel
    Ext.getCmp("navipanel").hide();
	Ext.getCmp("previcon").hide();
	Ext.getCmp("nexticon").hide();
    var pdfurl = "rest/article/"+arcID+"/download";
    var pdfPanel=document.getElementById('pdfcontent');
	pdfPanel.src=pdfurl;

};

 ccg.ui.doclist =Ext.create('Ext.tree.Panel', {
     store: ccg.data.docliststore,
 //    title: 'Document List',     
     useArrows: true,    
     frame:true,
     minHeight:'280',
     maxHeight: 360,
     border:true,
     collapsible:true,
     autoScroll: true,

     listeners: {
         itemclick: function(s,r) {           
        	 if(r.data.leaf)
        	 {
        		 // here load category panel
        		 var arcID=r.data.articleID;
        		 // set artileID for loading PDF
        		 ccg.data.currentArticle.id=arcID;
        		 ccg.data.currentArticle.title=r.data.text;
        		 ccg.ui.loadDocCategory(arcID);
        	 }
        	 
         },

         itemcontextmenu: function(view,record,item,index,e) {
        	 e.preventDefault();
        	 var article={'articleId':record.data.articleID, 'articleTitle':record.data.text};
        	 console.log(article);
        	 contextMenu.article = article;
        	 contextMenu.showAt(e.getXY());
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
		        	 id:'clientname',
		        	 html:'clientname',
		        	 style: 'color:white;font-weight: bold',
		        	 cls:'.ccg-header-title',
		        	 flex:1
		         },   
		         {
			        xtype:'button',
                    text: 'Content Ingestion',
                    id:'Ingestion',
                    iconCls: 'edit',                    
                    //displayText: 'Content Ingestion',
                    icon:'images/od-ingest.png',
                    hidden:true,
                    handler: function(){
                    	console.log('upload file....');
                    	console.log(ccg.ui.uploadfilepanel);
                    	ccg.ui.uploadfilepanel.show();   	
                    }
		         },
//		         {
//			        xtype:'button',
//                    text: 'Project',
//                    id:'project',
//                    //iconCls: 'edit',                    
//                    icon:'images/od-ingest.png',
//                    handler: function(){
//                    	console.log('upload file....');
//                    	console.log(ccg.ui.uploadfilepanel);
//                    	ccg.ui.uploadfilepanel.show();   	
//                    }
//			     },		         
		         {
		        	text: 'Project',
		        	menu: {
		        		xtype: 'menu',
		        		items: [{
		        			text: 'New Project',
		        			handler: function(){
		        				ccg.ui.projectpanel.show();
		        			}
		        		},{
		        			text: 'Menu Two',
		        			menu: {
		        				xtype: 'menu',
		        				items: [{
		        					text: 'Next Level'
		        				},{
		        					text: 'Next Level'
		        				},{
		        					text: 'Next Level'
		        				}]
		        			}
		        		},{
		        			text: 'Menu Three',
		        			scale: 'small'
		        		},{
		        			text: 'Menu Four',
		        			scale: 'small'
		        		}]
		        	}
		         },
		         {
                    text: 'Admin',
                    id:'Admin',
                    iconCls: 'file',    
                    icon:'images/profile.png',                 
                    displayText: 'Content Management',
                    //disabled:true,
                    hidden: true,
                    handler: function(){
                    	window.open("ccg_admin.html");
                    }
                  
		         },
		         {
	                text: 'Profile',
	                iconCls: 'file',  
	                icon:'images/usericon.png',    
	                handler: function() {                   
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
	                icon:'images/login_icon.png',    
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
	                icon:'images/logout.png',    
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
		  ],
		  listeners:{
	         beforerender: function(component, eOpts){
	        		Ext.Ajax.request({
	        	 		url:'rest/user/groups',
	        	 		callback: function(options,success,response) {
	        	 			var groups = Ext.util.JSON.decode(response.responseText);
	        	 			console.log(groups);
	        	 			if(groups.indexOf('admin') > -1){
	        	 				Ext.getCmp("Admin").show();
	        	 				Ext.getCmp("Ingestion").show();
	        	 			}
	        	 		}	
	        	 	});
	        		
	        		Ext.Ajax.request({
	        			 url: 'rest/client/name',
	        			 method:"GET",
	        			 success: function(response, opts) {
	        				 Ext.getCmp('clientname').update(response.responseText);
	        			 }
	        		});
	        		
	         },
	         afterlayout: function(){
	        		Ext.Ajax.request({
	        	 		url:'rest/user/licenseinfo',
	        	 		callback: function(options,success,response) {
	        	 			var licenseinfo = response.responseText;
	        	 			console.log(response.responseText);
	        	 			var showlicenseinfo = Ext.util.Cookies.get('showlicenseinfo');
	        	 			if(licenseinfo.length != 0 && !showlicenseinfo){
	        	 				var abx = Ext.Msg.alert('License Warning!!!', response.responseText);
	        	 				Ext.util.Cookies.set('showlicenseinfo', true);
	        	 			}
	        	 		}	
	        	 	});
	         }
		  }	
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
	    	iconCls:'file',
	        handler: function(){
	        	ccg.ui.createNewGroupPanel.show();
	        }
	    }
	    
	  ]
});
