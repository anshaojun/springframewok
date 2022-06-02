<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="include/taglib.jsp" %>
<html>
<head>
    <style type="text/css">
        #dept_main, #dept_particulars {
            width: 48.5%;
            display: inline-block;
            vertical-align: top;
            padding: 20px;
            background: white;
            box-sizing: border-box;
            height: 700px;
        }

        #menu_tree {
            margin-top: 20px;
            height: 750px;
            overflow: auto;
        }

    </style>
    <script>
        var dtree;
        $(function () {
            var iconPicker;
            var form;
            var select;
            //表单提交事件
            layui.use('form', function () {
                form = layui.form;
                form.verify({
                    personal: function (e) {
                        if (!e) {
                            return "必填项不能为空";
                        }
                        if (e.indexOf("/") == 0) {
                            return "不能以/开始";
                        }
                    }
                });
                form.on('submit(formDemo)', function (data) {
                    //新增
                    if ($("#id").val() == null || $("#id").val() == '' || $("#id").val().length == 0) {
                        if (select != null) {
                            data.field.mLevel = Number(select.mlevel) + 1;
                        } else {
                            data.field.mLevel = '1';
                        }
                        data.field.isLeaf = '1';
                    }
                    if (data.field.type == '1') {
                        data.field.icon = "";
                        data.field.url = "";
                    }
                    $.ajax({
                        type: "post",
                        data: data.field,
                        dataType: "json",
                        async: false,
                        url: ctx + "/menuManage/save.do",
                        success: function (result) {
                            if (result.code == 200) {
                                parent.layer.msg('保存成功', {icon: 6, shade: [0.3, '#000']}, function () {
                                    $(".layui-form")[0].reset();
                                    tree.reload('menu_tree', {url: '', data: loadMenu()});
                                    disablededit(false);
                                    switchdom(true);
                                });
                            } else {
                                parent.layer.msg(result.msg, {icon: 5, shade: [0.3, '#000']});
                            }
                        }
                    });
                    return false;
                });
                form.on('radio(type)', function (data) {
                    if (data.value == 1) {
                        switchdom(true);
                    } else {
                        switchdom(false);
                    }
                });
            });

            //菜单树
            layui.extend({
                dtree: '${ctx}/js/layui_ext/dtree/dtree',   // {/}的意思即代表采用自有路径，即不跟随 base 路径
                iconPicker: '${ctx}/js/iconpicker-master/module/iconPicker/iconPicker'
            }).use(['dtree', 'layer', 'jquery'], function () {
                dtree = layui.dtree, layer = layui.layer, $ = layui.jquery;
                // 初始化树
                dtree.render({
                    elem: "#menu_tree",
                    data: loadMenu(),
                    line: true,
                    skin: "zdysingle",
                });
                dtree.on("node('menu_tree')", function (obj) {
                    $("#operation").addClass("hide");
                    // 点击高亮
                    $.ajax({
                        type: "post",
                        url: ctx + "/menuManage/getMenu.do",
                        data: {"id": obj.param.nodeId},
                        dataType: "json",
                        async: false,
                        success: function (menu) {
                            if (menu != null) {
                                select = menu;
                                $("#menuName").val(menu.menuName);
                                if (menu.parent != null) {
                                    $("#parentId").val(menu.parent.id);
                                    $("#parentName").val(menu.parent.menuName);
                                } else {
                                    $("#parentId").val("");
                                    $("#parentName").val("");
                                }
                                $("#permission").val(menu.permission);
                                $("#url").val(menu.url);
                                $("#id").val(menu.id);
                                if (menu.icon != null) {
                                    iconPicker.checkIcon('iconPicker', menu.icon);
                                }
                                if (menu.type == '1') {
                                    $("input[name=type][value='1']").prop("checked", true);
                                    switchdom(true);
                                } else {
                                    $("input[name=type][value='0']").prop("checked", true);
                                    switchdom(false);
                                }

                                if (menu.isLeaf == '1') {
                                    $("input[name=isLeaf][value='1']").prop("checked", true);
                                } else {
                                    $("input[name=isLeaf][value='0']").prop("checked", true);
                                }
                                form.render();
                            }
                        }
                    });
                });
            });
            //图标选择器
            layui.use(['iconPicker', 'form', 'layer'], function () {
                iconPicker = layui.iconPicker,
                    form = layui.form,
                    layer = layui.layer,
                    $ = layui.$;
                iconPicker.render({
                    // 选择器，推荐使用input
                    elem: '#iconPicker',
                    // 数据类型：fontClass/unicode，推荐使用fontClass
                    type: 'fontClass',
                    // 是否开启搜索：true/false，默认true
                    search: false,
                    // 是否开启分页：true/false，默认true
                    page: true,
                    // 每页显示数量，默认12
                    limit: 12,
                    // 点击回调
                    click: function (data) {

                    },
                    // 渲染成功后的回调
                    success: function (d) {

                    }
                });
            });
            $("#delMenu").click(function () {
                if (select == null) {
                    parent.layer.msg('请选择一个菜单进行删除', {icon: 5, shade: [0.3, '#000']});
                } else {
                    parent.layer.confirm("确定删除菜单吗？", {icon: 3, btn: ['确定', '取消'], title: '警告'}, function () {
                        $.ajax({
                            type: "post",
                            data: {"id": select.id},
                            dataType: "json",
                            async: false,
                            url: ctx + "/menuManage/del.do",
                            success: function (result) {
                                if (result.code == 200) {
                                    parent.layer.msg('删除成功', {icon: 6, shade: [0.3, '#000']}, function () {
                                        select = null;
                                        $(".layui-form")[0].reset();
                                        dtree.reload('menu_tree', {url: '', data: loadMenu()});
                                        disablededit(false);
                                        switchdom(true);
                                    });
                                } else {
                                    parent.layer.msg(result.msg, {icon: 5, shade: [0.3, '#000']});
                                }
                            }
                        });
                    });
                }
            });
            $("#addMenu").click(function () {
                $(".layui-form")[0].reset();
                disablededit(true);
                switchdom(true);
                $("#id").val("");
                if (select != null) {
                    $("#parentId").val(select.id);
                    $("#parentName").val(select.menuName);
                }
            });
            $("#editMenu").click(function () {
                if (select == null) {
                    parent.layer.msg('请选择一个菜单进行修改', {icon: 5, shade: [0.3, '#000']});
                } else {
                    disablededit(true);
                }
            });
        });

        function switchdom(flag) {
            if (flag) {
                $("#iconbox").addClass("hide");
                $("#urlbox").addClass("hide");
                $("#url").removeAttr("lay-verify");
            } else {
                $("#iconbox").removeClass("hide");
                $("#urlbox").removeClass("hide");
                $("#url").attr("lay-verify", "personal");
            }
        }

        function disablededit(flag) {
            if (flag) {
                $("#operation").removeClass("hide");
                $("form input").each(function (i, o) {
                    if ($(o).attr("id") != 'parentName' && $(o).attr("name") != 'isLeaf' && $(o).attr("name") != 'isLeaf' && $(o).attr("name") != 'type') {
                        $(o).removeAttr("disabled");
                    }
                });
                $("form .layui-form-radio").each(function (i, o) {
                    if ($(o).siblings("input").attr("name") != "isLeaf") {
                        $(o).removeClass("layui-radio-disbaled layui-disabled");
                    }
                });
            } else {
                select = null;
                $("#operation").addClass("hide");
                $("form input").each(function (i, o) {
                    $(o).attr("disabled", "true");
                });
                $("form .layui-form-radio").each(function (i, o) {
                    $(o).addClass("layui-radio-disbaled layui-disabled");
                });
            }
        }

        function loadMenu() {
            var data = [];
            $.ajax({
                type: "post",
                dataType: "json",
                async: false,
                data: {"checkbox": false},
                url: ctx + "/menuManage/loadMenu.do",
                success: function (result) {
                    data = result;
                }
            });
            return data;
        }
    </script>
    <title>Title</title>
