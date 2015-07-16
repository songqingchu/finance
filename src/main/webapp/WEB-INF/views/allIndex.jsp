<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>盯市</title>
<script src="/resources/js/jquery.min.js" type="text/javascript"></script>
<script src="/resources/js/highstock.js"></script>
<script src="/resources/js/chartExt.js"></script>
<script src="/resources/js/jquery.min.js" type="text/javascript"></script>
<script src="/resources/js/highstock.js"></script>
<script src="/resources/js/chartExt.js"></script>
<script src="/resources/js/jquery-ui.js"></script>

<style type="text/css">
.choose .symbolA{
cursor:pointer;
}
a{
text-decoration:none; 
}

</style>
</head>
<body>
<div class="showDiv" style="width:400px;height:400px;float:left"><iframe  style="width:100%;height:100%;" src="http://finance.sina.com.cn/flash/cn.swf?symbol=sh000001"></iframe>  </div>
<div class="showDiv" class="showDiv"   style="width:400px;height:400px;float:left"><iframe style="width:100%;height:100%;" src="http://finance.sina.com.cn/flash/cn.swf?symbol=sh000001"></iframe>  </div>
<div class="showDiv"   style="width:400px;height:400px;float:left"><iframe style="width:100%;height:100%;" src="http://finance.sina.com.cn/flash/cn.swf?symbol=sh000001"></iframe>  </div>
<div class="showDiv"  style="width:400px;height:400px;float:left"><iframe style="width:100%;height:100%;" src="http://finance.sina.com.cn/flash/cn.swf?symbol=sh000001"></iframe>  </div>
<div class="showDiv"  style="width:400px;height:400px;float:left"><iframe style="width:100%;height:100%;" src="http://finance.sina.com.cn/flash/cn.swf?symbol=IF1507"></iframe>  </div>
</body>
</html>
<script>
   var windowWidth=$(window).width();
   var windowHight=$(window).height();
   var w=(windowWidth-20)/4;
   var h=(windowHight-20)/2;
   
   $(".showDiv").height(h);
   $(".showDiv").width(w);
   
   
</script>