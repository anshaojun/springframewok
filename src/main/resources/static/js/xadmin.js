$(function () {
    //加载用户信息
    $.ajax({
        type: "post",
        dataType: "json",
        async: false,
        url: ctx + "/login/sessionUser.do",
        success: function (data) {
            if (data && data.code == 200) {
                user = data.data.user;
                $("#portrait,#headicon").attr("src", "data:image/jpg;base64," + user.portrait);
                $("#username span").text(user.nickName);
                var menu = user.menuList;
                if (menu.length != 0) {
                    for (var i = 0; i < menu.length; i++) {
                        var m = menu[i];
                        if (m.type != '1') {
                            var isleaf = m.isLeaf == '1' ? 'leaf' : "";
                            var onelevel = $('<div class="h1 ' + isleaf + '" url="'+m.url+'"><i class="layui-icon '+m.icon+'"></i>' + m.menuName + '</div>');
                            if (m.child.length != 0) {
                                var twoWraper = $('<div class="con hide"></div>');
                            }
                            for (var j = 0; j < m.child.length; j++) {
                                var n = m.child[j];
                                if (n.type != '1') {
                                    isleaf = n.isLeaf == '1' ? 'leaf' : "";
                                    var twolevel = $('<div class="h2 ' + isleaf + '"  url="'+n.url+'"><i class="layui-icon '+n.icon+'"></i>' + n.menuName + '</div>');
                                    $(twoWraper).append(twolevel);
                                }
                            }
                            $("#menuArea").append(onelevel);
                            if ($(twoWraper).find(".h2").length != 0) {
                                $("#menuArea").append(twoWraper);
                            }
                        }
                    }
                }
            }
        }
    });
    $("#portrait").click(function () {
        if ($("#user_info").css("display") == "block") {
            $("#user_info").css("display", "none");
        } else {
            $("#user_info").css("display", "block");
        }
    });
    $("body").click(function (e) {
        if (e.target.className != "portrait") {
            if ($("#user_info").css("display") == "block") {
                $("#user_info").css("display", "none");
            }
        }
    });
    $("#user_info #logout").click(function () {
        $.ajax({
            type: "post",
            dataType: "json",
            url: ctx + "/login/logout.do",
            success: function (result) {
                if (result.code = 200) {
                    window.location.href = ctx;
                }
            }
        });
    });
    $('.container .left_open i').click(function (event) {
        if ($('.left-nav').css('left') == '0px') {
            $('.left-nav').animate({left: '-230px'}, 100);
            $('.page-content').animate({left: '0px'}, 100);
            $('.page-content-bg').hide();
        } else {
            $('.left-nav').animate({left: '0px'}, 100);
            $('.page-content').animate({left: '215px'}, 100);
            if ($(window).width() < 768) {
                $('.page-content-bg').show();
            }
        }

    });

    $('.page-content-bg').click(function (event) {
        $('.left-nav').animate({left: '-221px'}, 100);
        $('.page-content').animate({left: '0px'}, 100);
        $(this).hide();
    });
    $("#menuArea .h1,#menuArea .h2").click(function () {
        if ($(this).hasClass("leaf")) {
            $("#mainFrame").attr("src",ctx+"/"+$(this).attr("url"));
        } else {
            show(this);
        }
    });

});

function show(self) {
    $(self).next().slideToggle();
    $(self).parent().siblings().children(".con").each(function (i, o) {
        if ($(o).css("display") == "block") {
            $(this).slideToggle();
        }
    });
}

