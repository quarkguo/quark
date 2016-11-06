
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
/*
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
*/
ccg.data.buildSearchStore = function(jsondata,querystr){
	 var s=Ext.create('Ext.data.TreeStore', {
		    proxy: {
		        type: 'ajax',
		        url: ''
		    },
		    root: {
		        text: 'Related Content for Search:['+querystr+']',        
		        expanded: true
		    }
		 });
	 return s;
};
ccg.ui.doccategory =Ext.create('Ext.tree.Panel', {
    store: ccg.data.doccategorystore,
    minHeight: 240,
    maxHeight: 400,
//    width: 300,
    title: 'Document Cateogry',
    useArrows: true,
    autoScroll: true,
    overflowY : 'scroll',
    scroll:'vertical',
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
        	if(r.data&&r.data.startPage)
        	{
        		ccg.ui.updateSelectedContent(r.data);
        	}
        }
    },
});

ccg.ui.handleContentTBClick = function(index)
{
	 console.log(index);
	 var np=Ext.getCmp("navipanel");
	 var data=np.items.get(index).data;
	 var url='rest/article/'+data.articleID+'/'+data.startPage+'-'+data.endPage+'/download'
	 //ccg.ui.updateSelectedContent(data);
		var pdfPanel=document.getElementById('pdfcontent');
	 	pdfPanel.src=url;
	 
	 //console.log(eledata);
	 // here we update the content
};

ccg.ui.updateSelectedContent= function(data,searchKey){
	 	// rendering PDF document
		//Ext.getCmp('contenttabpanel').setActiveTab(0);
		var url='rest/article/'+data.articleID+'/'+data.startPage+'-'+data.endPage+'/download'
		if(searchKey!=null)
		{
			var query=escape(searchKey);
			url='rest/article/'+data.articleID+'/'+data.startPage+'-'+data.endPage+'/download';
			if(data.startPage==data.endPage)
			{
				url='rest/article/'+data.articleID+'/'+data.startPage+'/'+query+'/download';
				Ext.getCmp("navipanel").show();
				Ext.getCmp("previcon").show();
				Ext.getCmp("previcon").page=data.startPage;
				Ext.getCmp("previcon").articleID=data.articleID;
				Ext.getCmp("previcon").key=query;
				Ext.getCmp("nexticon").show();
				// now build the category panel
				var naviurl="rest/article/article/"+data.articleID+"/"+data.startPage+"/flatcategory";
				console.log(naviurl);
				Ext.Ajax.request({
			 		url:naviurl,
			 		callback: function(options,success,response) {
			 		var ary= Ext.util.JSON.decode(response.responseText);
			 		//console.log(o);
			 		Ext.getCmp("navipanel").removeAll();
			 			for(var i=0;i<ary.length&&i<5;i++)
			 			{
			 				var ele=ary[i];
			 				var item=Ext.create('Ext.toolbar.TextItem'
			 				);
			 				var ar=ele.text;
			 				if(ar.length>26) ar=ar.substring(0,24)+"...";
			 				item.html="<a href='#' onclick='ccg.ui.handleContentTBClick("+i+");return false;'><font color=green size=-2><u>["+ar+"]</u></font></a>";
			 				item.data=ele;
			 				Ext.getCmp("navipanel").add(item);
			 			}
			 			Ext.getCmp("navipanel").show();
			 			console.log("===== navipanel show");
			 		}
			 	});
			}
			else
			{	
				Ext.getCmp("navipanel").hide();
				Ext.getCmp("previcon").hide();
				Ext.getCmp("nexticon").hide();
			}
		}
		else
		{
			Ext.getCmp("navipanel").hide();
			Ext.getCmp("previcon").hide();
			Ext.getCmp("nexticon").hide();
		}
	 	var pdfPanel=document.getElementById('pdfcontent');
	 	pdfPanel.src=url;
	 	
	 	// here hide the navigate panel and hide prev/next button
	 	// now rendering Text content
	 	/*
	 	var texturl='rest/article/'+data.articleID+'/'+data.startposi+'-'+data.endposi+'/textcontent'
	 	//console.log(texturl);
	 	Ext.Ajax.request({
	 		url:texturl,
	 		callback: function(options,success,response) {
	 		var o= Ext.util.JSON.decode(response.responseText);
	 		if(searchKey!=null)
	 		{
	 			o=o.replace(searchKey,"<font color=red>"+searchKey+"</font>")
	 		}
	 		
	 		Ext.getCmp('contentpanel').update(o);
	 		Ext.getCmp('contentpanel').setTitle("Article:["+data.articleID+"] -- ["+data.text+"]");
	 		}
	 	});
	 	*/
};

