<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>ECharts</title>
<script src="/resources/js/jquery.js"></script>
</head>
<body>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	<div id="sRate_div" style="height: 400px"></div>
	<div id="r_div" style="height: 400px"></div>
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
			var r = ec.init(document.getElementById('r_div'));
			var sRate = ec.init(document.getElementById('sRate_div'));

			
			rOption = {
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
						text : '动态S'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : [ '动态S' ]
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
			

			//通过Ajax获取数据
			$.ajax({
				type : "get",
				async : false, //同步执行
				url : "/bb.json",
				dataType : "json", //返回数据形式为json
				success : function(result) {
					if (result) {
						//将返回的category和series对象赋值给options对象内的category和series
						//因为xAxis是一个数组 这里需要是xAxis[i]的形式
						//坐标
						rOption.xAxis[0].data = result.series;
						sRateOption.xAxis[0].data = result.series;

						
						sRateOption.series[0].data = result.sRate;
						rOption.series[0].data = result.r;

						//option.legend.data = result.legend;
										
												
						r.hideLoading();
						r.setOption(rOption);
						
						sRate.hideLoading();
						sRate.setOption(sRateOption);
						
					}
				},
				error : function(errorMsg) {
				}
			});
		});
	</script>
</body>