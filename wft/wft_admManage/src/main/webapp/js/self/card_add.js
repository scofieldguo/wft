var queryUrl = '';
var columnObjects = [
             		{index:'id',name: 'id', sortable: false, desc: '序号'},
             		{index:'no',name: 'no', sortable: false, desc: '卡号'},
             		{index:'prvid',name: 'prvid', sortable: false, desc: '省份'},
             		{index:'opid',name: 'opid', sortable: false, desc: '运营商'},
             		{index:'rflag',name: 'rflag', sortable: false, desc: '漫游'},
             		{index:'ctype',name: 'ctype', sortable: false, desc: '类型'},
             		{index:'atime',name: 'atime', sortable: false, desc: '起始日期', formatter: this.formatDate},
             		{index:'vetime',name: 'vetime', sortable: false, desc: '截止日期',formatter: this.formatDate},
             		{index:'bvalue',name: 'bvalue', sortable: false, desc: '时长',formatter: this.timeFormatter},
             		{index:'password',name: 'password', sortable: false, desc: '密码'},
             		{index:'repeat',name: 'repeat', sortable: false, desc: '重复卡'},
             		{index:'status',name: 'status', sortable: false, desc: '状态'},
             ];
var columnNames= new Array();
$.each(columnObjects,function(index,value){
	columnNames[index] = value.desc;
});
$.extend({
	tableInit : function(provinceMaps, operatorMaps, channelMaps) { //init table
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
			gridComplete : function() { //结束重新构造单元格
			},
		});
		grid.jqGrid('navGrid', '#pjmap1', {
			add : false,
			edit : false,
			del : false,
			search : false
		}, {}, {}, {}, {});
		grid.jqGrid('navButtonAdd', '#pjmap1', {
			caption : "列选择",
			onClickButton : function() {
				grid.jqGrid('columnChooser');
			}
		});
	},
});
$.fn.extend({
	uploadSubmit: function(){
		$(this).click(function(){
			var data = {};
			data.fileType = $("#fileType option:selected").val();
			data.opid = $("#opid option:selected").val();
			data.prvid = $("#prvid option:selected").val();
			data.ctype = $("#ctype option:selected").val();
			data.password = $("#password option:selected").val();
			data.repeatCard = $("#repeatCard option:selected").val();
			data.begVetime = $("#begVetime").val();
			data.endVetime = $("#endVetime").val();
			jQuery.ajaxFileUpload({
	        	url:'card_store_pre.do',
	        	secureuri :false,
				data: data,
	        	fileElementId :'cardFile',
	        	success : function (data, status){
					alert("上传文件成功");
	        	},
	        	error: function(data, status, e){
					alert("上传文件失败");
	        	}
			});
		});
	},
});