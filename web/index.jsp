<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>浦东新区民政彩票管理系统 - 登陆</title>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script src="js/index.js"></script>
    <style type="text/css">
        .header {
            height: 100px;
            min-width: 1104px;
            margin: 5px 30px;
            background: url('imgs/logo.png') no-repeat;
        }

        .header h1 {
            margin: 0 120px;
            line-height: 100px;
            font-family: '微软雅黑', '黑体', arial, sans-serif;
        }
    </style>
</head>
<body>
<div class="header">
    <h1>浦东新区民政彩票管理系统</h1>
</div>
<div class="container" style="min-width: 1024px;">
    <div class="row">
        <div class="col-xs-4 col-xs-offset-4 well">
            <h3>用户登陆</h3>

            <form>
                <div class="form-group">
                    <div class="input-group input-group-lg">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
                        <input class="form-control" name="name" type="text" placeholder="请输入用户名">
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group input-group-lg">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                        <input class="form-control" name="password" type="password" placeholder="请输入密码">
                    </div>
                </div>
                <div class="form-group col-xs-12">
                    <code class="glyphicon glyphicon-exclamation-sign col-xs-8" id="hint"></code>
                    <label class="checkbox-inline col-xs-4 pull-right"> <input type="checkbox" id="remember-me">记住我
                    </label>
                </div>
                <div class="form-group">
                    <button class="btn btn-default col-xs-5" type="reset">重置</button>
                    <button class="btn btn-primary col-xs-5 col-xs-offset-2" id="btn-submit" type="button">登陆</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>