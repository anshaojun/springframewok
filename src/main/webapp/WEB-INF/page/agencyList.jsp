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

        #agency_tree {
            margin-top: 20px;
            height: 750px;
            overflow: auto;
        }

    </style>
    <link rel="stylesheet" href="${ctx}/js/iconpicker-master/assets/layui/css/layui.css"/>
    <script src="${ctx}/js/iconpicker-master/module/common.js"></script>
    <script>
        $(function () {
            var form;
            var select;
            var tree;
            //表单提交事件
            layui.use('form', function () {
                form = layui.form;
                form.verify({});
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
                    $.ajax({
                        type: "post",
                        data: data.field,
                        dataType: "json",
                        async: false,
                        url: ctx + "/agencyManage/save.do",
                        success: function (result) {
                            if (result.code == 200) {
                                parent.layer.msg('保存成功', {icon: 6, shade: [0.3, '#000']}, function () {
                                    $(".layui-form")[0].reset();
                                    tree.reload('treeId', {url: '', data: loadAgency()});
                                    disablededit(false);
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
                    } else {
                    }
                });
            });
            //单位树
            layui.use(['tree', 'util'], function () {
                tree = layui.tree;
                tree.render({
                    elem: '#agency_tree',
                    data: loadAgency(),
                    id: 'treeId',
                    showCheckbox: false,     //是否显示复选框
                    onlyIconControl: true,
                    click: function (obj) {
                        $("#operation").addClass("hide");
                        // 点击高亮
                        $(".layui-tree-set").removeClass('layui-tree-set-active');
                        $(".layui-tree-set").find("i").css("color", "#c0c4cc");
                        $(".layui-tree-set").find(".layui-tree-txt").removeClass("text-white");
                        obj.elem.addClass('layui-tree-set-active');
                        obj.elem.children(".layui-tree-entry").find("i").css("color", "white");
                        obj.elem.children(".layui-tree-entry").find(".layui-tree-txt").addClass("text-white");
                        var id = obj.data.id;
                        $.ajax({
                            type: "post",
                            url: ctx + "/agencyManage/getAgency.do",
                            data: {"id": id},
                            dataType: "json",
                            async: false,
                            success: function (agency) {
                                if (agency != null) {
                                    select = agency;
                                    $("#agencyName").val(agency.agencyName);
                                    $("#agencyCode").val(agency.agencyCode);
                                    if (agency.parent != null) {
                                        $("#parentId").val(agency.parent.id);
                                        $("#parentName").val(agency.parent.agencyName);
                                    } else {
                                        $("#parentId").val("");
                                        $("#parentName").val("");
                                    }
                                    $("#id").val(agency.id);
                                    if (agency.type == '1') {
                                        $("input[name=type][value='1']").prop("checked", true);
                                    } else {
                                        $("input[name=type][value='0']").prop("checked", true);
                                    }

                                    if (agency.isLeaf == '1') {
                                        $("input[name=isLeaf][value='1']").prop("checked", true);
                                    } else {
                                        $("input[name=isLeaf][value='0']").prop("checked", true);
                                    }
                                    form.render();
                                }
                            }
                        });
                    }
                });
            });

            $("#delAgency").click(function () {
                if (select == null) {
                    parent.layer.msg('请选择一个单位进行删除', {icon: 5, shade: [0.3, '#000']});
                } else {
                    parent.layer.confirm("确定删除单位吗？", {icon: 3, btn: ['确定', '取消'], title: '警告'}, function () {
                        $.ajax({
                            type: "post",
                            data: {"id": select.id},
                            dataType: "json",
                            async: false,
                            url: ctx + "/agencyManage/del.do",
                            success: function (result) {
                                if (result.code == 200) {
                                    parent.layer.msg('删除成功', {icon: 6, shade: [0.3, '#000']}, function () {
                                        select = null;
                                        $(".layui-form")[0].reset();
                                        tree.reload('treeId', {url: '', data: loadAgency()});
                                        disablededit(false);
                                    });
                                } else {
                                    parent.layer.msg(result.msg, {icon: 5, shade: [0.3, '#000']});
                                }
                            }
                        });
                    });
                }
            });
            $("#addAgency").click(function () {
                $(".layui-form")[0].reset();
                disablededit(true);
                $("#id").val("");
                if (select != null) {
                    $("#parentId").val(select.id);
                    $("#parentName").val(select.agencyName);
                }
            });
            $("#editAgency").click(function () {
                if (select == null) {
                    parent.layer.msg('请选择一个单位进行修改', {icon: 5, shade: [0.3, '#000']});
                } else {
                    disablededit(true);
                }
            });
        });


        function disablededit(flag) {
            if (flag) {
                $("#operation").removeClass("hide");
                $("form input").each(function (i, o) {
                    if ($(o).attr("id") != 'parentName' && $(o).attr("id") != 'agencyCode' && $(o).attr("name") != 'isLeaf' && $(o).attr("name") != 'type') {
                        $(o).removeAttr("disabled");
                    }
                });
                $("form .layui-form-radio").each(function (i, o) {
                    if($(o).siblings("input").attr("name")!="isLeaf"){
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

        function loadAgency() {
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
    </script>
    <title>Title</title>
</head>
<body>
<div id="dept_main" style="margin-right: 2%;">
    <fieldset class="layui-elem-field layui-field-box">
        <legend>单位树</legend>
        <shiro:hasPermission name="sys:agency:add">
            <button id="addAgency" class="layui-btn layui-btn-sm  layui-btn-normal" lay-demo="addAgency"><i
                    class="layui-icon layui-icon-add-1"></i>添加下级单位
            </button>
        </shiro:hasPermission>
        <shiro:hasPermission name="sys:agency:edit">
            <button id="editAgency" class="layui-btn layui-btn-sm layui-btn-checked" lay-demo="addAgency"><i
                    class="layui-icon layui-icon-edit"></i>修改
            </button>
        </shiro:hasPermission>
        <shiro:hasPermission name="sys:agency:del">
            <button id="delAgency" class="layui-btn layui-btn-sm layui-btn-danger" lay-demo="delAgency"><i
                    class="layui-icon layui-icon-delete"></i>删除
            </button>
        </shiro:hasPermission>
        <div id="agency_tree"></div>
    </fieldset>
</div>
<div id="dept_particulars">
    <fieldset class="layui-elem-field layui-field-box">
        <legend>单位详情</legend>
        <form class="layui-form" action="" onsubmit="return false">
            <input id="id" type="text" name="id" style="display: none">
            <div class="layui-form-item">
                <label class="layui-form-label"><font color="red">*</font>单位编码</label>
                <div class="layui-input-block">
                    <input id="agencyCode" type="text" name="agencyCode"
                           placeholder="自动生成"
                           autocomplete="off" class="layui-input" disabled>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><font color="red">*</font>单位名称</label>
                <div class="layui-input-block">
                    <input id="agencyName" type="text" name="agencyName" required lay-verify="required"
                           placeholder="请输入输入框内容"
                           autocomplete="off" class="layui-input" maxlength="40" disabled>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">父单位</label>
                <div class="layui-input-block">
                    <input id="parentName" type="text" placeholder="请输入输入框内容" autocomplete="off" class="layui-input"
                           disabled>
                    <input id="parentId" type="text" style="display: none" name="parent.id" autocomplete="off">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label"><font color="red">*</font>类型</label>
                <div class="layui-input-block">
                    <input name="type" value="1" type="radio" title="单位" checked="" lay-filter="type" disabled>
                    <input name="type" value="0" type="radio" title="部门" lay-filter="type" disabled>
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
