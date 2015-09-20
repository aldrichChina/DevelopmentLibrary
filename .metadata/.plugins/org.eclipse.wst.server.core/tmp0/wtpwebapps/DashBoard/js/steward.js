var startDay;
var endDay;
var data = new Array();
$(document).ready(function() {
	refresh();
	getStewardData();
	bindRefresh(getStewardData);
	getPlaneList(getStewardData);
});
function getStewardData() {

	console.log("jia" + startDay.valueOf());
	console.log(endDay.valueOf());
	var $stewardList = $("#steward_list");

	$.get("/DashBoard/action/getstewardloginoutinfo", {
		org : "spring",
		plane : "B-9940",

		start : startDay.valueOf(),
		end : endDay.valueOf()

	}, function(data) {
		console.log(data)
		var $list = $("#steward_list");
		$list.empty();
		for ( var i = 0; i < data.length; i++) {
			var e = data[i];
			var unixTimestamp = new Date(e.ts);
			commonTime = unixTimestamp.toLocaleString();

			var tr = $("<tr>" + "<td>" + commonTime + "</td>" + "<td>"
					+ e.data.account + "</td>" + "<td>" + e.flight_no + "</td>"
					+ "</tr>");
			$list.append(tr);
		}

	});
	$.get("/DashBoard/action/getstewardorderinfo", {
		org : "spring",
		plane : "B-9940",

		start : startDay.valueOf(),
		end : endDay.valueOf()
	}, function(data) {
		console.log(data)
		console.log(data)
		var $list = $("#steward_order");
		$list.empty();
		for ( var i = 0; i < data.length; i++) {
			var e = data[i];
			var unixts = new Date(e.ts);
			var unixorderTs = new Date(e.data.orderTs);
			commonTimets = unixts.toLocaleString();
			commonTimeorderTs = unixorderTs.toLocaleString();
			var orderType;
			switch (e.type) {
			case "order_cancel":
				orderType = "取消订单";
				break;
			case "gathering":
				orderType = "收款";
				break;
			case "refund":
				orderType = "退款";
				break;
			}
			var payWay;
			switch (e.data.payWay) {
			case "0":
				payWay = "现金";
				break;
			case "1":
				payWay = "积分";
				break;
			case "2":
				payWay = "信用卡";
				break;
			}
			var tr = $("<tr>" + "<td>" + e.flight_no + "</td>" + "<td>"
					+ e.data.order_id + "</td>" + "<td>" + payWay + "</td>"
					+ "<td>" + commonTimeorderTs + "</td>" + "<td>" + orderType
					+ "</td>" + "<td>" + "0001" + "</td>" + "<td>"
					+ commonTimets + "</td>" + "</tr>");
			$list.append(tr);
		}
	});
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
	$('#stewardreservation')
			.val(
					startDay.format('MM/DD/YYYY') + " - "
							+ endDay.format('MM/DD/YYYY'));
	$('#stewardreservation').daterangepicker({
		"dateLimit" : {
			"days" : 6
		}
	}, function(start, end, label) {
		console.log("start" + start, "end" + end, label);
		startDay = start.hour(0).minute(0).seconds(0).milliseconds(0);
		endDay = end.hour(0).minute(0).seconds(0).milliseconds(0);
		console.log(startDay.valueOf());
		console.log(endDay.valueOf());
		getStewardData();
	});

}

function exportToFile() {
	exportTableToCSV($('#steward_attendant_table'),
			$('#export_steward_history'), '历史记录报表.csv');
}
function exportToOrderFile() {
	exportTableToCSV($('#steward_order_table'), $('#export_steward_Order'),
			'导出历史订单记录报表.csv');
}