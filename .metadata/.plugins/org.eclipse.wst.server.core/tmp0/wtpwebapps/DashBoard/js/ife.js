//获取ife信息

var start_time ;
var end_time ;

var cpuData = new Array();
var memData = new Array();
var diskData = new Array();
var disk_total;

var systemStatusTime = new Array();

var altitudeData = new Array();
var paData = new Array();
var gateData = new Array();
var updownData = new Array();

var planeInfoTime = new Array();
var paTime = new Array();
var gateTime = new Array();
var updownTime = new Array();
var heartTime = new Array();

var PLANE_STATUS = 0x1;
var PLANE_INFO = 0x2;
var HEART = 0x4;
var UP_DOWN = 0x8;
var FLAG_ALL = 0xF;
var currentFlag = 0;

var myChart;
var myChart_CPU;
var myChart_Memory;
var myChart_HardDisk;

$(function() {
	getPlaneList(drawIFE);
	//设置自适应
	window.onresize = function () {
	    myChart.resize();
	    myChart_CPU.resize();
	    myChart_Memory.resize();
	    myChart_HardDisk.resize();
	};
});

function drawIFE(planeNo) {

    currentDay = new moment({hour: 0, minute: 0, seconds: 0, milliseconds: 0});
    end_time = new moment(currentDay);
    start_time = (new moment(currentDay)).subtract(1, 'days');

    $('#ifereservation').val(start_time.format('MM/DD/YYYY')+" - "+end_time.format('MM/DD/YYYY'));
    $('#ifereservation').daterangepicker({
        "dateLimit": {
            "days": 7
        }
    }, function(start, end, label) {
    	start_time = start.hour(0).minute(0).seconds(0).milliseconds(0);
    	end_time = end.hour(0).minute(0).seconds(0).milliseconds(0);
    	prepareData(planeNo);
    });
    
    prepareData(planeNo);
}

function prepareData(planeNo) {
	hideAllLoading();
	showAllLoadingView();
	clearData();
	getSystemStatus(planeNo);
	getUpDownInfo(planeNo);
	getPlanestatus(planeNo);
	getHeartData(planeNo);
	getPlaneInfo(planeNo);
}

function showAllLoadingView() {
	showLoading($("#main"), "main-loading");
	showLoading($("#cpu"), "cpu-loading");
	showLoading($("#memory"), "memory-loading");
	showLoading($("#hardisk"), "hardisk-loading");
}

function clearData(){
	cpuData = [];
	memData = [];
    diskData = [];
	disk_total = 0;

	systemStatusTime = [];

	altitudeData = [];
	paData = [];
	gateData = [];
	updownData = [];

	planeInfoTime = [];
	paTime = [];
	gateTime = [];
	updownTime = [];
	heartTime = [];

	currentFlag = 0;
}

function getSystemStatus(planeNo) {
	// 获取系统信息
	$.get("/DashBoard/status/getsystemstatus", {
		org : "spring",
		plane : planeNo,
		start : start_time.valueOf(),
		end : end_time.valueOf(),
		optionalParams : null
	}, function(data, textStatus) {

		$.each(data, function(index, item) {
			var ts = item.ts;
			//若超过10分钟没有数据，则在无数据期间首尾各加一个0
			if ((ts - systemStatusTime[systemStatusTime.length - 1]) > 10 * 60 * 1000) {
				systemStatusTime.push(systemStatusTime[systemStatusTime.length - 1]);
				cpuData.push(0);
				memData.push(0);
				diskData.push(diskData[diskData.length - 1]);
				systemStatusTime.push(ts);
				cpuData.push(0);
				memData.push(0);
				diskData.push(diskData[diskData.length - 1]);
			}
			
			//加入有效数据
			systemStatusTime.push(ts);
			var cpu_used = (100 * item.data.ife_CPUUsage).toFixed(2);
			cpuData.push(cpu_used);
			var mem_used = (100 * item.data.ife_MemUsage).toFixed(2);
			memData.push(mem_used);
			// 计算硬盘使用率
			var disk_str = item.data.ife_DiskUsage;
			var total_str = disk_str.substr(disk_str.indexOf("fldata:total:") + 13);
			disk_total = parseFloat(total_str);
			var used_str = total_str.substr(total_str.indexOf("used:") + 5);
			var used = parseFloat(used_str);
			var disk_usage = (100 * used / disk_total).toFixed(2);
			diskData.push(disk_usage);
			
		});
		
		// 初始化cpu、memory、disk视图
		initCPU();
		initMemory();
		initHardDisk();
	});

}

