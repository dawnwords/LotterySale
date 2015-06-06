/**
 * Created by Dawnwords on 2015/6/6.
 */
$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".container").css("margin-top", (viewHeight - 418) / 2);
});