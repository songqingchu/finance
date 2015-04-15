<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="common/head.jsp"%> 
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>见证你在股市中成长的每一步</title>
<script src="/resources/js/jquery.js"></script> 
</head>
<body>


<c:if test="${exist==false}">
    您还没有添加盯市记录
</c:if>
 
<c:if test="${exist==true}">
    <a id="top_a" href="#yearVRate_div_a">年化</a>&nbsp;&nbsp;
    <a href="#rRate_div_a">动态R</a>&nbsp;&nbsp;
    <a href="#sRate_div_a">胜率</a>&nbsp;&nbsp;
    <a href="#pRate_div_a">仓率</a>&nbsp;&nbsp;
    <a href="#yRate_div_a">平均盈利率</a>&nbsp;&nbsp;
    
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	
	<div id="vRate_div" style="height: 400px"></div>
	
	<a id="yearVRate_div_a" href="#head_div">回到顶部</a>
	<div id="yearVRate_div" style="height: 400px"></div>
	
	<a id="sRate_div_a" href="#head_div">回到顶部</a>
	<div id="sRate_div" style="height: 400px"></div>
	
	<a id="rRate_div_a" href="#head_div">回到顶部</a>
	<div id="rRate_div" style="height: 400px"></div>
	
	<a id="pRate_div_a" href="#head_div">回到顶部</a>
	<div id="pRate_div" style="height: 400px"></div>
	
	<a id="yRate_div_a" href="#top_a">回到顶部</a>
	<div id="yRate_div" style="height: 400px"></div>
	
	
	
	<!-- ECharts单文件引入 -->
	<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
	<script type="text/javascript">
		// 路径配置
		require.config({
			paths : {
				echarts : 'http://echarts.baidu.com/build/dist'
			}
		});

		// 使用
		require([ 'echarts', 'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
		], function(ec) {
			// 基于准备好的dom，初始化echarts图表
			var vRate = ec.init(document.getElementById('vRate_div'));
			var yearVRate = ec.init(document.getElementById('yearVRate_div'));
			var rRate = ec.init(document.getElementById('rRate_div'));
			var pRate = ec.init(document.getElementById('pRate_div'));
			var sRate = ec.init(document.getElementById('sRate_div'));
			var yRate = ec.init(document.getElementById('yRate_div'));
			//var yyRate = ec.init(document.getElementById('yyRate_div'));

			
			vRateOption = {
				title : {
					text : '增长率'
				},
				tooltip : {
					trigger : 'axis'
				},
				legend : {
					data : [ '增长率', '上海', '深圳','创业板','中小板' ]
				},
				toolbox : {
					show : true,
					feature : {
						mark : {
							show : true
						},
						dataView : {
							show : true,
							readOnly : false
						},
						magicType : {
							show : true,
							type : [ 'line', 'bar' ]
						},
						restore : {
							show : true
						},
						saveAsImage : {
							show : true
						}
					}
				},
				calculable : true,
				xAxis : [ {
					type : 'category',
					boundaryGap : false,
					data : [ ]
				} ],
				yAxis : [ {
					type : 'value',
					precision: 2, 
					axisLabel : {
						formatter : '{value}'
					}
				} ],
				series : [ {
					name : '增长率',
					type : 'line',
					color: 'red',
					data : [ 11, 11, 15, 13, 12, 13, 10 ]
				}, {
					name : '上海',
					type : 'line',
					color: 'yellow',
					data : [ 1, -2, 2, 5, 3, 2, 0 ]
				} , {
					name : '深圳',
					type : 'line',
					color: 'blue',
					data : [ 1, -2, 2, 5, 3, 2, 0 ]
				}, {
					name : '创业板',
					type : 'line',
					color: 'blue',
					data : [ 1, -2, 2, 5, 3, 2, 0 ]
				}, {
					name : '中小板',
					type : 'line',
					color: 'blue',
					data : [ 1, -2, 2, 5, 3, 2, 0 ]
				}]
			};
			
			
			yearVRateOption = {
					title : {
						text : '预期年化'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : [ '预期年化' ]
					},
					toolbox : {
						show : true,
						feature : {
							mark : {
								show : true
							},
							dataView : {
								show : true,
								readOnly : false
							},
							magicType : {
								show : true,
								type : [ 'line', 'bar' ]
							},
							restore : {
								show : true
							},
							saveAsImage : {
								show : true
							}
						}
					},
					calculable : true,
					xAxis : [ {
						type : 'category',
						boundaryGap : false,
						data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
					} ],
					yAxis : [ {
						type : 'value',
						 precision: 2, 
						axisLabel : {
							formatter : '{value}'
						}
					} ],
					series : [ {
						name : '年化',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					}]
				};
			
			
			
			rRateOption = {
					title : {
						text : '动态R'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : [ '动态R' ]
					},
					toolbox : {
						show : true,
						feature : {
							mark : {
								show : true
							},
							dataView : {
								show : true,
								readOnly : false
							},
							magicType : {
								show : true,
								type : [ 'line', 'bar' ]
							},
							restore : {
								show : true
							},
							saveAsImage : {
								show : true
							}
						}
					},
					calculable : true,
					xAxis : [ {
						type : 'category',
						boundaryGap : false,
						data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
					} ],
					yAxis : [ {
						type : 'value',
						axisLabel : {
							formatter : '{value}'
						}
					} ],
					series : [ {
						name : '动态R',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					}]
				};
			
			
			
			
			sRateOption = {
					title : {
						text : '胜率'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : [ '胜率' ]
					},
					toolbox : {
						show : true,
						feature : {
							mark : {
								show : true
							},
							dataView : {
								show : true,
								readOnly : false
							},
							magicType : {
								show : true,
								type : [ 'line', 'bar' ]
							},
							restore : {
								show : true
							},
							saveAsImage : {
								show : true
							}
						}
					},
					calculable : true,
					xAxis : [ {
						type : 'category',
						boundaryGap : false,
						data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
					} ],
					yAxis : [ {
						type : 'value',
						axisLabel : {
							formatter : '{value}'
						}
					} ],
					series : [ {
						name : '胜率',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					}]
				};
			
			pRateOption = {
					title : {
						text : '仓位率'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : [ '仓位率' ]
					},
					toolbox : {
						show : true,
						feature : {
							mark : {
								show : true
							},
							dataView : {
								show : true,
								readOnly : false
							},
							magicType : {
								show : true,
								type : [ 'line', 'bar' ]
							},
							restore : {
								show : true
							},
							saveAsImage : {
								show : true
							}
						}
					},
					calculable : true,
					xAxis : [ {
						type : 'category',
						boundaryGap : false,
						data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
					} ],
					yAxis : [ {
						type : 'value',
						axisLabel : {
							formatter : '{value}'
						}
					} ],
					series : [ {
						name : '仓位率',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					}]
				};
			
			
			yRateOption = {
					title : {
						text : '平均盈利率'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : [ '平均盈利率' ,'平均盈利率（胜）','平均盈利率（损）' ]
					},
					toolbox : {
						show : true,
						feature : {
							mark : {
								show : true
							},
							dataView : {
								show : true,
								readOnly : false
							},
							magicType : {
								show : true,
								type : [ 'line', 'bar' ]
							},
							restore : {
								show : true
							},
							saveAsImage : {
								show : true
							}
						}
					},
					calculable : true,
					xAxis : [ {
						type : 'category',
						boundaryGap : false,
						data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
					} ],
					yAxis : [ {
						type : 'value',
						axisLabel : {
							formatter : '{value}'
						}
					} ],
					series : [ {
						name : '平均盈利率',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					},{
						name : '平均盈利率（胜）',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					},{
						name : '平均盈利率（损）',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					}]
				};
			
			
			yyRateOption = {
					title : {
						text : '平均盈利率（胜）'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : [ '平均盈利率（胜）','平均盈利率（损）' ]
					},
					toolbox : {
						show : true,
						feature : {
							mark : {
								show : true
							},
							dataView : {
								show : true,
								readOnly : false
							},
							magicType : {
								show : true,
								type : [ 'line', 'bar' ]
							},
							restore : {
								show : true
							},
							saveAsImage : {
								show : true
							}
						}
					},
					calculable : true,
					xAxis : [ {
						type : 'category',
						boundaryGap : false,
						data : [ '周一', '周二', '周三', '周四', '周五', '周六', '周日' ]
					} ],
					yAxis : [ {
						type : 'value',
						axisLabel : {
							formatter : '{value}'
						}
					} ],
					series : [ {
						name : '平均盈利率（胜）',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					},{
						name : '平均盈利率（损）',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					}]
				};
			
			
		
			//通过Ajax获取数据
			$.ajax({
				type : "get",
				async : false, //同步执行
				url : "/statsData.do",
				dataType : "json", //返回数据形式为json
				success : function(result) {
					if (result) {
						//将返回的category和series对象赋值给options对象内的category和series
						//因为xAxis是一个数组 这里需要是xAxis[i]的形式
						//坐标
						vRateOption.xAxis[0].data = result.series;
						yearVRateOption.xAxis[0].data = result.series;
						sRateOption.xAxis[0].data = result.series;
						rRateOption.xAxis[0].data = result.series;
						pRateOption.xAxis[0].data = result.series;
						yRateOption.xAxis[0].data = result.series;
						//yyRateOption.xAxis[0].data = result.series;
						
						//数据
						vRateOption.series[0].data = toFix(result.mine);
						vRateOption.series[1].data = toFix(result.sh);
						vRateOption.series[2].data = toFix(result.sz);
						vRateOption.series[3].data = toFix(result.ch);
						vRateOption.series[4].data = toFix(result.zh);
						yearVRateOption.series[0].data = toFix(result.year);
						sRateOption.series[0].data = toFix(result.sRate);
						rRateOption.series[0].data = toFix(result.rRate);
						pRateOption.series[0].data = toFix(result.pRate);
						yRateOption.series[0].data = toFix(result.yRate);
						yRateOption.series[1].data = toFix(result.yyRate);
						yRateOption.series[2].data = toFix(result.ysRate);
						//option.legend.data = result.legend;
										
						vRate.hideLoading();
						vRate.setOption(vRateOption);
							
						yearVRate.hideLoading();
						yearVRate.setOption(yearVRateOption);
						
						rRate.hideLoading();
						rRate.setOption(rRateOption);
						
						sRate.hideLoading();
						sRate.setOption(sRateOption);
						
						pRate.hideLoading();
						pRate.setOption(pRateOption);			
						
						yRate.hideLoading();
						yRate.setOption(yRateOption);
						
						//yyRate.hideLoading();
						//yyRate.setOption(yyRateOption);
						
					}
				},
				error : function(errorMsg) {
				}
			});
		});
		
		function toFix(array){
			var r=[];
			for(var i=0;i<array.length;i++){
				r.push(array[i].toFixed(2));
			}
			return r;
		}
	</script>
</c:if>
   
<jsp:include page="common/foot.jsp" flush="true"/>
</body>