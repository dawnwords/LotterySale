/**
 * Created by Dawnwords on 2015/6/6.
 */
$(document).ready(function () {
    var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    $(".container").css("margin-top", (viewHeight - 418) / 2);

    var hint = $("#hint");
    hint.css('display', 'none');
    function displayHint(msg) {
        hint.html(msg);
        hint.fadeIn("fast", function () {
            $(this).delay(1000).fadeOut("slow");
        });
    }

    $('#btn-submit').click(function () {
        var remember = $("#remember-me").is(':checked'),
            name = $("input[name='name']").val() || '',
            password = $("input[name='password']").val() || '';

        if (name.length == 0) {
            displayHint('用户名为空！');
            return;
        }

        if (password.length == 0) {
            displayHint('密码为空！');
            return;
        }

        $.ajax({
            url: "../login",
            dataType: "json",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify({
                name: name,
                password: password
            }),
            success: function (data) {
                if (data == 'success') {
                    window.location.replace("../main.jsp");
                } else {
                    displayHint('用户名或密码错误！');
                }
            }
        })

    });
});