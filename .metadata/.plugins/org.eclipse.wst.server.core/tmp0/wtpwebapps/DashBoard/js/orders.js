var myChart = echarts.init(document.getElementById('orders'));
var labelTop = {
    normal : {
        label : {
            show : true,
            position : 'center',
            formatter : '{b}',
            textStyle: {
                baseline : 'bottom'
            }
        },
        labelLine : {
            show : false
        }
    }
};
var labelFromatter = {
    normal : {
        label : {
            formatter : function (params){
                return 100 - params.value
            },
            textStyle: {
                baseline : 'top'
            }
        }
    },
}
var labelBottom = {
    normal : {
        color: '#ddd',
        label : {
            show : true,
            position : 'center'
        },
        labelLine : {
            show : false
        }
    },
    emphasis: {
        color: 'rgba(0,0,0,0)'
    }
};
var radius = [60, 80];
option = {
    legend: {
        x : 'center',
        y : 'bottom',
        data:[
            '现金订单','信用卡订单','全部订单'
        ]
    },
    title : {
        text: '',
        subtext: '',
        x: 'center'
    },
    toolbox: {
        show : true,
        feature : {
            dataView : {show: false, readOnly: false},
            magicType : {
                show: false, 
                type: ['pie', 'funnel'],
                option: {
                    funnel: {
                        width: '20%',
                        height: '30%',
                        itemStyle : {
                            normal : {
                                label : {
                                    formatter : function (params){
                                        return 'other\n' + params.value 
                                    },
                                    textStyle: {
                                        baseline : 'middle'
                                    }
                                }
                            },
                        } 
                    }
                }
            },
            restore : {show: false},
            saveAsImage : {show: false}
        }
    },
    series : [
        {
            type : 'pie',
            center : ['20%', '45%'],
            radius : radius,
            x: '0%', // for funnel
            itemStyle : labelFromatter,
            data : [
                {name:'其他', value:100, itemStyle : labelBottom},
                {name:'现金订单', value:0,itemStyle : labelTop}
            ]
        },
        {
            type : 'pie',
            center : ['50%', '45%'],
            radius : radius,
            x:'20%', // for funnel
            itemStyle : labelFromatter,
            data : [
                {name:'其他', value:100, itemStyle : labelBottom},
                {name:'信用卡订单', value:0,itemStyle : labelTop}
            ]
        },
        {
            type : 'pie',
            center : ['80%', '45%'],
            radius : radius,
            x:'40%', // for funnel
            itemStyle : labelFromatter,
            data : [
                {name:'其他', value:100, itemStyle : labelBottom},
                {name:'全部订单', value:0,itemStyle : labelTop}
            ]
        }
    ]
};
myChart.setOption(option);
window.onresize = function () {
    myChart.resize(); 
};

var currentDay1 ;
var currentDay2 ;
var currentDay3 ;
var currentMall1;
var currentMall2;
var currentPlane1;
var currentPlane2;
var currentPlane3;
var data2 = new Array();
var flights = new Array();
var currentFlight;

function getPayWay(way) {
	if(way == "0") {
		return "现金支付";
	} else if(way == "1") {
		return "积分支付";
	} else if(way == "2") {
		return "信用卡支付";
	} else {
		return "未知";
	}
}

function getSendWay(way) {
	if(way == "0") {
		return "机上自提";
	} else if(way == "1") {
		return "物流配送";
	} else {
		return "未知";
	}
}

function getOrderStatus(status) {
	if(status == "0") {
		return "初始态";
	} else if(status == "1") {
		return "等待处理";
	} else if(status == "2") {
		return "取消";
	} else if(status == "3") {
		return "成功";
	} else if(status == "4") {
		return "失败";
	} else if(status == "5") {
		return "退货";
	} else {
		return "未知";
	}
}


$(document).ready(function() {
    refresh();
});

function changePlane1(p) {
	if(p != currentPlane1) {
		currentPlane1 = p;
		getData1();
	}
}

