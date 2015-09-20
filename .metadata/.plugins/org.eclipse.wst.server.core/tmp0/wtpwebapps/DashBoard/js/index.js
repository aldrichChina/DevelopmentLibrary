var plane;
var time;
var startDay;
var endDay;
$(function() {
	bindRefresh(refresh);
	getPlaneList(updateFlightList);
	refresh(time);
});
function refresh() {
	
    currentDay = new moment({hour: 0, minute: 0, seconds: 0, milliseconds: 0});
     endDay = new moment(currentDay);
     startDay = (new moment(currentDay)).subtract(7, 'days');
    $('#indexreservation').val(startDay.format('MM/DD/YYYY')+" - "+endDay.format('MM/DD/YYYY'));
    $('#indexreservation').daterangepicker({
        "dateLimit": {
            "days": 7
        }
    }, function(start, end, label) {
    	console.log("start"+start,"end"+ end, label);
    	 startDay = start.hour(0).minute(0).seconds(0).milliseconds(0);
    	 endDay = end.hour(23).minute(59).seconds(59).milliseconds(0);
    	 updateFlightList(plane);
    });
    if(plane == undefined) {
		return;
	}
    updateFlightList(plane);
}

function updateFlightList(planeNo) {
	plane = planeNo;
	var $flightList = $("#flight_list");
	
	$.ajax({
		type : "GET",
		url : "/DashBoard/planeinfo/getflightlist",
		data : {
			org : "spring",
			plane : plane,
			start : startDay.valueOf(),
			end : endDay.valueOf()
		},
		beforeSend : function() {
			showLoading($(".table-responsive"), "flight-list");
		},
		complete : function() {
			hideLoading($("#flight-list"));
		},
		success : function(data, status) {
			$flightList.empty();
			$.each(data, function(kk, vv) {
				var nextFlight = "N/A";
				var planeNo = vv.plane;
				var pos = vv.value.length - 1;
				while(pos >= 0) {
					var item = vv.value[pos];
					var LaunchDelay=(item.fligLaunchTime + 1000 * item.fligLaunchDelay)<moment()?formatTime(item.fligLaunchTime + 1000 * item.fligLaunchDelay):"--";
					var LandingDelay=(item.fligLandingTime + 1000 * item.fligLandingDelay)<moment()?formatTime(item.fligLandingTime + 1000 * item.fligLandingDelay):"--";
					var tr = $("<tr>"
							+ "<td>" + planeNo + "</td>"
							+ "<td>" + item.fligNo + "</td>"
							+ "<td>" + nextFlight + "</td>"
							+ "<td>" + formatTime(item.fligLaunchTime) + "</td>"
							+ "<td>" + LaunchDelay + "</td>"
							+ "<td>" + formatTime(item.fligLandingTime) + "</td>"
							+ "<td>" + LandingDelay + "</td>"
							+ "<td>" + item.stewAccount + "</td>"
							+ "</tr>");
					$flightList.append(tr);
					nextFlight = item.fligNo;
					pos--;
				}
			});
		}
	});
	
}
