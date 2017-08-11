var queryUrl = 'card_list.do';
var modifyUrl = 'card_modify.do';
var queryData = {};
function formatDate(cellvalue){
	var date = new Date(cellvalue).format("yyyy-MM-dd hh:mm:ss");
	return date;
};
//时间转换
function timeFormatter(cellvalue){
	var hour = parseInt(cellvalue /3600);
	var min = parseInt(cellvalue %3600/60);
	return hour+"小时"+min+"分";
};
function modify(oper,cardNo,cardId,channelId){
	var operKey;
	if (oper=="stop"){
		operKey = "停用";
	}else{
		operKey = "启用";
	}
	if (confirm("确定"+operKey+"该卡["+cardNo+"]?")){
		jQuery.ajax({  
                 type: 'POST',  
                 url:modifyUrl,   
                 data: {oper:oper, id:cardId, channel:channelId},  
                 success:function(data){      
					jQuery("#cardtabel").jqGrid('setGridParam',{postData:queryData}).trigger("reloadGrid");
                }             
        });
	}
};
var columnObjects = [
             		{index:'id',name: 'id', sortable: false, desc: '卡号ID'},
             		{index:'no',name: 'no', sortable: false, desc: '卡号'},
             		{index:'channel',name: 'channel', sortable: false, desc: '渠道'},
             		{index:'opid',name: 'opid', sortable: false, desc: '运营商'},
             		{index:'prvid',name: 'prvid', sortable: false, desc: '省份'},
             		{index:'ctype',name: 'ctype', sortable: false, desc: '类型'},
             		{index:'ucount',name: 'ucount', sortable: false, desc: '分配次数'},
             		{index:'vetime',name: 'vetime', sortable: false, desc: '截止日期',formatter: this.formatDate},
             		{index:'bvalue',name: 'bvalue', sortable: false, desc: '时长',formatter: this.timeFormatter},
             		{index:'tvalue',name: 'tvalue', sortable: false, desc: '剩余时长',formatter: this.timeFormatter},
             		{index:'intime',name: 'intime', sortable: false, desc: '调入时间',formatter: this.formatDate},
             		{index:'utime',name: 'utime', sortable: false, desc: '最后使用时间',formatter: this.formatDate},
             		{index:'ustatus',name: 'ustatus', sortable: false, desc: '使用状态'},
             		{index:'cstatus',name: 'cstatus', sortable: false, desc: '卡片状态'},
             		{index:'cstatus',name: 'cstatus', sortable: false, desc: '操作'},
             ];
