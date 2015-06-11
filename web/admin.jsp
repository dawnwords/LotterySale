<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>浦东新区民政彩票管理系统 - 数据管理</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://cdn.datatables.net/1.10.7/css/jquery.dataTables.css"/>
    <link rel="stylesheet" href="css/admin.css"/>
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="//bartaz.github.io/sandbox.js/jquery.highlight.js"></script>
    <script src="//cdn.datatables.net/1.10.7/js/jquery.dataTables.min.js"></script>
    <script src="//cdn.datatables.net/plug-ins/1.10.7/features/searchHighlight/dataTables.searchHighlight.min.js"></script>
    <script src="js/admin.js"></script>
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
            <table id="table-unit" class="display" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>id</th>
                    <th>name</th>
                    <th>unitcode</th>
                    <th>address</th>
                    <th>manager</th>
                    <th>mobile</th>
                    <th>unitnum</th>
                    <th>area</th>
                    <th>population1</th>
                    <th>population2</th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="data-view" id="sale">
            <table id="table-sale" class="display" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>id</th>
                    <th>unitid</th>
                    <th>saleyear</th>
                    <th>salequarter</th>
                    <th>salemonth</th>
                    <th>s1</th>
                    <th>s2</th>
                    <th>s3</th>
                    <th>stotal</th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="data-view" id="user">
            <table id="table-user" class="display" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>id</th>
                    <th>name</th>
                    <th>authority</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</body>
</html>