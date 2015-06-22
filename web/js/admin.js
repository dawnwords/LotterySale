/**
 * Created by Dawnwords on 2015/6/9.
 */
$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".data-view").height(viewHeight - 200);

    var tab = $("#nav-bar").find("a"),
        modifyPopup = $("#modify"),
        modifySubmitBtn = $("#modify-submit"),
        modifyTitle = $("#modify-title"),
        addPopup = $("#add"),
        addSubmitBtn = $("#add-submit"),
        addTitle = $("#add-title"),
        deleteBtn = $("#modify-delete"),
        refreshNonLeafBtn = $("#function-refresh"),
        addBtn = $("#function-add"),
        uploadBtn = $("#upload-button"),
        uploadResult = $("#upload-result"),
        uploadFails = $("#upload-fails"),
        uploadPopup = $("#upload"),
        tables = [
            new Table('unit', "节点", [
                {
                    title: "fatherid",
                    addType: "select",
                    ajax: {
                        url: "../admin/fatheroption",
                        type: 'GET',
                        success: function (data) {
                            var options = "",
                                prefix = ["", "┗", "&nbsp;&nbsp;┗"],
                                select = $("#add-field-fatherid");
                            for (var i in data) {
                                var option = data[i];
                                options += "<option value='" + option.id + "' data-level=" + option.level + ">"
                                    + prefix[option.level] + option.name + "</option>";
                            }
                            select.html(options);
                            select.change(function () {
                                var level = select.find("option:selected").data("level");
                                $("#add-field-population1").prop('readonly', level != 1);
                                $("#add-field-population2").prop('readonly', level != 1);
                                $("#add-field-area").prop('readonly', level != 1);
                            });
                            select.change();
                        }
                    }
                },
                {title: "name", addType: "input"},
                {title: "unitcode", addType: "input"},
                {title: "address", addType: "input"},
                {title: "manager", addType: "input"},
                {title: "mobile", addType: "input"},
                {title: "unitnum", addType: false},
                {title: "area", addType: "input"},
                {title: "population1", addType: "input"},
                {title: "population2", addType: "input"}
            ], {unitid: 0}),
            new Table('sales', "销量", [
                {title: "unitid", addType: "input"},
                {
                    title: "saleyear",
                    addType: "select",
                    options: [2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024]
                },
                {title: "salequarter", addType: "disabled"},
                {
                    title: "salemonth",
                    addType: "select",
                    options: ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
                    associate: function () {
                        var month = +$("#add-field-salemonth").val();
                        $("#add-field-salequarter").val(Math.floor((month + 2) / 3));
                    }
                },
                {title: "s1", addType: "input"},
                {title: "s2", addType: "input"},
                {title: "s3", addType: "input"},
                {title: "stotal", addType: "input"}
            ], {unitid: 1}),
            new Table('user', "用户", [
                {title: "name", ftype: "disabled", addType: "input"},
                {
                    title: "authority",
                    ftype: "select",
                    addType: "select",
                    options: ["Admin", "Normal"]
                }
            ], false)];

    function showModifyPopup(fields, rowData) {
        var form = modifyPopup.find("form"),
            html = "";
        for (var i in fields) {
            var field = fields[i];
            var origin = rowData[i];
            html += '<div class="form-group" data-field="' + field.title + '" data-origin="' + origin + '">' +
                '    <label for="field-' + field.title + '" class="col-xs-2 control-label">' + field.title + ':</label>' +
                '    <div class="col-xs-10">';
            if (field.ftype == "select") {
                html += '        <select class="form-control" id="field-' + field.title + '">';
                for (var j in field.options) {
                    var option = field.options[j];
                    html += '<option value="' + option + '"' + (option == origin ? ' selected' : '') + '>' + option + '</option>';
                }
                html += '        </select>';
            } else {
                html += '<input type="text" class="form-control" id="field-' + field.title + '" value="' + origin + '"'
                    + (field.ftype == "disabled" ? " readonly" : "") + '>';
            }

            html += '    </div>' +
                '</div>';
        }
        form.html(html);
        modifyPopup.modal('show');
    }

    function showAddPopup() {
        var form = addPopup.find("form"),
            table = currentTable(),
            fields = table.fields,
            html = "",
            later = [],
            createLater = function (field) {
                return function () {
                    var select = $('#add-field-' + field.title);
                    select.change(field.associate);
                    select.change();
                }
            };

        addTitle.html("新增" + table.titleName + "数据");
        for (var i in fields) {
            var field = fields[i];
            if (field.addType) {
                html += '<div class="form-group" data-field="' + field.title + '">' +
                    '    <label for="add-field-' + field.title + '" class="col-xs-2 control-label">' + field.title + ':</label>' +
                    '    <div class="col-xs-10">';
                if (field.addType == "select") {
                    html += '        <select class="form-control" id="add-field-' + field.title + '">';
                    if (field.options) {
                        for (var j in field.options) {
                            var option = field.options[j];
                            html += '<option value="' + option + '">' + option + '</option>';
                        }
                    }
                    if (field.ajax) {
                        $.ajax(field.ajax);
                    }
                    if (field.associate) {
                        later[i] = createLater(field);
                    }
                    html += '        </select>';
                } else {
                    html += '<input type="text" class="form-control" id="add-field-' + field.title + '"'
                        + (field.addType == "disabled" ? " readonly" : "") + '>';
                }
                html += '    </div>' +
                    '</div>';
            }
        }

        form.html(html);
        addPopup.modal('show');

        for (var i in later) {
            later[i]();
        }
    }

    function currentTable() {
        return tables[$("#nav-bar").find(".active a").data("tab")];
    }

    function Table(tableName, titleName, fields, unitid) {
        var tempFields = fields.slice();
        tempFields.unshift({title: "id", ftype: "disabled"});

        var table = $("#table-" + tableName).DataTable({
            scrollY: viewHeight - 315,
            scrollCollapse: true,
            serverSide: true,
            searchHighlight: true,
            columns: tempFields,
            ajax: {
                url: '../admin/table',
                type: 'POST',
                contentType: 'application/json',
                data: function (o) {
                    o.table = tableName;
                    return JSON.stringify(o);
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
            var rowData = table.rows($(this)).data()[0];

            modifyTitle.html("修改" + titleName + "数据");
            modifyTitle.data('id', rowData[0]);
            modifyTitle.data('table', tableName);

            if (unitid) {
                $.ajax({
                    url: '../admin/tablefield',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        unitid: rowData[unitid.unitid],
                        table: tableName
                    }),
                    success: function (data) {
                        showModifyPopup(data, rowData);
                    }
                })
            } else {
                showModifyPopup(fields, rowData);
            }
        });

        this.name = tableName;
        this.table = table;
        this.titleName = titleName;
        this.fields = fields;
    }

    function clickTab() {
        var table = tables[$(this).data("tab")];
        $('.data-view').removeClass('active');
        $("#" + table.name).addClass('active');
        table.table.draw();
    }

    function reloadTable() {
        currentTable().table.ajax.reload(null, false);
    }

    function displayMsg(result, popup, msg, success) {
        result.html(msg);
        result.removeClass('success glyphicon-ok-sign fail glyphicon-remove-sign');
        result.addClass(success ? 'success glyphicon-ok-sign' : 'fail glyphicon-remove-sign');
        result.fadeIn("fast", function () {
            $(this).delay(1000).fadeOut("slow", function () {
                if (success) {
                    popup.modal('hide');
                    reloadTable();
                }
            });
        });
    }

    function updateDisplayMsg(msg, success) {
        displayMsg($(".modify.result"), modifyPopup, msg, success);
    }

    function addDisplayMsg(msg, success) {
        displayMsg($(".add.result"), addPopup, msg, success);
    }

    function submitUpdate() {
        var updates = [],
            id = modifyTitle.data('id'),
            table = modifyTitle.data('table');
        modifyPopup.find(".form-group").each(function () {
            var update = $(this).find("div").children().first().val();
            if (update == undefined || update == "null") {
                update = null;
            }
            updates.push({
                field: $(this).data('field'),
                origin: $(this).data('origin'),
                update: update
            });
        });
        $.ajax({
            url: '../admin/updatetable',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                id: id,
                table: table,
                updates: updates
            }),
            success: function (data) {
                if (data == 'success') {
                    updateDisplayMsg("修改成功", true);
                } else {
                    updateDisplayMsg('修改失败', false);
                }
            }
        });
    }

    function submitDelete() {
        var id = modifyTitle.data('id'),
            table = modifyTitle.data('table');
        $.ajax({
            url: '../admin/deletetable',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                id: id,
                table: table
            }),
            success: function (data) {
                if (data == 'success') {
                    updateDisplayMsg("删除成功", true);
                } else {
                    updateDisplayMsg('删除失败' + (data == 'fail' ? '：删除对象包含子节点' : ''), false);
                }

            }
        })
    }

    function submitRefresh() {
        refreshNonLeafBtn.button('loading');
        $.ajax({
            url: '../admin/refreshtable',
            type: 'POST',
            contentType: 'application/json',
            data: currentTable().name,
            success: function () {
                refreshNonLeafBtn.button('reset');
                reloadTable();
            }
        })
    }

    function submitAdd() {
        var adds = [],
            table = currentTable().name;
        addPopup.find(".form-group").each(function () {
            var add = $(this).find("div").children().first().val();
            if (add == undefined || add == "null") {
                add = null;
            }
            adds.push({
                field: $(this).data('field'),
                value: add
            });
        });
        $.ajax({
            url: '../admin/addtable',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                table: table,
                adds: adds
            }),
            success: function (data) {
                if (data == 'success') {
                    addDisplayMsg("修改成功", true);
                } else {
                    addDisplayMsg('修改失败', false);
                }
            }
        });
    }

    tab.click(clickTab);
    addBtn.click(showAddPopup);
    addSubmitBtn.click(submitAdd);
    deleteBtn.click(submitDelete);
    modifySubmitBtn.click(submitUpdate);
    refreshNonLeafBtn.click(submitRefresh);
    $("#upload-input").fileupload({
        url: '../admin/upload',
        submit: function () {
            uploadResult.removeClass("show");
            uploadResult.addClass("hidden");
            uploadBtn.find("span:last").html("正在导入...");
            uploadBtn.prop("disabled", true);
        },
        always: function (e, data) {
            uploadResult.removeClass("hidden");
            uploadResult.addClass("show");
            data = data.result;
            if (data.error) {
                uploadResult.find(".fail").html("失败:" + data.error);
            } else {
                uploadResult.find(".success").html("成功:" + data.success);
                uploadResult.find(".fail").html("失败:" + data.fails.length);
                var fails = "";
                for (var i in data.fails) {
                    fails += "<p class='result fail'>" + (i + 1) + ":" + data.fails[i] + "</p>";
                }
            }
            uploadFails.html(fails);
            uploadBtn.find("span:last").html("选择文件");
            uploadBtn.prop("disabled", false);
            uploadPopup.modal('show');
        }
    });
});
