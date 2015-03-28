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
</head>
<body>
<jsp:include page="common/head.jsp" flush="true"/>
<pre>
为了让您快速掌握本网站提供的功能，请您阅读以下说明
一 逐日盯市
让您逐日记录您的投资记录，以便生成统计数据，对您的投资系统进行多维度的总结，如图所示：
<img src="/resources/pic/record.png" style="width:80%;height=80%"></img>
其中，名词解释如下
（1）日期
即当日开市的日期，切勿填为非开市日期
（2）value
即当日您的账户资产，如果您使用了融资/融券，也应计算在内。
（3）change
由于您自己的原因，您可能向您的股票账户中增加资金，或者撤出资金，其中撤出资金为负。
（4）AYC，已结获利次数
（5）ASC，已结亏损次数
（6）NYC，未结获利次数
（7）NSC，未结亏损字数
（8）AYV，已结获利金额
（9）ASV，已结亏损金额
（10）NYV，未结获利金额
（11）NSV，未结亏损金额
（12）AYP，已结获利仓位
（13）ASP，已结亏损仓位
（14）NYP，未结获利仓位
（15）NSP，未结亏损仓位
（16）AYR，已结获利收益率
（17）ASR，已结亏损收益率
（18）NYR，未结获利收益率
（19）NSR，未结亏损收益率
二 分析图表
以图表的方式从多个角度展现您的投资记录。
（1）资金曲线图
记录您的资金增长率，比如您第一天资金为100000，第二天资金变为101000，则第一天的资金增长率为0（100-100），第二日为1（101-100），
同时有上证指数，深成指数，创业板综，中小板综指数增长率作为对比。
（2）年华预期
根据现时您的资金增长率，加上时间因素，预估您本年度的资金增长幅度，例如，您在两个月的时间资金增长为15，那您本年的年化预期为90，即
初始资金的1.9倍。
（3）胜率
即您所有投资中：收益为正的次数/您投资的总次数，投资收益为正的次数包括您仍在持有的股票，投资的总次数也包括您仍然持有的股票。
（4）动态R
即您所有投资中：总共获利/总共损失，总共获利包括您仍在持有股票的获利，总共损失包括您仍在持有股票的损失。
（5）仓位率
即您所有投资中：获利的总仓位/损失的总仓位，例如您100000的资金投资了3只股票，其中A5万，仓位为50,B3万，仓位30，C2万，仓位20，其中A和C亏损，
B盈利，您的仓位率为30/(50+20)
（6）平均收益率
即您所有投资中：收益率之和/投资的次数，收益率可为正负。
（7）平均盈利收益率
即您所有投资中：盈利收益率之和/盈利投资的次数。
（8）平均亏损收益率
即您所有投资中：亏损收益率之和/亏损投资的次数。









</pre>



</body>