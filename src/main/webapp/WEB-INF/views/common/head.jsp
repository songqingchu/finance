<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<style type="text/css">
.choose .symbolA{
cursor:pointer;
}
a{
text-decoration:none; 
}
body{
font-size:13px;
color:purple;
}
</style>

<div style="width:100%;border:0" id="head_div">
<br>
<span>     
<a href="record.do">逐日盯市</a> &nbsp;
<a href="stats.do">分析图表</a> &nbsp;
<a href="publicPool.do">盯市</a> &nbsp;
<a href="operate.do">操作</a> &nbsp;
<a href="history.do">经典</a> &nbsp;
<a href="help.do">新手</a> &nbsp;
<c:if test="${root==true}">
<a href="choose.do">模型筛选</a> &nbsp;
<a href="check.do">检测</a> &nbsp;
<a href="getToday.do?force=false">下载数据</a>【<a href="getToday.do?force=true">强制</a>】 &nbsp;
<a href="ananyse.do?force=false">分析数据</a>【<a href="ananyse.do?force=true">强制</a>】  &nbsp;
</c:if>
<c:if test="${user.userName!= null}">
${user.userName},<a href="loginOut.do?force=false">退出</a>  &nbsp;
</c:if>

<c:if test="${root==true}">
<b>
${sessionScope.isWorking}&nbsp;${sessionScope.downloaded}&nbsp;${sessionScope.choosen}
</b>
</c:if>
</span>
<hr style="color:blue;" size="1px">
</div>

