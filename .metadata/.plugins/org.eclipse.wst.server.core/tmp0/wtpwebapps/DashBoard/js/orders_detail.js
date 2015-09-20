var orderDetailData;


function getCookie(name) {
	var arr = document.cookie
			.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
	if (arr != null) {
		return unescape(arr[2]);
	}
	return null;
}

$(document).ready(function() {
	var s = getCookie("orderDetail");
	if(s != null) {
		orderDetailData = JSON.parse(s);
	}
    refrshOrderDetail();
    getPlaneList(refrshOrderDetail)
});

function refrshOrderDetail() {
	var $list = $("#order_detail_table");
	$list.empty();
	var tr = $("<tr>" + "<td> 订单编号 </td>" + "<td>" + ((orderDetailData==null)?"":orderDetailData.id)+"</td>" + "<tr>");
	$list.append(tr);
    tr = $("<tr>" + "<td> 商城名称 </td>" + "<td>" + ((orderDetailData==null)?"":orderDetailData.mall) + "</td>" + "<tr>");
	$list.append(tr);
	tr = $("<tr>" + "<td> 下单时间 </td>" + "<td>" + ((orderDetailData==null)?"":orderDetailData.time) + "</td>" + "<tr>");
	$list.append(tr);
	tr = $("<tr>" + "<td> 配送方式 </td>" + "<td>" + ((orderDetailData==null)?"":orderDetailData.sendWay) + "</td>" + "<tr>");
	$list.append(tr);
	tr = $("<tr>" + "<td> 支付方式 </td>" + "<td>" + ((orderDetailData==null)?"":orderDetailData.payWay) + "</td>" + "<tr>");
	$list.append(tr);
	tr = $("<tr>" + "<td> 订单金额 </td>" + "<td>" + ((orderDetailData==null)?"":orderDetailData.money) + "</td>" + "<tr>");
	$list.append(tr);
	tr = $("<tr>" + "<td> 订单状态 </td>" + "<td>" + ((orderDetailData==null)?"":orderDetailData.status) + "</td>" + "<tr>");
	$list.append(tr);
	tr = $("<tr>" + "<td> 航班号 </td>" + "<td>" + ((orderDetailData==null)?"":orderDetailData.flight) + "</td>" + "<tr>");
	$list.append(tr);
	tr = $("<tr>" + "<td> 飞机尾号 </td>" + "<td>" + ((orderDetailData==null)?"":orderDetailData.plane) + "</td>" + "<tr>");
	$list.append(tr);
	
	var $list2 = $("#goods_detail_table");
	$list2.empty();
	if(orderDetailData != null) {
		var goodslist = orderDetailData.goodsList;
		for(var i = 0; i <goodslist.length; i++) {
			tr = $("<tr>" 
			     + "<td>" + goodslist[i].productName + "</td>"
			     + "<td>"  + goodslist[i].count + "</td>" 
			     + "<td>"  + goodslist[i].goodsPrice + "</td>" 
			     + "<tr>");
			$list2.append(tr);
		}
	}
}
