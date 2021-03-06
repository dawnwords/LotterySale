﻿<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>浦东新区民政彩票管理系统 - 数据管理</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://cdn.datatables.net/1.10.7/css/jquery.dataTables.css"/>
    <link rel="stylesheet" href="http://cdn.bootcss.com/blueimp-file-upload/9.5.7/css/jquery.fileupload.min.css">
    <link rel="stylesheet" href="css/admin.css"/>
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="js/jquery-ui/widget.js"></script>
    <script src="//cdn.bootcss.com/blueimp-file-upload/9.5.7/jquery.iframe-transport.js"></script>
    <script src="//cdn.bootcss.com/blueimp-file-upload/9.5.7/jquery.fileupload.min.js"></script>
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
<div class="modal fade" id="modify" tabindex="-1" role="dialog" aria-labelledby="modify-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="modify-title"></h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal"></form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger pull-left" id="modify-delete">删除</button>
                <span class="glyphicon result modify"></span>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="modify-submit">提交</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="add" tabindex="-1" role="dialog" aria-labelledby="add-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="add-title"></h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal"></form>
            </div>
            <div class="modal-footer">
                <span class="glyphicon result add"></span>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="add-submit">提交</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="upload" tabindex="-1" role="dialog" aria-labelledby="upload-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="upload-title">导入结果</h4>
            </div>
            <div class="modal-body">
                <div id="upload-result" class="hidden">
                    <p class="result success"></p>
                    <p class="result fail"></p>
                </div>
                <div id="upload-fails"></div>
            </div>
        </div>
    </div>
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
        <div id="function">
            <button type="button" class="btn btn-default btn-sm" id="function-refresh" data-loading-text="正在刷新...">
                <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>刷新非叶节点数据
            </button>
            <button class="btn btn-default btn-sm fileinput-button" id="upload-button">
                <span class="glyphicon glyphicon-open-file"></span>
                <span>导入xls数据</span>
                <input id="upload-input" type="file" name="file"/>
            </button>
            <button type="button" class="btn btn-success btn-sm" id="function-add">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增数据
            </button>
        </div>
        <div class="data-view active" id="unit">
            <table id="table-unit" class="display" cellspacing="0" width="100%"></table>
        </div>
        <div class="data-view" id="sales">
            <table id="table-sales" class="display" cellspacing="0" width="100%"></table>
        </div>
        <div class="data-view" id="user">
            <table id="table-user" class="display" cellspacing="0" width="100%"></table>
        </div>
    </div>
</div>
</body>
</html>