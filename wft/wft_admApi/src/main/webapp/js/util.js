String.prototype.trim = function() {
	return this.replace(/(\s*$)|(^\s*)/g, '');
};

function next() {
	var f = document.getElementById("pageform");
	var pi = parseInt(f.pageId.value);
	if (pi < f.pageNum.value) {
		f.pageId.value = pi + 1;
		f.submit();
	}
}

function prev() {
	var f = document.getElementById("pageform");
	var pi = parseInt(f.pageId.value);
	if (pi > 1) {
		f.pageId.value = pi - 1;
		f.submit();
	}
}

function jump() {
	var f = document.getElementById("pageform");
	f.submit();
}
//搜索数据时重新设置为1，页面从第一行开始20130517
function resetpageid(){
	  $("#pageId").val(1);
	  return true;
	}
//搜索数据时重新设置为1，页面从第一行开始20130517
function onSubmitPageCheck(){
	var f = document.getElementById("pageform");
	f.pageId.value = 1;
	return true;
}

function _submitPaged(pageid) {
	var f = document.getElementById("pageform");
	f.pageId.value = pageid;
	f.submit();
}

function showRecommendOption(action, pid, chid, sid, srctype, srcid) {
	var url = action + "&pid=" + pid + "&chid=" + chid + "&sid=" + sid
			+ "&srctype=" + srctype + "&srcid=" + srcid;
	window
			.open(
					url,
					"recommendView",
					"height=120,width=400,resizable=yes,status=no,toolbar=no,menubar=no,location=no");
}

/**
 *
 */
function initDateField(fdate, tdate) {
	var today = new Date();
	var separator = "-";

	thismonth = today.getMonth() + 1;
	thisyear = today.getYear();
	thisday = today.getDate();

	if (thismonth < 10)
		thismonth = "0" + thismonth;
	if (thisday < 10)
		thisday = "0" + thisday;

	var frtime = document.getElementById(fdate);
	var totime = document.getElementById(tdate);

	if (frtime.value == "") {
		frtime.value = thisyear + separator + thismonth + separator + thisday;// +'
																				// 00:00';
	}

	if (totime.value == "") {
		totime.value = thisyear + separator + thismonth + separator + thisday;// +'
																				// 23:59';
	}
}

function OnDelete() {
	return window.confirm('确实要删除吗');
}

// ��ȡһ�鵥ѡ��ť�е�ѡ��ֵ
function getRadioCheckValue(radioName) {
	var el = document.getElementsByTagName("input");
	var len = el.length;
	for (var i = 0; i < len; i++) {
		if ((el[i].type == "radio") && (el[i].name == radioName)) {
			if (el[i].checked == true)
				return el[i].value;
		}
	}
	return "";
}

// Following script(s)

/**
 * Required parameter/variable: blockCount
 */

var bvfPfx = 'bvf';
var bplyPfx = 'bply';

var bpvf = new Object();
var bply = new Object();

function enableBlock(id) {
	var bvfId = bvfPfx + id;
	var visible = document.getElementById(bvfId).checked;

	bpvf[id] = visible ? 1 : 0;
}

function moveUp(bplyIdx) {
	swapPosition(bplyIdx, bplyIdx - 1);
}
function moveDown(bplyIdx) {
	swapPosition(bplyIdx, bplyIdx + 1);
}

function submitSort() {
	var f = document.forms[0];

	f.sortedIDs.value = getBPOrderStr();
	f.visibleStr.value = getBPVisibleStr();
	f.submit();
}

function swapPosition(from, to) {
	var frId = bplyPfx + from;
	var toId = bplyPfx + to;

	// swap data
	var tmp = bply[frId];
	bply[frId] = bply[toId];
	bply[toId] = tmp;

	// swap view
	var frObj = document.getElementById(frId);
	var toObj = document.getElementById(toId);

	var tmp2 = frObj.innerHTML;

	frObj.innerHTML = toObj.innerHTML;
	toObj.innerHTML = tmp2;
}

