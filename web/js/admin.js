/**
 * Created by Dawnwords on 2015/6/9.
 */
$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".data-view").height(viewHeight - 200);

    var tab = $("#nav-bar").find("a"),
        popup = $("#modify"),
        tables;

    function Table(tableName, titleName, fields) {
        var columns = [{title: "id"}];
        for (var i = 0; i < fields.length; i++) {
            columns.push({title: fields[i].name});
        }

        var buildSelectHtml = function (field, origin) {
            var html = '<div class="form-group">' +
                '    <label for="field-' + field.name + '" class="col-xs-2 control-label">' + field.name + ':</label>' +
                '    <div class="col-xs-10">' +
                '        <select class="form-control" id="field-' + field.name + '">';
            for (var i in field.options) {
                var option = field.options[i];
                html += '<option' + (option == origin ? ' selected' : '') + '>' + option + '</option>';
            }
            html += '        </select>' +
                '    </div>' +
                '</div>';
            return html;
        };

        var buildInputHtml = function (field, origin, disabled) {
            return '<div class="form-group">' +
                '    <label for="field-' + field.name + '" class="col-xs-2 control-label">' + field.name + ':</label>' +
                '    <div class="col-xs-10">' +
                '        <input type="text" class="form-control" id="field-' + field.name + '" placeholder="' + origin + '"' + (disabled ? " readonly" : "") + '>' +
                '    </div>' +
                '</div>';
        };

        var table = $("#table-" + tableName).DataTable({
            scrollY: viewHeight - 315,
            scrollCollapse: true,
            serverSide: true,
            searchHighlight: true,
            columns: columns,
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
            $("#modify-title").html("修改" + titleName + "数据");
            var tempFields = fields.slice(),
                form = popup.find("form"),
                html = "",
                rowData = table.rows($(this)).data()[0];
            tempFields.unshift({name: "id", type: "disabled"});
            for (var i in tempFields) {
                var field = tempFields[i];
                var origin = rowData[i];
                if (field.type == "select") {
                    html += buildSelectHtml(field, origin);
                } else {
                    html += buildInputHtml(field, origin, field.type == "disabled");
                }
            }
            form.html(html);
            popup.modal('show');
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

    tab.click(clickTab);
    tables = [
        new Table('unit', "节点", [
            {name: "name", type: "input"},
            {name: "unitcode", type: "input"},
            {name: "address", type: "input"},
            {name: "manager", type: "input"},
            {name: "mobile", type: "input"},
            {name: "unitnum", type: "disabled"},
            {name: "population1", type: "input"},
            {name: "population2", type: "input"}
        ]),
        new Table('sale', "销量", [
            {name: "unitid", type: "disabled"},
            {name: "saleyear", type: "disabled"},
            {name: "salequarter", type: "disabled"},
            {name: "salemonth", type: "disabled"},
            {name: "s1", type: "input"},
            {name: "s2", type: "input"},
            {name: "s3", type: "input"},
            {name: "stotal", type: "input"}
        ]),
        new Table('user', "用户", [
            {name: "name", type: "disabled"},
            {name: "authority", type: "select", options: ["Admin", "Normal"]}
        ])];
});