// 获取开关机信息
function getUpDownInfo(planeNo) {
	$.get("/DashBoard/status/getifeupdowninfo", {
		org : "spring",
		plane : planeNo,
		start : start_time.valueOf(),
		end : end_time.valueOf(),
		optionalParams : null
	}, function(data, textStatus) {
		$.each(data, function(index, item) {
			// 开机设为2
			// 画矩形
			if (item.status == 0) {
				// 为0，先加一个1
				updownTime.push(item.ts);
				updownData.push(1);
				updownTime.push(item.ts);
				updownData.push(0);
			} else {
				// 不为0，先加一个0
				updownTime.push(item.ts);
				updownData.push(0);
				updownTime.push(item.ts);
				updownData.push(1);
			}
		});
		
		currentFlag |= UP_DOWN;
		if (currentFlag == FLAG_ALL) {
			initMain();
		}

	});
}

// 获取飞机状态信息
function getPlanestatus(planeNo) {

	$.get("/DashBoard/status/getplanestatus", {
		org : "spring",
		plane : planeNo,
		start : start_time.valueOf(),
		end : end_time.valueOf(),
		optionalParams : null
	}, function(data, textStatus) {
		$.each(data, function(index, item) {
			var ts = item.ts;
			
			//加入有效数据
			// at_gate为true时设为2
			if (item.data.at_gate) {
				if (gateData[gateData.length - 1] == 0) {
					//从0到2，前面加一个0
					gateTime.push(ts);
					gateData.push(0);
				}
			    gateTime.push(ts);
				gateData.push(2);
			} else {
				if (gateData[gateData.length - 1] == 2) {
					//从2到0，前面加一个2
					gateTime.push(ts);
					gateData.push(2);
				}
				gateTime.push(ts);
				gateData.push(0);
			}
			
			// passenger_announcement为true时设为3
			if (item.data.passenger_announcement) {
				if (paData[paData.length - 1] == 0) {
					//从0到3，前面加一个0
					paTime.push(ts);
					paData.push(0);
				}
				paTime.push(ts);
				paData.push(3);
			} else {
				if (paData[paData.length - 1] == 3) {
					//从3到0，前面加一个3
					paTime.push(ts);
					paData.push(3);
				}
				paTime.push(ts);
				paData.push(0);
			}
		});
		
		currentFlag |= PLANE_STATUS;
		if (currentFlag == FLAG_ALL) {
			initMain();
		}

	});
}

// 获取心跳信息
function getHeartData(planeNo) {

	$.get("/DashBoard/status/getheartbeatrecord", {
		org : "spring",
		plane : planeNo,
		start : start_time.valueOf(),
		end : end_time.valueOf(),
		optionalParams : null
	}, function(data, textStatus) {
		$.each(data, function(index, item) {
			heartTime.push(item.receiveTime);
		});
		
		currentFlag |= HEART;
		if (currentFlag == FLAG_ALL) {
			initMain();
		}
	});
}

// 获取飞机信息
function getPlaneInfo(planeNo) {

	$.get("/DashBoard/status/getplaneinfo", {
		org : "spring",
		plane : planeNo,
		start : start_time.valueOf(),
		end : end_time.valueOf(),
		optionalParams : null
	}, function(data, textStatus) {

		$.each(data, function(index, item) {
			if(item.data.altitude) {
				var ts = item.ts;
				//若超过10分钟没有数据，则在无数据期间首尾各加一个0
				if ((ts - planeInfoTime[planeInfoTime.length - 1]) > 1000 * 60 * 10) {
					planeInfoTime.push(planeInfoTime[planeInfoTime.length - 1]);
					altitudeData.push(0);
					planeInfoTime.push(ts);
					altitudeData.push(0);
				}
				
				// 飞机信息
				planeInfoTime.push(ts);
				//单位：英尺
				altitudeData.push(Math.round(parseInt(item.data.altitude) * 3.2808399));
			}
		});
		
		currentFlag |= PLANE_INFO;
		if (currentFlag == FLAG_ALL) {
			initMain();
		}
	});
}

