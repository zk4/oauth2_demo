<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<h1>QQ login</h1>
<form action="/login" method="post">
    <input type="text" name="username" placeholder="输入用户名 ">
    <input type="text" name="password" placeholder="输入密码  ">
    <input type="hidden" name="state" value="${state}" />
    <input type="hidden" name="response_type" value="${response_type}" />

    <input type="submit" value="提交">
</form>
<#if error??>
    ${error}
</#if>
</body>
</html>