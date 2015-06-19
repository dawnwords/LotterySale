/**
 * Created by Dawnwords on 2015/6/9.
 */
$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".data-view").height(viewHeight - 200);

    var tab = $("#nav-bar").find("a"),
        popup = $("#modify"),
        updateBtn = $("#modify-submit"),
        deleteBtn = $("#modify-delete"),
        modifyTitle = $("#modify-title"),
        modifyResult = $(".modify-result"),
        tables = [
            new Table('unit', "节点", [
                {title: "name"},
                {title: "unitcode"},
                {title: "address"},
                {title: "manager"},
                {title: "mobile"},
                {title: "unitnum"},
                {title: "area"},
                {title: "population1"},
                {title: "population2"}
            ], {unitid: 0}),
            new Table('sales', "销量", [
                {title: "unitid"},
                {title: "saleyear"},
                {title: "salequarter"},
                {title: "salemonth"},
                {title: "s1"},
                {title: "s2"},
                {title: "s3"},
                {title: "stotal"}
            ], {unitid: 1}),
            new Table('user', "用户", [
                {title: "name", ftype: "disabled"},
                {title: "authority", ftype: "select", options: ["Admin", "Normal"]}
            ], false)];

    function showPopup(fields, rowData) {
        var form = popup.find("form"),
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
                    html += '<option value=' + option + ' "' + (option == origin ? ' selected' : '') + '>' + option + '</option>';
                }
                html += '        </select>'
            } else {
                html += '<input type="text" class="form-control" id="field-' + field.title + '" value="' + origin + '"'
                    + (field.ftype == "disabled" ? " readonly" : "") + '>';
            }

            html += '    </div>' +
                '</div>';
        }
        form.html(html);
        popup.modal('show');
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
                        showPopup(data, rowData);
                    }
                })
            } else {
                showPopup(fields, rowData);
            }
        });

        this.name = tableName;
        this.table = table;
    }

    function clickTab() {
        var table = tables[$(this).data("tab")];
        $('.data-view').removeClass('active');
        $("#" + table.name).addClass('active');
        table.table.draw();
    }

    function updateDisplayMsg(msg, table) {
        modifyResult.html(msg);
        modifyResult.removeClass('success glyphicon-ok-sign fail glyphicon-remove-sign');
        modifyResult.addClass(table ? 'success glyphicon-ok-sign' : 'fail glyphicon-remove-sign');
        modifyResult.fadeIn("fast", function () {
            $(this).delay(1000).fadeOut("slow", function () {
                if (table) {
                    popup.modal('hide');
                }
            });
        });
        if (table) {
            tables[{unit: 0, sales: 1, user: 2}[table]].table.ajax.reload(null, false);
        }
    }

    function submitUpdate() {
        var updates = [],
            id = modifyTitle.data('id'),
            table = modifyTitle.data('table');
        $(".form-group").each(function () {
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
                    updateDisplayMsg("修改成功", table);
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
                    updateDisplayMsg("删除成功", table);
                } else {
                    updateDisplayMsg('删除失败' + (data == 'fail' ? '：删除对象包含子节点' : ''), false);
                }

            }
        })
    }

    tab.click(clickTab);
    updateBtn.click(submitUpdate);
    deleteBtn.click(submitDelete);
});