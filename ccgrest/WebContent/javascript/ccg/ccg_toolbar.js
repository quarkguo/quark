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
        },
        folderSort: true,
        sorters: [{
            property: 'text',
            direction: 'ASC'
        }]
    });
 ccg.ui.doclist =Ext.create('Ext.tree.Panel', {
     store: ccg.data.docliststore,
     height: 300,
     width: 250,
     title: 'Document List',
     useArrows: true,

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
		  style: 'background-image: -webkit-linear-gradient(top,#66b3ff, #1a8cff)',
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
    