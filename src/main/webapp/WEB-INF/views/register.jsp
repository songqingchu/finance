<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<head>
<head>
<meta charset="utf-8">
<title>见证你在股市中成长的每一步</title>
<!--[if lt IE 9]><script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
<script src="/resources/js/jquery.min.js" type="text/javascript"></script>
<script src="/resources/js/highstock.js"></script>
<script src="/resources/js/chartExt.js"></script>
</head>
<body>
<jsp:include page="common/head.jsp" flush="true"/>
	<div id="report" style="width:100%;height: 600px;align:center;text-align: center;">
	  <br><br><br><br><br><br><br><br>
	  <form action="register.do" method="POST">
	  <font color="red">${message}</font>
	  <br><br>
	   用户名 <input type="text" name="userName"/><br><br>
	  密&nbsp;&nbsp;码   <input type="password" name="passWord"/><br><br>
	&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="注册"/>&nbsp;&nbsp;&nbsp;&nbsp;
	 </form>
	
	</div>
<jsp:include page="common/foot.jsp" flush="true"/>
</body>
<script>

</script>
</html>