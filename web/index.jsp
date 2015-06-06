<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>浦东新区民政彩票管理系统 - 登陆</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="js/index.js"></script>
</head>
<body>
<jsp:include page="include-header.jsp" flush="true"/>
<div class="container" style="min-width: 1024px;">
    <div class="row">
        <div class="col-xs-4 col-xs-offset-4 well">
            <h3>用户登陆</h3>

            <form>
                <div class="form-group">
                    <div class="input-group input-group-lg">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
                        <input class="form-control" type="text" placeholder="请输入用户名">
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group input-group-lg">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                        <input class="form-control" type="password" placeholder="请输入密码">
                    </div>
                </div>
                <div class="form-group col-xs-4 col-xs-offset-8">
                    <label class="checkbox-inline"> <input type="checkbox" name="remember_me">记住我 </label>
                </div>
                <div class="form-group">
                    <button class="btn btn-default col-xs-5" type="reset">重置</button>
                    <button class="btn btn-primary col-xs-5 col-xs-offset-2" type="submit">登陆</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>