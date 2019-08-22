var dataGridHeight;
$(function(){
	initDGH();
	initTable();
});
function initDGH(){
	var winh = $(window).height();
	var ph = $('#pid').height();
	var toppp = $("#pid").offset().top;
	dataGridHeight = winh-ph-toppp-21;
}
window.onresize=function(){
	initDGH();
	$('#table').datagrid('resize',{
		height : dataGridHeight,
		width : '100%'
	});
}
function initTable(){
	$('#table').datagrid({
		striped : true,
		pagination : true,
		rownumbers : true,
		height : dataGridHeight,
		method:'POST',
		url:'/enterpriseInfo/getAllEnterprise',
		loadMsg : '数据载入中，请稍等...',
		columns:[[
		          {field: 'id',			title:'企业ID',		width:setWidth(0.1)},
		          {field: 'name',		title:'企业名称',		width:setWidth(0.2)},
		          {field: 'orgPhone',	title:'手机号',		width:setWidth(0.2)},
		          {field: 'orgEmail',	title:'邮箱',		width:setWidth(0.15)},
		          {field: 'smsSign',	title:'短信签名',		width:setWidth(0.15)},
		          {field: 'f6',			title:'操作',		width:setWidth(0.2),
		        	  formatter:function(value,row,index){
		        		  var str = '';
		        		  str += '<a style="margin-left:5px" href="#" name="btn_edit" onclick="openEdit('+index+')">编辑</a>';
		        		  //str += '<a style="margin-left:20px" href="#" name="" onclick="pswdChange('+index+')">密码修改</a>';
		        		  str += '<a style="margin-left:20px" href="#" name="btn_sign" onclick="signChange('+index+')">短信签名修改</a>';
		        		  str += '<a style="margin-left:20px" href="#" name="btn_delete" onclick="openDelete('+index+')">删除</a>';
		        		  return str;
		        	  }
		          }
		]]
	});
}
function setWidth(i){
	return i*100+'%';
}
function openDelete(index){
	var row = $('#table').datagrid('getData').rows[index];
	$.messager.confirm('提示','确认删除？',function(data1){
		if(data1){
//			$.ajax({
//			    url:"",
//			    async:false,
//			    data:{},
//			    type:"POST",
//			    success:function(data){
			    	$.messager.alert('提示','删除成功！','info',function(){
						$('#table').datagrid('reload');
					});
//			    }
//			});
		}
	});
}
function pswdChange(index){
	$('#dialogPswdChange').dialog('open');
}
function signChange(index){
	$('#dialogSignChange').dialog('open');
}
function openAdd(){
	$('#dialogAdd').dialog('open');
}
function closeDialogAdd(){
	$('#dialogAdd').dialog('close');
}
function closeDialogEdit(){
	$('#dialogEdit').dialog('close');
}
function openEdit(index){
	$('#dialogEdit').dialog('open');
}
function openReset(){
	$('#param1').textbox('setValue','');
	$('#param2').textbox('setValue','');
}
function openSearch(){
	var orgid = $('#param1').textbox('getValue');
	var orgname = $('#param2').textbox('getValue');
	var params = new Array();
	params['orgid'] = orgid;
	params['orgname'] = orgname;
	$('#table').datagrid('options').queryParams = params;
	$('#table').datagrid('reload');
}
function saveDialogAdd(){
	var name = $('#addInfo1').textbox('getValue');
	var orgPhone = $('#addInfo2').textbox('getValue');
	var orgEmail = $('#addInfo3').textbox('getValue');
	var orgAddress = $('#addInfo4').textbox('getValue');
	$.ajax({
	    url:"/enterpriseInfo/add",
	    async:false,
	    data:{'name':name,'orgPhone':orgPhone,'orgEmail':orgEmail,'orgAddress':orgAddress},
	    type:"POST",
	    success:function(data){
	    	$.messager.alert('提示','新增成功！','info',function(){
				$('#table').datagrid('reload');
			});
	    }
	});
}