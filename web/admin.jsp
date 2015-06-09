<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>浦东新区民政彩票管理系统 - 数据管理</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="js/admin.js"></script>
    <link rel="stylesheet" href="css/admin.css"/>
</head>
<body>
<div class="header">
    <h1>浦东新区民政彩票管理系统 - 数据管理</h1>
    <a class="btn btn-default text-center" href="main.jsp">退出编辑</a>
</div>
<div class="content">
    <div class="wrapper">
        <ul id="nav-bar" class="nav nav-tabs">
            <li role="presentation" class="active">
                <a href="" data-toggle="tab" data-tab="0">节点数据编辑</a>
            </li>
            <li role="presentation">
                <a href="" data-toggle="tab" data-tab="1">销量数据编辑</a>
            </li>
            <li role="presentation">
                <a href="" data-toggle="tab" data-tab="2">用户数据编辑</a>
            </li>
        </ul>
        <div class="data-view active" id="unit">
            节点数据编辑
        </div>
        <div class="data-view" id="sale">
            销量数据编辑
        </div>
        <div class="data-view" id="user">
            用户数据编辑
        </div>
    </div>
</div>
</body>
</html>