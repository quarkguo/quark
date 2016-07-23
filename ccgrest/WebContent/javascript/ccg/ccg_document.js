
ccg.data.doccategorystore = Ext.create('Ext.data.TreeStore', {
        proxy: {
            type: 'ajax'
           // url: 'json/doc1_category.json'
        },
        autoLoad:false,
        root: {
            text: 'Document Category:',        
            expanded: true
        },
        listeners: {
            
        }
    });
ccg.data.relateddocstore = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: 'json/doclist.json'
    },
    root: {
        text: 'Related Content:',        
        expanded: true
    }
});

ccg.ui.doccategory =Ext.create('Ext.tree.Panel', {
    store: ccg.data.doccategorystore,
 //   height: 360,
    width: 300,
    title: 'Document Cateogry',
    useArrows: true,
    autoload:false,
    tools:[
	{
		type: 'gear', // this doesn't appear to work, probably I need to use a valid class
		tooltip: 'Document Meta Data',
		handler: function() {
			ccg.ui.metapanel.show();
		}
	}
    ],
    listeners: {
        itemclick: function(s,r) {           
       	 	console.log(r);
       	 	if(r.data.categoryID)
       	 	{
       	 		// pull content of categoryID
       	 	   var urlstr="rest/category/"+r.data.categoryID+"/content";
       	 	   console.log(urlstr);      	 	   
       	 	   // ajax call
       	 	   Ext.Ajax.request({
       	       url: urlstr,

       	       callback: function(options, success, response) {
       	    	 console.log(response.responseText);
       	    	var o= Ext.util.JSON.decode(response.responseText);
       	    	console.log(o);
       	    	Ext.getCmp('contentpanel').update(o.categorycontent);
       	    	Ext.getCmp('contentpanel').setTitle("Content Panel -- Article:["+o.articleID+"] -- Category:["+o.categoryID+"]");
       	     }
       	 	});
       	 	}
       	 	else if(r.data.subcategoryID)
    	 	{
        	 	   var urlstr="rest/subcategory/"+r.data.subcategoryID+"/content";
           	 	   console.log(urlstr);      	 	   
           	 	   // ajax call
           	 	   Ext.Ajax.request({
           	       url: urlstr,

           	       callback: function(options, success, response) {
           	    	 console.log(response.responseText);
           	    	var o= Ext.util.JSON.decode(response.responseText);
           	    	console.log(o);
           	    	Ext.getCmp('contentpanel').update(o.subcategorycontent);
           	    	Ext.getCmp('contentpanel').setTitle("Content Panel -- Article:["+o.articleID+"] -- Category:["+o.categoryID+"] -- SubCategory:["+o.subcategoryID+"]");
           	     }
           	 	});
    	 		
    	 		
    	 	}
        }
    },
});

