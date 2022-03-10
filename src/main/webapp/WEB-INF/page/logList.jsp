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
                        <input type="text" class="layui-input" id="operUserName" placeholder="登录名"/>
                    </div>
                    <div class="layui-col-md2">
                        <input type="text" class="layui-input" id="operModel" placeholder="操作模块"/>
                    </div>
                    <div class="layui-col-md2">
                        <input type="text" class="layui-input" id="operType" placeholder="操作类型"/>
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
            url: ctx + '/logManager/queryByPage.do',
            title: '日志查看',
            limit: 15,
            limits: [15, 30, 45, 60, 75, 90],
            cellMinWidth: 100,
            even: true,
            defaultToolbar: ['filter', 'print', 'exports'], //这里在右边显示
            toolbar: '#toolbar',   //这里在左边显示，然后指定到模版id
            cols: [
                [ {
                    title: '操作模块',
                    field: 'operModel',
                    align: "center",
                }, {
                    field: 'id',
                    hide: true,
                    title: "id"
                }, {
                    field: 'operType',
                    title: '操作类型',
                    sort: true
                }, {
                    field: 'operDesc',
                    title: '操作描述',
                    sort: true
                }, {
                    field: 'operMethod',
                    title: '请求方法',
                    sort: true
                }, {
                    field: 'operParam',
                    title: '请求参数',
                    sort: true
                }, {
                    field: 'operReturn',
                    title: '返回结果',
                    sort: true
                }, {
                    field: 'operUserName',
                    title: '操作用户',
                    sort: true
                }, {
                    field: 'operIp',
                    title: '操作IP',
                    sort: true

                }, {
                    field: 'operUri',
                    title: '操作URI',
                    sort: true
                }, {
                    field: 'operTime',
                    title: '操作时间',
                    sort: true
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
        /*//监听头工具栏
        table.on('toolbar(layList)', function (obj) {

        });
        //监听工具条
        table.on('tool(layList)', function (obj) {

        });*/
        var $ = layui.$,
            active = {
                reload: function () {
                    var operUserName = $("#operUserName").val();
                    var operModel = $("#operModel").val();
                    var operType = $("#operType").val();
                    //执行重载
                    table.reload('layTable', {
                        page: {
                            curr: 1
                        },
                        where: {
                            operUserName: operUserName,
                            operModel: operModel,
                            operType: operType
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
