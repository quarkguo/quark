
ccg.data.doccategorystore = Ext.create('Ext.data.TreeStore', {
        proxy: {
            type: 'ajax',
            url: 'json/doc1_category.json'
        },
        root: {
            text: 'Document Category:',
            id: 'src',
            expanded: true
        },
        folderSort: true,
        sorters: [{
            property: 'text',
            direction: 'ASC'
        }]
    });

ccg.ui.doccategory =Ext.create('Ext.tree.Panel', {
    store: ccg.data.doccategorystore,
    height: 500,
    width: 300,
    title: 'Document Cateogry',
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
		       	ccg.ui.doccategory
		]
	}
	]
});
