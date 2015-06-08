<%@ page import="cn.edu.fudan.bean.User" %>
<%@ page import="cn.edu.fudan.util.Session" %>
<% User user = new Session(request, response).user(); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>浦东新区民政彩票管理系统</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/main.css"/>
    <link rel="stylesheet" href="js/jstree/themes/default/style.min.css"/>
    <script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="js/echarts/echarts.js"></script>
    <script src="js/jstree/jstree.min.js"></script>
    <script src="js/typeahead/bootstrap3-typeahead.min.js"></script>
    <script src="js/hint.js"></script>
    <script src="js/unit.js" charset="UTF-8"></script>
    <script src="js/charts.js" charset="UTF-8"></script>
    <script src="js/main.js" charset="UTF-8"></script>
</head>
<body>
<div class="header">
    <h1 class="col-xs-7">浦东新区民政彩票管理系统</h1>
    <% if (user!=null) { %>
    <a class="col-xs-1 btn btn-default text-center" data-toggle="modal" data-target="#change_password">账号管理</a>
    <% if(user.isAdmin()){%>
    <a class="col-xs-1 btn btn-default text-center" href="admin.jsp">数据管理</a>
    <% } %>
    <a class="col-xs-1 btn btn-danger text-center" href="logout">注销</a>
    <% } %>
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
            </ul>
            <div class="row" id="funcbar">
                <div class="col-xs-5 funcbar left">
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
                            <input type="radio" name="population" value="1">人均/户籍
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="population" value="2">人均/来沪
                        </label>
                    </div>
                </div>
                <div class="funcbar col-xs-5 right" id="funcbar-view">
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
                <button class="btn btn-primary col-xs-2" id="funcbar-refresh" type="button">刷新图表</button>
            </div>
            <div id="chart"></div>
        </div>
    </div>
</div>
<div style="clear: both"></div>
</body>
</html>