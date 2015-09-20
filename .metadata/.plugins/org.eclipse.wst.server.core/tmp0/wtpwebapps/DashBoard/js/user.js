var myChart;
var myChart_Register;

var mainCurrentDay;
var registerCurrentDay;
var exportStartDay;
var exportEndDay;

var mainPlane;
var registerPlane;
var exportPlane;

$(function() {
	demo();
	datePicker();
	getPlaneListData(setAllDropdownPlaneList);
});

function demo() {
	initFlightRegister();
	initDateRegister();
}

function datePicker() {
	var currentDay = new moment({hour: 0, minute: 0, seconds: 0, milliseconds: 0});
	mainCurrentDay = currentDay;
	registerCurrentDay = currentDay;
	exportStartDay = new moment(currentDay).subtract(7, 'days');
	exportCurrentDay = currentDay;
	exportEndDay = new moment(currentDay).subtract(-1, 'days');
	
	$('#main-datetimepicker').datetimepicker('setDate', mainCurrentDay.toDate());//设置初始显示时间
    $('#main-datetimepicker').datetimepicker().on('changeDate', function(ev){//日期选择事件
    	mainCurrentDay = ev.date;
    	drawMain();
    });
    
    $('#register-datetimepicker').datetimepicker('setDate', registerCurrentDay.toDate());//设置初始显示时间
    $('#register-datetimepicker').datetimepicker().on('changeDate', function(ev){//日期选择事件
    	registerCurrentDay = ev.date;
    	drawRegister();
    });
    
    $('#reservation').val(exportStartDay.format('MM/DD/YYYY')+" - "+exportCurrentDay.format('MM/DD/YYYY'));
    $('#reservation').daterangepicker({
        "dateLimit": {
            "days": 7
        }
    }, function(start, end, label) {
    	exportStartDay = start.hour(0).minute(0).seconds(0).milliseconds(0);
    	exportEndDay = new moment(end.hour(0).minute(0).seconds(0).milliseconds(0)).subtract(-1, 'days');
    });
}

function mainCallback(plane) {
	mainPlane = plane;
	drawMain();
}

function drawMain() {
	//draw main
}

function registerCallback(plane) {
	registerPlane = plane;
	drawRegister();
}

function drawRegister() {
	//draw register
}

function exportCallback(plane) {
	exportPlane = plane;
}

function setAllDropdownPlaneList(data) {
	setDropdownList(data, $("#main-plane-select"), $("#main-plane-list"),
			mainCallback);
	setDropdownList(data, $("#register-plane-select"), $("#register-plane-list"),
			registerCallback);
	setDropdownList(data, $("#export-plane-select"), $("#export-plane-list"),
			exportCallback);
}


function initFlightRegister() {
	myChart = echarts.init(document.getElementById('main'));
	option = {
		tooltip : {
			trigger : 'axis',
			formatter : function(params) {
				var tip = '航班号' + ' : ' + params[0].name + '<br/>';
				tip += params[0].seriesName + ' : ' + params[0].value
						+ '<br/> ';
				if (typeof (params[1]) != 'undefined') {
					tip += params[1].seriesName + ' : ' + params[1].value
							+ '<br/> ';
				}
				if (typeof (params[2]) != 'undefined') {
					tip += params[2].seriesName + ' : ' + params[2].value;
				}
				return tip;
			}
		},
		legend : {
			data : [ '会员注册', '非会员乘客开机量', '非会员总数' ],
			x : 'center'
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
				    show: false
				}
			}
		},
		dataZoom : {
			show : true,
			realtime : true,
			start : 0,
			end : 100
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			boundaryGap : false,
			axisLine : {
				onZero : false
			},
			data : ['9C8815', '9C8816', '9C8881', '9C8882', '9C8931', '9C8932', '9C8933']
		} ],
		yAxis : [ {
			name : '人数',
			type : 'value'
		} ],
		series : [ {
			name : '会员注册',
			type : 'line',
			data : [148, 154, 168, 169, 160, 172, 168]
		},
		{
			name : '非会员乘客开机量',
			type : 'line',
			data : [165, 172, 180, 189, 179, 177, 182]
		},
		{
			name : '非会员总数',
			type : 'line',
			data : [188, 190, 198, 192, 189, 192, 190]
		}]
	};
	myChart.setOption(option); 
}

function initDateRegister() {

    myChart_Register = echarts.init(document.getElementById('rigister'));
	option = {
		tooltip : {
			trigger : 'axis',
			formatter : function(params) {
				return params[0].name + '<br/>' + params[0].seriesName + ' : '
						+ params[0].value;
			}
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
				    show: false
				}
			}
		},
		dataZoom : {
			show : true,
			realtime : true,
			start : 0,
			end : 100
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			boundaryGap : false,
			axisLine : {
				onZero : false
			},
			data : ['2015/08/15', '2015/08/16', '2015/08/17', '2015/08/18', '2015/08/19', '2015/08/20', '2015/08/21']
		} ],
		yAxis : [ {
			name : '人数',
			type : 'value'
		} ],
		series : [ {
			name : '7日内注册量',
			type : 'line',
			data : [148, 154, 168, 169, 165, 160, 150]
		}]
	};
    myChart_Register.setOption(option);
}

//自适应设置
window.onresize = function () {
    myChart.resize();
    myChart_Register.resize();
};

function exportToFile() {
    exportTableToCSV($('#steward_attendant_table'), $('#user_member_registration'), '会员注册统计报表.csv');
}
