<%@ page import="cn.edu.fudan.bean.User" %>
<%@ page import="cn.edu.fudan.util.Session" %>
<% User user = new Session(request, response).user(); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>浦东新区民政彩票管理系统</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://cdn.datatables.net/1.10.7/css/jquery.dataTables.css"/>
    <link rel="stylesheet" href="css/main.css"/>
    <link rel="stylesheet" href="js/jstree/themes/default/style.min.css"/>
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="js/echarts/echarts.js"></script>
    <script src="js/jstree/jstree.min.js"></script>
    <script src="js/typeahead/bootstrap3-typeahead.min.js"></script>
    <script src="//bartaz.github.io/sandbox.js/jquery.highlight.js"></script>
    <script src="//cdn.datatables.net/plug-ins/1.10.7/features/searchHighlight/dataTables.searchHighlight.min.js"></script>
    <script src="//cdn.datatables.net/1.10.7/js/jquery.dataTables.min.js"></script>
    <script src="js/hint.js"></script>
    <script src="js/unit.js" charset="UTF-8"></script>
    <script src="js/charts.js" charset="UTF-8"></script>
    <script src="js/main.js" charset="UTF-8"></script>
</head>
<body>
<div class="header">
    <h1>浦东新区民政彩票管理系统</h1>
    <% if (user != null) { %>
    <a class="btn btn-danger text-center" href="logout">注销</a>
    <a class="btn btn-default text-center" data-toggle="modal" data-target="#change-password">账号管理</a>
    <% if (user.isAdmin()) {%>
    <a class="btn btn-default text-center" href="admin.jsp">数据管理</a>
    <% } %>
    <% } %>
</div>
<div class="modal fade" id="change-password" tabindex="-1" role="dialog" aria-labelledby="change-password-title"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="change-password-title">修改密码</h4>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="change-password-old" class="control-label">原始密码：</label>
                        <input type="password" class="form-control" id="change-password-old" placeholder="请输入原始密码">
                    </div>
                    <div class="form-group">
                        <label for="change-password-new" class="control-label">新密码：</label>
                        <input type="password" class="form-control" id="change-password-new" placeholder="请输入新密码">
                    </div>
                    <div class="form-group">
                        <label for="change-password-confirm" class="control-label">确认新密码:</label>
                        <input type="password" class="form-control" id="change-password-confirm" placeholder="请再次输入新密码">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <span class="glyphicon change-pass"></span>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="change-password-submit">提交</button>
            </div>
        </div>
    </div>
</div>
<div class="content">
    <div class="left-side" id="unit">
        <div class="unit-wrapper">
            <div class="unit-title">选择节点</div>
            <div class="unit-content" id="unit-tree"></div>
        </div>
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-xs-3 control-label" for="unit-keyword">快速检索:</label>

                <div class="col-xs-9">
                    <input class="form-control" id="unit-keyword" type="text" placeholder="请输入节点名称"/>
                </div>
            </div>
        </form>
        <div class="unit-wrapper">
            <div class="unit-title">已选节点
                <button class="btn btn-xs btn-danger" id="unit-deselect" type="submit">清空</button>
            </div>
            <div class="unit-content" id="unit-selected"></div>
        </div>
    </div>
    <div class="right-side">
        <div class="funcbar-wrapper">
            <ul id="nav-bar" class="nav nav-tabs">
                <li role="presentation" class="active" data-dimen="time">
                    <a href="" data-toggle="tab" data-mode="0">销量分析</a>
                </li>
                <li role="presentation" data-dimen="compare">
                    <a href="" data-toggle="tab" data-mode="1">同/环比</a>
                </li>
                <li role="presentation" data-dimen="compare">
                    <a href="" data-toggle="tab" data-mode="2">销量预警</a>
                </li>
            </ul>
            <div class="row" id="funcbar">
                <div class="col-xs-6 funcbar left">
                    <div id="funcbar-time">
                        <label class="radio-inline">
                            <input type="radio" name="time" value="0">年度
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="time" value="1">季度
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="time" value="2" checked>月度
                        </label>
                    </div>
                    <div class="hide" id="funcbar-compare">
                        <label class="radio-inline">
                            <input type="radio" name="compare" value="0" checked>同比
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="compare" value="1">环比
                        </label>
                    </div>
                    <div id="funcbar-population">
                        <label class="radio-inline">
                            <input type="radio" name="population" value="0" checked>非人均
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="population" value="1">户籍人口人均
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="population" value="2">实有人口人均
                        </label>
                    </div>
                </div>
                <div class="funcbar col-xs-10 left-cross hide" id="funcbar-warning">
                    <label for="funcbar-warning-year">选择年份</label>
                    <select class="input-sm" id="funcbar-warning-year"></select>
                    <label for="funcbar-warning-month">选择月份</label>
                    <select class="input-sm" id="funcbar-warning-month"></select>
                </div>
                <div class="funcbar col-xs-4 right" id="funcbar-view">
                    <label class="checkbox-inline">
                        <input type="checkbox" id="funcbar-view-mark">指标值
                    </label>
                    <label class="checkbox-inline">
                        <input type="checkbox" id="funcbar-view-avg">平均值
                    </label>
                    <label class="checkbox-inline">
                        <input type="checkbox" id="funcbar-view-maxmin">最值
                    </label>
                </div>
                <button class="btn btn-primary col-xs-2 hide" id="funcbar-search" type="button">搜索</button>
                <button class="btn btn-primary col-xs-2" id="funcbar-refresh" type="button">刷新图表</button>
            </div>
            <div id="chart"></div>
            <div class="hide" id="table">
                <table class="display" cellspacing="0" width="100%"></table>
            </div>
        </div>
    </div>
</div>
<div style="clear: both"></div>
</body>
</html>