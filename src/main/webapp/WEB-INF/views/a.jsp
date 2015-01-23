<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>ECharts</title>
<script src="/resources/js/jquery.js"></script>
</head>
<body>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	<div id="value_div" style="height: 400px"></div>
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
			var value = ec.init(document.getElementById('value_div'));
			var r = ec.init(document.getElementById('r_div'));
			valueOption = {
				title : {
					text : '基准'
				},
				tooltip : {
					trigger : 'axis'
				},
				legend : {
					data : [ '双子', '上海', '深圳' ]
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
					name : '双子',
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
				}]
			};
			
			rOption = {
					title : {
						text : 'R'
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : [ 'R', 'V' ]
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
						name : 'R',
						type : 'line',
						color: 'red',
						data : [ 11, 11, 15, 13, 12, 13, 10 ]
					}, {
						name : 'V',
						type : 'line',
						color: 'yellow',
						data : [ 1, -2, 2, 5, 3, 2, 0 ]
					} ]
				};

			//通过Ajax获取数据
			$.ajax({
				type : "get",
				async : false, //同步执行
				url : "/",
				dataType : "json", //返回数据形式为json
				success : function(result) {
					if (result) {
						//将返回的category和series对象赋值给options对象内的category和series
						//因为xAxis是一个数组 这里需要是xAxis[i]的形式
						//坐标
						valueOption.xAxis[0].data = result.series;
						//数据
						valueOption.series[0].data = result.mine;
						valueOption.series[1].data = result.sh;
						valueOption.series[2].data = result.sz;

						//option.legend.data = result.legend;
						
						value.hideLoading();
						value.setOption(valueOption);
					}
				},
				error : function(errorMsg) {
				}
			});
		});
	</script>
</body>