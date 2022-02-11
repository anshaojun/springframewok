<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/taglib.jsp" %>
<%@ include file="include/common.jsp" %>
<html>

<head>
    <title>请登录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="UTF-8"/>
    <meta name="keywords"
          content="Hotair Login Form Responsive web template, Bootstrap Web Templates, Flat Web Templates, Android Compatible web template, Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design"/>
    <link rel="stylesheet" href="${ctx}/css/login.css" type="text/css" media="all"/>
    <script type="text/javascript">
        if (window != top)
            top.location.href = location.href;
    </script>
</head>

<body>

<section class="w3l-hotair-form">
    <div id="title">
        <span>基于Springboot2.X</span>
        <span>后台管理系统</span>
    </div>
    <div class="container">
        <div class="workinghny-form-grid">
            <div class="main-hotair">
                <div class="content-wthree">
                    <h2>登录</h2>
                    <form class="inputForm" action="${ctx}/login/login.do" method="post">
                        <div class="boxing">
                            <input type="text" class="text" name="username" placeholder="请输入用户名" autocomplete="off"
                                   required>
                        </div>
                        <div class="boxing">
                            <input type="password" class="password" name="password" placeholder="请输入密码"
                                   autocomplete="off"
                                   required>
                        </div>
                        <div id="box" onselectstart="return false;">
                            <div class="bgColor"></div>
                            <div class="txt">滑动解锁</div>
                            <!--给i标签添加上相应字体图标的类名即可-->
                            <div class="slider"><i class="iconfont icon-double-right"></i></div>
                        </div>
                        <%--<div class="boxing">
                            <input type="text" class="text" name="validation" placeholder="请输入验证码" autocomplete="off"
                                   required>
                        </div>
                        <img id="validation" src="${ctx}/login/validation.do" onclick="javascript:$(this).attr('src','${ctx}/login/validation.do?'+Math.random());">--%>
                        <button class="btn" type="submit">登录</button>
                    </form>
                </div>
                <div class="w3l_form align-self">
                </div>
            </div>
        </div>
        <!-- //form -->
    </div>
    <!-- copyright-->
    <div class="copyright text-center">
    </div>
    <!-- //copyright-->
</section>
<!-- //form section start -->

<script>
    var check = false;
    $(document).ready(function (c) {
        $(".container").animate({right: "10%"}, 800, 'linear');
        $(".inputForm").submit(function () {
            if (validform().form()) {
                if (!check) {
                    flash();
                    return false;
                }
                var options = {
                    type: 'POST',
                    url: $(".inputForm").action,
                    dataType: 'json',
                    beforeSubmit: function (formData, jqForm, option) {
                        layer.load();
                        // 表单提交之前的回调函数，一般用户表单验证
                        // formData: 数组对象,提交表单时,Form插件会以Ajax方式自动提交这些数据,格式Json数组,形如[{name:userName, value:admin},{name:passWord, value:123}]
                        // jqForm: jQuery对象,，封装了表单的元素
                        // options: options对象
                        formData[1].value = md5(formData[1].value);
                        /* 表单提交前的操作 */
                        return true;  // 只要不返回false,表单都会提交
                    },
                    success: function (responseText, statusText, xhr, $form) {    // 成功后的回调函数(返回数据由responseText获得)
                        layer.closeAll();
                        if (responseText.code == 200) {
                            //重定向到之前的页面
                            var savedUrl = responseText.data.savedUri;
                            var successUrl = '${ctx}';
                            if (savedUrl != null && savedUrl != 'undefined' && savedUrl.length != 0) {
                                successUrl += savedUrl;
                            }
                            window.location.href = successUrl;
                        } else {
                            layer.alert(responseText.msg, {icon: 5});
                        }
                    },
                    error: function (xhr, status, err) {
                        alert("操作失败!");    // 访问地址失败，或发生异常没有正常返回
                    }/*,
                    clearForm: true,    // 成功提交后，清除表单填写内容
                    resetForm: true */   // 成功提交后，重置表单填写内容
                };
                $(".inputForm").ajaxSubmit(options);
            }
            return false;
        });
    });

    //一、定义了一个获取元素的方法
    function getEle(selector) {
        return document.querySelector(selector);
    }

    //二、获取到需要用到的DOM元素
    var box = getEle("#box"),//容器
        bgColor = getEle(".bgColor"),//背景色
        txt = getEle(".txt"),//文本
        slider = getEle(".slider"),//滑块
        icon = getEle(".slider>i"),
        successMoveDistance = box.offsetWidth - slider.offsetWidth,//解锁需要滑动的距离
        downX,//用于存放鼠标按下时的位置
        isSuccess = false;//是否解锁成功的标志，默认不成功

    //三、给滑块添加鼠标按下事件
    slider.onmousedown = mousedownHandler;

    //3.1鼠标按下事件的方法实现
    function mousedownHandler(e) {
        bgColor.style.transition = "";
        slider.style.transition = "";
        var e = e || window.event || e.which;
        downX = e.clientX;
        //在鼠标按下时，分别给鼠标添加移动和松开事件
        document.onmousemove = mousemoveHandler;
        document.onmouseup = mouseupHandler;
    };

    //四、定义一个获取鼠标当前需要移动多少距离的方法
    function getOffsetX(offset, min, max) {
        if (offset < min) {
            offset = min;
        } else if (offset > max) {
            offset = max;
        }
        return offset;
    }

    //3.1.1鼠标移动事件的方法实现
    function mousemoveHandler(e) {
        var e = e || window.event || e.which;
        var moveX = e.clientX;
        var offsetX = getOffsetX(moveX - downX, 0, successMoveDistance);
        bgColor.style.width = offsetX + "px";
        slider.style.left = offsetX + "px";

        if (offsetX == successMoveDistance) {
            success();
        }
        //如果不设置滑块滑动时会出现问题（目前还不知道为什么）
        e.preventDefault();
    };

    //3.1.2鼠标松开事件的方法实现
    function mouseupHandler(e) {
        if (!isSuccess) {
            bgColor.style.width = 0 + "px";
            slider.style.left = 0 + "px";
            bgColor.style.transition = "width 0.8s linear";
            slider.style.transition = "left 0.8s linear";
        }
        document.onmousemove = null;
        document.onmouseup = null;
    };

    //五、定义一个滑块解锁成功的方法
    function success() {
        check = true;
        isSuccess = true;
        txt.innerHTML = "解锁成功";
        txt.style.color = "white";
        bgColor.style.backgroundColor = "#15D36A";
        slider.className = "slider active";
        slider.style.backgroundImage = "url('../images/success.png')";
        //滑动成功时，移除鼠标按下事件和鼠标移动事件
        slider.onmousedown = null;
        document.onmousemove = null;
    }

    function flash() {
        setTimeout(" $('.txt').css('color','red')", 100);
        setTimeout("$('.txt').css('color','#999')", 200);
        setTimeout(" $('.txt').css('color','red')", 300);
        setTimeout("$('.txt').css('color','#999')", 400);
    }
</script>

</body>

</html>