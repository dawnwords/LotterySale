/**
 * Created by Dawnwords on 2015/6/9.
 */
$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".data-view").height(viewHeight - 200);

    var tab = $("#nav-bar").find("a"),
        tableUnit = $("#table-unit");

    function clickTab() {
        var tabId = ['unit', 'sale', 'user'][$(this).data("tab")];
        $('.data-view').removeClass('active');
        $("#" + tabId).addClass('active');
    }

    tab.click(clickTab);
    tableUnit.DataTable({
        scrollY: viewHeight - 315,
        scrollCollapse: true,
        serverSide: true,
        ajax: {
            url: '../admin/tabunit',
            type: 'POST',
            contentType: 'application/json',
            data: function (o) {
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
});