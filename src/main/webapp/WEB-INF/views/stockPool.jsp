<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>复盘</title>



<style type="text/css">
.choose .symbolA {
	cursor: pointer;
}

a {
	text-decoration: none;
}
</style>
</head>
<body>
	<jsp:include page="common/head.jsp" flush="true" />



	<div style="width: 180px; float: left;" id="leftDiv">
		<div style="width: 180px; float: left;">
			<span style="width: 80px; float: left;"> <b><a href="#"
					id="acvuSymbol" class="choose" index="1" cat="acvuSymbol"
					style="background-color: pink">acvu:</a></b><font size="2">${acvuSize}</font>
			</span> <span style="width: 80px; float: left;"> <b><a href="#"
					id="bigSymbol" class="choose" index="2" cat="bigSymbol">big&nbsp;:</a></b><font
				size="2">${bigSize}</font>
			</span> <span style="width: 80px; float: left;"> <b><a href="#"
					id="av5Symbol" class="choose" index="3" cat="av5Symbol">av5&nbsp;:</a></b><font
				size="2">${av5Size}</font>
			</span> <span style="width: 80px; float: left;"> <b><a href="#"
					id="av10Symbol" class="choose" index="4" cat="av10Symbol">av10:</a></b><font
				size="2">${av10Size}</font>
			</span> 
			</span> <span style="width: 80px; float: left;"> <b><a href="#"
					id="tpSymbol" class="choose" index="4" cat="tpSymbol">tp:</a></b><font
				size="2">${tpSize}</font>
			</span> 
			</span> <span style="width: 80px; float: left;"> <b><a href="#"
					id="cbSymbol" class="choose" index="4" cat="cbSymbol">cb:</a></b><font
				size="2">${cbSize}</font>
			</span> 
			</span> <span style="width: 80px; float: left;"> <b><a href="#"
					id="ratioSymbol" class="choose" index="4" cat="ratioSymbol">ratio:</a></b><font
				size="2">${ratioSize}</font>
			</span> 
			</span> <span style="width: 80px; float: left;"> <b><a href="#"
					id="cb2Symbol" class="choose" index="4" cat="cb2Symbol">cb2:</a></b><font
				size="2">${cb2Size}</font>
			</span> 
			<br>
		</div>

		<div
			style="width: 180px; float: left; overflow-y: auto; position: relative"
			id="listDiv">
			<c:forEach var="s" items="${acvu}">
				<span class="acvuSymbol symbol ${s.getPosition()}" type="acvu"  symbol="${s.getSymbol()}" 
					style="width: 160px; float: left;"><a href="#" type="acvu" 
					idx="${s.getIndex()}" symbol="${s.getSymbol()}" class="symbolA"
					id="${s.getSymbol()}">${s.getCode()}&nbsp;${s.getNameFormat()}<c:if
							test="${s.ting==true}">
							<font color="red"><b>停牌</b></font>
						</c:if></a></span>
			</c:forEach>

			<c:forEach var="s" items="${big}">
				<span style="display: none"
					class="bigSymbol  symbol ${s.getPosition()}" type="big"  symbol="${s.getSymbol()}" 
					style="width:160px;float:left;"> <a href="#" type="big" 
					idx="${s.getIndex()}" symbol="${s.getSymbol()}"
					id="${s.getSymbol()}" class="symbolA">${s.getCode()}&nbsp;${s.getNameFormat()}<c:if
							test="${s.ting==true}">
							<font color="red"><b>停牌</b></font>
						</c:if></a></span>
			</c:forEach>

			<c:forEach var="s" items="${av5}">
				<span style="display: none"
					class="av5Symbol  symbol ${s.getPosition()}" type="av5"  symbol="${s.getSymbol()}" 
					style="width:160px;float:left;"> <a href="#" type="av5" 
					idx="${s.getIndex()}" symbol="${s.getSymbol()}"
					id="${s.getSymbol()}" class="symbolA">${s.getCode()}&nbsp;${s.getNameFormat()}<c:if
							test="${s.ting==true}">
							<font color="red"><b>停牌</b></font>
						</c:if></a></span>
			</c:forEach>

			<c:forEach var="s" items="${av10}">
				<span style="display: none"
					class="av10Symbol  symbol ${s.getPosition()}" type="av10"  symbol="${s.getSymbol()}" 
					style="width:160px;float:left;"> <a href="#" type="av10" 
					idx="${s.getIndex()}" symbol="${s.getSymbol()}"
					id="${s.getSymbol()}" class="symbolA">${s.getCode()}&nbsp;${s.getNameFormat()}<c:if
							test="${s.ting==true}">
							<font color="red"><b>停牌</b></font>
						</c:if></a></span>
			</c:forEach>
			
			<c:forEach var="s" items="${tp}">
				<span style="display: none"
					class="tpSymbol  symbol ${s.getPosition()}" type="tp"  symbol="${s.getSymbol()}" 
					style="width:160px;float:left;"> <a href="#" type="tp" 
					idx="${s.getIndex()}" symbol="${s.getSymbol()}"
					id="${s.getSymbol()}" class="symbolA">${s.getCode()}&nbsp;${s.getNameFormat()}<c:if
							test="${s.ting==true}">
							<font color="red"><b>停牌</b></font>
						</c:if></a></span>
			</c:forEach>
			
			<c:forEach var="s" items="${cb}">
				<span style="display: none"
					class="cbSymbol  symbol ${s.getPosition()}" type="cb"  symbol="${s.getSymbol()}" 
					style="width:160px;float:left;"> <a href="#" type="cb" 
					idx="${s.getIndex()}" symbol="${s.getSymbol()}"
					id="${s.getSymbol()}" class="symbolA">${s.getCode()}&nbsp;${s.getNameFormat()}<c:if
							test="${s.ting==true}">
							<font color="red"><b>停牌</b></font>
						</c:if></a></span>
			</c:forEach>
				
			<c:forEach var="s" items="${ratio}">
				<span style="display: none"
					class="ratioSymbol  symbol ${s.getPosition()}" type="ratio"  symbol="${s.getSymbol()}" 
					style="width:160px;float:left;"> <a href="#" type="ratio" 
					idx="${s.getIndex()}" symbol="${s.getSymbol()}"
					id="${s.getSymbol()}" class="symbolA">${s.getCode()}&nbsp;${s.getNameFormat()}<c:if
							test="${s.ting==true}">
							<font color="red"><b>停牌</b></font>
						</c:if></a></span>
			</c:forEach>
			
			<c:forEach var="s" items="${cb2}">
				<span style="display: none"
					class="cb2Symbol  symbol ${s.getPosition()}" type="cb"  symbol="${s.getSymbol()}" 
					style="width:160px;float:left;"> <a href="#" type="cb" 
					idx="${s.getIndex()}" symbol="${s.getSymbol()}"
					id="${s.getSymbol()}" class="symbolA">${s.getCode()}&nbsp;${s.getNameFormat()}<c:if
							test="${s.ting==true}">
							<font color="red"><b>停牌</b></font>
						</c:if></a></span>
			</c:forEach>

		</div>
	</div>
	<!-- 
