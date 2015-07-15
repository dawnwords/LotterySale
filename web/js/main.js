/**
 * Created by Dawnwords on 2015/5/15.
 */

$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".unit-content:first").height((viewHeight - 285) * 0.6);
    $(".unit-content:last").height((viewHeight - 285) * 0.4);
    $("#chart").height(viewHeight - 272);

    var mode = 0,
        tree = new Unit("#unit-tree", "#unit-selected", "#unit-keyword"),
        unitHint = new Hint("#unit", 2400),
        chart = new Chart('chart'),
        tab = $("#nav-bar").find("a"),
        time = $("#funcbar-time"),
        compare = $("#funcbar-compare"),
        population = $("#funcbar-population"),
        refresh = $("#funcbar-refresh"),
        view = $("#funcbar-view"),
        search = $("#funcbar-search"),
        warning = $("#funcbar-warning"),
        table = $("#table"),
        maxmin = $("#funcbar-view-maxmin"),
        avg = $("#funcbar-view-avg"),
        mark = $("#funcbar-view-mark"),
        deselect = $("#unit-deselect"),
        changePassPopup = $("#change-password"),
        changePassBtn = $("#change-password-submit"),
        changePassHint = $(".change-pass"),
        visiability = [
            [chart, time, population, view, refresh],
            [chart, compare, population, view, refresh],
            [table, warning, search]
        ];

    function updateGraph() {
        var dimen = $("#nav-bar").find(".active").data("dimen");
        chart[dimen + "SaleGraphUpdate"]({
            maxmin: maxmin.is(":checked"),
            avg: avg.is(":checked"),
            mark: mark.is(":checked"),
            dimen: +$("input[name=" + dimen + "]:checked", "#funcbar-" + dimen).val(),
            population: +$("input[name=population]:checked", "#funcbar-population").val()
        });
    }

    function postAjax(url, data) {
        $.ajax({
            url: url,
            dataType: 'json',
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(data),
            success: function (data) {
                chart.setGraphData(data);
                updateGraph();
            }
        });
    }

    function clickTab() {
        mode = +$(this).data("mode");
        chart.addClass("hide");
        table.addClass("hide");
        time.addClass("hide");
        compare.addClass("hide");
        population.addClass("hide");
        warning.addClass("hide");
        view.addClass("hide");
        search.addClass("hide");
        refresh.addClass("hide");

        for(var i in visiability[mode]){
            visiability[mode][i].removeClass("hide");
        }

        tree.deselectAll();
        chart.setGraphData(null);
    }

    function clickRefresh() {
        var unitid = tree.getSelectedIds();
        if (unitid.length == 0) {
            unitHint.show("请至少选中一个单位");
        } else if (mode) {
            if (unitid.length == 1) {
                postAjax("../comparesale", {unitId: unitid[0]});
            } else {
                unitHint.show("同环比只支持选中一个单位");
            }
        } else {
            postAjax("../sale", {unitId: unitid});
        }
    }

    function passDisplayMsg(msg, success) {
        changePassHint.html(msg);
        changePassHint.removeClass('success glyphicon-ok-sign fail glyphicon-remove-sign');
        changePassHint.addClass(success ? 'success glyphicon-ok-sign' : 'fail glyphicon-remove-sign');
        changePassHint.fadeIn("fast", function () {
            $(this).delay(1000).fadeOut("slow", function () {
                if (success) {
                    changePassPopup.modal('hide');
                    changePassPopup.find('form')[0].reset();
                }
            });
        });
    }

    function changePassword() {
        var oldPass = $("#change-password-old").val() || "",
            newPass = $("#change-password-new").val() || "",
            confirmPass = $("#change-password-confirm").val() || "";

        if (oldPass.length == 0) {
            passDisplayMsg("请输入原始密码", false);
            return;
        }

        if (newPass.length == 0) {
            passDisplayMsg("请输入新密码", false);
            return;
        }

        if (confirmPass.length == 0) {
            passDisplayMsg("请确认新密码", false);
            return;
        }

        if (confirmPass != newPass) {
            passDisplayMsg("两次密码输入不一致", false);
            return;
        }

        $.ajax({
            url: "../changepass",
            dataType: "json",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify({
                oldPass: oldPass,
                newPass: newPass
            }),
            success: function (data) {
                if (data == 'success') {
                    passDisplayMsg("密码修改成功", true);
                } else {
                    passDisplayMsg('原始密码错误', false);
                }
            }
        });
    }

    changePassHint.css('display', 'none');
    changePassBtn.click(changePassword);

    tab.click(clickTab);

    refresh.click(clickRefresh);

    maxmin.click(updateGraph);
    avg.click(updateGraph);
    mark.click(updateGraph);
    time.find("input[name=time]").change(updateGraph);
    compare.find("input[name=compare]").change(updateGraph);
    population.find("input[name=population]").change(updateGraph);

    deselect.click(tree.deselectAll);


});

