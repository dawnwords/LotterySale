/**
 * Created by Dawnwords on 2015/5/17.
 */
function Hint(element, content) {
    var hint = $(element), isShowing = false;
    hint.popover({
        content: content,
        template: "<div class='popover text-danger hint' role='tooltip'><div class='arrow'></div><div class='popover-content'></div></div>",
        trigger: 'manual',
        animation: true,
        delay: {show: 500, hide: 100}
    }).on('show.bs.popover', function () {
        setTimeout(function () {
            hint.popover('hide');
        }, 5000);
    }).on('hidden.bs.popover', function () {
        isShowing = false;
    });
    this.show = function () {
        if (!isShowing) {
            isShowing = true;
            hint.popover('show');
        }
    }
}