function changePlane2(p) {
	if(p != currentPlane2) {
		currentPlane2 = p;
		getData2();
	}
}

function changePlane3(p) {
	if(p != currentPlane3) {
		currentPlane3 = p;
	}
}

function changeMall1(m) {
	if(m != currentMall1) {
		currentMall1 = m;
		getData1();
	}
}

function changeMall2(m) {
	if(m != currentMall2) {
		currentMall2 = m;
		getData2();
	}
}

function changeDay1(day) {
	if(day != currentDay1) {
		currentDay1 = day;
		getData1();
	}
}

function changeDay2(day) {
	if(day != currentDay2) {
		currentDay2 = day;
		getData2();
	}
}

var cashNum = 0;
var cardNum = 0;
var totalNum = 0;

function getData1() {
	if(currentDay1 == null || currentMall1 == null || currentPlane1 == null) {
		return;
	}
	myChart.clear();
	cashNum = 0;
	cardNum = 0;
	totalNum = 0;
	$.get("/DashBoard/action/getorderinfo", {
        org : "spring",
        plane: currentPlane1,
         mall: currentMall1,
        start: currentDay1.valueOf(),
        end: (currentDay1.valueOf()+1000*60*60*24-1)
    }, function(ret) {
    	for(var i = 0; i < ret.length; i++) {
    		var info = ret[i].orderPayWay;
    		if(info == "0") {
    			++cashNum;
    		} else if(info == "2") {
    			++cardNum;
    		}
    		++totalNum;
    		
    	}
    	fillChart();
    });
}

function fillChart() {
	option.series[0].data[0].value = totalNum-cashNum;
	option.series[0].data[1].value = cashNum;
	option.series[1].data[0].value = totalNum-cardNum;
	option.series[1].data[1].value = cardNum;
	option.series[2].data[0].value = 0;
	option.series[2].data[1].value = totalNum;
	var formatter = {
		    normal : {
		        label : {
		            formatter : function (params){
		                return totalNum - params.value;
		            },
		            textStyle: {
		                baseline : 'top'
		            }
		        }
		    },
		};
	option.series[0].itemStyle = formatter;
	option.series[1].itemStyle = formatter;
	option.series[2].itemStyle = formatter;
	myChart.setOption(option);
}

function OrderInfo(id, mall, time, money, payWay, sendWay, status, flight, plane, goodList) {
    this.id = id;
    this.mall = (mall=="SpringMall")?"绿翼商城":"DFASS商城";
    this.time = (new moment(time)).format("YYYY-MM-DD HH:mm");
    this.money = "¥"+money;
    this.payWay = getPayWay(payWay);
    this.sendWay = getSendWay(sendWay);
    this.status = getOrderStatus(status);
    this.flight = flight;
    this.plane = plane;
    this.goodsList = goodList;
}


function setCookie(name,value){
     var exp = new Date();
     exp.setTime(exp.getTime() + 60*60*1000);
     document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
     location.href = "Read.htm"; //接收页面.
}

function showDetail(i) {
	var str = "";
	if(data2 != null) {
		str = JSON.stringify(data2[i]);
	}
	setCookie("orderDetail", str);
	window.location.href = encodeURI("orders_detail.html");

}

function fillTable() {
	var $list= $("#order_table_body");
    $list.empty();
    if(flights.length == 0 || currentFlight == null) {
    	return;
    }
    for(var i = 0; i < flights.length; i++) {
    	if(flights[i].flightId == currentFlight) {
    		data2 = flights[i].orders;
    	};
    }
    if(data2.lenght == 0) {
    	return;
    }
    for(var i=0; i < data2.length; i++) {
        var e = data2[i];
        var tr = $("<tr>"
                + "<td>" + e.id + "</td>"
                + "<td>" + e.mall + "</td>"
                + "<td>" + e.time + "</td>"
                + "<td>" + e.money + "</td>"
                + "<td>" + e.payWay + "</td>"
                + "<td>" + e.sendWay + "</td>"
                + "<td>" + e.status + "</td>"
                + "<td>" + '<a href="#" class="btn-link" onclick= "showDetail('+  i + ')">订单详情>></a>' 
                + "<td>" +
                + "<tr>");
        $list.append(tr);
    };
}

