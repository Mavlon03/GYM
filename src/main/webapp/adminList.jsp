<%@ page import="java.util.List" %>
<%@ page import="uz.pdp.gym.config.Admin" %>
<%@ page import="uz.pdp.gym.repo.AdminRepo" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <title>Admin List</title>
</head>
<body>

<%
    AdminRepo adminRepo = new AdminRepo();
    List<Admin> admins = adminRepo.findAll();
%>
<a href="/add.jsp">Back</a>
<table class="table mt-3">
    <thead>
    <tr>
        <td>Id</td>
        <td>Firstname</td>
        <td>Lastname</td>
        <td>password</td>
        <td>Role</td>
    </tr>
    </thead>
    <tbody>
    <%
        for (Admin admin : admins) {
    %>
    <tr>
        <td><%= admin.getId() %></td>
        <td><%= admin.getFirstname() %></td>
        <td><%= admin.getLastname() %></td>
        <td><%= admin.getPassword() %></td>
        <td><%= admin.getRoles() %></td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

</body>
</html>
