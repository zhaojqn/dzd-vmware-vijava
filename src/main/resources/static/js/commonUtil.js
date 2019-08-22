var pc; 
$.parser.onComplete = function () {
	if (pc) clearTimeout(pc);
	pc = setTimeout(closes, 1000);
} 
function closes() {
	$('#loading').fadeOut('normal', function () {
		$(this).remove();
	});
}
function stringTrim(str){
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
function amendDate(jsondate) {// 修正日期(长数字变时间)-luweiguo
	if(jsondate == null){
		return;
	}
	jsondate = "" + jsondate + "";
	if (jsondate.indexOf("+") > 0) {
		jsondate = jsondate.substring(0, jsondate.indexOf("+"));
	} else if (jsondate.indexOf("-") > 0) {
		jsondate = jsondate.substring(0, jsondate.indexOf("-"));
	}
	var date = new Date(parseInt(jsondate, 10));
	var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
	var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
	return date.getFullYear() + "-" + month + "-" + currentDate + " " + hours
			+ ":" + minutes + ":" + seconds;
}