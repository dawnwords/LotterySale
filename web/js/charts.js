/**
 * Created by Dawnwords on 2015/5/15.
 */
var graphData = [];
var chart;

var newSeries = function () {
    return [
        {name: "电脑", type: "bar", data: []},
        {name: "即开", type: "bar", data: []},
        {name: "中福在线", type: "bar", data: []},
        {name: "总量", type: "bar", data: []}
    ];
};

var pushSeries = function (series, d) {
    series[0].data.push(d.s1);
    series[1].data.push(d.s2);
    series[2].data.push(d.s3);
    series[3].data.push(d.stotal);
};

var formatter = [
    function (s) {
        return s + "年";
    },
    function (s) {
        var tokens = s.split("/");
        return tokens[0] + "年第" + (+tokens[1]) / 3 + "季度"
    },
    function (s) {
        return s;
    }
];

var getOpt = function (x, series) {
    return {
        calculable: true,
        grid: {'y': 80, 'y2': 100},
        xAxis: [{type: 'category', 'axisLabel': {'interval': 0}, data: x}],
        yAxis: [{name: '销量', show: true, type: 'value'}],
        tooltip: {trigger: 'axis'},
        toolbox: {
            show: true,
            orient: 'vertical',
            x: 'right',
            y: 'center',
            feature: {
                mark: {show: true},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        series: series
    };
};

var updateSaleGraph = function () {
    if (graphData == null || graphData.length == 0)return;
    var dimen = +$("input[name=time]:checked", "#funcbar-time").val();
    chart.clear();
    var data, times = [], series, timeFormat = function (s) {
        return formatter[dimen](s);
    };
    if (graphData.length == 1) {
        series = newSeries();
        data = graphData[0];
        data = [data.yearData, data.quarterData, data.monthData][dimen];
        for (var i in data) {
            var d = data[i];
            pushSeries(series, d);
            times.push(timeFormat(d.time));
        }
        chart.setOption(getOpt(times, series));
    } else {
        var unitNames = [];
        for (var i in graphData) {
            data = graphData[i];
            unitNames.push(data.unitName);

            data = [data.yearData, data.quarterData, data.monthData][dimen];
            for (var d in data) {
                times[data[d].time] = d;
            }
        }
        var timeline = [];
        var options = [];
        for (var time in times) {
            timeline.push(time);
            series = newSeries();
            for (var i in graphData) {
                data = graphData[i];
                data = [data.yearData, data.quarterData, data.monthData][dimen];
                pushSeries(series, data[times[time]]);
            }
            options.push(getOpt(unitNames, series));
        }
        chart.setOption({
            options: options,
            timeline: {
                data: timeline,
                label: {formatter: timeFormat},
                checkpointStyle: {
                    symbol: 'auto',
                    symbolSize: 'auto',
                    color: 'blue',
                    bordercolor: 'blue',
                    label: {show: false, textStyle: {color: 'auto'}}
                },
                autoPlay: false,
                playInterval: 2000
            }
        });
    }
};

var getTimeData = function (selectedUnit) {
    $.ajax({
        url: '../sale',
        dataType: 'json',
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify({unitId: selectedUnit}),
        success: function (data) {
            console.log(data);
            graphData = data;
            updateSaleGraph();
        }
    });
};

var getRatioData = function () {

};
