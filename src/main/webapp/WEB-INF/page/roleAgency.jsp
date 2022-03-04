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
            url: ctx + "/agencyManage/loadAgency.do",
            success: function (result) {
                data = result;
            }
        });
        return data;
    }

    var agencyId;
    var tree;
    //菜单树
    layui.use(['tree', 'util'], function () {
        tree = layui.tree;
        tree.render({
            elem: '#agency_tree',
            data: loadMenu(),
            id: 'treeId',
            showCheckbox: false,     //是否显示复选框
            onlyIconControl: true,
            click: function (obj) {
                $("#operation").addClass("hide");
                // 点击高亮
                $(".layui-tree-set").removeClass('layui-tree-set-active');
                $(".layui-tree-set").find("i").css("color","#666");
                $(".layui-tree-set").find(".layui-tree-txt").removeClass("text-white");
                obj.elem.addClass('layui-tree-set-active');
                obj.elem.children(".layui-tree-entry").find("i").css("color","white");
                obj.elem.children(".layui-tree-entry").find(".layui-tree-txt").addClass("text-white");
                agencyId = obj.data.id;
            }
        });
        $.ajax({
            type: "post",
            data: {"id": '${role.id}'},
            async: false,
            dataType: "json",
            url: ctx + "/roleManage/getConnectedAgency.do",
            success: function (data) {
                if (data != null) {
                    $("#agency_tree").find(".layui-tree-set").each(function (i, o) {
                        if ($(this).attr("data-id") == data.id) {
                            $(this).children().find(".layui-tree-txt")[0].click();
                        }
                    })
                }
            }
        });
    });

</script>
<body>
<div id="agency_tree"></div>
</body>
<script>
</script>
</html>
