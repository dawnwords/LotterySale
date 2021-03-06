/**
 * Created by Dawnwords on 2015/5/15.
 */

$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".unit-content:first").height((viewHeight - 285) * 0.6);
    $(".unit-content:last").height((viewHeight - 285) * 0.4);
    $("#chart").height(viewHeight - 272);
    $("#table").height(viewHeight - 272);

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
        year = $("#funcbar-warning-year"),
        month = $("#funcbar-warning-month"),
        unitInfoPopup = $("#show-unit"),
        visibility = [
            [chart, time, population, view, refresh],
            [chart, compare, population, view, refresh],
            [table, warning, search]
        ], dataTable;


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

        for (var i in visibility[mode]) {
            visibility[mode][i].removeClass("hide");
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

    function yearMonthSelect() {
        $.ajax({
            url: "../year",
            type: 'GET',
            success: function (data) {
                for (var i in data) {
                    year.append($("<option>").val(i).text(i));
                }
                year.change(function () {
                    month.html("");
                    var m = data[year.val()];
                    for (var i in m) {
                        month.append($("<option>").val(m[i]).text(m[i]));
                    }
                });
                year.change();
            }
        });
    }

    function clickSearch() {
        if (dataTable) {
            dataTable.ajax.reload(null, false);
        } else {
            dataTable = table.find("table").DataTable({
                scrollY: viewHeight - 315,
                scrollCollapse: true,
                serverSide: true,
                searchHighlight: true,
                columns: [
                    {title: "序号"},
                    {title: "区代码"},
                    {title: "地址"},
                    {title: "上月总量"},
                    {title: "该月总量"},
                    {title: "增幅"}
                ],
                ajax: {
                    url: '../wsale',
                    type: 'POST',
                    contentType: 'application/json',
                    data: function (o) {
                        o.year = year.val();
                        o.month = month.val();
                        return JSON.stringify(o);
                    },
                    dataSrc: function (json) {
                        for (var i = 0, ien = json.data.length; i < ien; i++) {
                            json.data[i][5] = (json.data[i][5] * 100).toFixed(1) + "%";
                        }
                        return json.data;
                    }
                },
                language: {
                    emptyTable: "数据不可用",
                    info: "第_START_至第_END_项，共_TOTAL_项",
                    infoEmpty: "无项目",
                    infoFiltered: "(从总计_MAX_项中过滤)",
                    infoPostFix: "",
                    thousands: ",",
                    lengthMenu: "显示_MENU_项目",
                    loadingRecords: "载入中...",
                    processing: "处理中...",
                    search: "关键字查询：",
                    zeroRecords: "无匹配项",
                    paginate: {
                        first: "首页",
                        last: "尾页",
                        next: "下一页",
                        previous: "上一页"
                    },
                    aria: {
                        sortAscending: ": 升序",
                        sortDescending: ": 降序"
                    }
                }
            });
            table.on("click", "tr", function () {
                $.ajax({
                    url: '../unitinfo',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        unitId: dataTable.rows($(this)).data()[0][0]
                    }),
                    success: function (data) {
                        if(data.unitInfo && data.unitInfo.length > 0){
                            var html = "";
                            for (var i in data.unitInfo) {
                                var info = data.unitInfo[i];
                                html += '<div class="form-group">' +
                                    '   <label for="field-' + info.title + '" class="col-xs-2 control-label">' + info.titleCh + ':</label>' +
                                    '   <div class="col-xs-10">' +
                                    '       <input type="text" class="form-control" id="field-' + info.title + '" value="' + info.value + '" readonly>'+
                                    '    </div>' +
                                    '</div>';
                            }
                            unitInfoPopup.find("form").html(html);
                            unitInfoPopup.modal('show');
                        }
                    }
                })
            });
        }
    }

    changePassHint.css('display', 'none');
    changePassBtn.click(changePassword);

    tab.click(clickTab);

    refresh.click(clickRefresh);
    search.click(clickSearch);

    maxmin.click(updateGraph);
    avg.click(updateGraph);
    mark.click(updateGraph);
    time.find("input[name=time]").change(updateGraph);
    compare.find("input[name=compare]").change(updateGraph);
    population.find("input[name=population]").change(updateGraph);

    deselect.click(tree.deselectAll);

    yearMonthSelect();
});

