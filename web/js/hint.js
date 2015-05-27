/**
 * Created by Dawnwords on 2015/5/17.
 */
function Hint(element, displayTime) {
    var hint = $(element), isShowing = false;
    var msg = "";
    hint.popover({
        content: function () {
            return msg;
        },
        template: "<div class='popover text-danger hint' role='tooltip'><div class='arrow'></div><div class='popover-content'></div></div>",
        trigger: 'manual',
        animation: true,
        delay: {show: 500, hide: 100}
    }).on('show.bs.popover', function () {
        setTimeout(function () {
            hint.popover('hide');
        }, displayTime);
    }).on('hidden.bs.popover', function () {
        isShowing = false;
    });
    this.show = function (message) {
        if (!isShowing) {
            isShowing = true;
            msg = message;
            hint.popover('show');
        }
    }
}