ccg.ui.contentsearchPanel=Ext.create('Ext.window.Window', {
    title: 'Search Content', 
    width: 300,
    bodyPadding: 10,
    defaultType: 'textfield',
    frame: true,
    id:'contentSearchPanel',
    bodyBorder: true,
    items: [
            {
            	fieldLabel: 'Keyword:',
            	name: 'query'            	
            }],
	            buttons: [{
                text: 'search',
                handler: function () {
                	//alert("search");
                	var keyword=ccg.ui.contentsearchPanel.items.items[0].getValue();
                	//console.log(keyword);
                	
                	var jdata={"query":keyword,"limit":100};
                	console.log(jdata);
                	ccg.data.relateddocstore.load({url:"rest/search",params:jdata,method:"GET"});
                	/*
                	Ext.Ajax.request({
                  	     url: "rest/search",
                  	     method:"GET",
                  	     params:jdata,
                  	   success: function(response, opts) {
                           var jdata = Ext.decode(response.responseText);
                           console.log(jdata);
                         //  ccg.ui.relateddoclist.update(jdata);
                        },
                        failure: function(response, opts) {
                           console.log('server-side failure with status code ' + response.status);
                        }
                	});
                	*/
                	ccg.ui.contentsearchPanel.hide();
                }
            	},
                {
                 text: 'Cancel',
                 handler: function () {
                	 ccg.ui.contentsearchPanel.hide();
                 }
                }
            ],
            listeners:{
            	beforeclose:function(win) {
                	 ccg.ui.contentsearchPanel.hide();
                	 return false; 
                }
            }
});
ccg.ui.relateddoclist =Ext.create('Ext.tree.Panel', {
    store: ccg.data.relateddocstore,
    height: 240,
    width: 300,
    title: 'Related Content:',
    useArrows: true,
    tools: [
     {
            type: 'search', // this doesn't appear to work, probably I need to use a valid class
            tooltip: 'Search Related Content',
            handler: function() {
                console.log('TODO: Add project');
                console.log(ccg.ui.contentsearchPanel);
                ccg.ui.contentsearchPanel.show();
            }
        }],
    listeners: {
            itemclick: function(s,r) {           
           	 	console.log(r);
           	 	if(r.data.categoryID)
           	 	{
           	 		// pull content of categoryID
           	 	   var urlstr="rest/category/"+r.data.categoryID+"/content";
           	 	   // set gloabl variable for PDF
           	 	   ccg.data.currentArticle.id=r.data.articleId;
           	 	   ccg.data.currentArticle.title=r.data.articleTitle;
           	 	   console.log(urlstr);      	 	   
           	 	   // ajax call
           	 	Ext.Ajax.request({
           	     url: urlstr,
           	     callback: function(options, success, response) {
           	    	 console.log(response.responseText);
           	    	var o= Ext.util.JSON.decode(response.responseText);
           	    	console.log(o);
           	    	var keyword=ccg.ui.contentsearchPanel.items.items[0].getValue();
           	    	var regex=new RegExp('(' + keyword + ')', 'gi')
           	    	var replacedtext=o.categorycontent.replace(regex, "<span class='category-content-search-token'>$1</span>")
           	    	console.log(replacedtext);
           	    	Ext.getCmp('contentpanel').update(replacedtext);
           	    	Ext.getCmp('contentpanel').setTitle("Content Panel -- Article:["+o.articleID+"] -- Category:["+o.categoryID+"]");
           	    	// need to sync the tree panel and category panel
           	    	var root=ccg.ui.doclist.getRootNode();
           	    	console.log(root);
           	    	for(var i=0;i<root.childNodes.length;i++)
           	    	{
           	    		var child=root.childNodes[i];
           	    		console.log(child);
           	    		if(child.data.articleID+""==o.articleID+"")
           	    		{
           	    			console.log("found "+child);
           	    			ccg.ui.doclist.getSelectionModel().select(child,true);
           	    			//ccg.ui.doclist.fireEvent('itemclick',child,0);
           	    			ccg.ui.loadDocCategory(o.articleID);
           	    			break;
           	    		}
           	    	}
           	     }
           	 });
           	 	}}
    }
});

ccg.ui.docmainpanel=Ext.create("Ext.panel.Panel",{	
	layout:'border',
	items:[{
		region:'west',
		title:'content',		
	},
	{ 
		region: 'center',
		items:[
		  
		]
	}
	]
});

ccg.ui.metapanel=Ext.create('Ext.form.Panel', {  
    title: 'Document Metadata', 
    width: 300,
    bodyPadding: 10,
    defaultType: 'textfield',
    frame: true,
    id:'docmetaform',
    bodyBorder: true, 
    floating: true,
    closable : true,
    draggable: true,
    items: [
            {
            	fieldLabel: 'Article ID',
            	name: 'articleId'
            	
            },
        {
            fieldLabel: 'Title',
            name: 'title'
        },
        {
            fieldLabel: 'Type',
            name: 'type'
        },
        {
            fieldLabel: 'Author',
            name: 'author'
        },
        {
            fieldLabel: 'Company',
            name: 'company'
        },
        {
            fieldLabel: 'Accept Status',
            name: 'acceptStatus'
        },
        {
            fieldLabel: 'Praisal Scores',
            name: 'praisalscore'
        }
        /*{
            xtype: 'datefield',
            fieldLabel: 'Created Date',
            name: 'createDate'
        }*/
    ],
    listeners:{
    	beforeclose:function(win) {
    		 ccg.ui.metapanel.hide();
        	 return false; 
        }
    },
    buttons: [{
        text: 'Submit',
        handler: function () {
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
            ccg.ui.metapanel.hide();
        }
    }]
});

