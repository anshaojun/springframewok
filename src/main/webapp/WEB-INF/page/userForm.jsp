<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="include/taglib.jsp" %>
<html>
<head>
    <title>Title</title>
    <style>
        #head-shadow {
            width: 80px;
            height: 80px;
            display: block;
            margin: 0 auto;
            border-radius: 40px;
            cursor: pointer;
        }

        #head-display {
            border: none;
            width: 80px;
            height: 80px;
            position: absolute;
            margin: 0 auto;
            top: 10px;
            left: 260px;
            pointer-events: none;
            border-radius: 40px;
        }
    </style>
    <script>
        $(function () {
            if (!'${user.portrait}') {
                $("#head-display").attr("src", "${ctx}/images/defaultportrait.jpg");
            } else {
                $("#head-display").attr("src", "data:image/jpg;base64,${user.portrait}");
            }
            if (${! empty user.id}) {
                $("#passWord").attr("disabled", "disabled");
            }
        });
    </script>
</head>
<body>
<fieldset class="layui-elem-field layui-field-box" style="border: none">
    <form class="layui-form" action="" onsubmit="return false" enctype="multipart/form-data">
        <div class="layui-form-item center">
            <input id="head" type="file" name="head" style="display: none">
            <label id="head-shadow" for="head"></label>
            <img id="head-display">
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>用户名</label>
            <input type="hidden" id="id" name="id" value="${user.id}">
            <div class="layui-input-block">
                <input type="text" name="userName" value="${user.userName}" required lay-verify="required"
                       placeholder="请输入输入框内容" autocomplete="off" class="layui-input" maxlength="30">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>密码</label>
            <div class="layui-input-block">
                <input id="passWord" type="password" name="passWord" value="${user.passWord}" required
                       lay-verify="required"
                       placeholder="请输入密码框内容" autocomplete="off" class="layui-input" maxlength="30">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>昵称</label>
            <div class="layui-input-block">
                <input type="text" name="nickName" value="${user.nickName}" required lay-verify="required"
                       placeholder="请输入输入框内容" autocomplete="off" class="layui-input" maxlength="30">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>电话</label>
            <div class="layui-input-block">
                <input type="text" name="tel" value="${user.tel}" required lay-verify="required|phone"
                       placeholder="请输入输入框内容"
                       autocomplete="off" class="layui-input" maxlength="15">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label"><font color="red">*</font>邮箱</label>
            <div class="layui-input-block">
                <input type="text" name="mail" value="${user.mail}" required lay-verify="required|email"
                       placeholder="请输入输入框内容"
                       autocomplete="off" class="layui-input" maxlength="30">
            </div>
        </div>
        <button class="layui-btn" lay-submit lay-filter="formDemo" style="display: none ">立即提交</button>
    </form>
</fieldset>
</body>
<script>
    var valid = false;

    function sub() {
        $(".layui-btn").click();
        return valid;
    }

    layui.use('form', function () {
        var form = layui.form;
        form.on('submit(formDemo)', function (data) {
            valid = true;
            return false;
        });
    });
</script>
</html>
