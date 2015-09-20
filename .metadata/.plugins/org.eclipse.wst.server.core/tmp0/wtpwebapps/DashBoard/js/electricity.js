var currentDay = new moment({hour: 0, minute: 0, seconds: 0, milliseconds: 0});
var currentTime = currentDay.format('YYYY-MM-DD');
var battery;

$(function() {
	bindDatePickerRefresh();
	bindRefresh(getBatteryInfo);
});

function bindDatePickerRefresh() {
    $('#datetimepicker').datetimepicker('setDate', currentDay.toDate());
    $('#datetimepicker').datetimepicker().on('changeDate', function(ev){
    	currentDay = new moment(ev.date.valueOf());
    	currentTime = currentDay.format('YYYY-MM-DD');
    	getBatteryInfo();
    });
}

function getBatteryInfo() {
	$.ajax({
		type : "GET",
		url : "/DashBoard/usage/getbatteryusageinfo",
		data : {
			org : "spring",
			day : currentDay.valueOf()
		},
		beforeSend : function() {
			showLoading($(".table-responsive"), "flight-battery-list");
		},
		complete : function() {
			hideLoading($("#flight-battery-list"));
		},
		success : function(data, status) {
			$("#battery-statistics").empty();
			$.each(data, function(index, item) {
				battery = [];
				for(var i=0; i<4; i++) {
					battery.push(0);
				}
				var plane = item.plane;
				var batteryInfo;
				if(item.value) {
					batteryInfo= item.value[0].batteryInfo;
					$.each(batteryInfo, function(key, value) {
						addData(value.value);
					});
				}
				addToTable(plane);
			});

		}
	});
}

function addData(value) {
	if(value > 75) {
		battery[0]++;
	} else if(value > 50){
		battery[1]++;
	} else if(value >25) {
		battery[2]++;
	} else {
		battery[3]++;
	}
}

function addToTable(plane) {
	var href_str = "electricity_detail.html?plane=" + plane + "&day=" + currentDay.valueOf();
	var tr = $("<tr>"
			+ "<td><a href='" + href_str + "'" + "class='btn-link'>" + plane + "</a></td>"
			+ "<td>" + battery[0] + "台" + "</td>"
			+ "<td>" + battery[1] + "台" + "</td>"
			+ "<td>" + battery[2] + "台" + "</td>"
			+ "<td>" + battery[3] + "台" + "</td>"
			+ "<td>" + currentTime + "</td>"
			+ (battery[3] == 0 ? "<td/>" : "<td style='color:Red;'><img src='../images/warning.png' style='border:0;' /> 已出现低电量PAD，请尽快更换</td>")
			+ "</tr>"
			);
	tr.appendTo($("#battery-statistics"));
}