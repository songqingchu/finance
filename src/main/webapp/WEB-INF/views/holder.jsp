<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>

<head>
<title>股东户数对照表</title>
</head>
<script src="/resources/js/jquery.min.js" type="text/javascript"></script>
<script src="/resources/js/jquery-ui.js"></script>
<script src="/resources/js/highchart/highcharts.js"></script>
<script src="/resources/js/chartExt.js"></script>
<script src="/resources/js/layer/layer.js"></script>
<body>

<div style="width:100%;border:0" id="head_div">
<br>
<span style="height:20px;v-align:middle;display:block">     
<a href="record.do">盯市</a> &nbsp;
<a href="stats.do">图表</a> &nbsp;
<a href="publicPool.do">实时</a> &nbsp;
<a href="operate.do?currentCat=&currentSymbol=">股票池</a> &nbsp;
<!-- 
<a href="help.do">新手</a> &nbsp;
 -->
<a href="history.do">经典</a> &nbsp;
<a href="holderList.do">股东户数</a> &nbsp;


<c:if test="${root==true}">
<a href="choose.do">复盘</a> &nbsp;
<!-- 
<a href="rixing.do">日省</a> &nbsp;
<a href="canwu.do">参悟</a> &nbsp;
 -->
<a href="check.do">检测</a> &nbsp;
<a href="getToday.do?force=false">下载</a>【<a href="getToday.do?force=true">强制</a>】 &nbsp;
<a href="ananyse.do?force=false">分析</a>【<a href="ananyse.do?force=true">强制</a>】  &nbsp;
</c:if>
<c:if test="${user.userName!= null}">
${user.userName},<a href="loginOut.do?force=false">退出</a>  &nbsp;
</c:if>

<c:if test="${root==true}">
<b>
${sessionScope.isWorking}&nbsp;${sessionScope.downloaded}&nbsp;${sessionScope.choosen}
</b>
</c:if>
</span> <br>

<span  style="height:14px;display:inline-block;font-size:15px;">上:</span>
<span id="shang" style="height:14px;display:inline-block;font-size:15px;"></span>&nbsp;

<span  style="height:14px;display:inline-block;font-size:15px;">深:</span>
<span id="shen" style="height:14px;display:inline-block;font-size:15px;"></span>&nbsp;

<span  style="height:14px;display:inline-block;font-size:15px;">中:</span>
<span id="zhong" style="height:14px;display:inline-block;font-size:15px;"></span>&nbsp;

<span  style="height:14px;display:inline-block;font-size:15px;">创:</span>
<span id="chuang" style="height:14px;display:inline-block;font-size:15px;"></span>&nbsp;

<hr style="color:blue;" size="1px">
</div>


 

   
<div style="width:100%;" id="list">
<c:forEach var="s" items="${all}">  

     
     <div id="holderDiv" style="height: 300px;width:150px;">
     <a href="#" symbol="${s.symbol}" class="symbol" id="${s.symbol}" name="${s.name}" ">
       ${s.name}
     </a>
  <div id="${s.symbol}holderContentDiv" style="float:left;;padding-left:30px">

  </div>
  <div id="${s.symbol}holderChartDiv" style="height: 300px;float:left;">

  </div>
</div>

<br>
</c:forEach>

</div>

<div id="container" style="height: 800px;float:left;display:none"></div>
<jsp:include page="common/foot.jsp" flush="true"/>
</body>

<script>
var index=100;
var max=0;
var rrr;
$.ajax({
		type : "get",
		async : true, //同步执行
		url : "/allkData.do",
		dataType : "json", //返回数据形式为json
		success : function(result) {
			if (result) {
				max=result.length;
				rrr=result;
				for(var i=0;i<100;i++){
					var map=result[i];
					var symbol=map.symbol;
		               // $('#'+symbol+"#holderContentDiv").html(map.holder);
				    	
				    	if(map.years){
				    		console.log('#'+symbol+'holderChartDiv');
				    		$('#'+symbol+'holderChartDiv').highcharts({ chart: { type: 'bar' }, 
		                     title: { text: '' }, 
		                     subtitle: { text: '' },
		                     xAxis: { categories: map.years, title: { text: null } }, 
		                     series: [{ name: '12', data: map.v12}, { name: '9', data:  map.v9 },{ name: '6', data:  map.v6 } ,{ name: '3', data:  map.v3} ] 
		                  }); 
				    	}	
				}
				
				inedx=100;
			}
		},
		error : function(errorMsg) {
		}
	});
	
$(document).keydown(function(event){ 
	if(event.keyCode == 37){
		event.preventDefault(); 
	    event.stopPropagation();
		
		var end=0;
		if(index+10<max){
			end=index+100;
		}else{
			end=max;
		}

		for(var i=index;i<end;i++){
			var map=rrr[i];
			var symbol=map.symbol;
	           // $('#'+symbol+"#holderContentDiv").html(map.holder);
		    	if(map.years){
		    		console.log('#'+symbol+'holderChartDiv');
		    		$('#'+symbol+'holderChartDiv').highcharts({ chart: { type: 'bar' }, 
	                 title: { text: '' }, 
	                 subtitle: { text: '' },
	                 xAxis: { categories: map.years, title: { text: null } }, 
	                 series: [{ name: '12', data: map.v12}, { name: '9', data:  map.v9 },{ name: '6', data:  map.v6 } ,{ name: '3', data:  map.v3} ] 
	              }); 
		    	}		
		}
		index=index+100;
	}
});

//从公众池中移除
$(".symbol").on("click",function(){

	event.preventDefault(); 
    event.stopPropagation();
	
	var end=0;
	if(index+100<max){
		end=index+100;
	}else{
		end=max;
	}

	for(var i=index;i<end;i++){
		var map=rrr[i];
		var symbol=map.symbol;
           // $('#'+symbol+"#holderContentDiv").html(map.holder);
	    	if(map.years){
	    		console.log('#'+symbol+'holderChartDiv');
	    		$('#'+symbol+'holderChartDiv').highcharts({ chart: { type: 'bar' }, 
                 title: { text: '' }, 
                 subtitle: { text: '' },
                 xAxis: { categories: map.years, title: { text: null } }, 
                 series: [{ name: '12', data: map.v12}, { name: '9', data:  map.v9 },{ name: '6', data:  map.v6 } ,{ name: '3', data:  map.v3} ] 
              }); 
	    	}		
	}
	index=index+100;
	
});
</script>