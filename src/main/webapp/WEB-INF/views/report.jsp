<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>操作</title>
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

</style>
</head>
<body>
<jsp:include page="common/head.jsp" flush="true"/>
 

   
<div style="width:20%;float:left;" id="list">
<c:forEach var="s" items="${l}">  
     <span  class="">
       ${s.pingNum}
     </span>
     
     <span  class="">
       ${s.dateString}
     </span>
     
     <span  class="" style="width:180px;float:left;">
     <a href="${s.link}" >
      ${s.title}
     </a>
     </span>
</c:forEach>
</div>

<div id="container" style="height: 800px;float:left;width:80%;">
${now.content}
</div>


<jsp:include page="common/foot.jsp" flush="true"/>
</body>