ccg.ui.userprofilepanel=Ext.create('Ext.form.Panel', {  
    title: 'Profile', 
    width: 300,
    bodyPadding: 10,
    defaultType: 'textfield',
    frame: true,
    id:'userprofile',
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
            	fieldLabel:'User ID',
            	name:'userID',
            	editable:false,
            	fieldStyle: 'color: #ccc;'            	
            }
            ,
            {
            fieldLabel: 'User name (Email)',
            name: 'username',
            editable:false,
            fieldStyle: 'color: #ccc;'   
        },
        {
            fieldLabel: 'Name:',
            name: 'name',
            fieldStyle: 'color: #336699;'   
        },
        {
            fieldLabel: 'Address',
            name: 'address',
            fieldStyle: 'color: #336699;'
        },
        {
            fieldLabel: 'phone',
            name: 'phone',
            fieldStyle: 'color: #336699;'
        }
        
    ],
    listeners:{
    	beforeclose:function(win) {
    		 ccg.ui.userprofilepanel.hide();
        	 return false; 
        }
    },
    buttons: [{
        text: 'Update',
        handler: function () {
        	
            var form = this.up('form').getForm();
            console.log(form.getValues());
            if (form.isValid()) {
               // making ajax calls
               var urlstr="rest/user/updateProfile";
               console.log(urlstr);
               Ext.Ajax.request({
                   url: urlstr,
                   method: 'POST',
                   jsonData: form.getValues(),
                   success: function(response, opts) {
                    console.log(response.responseText);   
                    ccg.ui.userprofilepanel.hide();
                   },
                   failure: function(response, opts) {
                      console.log('server-side failure with status code ' + response.status);
                      alert("Update Error!!");
                   }
                });
            }
            else
            {
            	alert("invalid data!");
            }
            
        	 
        }
    }]
});

