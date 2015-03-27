<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>见证你在股市中成长的每一步</title>
<script src="/resources/js/jquery.min.js" type="text/javascript"></script>
<script src="/resources/js/highstock.js"></script>
<script src="/resources/js/chartExt.js"></script>
<script src="/resources/js/jquery-ui.js"></script>

<!-- DataTables -->
<script type="text/javascript" charset="utf8" src="/resources/js/jquery.dataTables.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/jquery.dataTables.css">
<link rel="stylesheet" type="text/css" href="/resources/css/jquery-ui.css">
<style type="text/css">
.choose .symbolA{
cursor:pointer;
}
a{
text-decoration:none; 
}
tr th {
  border:1px solid;
  font-size:12px;
  background: #68EE68;
}

td {
  background: #68EE68;
}

</style>
</head>
<body>
<jsp:include page="common/head.jsp" flush="true"/>   

<div style="float:left;width:100%;height:500px">
<form id="addForm" action="addRecord.do" method="post">
<input name='lastValue' style='width:80px' type='hidden' value="${lastValue}"></input>
<input name='lastVRate' style='width:80px' type='hidden' value="${lastVRate}"></input>
   <table id="recordTable" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th width="70px">日期</th>
						<th width="90px">Value</th>
						<th width="70px">Change</th>
						<th width="40px">AYC</th>
						<th width="40px">ASC</th>
                        <th width="40px">NYC</th>
						<th width="40px">NSC</th>
						<th width="80px">AYV</th>
						<th width="80px">ASV</th>
						<th width="70px">NYV</th>
						<th width="70px">NSV</th>
						<th width="40px">AYP</th>
						<th width="40px">ASP</th>
						<th width="40px">NYP</th>
						<th width="40px">NSP</th>
						<th width="40px">AYR</th>
						<th width="40px">ASR</th>
						<th width="40px">NYR</th>
						<th width="40px">NSR</th>
					</tr>
				</thead>
				
				
				<tbody id="recordTableBody" >
				<c:forEach var="d" items="${data}">  
				<tr>
				       <td>${d.date}</td>
				       <td>${d.value}</td>
				       <td>${d.change}</td>
				       <td>${d.ayCount}</td>
				       <td>${d.asCount}</td>
				       <td>${d.nyCount}</td>
				       <td>${d.nsCount}</td>
				       <td>${d.ayValue}</td>
				       <td>${d.asValue}</td>
				       <td>${d.nyValue}</td>
				       <td>${d.nsValue}</td>
				       <td>${d.ayPosition}</td>
				       <td>${d.asPosition}</td>
				       <td>${d.nyPosition}</td>
				       <td>${d.nsPosition}</td>
				       <td>${d.ayRateFormat}</td>
				       <td>${d.asRateFormat}</td>
				       <td>${d.nyRateFormat}</td>
				       <td>${d.nsRateFormat}</td>
				</tr>
				</c:forEach>
				<c:if test="${size>0}">
				<tr>
				       <td></td>
				       <td><a href="#" toId="value" value="${t.value}" class="tianchong">填充</a></td>
				       <td></td>
				       <td><a href="#" toId="ayc" value="${t.ayCount}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="asc" value="${t.asCount}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nyc" value="${t.nyCount}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nsc" value="${t.nsCount}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="ayv" value="${t.ayValue}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="asv" value="${t.asValue}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nyv" value="${t.nyValue}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nsv" value="${t.nsValue}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="ayp" value="${t.ayPosition}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="asp" value="${t.asPosition}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nyp" value="${t.nyPosition}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nsp" value="${t.nsPosition}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="ayr" value="${t.ayRateFormat}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="asr" value="${t.asRateFormat}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nyr" value="${t.nyRateFormat}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nsr" value="${t.nsRateFormat}" class="tianchong">填充</a></td>
				</tr>
				</c:if>
				</tbody>
    </table>
</form>
    <br><br>
    <a href="#" id="addButton">新增盯市记录</a>&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="#" id="cancelButton">取消</a>&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="stats.do" id="cancelButton">查看图表</a>
</div>
</body>
<script>
var newNode=false; 
$.datepicker.setDefaults($.datepicker.regional['zh-CN']);
var html="<tr id='newTr'><td><input  name='date' style='width:68px' type='text'  id='date'></input></td>"+
             "<td><input id='value' name='value' style='width:80px' type='text'></input></td>"+
             "<td><input name='change' style='width:60px' type='text' value='0'></input></td>"+
             "<td><input id='ayc' name='ayc' style='width:30px' type='text'></input></td>"+
             "<td><input id='asc' name='asc' style='width:30px' type='text'></input></td>"+
             "<td><input id='nyc' name='nyc'  style='width:30px' type='text'></input></td>"+
             "<td><input id='nsc' name='nsc'  style='width:30px' type='text'></input></td>"+
             "<td><input id='ayv' name='ayv'  style='width:70px' type='text'></input></td>"+
             "<td><input id='asv' name='asv'  style='width:70px' type='text'></input></td>"+
             "<td><input id='nyv' name='nyv'  style='width:60px' type='text'></input></td>"+
             "<td><input id='nsv' name='nsv'  style='width:60px' type='text'></input></td>"+
             "<td><input id='ayp' name='ayp'  style='width:30px' type='text'></input></td>"+
             "<td><input id='asp' name='asp'  style='width:30px' type='text'></input></td>"+
             "<td><input id='nyp' name='nyp'  style='width:30px' type='text'></input></td>"+
             "<td><input id='nsp' name='nsp'  style='width:30px' type='text'></input></td>"+
             "<td><input id='ayr' name='ayr'  style='width:30px' type='text'></input></td>"+
             "<td><input id='asr' name='asr'  style='width:30px' type='text'></input></td>"+
             "<td><input id='nyr' name='nyr'  style='width:30px' type='text'></input></td>"+
             "<td><input id='nsr' name='nsr'  style='width:30px' type='text'></input></td>";

$("#addButton").click(function(){
	if(newNode==true){
		$("#addForm").submit();
		return;
	}
	$(this).text("提交");
	$(html).appendTo("#recordTableBody");
	
	$( "#date" ).datepicker({ dateFormat: 'yy.mm.dd' });
	newNode=true;
});

$("#cancelButton").click(function(){
	if(newNode==false){
		return;
	}
	$("#newTr").remove();
	$("#addButton").text("新增盯市记录");
	newNode=false;
});

$(".tianchong").click(function(){
    var target=$(this).attr("toId");
    var val=$(this).attr("value");
    $("#"+target).val(val);
});

</script>