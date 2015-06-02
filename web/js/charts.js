/**
 * Created by Dawnwords on 2015/5/15.
 */
function Chart(elementId) {
    var graphData = [];
    var chart = echarts.init(document.getElementById(elementId));
    var calculable = true;
    var currentOpt;

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
            dataZoom: {show: true},
            grid: {y: 50, y2: 150},
            xAxis: [{type: 'category', 'axisLabel': {'interval': 0}, data: x}],
            yAxis: [{name: '销量', show: true, type: 'value'}],
            tooltip: {
                trigger: 'axis',
                formatter: function (params) {
                    var res = tooltipOption.title + '(' + params[0].name + '):<table>';
                    for (var i = 0, l = params.length; i < l; i++) {
                        var val = params[i].value;
                        val = typeof(val) == 'string' ? val : tooltipOption.format(val);
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
                x: 'left',
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

    var isArray = function (o) {
        return Object.prototype.toString.call(o) === '[object Array]';
    };

    var setOption = function (opt) {
        currentOpt = opt;
        chart.setOption(opt);
    };

    var populationAvgs = function (data) {
        var avgFormatter = function (d) {
            d *= 1000;
            return d.toFixed(2) + "元/千人";
        };
        return [
            {
                data: function (d) {
                    return d;
                },
                title: '彩票销量（非人均）',
                format: function (d) {
                    return d + "元";
                }
            }, {
                data: function (d) {
                    return data.population1 > 0 ? d / data.population1 : -1;
                },
                title: '户籍人口人均彩票销量',
                format: avgFormatter
            }, {
                data: function (d) {
                    return data.population2 > 0 ? d / data.population2 : -1;
                },
                title: '来沪人口人均彩票销量',
                format: avgFormatter
            }
        ];
    };

    var updateSaleGraph = function (dimen, maxmin, avg, mark, popmod) {
        var data, d, i, times = [], series, populationAvg, timeFormat = function (s) {
            return formatter[dimen](s);
        };
        if (graphData.length == 1) {
            series = newSeries(maxmin, avg, mark);
            data = graphData[0];
            populationAvg = populationAvgs(data)[popmod];
            data = [data.yearData, data.quarterData, data.monthData][dimen];
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
                    populationAvg = populationAvgs(data)[popmod];
                    data = [data.yearData, data.quarterData, data.monthData][dimen];
                    pushSeries(series, data[times[time]], populationAvg);
                }
                options.push(seriesOpt(x, series, populationAvg));
            }
            setOption(timelineOpt(options, timeline, timeFormat));
        }
    };

    var updateCompareSaleGraph = function (dimen, maxmin, avg, mark, popmod) {
        var timeline = dimen ? graphData.month : graphData.year,
            data = dimen ? graphData.monthData : graphData.yearData,
            xData = dimen ? graphData.year : graphData.month,
            options = [], series, x, i, d, timeFormatter = function (s) {
                return s + (dimen ? "月" : "年");
            }, populationAvg;

        for (i in timeline) {
            series = newSeries(maxmin, avg, mark);
            x = [];
            populationAvg = populationAvgs(graphData)[popmod];
            for (d in xData) {
                x.push(xData[d] + (dimen ? "年" : "月"));
                pushSeries(series, data[i][d] == undefined ? {} : data[i][d], populationAvg);
            }
            options.push(seriesOpt(x, series, populationAvg));
        }
        setOption(timelineOpt(options, timeline, timeFormatter));
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

    this.updateGraph = function (maxmin, avg, mark) {
        if (graphData == null) return;

        chart.clear();
        var dimen, population = +$("input[name=population]:checked", "#funcbar-population").val();
        if (isArray(graphData)) {
            if (graphData.length > 0) {
                dimen = +$("input[name=time]:checked", "#funcbar-time").val();
                updateSaleGraph(dimen, maxmin, avg, mark, population);
            }
        } else {
            dimen = +$("input[name=compare]:checked", "#funcbar-compare").val();
            updateCompareSaleGraph(dimen, maxmin, avg, mark, population);
        }
    };

    this.setGraphData = function (d) {
        graphData = d;
        this.updateGraph();
    }
}