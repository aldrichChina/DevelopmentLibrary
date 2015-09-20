var devices;
var batteryCurvData;
var search = location.search.substr(1);
var planeNo = search.replace(/.*plane=(.*)&.*/, "$1");
var currentDay = parseInt(search.replace(/.*day=(.*)/, "$1"));

$(function() {
	bindDatePickerRefresh();
});

function bindDatePickerRefresh() {
	$('#datetimepicker').datetimepicker('setDate', new moment(currentDay).toDate());
    $('#datetimepicker').datetimepicker().on('changeDate', function(ev){
    	currentDay = new moment(ev.date.valueOf());
    	getDeviceBatteryInfo();
    });
    getDeviceBatteryInfo();
}

function getDeviceBatteryInfo() {
	devices = [];
	batteryCurvData = [];
	showLoading($(".dataTable_wrapper"), "battery-list-loading");
	$.get("/DashBoard/usage/getbatteryusageinfo", {
		org : "spring",
		plane : planeNo,
		day : currentDay.valueOf()
	}, function(data, textStatus) {
		
		$.each(data, function(index, item) {
			var batteryInfo;
			if(item.value) {
				batteryInfo= item.value[0].batteryInfo;
				$.each(batteryInfo, function(kk, vv) {
					devices.push(["", vv._id, vv.value, vv.seat ? vv.seat.slice(0, 3).toString() : ""]);
				});
			}
		});
		fillDeviceBatteryList();
	});
}

function fillDeviceBatteryList() {
	hideLoading($("#battery-list-loading"));
	fillTableData();
}

function fillTableData() {
	$("#padList").DataTable({
		destroy : true,
		responsive : true,
		"aaData" : devices,
		"columns" : [ {
			"data" : null,
			"defaultContent" : "<input type='checkbox' />"
		}, 
		null, 
		null, 
		null ],
		"fnDrawCallback" : function() {
			$("#padList tr :checkbox").click(function() {
				if($(this).is(":checked")) {
					addCurvData($(this).parent().next().text());
				} else {
					deleteCurvData($(this).parent().next().text());
				}
			});
		}
	});
}

function addCurvData(deviceId) {
	showLoading($("#pad_electricity"), "battery-curv-loading");
	$.get("/DashBoard/usage/getbatteryusagecurve", {
		org : "spring",
		plane : planeNo,
		deviceId : deviceId,
		day : currentDay.valueOf()
	}, function(data, textStatus) {
		var curvData = [];
		$.each(data.curve, function(index, item) {
			curvData.push([item.ts, item.data.battery]);
		});
		batteryCurvData.push([deviceId, curvData]);
		drawBatteryCurv();
	});
}

function deleteCurvData(deviceId) {
	var index = 0;
	while(index < batteryCurvData.length) {
		if(batteryCurvData[index][0] == deviceId) {
			batteryCurvData.splice(index, 1);
			break;
		}
		index++;
	}
	drawBatteryCurv();
}

function drawBatteryCurv() {
	hideLoading($("#battery-curv-loading"));
	var myChart = echarts.init(document.getElementById('pad_electricity'));
	option = {
		title : {
			text : '',
			subtext : ''
		},
		tooltip : {
			trigger : 'item',
			formatter : function(params) {
				return formatTime(Date.parse(params.value[0])) + '<br/>'
				      + '设备id：' + params.seriesName + '<br/>'
				      + '电量：' + params.value[1];
			}
		},
		legend : {
			data : (function() {
				var legendList = [];
				var index = 0;
				while(index < batteryCurvData.length) {
					legendList.push(batteryCurvData[index][0]);
					index++;
				}
				return legendList;
			})()
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
					show : true
				}
			}
		},
		grid : {
			y2 : 80
		},
		xAxis : [ {
			type : 'time',
		} ],
		yAxis : [ {
			name : '电量(%)',
			type : 'value',
		} ],
		series : (function() {
			var curvArray = [];
			var index = 0;
			while(index < batteryCurvData.length) {
				curvArray.push(getCurvByIndex(index));
				index++;
			}
			return curvArray;
			
			function getCurvByIndex(index) {
				var curv = {
						name : batteryCurvData[index][0],
						type : 'line',
						smooth : true,
						showAllSymbol : true,
						symbolSize : function(value) {
							return 0;
						},
						data : (function() {
							var data = [];
							var batteryData = batteryCurvData[index][1];
							var pos = 0;
							while (pos < batteryData.length) {
								
								var time = batteryData[pos][0];
								var value = batteryData[pos][1];
								data.push([ new Date(time), value ]);
								pos++;
							}
							return data;
						})()
				};
				return curv;
			}
		})()
	};

	myChart.setOption(option);
	window.onresize = myChart.resize;
}
