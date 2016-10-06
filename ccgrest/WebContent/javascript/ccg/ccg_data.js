// ccg_data.js
// created by ying guo 06/2016  CCG data model and store
var ccg={};
ccg.clientname={};
Ext.Ajax.request({
	 url: 'rest/client/name',
	 method:"GET",
	 success: function(response, opts) {
		ccg.clientname=response.responseText;
		Ext.util.Cookies.set('clientname', ccg.clientname);
	 }
})();;

