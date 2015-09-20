function getifeversioninfo(){
	alert("版本号: 1.0.0.14");
}
// 导出表格到csv文件  
// 注意对通过a标签实现导出文件，经实测，支持程度不同浏览器不同：firfox（完美支持） > chrome（文件名有时忽略，chrome BUG） > IE（不支持）
function exportTableToCSV($table, $link, filename) {
    var $rows = $table.find('tr:has(td)');
    // Temporary delimiter characters unlikely to be typed by keyboard
    // This is to avoid accidentally splitting the actual contents
    tmpColDelim = String.fromCharCode(11); // vertical tab character
    tmpRowDelim = String.fromCharCode(0); // null character

    // actual delimiter characters for CSV format
    colDelim = '","';
    rowDelim = '"\r\n"';
    // Grab text from table into CSV formatted string
    csv = '"'
            + $rows.map(function(i, row) {
                var $row = $(row), $cols = $row.find('td');

                return $cols.map(function(j, col) {
                    var $col = $(col), text = $col.text();

                    return text.replace(/"/g, '""'); // escape double quotes

                }).get().join(tmpColDelim);

            }).get().join(tmpRowDelim).split(tmpRowDelim).join(rowDelim).split(
                    tmpColDelim).join(colDelim) + '"';
    csvData = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(csv);
    
    $link.attr({
        'download' : filename,
        'href' : csvData,
        'target' : '_blank'
    });
}
function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    var CSV = '';    
    if (ShowLabel) {
        var row = "";
        for (var index in arrData[0]) {
            row += index + ',';
        }
        row = row.slice(0, -1);
        CSV += row + '\r\n';
    }
    for (var i = 0; i < arrData.length; i++) {
        var row = "";
        for (var index in arrData[i]) {
            row += '"' + arrData[i][index] + '",';
        }
        row.slice(0, row.length - 1);
        CSV += row + '\r\n';
    }
    if (CSV == '') {        
        alert("Invalid data");
        return;
    }   
    var fileName = ReportTitle;
    var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);
    var link = document.createElement("a");    
    link.href = uri;
    link.style = "visibility:hidden";
    link.download = fileName + ".csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
/**
 * 避免 XSS
 */
