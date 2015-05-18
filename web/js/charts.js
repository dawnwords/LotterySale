/**
 * Created by Dawnwords on 2015/5/15.
 */
function Chart(elementId) {
    var graphData = [];
    var chart = echarts.init(document.getElementById(elementId));
    var calculable = true;

    var newSeries = function (maxmin, avg, mark) {
        var markPoint = {
            data: maxmin ? [{
                type: 'max',
                name: '最大值'
            }, {
                type: 'min',
                name: '最小值'
            }] : []
        };
        var markLine = {
            data: avg ? [{
                type: 'average',
                name: '平均值'
            }] : []
        };
        var itemStyle = {
            normal: {
                label: {
                    show: mark
                }
            }
        };
        return [
            {name: "电脑", type: "bar", data: [], markPoint: markPoint, markLine: markLine, itemStyle: itemStyle},
            {name: "即开", type: "bar", data: [], markPoint: markPoint, markLine: markLine, itemStyle: itemStyle},
            {name: "中福在线", type: "bar", data: [], markPoint: markPoint, markLine: markLine, itemStyle: itemStyle},
            {name: "总量", type: "bar", data: [], markPoint: markPoint, markLine: markLine, itemStyle: itemStyle},
        ];
    };

    var timelineOpt = function (options, data, timeFormat) {
        return {
            options: options,
            timeline: {
                data: data,
                y2: 50,
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
        };
    };

    var seriesOpt = function (x, series) {
        var dataItemNames = [];
        for (var i in series) {
            dataItemNames.push(series[i].name);
        }
        return {
            calculable: calculable,
            title: {
                text: '',
                subtext: '浦东新区民政彩票管理系统',
                x: 'center',
                y: 150,
                zlevel: 0,
                subtextStyle: {
                    fontSize: 40,
                    color: 'rgba(204, 204, 204, 0.37)'
                }
            },
            dataZoom: {show: true},
            grid: {y: 50, y2: 150},
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
            legend: {
                x: 'left',
                data: dataItemNames
            },
            series: series
        };
    };

    var pushSeries = function (series, d) {
        var correct = function (i) {
            return i < 0 ? undefined : i;
        };
        series[0].data.push(correct(d.s1));
        series[1].data.push(correct(d.s2));
        series[2].data.push(correct(d.s3));
        series[3].data.push(correct(d.stotal));
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
            var tokens = s.split("/");
            return tokens[1] == '01' ? s : tokens[1];
        }
    ];

    var isArray = function (o) {
        return Object.prototype.toString.call(o) === '[object Array]';
    };

    var updateSaleGraph = function (dimen, maxmin, avg, mark) {
        var data, d, i, times = [], series, timeFormat = function (s) {
            return formatter[dimen](s);
        };
        if (graphData.length == 1) {
            series = newSeries(maxmin, avg, mark);
            data = graphData[0];
            data = [data.yearData, data.quarterData, data.monthData][dimen];
            for (i in data) {
                d = data[i];
                pushSeries(series, d);
                times.push(timeFormat(d.time));
            }
            chart.setOption({options: [seriesOpt(times, series)]});
        } else {
            var x = [];
            for (i in graphData) {
                data = graphData[i];
                x.push(data.unitName);

                data = [data.yearData, data.quarterData, data.monthData][dimen];
                for (d in data) {
                    times[data[d].time] = d;
                }
            }
            var timeline = [];
            var options = [];
            for (var time in times) {
                timeline.push(time);
                series = newSeries(maxmin, avg, mark);
                for (i in graphData) {
                    data = graphData[i];
                    data = [data.yearData, data.quarterData, data.monthData][dimen];
                    pushSeries(series, data[times[time]]);
                }
                options.push(seriesOpt(x, series));
            }
            chart.setOption(timelineOpt(options, timeline, timeFormat));
        }
    };

    var updateCompareSaleGraph = function (dimen, maxmin, avg, mark) {
        var timeline = dimen ? graphData.month : graphData.year,
            data = dimen ? graphData.monthData : graphData.yearData,
            xData = dimen ? graphData.year : graphData.month,
            options = [], series, x, i, timeFormatter = function (s) {
                return s + (dimen ? "月" : "年");
            };

        for (i in timeline) {
            series = newSeries(maxmin, avg, mark);
            x = [];
            for (var d in xData) {
                x.push(xData[d] + (dimen ? "年" : "月"));
                pushSeries(series, data[i][d] == undefined ? {} : data[i][d]);
            }
            options.push(seriesOpt(x, series));
        }
        chart.setOption(timelineOpt(options, timeline, timeFormatter));
    };

    chart.on(echarts.config.EVENT.MAGIC_TYPE_CHANGED, function (param) {
        if (param.type == 'magicTypeChanged') {
            var option = chart.getOption();
            calculable = param.magicType.bar;
            option.calculable = calculable;
            chart.setOption(option);
            chart.refresh();
        }
    });

    this.updateGraph = function (maxmin, avg, mark) {
        if (graphData == null) return;

        chart.clear();
        var dimen;
        if (isArray(graphData)) {
            if (graphData.length > 0) {
                dimen = +$("input[name=time]:checked", "#funcbar-time").val();
                updateSaleGraph(dimen, maxmin, avg, mark);
            }
        } else {
            dimen = +$("input[name=compare]:checked", "#funcbar-compare").val();
            updateCompareSaleGraph(dimen, maxmin, avg, mark);
        }
    };

    this.setGraphData = function (d) {
        graphData = d;
        this.updateGraph();
    }
}