var columnNames= new Array();
$.each(columnObjects,function(index,value){
	columnNames[index] = value.desc;
});
var channel;
$.extend(
	{
		tableInit: function(provinceMaps, operatorMaps, channelMaps){
			var grid = $("#cardtabel").jqGrid({
				url : queryUrl,
				datatype : "json",
				colNames : columnNames,
				colModel : columnObjects,
				rowNum : 20,
				emptyrecords : "无记录",
				gridview : true,
				rowList : [ 10, 20, 30 ],
				pager : '#pjmap1',
				cellsubmit : 'clientArray',
				sortname : 'id',
				viewrecords : true,
				sortorder : "asc",
				caption : "渠道卡池列表",
				autowidth : true,
				jsonReader : {
					repeatitems : false,
					id : 0
				},
				height : '100%',
				gridComplete: function(){
					var ids = grid.jqGrid('getDataIDs');
					for(var i=0;i < ids.length;i++){
						var rowId = ids[i];
						var rowData = grid.jqGrid('getRowData',rowId);
						var resultObject = {};
						resultObject.prvid = provinceMaps[rowData.prvid];
						resultObject.opid = operatorMaps[rowData.opid];
						resultObject.channel = channelMaps[channel];
						var ctype = rowData.ctype;
						if (ctype == 1){
							resultObject.ctype = "包月卡";
						}else if (ctype == 2){
							resultObject.ctype = "不可跨月时长卡";
						}else if (ctype == 3){
							resultObject.ctype = "可跨月时长卡";
						}else if (ctype == 4){
							resultObject.ctype = "电子卡";
						}else if (ctype == 5){
							resultObject.ctype = "账期卡";
						}else{
							resultObject.ctype = ctype;
						}
						var ustatus = rowData.ustatus;
						if (ustatus == 0){
							resultObject.ustatus = "正常";
						}else{
							resultObject.ustatus = "异常";
						}
						
						var cstatus = rowData.cstatus;
						if(cstatus == 2){
							resultObject.act = "<input style='height:22px;' type='button' value='停用' onclick=\"modify('stop','"+rowData.no+"','"+rowData.id+"','"+channel+"')\" />";
							resultObject.cstatus = "可用";
						}else if(cstatus == 3){
							resultObject.act = "<input style='height:22px;' type='button' value='停用' onclick=\"modify('stop','"+rowData.no+"','"+rowData.id+"','"+channel+"')\" />";
							resultObject.cstatus = "占用";
						}else if(cstatus == 4){
							resultObject.act = "<input style='height:22px;' type='button' value='启用' onclick=\"modify('open','"+rowData.no+"','"+rowData.id+"','"+channel+"')\" />";
							resultObject.cstatus = "<font color=\"red\">停用</font>";
						}
						grid.jqGrid('setRowData',ids[i],resultObject);
					}
				},
			});
			grid.jqGrid('navGrid', '#pjmap1', {
				add : false,
				edit : false,
				del : false,
				search: false
			}, {}, {}, {}, {});
			grid.jqGrid('navButtonAdd', '#pjmap1', {
				caption : "列选择",
				onClickButton : function() {
					grid.jqGrid('columnChooser');
				}
			});
			grid.jqGrid('navButtonAdd', '#pjmap1', {
				caption : "导出本页",
				buttonicon: "ui-icon-disk",
				title: "导出到Excel文件",
				onClickButton : function() {
					$("#Fall").val(0);
					$("#Fpage").val(grid.jqGrid('getGridParam', 'page'));
					$("#Frows").val(grid.jqGrid('getGridParam', 'rowNum'));
					$("#Fchannel").val(queryData.channel);
					$("#Fno").val(queryData.no);
					$("#Fopid").val(queryData.opid);
					$("#Fcstatus").val(queryData.cstatus);
					$("#FbegVetime").val(queryData.begVetime);
					$("#FendVetime").val(queryData.endVetime);
					var form = $("#pageform");
					form.submit();
				}
			});
			grid.jqGrid('navButtonAdd', '#pjmap1', {
				caption : "导出全部",
				buttonicon: "ui-icon-disk",
				title: "导出到Excel文件",
				onClickButton : function() {
					$("#Fall").val(1);
					$("#Fpage").val(grid.jqGrid('getGridParam', 'page'));
					$("#Frows").val(grid.jqGrid('getGridParam', 'rowNum'));
					$("#Fchannel").val(queryData.channel);
					$("#Fno").val(queryData.no);
					$("#Fopid").val(queryData.opid);
					$("#Fcstatus").val(queryData.cstatus);
					$("#FbegVetime").val(queryData.begVetime);
					$("#FendVetime").val(queryData.endVetime);
					var form = $("#pageform");
					form.submit();
				}
			});
		},
	}
);
$.fn.extend({
	searchSubmit: function(){
		$(this).click(function(){
			channel = $("#channel option:selected").val();
			queryData.channel = channel;
			queryData.opid = $("#opid option:selected").val();
			queryData.no = $("#no").val();
			queryData.begVetime = $("#begVetime").val();
			queryData.endVetime = $("#endVetime").val();
			queryData.cstatus = $("#cstatus option:selected").val();
			$("#cardtabel").jqGrid('setGridParam',{
				url: queryUrl,//你的搜索程序地址 
        		postData:queryData, //发送搜索条件 
        		page:1 
    		}).trigger("reloadGrid"); //重新载入
		});
	}
});