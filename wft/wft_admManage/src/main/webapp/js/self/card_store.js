var queryUrl = 'card_store_list.do';
var queryData = {};
//日期解析
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
var columnObjects = [
		{index:'id',name: 'id', sortable: false, desc: '卡号ID'},
		{index:'no',name: 'no', sortable: false, desc: '卡号'},
		{index:'prvid',name: 'prvid', sortable: false, desc: '省份'},
		{index:'opid',name: 'opid', sortable: false, desc: '运营商'},
		{index:'rflag',name: 'rflag', sortable: false, desc: '漫游类型'},
		{index:'ctype',name: 'ctype', sortable: false, desc: '类型'},
		{index:'atime',name: 'atime', sortable: false, desc: '入库日期', formatter: this.formatDate},
		{index:'vetime',name: 'vetime', sortable: false, desc: '截止日期',formatter: this.formatDate},
		{index:'bvalue',name: 'bvalue', sortable: false, desc: '时长',formatter: this.timeFormatter},
		{index:'intime',name: 'intime', sortable: false, desc: '出库时间',formatter: this.formatDate},
		{index:'channel',name: 'channel', sortable: false, desc: '出库渠道'},
];
var columnNames= new Array();
$.each(columnObjects,function(index,value){
	columnNames[index] = value.desc;
});
$.extend(
	{	
		tableInit :function(provinceMaps, operatorMaps, channelMaps){ //init table
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
				caption : "卡池卡列表",
				autowidth : true,
				jsonReader : {
					repeatitems : false,
					id : 0
				},
				height : '100%',
				gridComplete: function(){ //结束重新构造单元格
					var ids = grid.jqGrid('getDataIDs');
					for(var i=0;i < ids.length;i++){
						var rowId = ids[i]; //Line id
						var rowData = grid.jqGrid('getRowData',rowId);
						var resultObject = {};
						resultObject.prvid = provinceMaps[rowData.prvid];
						resultObject.opid = operatorMaps[rowData.opid];
						resultObject.channel = channelMaps[rowData.channel];
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
						var rflag = rowData.rflag;
						if(rflag == 2){
							resultObject.rflag = "可以漫游";
						}else{
							resultObject.rflag = "不可以漫游";
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
		},
	}
);

$.fn.extend({
	searchSubmit: function(){
		$(this).click(function(){
			queryData.channel = $("#channel option:selected").val();
			queryData.opid = $("#opid option:selected").val();
			queryData.ctype = $("#ctype option:selected").val();
			queryData.prvid = $("#prvid option:selected").val();
			queryData.begVetime = $("#begVetime").val();
			queryData.endVetime = $("#endVetime").val();
			queryData.begIntime = $("#begIntime").val();
			queryData.endIntime = $("#endIntime").val();
			queryData.cstatus = $("#cstatus option:selected").val();
			$("#cardtabel").jqGrid('setGridParam',{
				url: queryUrl,//你的搜索程序地址 
        		postData:queryData, //发送搜索条件 
        		page:1 
    		}).trigger("reloadGrid"); //重新载入
		});
	},
	addBthSubmit: function(){
		$(this).click(function(){
			window.location.href = "card_store_add.do";
		});
	}
});