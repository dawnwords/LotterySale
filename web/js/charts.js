/**
 * Created by Dawnwords on 2015/5/15.
 */
function Chart(elementId) {
    var graphData = [];
    var chart = echarts.init(document.getElementById(elementId));
    var calculable = true;
    var currentOpt;

    var newSeries = function (chartOpt) {
        var markPoint = {
            data: chartOpt.maxmin ? [{
                type: 'max',
                name: '最大值'
            }, {
                type: 'min',
                name: '最小值'
            }] : []
        };
        var markLine = {
            data: chartOpt.avg ? [{
                type: 'average',
                name: '平均值'
            }] : []
        };
        var itemStyle = {
            normal: {
                label: {
                    show: chartOpt.mark
                }
            }
        };
        return [
            {name: "电脑", type: "bar", data: [], markPoint: markPoint, markLine: markLine, itemStyle: itemStyle},
            {name: "即开", type: "bar", data: [], markPoint: markPoint, markLine: markLine, itemStyle: itemStyle},
            {name: "中福在线", type: "bar", data: [], markPoint: markPoint, markLine: markLine, itemStyle: itemStyle},
            {name: "总量", type: "bar", data: [], markPoint: markPoint, markLine: markLine, itemStyle: itemStyle}
        ];
    };

    var timelineOpt = function (options, data, timeFormat) {
        return {
            options: options,
            timeline: {
                data: data,
                y2: 50,
                label: {formatter: timeFormat},
                symbolSize: 6,
                checkpointStyle: {
                    symbol: 'auto',
                    symbolSize: 6,
                    color: '#337ab6',
                    bordercolor: '#337ab6',
                    label: {show: false, textStyle: {color: 'auto'}}
                },
                autoPlay: false,
                playInterval: 2000
            }
        };
    };

    var seriesOpt = function (x, series, tooltipOption) {
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
            backgroundColor: 'rgba(255, 255, 255, 1)',
            dataZoom: {show: true},
            grid: {y: 50, y2: 150},
            xAxis: [{type: 'category', 'axisLabel': {'interval': 0}, data: x}],
            yAxis: [{name: tooltipOption.unit, show: true, type: 'value'}],
            tooltip: {
                trigger: 'axis',
                formatter: function (params) {
                    var res = tooltipOption.title + '(' + params[0].name + '):<table>';
                    for (var i = 0, l = params.length; i < l; i++) {
                        var val = params[i].value;
                        val = typeof(val) == 'string' ? val : (val.toFixed(2) + tooltipOption.unit);
                        res += '<tr><td>' + params[i].seriesName + '</td><td>:</td><td>' + val + '</td></tr>';
                    }
                    return res + "</table>";
                }
            },
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
                x: 'right',
                selected: {'电脑': false, '即开': false, '中福在线': false, '总量': true},
                data: ['电脑', '即开', '中福在线', '总量']
            },
            series: series
        };
    };

    var pushSeries = function (series, d, populationAvg) {
        var correct = function (i) {
            return i < 0 ? undefined : i;
        };
        series[0].data.push(correct(populationAvg.data(d.s1)));
        series[1].data.push(correct(populationAvg.data(d.s2)));
        series[2].data.push(correct(populationAvg.data(d.s3)));
        series[3].data.push(correct(populationAvg.data(d.stotal)));
    };

    var formatter = [
        function (s) {
            var tokens = s.split("/");
            return tokens[0] + "年";
        },
        function (s) {
            var tokens = s.split("/");
            return tokens[0] + "年第" + (+tokens[1]) / 3 + "季度"
        },
        function (s) {
            var tokens = s.split("/");
            return tokens[1] == '01' ? (tokens[0] + "-" + tokens[1]) : tokens[1];
        }
    ];

    var setOption = function (opt) {
        currentOpt = opt;
        chart.setOption(opt);
    };

    var populationAvgs = function (data) {
        return [
            {
                data: function (d) {
                    return d;
                },
                title: '彩票销量(非人均)',
                unit: '元'
            }, {
                data: function (d) {
                    return data.population1 > 0 ? d / data.population1 * 1000 : -1;
                },
                title: '户籍人口人均彩票销量',
                unit: '元/千人'
            }, {
                data: function (d) {
                    return data.population2 > 0 ? d / data.population2 : -1;
                },
                title: '来沪人口人均彩票销量',
                unit: '元/千人'
            }
        ];
    };

    chart.on(echarts.config.EVENT.MAGIC_TYPE_CHANGED, function (param) {
        calculable = param.magicType.bar;
        if (currentOpt.options === undefined) {
            currentOpt.calculable = calculable;
        } else {
            for (var i in currentOpt.options) {
                currentOpt.options[i].calculable = calculable;
            }
        }
        chart.setOption(currentOpt);
    });


    this.timeSaleGraphUpdate = function (chartOpt, popmod) {
        if (graphData == null || graphData.length <= 0) return;

        var data, d, i, times = [], series, populationAvg, timeFormat = function (s) {
            return formatter[chartOpt.dimen](s);
        };
        if (graphData.length == 1) {
            series = newSeries(chartOpt);
            data = graphData[0];
            populationAvg = populationAvgs(data)[chartOpt.population];
            data = [data.yearData, data.quarterData, data.monthData][chartOpt.dimen];
            for (i in data) {
                d = data[i];
                pushSeries(series, d, populationAvg);
                times.push(timeFormat(d.time));
            }
            setOption(seriesOpt(times, series, populationAvg));
        } else {
            var x = [];
            for (i in graphData) {
                data = graphData[i];
                x.push(data.unitName);

                data = [data.yearData, data.quarterData, data.monthData][chartOpt.dimen];
                for (d in data) {
                    times[data[d].time] = d;
                }
            }
            var timeline = [];
            var options = [];
            for (var time in times) {
                timeline.push(time);
                series = newSeries(chartOpt);
                for (i in graphData) {
                    data = graphData[i];
                    populationAvg = populationAvgs(data)[chartOpt.population];
                    data = [data.yearData, data.quarterData, data.monthData][chartOpt.dimen];
                    pushSeries(series, data[times[time]], populationAvg);
                }
                options.push(seriesOpt(x, series, populationAvg));
            }
            setOption(timelineOpt(options, timeline, timeFormat));
        }
    };

    this.compareSaleGraphUpdate = function (chartOpt) {
        var timeline = chartOpt.dimen ? graphData.month : graphData.year,
            data = chartOpt.dimen ? graphData.monthData : graphData.yearData,
            xData = chartOpt.dimen ? graphData.year : graphData.month,
            options = [], series, x, i, d, timeFormatter = function (s) {
                return s + ( chartOpt.dimen ? "月" : "年");
            }, populationAvg;

        for (i in timeline) {
            series = newSeries(chartOpt);
            x = [];
            populationAvg = populationAvgs(graphData)[chartOpt.population];
            for (d in xData) {
                x.push(xData[d] + ( chartOpt.dimen ? "年" : "月"));
                pushSeries(series, data[i][d] == undefined ? {} : data[i][d], populationAvg);
            }
            options.push(seriesOpt(x, series, populationAvg));
        }
        setOption(timelineOpt(options, timeline, timeFormatter));
    };

    this.setGraphData = function (d) {
        graphData = d;
    }
}