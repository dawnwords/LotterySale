/**
 * Created by Dawnwords on 2015/5/15.
 */
$(document).ready(function () {
    var mode = 0,
        tree = new Unit("#unit-tree", "#unit-selected"),
        time = $("#funcbar-time"),
        compare = $("#funcbar-compare"),
        refresh = $("#funcbar-refresh"),
        maxmin = $("#funcbar-view-maxmin"),
        avg = $("#funcbar-view-avg"),
        mark = $("#funcbar-view-mark"),
        hint = new Hint("#unit", "同环比只支持选中一个单位");

    chart = echarts.init(document.getElementById('chart'));

    $("#nav-bar a").click(function () {
        mode = $(this).data("mode");
        if (mode) {
            time.addClass("hide");
            compare.removeClass("hide");
        } else {
            time.removeClass("hide");
            compare.addClass("hide");
        }
        refresh.trigger('click');
    });

    time.find("input[name=time]").change(updateGraph);
    compare.find("input[name=compare]").change(updateGraph);

    refresh.click(function () {
        var unitid = tree.getSelectedIds();
        if (mode) {
            if (unitid.length == 1 && tree.getNodeLevelById(unitid[0]) == 4) {
                console.log(tree.getNodeById(unitid[0]));
                postAjax("../comparesale", {unitId: unitid[0]});
            } else {
                hint.show();
            }
        } else {
            postAjax("../sale", {unitId: unitid});
        }
    });

    maxmin.click(updateGraph);
    avg.click(updateGraph);
    mark.click(updateGraph);

});

var postAjax = function (url, data) {
    $.ajax({
        url: url,
        dataType: 'json',
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(data),
        success: function (d) {
            graphData = d;
            updateGraph();
        }
    });
};
