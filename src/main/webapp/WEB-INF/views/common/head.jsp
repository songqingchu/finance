<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script src="/resources/js/jquery.min.js" type="text/javascript"></script>
<script src="/resources/js/highstock.js"></script>
<script src="/resources/js/chartExt.js"></script>
<script src="/resources/js/layer/layer.js"></script>
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
<a href="operate.do?currentCat=&currentSymbol=">操作</a> &nbsp;
<a href="history.do">经典</a> &nbsp;
<a href="help.do">新手</a> &nbsp;
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
&nbsp;&nbsp;&nbsp;&nbsp;
<span id="shang"></span>
<span id="shen"></span>
<span id="zhong"></span>
<span id="chuang"></span>

</span>
<hr style="color:blue;" size="1px">
</div>

<script>


fresh();

function fresh(){
	
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