</head>
<body>
<div id="dept_main" style="margin-right: 2%;">
    <fieldset class="layui-elem-field layui-field-box">
        <legend>菜单树</legend>
        <shiro:hasPermission name="sys:menu:add">
            <button id="addMenu" class="layui-btn layui-btn-sm  layui-btn-normal" lay-demo="addMenu"><i
                    class="layui-icon layui-icon-add-1"></i>添加下级菜单
            </button>
        </shiro:hasPermission>
        <shiro:hasPermission name="sys:menu:edit">
            <button id="editMenu" class="layui-btn layui-btn-sm layui-btn-checked" lay-demo="addMenu"><i
                    class="layui-icon layui-icon-edit"></i>修改
            </button>
        </shiro:hasPermission>
        <shiro:hasPermission name="sys:menu:del">
            <button id="delMenu" class="layui-btn layui-btn-sm layui-btn-danger" lay-demo="delMenu"><i
                    class="layui-icon layui-icon-delete"></i>删除
            </button>
        </shiro:hasPermission>
        <ul id="menu_tree"></ul>
    </fieldset>
</div>
<div id="dept_particulars">
    <fieldset class="layui-elem-field layui-field-box">
        <legend>菜单详情</legend>
        <form class="layui-form" action="" onsubmit="return false">
            <input id="id" type="text" name="id" style="display: none">
            <div class="layui-form-item">
                <label class="layui-form-label"><font color="red">*</font>菜单名</label>
                <div class="layui-input-block">
                    <input id="menuName" type="text" name="menuName" required lay-verify="required"
                           placeholder="请输入输入框内容"
                           autocomplete="off" class="layui-input" maxlength="10" disabled>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">父菜单</label>
                <div class="layui-input-block">
                    <input id="parentName" type="text" placeholder="请输入输入框内容" autocomplete="off" class="layui-input"
                           disabled>
                    <input id="parentId" type="text" style="display: none" name="parent.id" autocomplete="off">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><font color="red">*</font>权限标识</label>
                <div class="layui-input-block">
                    <input id="permission" type="text" name="permission" required lay-verify="required"
                           placeholder="请输入输入框内容"
                           autocomplete="off" class="layui-input" maxlength="60"
                           onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'')" onpaste="return false"
                           ondragenter="return false" oncontextmenu="return false;" style="ime-mode:disabled"
                           disabled
                    >
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><font color="red">*</font>类型</label>
                <div class="layui-input-block">
                    <input name="type" value="1" type="radio" title="按钮" checked="" lay-filter="type" disabled>
                    <input name="type" value="0" type="radio" title="菜单" lay-filter="type" disabled>
                </div>
            </div>
            <div class="layui-form-item hide" id="urlbox">
                <label class="layui-form-label"><font color="red">*</font>访问地址</label>
                <div class="layui-input-block">
                    <input id="url" type="text" name="url" placeholder="请输入输入框内容" autocomplete="off"
                           class="layui-input" maxlength="100"
                           onkeyup="this.value=this.value.replace(/[\u4e00-\u9fa5]/g,'')" onpaste="return false"
                           ondragenter="return false" oncontextmenu="return false;" style="ime-mode:disabled"
                           lay-verify="personal"
                    >
                </div>
            </div>
            <div class="layui-form-item hide" id="iconbox">
                <label class="layui-form-label"><font color="red">*</font>图标</label>
                <div class="layui-input-block">
                    <input id="iconPicker" name="icon" lay-filter="iconPicker">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><font color="red">*</font>是否底级</label>
                <div class="layui-input-block">
                    <input name="isLeaf" value="1" type="radio" title="是" checked="" disabled>
                    <input name="isLeaf" value="0" type="radio" title="否" disabled>
                </div>
            </div>
            <div class="layui-form-item">
                <div id="operation" class="layui-input-block hide">
                    <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="formDemo">提交</button>
                </div>
            </div>
        </form>
    </fieldset>
</div>
</body>
</html>
