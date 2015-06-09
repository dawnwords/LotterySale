/**
 * Created by Dawnwords on 2015/6/9.
 */
$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".data-view").height(viewHeight - 200);

    var tab = $("#nav-bar").find("a");

    function clickTab() {
        var tabId = ['unit', 'sale', 'user'][$(this).data("tab")];
        $('.data-view').removeClass('active');
        $("#" + tabId).addClass('active');
    }

    tab.click(clickTab);
});