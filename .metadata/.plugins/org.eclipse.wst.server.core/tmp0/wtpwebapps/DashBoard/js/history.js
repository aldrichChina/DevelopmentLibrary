var myChart = echarts.init(document.getElementById('main'));
var currentIndex = -1;
var lines = new Array();
var points = new Array();
var data = new Array();
var allData;
var showMap = false;
var curPlane;
var map = new window.BMap.Map("bmap");
map.enableAutoResize();
map.enableScrollWheelZoom(true);
map.setZoom(5);

function Places(s, d) {
    this.src = s;
    this.dst = d;
    this.smoothness = 0;

    this.isSame = function(_place1, _place2) {
        var flag = (this.src == _place1 || this.dst == _place1);
        if(!flag) {
            return false;
        }
        flag = (this.src == _place2 || this.dst == _place2);
        return flag;
    };
}

option = {
    color: ['#ff7f50'],
    title: {
        text: '',
        x: 'center'
    },
    tooltip: {
        trigger: 'item',
        formatter: '{b}'
    },

    toolbox: {
        show: true,
        feature: {
            mark: { show: false },
            dataView: { show: false, readOnly: false },
            restore: { show: false },
            saveAsImage: { show: false }
        }
    },
    dataRange: {
        show: false,
        min : 0,
        max : 100,
        y: '60%',
        calculable : true,
        color: ['#ff3333', 'orange', 'yellow','lime','aqua']
    },
    series: [
        {
            name: '地图',
            type: 'map',
            mapType: 'china',
            itemStyle:{
                normal:{label:{show:true}},
                emphasis:{label:{show:true}}
            },
            roam: true,
            hoverable: false,
            data: [],
            geoCoord: {},
            scaleLimit:{max:5.0, min:0.5}
        }

    ]
};

myChart.setOption(option);
window.onresize = myChart.resize;

$(document).ready(
    function() {
        refresh();
        getPlaneList(getPlaneData);

    }
);

function getDateString(date) {
    return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
}

function getTimeString(date) {
    return (date.getHours() < 10 ? (0 + "" + date.getHours()) : date.getHours()) +":" + (date.getMinutes() < 10 ? (0 + "" + date.getMinutes()) : date.getMinutes());
}

function planeHistoryData(name, type, placeStart, placeEnd, tStart, tEnd, latStart, lonStart, latEnd, lonEnd) {
    this.name = name;
    this.type = type;
    this.placeStart = placeStart;
    this.placeEnd = placeEnd;
    this.timeStart = new Date(tStart);
    this.timeEnd = new Date(tEnd);
    this.latStart = latStart;
    this.lonStart = lonStart;
    this.latEnd = latEnd;
    this.lonEnd = lonEnd;
    this.startT = tStart;
    this.endT = tEnd;
}

function switchMap(index) {
    map.clearOverlays();
    if(index == -1) {
        showMap = false;
        $("#main").show();
        $("#bmap").hide();
    }else {
        showMap = true;
        $("#main").hide();
        $("#bmap").show();
        var p = data[index];
        window.setTimeout(function(){
            map.reset();
            var center = new window.BMap.Point((p.lonStart+p.lonEnd)/2.0, (p.latStart+p.latEnd)/2.0);
            map.centerAndZoom(center, 5);
        }, 300);
         $.get("/DashBoard/status/getplaneinfo", {
                org : "spring",
                plane: p.type,
                start: p.startT,
                end: p.endT
            }, function(ret) {
                map.clearOverlays();
                var tracks = [];
                var count = 0;
                for(var i = 0; i <ret.length; i++) {
                    ++count;
                    var e = ret[i];
                    tracks.push(new BMap.Point(e.data.longitude, e.data.latitude));
                    if(count == 20) {
                        BMap.Convertor.transMore(tracks,0,callback);
                        var x = tracks[19];
                        tracks = [];
                        tracks.push(x);
                        count = 0;
                    };
                }
                if(tracks.length > 1) {
                    BMap.Convertor.transMore(tracks,0,callback);
                };
            });
    };
}

var callback = function(points){
     if(!showMap) {
         return;
     }
     var realData = [];
     for(var i = 0; i < points.length; i++) {
         if(points[i].error == 0) {
             realData.push(new BMap.Point(points[i].x, points[i].y));
         }
     }
     if(realData.length > 1) {
         var polyline = new BMap.Polyline(realData,
                    { strokeColor:"blue",
                      strokeWeight:2.0,
                      strokeOpacity:0.5,
                      strokeStyle:"solid"//solid, dash
                    });
         map.addOverlay(polyline);
     }
};

function selectPlane(index) {
    if(data == null || index >= data.length) {
        return;
    }
    if(index == currentIndex && index != -1) {
        $("#history_table tr:gt("+currentIndex+"):eq("+currentIndex+") td:nth-child(7)").html('<a href="#" onclick="selectPlane(\''+currentIndex+ '\')"><img src="../images/history_flight.png" /></a>');
        currentIndex  = -1;
        switchMap(currentIndex);
    }else {
        updateTable(index);
        switchMap(index);
    }
}

function updateTable(index) {
    if(index == currentIndex) {
        return;
    }
    if(index != -1) {
        $("#history_table tr:gt("+index+"):eq("+index+") td:nth-child(7)").html('<a href="#" onclick="selectPlane(\''+index+ '\')"><img src="../images/history_currentflight.png" /></a>');
    }
    $("#history_table tr:gt("+currentIndex+"):eq("+currentIndex+") td:nth-child(7)").html('<a href="#" onclick="selectPlane(\''+currentIndex+ '\')"><img src="../images/history_flight.png" /></a>');
    currentIndex = index;
}

