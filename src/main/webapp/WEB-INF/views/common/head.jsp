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
</style>

<div style="width:100%;border:0" id="head_div">
<br>
<span>     
<a href="record.do">逐日盯市</a> &nbsp;&nbsp;
<a href="stats.do">分析图表</a> &nbsp;&nbsp;
<a href="publicPool.do">公众股票池</a> &nbsp;&nbsp;
<a href="history.do">历史经典K线</a> &nbsp;&nbsp;
<a href="help.do">新手必读</a> &nbsp;&nbsp;
<c:if test="${root==true}">
<a href="choose.do">模型筛选</a> &nbsp;&nbsp;&nbsp;&nbsp;
<a href="getToday.do?force=false">下载今日数据</a>【<a href="getToday.do?force=true">强制</a>】 &nbsp;&nbsp;
<a href="ananyse.do?force=false">分析数据</a>【<a href="ananyse.do?force=true">强制</a>】  &nbsp;&nbsp;
</c:if>
${user.userName},<a href="loginOut.do?force=false">退出</a>  &nbsp;&nbsp;
</span>
<hr style="color:blue;" size="1px">
</div>