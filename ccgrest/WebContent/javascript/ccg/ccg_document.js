
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
        text: 'Related Document:',        
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
       	     }
       	 });
       	 	}
       	 
        }
    },
});

ccg.ui.relateddoclist =Ext.create('Ext.tree.Panel', {
    store: ccg.data.relateddocstore,
    height: 240,
    width: 300,
    title: 'Related Documents:',
    useArrows: true,
    dockedItems: [{
        xtype: 'toolbar',
        items: []
    }]
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
    height: 360,
    width: 300,
    bodyPadding: 10,
    defaultType: 'textfield',
    items: [
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
            name: 'title'
        },
        {
            fieldLabel: 'Company',
            name: 'type'
        },
        {
            fieldLabel: 'Accept Status',
            name: 'title'
        },
        {
            fieldLabel: 'Praisal Scores',
            name: 'type'
        },
        {
            xtype: 'datefield',
            fieldLabel: 'CreatedTS',
            name: 'createTS'
        }
    ]
})