/**
 * Created by Dawnwords on 2015/6/9.
 */
$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".data-view").height(viewHeight - 200);

    var tab = $("#nav-bar").find("a"),
        popup = $("#modify"),
        updateBtn = $("#modify-submit"),
        modifyTitle = $("#modify-title"),
        modifyResult = $(".modify-result"),
        tables;

    function Table(tableName, titleName, fields) {
        var columns = [{title: "id"}];
        for (var i = 0; i < fields.length; i++) {
            columns.push({title: fields[i].name});
        }

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
            var tempFields = fields.slice(),
                form = popup.find("form"),
                html = "",
                rowData = table.rows($(this)).data()[0];

            modifyTitle.html("修改" + titleName + "数据");
            modifyTitle.data('id', rowData[0]);
            modifyTitle.data('table', tableName);
            tempFields.unshift({name: "id", type: "disabled"});
            for (var i in tempFields) {
                var field = tempFields[i];
                var origin = rowData[i];
                html += '<div class="form-group" data-field="' + field.name + '" data-origin="' + origin + '">' +
                    '    <label for="field-' + field.name + '" class="col-xs-2 control-label">' + field.name + ':</label>' +
                    '    <div class="col-xs-10">';
                if (field.type == "select") {
                    html += '        <select class="form-control" id="field-' + field.name + '">';
                    for (var j in field.options) {
                        var option = field.options[j];
                        html += '<option value=' + option + ' "' + (option == origin ? ' selected' : '') + '>' + option + '</option>';
                    }
                    html += '        </select>'
                } else {
                    html += '<input type="text" class="form-control" id="field-' + field.name + '" value="' + origin + '"'
                        + (field.type == "disabled" ? " readonly" : "") + '>';
                }

                html += '    </div>' +
                    '</div>';
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

    function updateDisplayMsg(msg, success) {
        modifyResult.html(msg);
        modifyResult.removeClass('success glyphicon-ok-sign fail glyphicon-remove-sign');
        modifyResult.addClass(success ? 'success glyphicon-ok-sign' : 'fail glyphicon-remove-sign');
        modifyResult.fadeIn("fast", function () {
            $(this).delay(1000).fadeOut("slow", function(){
                if(success){
                    popup.modal('hide');
                }
            });
        });
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
                    updateDisplayMsg("修改成功", true);
                    tables[{unit: 0, sales: 1, user: 2}[table]].table.ajax.reload();
                } else {
                    updateDisplayMsg('修改失败', false);
                }
            }
        });
    }

    tab.click(clickTab);
    updateBtn.click(submitUpdate);
    tables = [
        new Table('unit', "节点", [
            {name: "name", type: "input"},
            {name: "unitcode", type: "input"},
            {name: "address", type: "input"},
            {name: "manager", type: "input"},
            {name: "mobile", type: "input"},
            {name: "unitnum", type: "input"},
            {name: "area", type: "input"},
            {name: "population1", type: "input"},
            {name: "population2", type: "input"}
        ]),
        new Table('sales', "销量", [
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