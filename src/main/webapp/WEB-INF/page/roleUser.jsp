<%@ include file="include/taglib.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script>
    function loadUser() {
        var data = [];
        $.ajax({
            type: "post",
            dataType: "json",
            async: false,
            url: ctx + "/userManage/loadUser.do",
            success: function (result) {
                data = result;
            }
        });
        return data;
    }

    var tree;
    //菜单树
    layui.use(['tree', 'util'], function () {
        tree = layui.tree;
        tree.render({
            elem: '#user_tree',
            data: loadUser(),
            id: 'treeId',
            showCheckbox: true,     //是否显示复选框
            onlyIconControl: true
        });
        $.ajax({
            type: "post",
            data: {"id": '${role.id}'},
            async: false,
            dataType: "json",
            url: ctx + "/roleManage/getConnectedUser.do",
            success: function (data) {
                if (data != null && data.length != 0) {
                    tree.setChecked('treeId', data);
                }
            }
        });
    });

</script>
<body>
<div id="user_tree"></div>
</body>
<script>
</script>
</html>
