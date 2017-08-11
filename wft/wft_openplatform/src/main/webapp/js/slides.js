function banner(){	
	var bn_id1 = 0,bn_id2= 1,speed=5000,qhjg = 1,MyMar33;
	var colorArr =  new Array('#f9f9f9','#f9f9f9','#d0e9ff','#cfd3d6');
	$("#banner .d1").hide();
	$("#banner .d1").eq(0).fadeIn("slow");
	if($("#banner .d1").length>1){
		$("#banner_id li").eq(0).addClass("nuw");
		$("#slides").css("background-color",colorArr[0]);
		function Marquee(){
			bn_id2 = bn_id1+1;
			if(bn_id2>$("#banner .d1").length-1){
				bn_id2 = 0;
			}
			$("#slides").css("background-color",colorArr[bn_id2]);
			$("#banner .d1").eq(bn_id1).css("z-index","2");
			$("#banner .d1").eq(bn_id2).css("z-index","1");
			$("#banner .d1").eq(bn_id2).show();
			$("#banner .d1").eq(bn_id1).fadeOut("slow");
			$("#banner_id li").removeClass("nuw");
			$("#banner_id li").eq(bn_id2).addClass("nuw");
			bn_id1=bn_id2;
		};
		MyMar=setInterval(Marquee,speed);
		$("#banner_id li").click(function(){
			var bn_id3 = $("#banner_id li").index(this);
			if(bn_id3!=bn_id1&&qhjg==1){
				qhjg = 0;
				$("#slides").css("background-color",colorArr[bn_id3]);
				$("#banner .d1").eq(bn_id1).css("z-index","2");
				$("#banner .d1").eq(bn_id3).css("z-index","1");
				$("#banner .d1").eq(bn_id3).show();
				$("#banner .d1").eq(bn_id1).fadeOut("slow",function(){qhjg = 1;});
				$("#banner_id li").removeClass("nuw");
				$("#banner_id li").eq(bn_id3).addClass("nuw");
				bn_id1=bn_id3;
			}
		});
		$("#banner_id").hover(
			function(){
				clearInterval(MyMar);
			},
			function(){
				MyMar=setInterval(Marquee,speed);
			}
		)	
	}
	else{
		$("#banner_id").hide();
	}
}