function fillTable() {
	hideLoading($("#flight-list"));
    var $list= $("#history_table_body");
    $list.empty();
    data.sort(function(a, b){return (a.timeStart < b.timeStart)?1:-1;});
    for(var i=0; i < data.length; i++) {
        var e = data[i];
        var img;
        if(currentIndex == i) {
            img =  "<td>" + '<a href="#" onclick="selectPlane(\''+i+ '\')"><img src="../images/history_currentflight.png" /></a>' + "</td>";
        } else {
            img =  "<td>" + '<a href="#" onclick="selectPlane(\''+i+ '\')"><img src="../images/history_flight.png"  /></a>' + "</td>";
        }
        var endTime = getTimeString(e.timeEnd);
        if(e.timeEnd.getDate() > e.timeStart.getDate()) {
             endTime += "<img src=\"../images/1.png\" style=\"border:0;\" />";
        }
        var tr = $("<tr>"
                + "<td>" + getDateString(e.timeStart) + "</td>"
                + "<td>" + e.placeStart + "</td>"
                + "<td>" + e.placeEnd + "</td>"
                + "<td>" + e.name + "</td>"
                + "<td>" + getTimeString(e.timeStart) + "</td>"
                + "<td>" + endTime + "</td>"
                + img
                + "<tr>");
        $list.append(tr);
    }
}

function refresh() {
    currentDay = new moment({hour: 0, minute: 0, seconds: 0, milliseconds: 0});
    endDay = new moment(currentDay);
    startDay = (new moment(currentDay)).subtract(7, 'days');
    $('#history_reservation').val(startDay.format('MM/DD/YYYY')+" - "+endDay.format('MM/DD/YYYY'));
    $('#history_reservation').daterangepicker({
        "dateLimit": {
            "days": 7
        }
    }, function(start, end, label) {
        switchMap(-1);
        startDay = start.hour(0).minute(0).seconds(0).milliseconds(0);
        console.info("jia "+startDay);
        endDay = end.hour(0).minute(0).seconds(0).milliseconds(0);
        console.info("jia "+endDay);
        getData();
    });
    getData();
}

function fillMap() {
    myChart.clear();
    for(var i = 0; i < data.length; i++) {
        var p = data[i];

        drawMarkLine(p.placeStart, p.latStart, p.lonStart, p.placeEnd, p.latEnd, p.lonEnd, i/data.length*100);
    }
}

function initData() {
    data = [];
    currentIndex = -1;
    fillTable();
    fillMap();
    myChart.hideLoading();
}

function getData() {
    myChart.showLoading({
        text: '读取数据中...',
    });
    showLoading($(".table-responsive"), "flight-list");
    // 获取最近一周
    $.get("/DashBoard/planeinfo/getflightlist", {
        org : "spring",
        start: startDay.valueOf(),
        end: endDay.valueOf()
    }, function(ret) {
        allData = [];
        for(var i = 0; i < ret.length; i++) {
            var plane = ret[i];
            if(plane != null && plane.value != null) {
                for(var k = 0; k < plane.value.length; k++) {
                    var info = plane.value[k];
                    allData.push(new planeHistoryData(info.fligNo, info.fligUniqueCode, info.fligLaunchCity, info.fligLandingCity,
                            info.fligLaunchTime,  info.fligLandingTime,
                            info.launchAirPortInfo.latitude,  info.launchAirPortInfo.longitude,
                            info.landingAirPortInfo.latitude,  info.landingAirPortInfo.longitude));
                }
            }
        }
        getPlaneData(curPlane);
    });
}

function getPlaneData(plane) {
    if(plane == undefined){
        return;
    }
    curPlane = plane;

    if(allData == undefined){
        return;
    }

    data = [];
    lines = [];
    var index = 0;
    while(index < allData.length) {
        if (allData[index].type == plane) {
            data.push(allData[index]);
        }
        index++;
    }
    currentIndex = -1;
    fillTable();
    fillMap();
    myChart.hideLoading();
    switchMap(-1);
}


function realPoint(x, y) {
    this.x = x;
    this.y = y;
}


function drawMarkLine(src, srclat, srclon, dst, dstlat, dstlon, val) {

    if(!option.series[0].geoCoord.hasOwnProperty(src)){
        option.series[0].geoCoord[src] = [srclon,srclat];
    }
    if(!option.series[0].geoCoord.hasOwnProperty(dst)){
        option.series[0].geoCoord[dst] = [dstlon,dstlat];
    }
    myChart.setOption(option);

    var found = false;
    var smt = 0;
    for(var k = 0; k<lines.length; k++) {
        if(lines[k].isSame(src, dst)) {
            lines[k].smoothness += 0.05;
            smt = lines[k].smoothness;
            found = true;
            break;
        }
    }
    if(!found) {
        lines.push(new Places(src, dst));
    }

    found = false;
    for(var j = 0; j<points.length; j++) {
        if(points[j] == dst) {
            found = true;
            break;
        }
    }
    if(!found) {
        myChart.addMarkPoint(0, {
            symbol : 'emptyCircle',
            symbolSize : 6,
            effect : {
                show : true,
                shadowBlur : 1,
            },
            data : [ {
                name : dst, value:val
            } ]
        });
        points.push(dst);
    }

    myChart.addMarkLine(0, {
        smooth:true,
        effect:{
            show:false,
            scaleSize:1,
            period:30,
            shadowBlur:10
        },
        itemStyle:{
            normal:{
               label:{show:false}
            }
        },
        data: [[{name: src, smoothness:smt},{name: dst,value:val}]]
    });
}

