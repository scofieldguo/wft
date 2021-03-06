function checkemailformat(email){
	var e= jQuery.trim($("#"+email).val());
	if(e.length<1){
		return false;
	}else if(!/^[.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(e)){
		return false;
	}else return true;
}

function checkPassword(password){
	var e= jQuery.trim($("#"+password).val());
	var len=getStringLength(e);
	if(len<6||len>14){
		return false;
	}else return true;
}

function checkRepassword(password,repassword){
	var e1= jQuery.trim($("#"+password).val());
	var e2= jQuery.trim($("#"+repassword).val());
	if(e1==e2){
		return true;
	}else return false;
}

function checkPhoneNumber(phoneNumber){
	var pNumber=$.trim($("#"+phoneNumber).val());
	if(/^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/.test(pNumber)){
		return true;
	}else return false;
}

function checkMobile(mobile){
	var m=$.trim($("#"+mobile).val());
	if(m=="") return 0;
	if(/^13\d{9}$/.test( m ) | /^15\d{9}$/.test( m ) | /^18\d{9}$/.test( m ) | /^17\d{9}$/.test( m ) | /^14\d{9}$/.test( m ))
		return true;
	return false;
}
function checkWebsite(url){
	var website=$.trim($("#"+url).val());
	if(/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test(website)){
		return true;
	}else{
		return false;
	}
}

function checkNumber(num,min,max){
	var n=$.trim($("#"+num).val());
    if( /^[0-9]+$/.test( n ) && n <=max && n >= min)
		return true;
	else return false;
}
/**
 * 
 * 允许格式：字母、数字
 */
function checkString(id,minLen,maxLen){
	var str=$.trim($("#"+id).val());
	var len=str.length;
	if(len<minLen||len>maxLen){
		return false;
	}else if(!/[A-Za-z0-9]/.test(str)){
		return false;
	}else return true;
}
/**
 * 
 * 允许格式：字母、数字、空格和汉字
 * 汉字算两个字符
 */
function checkCnString(id,minLen,maxLen){
	var str=$.trim($("#"+id).val());
	var len=getStringLength(str);
	if(len<minLen||len>maxLen){
		return false;
	}else if(/^[a-zA-Z0-9\u4e00-\u9fa5\s\(\)]+$/.test(str)){
		return  true;
	}else return false;
}

function checkPostcode(postcode) {
	var code = $.trim($("#"+postcode).val());
	if(/^[1-9]\d{5}$/.test(code))
		return true;
	else return false;
}

function fixLength(str,maxLen){
	var len=str.lenth;
	if(len>maxLen)
		return false;
	else return true;
}

function checkEmpty(id){
	var e= jQuery.trim($("#"+id).val());
	if(e.length<1){
		return false;
	}else return true;
}
