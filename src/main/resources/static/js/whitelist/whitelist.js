var dataGridHeight;
var role;
$(function(){
	role=getRoleByUser();
	if(role=="ROLE_ADMIN"||role=="ROLE_OTHER"){//超级管理员、平台管理员没有新增的权限,所以隐藏这个按钮
		$('#addButton').hide();                //普通用户看到当前用户所在企业的白名单，平台管理员要看到当前用户所在企业以及子企业的白名单信息，超管要看到所有白名单信息
	}else if(role=="ROLE_USER"){
		$('#addButton').show();
	}
	initDGH();
	initTable();
	openReset();
	timeJudge();//查询条件中时间选择判断
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
	if ($.fn.datagrid){
	    $.fn.datagrid.defaults.pageSize = 15;
	    $.fn.datagrid.defaults.pageList = [15,30,45];
	}
	if ($.fn.pagination){
	    $.fn.pagination.defaults.beforePageText = '第';
	    $.fn.pagination.defaults.afterPageText = '页,共{pages}页';
	    $.fn.pagination.defaults.displayMsg = '当前显示{from}到{to}条,共{total}条记录';
	}
	$('#table').datagrid({
		striped : true,
		pagination : true,
		rownumbers : true,
		height : dataGridHeight,
		method:'POST',
		url:'/whitelist/getAllWhitelist',
		loadMsg : '数据载入中，请稍等...',
		columns:[[
			 {field: 'ck',			checkbox:true},
		          {field: 'orgId',			title:'企业ID',		width:setWidth(0.1),
		        	  formatter:function(value,row,index){
		        		  if(value==-1)return "";
		        		  else return value;
		        	  }
		          },
		          {field: 'orgName',		title:'企业名称',		width:setWidth(0.1)},
		          {field: 'ip',	title:'白名单IP',		width:setWidth(0.1)},
		          {field: 'note',	title:'备注',		width:setWidth(0.1)},
		          {field: 'useFlag',	title:'状态',		width:setWidth(0.05),
		        	  formatter:function(value,row,index){
		        		  if(value==0)return "启用";
		        		  else if(value==1)return "停用";
		        	  }
		          },
		          {field: 'source',	title:'来源',		width:setWidth(0.05),
		        	  formatter:function(value,row,index){
		        		  if(value==0)return "手动录入";
		        		  else if(value==1)return "短信平台";
		        	  }
		          },
		          {field: 'creater',	title:'创建人',		width:setWidth(0.08)},
		          {field: 'createTime',	title:'创建时间',		width:setWidth(0.1) ,
		        	  formatter:function(value,row,index){
		        		  return getLocalTime(value);
		        	  }
		          },
		          {field: 'updater',	title:'更新人',		width:setWidth(0.09)},
		          {field: 'updateTime',	title:'更新时间',		width:setWidth(0.1) ,
		        	  formatter:function(value,row,index){
		        		  return getLocalTime(value);
		        	  }
		          },
		          {field: 'f6',			title:'操作',		width:setWidth(0.1),
		        	  formatter:function(value,row,index){
		        		  var str = '';
		        		  if(row.useFlag==0){
		        			  str += '<a style="margin-left:10px" href="#" name="btn_nouse" onclick="openNouse('+row.id+','+index+')">停用</a>';
			        	  }else if(row.useFlag==1){
		        			  str += '<a style="margin-left:10px" href="#" name="btn_nouse" onclick="openUse('+row.id+','+index+')">启用</a>';
			        	  }
		        		  str += '<a style="margin-left:10px" href="#" name="btn_edit" onclick="openEdit('+index+')">编辑</a>';
		        		  str += '<a style="margin-left:10px" href="#" name="btn_delete" onclick="openDelete('+row.id+','+row.useFlag+','+index+')">删除</a>';
		        		  return str;
		        	  }
		          }
		]],
		onLoadSuccess: function(data){
	    	$(this).datagrid('doCellTip', { 'max-width': '600px', 'delay': 500 });// easyUI 鼠标悬停显示内容			
		}
	});
}
function setWidth(i){
	return i*100+'%';
}
function openDelete(value,useFlag,index){
	if(useFlag==0){
		$.messager.alert('提示','启用状态下的白名单不允许删除，请先停用！','info',function(){			
		});
		return;
	}
	$.messager.confirm('提示','确认删除？',function(data){
		if(data){
			$.ajax({
			    url:"/whitelist/delWhitelist",
			    async:false,
			    data:{'id':value},
			    type:"POST",
			    success:function(data){
			    	$.messager.alert('提示','删除成功！','info',function(){
						$('#table').datagrid('reload');
					});
			    }
			});
		}		
	});
}
function openNouse(value,index){
	$.messager.confirm('提示','确认停用？',function(data){
		if(data){
			$.ajax({
			    url:"/whitelist/nouseWhitelist",
			    async:false,
			    data:{'id':value},
			    type:"POST",
			    success:function(data){
			    	$.messager.alert('提示','停用成功！','info',function(){
						$('#table').datagrid('reload');
					});
			    }
			});
		}		
	});
}
function openUse(value,index){
	$.messager.confirm('提示','确认启用？',function(data){
		if(data){
			$.ajax({
			    url:"/whitelist/useWhitelist",
			    async:false,
			    data:{'id':value},
			    type:"POST",
			    success:function(data){
			    	$.messager.alert('提示','启用成功！','info',function(){
						$('#table').datagrid('reload');
					});
			    }
			});
		}		
	});
}
function clearAdd(){
	$('#addInfo1').textbox('setValue','');
	$('#addInfo2').combobox('setValue','');
	$('#addInfo3').textbox('setValue','');
}
function openAdd(){
	clearAdd();
	setOrgInfo();
	$('#addInfo2').combobox('setValue',0);
	$('#dialogAdd').dialog('open');
}
function closeDialogAdd(){
	$('#dialogAdd').dialog('close');
}
function closeDialogEdit(){
	$('#dialogEdit').dialog('close');
}
function clearEdit(){
	$('#editInfo1').textbox('setValue','');
	$('#editInfo2').textbox('setValue','');
	$('#editInfoId').val('');
}
function openEdit(index){
	clearEdit();
	var row = $('#table').datagrid('getData').rows[index];
	$('#orgIdEdit').textbox('setValue',row.orgId);
	$('#editInfo1Now').textbox('setValue',row.ip);//作为名称是否重复判断时的标准，如果编辑时名称跟当前名称相同，不做重复判断
	$('#editInfo1').textbox('setValue',row.ip);
	$('#editInfo2').textbox('setValue',row.note);
	$('#editInfoId').val(row.id);
	$('#dialogEdit').dialog('open');
}
function openReset(){
	/*$('#param1').textbox('setValue','');
	$('#param2').textbox('setValue','');*/
	$('#param3').textbox('setValue','');
	$('#param4').combobox('setValue','');
	$('#sTime').datetimebox('setValue','');
	$('#eTime').datetimebox('setValue','');
}
function openSearch(){
	var params = new Array();
	/*params['orgId'] = $('#param1').textbox('getValue');
	params['orgName'] = $('#param2').textbox('getValue');*/
	params['ip'] = $('#param3').textbox('getValue');
	params['useFlag'] = $('#param4').combobox('getValue');
	params['sTime'] = $('#sTime').datetimebox('getValue');
	params['eTime'] = $('#eTime').datetimebox('getValue');
	$('#table').datagrid('options').queryParams = params;
	$('#table').datagrid('reload');
}
function saveDialogAdd(){
	var orgId = $('#orgIdAdd').textbox('getValue');
	var ip = $('#addInfo1').textbox('getValue');
	var useFlag = $('#addInfo2').combobox('getValue');
	var note = $('#addInfo3').textbox('getValue');
	if(note.length>500){
		$.messager.alert('提示','请输入少于500字符的备注！','info',function(){			
		});
		return;
	}
	if(ip==null||ip==""){
		$.messager.alert('提示','请输入白名单IP！','info',function(){			
		});
	}else{
		if(!checkIp(ip)){
			$.messager.alert('提示','IP输入格式有误！','info',function(){			
			});
		}else{
			if(checkRepeatIp(ip,orgId)){
				$.messager.alert('提示','同一个企业下白名单ip不能重复，请重新输入！','info',function(){			
				});
				return;
			}
			$.ajax({
			    url:"/whitelist/addWhitelist",
			    async:false,
			    data:{'ip':ip,'useFlag':useFlag,'note':note},
			    type:"POST",
			    success:function(data){
			    	closeDialogAdd();
			    	$.messager.alert('提示','保存成功！','info',function(){
						$('#table').datagrid('reload');
					});
			    },
			    error:function(){
			    	closeDialogAdd();
			    	$.messager.alert('提示','保存失败！','info',function(){
						$('#table').datagrid('reload');
					});
			    }
			});
		}
	}	
}

function saveDialogEdit(){	
	var orgId = $('#orgIdEdit').textbox('getValue');
	var ipNow = $('#editInfo1Now').textbox('getValue');
	var ip = $('#editInfo1').textbox('getValue');
	var note = $('#editInfo2').textbox('getValue');
	var id=$('#editInfoId').val();
	if(note.length>500){
		$.messager.alert('提示','请输入少于500字符的备注！','info',function(){			
		});
		return;
	}
	if(ip==null||ip==""){
		$.messager.alert('提示','请输入白名单IP！','info',function(){			
		});
	}else{
		if(!checkIp(ip)){
			$.messager.alert('提示','IP输入格式有误！','info',function(){			
			});
		}else{
			if(ip==ipNow){			
			}else if(checkRepeatIp(ip,orgId)){
				$.messager.alert('提示','同一个企业下白名单ip不能重复，请重新输入！','info',function(){			
				});
				return;
			}
			$.ajax({
			    url:"/whitelist/editWhitelist",
			    async:false,
			    data:{'ip':ip,'note':note,'id':id},
			    type:"POST",
			    success:function(data){
			    	closeDialogEdit();
			    	$.messager.alert('提示','修改成功！','info',function(){
						$('#table').datagrid('reload');
					});
			    },
			    error:function(){
			    	closeDialogEdit();
			    	$.messager.alert('提示','修改失败！','info',function(){
						$('#table').datagrid('reload');
					});
			    }
			});
		}
	}
	
}

function checkRepeatIp(ip,orgId){//验证同一个企业下白名单ip是否重复
	var repeatFlag;
	$.ajax({
	    url:"/whitelist/checkRepeatIp",
	    async:false,
	    data:{'ip':ip,'orgId':orgId},
	    type:"POST",
	    success:function(data){
	    	repeatFlag=data;	    	    
	    },
	    error:function(){	    	
	    }
	});	
	return repeatFlag;	
}