function initMain() {
	hideLoading($("#main-loading"));
	myChart = echarts.init(document.getElementById('main'));
	myChart.clear();
	option = {
		title : {
			text : '',
			subtext : '',
			x : 'center'
		},
		tooltip : {
			trigger : 'item',
			formatter : function(params) {
				var tip = formatTime(Date.parse(params.value[0]));
				if (params.seriesName == '飞行高度') {
					tip += '<br/>' + params.seriesName + ' : ' + params.value[1] + " 英尺";
				} else {
					tip += '<br/>' + params.seriesName + ' : ' + -params.value[1];
				}
				return tip;
			}
		},
		legend : {
			data : [ '飞行高度', '心跳', 'PA', '开关机', '开关舱门' ],
			x : 'left'
		},
		toolbox : {
			show : true,
			feature : {
				mark : {
					show : false
				},
				dataView : {
					show : false,
					readOnly : true
				},
				magicType : {
					show : false,
					type : [ 'line', 'bar', ]
				},
				restore : {
					show : false
				},
				saveAsImage : {
					show : false
				}
			}
		},
		dataZoom : {
			show : true,
			realtime : true,
			start : 0,
			end : 100
		},
		
		grid : {
			y2 : 80
		},
		xAxis : [ {
			type : 'time',
			scale : true,
			splitNumber : 10
		} ],
		yAxis : [ {
			name : '飞行高度(英尺)',
			type : 'value',
			max : 200000
		}, {
			name : '心跳/PA/开关机/开关舱门',
			type : 'value',
			axisLabel : {
				formatter : function(v) {
					return -v;
				}
			}
		} ],
		series : [ {
			name : '飞行高度',
			type : 'line',
			showAllSymbol : true,
			symbolSize : function(value) {
				return 0;
			},
			data : (function() {
				var data = [];
				var index = 0;
				while (index < planeInfoTime.length) {
					data.push([ new Date(planeInfoTime[index]), altitudeData[index] ]);
					index++;
				}
				return data;
			})()

		}, {
			name : '心跳',
			type : 'scatter',
			yAxisIndex : 1,
			showAllSymbol : true,
			symbolSize : function(value) {
				return 3;
			},
			data : (function() {
				var oriData = [];
				var index = 0;
				while (index < heartTime.length) {
					oriData.push([ new Date(heartTime[index]), -4 ]);
					index++;
				}
				return oriData;
			})()
		}, {
			name : 'PA',
			type : 'line',
			showAllSymbol : true,
			symbolSize : function(value) {
				return 0;
			},
			yAxisIndex : 1,
			data : (function() {
				var oriData = [];
				var index = 0;
				while (index < paTime.length) {
					oriData.push([ new Date(paTime[index]), -paData[index] ]);
					index++;
				}
				return oriData;
			})()
		}, {
			name : '开关机',
			type : 'line',
			showAllSymbol : true,
			symbolSize : function(value) {
				return 0;
			},
			yAxisIndex : 1,
			itemStyle : {
				normal : {
					areaStyle : {
						type : 'default'
					}
				}
			},
			data : (function() {
				var oriData = [];
				var index = 0;
				while (index < updownTime.length) {
					oriData.push([ new Date(updownTime[index]), -updownData[index] ]);
					index++;
				}
				return oriData;
			})()
		}, {
			name : '开关舱门',
			type : 'line',
			showAllSymbol : true,
			symbolSize : function(value) {
				return 0;
			},
			yAxisIndex : 1,
			data : (function() {
				var oriData = [];
				var index = 0;
				while (index < gateTime.length) {
					oriData.push([ new Date(gateTime[index]), -gateData[index] ]);
					index++;
				}
				return oriData;
			})()
		}]
		
	};
myChart.setOption(option);
}

