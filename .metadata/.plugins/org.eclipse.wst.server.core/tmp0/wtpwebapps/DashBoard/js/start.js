var myChart;
var myChart_Percent;
var myChart_In7Days;

myChart = echarts.init(document.getElementById('start'));
option1 = {
	title : {
		text : '',
		subtext : ''
	},
	tooltip : {
		trigger : 'axis'
	},
	legend : {
		data : [ '开机量', '乘客数' ]
	},
	toolbox : {
		show : true,
		feature : {
			mark : {
				show : false
			},
			dataView : {
				show : false,
				readOnly : false
			},
			magicType : {
				show : false,
				type : [ 'line', 'bar', 'stack', 'tiled' ]
			},
			restore : {
				show : false
			},
			saveAsImage : {
				show : false
			}
		}
	},
	calculable : false,
	xAxis : [ {
		type : 'category',
		boundaryGap : false,
		data : [ '' ]
	} ],
	yAxis : [ {
		name : '乘客数',
		type : 'value',
		max : 200,
	} ],
	series : [ {
		name : '开机量',
		type : 'line',
		smooth : true,
		itemStyle : {
			normal : {
				areaStyle : {
					type : 'default'
				}
			}
		},
		data : [ 0 ]
	}, {
		name : '乘客数',
		type : 'line',
		smooth : true,
		itemStyle : {
			normal : {
				areaStyle : {
					type : 'default'
				}
			}
		},
		data : [ 0 ]
	}

	]
};
myChart.setOption(option1);

myChart_Percent = echarts.init(document.getElementById('startPercent'));
option2 = {
	title : {
		text : '',
		subtext : ''
	},
	tooltip : {
		trigger : 'axis'
	},
	legend : {
		data : [ 'PAD总数(%)', '开机率(%)' ]
	},
	toolbox : {
		show : true,
		feature : {
			mark : {
				show : false
			},
			dataView : {
				show : false,
				readOnly : false
			},
			magicType : {
				show : false,
				type : [ 'line', 'bar' ]
			},
			restore : {
				show : false
			},
			saveAsImage : {
				show : false
			}
		}
	},
	calculable : false,

	xAxis : [ {
		type : 'category',
		data : [ '' ]
	}, {
		type : 'category',
		axisLine : {
			show : false
		},
		axisTick : {
			show : false
		},
		axisLabel : {
			show : false
		},
		splitArea : {
			show : false
		},
		splitLine : {
			show : false
		},
		data : [ '' ]
	} ],
	yAxis : [ {
		type : 'value',
		axisLabel : {
			formatter : '{value} %'
		}
	} ],
	series : [ {
		name : 'PAD总数(%)',
		type : 'bar',
		xAxisIndex : 1,
		itemStyle : {
			normal : {
				color : 'rgba(212,212,212,1)',
				label : {
					show : true
				}
			}
		},
		data : [ 100 ]

	}, {
		name : '开机率(%)',
		type : 'bar',
		itemStyle : {
			normal : {
				color : 'rgba(0,185,217,1)',
				label : {
					show : true
				}
			}
		},
		data : [ 0 ]
	} ]
};
myChart_Percent.setOption(option2);

myChart_In7Days = echarts.init(document.getElementById('startPercentIn7Days'));
option3 = {
	title : {
		text : '',
		subtext : ''
	},
	tooltip : {
		trigger : 'axis'
	},
	legend : {
		data : [ 'PAD总数(%)', '开机率(%)' ]
	},
	toolbox : {
		show : true,
		feature : {
			mark : {
				show : false
			},
			dataView : {
				show : false,
				readOnly : false
			},
			magicType : {
				show : false,
				type : [ 'line', 'bar' ]
			},
			restore : {
				show : false
			},
			saveAsImage : {
				show : false
			}
		}
	},
	calculable : false,

	xAxis : [ {
		type : 'category',
		data : [ '' ]
	}, {
		type : 'category',
		axisLine : {
			show : false
		},
		axisTick : {
			show : false
		},
		axisLabel : {
			show : false
		},
		splitArea : {
			show : false
		},
		splitLine : {
			show : false
		},
		data : [ '' ]
	} ],
	yAxis : [ {
		type : 'value',
		axisLabel : {
			formatter : '{value} %'
		}
	} ],
	series : [ {
		name : 'PAD总数(%)',
		type : 'bar',
		xAxisIndex : 1,
		itemStyle : {
			normal : {
				color : 'rgba(212,212,212,1)',
				label : {
					show : true
				}
			}
		},
		data : [ 100 ]

	}, {
		name : '开机率(%)',
		type : 'bar',
		itemStyle : {
			normal : {
				color : 'rgba(225,100,0,1)',
				label : {
					show : true
				}
			}
		},
		data : [ 0 ]
	} ]
};
myChart_In7Days.setOption(option3);

