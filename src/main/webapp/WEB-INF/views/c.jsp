<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>ECharts</title>
<script src="/resources/js/jquery.js"></script>
</head>
<body>

<div style="width:150px;float:left;">
<b>acvu</b>
<br>
<c:forEach var="s" items="${acvu}">  
     <a href="http://finance.sina.com.cn/realstock/company/${s.symbol}/nc.shtml" target="_blank">${s.symbol}</a><br>
</c:forEach>
</div>
   
<div style="width:150px;float:left;">
<b>big</b>
<br>
<c:forEach var="s" items="${big}">  
     <a href="http://finance.sina.com.cn/realstock/company/${s.symbol}/nc.shtml" target="_blank">${s.symbol}</a><br>
</c:forEach>
</div>

<div style="width:150px;float:left;">
<b>tp</b>
<br>
<c:forEach var="s" items="${tp}">  
     <a href="http://finance.sina.com.cn/realstock/company/${s.symbol}/nc.shtml" target="_blank">${s.symbol}</a><br>
</c:forEach>
</div>

<div style="width:150px;float:left;">
<b>av5</b>
<br>
<c:forEach var="s" items="${av5}">  
     <a href="http://finance.sina.com.cn/realstock/company/${s.symbol}/nc.shtml" target="_blank">${s.symbol}</a><br>
</c:forEach>
</div>

<div style="width:150px;float:left;">
<b>av10</b>
<br>
<c:forEach var="s" items="${av10}">  
     <a href="http://finance.sina.com.cn/realstock/company/${s.symbol}/nc.shtml" target="_blank">${s.symbol}</a><br>
</c:forEach>
</div>
</body>