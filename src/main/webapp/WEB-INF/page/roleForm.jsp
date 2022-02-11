<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="include/taglib.jsp" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript">
        $(function () {
            $("#permission option[value='" + '${role.permission}' + "']").attr("selected", true);
        })
    </script>
</head>
<body>
<fieldset class="layui-elem-field layui-field-box" style="border: none">
    <form class="layui-form" action="" onsubmit="return false">
        <div class="layui-form-item">
            <label class="layui-form-label">角色名称</label>
            <div class="layui-input-block">
                <input type="hidden" id="id" name="id" value="${role.id}">
                <input type="text" id="roleName" name="roleName" required lay-verify="required" placeholder="请输入输入框内容"
                       autocomplete="off"
                       class="layui-input"
                       value="${role.roleName}"
                       maxlength="20">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">角色权限</label>
            <div class="layui-input-block">
                <select name="permission" id="permission" lay-verify="required" lay-search>
                    <option value=""></option>
                    <c:forEach var="p" items="${requestScope.permissions}">
                        <option value="${p.code}">${p.name}</option>
                    </c:forEach>
                </select>
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
