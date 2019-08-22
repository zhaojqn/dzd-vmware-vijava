var isPt = false;
var isSys = false;
var isRole = false;
var sysadmin = "ROLE_SYS_ADMIN";
var ptadmin = "ROLE_OTHER";
var role = "ROLE_USER";
$(function(){
	isPtf();//------test
})

function timeJudge(){
	$("#sTime").datetimebox({
		onSelect : function(beginDate) {
			$('#eTime').datetimebox('calendar').calendar({
				validator : function(date) {
					return beginDate <= date;
				}
			});
		}
	});
	$("#eTime").datetimebox({
		onSelect : function(beginDate) {
			$('#sTime').datetimebox('calendar').calendar({
				validator : function(date) {
					return beginDate >= date;
				}
			});
		}
	});
}
//时间戳转换日期格式
function getLocalTime(nS) {
//	var now = new Date();
	if(nS != null && nS != ""){
		now = new Date(nS);
		var  year=now.getFullYear();     
		var  month=now.getMonth()+1;     
		month = getFormatDate(month);
		var  date=now.getDate();     
		date = getFormatDate(date);
		var  hour=now.getHours();    
		hour = getFormatDate(hour);
		var  minute=now.getMinutes();
		minute = getFormatDate(minute);
		var  second=now.getSeconds();
		second = getFormatDate(second);
		return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
	}else return "";
	
}

function getFormatDate(num){
	if(num < 10 && num.toString().length<2){
		return '0'+num;
	}else {
		return num;
	}
}
//获取用户所属角色，判断是否平台管理员
function isPtf(){
	$.ajax({
	    url:"/sp/getRoleByUser",
	    async:false,
	    type:"POST",
	    dataType : 'json',
	    success:function(data){
	    	for(var i=0;i<data.length;i++){
	    		if(data[i].CODE == ptadmin){
	    			isPt = true;
//	    			break;
	    		}
	    		if(data[i].CODE == sysadmin){
	    			isSys = true;
//	    			break;
	    		}
	    		if(data[i].CODE == role){
	    			isRole = true;
//	    			break;
	    		}
	    	}
	    	return isPt;
	    },
	    error:function(){
	    	$.messager.alert('提示','获取用户角色信息失败！','info',function(){
			});
	    }
	})
}

function checkIp(ip)    
{ 
   var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/;//正则表达式   
   if(re.test(ip))   
   {   
       if( RegExp.$1<256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256) 
       return true;   
   }   
   return false;    
}

function checkPhonenum(string) {
	/*var pattern = /^1[34578]\d{9}$/;//  /^0?1[3|4|5|8][0-9]\d{8}$/
	if (pattern.test(string)) {
		return true;
	}
	return false;*/
	//var patrn=/^(\d\,)?\d$/;    
	var patrn= /^\d+(\,\d+)+$/;
	if(!isNaN(string))return true;
	if (patrn.exec(string)) return true;  
	return false;  
};

function isNumOrChar(value){//判断是否是数字或字母
	var Regx = /^[A-Za-z0-9]*$/;
	if (Regx.test(value))
	{
	return true;
	}
	else
	{
	return false;
	} 
}
/*手机号有如下规则:
	(1)必须全为数字;
	(2)必须是11位.(有人说还有10位的手机号,这里先不考虑);
	(3)必须以1开头(有人见过以2开头的手机号吗?)
	(4)第2位是34578中的一个.*/

//验证输入框输入字符的长度，长度大于11则给出提示并重置输入框
function validateLength(textArea){
	var trueLength=textArea.value.length;  
	 if(trueLength>=11){
		 parent.layer.msg("输入必须少于11个字符",{icon:2,time:1000});
		 $("#"+textArea.id+"").val("");
	 }			 	      
}
//验证端口0-65535
function isPort(str)  
{  
    var parten=/^(\d)+$/g;  
    if(!isNaN(str)&&parten.test(str)&&parseInt(str)<=65535&&parseInt(str)>=0){  
        return true;  
     }else{  
        return false;  
     }   
}
//http地址验证
function IsURL(str_url){   
//	var strRegex = "^((https|http|ftp|rtsp|mms)?://)" 
//	  + "?(([0-9A-Za-z_!~*'().&=+$%-]+: )?[0-9A-Za-z_!~*'().&=+$%-]+@)?" //ftp的user@ 
//	        + "(([0-9].)[0-9]" // IP形式的URL- 199.194.52.184 
//	        + "|" // 允许IP和DOMAIN（域名）
//	        + "([0-9A-Za-z_!~*'()-]+.)*" // 域名-  
//	        + "([0-9A-Za-z][0-9A-Za-z-])?[0-9A-Za-z]." // 二级域名 
//	        + "[a-zA-Z])" // first level domain- .com or .museum 
//	        + "(:[0-9])?"; // 端口- :80 
//	        + "((/?)|" // a slash isn't required if there is no file name 
//	        +"[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/";
//	var strRegex=/^(((https|http|ftp|rtsp|mms)?:\/\/)?)+[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
	  var strRegex = "^((https|http|ftp|rtsp|mms)?://)"    
		    + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@    
		    + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184 
		    + "|" // 允许IP和DOMAIN（域名）   
		    + "([0-9a-zA-Z_!~*'()-]+\.)*" // 域名- www.    
		    + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名    
		    + "[a-z]{2,6})" // first level domain- .com or .museum    
		    + "(:[0-9]{1,4})?" // 端口- :80    
		    + "((/?)|" // a slash isn't required if there is no file name    
		    + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";  
	  var re=new RegExp(strRegex);
	  return (re.test(str_url));  
	} 


//校验敏感词
function checkSensitiveWord(msg){
	var sensitve=null;
	$.ajax({
	    url:"/smsRelay/checkSensitiveWord",
	    async:false,
	    data:{'msg':msg},
	    type:"POST",
	    success:function(data){
	    	sensitve=data;	    	
	    },
	    error:function(){
	    }
	});		
	return sensitve;
}
//新增页面设置企业信息
function setOrgInfo(){
	$.ajax({
	    url:"/smsTemplate/getOrgInfoByUser",
	    async:false,
	    type:"POST",
	    dataType : 'json',
	    success:function(data){
	    	$('#orgNameAdd').textbox('setValue',data[0].name);
	    	$('#orgIdAdd').textbox('setValue',data[0].id);
	    },
	    error:function(){
	    	$.messager.alert('提示','获取用户所属企业信息失败！','info',function(){
			});
	    }
	})
}

function getRoleByUser(){
	var role=null;
	$.ajax({
		url : "/smsTemplate/getRoleByUser",
		type : 'post',
		async : false,
		success : function(data) {
			role = data;
		}
	});
	return role;
}