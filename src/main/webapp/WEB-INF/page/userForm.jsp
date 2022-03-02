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
            $('#head').change(function () {
                // 先获取用户上传的文件对象
                let fileObj = this.files[0];
                let suffix = fileObj.name.substring(fileObj.name.lastIndexOf(".")).toLowerCase();
                if (!checkFileExt(suffix)) {
                    layer.alert("图片格式不支持！", {icon: 0});
                    $('#head').value = "";
                    return;
                }
                if (fileObj.size > 1024 * 1024 * 5) {
                    if (!checkFileExt(suffix)) {
                        layer.alert("图片不能大于5mb！", {icon: 0});
                        $('#head').value = "";
                        return;
                    }
                }
                // 生成一个文件读取的内置对象
                let fileReader = new FileReader();
                // 将文件对象传递给内置对象
                fileReader.readAsDataURL(fileObj); //这是一个异步执行的过程，所以需要onload回调函数执行读取数据后的操作
                // 将读取出文件对象替换到img标签
                fileReader.onload = function () {  // 等待文件阅读器读取完毕再渲染图片
                    $('#head-display').attr('src', fileReader.result)
                }
            });
        });

        function checkFileExt(ext) {
            if (!ext.match(/.jpg|.gif|.png|.bmp/i)) {
                return false;
            }
            return true;
        }
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
