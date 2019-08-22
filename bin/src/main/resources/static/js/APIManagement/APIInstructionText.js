var dataGridHeight;
$(function(){
	initDGH();
	initTable();
});
function initDGH(){
	var winh = $(window).height();
	var toppp = $("#table").offset().top;
	dataGridHeight = winh-toppp-1;
}
function initTable(){
	$('#table').datagrid({
		striped : true,
//		rownumbers : true,
//		height : dataGridHeight,
		method:'POST',
//		url:'/vehicle/getAllVehicle',
		loadMsg : '数据载入中，请稍等...',
		columns:[[
		          {field: 'id',			title:'序号',		width:setWidth(0.2)},
		          {field: 'name',		title:'绑定设备',		width:setWidth(0.4)},
		          {field: 'url',		title:'操作',		width:setWidth(0.4),
		        	  formatter:function(value,row,index){
		        		  var str = '<a href="'+value+'" name="" onclick="">查看</a>';
		        		  return str;
		        	  }
		          }
		]],
		data: [
			{id:'1',	url:'/APIInstructionText/detail1',		name:'行业卡短信下发API'},
			{id:'2', 	url:'/APIInstructionText/detail2',		name:'物联网卡短信下发API'},
			{id:'3', 	url:'/detail3',		name:'行业卡短信下发历史记录查询API'},
			{id:'4', 	url:'/detail4',		name:'物联网卡短信下发历史记录查询API'},
			{id:'5', 	url:'/detail5',		name:'行业卡短信上行接收API'},
			{id:'6', 	url:'/detail6',		name:'物联网卡短信上行接收API'},
			{id:'7',	url:'/detail7',		name:'行业卡短信上行接收历史记录查询API'},
			{id:'8',	url:'/detail8',		name:'物联网卡短信上行历史记录查询API'},
			{id:'9', 	url:'/detail9',		name:'行业卡至物联网卡转发API'},
			{id:'10', 	url:'/detail10',	name:'物联网卡至行业卡转发API'},
			{id:'11', 	url:'/detail11',	name:'行业卡至物联网卡转发历史记录查询API'},
			{id:'12', 	url:'/detail12',	name:'物联网卡至行业卡转发历史记录查询API'},
			{id:'13', 	url:'/detail13',	name:'短信余量查询API'}
		]
	});
}
function setWidth(i){
	return i*100+'%';
}