function filterXSS(val) {
    val = val.toString();
    return val.replace(/[<%3C]/g, "&lt;")
             .replace(/[>%3E]/g, "&gt;")
             .replace(/"/g, "&quot;")
             .replace(/'/g, "&#39;");
};

/**
 * 获取飞机列表，根据飞机下拉列表框和搜索框的选择提供callback(flightNo)回调
 * 需要在class="sidebar-search"的div元素中为input元素添加id="plane-input",为button元素添加id="plane-search"
 */
function getPlaneList(callback) {
	$.get("/DashBoard/planeinfo/getplanelist", {
		org : "spring",
		page : 1,
		rows : 10
	}, function(data, textStatus) {
		$.each(data, function(index, item) {
			var flight = item;
			// 为ul创建li节点，并添加click事件
			var $node_li = $("<li><a href='#'>" + flight + "</a></li>").click(
					function() {
						var btn_html = $(this).text()
								+ "<span class='caret'></span>";
						$("#plane-select").html(btn_html);
						callback($(this).text());
					});
			$("#plane-list").append($node_li);
		});
		// 搜索飞机号
		$("#plane-search").click(
				function() {
					var text = filterXSS($("#plane-input").val());
					var plane_html = text + "<span class='caret'></span>";
					$("#plane-select").html(plane_html);
					callback(text);
				});
		// 使用ul中第一个子节点画ife页面
		var plane = $("#plane-list li:first-child").text();
		$("#plane-select").html(plane + "<span class='caret'></span>");
		callback(plane);
	});

}

/**
 * 获取飞机列表
 */
function getPlaneListData(callback) {
	$.get("/DashBoard/planeinfo/getplanelist", {
		org : "spring",
		page : 1,
		rows : 10
	}, function(data, textStatus) {
		callback(data);
	});
}

/**
 * 为单个下拉框添加数据
 * @param data 列表项数据数组
 * @param $select class为dropdown-toggle的button元素
 * @param $list class为dropdown-menu的ul元素
 * @param callback 列表项选中回调函数
 */
function setDropdownList(data, $select, $list, callback) {
	$.each(data, function(index, item) {
		// 为ul创建li节点，并添加click事件
		var $node_li = $("<li><a>" + item + "</a></li>").click(
				function() {
					var btn_html = $(this).text()
							+ "<span class='caret'></span>";
					$select.html(btn_html);
					callback($(this).text());
				});
		$list.append($node_li);
	});
	var select_data = $list.children()[0].innerText;
	$select.html(select_data + "<span class='caret'></span>");
	callback(select_data);
}

/**
 * 绑定更新按钮
 */
function bindRefresh(callback) {
	$("#btn-refresh").click(function() {
		refresh();
	});
	refresh();
	
	function refresh() {
		var time = formatTime(new Date().getTime());
		var html_data = "更新于" + time + "&nbsp;<button id='btn-refresh' type='button' class='fa fa-refresh'></button>";
		$("#refresh-bar").html(html_data);
		//重新绑定
		$("#btn-refresh").click(function() {
			refresh();
		});
		callback();
	}
}

/**
 * 格式化时间
 */
function formatTime(ts) {
	var date = new Date(ts);
	var dateStr = date.getFullYear()
			+ "-"
			+ (date.getMonth() + 1)
			+ "-"
			+ date.getDate()
			+ " "
			+ (date.getHours() < 10 ? "0" + date.getHours() : date
					.getHours())
			+ ":"
			+ (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
					.getMinutes());
	return dateStr;
}

/**
 * 添加loading功能
 * @param $div 需要在请求时添加loading的容器
 * @param id 为新建的loading添加一个id，方便删除等操作
 */
function showLoading($div, id) {
	var top = $div.position().top;
	var left = $div.position().left;
	var width = $div.width();
	var height = $div.height();
	
	var $loading = $("<div id='" + id +"'>" + "<div class='loading-view' " +
			"style='background:rgba(255, 255, 255, 0.6) url(../images/loading.gif) center no-repeat;" +
			"position:absolute;top:" + top + "px;left:" + left + "px;width:" + 
			width + "px;height:" + height + "px'/></div>");
	$div.parent().append($loading);
}

function hideLoading($loading) {
	$loading.remove();
}

function hideAllLoading() {
	$(".loading-view").parent().remove();
}

function getPlaneType(select, list, callback) {
	$.get("/DashBoard/planeinfo/getplanelist", {
		org : "spring",
		page : 1,
		rows : 10
	}, function(data, textStatus) {
		$.each(data, function(index, item) {
			var flight = item;
			var $node_li = $("<li><a href='#'>" + flight + "</a></li>").click(
					function() {
						var btn_html = $(this).text()
								+ "<span class='caret'></span>";
						select.html(btn_html);
						callback($(this).text());
					});
			list.append($node_li);
		});
		var plane = list.children().first().text();
		select.html(plane + "<span class='caret'></span>");
		callback(plane);
	});
}

var mallTypes;

function getMallType(select, list, callback) {
	mallTypes = [];
	$.get("/DashBoard/action/getmall", {
		org : "spring"
	}, function(data, textStatus) {
		mallTypes = data;
		for(var i = 0; i < data.length; i++) {
			var info = data[i];
			var $node_li = $("<li><a href='#'>" + info.name + "</a></li>").click(
					function() {
						var btn_html = $(this).text()
								+ "<span class='caret'></span>";
						select.html(btn_html);
						for(var j = 0; j < mallTypes.length; j++) {
							if(mallTypes[j].name == $(this).text()) {
								callback(mallTypes[j].value);
								break;
							}
						}
					});
			list.append($node_li);
		}
		
		if(data.length > 0) {
			select.html(data[0].name + "<span class='caret'></span>");
			callback(data[0].value);
		}
	});	
}