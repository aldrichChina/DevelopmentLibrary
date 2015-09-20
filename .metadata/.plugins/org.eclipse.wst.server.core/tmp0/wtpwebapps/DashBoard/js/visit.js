var myChart = echarts.init(document.getElementById('visit'));
$(function() {
	getPlaneList(option);
});
option = {
    title: {
        text: '',
        subtext: ''
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['访问次数', '访问时长']
    },
    toolbox: {
        show: true,
        feature: {
            mark: { show: false },
            dataView: { show: false, readOnly: false },
            magicType: { show: false, type: ['line', 'bar'] },
            restore: { show: false },
            saveAsImage: { show: false }
        }
    },
    calculable: true,
    xAxis: [
        {
            type: 'category',
            data: ['视频', '绿翼商城', '购物中心', '游戏', '新闻', '电子书', '儿童世界', '空联接机', '飞行信息']
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: [
        {
            name: '访问次数',
            type: 'bar',
            data: [12557, 4900, 6300, 9500, 11007, 7670, 3560, 2650, 3260],
            markPoint: {
                data: [
                    { type: 'max', name: '最大值' },
                    { type: 'min', name: '最小值' }
                ]
            },
            markLine: {
                data: [
                    { type: 'average', name: '平均值' }
                ]
            }
        },
        {
            name: '访问时长',
            type: 'bar',
            data: [2953.57, 1390.68, 1730.97, 1990.35, 2210.52, 1707.22, 1570.77, 1280.15, 1250.85],
            markPoint: {
                data: [
                    { name: '最高', value: 2953.57, xAxis: 0, yAxis: 2953.57, symbolSize: 25 },
                    { name: '最低', value: 1250.85, xAxis: 8, yAxis: 1250.85, symbolSize: 25 }
                ]
            },
            markLine: {
                data: [
                    { type: 'average', name: '平均值' }
                ]
            }
        }
    ]
};
                    
myChart.setOption(option);
window.onresize = function () {
    myChart.resize();
}