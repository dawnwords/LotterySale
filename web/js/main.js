/**
 * Created by Dawnwords on 2015/5/15.
 */
$(document).ready(function () {
    var mode = 0,
        tree = $("#unit-tree").jstree(treeOpt),
        time = $("#funcbar-time"),
        compare = $("#funcbar-compare"),
        refresh = $("#funcbar-refresh"),
        maxmin = $("#funcbar-view-maxmin"),
        avg = $("#funcbar-view-avg"),
        mark = $("#funcbar-view-mark");

    chart = echarts.init(document.getElementById('chart'));

    tree.bind("changed.jstree", check_unit);

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
        var unitid = tree.jstree('get_selected');
        if (mode) {
            if (unitid.length == 1 && +$("#unit-tree").jstree('get_node',unitid,true).attr('aria-level') == 4) {
                console.log(tree.jstree('get_node', unitid[0]));
                postAjax("../comparesale", {unitId: unitid[0]});
            } else {
                //TODO handle multi-unit choice
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