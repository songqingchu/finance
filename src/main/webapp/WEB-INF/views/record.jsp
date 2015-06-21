<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>逐日钉市-赌博者之路</title>

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

<div style="float:left;width:100%;">
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
						<th width="60px">AYV</th>
						<th width="60px">ASV</th>
						<th width="50px">NYV</th>
						<th width="50px">NSV</th>
						<th width="40px">AYP</th>
						<th width="40px">ASP</th>
						<th width="40px">NYP</th>
						<th width="40px">NSP</th>
						<th width="40px">AYR</th>
						<th width="40px">ASR</th>
						<th width="40px">NYR</th>
						<th width="40px">NSR</th>
						<th width="40px">操作</th>
					</tr>
				</thead>
				
				
				<tbody id="recordTableBody" >
				<c:forEach var="d" items="${data}">  
				<tr>
				       <td>${d.dateFormat}</td>
				       <td>${d.money}</td>
				       <td>${d.modi}</td>
				       <td>${d.ayc}</td>
				       <td>${d.asc}</td>
				       <td>${d.nyc}</td>
				       <td>${d.nsc}</td>
				       <td>${d.ayv}</td>
				       <td>${d.asv}</td>
				       <td>${d.nyv}</td>
				       <td>${d.nsv}</td>
				       <td>${d.ayp}</td>
				       <td>${d.asp}</td>
				       <td>${d.nyp}</td>
				       <td>${d.nsp}</td>
				       <td>${d.ayRateFormat}</td>
				       <td>${d.asRateFormat}</td>
				       <td>${d.nyRateFormat}</td>
				       <td>${d.nsRateFormat}</td>
				       <td>
				       <c:if test="${d.last==true}">
				       <a href="delLastLine.do">删除</a>
				       </c:if>
				       </td>
				</tr>
				</c:forEach>
				<c:if test="${size>0}">
				<tr>
				       <td></td>
				       <td><a href="#" toId="v" v="${t.money}" class="tianchong">填充</a></td>
				       <td></td>
				       <td><a href="#" toId="ayc" v="${t.ayc}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="asc" v="${t.asc}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nyc" v="${t.nyc}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nsc" v="${t.nsc}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="ayv" v="${t.ayv}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="asv" v="${t.asv}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nyv" v="${t.nyv}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nsv" v="${t.nsv}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="ayp" v="${t.ayp}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="asp" v="${t.asp}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nyp" v="${t.nyp}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nsp" v="${t.nsp}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="ayr" v="${t.ayRateFormat}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="asr" v="${t.asRateFormat}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nyr" v="${t.nyRateFormat}" class="tianchong">填充</a></td>
				       <td><a href="#" toId="nsr" v="${t.nsRateFormat}" class="tianchong">填充</a></td>
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
<jsp:include page="common/foot.jsp" flush="true"/>
</body>


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
    var val=$(this).attr("v");
    $("#"+target).val(val);
});

</script>