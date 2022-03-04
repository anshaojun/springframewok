<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="include/taglib.jsp" %>
<html>
<head>
    <title>Title</title>
    <link href="${ctx}/css/data.css" type="text/css" rel="stylesheet"/>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card layui-form">
                <div class="layui-card-body layui-row layui-col-space15">
                    <div class="layui-col-md2">
                        <input type="text" class="layui-input" id="roleName" placeholder="角色名称"/>
                    </div>
                    <div class="layui-col-md2">
                        <button class="layui-btn" data-type="reload"><i class="layui-icon layui-icon-search"></i>查询
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-col-md12">
            <div class="layui-card layui-form">
                <div class="layui-card-body">
                    <table class="layui-hide" id="layListId" lay-filter="layList"></table>
                    <script type="text/html" id="toolbar">
                        <div class="layui-btn-container">
                            <shiro:hasPermission name="sys:permission:add">
                                <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="add"><i
                                        class="layui-icon layui-icon-add-1"></i> 新增
                                </button>
                            </shiro:hasPermission>
                        </div>
                    </script>
                    <script type="text/html" id="bar">
                        <div class="layui-btn-group">
                            <shiro:hasPermission name="sys:permission:adduser">
                                <button type="button" class="layui-btn layui-btn-sm layui-btn-checked"
                                        lay-event="addUser">
                                    <i class="layui-icon layui-icon-group"></i>添加用户
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:permission:addmenu">
                                <button type="button" class="layui-btn layui-btn-sm layui-btn-normal"
                                        lay-event="addMenu">
                                    <i class="layui-icon layui-icon-vercode"></i> 菜单权限
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:permission:addagency">
                                <button type="button" class="layui-btn layui-btn-sm layui-btn-normal"
                                        lay-event="addAgency">
                                    <i class="layui-icon layui-icon-vercode"></i> 单位权限
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:permission:edit">
                                <button type="button" class="layui-btn layui-btn-sm layui-btn-warm" lay-event="edit">
                                    <i class="layui-icon layui-icon-edit"></i> 修改
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:permission:del">
                                <button type="button" class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">
                                    <i class="layui-icon layui-icon-delete"></i> 删除
                                </button>
                            </shiro:hasPermission>
                        </div>
                    </script>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    layui.use('table', function () {
        var table = layui.table;
        table.render({
            elem: '#layListId',
            id: 'layTable',
            url: ctx + '/roleManage/queryByPage.do',
            title: '角色维护',
            cellMinWidth: 100,
            even: true,
            defaultToolbar: ['filter', 'print', 'exports'], //这里在右边显示
            toolbar: '#toolbar',   //这里在左边显示，然后指定到模版id
            cols: [
                [{
                    field: 'id',
                    hide: true,
                    title: "id"
                }, {
                    field: 'roleName',
                    title: '角色名称',
                    width: '65%',
                    sort: true
                }, {
                    title: '操作',
                    align: 'center',
                    toolbar: '#bar',
                    width: '35%'
                }]
            ],
            parseData: function (res) { //res 即为原始返回的数据
                return {
                    "code": 0, //解析接口状态   （好像必须是0 才可以）
                    "msg": "", //解析提示文本
                    "count": res.totalNum, //解析数据长度
                    "data": res.list //解析数据列表
                };
            },
            page: true
        });
        //监听头工具栏
        table.on('toolbar(layList)', function (obj) {
            if (obj.event === 'add') {
                var index = parent.layer.open({
                    title: "新增角色",
                    area: ['600px', '350px'],
                    type: 2,
                    content: ctx + "/roleManage/form.do",
                    btn: ['确定', '取消'],
                    yes: function (index, layero) {
                        //调用
                        var frame = $(layero).find("iframe")[0].contentWindow;
                        var valid = frame.sub();
                        if (valid) {
                            var roleName = $(layero).find('iframe').contents().find("#roleName").val();
                            var permission = $(layero).find('iframe').contents().find("#permission").val();
                            $.ajax({
                                type: "post",
                                data: {"roleName": roleName, "permission": permission},
                                dataType: "json",
                                url: ctx + "/roleManage/save.do",
                                async: false,
                                success: function (result) {
                                    if (result.code == 200) {
                                        parent.layer.close(index);
                                        parent.layer.msg('保存成功', {icon: 6, shade: [0.3, '#000']}, function () {
                                            table.reload('layTable', {});
                                        });
                                    } else {
                                        parent.layer.msg(result.msg, {icon: 5, shade: [0.3, '#000']});
                                    }
                                }
                            });
                        }
                    },
                    btn2: function () {
                        layer.closeAll(index); //关闭当前窗口
                    }
                });
            }
            /*else if(obj.event === 'LAYTABLE_EXCEL'){
                var formSelect = form.val('searchForm');
                $.ajax({
                    type: 'get'
                    ,url: ctx + '/inventoryReport/getInventoryHistoryList'
                    ,data: formSelect
                    ,success:function(res){
                        table.exportFile('tableList', res.data,'xls');
                    }
                });
            }*/
        });
        //监听工具条
        table.on('tool(layList)', function (obj) {
            var data = obj.data; //获得当前行数据
            switch (obj.event) {
                case 'addUser':
                    var index = parent.layer.open({
                        title: "用户选择",
                        area: ['300px', '700px'],
                        type: 2,
                        content: ctx + "/roleManage/roleUser.do?id=" + data.id,
                        btn: ['确定', '取消'],
                        yes: function (index, layero) {
                            //调用
                            var frame = $(layero).find("iframe")[0].contentWindow;
                            var checkedData = frame.tree.getChecked('treeId');
                            var users = getChildNodes(checkedData, []);
                            $.ajax({
                                type: "post",
                                data: {"roleId": data.id, "userIds": users},
                                dataType: "json",
                                url: ctx + "/roleManage/roleUser.do",
                                async: false,
                                success: function (result) {
                                    if (result.code == 200) {
                                        parent.layer.close(index);
                                        parent.layer.msg('保存成功', {icon: 6, shade: [0.3, '#000']});
                                    } else {
                                        parent.layer.msg(result.msg, {icon: 5, shade: [0.3, '#000']});
                                    }
                                }
                            });
                        },
                        btn2: function () {
                            layer.closeAll(index); //关闭当前窗口
                        }
                    });
                    break;
                case 'addMenu':
                    var index = parent.layer.open({
                        title: "菜单权限",
                        area: ['300px', '700px'],
                        type: 2,
                        content: ctx + "/roleManage/roleMenu.do?id=" + data.id,
                        btn: ['确定', '取消'],
                        yes: function (index, layero) {
                            //调用
                            var frame = $(layero).find("iframe")[0].contentWindow;
                            var checkedData = frame.tree.getChecked('treeId');
                            var menus = getChildNodes(checkedData, [], true);
                            $.ajax({
                                type: "post",
                                data: {"roleId": data.id, "menuIds": menus},
                                dataType: "json",
                                url: ctx + "/roleManage/roleMenu.do",
                                async: false,
                                success: function (result) {
                                    if (result.code == 200) {
                                        parent.layer.close(index);
                                        parent.layer.msg('保存成功', {icon: 6, shade: [0.3, '#000']});
                                    } else {
                                        parent.layer.msg(result.msg, {icon: 5, shade: [0.3, '#000']});
                                    }
                                }
                            });
                        },
                        btn2: function () {
                            layer.closeAll(index); //关闭当前窗口
                        }
                    });
                    break;
                case 'addAgency':
                    var index = parent.layer.open({
                        title: "单位权限",
                        area: ['400px', '700px'],
                        type: 2,
                        content: ctx + "/roleManage/roleAgency.do?id=" + data.id,
                        btn: ['确定', '取消'],
                        yes: function (index, layero) {
                            //调用
                            var frame = $(layero).find("iframe")[0].contentWindow;
                            var agencys = new Array();
                            var agencyId = frame.agencyId;
                            if(agencyId==null||agencyId.length==0){
                                parent.layer.msg('请选择一个单位', {icon: 5, shade: [0.3, '#000']});
                                return;
                            }
                            agencys.push(agencyId);
                            $.ajax({
                                type: "post",
                                data: {"roleId": data.id, "agencyIds": agencys},
                                dataType: "json",
                                url: ctx + "/roleManage/roleAgency.do",
                                async: false,
                                success: function (result) {
                                    if (result.code == 200) {
                                        parent.layer.close(index);
                                        parent.layer.msg('保存成功', {icon: 6, shade: [0.3, '#000']});
                                    } else {
                                        parent.layer.msg(result.msg, {icon: 5, shade: [0.3, '#000']});
                                    }
                                }
                            });
                        },
                        btn2: function () {
                            layer.closeAll(index); //关闭当前窗口
                        }
                    });
                    break;
                case 'edit':
                    var index = parent.layer.open({
                        title: "修改角色",
                        area: ['600px', '350px'],
                        type: 2,
                        content: ctx + "/roleManage/form.do?id=" + data.id,
                        btn: ['确定', '取消'],
                        yes: function (index, layero) {
                            //调用
                            var frame = $(layero).find("iframe")[0].contentWindow;
                            var valid = frame.sub();
                            if (valid) {
                                var roleName = $(layero).find('iframe').contents().find("#roleName").val();
                                var permission = $(layero).find('iframe').contents().find("#permission").val();
                                var id = $(layero).find('iframe').contents().find("#id").val();
                                $.ajax({
                                    type: "post",
                                    data: {"roleName": roleName, "id": id, "permission": permission},
                                    dataType: "json",
                                    url: ctx + "/roleManage/save.do",
                                    async: false,
                                    success: function (result) {
                                        if (result.code == 200) {
                                            parent.layer.close(index);
                                            parent.layer.msg('保存成功', {icon: 6, shade: [0.3, '#000']}, function () {
                                                table.reload('layTable', {});
                                            });
                                        } else {
                                            parent.layer.msg(result.msg, {icon: 5, shade: [0.3, '#000']});
                                        }
                                    }
                                });
                            }
                        },
                        btn2: function () {
                            layer.closeAll(index); //关闭当前窗口
                        }
                    });
                    break;
                case 'del':
                    parent.layer.confirm("确定删除角色吗？", {icon: 3, btn: ['确定', '取消'], title: '警告'}, function () {
                        $.ajax({
                            type: "post",
                            data: {"id": data.id},
                            dataType: "json",
                            url: ctx + "/roleManage/delete.do",
                            async: false,
                            success: function (result) {
                                if (result.code == 200) {
                                    parent.layer.msg('删除成功', {icon: 6, shade: [0.3, '#000']}, function () {
                                        table.reload('layTable', {});
                                    });
                                } else {
                                    parent.layer.msg(result.msg, {icon: 5, shade: [0.3, '#000']});
                                }
                            }
                        });
                    });
                    break;
                default:
                    break;
            }

        });
        var $ = layui.$,
            active = {
                reload: function () {
                    var roleName = $("#roleName").val();
                    //执行重载
                    table.reload('layTable', {
                        page: {
                            curr: 1
                        },
                        where: {
                            roleName: roleName
                        }
                    });
                },
                getCheckData: function () { //获取选中数据
                    var checkStatus = table.checkStatus('layTable'),
                        data = checkStatus.data;
                    layer.alert(JSON.stringify(data));
                },
                getCheckLength: function () { //获取选中数目
                    var checkStatus = table.checkStatus('layTable'),
                        data = checkStatus.data;
                    layer.msg('选中了：' + data.length + ' 个');
                },
                isAll: function () { //验证是否全选
                    var checkStatus = table.checkStatus('layTable');
                    layer.msg(checkStatus.isAll ? '全选' : '未全选')
                }

            };
        $('.layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] && active[type].call(this);
        });
    });
</script>
</html>