function initCPU() {
	hideLoading($("#cpu-loading"));
	myChart_CPU = echarts.init(document.getElementById('cpu'));
	myChart_CPU.clear();
	option = {
		tooltip : {
			trigger : 'item',
			formatter : function(params) {
				return formatTime(Date.parse(params.value[0])) + '<br/>'
						+ params.seriesName + '：' + params.value[1] + '%';
			}
		},
		legend : {
			data : [ 'CPU使用率' ]
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
		dataZoom : {
			show : true,
			realtime : true,
			start : 0,
			end : 100
		},

		grid : {
			y2 : 80
		},
		xAxis : [ {
			type : 'time',
			splitNumber : 10
		} ],
		yAxis : [ {
			name : 'CPU使用率(%)',
			type : 'value',
		} ],
		series : [ {
			name : 'CPU使用率',
			type : 'line',
			showAllSymbol : true,
			symbolSize : function(value) {
				return 0;
			},
			
			data : (function() {
				var data = [];
				var index = 0;
				while (index < cpuData.length) {
					data.push([ new Date(systemStatusTime[index]), cpuData[index] ]);
					index++;
				}
				return data;
			})()

		} ]
	};

myChart_CPU.setOption(option);
}

function initMemory() {
	hideLoading($("#memory-loading"));
	myChart_Memory = echarts.init(document.getElementById('memory'));
	myChart_Memory.clear();
	option = {
		tooltip : {
			trigger : 'item',
			formatter : function(params) {
				return formatTime(Date.parse(params.value[0])) + '<br/>'
						+ params.seriesName + '：' + params.value[1] + '%';
			}
		},
		legend : {
			data : [ '内存使用率' ]
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
		dataZoom : {
			show : true,
			realtime : true,
			start : 0,
			end : 100
		},

		grid : {
			y2 : 80
		},
		xAxis : [ {
			type : 'time',
			splitNumber : 10
		} ],
		yAxis : [ {
			name : '内存使用率(%)',
			type : 'value'
		} ],
		series : [ {
			name : '内存使用率',
			type : 'line',
			showAllSymbol : true,
			symbolSize : function(value) {
				return 0;
			},
			data : (function() {
				var data = [];
				var index = 0;
				while (index < systemStatusTime.length) {
					data.push([ new Date(systemStatusTime[index]), memData[index] ]);
					index++;
				}
				return data;
			})()

		} ]
	};

myChart_Memory.setOption(option);
}

function initHardDisk() {
	hideLoading($("#hardisk-loading"));
	myChart_HardDisk = echarts.init(document.getElementById('hardisk'));
	myChart_HardDisk.clear();
	option = {
		tooltip : {
			trigger : 'item',
			formatter : function(params) {
				return formatTime(Date.parse(params.value[0])) + '<br/>'
						+ '硬盘使用率' + ' : ' + params.value[1] + '%' + '<br/>'
						+ '已使用：'
						+ (params.value[1] / 100 * disk_total).toFixed(2) + 'G'
						+ '<br/>' + '硬盘容量：' + disk_total + 'G';
			}

		},
		legend : {
			data : [ '硬盘使用率' ]
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
		dataZoom : {
			show : true,
			realtime : true,
			start : 0,
			end : 100
		},

		grid : {
			y2 : 80
		},
		xAxis : [ {
			type : 'time',
			splitNumber : 10
		} ],
		yAxis : [ {
			name : '硬盘使用率(%)',
			type : 'value'
		} ],
		series : [ {
			name : '硬盘使用率',
			type : 'line',
			showAllSymbol : true,
			symbolSize : function(value) {
				return 0;
			},
			data : (function() {
				var data = [];
				var index = 0;
				while (index < systemStatusTime.length) {
					data.push([ new Date(systemStatusTime[index]), diskData[index] ]);
					index++;
				}
				return data;
			})()

		} ]
	};

myChart_HardDisk.setOption(option);
}

//自适应设置
window.onresize = function () {
    myChart.resize(); 
    myChart_CPU.resize(); 
    myChart_Memory.resize(); 
    myChart_HardDisk.resize();
};