// 自适应设置
window.onresize = function() {
	myChart.resize();
	myChart_Percent.resize();
	myChart_In7Days.resize();
};

$(document).ready(function() {
	refresh();
	refreshDownload();
	getPlaneList(getData);
});

var currentPlane;
var currentDay;
var flights = new Array();
var values = new Array();
var passngers = new Array();
var ratios = new Array();
var percents = new Array();
var per = new Array();
var rat = new Array();
var days = new Array();
var startDay;
var endDay;
var numDay = 0;
function changePlane(plane) {
	if (plane != currentPlane) {
		currentPlane = plane;
		getData();
		getPercentageData();
	}
}

function changeDay(day) {
	day.hour(0).minute(0).seconds(0).milliseconds(0);
	if (currentDay != day) {
		currentDay = day;
		getData();
	}
}

function setDays() {
	numDay = endDay.diff(startDay, 'day');
	days = [];
	for (var i = 0; i <= numDay; i++) {
		var t = new moment(startDay);
		t.add(i, 'days');
		days.push(t.format('YYYY-MM-DD'));
	}
}

function refresh() {
	currentDay = new moment({
		hour : 0,
		minute : 0,
		seconds : 0,
		milliseconds : 0
	});
	endDay = new moment(currentDay);
	startDay = (new moment(currentDay)).subtract(6, 'days');

	$('#datetimepicker').datetimepicker('setDate', currentDay.toDate());
	$('#datetimepicker').datetimepicker().on('changeDate', function(ev) {
		changeDay(new moment(ev.date.valueOf()));
	});
	$('#reservation')
			.val(
					startDay.format('MM/DD/YYYY') + " - "
							+ endDay.format('MM/DD/YYYY'));
	$('#reservation').daterangepicker({
		"dateLimit" : {
			"days" : 6
		}
	}, function(start, end, label) {
		startDay = start.hour(0).minute(0).seconds(0).milliseconds(0);
		endDay = end.hour(0).minute(0).seconds(0).milliseconds(0);
		setDays();
		getPercentageData();
	});

	setDays();
	getPlaneList(changePlane);
	getData();
	getPercentageData();
}
function refreshDownload() {
	currentDay = new moment({
		hour : 0,
		minute : 0,
		seconds : 0,
		milliseconds : 0
	});
	endDay = new moment(currentDay);
	startDay = (new moment(currentDay)).subtract(30, 'days');
	$('#start_TimePickerDownloader')
			.val(
					startDay.format('MM/DD/YYYY') + " - "
							+ endDay.format('MM/DD/YYYY'));
	$('#start_TimePickerDownloader').daterangepicker(
			{
				"dateLimit" : {
					"days" : 30
				}
			},
			function(start, end, label) {
				DownloaderStartDay = start.hour(0).minute(0).seconds(0)
						.milliseconds(0);
				DownloaderEndDay = end.hour(0).minute(0).seconds(0)
						.milliseconds(0);
				$.get("/DashBoard/usage/getpadusageinfo", {
					org : "spring",
					plane : currentPlane,
					start : DownloaderStartDay.valueOf(),
					end : DownloaderEndDay.valueOf()
				}, function(ret) {
					DownData = ret
				});
			});
}
function getData() {
	if (currentPlane == null || currentDay == null) {
		return;
	}
	myChart.clear();
	myChart.showLoading({
		text : '读取数据中...',
	});
	myChart_Percent.clear();
	myChart_Percent.showLoading({
		text : '读取数据中...',
	});
	$.get("/DashBoard/usage/getpadusageinfo", {
		org : "spring",
		plane : currentPlane,
		start : currentDay.valueOf(),
		end : (currentDay.valueOf() + 1000 * 60 * 60 * 24 - 1)
	}, function(ret) {
		flights = [];
		values = [];
		passengers = [];
		ratios = [];
		percents = [];
		for (var i = 0; i < ret.length; i++) {
              flights.push(ret[i].flight_no);
			values.push(ret[i].value);
			passengers.push(ret[i].passenger_no);
			if (ret[i].passenger_no != 0) {
				ratios.push((ret[i].value / ret[i].passenger_no * 100)
						.toFixed(2));
			} else {
				ratios.push(0);
			}
			percents.push(100);
		}
		if (ret.length == 0) {
			flights = [ '' ];
			values = [ 0 ];
			passengers = [ 0 ];
			ratios = [ 0 ];
			percents = [ 100 ];
		}
		drawTable1();
		drawTable2();
	});
}

