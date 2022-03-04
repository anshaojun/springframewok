<%@ include file="include/taglib.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script>
    function loadMenu() {
        var data = [];
        $.ajax({
            type: "post",
            dataType: "json",
            async: false,
            url: ctx + "/menuManage/loadMenu.do",
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
            elem: '#menu_tree',
            data: loadMenu(),
            id: 'treeId',
            showCheckbox: true,     //是否显示复选框
            onlyIconControl: true,
            checkChirld: false
        });
        $.ajax({
            type: "post",
            data: {"id": '${role.id}'},
            async: false,
            dataType: "json",
            url: ctx + "/roleManage/getConnectedMenu.do",
            success: function (data) {
                if (data != null && data.length != 0) {
                    var selected = [];
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].isLeaf == '1') {
                            selected[i] = data[i].id;
                        }
                    }
                    tree.setChecked('treeId', selected);
                }
            }
        });
    });

</script>
<body>
<div id="menu_tree"></div>
</body>
<script>
</script>
</html>
