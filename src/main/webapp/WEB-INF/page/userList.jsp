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
                        <input type="text" class="layui-input" id="userName" placeholder="用户名称"/>
                    </div>
                    <div class="layui-col-md2">
                        <input type="text" class="layui-input" id="nickName" placeholder="昵称"/>
                    </div>
                    <div class="layui-col-md2">
                        <input type="text" class="layui-input" id="tel" placeholder="电话"/>
                    </div>
                    <div class="layui-col-md2">
                        <input type="text" class="layui-input" id="mail" placeholder="邮箱"/>
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
                            <shiro:hasPermission name="sys:user:add">
                                <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="add"><i
                                        class="layui-icon layui-icon-add-1"></i> 新增
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:user:del">
                                <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="batchDel"><i
                                        class="layui-icon layui-icon-delete"></i> 删除
                                </button>
                            </shiro:hasPermission>
                        </div>
                    </script>
                    <script type="text/html" id="bar">
                        <div class="layui-btn-group">
                            <shiro:hasPermission name="sys:user:edit">
                                <button type="button" class="layui-btn layui-btn-sm layui-btn-warm" lay-event="edit">
                                    <i class="layui-icon layui-icon-edit"></i> 修改
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="sys:user:del">
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
            url: ctx + '/userManage/queryByPage.do',
            title: '用户维护',
            cellMinWidth: 100,
            even: true,
            limit: 15,
            limits: [15, 30, 45, 60, 75, 90],
            defaultToolbar: ['filter', 'print', 'exports'], //这里在右边显示
            toolbar: '#toolbar',   //这里在左边显示，然后指定到模版id
            cols: [
                [{
                    type: 'checkbox',
                    width: 50,
                    style: "margin-top:5px"
                }, {
                    title: '头像',
                    field: 'portrait',
                    align: "center",
                    width: 100,
                    templet: '<div><img style="width: 28px;height: 28px;border-radius: 14px;border: 1px gray solid" src="data:image/jpeg;base64,{{ d.portrait}}"></div>',
                }, {
                    field: 'userName',
                    title: '用户名称',
                    sort: true
                }, {
                    field: 'nickName',
                    title: '昵称',
                    sort: true
                }, {
                    field: 'mail',
                    title: '邮箱',
                    width: '15%',
                    sort: true
                }, {
                    field: 'tel',
                    title: '电话',
                    sort: true
                }, {
                    field: 'lastLoginTime',
                    title: '上次登录时间',
                    sort: true
                }, {
                    field: 'lastLoginIp',
                    title: '上次登录地址',
                    sort: true
                }, {
                    title: '操作',
                    align: 'center',
                    toolbar: '#bar',
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
                    title: "新增用户",
                    area: ['600px', '500px'],
                    type: 2,
                    content: ctx + "/userManage/form.do",
                    btn: ['确定', '取消'],
                    yes: function (index, layero) {
                        //调用
                        var frame = $(layero).find("iframe")[0].contentWindow;
                        var valid = frame.sub();
                        if (valid) {
                            var formData = new FormData($(layero).find('iframe').contents().find("form")[0]);
                            formData.set("passWord", md5(formData.get("passWord")));
                            $.ajax({
                                type: "post",
                                data: formData,
                                dataType: "json",
                                contentType: false, //必须
                                processData: false, //必须
                                url: ctx + "/userManage/save.do",
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
            } else if (obj.event === 'batchDel') {
                var selected = table.checkStatus('layTable').data;
                if (selected.length == 0) {
                    parent.layer.msg('请选择至少一条记录', {icon: 5, shade: [0.3, '#000']});
                    return;
                }
                parent.layer.confirm("确定删除用户吗？", {icon: 3, btn: ['确定', '取消'], title: '警告'}, function () {
                    var ids = [];
                    for (var i = 0; i < selected.length; i++) {
                        ids.push(selected[i].id);
                    }
                    $.ajax({
                        type: "post",
                        data: {"ids": ids},
                        dataType: "json",
                        async: false,
                        url: ctx + "/userManage/batchDelete.do",
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
                case 'edit':
                    var index = parent.layer.open({
                        title: "修改用户",
                        area: ['600px', '500px'],
                        type: 2,
                        content: ctx + "/userManage/form.do?id=" + data.id,
                        btn: ['确定', '取消'],
                        yes: function (index, layero) {
                            //调用
                            var frame = $(layero).find("iframe")[0].contentWindow;
                            var valid = frame.sub();
                            if (valid) {
                                var formData = new FormData($(layero).find('iframe').contents().find("form")[0]);
                                formData.delete("passWord");
                                $.ajax({
                                    type: "post",
                                    data: formData,
                                    dataType: "json",
                                    contentType: false, //必须
                                    processData: false, //必须
                                    url: ctx + "/userManage/save.do",
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
                    parent.layer.confirm("确定删除用户吗？", {icon: 3, btn: ['确定', '取消'], title: '警告'}, function () {
                        $.ajax({
                            type: "post",
                            data: {"id": data.id},
                            dataType: "json",
                            url: ctx + "/userManage/delete.do",
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
                    var userName = $("#userName").val();
                    var nickName = $("#nickName").val();
                    var tel = $("#tel").val();
                    var mail = $("#mail").val();
                    //执行重载
                    table.reload('layTable', {
                        page: {
                            curr: 1
                        },
                        where: {
                            userName: userName,
                            nickName: nickName,
                            tel: tel,
                            mail: mail,
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