function drawTable1() {
	option1.xAxis = [ {
		type : 'category',
		boundaryGap : false,
		data : flights
	} ];
	option1.series[0].data = values;
	option1.series[1].data = passengers;
	myChart.setOption(option1);

	myChart.hideLoading();
}

function drawTable2() {
	option2.xAxis = [ {
		type : 'category',
		data : flights
	}, {
		type : 'category',
		axisLine : {
			show : false
		},
		axisTick : {
			show : false
		},
		axisLabel : {
			show : false
		},
		splitArea : {
			show : false
		},
		splitLine : {
			show : false
		},
		data : flights
	} ];
	option2.series[0].data = percents;
	option2.series[1].data = ratios;
	myChart_Percent.setOption(option2);
	myChart_Percent.hideLoading();
}

function DataPerDay() {
	this.value = 0;
	this.count = 0;
}

var processedData = new Array();

function getPercentageData() {
	if (startDay == null || endDay == null || currentPlane == null) {
		return;
	}

	myChart_In7Days.clear();
	myChart_In7Days.showLoading({
		text : '读取数据中...',
	});
	$.get("/DashBoard/usage/getpadusageinfo", {
		org : "spring",
		plane : currentPlane,
		start : startDay.valueOf(),
		end : ((new moment(endDay)).hour(23).minute(59).seconds(59)
				.milliseconds(999)).valueOf()
	},
			function(ret) {
				per = [];
				rat = [];
				processedData = [];
				for (var j = 0; j <= numDay; j++) {
					per.push(100);
					rat.push(0);
					processedData.push(new DataPerDay());
				}
				for (var i = 0; i < ret.length; i++) {
					var v = ret[i].value;
					var c = ret[i].passenger_no;
					var d = (new moment(ret[i].flight_date)).hour(0).minute(0)
							.seconds(0).milliseconds(0);
					var k = d.diff(startDay, 'day');
					if (k < processedData.length && k >= 0) {
						processedData[k].value += v;
						processedData[k].count += c;
					}
				}
				for (var z = 0; z < processedData.length; z++) {
					if (processedData[z].count != 0) {
						rat[z] = (processedData[z].value
								/ processedData[z].count * 100).toFixed(2);
					} else {
						per[z] = 0;
					}
				}
				drawTable3();
			});
}

function drawTable3() {
	option3.xAxis = [ {
		type : 'category',
		data : days
	}, {
		type : 'category',
		axisLine : {
			show : false
		},
		axisTick : {
			show : false
		},
		axisLabel : {
			show : false
		},
		splitArea : {
			show : false
		},
		splitLine : {
			show : false
		},
		data : days
	} ];
	option3.series[0].data = per;
	option3.series[1].data = rat;
	myChart_In7Days.setOption(option3);
	myChart_In7Days.hideLoading();
}
function exportToFile() {
	exportTableToCSV($('#steward_attendant_table'),
			$('#start_passenger_employ'), '乘客使用统计报表.csv');
}
function exportJsonFile() {
	JSONToCSVConvertor(DownData, "乘客使用统计报表", true);
}