<div id="check_div" style="float:left;margin-left: 80px"></div>
 -->

<div id="holderDiv" style="height: 800px;width:150px;float:left;display:none">
  <div id="holderContentDiv" style="float:left;padding-left:30px">

  </div>
  <div id="holderChartDiv" style="height: 400px;float:left;">

  </div>
</div>
<div id="container" style="height: 800px; float: left;"></div>




	<jsp:include page="common/foot.jsp" flush="true" />
	
	
	
	
	
	<script>
var r=false;
fresh();
function fresh(){
 	/* $("#shang").html("");
    $("#shen").html("");
    $("#chuang").html("");
    $("#zhong").html("");  */
	
	$.ajax({
		type : "get",
		async : true, //同步执行
		//"/addPublicPool.do?replace=false&symbols="+symbol+"-"+type,
		url : "/indexReal.do",
		dataType : "json", //返回数据形式为json
		success : function(result) {
			if (result) {
               $("#shang").html(result.sh);
               $("#shen").html(result.sz);
               $("#chuang").html(result.ch);
               $("#zhong").html(result.zh);
			}
		},
		error : function(errorMsg) {
		}
	});
}
</script>
	<script>


   /* var f='function a(i){if(i>0){return true;}else{ return false;};} a(i)';
   var i=1;
   eval("alert(eval(f))"); */
   var windowWidth=$(window).width();
   var windowHight=$(window).height();
   var w=windowWidth-220;
   var h=windowHight*0.8;
   $("#container").width(w-270);
   $("#container").height(h);
   $("#holderDiv").height(h);
   $("#holderDiv").width(270);
   $("#holderChartDiv").width(260);
   
   
   $("#leftDiv").height(h);
   $("#listDiv").height(h-100);
   
   var acvu=[${acvuStr}];
   var great=[${bigStr}];
   var av5=[${av5Str}];
   var av10=[${av10Str}];
   
   var currentCat="acvuSymbol";
   var currentCatIndex=1;
   
   var head=$(".head.acvuSymbol").get(0);
   var tail=$(".tail.acvuSymbol").get(0);
   
   var base;
   var start=0;
   var end=0;
   var total=0;
   var currentSymbol;
   var currentNode=tail;
   
   
   var all={};
   
   
   
   function getParam(name)
   {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
   }

   var scope=7;
   if(getParam("scope")){
   scope=getParam("scope");
   }
   
   //loading 图标
   function loadLayer(){
	    layer.load(1, {
		    type:3,
		    area: ['355px', '127px'],
		    shade: [0.1,'#000']//0.1透明度的白色背景
	    });
   };
   $.ajax({
			type : "get",
			url : "/getAll.do",
			dataType : "json", //返回数据形式为json
			beforeSend: function(){
				loadLayer();	
			},
			success : function(result) {
				if (result) {
					all=result;
					layer.closeAll();
					$(".symbolA").eq(0).click();
				}
			},
			error : function(errorMsg) {
				alert(errorMsg);
			}
   });
   
   
   
   
   
   $(".symbolA").on("click",function(){
	   currentNode=$(this).parent();
	   var symbol=$(this).attr("symbol");
	   currentSymbol=symbol;
	   $(".symbol").css("background-color","");
	   $(currentNode).css("background-color","pink");
	   
	    var idx=$(this).attr("idx");
	   
	    if(all.length>0){  
		   var a=all[idx];
		   if(a.symbol==symbol){
			    base=a;
				total=base.data.length;
				if(total>80){
					start=total-80;
				}else{
					start=0;
				}
				
				var copyMap={};
		    	copyMap.av5 = base.av5.slice(start);
		    	copyMap.av10 = base.av10.slice(start);
		    	copyMap.av20 = base.av20.slice(start);
		    	copyMap.data = base.data.slice(start);
		    	copyMap.vol = base.vol.slice(start);
		    	copyMap.start=base.start;
		    	copyMap.high=base.high;
		    	copyMap.low=base.low;
		    	copyMap.end=base.end;
		    	copyMap.name=base.name;
		    	copyMap.av5Tips=base.av5Tips;
		    	copyMap.acvuTips=base.acvuTips;
		    	copyMap.bigTips=base.bigTips;
		    	
		    	$("#holderDiv").show();
			    	$("#holderContentDiv").html(base.holder);
			    	
			    	if(base.years){
			    		$('#holderChartDiv').highcharts({ chart: { type: 'bar' }, 
                         title: { text: '' }, 
                         subtitle: { text: '' },
                         xAxis: { categories: base.years, title: { text: null } }, 
                         series: [{ name: '12', data: base.v12}, { name: '9', data:  base.v9 },{ name: '6', data:  base.v6 } ,{ name: '3', data:  base.v3} ] 
                      }); 
			    	}
		    	tradeChart(copyMap);
		   }
	   }
   });
   
   
   $(".choose").on("click",function(){
	   currentCat=$(this).attr("cat");
	   head=$(".head."+currentCat).get(0);
	   tail=$(".tail."+currentCat).get(0);
	   
	   
	   
	   var id=$(this).attr("id");
	   $(".choose").css("background-color","");
	   $(this).css("background-color","pink");
	   $(".symbol").css("display","none");
	   var aa=$("."+id);
	   $("."+id).attr("style","display:block;width:160px;float:left;");
	   
	   currentNode=tail;//切换
   });
   
   $(document).keydown(function(event){ 
	     
	   

	   if(event.key == 'BrowserForward'||event.keyCode == 167){
	    	event.preventDefault(); 
		    event.stopPropagation();
		    if(!r){
		    	r=true;
		    	setInterval('fresh()',3000);  
		    }else{
		    	r=false;
		    	window.location.reload(); 
		    } 
		}
	
	    if(event.key == 'BrowserBack'||event.keyCode == 166){
	    	event.preventDefault(); 
		    event.stopPropagation();
	    	var symbol=$(currentNode).attr("symbol");
	    	var type=$(currentNode).attr("type");
	    	
	    	$.ajax({
	 			type : "get",
	 			url : "/addPublicPool.do?replace=false&symbols="+symbol+"-"+type,
	 			dataType : "json", //返回数据形式为json
	 			beforeSend: function(){
	 				loadLayer();	
	 			},
	 			success : function(result) {
	 				if (result) {
	 					layer.closeAll();
	 				}
	 			},
	 			error : function(errorMsg) {
	 				alert(errorMsg);
	 			}
	    });
	    }
	    if(event.keyCode == 33||event.keyCode == 34){
	    	event.preventDefault(); 
		    event.stopPropagation();
	    	if(event.keyCode == 33) {
               if(currentCatIndex==1){
            	   currentCatIndex=8
               }else{
            	   currentCatIndex--;
               }
		    }
	    	if(event.keyCode == 34) {
	    	   if(currentCatIndex==8){
	    		   currentCatIndex=1
	           }else{
	        	   currentCatIndex++;
	           }
		    }
	    	
	    	if(currentCatIndex==1){
	    		currentCat='acvu';
	    	}
            if(currentCatIndex==2){
            	currentCat='big';
	    	}
            if(currentCatIndex==3){
            	currentCat='av5';
            }
            if(currentCatIndex==4){
            	currentCat='av10';
            }
            if(currentCatIndex==5){
            	currentCat='tp';
	    	}
            if(currentCatIndex==6){
            	currentCat='cb';
            }
            if(currentCatIndex==7){
            	currentCat='ratio';
            }
            if(currentCatIndex==8){
            	currentCat='cb2';
            }

	 	   head=$(".head."+currentCat+"Symbol").get(0);
	 	   tail=$(".tail."+currentCat+"Symbol").get(0);
	 	   

	 	   $(".choose").css("background-color","");
	 	   $("#"+currentCat+"Symbol").css("background-color","pink");
	 	   $(".symbol").css("display","none");
	 	   $("."+currentCat+"Symbol").attr("style","display:block;width:160px;float:left;");
	 	   
	 	   currentNode=tail;//切换 	
	    }	
	    
	    if(event.keyCode == 38||event.keyCode == 40){
	    	event.preventDefault(); 
		    event.stopPropagation();
	    	if(event.keyCode == 38) {
	    		if(start+40<total){
	    			start=start+40;
	    		}
		    }
	    	if(event.keyCode == 40) {
	        	if(start-20>0){
	        		start=start-20;
	        	}
		    }
	        var copyMap={};
	    	copyMap.av5 = base.av5.slice(start);
	    	copyMap.av10 = base.av10.slice(start);
	    	copyMap.av20 = base.av20.slice(start);
	    	copyMap.data = base.data.slice(start);
	    	copyMap.vol = base.vol.slice(start);
	    	copyMap.start=base.start;
	    	copyMap.high=base.high;
	    	copyMap.low=base.low;
	    	copyMap.end=base.end;
	    	copyMap.name=base.name;
	    	copyMap.av5Tips=base.av5Tips;
	    	copyMap.acvuTips=base.acvuTips;
	    	copyMap.bigTips=base.bigTips;

	    	$("#holderDiv").show();
		    	$("#holderContentDiv").html(base.holder);
		    	
		    	if(base.years){
		    		$('#holderChartDiv').highcharts({ chart: { type: 'bar' }, 
                     title: { text: '' }, 
                     subtitle: { text: '' },
                     xAxis: { categories: base.years, title: { text: null } }, 
                     series: [{ name: '12', data: base.v12}, { name: '9', data:  base.v9 },{ name: '6', data:  base.v6 } ,{ name: '3', data:  base.v3} ] 
                  }); 
		    	}
	    	tradeChart(copyMap);
	    }
	    
	    
	    if(event.keyCode == 37||event.keyCode == 39){
	    	event.preventDefault(); 
		    event.stopPropagation();
	    	var nodeNow=null;
	        if(event.keyCode == 37) {
	        	if($(currentNode).hasClass("head")){
	        		nodeNow=tail;
	        	}else{
	        		nodeNow=$(currentNode).prev();
	        	}
	        }
	    	if(event.keyCode == 39) {
	    		if($(currentNode).hasClass("tail")){
                	nodeNow=head;
                }else{
	        		nodeNow=$(currentNode).next();
	        	}
		    } 
	    	
	  	   
	  	    
	    	var symbol=$($(nodeNow).children().get(0)).attr("symbol");
		    currentSymbol=symbol;
		    currentNode=$(nodeNow);
		    
		    
		    var top=$(nodeNow).position().top;
		    var stop=$("#listDiv").scrollTop();
		    $("#listDiv").animate({scrollTop:(top+stop)},500);
		    
		    $(nodeNow).css("background-color","pink").siblings().css("background-color","");

	  	   var idx=$($(nodeNow).children().get(0)).attr("idx");
	  	   
	  	 if(all.length>0){
			   var a=all[idx];
			   if(a.symbol==symbol){
				    base=a;
					total=base.data.length;
					if(total>80){
						start=total-80;
					}else{
						start=0;
					}
					
					var copyMap={};
			    	copyMap.av5 = base.av5.slice(start);
			    	copyMap.av10 = base.av10.slice(start);
			    	copyMap.av20 = base.av20.slice(start);
			    	copyMap.data = base.data.slice(start);
			    	copyMap.vol = base.vol.slice(start);
			    	copyMap.start=base.start;
			    	copyMap.high=base.high;
			    	copyMap.low=base.low;
			    	copyMap.end=base.end;
			    	copyMap.name=base.name;
			    	copyMap.av5Tips=base.av5Tips;
			    	copyMap.acvuTips=base.acvuTips;
			    	copyMap.bigTips=base.bigTips;
			    	
			    	$("#holderDiv").show();
 			    	$("#holderContentDiv").html(base.holder);
 			    	
 			    	if(base.years){
 			    		$('#holderChartDiv').highcharts({ chart: { type: 'bar' }, 
	                         title: { text: '' }, 
	                         subtitle: { text: '' },
	                         xAxis: { categories: base.years, title: { text: null } }, 
	                         series: [{ name: '12', data: base.v12}, { name: '9', data:  base.v9 },{ name: '6', data:  base.v6 } ,{ name: '3', data:  base.v3} ] 
	                      }); 
 			    	}
			    	tradeChart(copyMap);
			   }
		   }
	  	   
	  	   /* //for(var i=0;i<all.length;i++){
	  	   if(all.length>0){
			   var a=all[idx];
			   if(a.symbol==symbol){
				    base=a;
					total=base.data.length;
					if(total>80){
						start=total-80;
					}else{
						start=0;
					}
					
					var copyMap={};
			    	copyMap.av5 = base.av5.slice(start);
			    	copyMap.av10 = base.av10.slice(start);
			    	copyMap.av20 = base.av20.slice(start);
			    	copyMap.data = base.data.slice(start);
			    	copyMap.vol = base.vol.slice(start);
			    	copyMap.start=base.start;
			    	copyMap.high=base.high;
			    	copyMap.low=base.low;
			    	copyMap.end=base.end;
			    	copyMap.name=base.name;
			    	copyMap.av5Tips=base.av5Tips;
			    	copyMap.acvuTips=base.acvuTips;
			    	copyMap.bigTips=base.bigTips;
			    	
			    	tradeChart(copyMap);
			   }
		   }else{
	  	 
	  	      $.ajax({
	  			type : "get",
	  			async : true, //同步执行
	  			url : "/kData.do?symbol="+symbol,
	  			dataType : "json", //返回数据形式为json
	  			success : function(result) {
	  				if (result) {
	  					base=result;
	  					total=base.data.length;
	  					if(total>80){
	  						start=total-80;
	  					}else{
	  						start=0;
	  					}
	  					
	  					var copyMap={};
	  			    	copyMap.av5 = base.av5.slice(start);
	  			    	copyMap.av10 = base.av10.slice(start);
	  			    	copyMap.av20 = base.av20.slice(start);
	  			    	copyMap.data = base.data.slice(start);
	  			    	copyMap.vol = base.vol.slice(start);
	  			    	copyMap.start=base.start;
	  			    	copyMap.high=base.high;
	  			    	copyMap.low=base.low;
	  			    	copyMap.end=base.end;
	  			    	copyMap.name=base.name;
	  			    	copyMap.av5Tips=base.av5Tips;
	  			    	copyMap.acvuTips=base.acvuTips;
	  			    	copyMap.bigTips=base.bigTips;
	  			    	tradeChart(copyMap);
	  				}
	  			},
	  			error : function(errorMsg) {
	  			}
	  		}); 
		   } */
	    }
   });  
   
   
   function tradeChart(all) {
		var crrentData = [];
		var retTrade = {
			"vl" : "1393776000000^6.67^6.64^6.71^6.56^76074355^503907245^-1.1905^-0.08^0.787^6.72^6.64^6.901^null^7.2427^null~1393862400000^7.0^6.92^7.1^6.78^212249601^1467381151^4.2169^0.28^2.194^6.64^6.71^6.866^null^7.2323^null~1393948800000^6.92^6.76^6.92^6.75^59026244^402549854^-2.3121^-0.16^0.61^6.92^6.726^6.805^null^7.2187^null~1394035200000^6.76^7.32^7.43^6.69^266097004^1900909534^8.284^0.56^2.751^6.76^6.872^6.805^null^7.223^null~1394121600000^7.34^7.27^7.46^7.23^171953502^1263703960^-0.6831^-0.05^1.778^7.32^6.982^6.816^null^7.2293^null~1394380800000^7.22^7.15^7.33^7.04^116402600^837063095^-1.6506^-0.12^1.203^7.27^7.084^6.862^null^7.23^null~1394467200000^7.19^7.5^7.59^7.15^189248809^1412509385^4.8951^0.35^1.957^7.15^7.2^6.955^null^7.2393^null~1394553600000^7.48^7.6^7.85^7.42^208987285^1603158727^1.3333^0.1^2.161^7.5^7.368^7.047^null^7.2387^null~1394640000000^7.58^7.71^7.85^7.53^149235872^1149903973^1.4474^0.11^1.543^7.6^7.446^7.159^null^7.246^null~1394726400000^7.68^7.53^7.81^7.5^130310997^996213411^-2.3346^-0.18^1.347^7.71^7.498^7.24^null^7.2373^null~1394985600000^7.6^7.76^7.8^7.59^117707900^908293030^3.0544^0.23^1.217^7.53^7.62^7.352^null^7.241^null~1395072000000^7.76^7.6^7.76^7.55^84389956^642680623^-2.0619^-0.16^0.873^7.76^7.64^7.42^null^7.2397^null~1395158400000^7.51^7.5^7.54^7.24^157546281^1158670983^-1.3158^-0.1^1.629^7.6^7.62^7.494^null^7.2383^null~1395244800000^7.5^7.51^7.73^7.4^115455187^876828993^0.1333^0.01^1.194^7.5^7.58^7.513^null^7.2427^null~1395331200000^7.57^8.01^8.08^7.45^270439501^2118807009^6.6578^0.5^2.796^7.51^7.676^7.587^null^7.2627^null~1395590400000^8.01^8.06^8.17^7.85^139877099^1122120961^0.6242^0.05^1.446^8.01^7.736^7.678^null^7.281^null~1395676800000^8.04^7.98^8.2^7.9^95754115^769251859^-0.9926^-0.08^0.99^8.06^7.812^7.726^null^7.2953^null~1395763200000^7.99^7.85^8.04^7.79^70405842^553737780^-1.6291^-0.13^0.728^7.98^7.882^7.751^null^7.3063^null~1395849600000^7.8^7.98^8.1^7.73^107800631^857121798^1.6561^0.13^1.115^7.85^7.976^7.778^null^7.3253^null~1395936000000^7.93^8.12^8.24^7.92^110530999^896881736^1.7544^0.14^1.143^7.98^7.998^7.837^null^7.3503^null~1396195200000^8.16^8.09^8.22^7.96^67897119^546825578^-0.3695^-0.03^0.702^8.12^8.004^7.87^null^7.3743^null~1396281600000^8.08^8.08^8.18^8.05^52524942^426237417^-0.1236^-0.01^0.543^8.09^8.024^7.918^null^7.4013^null~1396368000000^8.14^8.29^8.4^8.11^128655948^1069009796^2.599^0.21^1.331^8.08^8.112^7.997^null^7.432^null~1396454400000^8.3^8.1^8.39^8.07^98636692^810649950^-2.2919^-0.19^1.02^8.29^8.136^8.056^null^7.458^null~1396540800000^8.1^8.11^8.15^8.01^64285871^519860625^0.1235^0.01^0.665^8.1^8.134^8.066^null^7.4897^null~1396886400000^8.05^8.15^8.23^7.94^96118504^778608412^0.4932^0.04^0.994^8.11^8.146^8.075^null^7.5383^null~1396972800000^8.16^8.05^8.19^8.02^92985418^749184322^-1.227^-0.1^0.962^8.15^8.14^8.082^null^7.5877^null~1397059200000^8.04^8.05^8.08^7.82^160169456^1271310054^0.0^0.0^1.657^8.05^8.092^8.102^null^7.6333^null~1397145600000^8.0^7.95^8.01^7.89^77595171^616719793^-1.2422^-0.1^0.803^8.05^8.062^8.099^null^7.6787^null~1397404800000^7.95^7.93^8.01^7.83^53024683^419179437^-0.2516^-0.02^0.548^7.95^8.026^8.08^null^7.719^null~1397491200000^7.86^7.76^7.91^7.7^75881356^590811570^-2.1438^-0.17^0.785^7.93^7.948^8.047^null^7.7563^null~1397577600000^7.74^7.87^7.92^7.71^70965625^557522103^1.4175^0.11^0.734^7.76^7.912^8.026^null^7.788^null~1397664000000^7.88^7.75^7.89^7.73^45759242^355678857^-1.5248^-0.12^0.473^7.87^7.852^7.972^null^7.821^null~1397750400000^7.7^7.78^7.8^7.62^41008279^316044522^0.3871^0.03^0.424^7.75^7.818^7.94^null^7.8363^null~1398009600000^7.71^7.66^7.8^7.65^45448390^351633982^-1.5424^-0.12^0.47^7.78^7.764^7.895^null^7.8493^null~1398096000000^7.65^7.74^7.77^7.62^64815843^498926810^1.0444^0.08^0.67^7.66^7.76^7.854^null^7.869^null~1398182400000^7.72^7.8^7.83^7.7^48885958^380461201^0.7752^0.06^0.506^7.74^7.746^7.829^null^7.879^null~1398268800000^7.83^7.99^8.1^7.77^139640297^1118052366^2.4359^0.19^1.444^7.8^7.794^7.823^null^7.892^null~1398355200000^8.07^7.94^8.11^7.9^69763109^558804653^-0.6258^-0.05^0.722^7.99^7.826^7.822^null^7.8997^null~1398614400000^7.98^7.97^8.16^7.92^96845118^777834925^0.3778^0.03^1.002^7.94^7.888^7.826^null^7.9143^null~1398700800000^7.91^7.94^7.98^7.67^67109783^524493489^-0.3764^-0.03^0.694^7.97^7.928^7.844^null^7.9203^null~1398787200000^7.87^7.9^7.98^7.77^39547643^312451018^-0.5038^-0.04^0.409^7.94^7.948^7.847^null^7.9303^null~1399219200000^7.85^7.74^7.86^7.67^57717483^446552195^-2.0253^-0.16^0.597^7.9^7.898^7.846^null^7.9383^null~1399305600000^7.74^7.79^7.9^7.74^46379716^362147101^0.646^0.05^0.48^7.74^7.868^7.847^null^7.9477^null~1399392000000^7.79^7.87^7.89^7.75^52723015^413257743^1.027^0.08^0.545^7.79^7.848^7.868^null^7.943^null~1399478400000^7.48^7.35^7.58^7.32^75190490^559315921^-1.4745^-0.11^0.778^7.46^7.73^7.829^null^7.9193^null~1399564800000^7.35^7.26^7.37^7.1^62968012^455104583^-1.2245^-0.09^0.651^7.35^7.602^7.775^null^7.8953^null~1399824000000^7.28^7.32^7.43^7.19^51722748^378905109^0.8264^0.06^0.535^7.26^7.518^7.708^null^7.8777^null~1399910400000^7.39^7.53^7.59^7.37^89727866^674165060^2.8689^0.21^0.928^7.32^7.466^7.667^null^7.8627^null~1399996800000^7.65^7.63^7.83^7.61^93198701^717443075^1.328^0.1^0.964^7.53^7.418^7.633^null^7.8463^null~1400083200000^7.62^7.58^7.64^7.53^45869870^347998497^-0.6553^-0.05^0.474^7.63^7.464^7.597^null^7.8293^null~1400169600000^7.58^7.7^7.8^7.58^96234169^742805234^1.5831^0.12^0.995^7.58^7.552^7.577^null^7.8167^null~1400428800000^7.66^7.69^7.78^7.52^59464663^455281149^-0.1299^-0.01^0.615^7.7^7.626^7.572^null^7.7967^null~1400515200000^7.7^7.74^7.78^7.64^68587514^530138261^0.6502^0.05^0.709^7.69^7.668^7.567^null^7.7847^null~1400601600000^7.68^7.74^7.77^7.58^54343526^417012721^0.0^0.0^0.562^7.74^7.69^7.554^null^7.7723^null~1400688000000^7.75^7.89^7.94^7.73^92142006^726170189^1.938^0.15^0.953^7.74^7.752^7.608^null^7.7637^null~1400774400000^7.92^8.15^8.2^7.85^133827747^1079426650^3.2953^0.26^1.384^7.89^7.842^7.697^null^7.767^null~1401033600000^8.2^8.11^8.29^8.07^60755644^494191825^-0.4908^-0.04^0.628^8.15^7.926^7.776^null^7.769^null~1401120000000^8.11^8.17^8.25^8.06^80099743^655534259^0.7398^0.06^0.828^8.11^8.012^7.84^null^7.7763^null~1401206400000^8.18^8.45^8.46^8.17^155707433^1298336237^3.4272^0.28^1.611^8.17^8.154^7.922^null^7.7937^null~",
			"ccode" : "000002.sz",
			"tag" : "mtag_s430101_房地产开发",
			"cname" : "万  科Ａ"
		};
		new highStockChart('container', retTrade, crrentData, all);
	}
</script>
</body>