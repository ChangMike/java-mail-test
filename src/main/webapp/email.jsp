<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <title>发送邮件</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
</head>
<body>
    <form method="post" action="/mail.do">
        <div>
            发信人：<input type="email" name="from"/>
        </div>
        <div>
            收信人：<input type="email" name="to"/>
        </div>
        <div>
            主&nbsp;&nbsp;&nbsp;题：<input type="text" name="subject"/>
        </div>
        <div>
            <input type="textarea" name="text" rows="20" cols="20" style="width:250px; height:100px"/>
        </div>
        <div>
            <button type="submit">发送</button>
        </div>
    </form>
</body>
</html>
