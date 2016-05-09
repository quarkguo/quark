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
 ccg.ui.doclist =Ext.create('Ext.tree.Panel', {
     store: ccg.data.docliststore,
     height: 300,
     width: 250,
     title: 'Document List',
     useArrows: true,
     listeners: {
         itemclick: function(s,r) {           
        	 ccg.ui.doccategory.getRootNode().removeAll();
                 var arcID=r.data.articleID;
                 var urlstr="rest/article/"+arcID+"/category";
                 console.log(urlstr);         
               //  ccg.data.doccategorystore.removeAll();
                 ccg.data.doccategorystore.load({url:urlstr});
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
		        	 cls:'ccg-header-title',
		        	 html:document.title,
		        	 flex:1
		         },
		    {
                    text: 'Content Ingestion',
                    iconCls: 'edit',                    
                    displayText: 'Content Ingestion'                  	 
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
                text: 'Setting',
                iconCls: 'file',                    
                displayText: 'Content Management'                  	 
		    }
		  ]
	});
    