function getBPVisibleStr() {
	var idx = 1;
	var str = '';
	for (id in bpvf) {
		if (idx++ > 1)
			str = str + ';';
		str = str + id + ':' + bpvf[id];
	}

	return str;
}

function getBPOrderStr() {
	var str = '';
	for (i = 1; i <= blockCount; i++) {
		if (i > 1)
			str = str + ',';
		var bplyId = bplyPfx + i;

		str = str + bply[bplyId]
	}
	return str;
}

/**
 * ����ؼ���ʼ��
 */
function initDateField2() {
	var today = new Date();
	var separator = "-";

	thismonth = today.getMonth() + 1;
	thisyear = today.getYear();
	thisday = today.getDate();

	if (thismonth < 10)
		thismonth = "0" + thismonth;
	if (thisday < 10)
		thisday = "0" + thisday;

	var frtime = document.getElementById('frdate');
	var totime = document.getElementById('todate');

	if (frtime.value == "") {
		frtime.value = thisyear + separator + thismonth + separator + thisday
				+ ' 00:00';
	}

	if (totime.value == "") {
		totime.value = thisyear + separator + thismonth + separator + thisday
				+ ' 23:59';
	}
}

function mark(spanTag) {
	window.alert(spanTag.id);

	spanTag.style = 'FONT-WEIGHT: bold;';

	return true;
}

function clearContent(components) {
	var ids = components.split(",");
	for (var i = 0; i < ids.length; i++) {
		document.getElementById(ids[i]).value = "";
	}
}

/*
 * �ж�һ����Ͽ��Ƿ���ֵѡ��
 */
function isCheckBoxEmpty(checkBoxName) {
	var el = document.getElementsByTagName("input");
	var len = el.length;
	for (var i = 0; i < len; i++) {
		if ((el[i].type == "checkbox") && (el[i].name == checkBoxName)) {
			if (el[i].checked == true)
				return false;
		}
	}
	return true;
}

/*
 * ��Ͽ�-ȫѡ
 */
function selectAll(checkBoxName) {
	var el = document.getElementsByTagName("input");
	var len = el.length;
	for (var i = 0; i < len; i++) {
		if ((el[i].type == "checkbox") && (el[i].name == checkBoxName)) {
			if (el[i].checked != true)
				el[i].checked = true;
		}
	}
	return true;
}

/*
 * ��Ͽ�-��ѡ
 */
function invertSelect(checkBoxName) {
	var el = document.getElementsByTagName("input");
	var len = el.length;
	for (var i = 0; i < len; i++) {
		if ((el[i].type == "checkbox") && (el[i].name == checkBoxName)) {
			el[i].checked = el[i].checked == true ? false : true;
		}
	}
	return true;
}

/*
 *
 */
function canselSelect(checkBoxName) {
	var el = document.getElementsByTagName("input");
	var len = el.length;
	for (var i = 0; i < len; i++) {
		if ((el[i].type == "checkbox") && (el[i].name == checkBoxName)) {
			if (el[i].checked == true)
				el[i].checked = false;
		}
	}
	return true;
}
/*
 */
function countWords(wordStr) {
	var words = wordStr.split("");
	var en = /^[a-zA-z0-9_()@,]$/;
	var cn = /^[\u4e00-\u9fa5]$/;
	var num = 0;
	for (var i = 0; i < words.length; i++) {
		if (en.test(words[i]))
			num++;
		else if (cn.test(words[i]))
			num = num + 2;

	}
}

function showTbody(id) {
	document.getElementById(id).style.display = "block";
}
function hideTbody(id) {
	document.getElementById(id).style.display = "none";
}

function SetCwinHeight(cwin) {
	
	if (document.getElementById) {
		if (cwin && !window.opera) {
			
			if (cwin.contentDocument && cwin.contentDocument.body.offsetHeight)
			{
				
			cwin.height = cwin.contentDocument.body.offsetHeight;
			
			}
			else if (cwin.Document && cwin.Document.body.scrollHeight)
			{
			cwin.height = cwin.Document.body.scrollHeight;
			}
		}
	}
}