ccg.ui.contentsearchPanel=Ext.create('Ext.window.Window', {
    title: 'Search Content', 
    width: 360,
    bodyPadding: 10,
    defaultType: 'textfield',
    frame: true,
    id:'contentSearchPanel',
    bodyBorder: true,
    items: [
            {
            	fieldLabel: 'Keyword:',
            	name: 'query'            	
            },
            {
            	xtype: 'checkboxgroup',            	
            	fieldLabel: 'Articel Types',
            	allowBlank:false,
            	columns:3,
            	id: 'articletype',            	
            	items:[
             	]
            }
            ],
	            buttons: [{
                text: 'search',
                handler: function () {
                	//alert("search");
                	if(Ext.getCmp('categorytabpanel').items.length>3)
                	{
                		Ext.Msg.alert("At Most Three Search Panels. Please close one before new search.");
                		return ;
                	}
                	var keyword=ccg.ui.contentsearchPanel.items.items[0].getValue();
                	//console.log(keyword);
                	
                	var jdata={"query":keyword,"limit":500};
                	console.log(jdata);
                	var thestore=ccg.data.buildSearchStore(jdata,keyword);
                    var searchPanel=new ccg.ui.relateddoclist(thestore,keyword);
                    Ext.getCmp('categorytabpanel').add(searchPanel).show();
                 
                    	var myMask = Ext.MessageBox.wait("Processing....","Searching Article...");
                   // 
                    // now load data
                   // thestore.on("load",function(){myMask.hide();})
                    thestore.load({url:"rest/search",params:jdata,method:"GET",callback:function(){myMask.close();}});
                	//ccg.data.relateddocstore.on("load",function(eopts){ccg.ui.relateddoclist.expandAll();});
                	//ccg.data.relateddocstore.load({url:"rest/search",params:jdata,method:"GET"});
                	
                    /*
                	Ext.Ajax.request({
                  	     url: "rest/search",
                  	     method:"GET",
                  	     params:jdata,
                  	   success: function(response, opts) {
                           var jdata = Ext.decode(response.responseText);
                           console.log(jdata);
                           console.log(keyword);
                           var thestore=ccg.data.buildSearchStore(jdata,keyword);
                           var searchPanel=new ccg.ui.relateddoclist(thestore,keyword);
                           Ext.getCmp('categorytabpanel').add(searchPanel).show();
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
                beforerender: function(component, eOpts){
                    Ext.Ajax.request({
                      	 url: 'rest/config/articletype',
                      	 method:"GET",
                      	 success: function(response, opts) {
                      		 var typenames = Ext.decode(response.responseText);
                      		Ext.getCmp("articletype").removeAll();
                      		
                      		console.log(typenames);
                      		
                      		for(var i=0;i<typenames.length;i++)
                      		{
                      			var ele=typenames[i];
                      			var checkbox=new Ext.form.Checkbox({boxLabelAlign:'before',boxLabel:ele,name:ele,inputValue:ele})
                      			Ext.getCmp("articletype").items.add(checkbox);
                      		}
                      		Ext.getCmp("articletype").doLayout();
                      	 },
                      	 failure: function(response, opts) {
                      		 alert("load data error!!");
                      	 }
                    });
                },
            	beforeclose:function(win) {
                	 ccg.ui.contentsearchPanel.hide();
                	 return false; 
                }
            }
});
// define related documement
ccg.ui.mycolors=["#ffcccc","#ccffcc","orange"];
ccg.ui.relateddoclist = function (datastore,searchkey){ 
	var index=Ext.getCmp('categorytabpanel').items.length;
	var c=ccg.ui.mycolors[index-1];
	var panel=Ext.create('Ext.tree.Panel', {
	minHeight: 320,
	maxHeight:360,
	style: {borderColor:c, borderStyle:'double', borderWidth:'3px'},
    store: datastore,
    title: 'Related Content for:['+searchkey+']',   
    useArrows: true,
    closable: true,
    autoScroll:true,
    scroll:'vertical',
    listeners: {
            itemclick: function(s,r) {
            	
            	if(r.data&&r.data.startPage)
            	{
            		ccg.ui.updateSelectedContent(r.data,searchkey);
            		if(r.data.text.indexOf("Article")>-1)
            		{
            			if(r.data.expanded!=true)
            			{
            				this.expandNode(r,true);
            			}
            		}
            	}	
            	// now check if the tree is expanded
            	
            }
    }
	});
	return panel;
	};

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
    width: 500,
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
            	anchor : '100%',
            	labelAlign: 'right',
            	name: 'articleId'
            	
            },
        {
            fieldLabel: 'Title',
            anchor : '100%',
            labelAlign: 'right',
            name: 'title'
        },
		{
			xtype : 'textarea',
			fieldLabel : 'Description',
			name: 'description',
			//value: 'items: \nitem2\n\n\nitem3:\n',
			height: 150,
			labelAlign: 'right',
			anchor : '100%',
		},
        {
            fieldLabel: 'Type',
            anchor : '100%',
            labelAlign: 'right',
            name: 'type'
        },
        {
            fieldLabel: 'Author',
            anchor : '100%',
            labelAlign: 'right',
            name: 'author'
        },
        {
            fieldLabel: 'Company',
            anchor : '100%',
            labelAlign: 'right',
            name: 'company'
        },
        {
            fieldLabel: 'Accept Status',
            anchor : '100%',
            labelAlign: 'right',
            name: 'acceptStatus'
        },
        {
            fieldLabel: 'Praisal Scores',
            anchor : '100%',
            labelAlign: 'right',
            name: 'praisalscore'
        },
        {
        	xtype: 'checkboxfield',
        	fieldLabel: 'Delete this article',
        	value:0,
        	inputValue:true,
        	uncheckValue:false,
        	anchor : '100%',
        	labelAlign: 'right',
        	name:'deleteArticle'
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
        },
        beforerender: function(component, eOpts){
    		Ext.Ajax.request({
    	 		url:'rest/user/groups',
    	 		callback: function(options,success,response) {
    	 			var groups = Ext.util.JSON.decode(response.responseText);
    	 			console.log(groups);
    	 			if(groups.indexOf('admin') > -1){
    	 				Ext.getCmp("metasubmit").enable();
    	 			}
    	 		}	
    	 	});
    	 
            //console.log(btn2);
            console.log("before");
        }
    },
    buttons: [{
        text: 'Submit',
        id: 'metasubmit',
        disabled: true,
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
                      window.location.reload();
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

/*
ccg.data.patterns=[];
ccg.article.pattern.loader=function(){
	var pattern;
	Ext.Ajax.request({
		url: 'rest/config/pattern',
		method: 'GET',
		success: function(response, opts){
			pattern = Ext.decode(response.responseText);
			console.log(pattern);
			pattern = pattern.patternConfigs;
			var index;
			for(index = 0; index < pattern.length; ++index){
				ccg.data.patterns.push({
					boxLabel: pattern[index].display,
					inputValue: pattern[index].name,
					name: 'pattern'					
				});
			}
			//ccg.data.pattern = pattern;
			console.log(ccg.data.patterns);
			ccg.initUploadFilePanel();
		},
		failure: function(response, opts){
			alert("patter load error");
		}
	});
}();
*/
ccg.initUploadFilePanel = function(){
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
	        buttonText: 'Select File...',
			listeners: {
				change: function(fld, value) {
					var newValue = value.replace(/C:\\fakepath\\/g, '');
					fld.setRawValue(newValue);
					console.log(fld);
					console.log(ccg.ui.uploadfilepanel.getForm().findField("title"));
					//ccg.ui.uploadfilepanel.getForm().findField("title").setRawValue(newValue);
					var position = newValue.indexOf(".");
					var title = newValue.substring(0, position);
					this.up('form').getForm().findField('title').setRawValue(title);
				}
			}
	    },		
	    {
			xtype : 'textfield',
			fieldLabel : 'Title',
			name: 'title',
			labelAlign: 'right',
			anchor : '100%',
		},
		{
			xtype : 'textarea',
			fieldLabel : 'Description',
			name: 'description',
			//value: 'items: \nitem2\n\n\nitem3:\n',
			height: 200,
			labelAlign: 'right',
			anchor : '100%',
		},
		{
			//xtype : 'textfield',
			xtype: 'combo',
			id:'comboDocType',
			fieldLabel : 'Type',
			name: 'type',
			store: ['default', 'a','b'],
			value: 'default',
			editable: false,
			labelAlign: 'right',
			anchor : '100%',
		},
		{
		    xtype: 'component',
		    autoEl: {
		        tag: 'a',
		        href: '#',
		        html: '<p style="text-align:right">Create new type</p>',
		    },

		},
		
	/*	{
			xtype : 'fieldcontainer',
			fieldLabel : 'Category Pattern',
			defaultType : 'radiofield',
			labelAlign: 'right',
			defaults : {
				flex : 1
			},
			//layout : 'hbox',
			items : ccg.data.patterns
		},
		*/
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
        },
        
        
	    //listeners:{
        render:function(panel){
        		var link = panel.getEl().down('a');
        		link.on('click', function(e){
        			e.preventDefault();
        			Ext.MessageBox.prompt('New Type', 'Please enter new type:', messageBoxCallback);
        		})
        },
	    //}
        
        beforerender: function(component, eOpts){
            Ext.Ajax.request({
              	 url: 'rest/config/articletype',
              	 method:"GET",
              	 success: function(response, opts) {
              		 var typenames = Ext.decode(response.responseText);
              		 console.log(typenames);
              		Ext.getCmp('comboDocType').bindStore(typenames);
              	 },
              	 failure: function(response, opts) {
              		 alert("load data error!!");
              	 }
            });
    		//Ext.Ajax.request({
    		//	 url: 'rest/client/name',
    		//	 method:"GET",
    		//	 success: function(response, opts) {
    		//		 Ext.getCmp('comboDocType').bindStore(['aa', 'bb']);
    		//	 }
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
                    					ccg.ui.uploadfilepanel.getForm().findField("title").setRawValue('');
                    					Ext.MessageBox.confirm("Success", "Do you want load another one?", function(btn){
                    						if(btn === 'no'){
                    							ccg.ui.uploadfilepanel.hide();
                    							window.location.reload();
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
}
function messageBoxCallback(btn, text){
	console.log(btn);
	if(btn == 'ok'){
		console.log('clikc on ok');
		//Ext.getCmp('comboDocType').bindStore(['aa', 'bb', text]);
        Ext.Ajax.request({
         	 url: 'rest/config/savearticletype/' + text,
         	 method:"GET",
         	 success: function(response, opts) {
         		var typenames = Ext.decode(response.responseText);
         		console.log(typenames); 
         		Ext.getCmp('comboDocType').bindStore(typenames);
         		
         	 },
         	 failure: function(response, opts) {
         		 alert("save data error!!");
         	 }
       });
		
		Ext.getCmp('comboDocType').setValue(text);
		//console.log(Ext.getCmp('comboDocType').getStore().data);
		//console.log(Ext.getCmp('comboDocType').store.insert(0, {value: 'text'}));
		
	}
}
