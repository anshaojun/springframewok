<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path;
    request.getSession().setAttribute("basePath", basePath);
%>
<c:set var="ctx" value="${basePath}"/>
<link type="text/css" href="${ctx}/css/common.css" rel="stylesheet"/>
<link type="text/css" href="${ctx}/css/font-awesome-4.7.0/css/font-awesome.css" rel="stylesheet"/>
<link type="text/css" href="${ctx}/js/layui/css/layui.css" rel="stylesheet"/>
<link rel="stylesheet" href="${ctx}/js/layui_ext/dtree/dtree.css" type="text/css">
<link rel="stylesheet" href="${ctx}/js/layui_ext/dtree/font/dtreefont.css" type="text/css">
<link type="text/css" rel="stylesheet" href="${ctx}/js/iconpicker-master/assets/layui/css/layui.css"/>
<link rel="shortcut icon" href="${ctx}/images/favicon.png" type="image/x-icon">
<script type="text/javascript" src="${ctx}/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/messages_zh.js"></script>
<script type="text/javascript" src="${ctx}/js/md5.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-form.js"></script>
<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctx}/js/layui/layui.js"></script>
<script type="text/javascript">
    var ctx = '${ctx}';
    var user;

    function getChildNodes(treeNode, result, excludeParent) {
        for (var i in treeNode) {
            if (treeNode[i].children.length == 0) {
                result.push(treeNode[i].id);
            } else {
                if (excludeParent) {
                    result.push(treeNode[i].id);
                }
                result = getChildNodes(treeNode[i].children, result, excludeParent);
            }
        }
        return result;
    }
</script>