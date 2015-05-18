/**
 * Created by Dawnwords on 2015/5/15.
 */

$(document).ready(function () {
    $(".unit-content").height((document.body.scrollHeight - 236) / 2);
    $("#chart").height(document.body.scrollHeight - 252);

    var mode = 0,
        tree = new Unit("#unit-tree", "#unit-selected"),
        hint = new Hint("#unit", "同环比只支持选中一个单位", 2400),
        chart = new Chart('chart'),
        tab = $("#nav-bar").find("a"),
        time = $("#funcbar-time"),
        compare = $("#funcbar-compare"),
        refresh = $("#funcbar-refresh"),
        maxmin = $("#funcbar-view-maxmin"),
        avg = $("#funcbar-view-avg"),
        mark = $("#funcbar-view-mark"),
        deselect = $(".unit-deselect");

    function postAjax(url, data) {
        $.ajax({
            url: url,
            dataType: 'json',
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(data),
            success: function (data) {
                chart.setGraphData(data);
            }
        });
    }

    function updateGraph() {
        chart.updateGraph(maxmin.is(":checked"), avg.is(":checked"), mark.is(":checked"));
    }

    function clickTab() {
        mode = $(this).data("mode");
        if (mode) {
            time.addClass("hide");
            compare.removeClass("hide");
        } else {
            time.removeClass("hide");
            compare.addClass("hide");
        }
        refresh.trigger('click');
    }

    function clickRefresh() {
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
    }

    tab.click(clickTab);

    refresh.click(clickRefresh);

    maxmin.click(updateGraph);
    avg.click(updateGraph);
    mark.click(updateGraph);
    time.find("input[name=time]").change(updateGraph);
    compare.find("input[name=compare]").change(updateGraph);

    deselect.click(tree.deselectAll);
});

