<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="include/taglib.jsp" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta name="renderer" content="webkit">
    <!--国产浏览器高速模式-->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${ctx}/css/xadmin.css" type="text/css"/>
    <script type="text/javascript" src="${ctx}/js/xadmin.js"></script>
    <title>后台管理</title>
</head>
<body>
<!-- 顶部开始 -->
<div class="container">
    <div class="logo"><i id="icon1"></i><a href="${ctx}">SpringBoot2</a></div>
    <div class="left_open">
        <i class="fa fa-exchange"></i>
    </div>
    <div id="searchBox">
        <input id="searchArea" maxlength="10"/>
        <i class="fa fa-search"></i>
    </div>
    <div id="user">
        <img id="portrait" class="portrait"/>
        <div id="user_info">
            <div class="layui-form ays-form-prev">
                <ul>
                    <li>
                        <a href="javascript:void(0)"><i class="layui-icon layui-icon-user"></i>个人中心</a>
                    </li>
                </ul>
                <HR align="center" width="100%" color="gray" SIZE=1>
                <ul>
                    <li>
                        <a id="logout" href="javascript:void(0)"><i class="layui-icon layui-icon-logout"></i>注销</a>
                    </li>
                </ul>
            </div>

        </div>
    </div>
</div>
<div class="left-nav">
    <div id="personal">
        <img id="headicon">
        <div id="username"><span></span></div>
    </div>
    <div id="side-nav">
        <aside class="accordion">
            <div id="menuArea"></div>
        </aside>
    </div>
</div>

<div class="page-content">
    <iframe id="mainFrame" width="100%" height="100%" frameborder="0"></iframe>
</div>

<div class="page-content-bg"></div>
</body>
</html>
