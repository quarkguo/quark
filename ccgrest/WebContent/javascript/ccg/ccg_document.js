
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
    dockedItems: [{
        xtype: 'toolbar',
        items: []
    }],
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
        }
    }]
})