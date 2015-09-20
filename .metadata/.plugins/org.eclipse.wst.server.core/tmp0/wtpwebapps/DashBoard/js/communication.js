var data = new Array();

$(document).ready(
        function() {
            refresh();
        }
);

function planeData(name, lastSendTime, lastReceiveTime, c0, d0, c1, d1) {
    this.name = name;
    var date = new Date(lastSendTime);
    date.toLocaleString().replace(/(\d{2}:\d{2}).*/, "$1")
    this.lastSendTime = date.toLocaleString().replace(/(\d{2}:\d{2}).*/, "$1");
    date = new Date(lastReceiveTime);
    this.lastReceiveTime = date.toLocaleString().replace(/(\d{2}:\d{2}).*/, "$1");
    this.c0 = c0;
    this.d0 = d0;
    this.c1 = c1;
    this.d1 = d1;
}

function showTip(des) {
    //todo: show tips
}

function fillTable() {
    var $list= $("#communication_table_body");
    $list.empty();
    for(var i=0; i < data.length; i++) {
        var e = data[i];
        var tr = $("<tr>"
                + "<td>" + e.name + "</td>"
                + "<td>" + e.lastSendTime + "</td>"
                + "<td>" + e.lastReceiveTime + "</td>"
                + "<td>" + getCondition(e.c0, e.d0) + "</td>"
                + "<td>--</td>"
                + "<tr>");
        $list.append(tr);
    }
}

function refresh() {
    getData();
}

function updateTimeLabel() {
    var $label = $("#last_time_tip");
    var date = new Date();
    var dateStr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()  + " " + date.getHours() +":" + (date.getMinutes()< 10 ? (0 + "" + date.getMinutes()) : date.getMinutes());
    $label.html("更新于" + dateStr +'&nbsp;<button type="button" class="fa fa-refresh" onclick="refresh()"></button>');
}

function getData() {
    $.get("/DashBoard/status/getplanelastheartbeatstatus", {
        org : "spring"
    }, function(ret) {
        data = new Array();
        $.each(ret, function(kk, vv) {
        	if(vv.value != null) {
                var p = new planeData(vv.plane, vv.value.lastSendTime, vv.value.lastReceiveTime, vv.value.netCondition == 0, null, true, null);
                data.push(p);
        	}
        });
        fillTable();
        updateTimeLabel();
    });
}

function getCondition(isGood, description) {
    if (isGood) {
        return "通信中<img src=\"../images/cricle_green.png\" style=\"border:0;\" />";
    } else {
        if (description == null) {
            description = "";
        }
        return '无通信<img src="../images/cricle_red.png" style="border:0;" onclick= "showTip(\''
                + description + '\')"/>';
    }
}

function exportToFile() {
     exportTableToCSV($('#communication_table'), $('#export_link'), '空地通信状态.csv');
}

