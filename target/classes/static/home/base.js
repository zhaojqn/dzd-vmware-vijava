// JavaScript Document


$(function(){
	// IE6背景透明
    if (isIE6()) {
        DD_belatedPNG.fix(".name_poto .tips , .nav .nav_my .nav_a , .nav .nav_my .nav_a span , background");
		DD_belatedPNG.fix(".smsc_port .pb_icon01 , .smsc_port .pb_icon02 , .try_btn , background");
		//IE6加载脚本 dd-png.js
        var _doc = document.getElementsByTagName('head')[0];
    	var js = document.createElement('script');
    	js.setAttribute('type', 'text/javascript');           
        js.setAttribute('src', '${pageContext.request.contextPath}/front/js/dd-png.js');           
        _doc.appendChild(js);
	}
	
	//判断ie6
	function isIE6(){
		return $.browser.msie && ($.browser.version == "6.0") && !$.support.style;
	}
})

