$.fn.smartTime = function(options){
    var defaults = {
        to: "yyyy-MM-dd",
		attr: "smartTime"
    };
    var opts = $.extend(defaults, options);
    return this.each(function () {
        var $this = $(this);
        var now = new Date().getTime();
		var old = $this.attr(opts.attr);
		if (!old||old<1000){
			return;
		}
        var t = now - old*1000;
        var newTimeStr = "";
        if (t<1000*60*2){
            newTimeStr = "刚刚";
        } else if (t < 1000*60*60){
            newTimeStr = parseInt(t/(1000*60))+"分钟前";
        } else if (t < 1000*60*60*24){
            newTimeStr = parseInt(t/(1000*60*60))+"小时前";
        } else if (t < 1000*60*60*24*30){
            newTimeStr = parseInt(t/(1000*60*60*24))+"天前";
        } else {
            newTimeStr = new Date(old*1000).format(opts.to);
        }
        $this.html(newTimeStr);
    });
}
Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}