ccg.ui.passwordresetpanel=Ext.create('Ext.form.Panel', {  
    title: 'Password Reset', 
    width: 300,
    bodyPadding: 10,
    defaultType: 'textfield',
    frame: true,
    id:'passwordreset',
    bodyBorder: true, 
    floating: true,
    closable : true,
    draggable: true,
    items: [
            {
            	xtype:"panel",
            	html:'<img src="images/padlock.jpg" width=100 height=100 align="right" />'
            },
            {
            	fieldLabel:'User ID',
            	name:'userID',
            	editable:false,
            	fieldStyle: 'color: #ccc;'            	
            }
            ,
            {
            fieldLabel: 'User name (Email)',
            name: 'username',
            editable:false,
            fieldStyle: 'color: #ccc;'   
           },
            {
            inputType:'password',
            fieldLabel: 'Current Password',
            name: 'oldpass',
            required:true
        },
        
        {
        	 inputType: 'password',
            fieldLabel: 'New Password:',
            name: 'newpass',
            require: true            
        },
        {
        	 inputType: 'password',
            fieldLabel: 'Retype New Password',
            name: 'newpass2'
           
        }
        
    ],
    listeners:{
    	beforeclose:function(win) {
    		ccg.ui.passwordresetpanel.hide();
        	 return false; 
        }
    },
    buttons: [{
        text: 'Reset Password',
        handler: function () {
         var form = this.up('form').getForm();
         var pv=form.getValues();
         if(pv.oldpass==""||pv.newpass==""||pv.newpass2==-"")
         {
        	 Ext.Msg.alert("Error Message","Missing Data!!");
        	 return;
         }
         else
        	 {
        	 if(pv.newpass!=pv.newpass2)
        		 {
        		 Ext.Msg.alert("Error Message","new Passwords do not match.");
        		  return;
        		 }
        	 }
         // here the data pass validation
               var urlstr="rest/user/updatePassword";
            
               Ext.Ajax.request({
                   url: urlstr,
                   method: 'POST',
                   jsonData: pv,
                   success: function(response, opts) {
                     if(response.responseText.indexOf("success")>-1)
                     {
                    	 Ext.Msg.alert("Message","Password Updated!!");
                    	   ccg.ui.passwordresetpanel.hide();
                     }
                     else
                     {
                    	 Ext.Msg.alert("Message","Password Update Failed!")
                     }
                   },
                   failure: function(response, opts) {
                	   Ext.Msg.alert("Error","Could not update Password");
                   }
                });
            
        }
    }]
});
ccg.ui.uploadfilepanel=Ext.create('Ext.form.Panel', { 
	id : 'uploadfileform',
	//renderTo : 'formId',
	border : true,
	title: 'Upload file',
	width : 600,
	bodyPadding: 10,
    bodyBorder: true, 
    floating: true,
    closable : true,
    draggable: true,
    hasUpload:true,
	items : [ 
	    {
	        xtype: 'filefield',
	        name: 'file',
	        fieldLabel: 'File to be uploaded',
	        labelAlign: 'right',
	       // labelWidth: 50,
	        msgTarget: 'side',
	        allowBlank: false,
	        anchor: '100%',
	        buttonText: 'Select Photo...',
			listeners: {
				change: function(fld, value) {
					var newValue = value.replace(/C:\\fakepath\\/g, '');
					fld.setRawValue(newValue);
				}
			}
	    },		
		
		{
			xtype : 'textarea',
			fieldLabel : 'Description',
			name: 'description',
			labelAlign: 'right',
			anchor : '100%',
		},
		{
			xtype : 'textfield',
			fieldLabel : 'Type',
			name: 'type',
			labelAlign: 'right',
			anchor : '100%',
		},
		{
			xtype : 'fieldcontainer',
			fieldLabel : 'Category Pattern',
			defaultType : 'radiofield',
			labelAlign: 'right',
			defaults : {
				flex : 1
			},
			//layout : 'hbox',
			items : [ {
				boxLabel : '1.  2. | 1.1.  2.1. ',
				inputValue : 'proposal_1',
				name: 'pattern',
				id : 'radio1'
			}, {
				boxLabel : '1.0  2.0  | 1.1  2.1 ',
				inputValue : 'proposal_2',
				name: 'pattern',
				id : 'radio2'
			}, {
				boxLabel : '1  2  | 1.1  2.1 ',
				inputValue : 'proposal_3',
				name: 'pattern',
				id : 'radio3'
			} ]
		},
		{
			xtype : 'textfield',
			fieldLabel : 'Status',
			name: 'status',
			labelAlign: 'right',
			anchor : '100%',
		},
		{
			xtype : 'textfield',
			fieldLabel : 'Company',
			name: 'company',
			labelAlign: 'right',
			anchor : '100%',
		},		
	],
    listeners:{
    	
    	beforeclose:function(win) {
    		ccg.ui.uploadfilepanel.hide();
        	 return false; 
        }
    },
    buttons: [{
        text: 'Upload',
        handler: function() {
            var form = this.up('form').getForm();
            if(form.isValid()){
                form.submit({
                    url: 'upload',
                    waitMsg: 'Uploading your file...',
                    success: function(form, action) {
                    	console.log('=== success');
                    	Ext.MessageBox.confirm('Upload Confirmation', action.result.category, function(btn){
                    		if(btn === 'yes'){
                    		       //some code
                    			console.log("yes");
                    			var waitbox = Ext.MessageBox.wait("Processing....", "Please wait....");
                    			
                    			Ext.Ajax.request({
                    				url: 'upload',
                    				method:'POST',
                    				params: {
                    					action: 'confirmed',
                    					requestData:action.result.base64Request
                    				},
                    				success: function(responseMsg){
                    					waitbox.hide();
                    					Ext.MessageBox.confirm("Success", "Do you want load another one?", function(btn){
                    						if(btn === 'no'){
                    							ccg.ui.uploadfilepanel.hide();
                    						}
                    					});
                    				},
                    				failure: function(responseMsg){
                    					waitbox.hide();
                    					Ext.MessageBox.alert("Failure", responseMsg);
                    				}
                    			});      			
                    			
                    		}
                    		else{
                    			console.log("No");
                    			// do nothiing;
                    		}
                    	});
                    },
                    failure: function(form, action){
                    	console.log('==== fail====');
                    	Ext.Msg.alert('Failure', action.response.responseText);
                    }
                });
            } 
            
        }
    }]
});	