function FlightData(f) {
	this.flightId = f;
	this.orders = new Array();
}

function updateFlights(info) {
	var f = info.orderFlightId;
	var found = false;
	var index = flights.length;
	for(var j = 0; j < flights.length; j++) {
		if(flights[j].flightId == f) {
			found = true;
			index = j;
			break;
		}
	}
	if(!found) {
		flights.push(new FlightData(f));
	}
	flights[index].orders.push(new OrderInfo(
			info.orderId, info.goodsMall, info.orderTime, info.orderGoodsPrice,
			info.orderPayWay, info.distributionWay, info.orderStatus, info.orderFlightId, info.planeUniqueNo, info.goodsList));
}

function changeFlight(f) {
	currentFlight = f;
}

function updateFlightList() {
	$("#flight-list").empty();
	if(flights.length == 0) {
		$("#flight-select").html("航班号" + "<span class='caret'></span>");
		changeFlight(null);
		return;
	}

	for(var j = 0; j < flights.length; j++) {
		var $node_li = $("<li><a href='#'>" + flights[j].flightId + "</a></li>").click(
				function() {
					var btn_html = $(this).text() + "<span class='caret'></span>";
					$("#flight-select").html(btn_html);
					changeFlight( $(this).text());
					fillTable();
				});
		$("#flight-list").append($node_li);
	}
 
	var flight = $("#flight-list").children().first().text();
	$("#flight-select").html(flight + "<span class='caret'></span>");
	changeFlight(flight);
}

function getData2() {
	if(currentDay2 == null || currentMall2 == null || currentPlane2 == null) {
		return;
	}
	data2 = [];
	flights = [];
	currntFlight = null;
	$.get("/DashBoard/action/getorderinfo", {
        org : "spring",
        plane: currentPlane2,
         mall: currentMall2,
        start: currentDay2.valueOf(),
        end: (currentDay2.valueOf()+1000*60*60*24-1)
    }, function(ret) {
    	for(var i = 0; i < ret.length; i++) {
    		info = ret[i];
    		updateFlights(info);
    	}
    	updateFlightList();
    	fillTable();
    });
}


function initUI() {
	getPlaneType($("#plane-select1"), $("#plane-list1"), changePlane1);
	getPlaneType($("#plane-select2"), $("#plane-list2"), changePlane2);
	getPlaneType($("#plane-select3"), $("#plane-list3"), changePlane3);
	getMallType($("#mall-select1"), $("#mall-list1"), changeMall1);
	getMallType($("#mall-select2"), $("#mall-list2"), changeMall2);	
	currentDay1 = new moment({hour: 0, minute: 0, seconds: 0, milliseconds: 0});
	currentDay2 = new moment({hour: 0, minute: 0, seconds: 0, milliseconds: 0});
    $('#datetimepicker').datetimepicker('setDate', currentDay1.toDate());
    $('#datetimepicker').datetimepicker().on('changeDate', function(ev){
        changeDay1(new moment(ev.date.valueOf()).hour(0).minute(0).seconds(0).milliseconds(0));
    });
    $('#datetimepicker1').datetimepicker('setDate', currentDay2.toDate());
    $('#datetimepicker1').datetimepicker().on('changeDate', function(ev){
        changeDay2(new moment(ev.date.valueOf()).hour(0).minute(0).seconds(0).milliseconds(0));
    });
    changeDay1(currentDay1);
    changeDay2(currentDay2);
}


function refresh() {
	initUI();
}
function exportToFile() {
    exportTableToCSV($(info), $('#orders_mall_orders'), '商城订单统计报表.csv');
}
