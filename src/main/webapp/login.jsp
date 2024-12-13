<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 12/12/2024
  Time: 4:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login page</title>
</head>
<body>
<%

%>
<div class="login-container">
    <h2>Login</h2>
    <form action="/login" method="post">
        <input type="text" name="firstname" placeholder="Firstname" required>
        <input type="text" name="phone" placeholder="Phone" required>
        <button type="submit">Login</button>
    </form>
</div>
</body>
</html>
