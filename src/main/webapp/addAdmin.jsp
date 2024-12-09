<%@ page import="uz.pdp.gym.repo.AdminRepo" %>
<%@ page import="uz.pdp.gym.config.Admin" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>

<div class="container">
    <h2 class="mt-5">Add Admin</h2>
    <form action="/add/admin" method="post">
        <!-- First Name -->
        <div class="mb-3">
            <input type="text" class="form-control" name="firstname"laceholder="Enter firstname" required>
        </div>

        <!-- Last Name -->
        <div class="mb-3">
            <input type="text" class="form-control" name="lastname" placeholder="Enter lastname" required>
        </div>

        <!-- Password -->
        <div class="mb-3">
            <input type="password" class="form-control" name="password" placeholder="Enter password" required>
        </div>

        <!-- Add Button -->
        <div class="form-button">
            <button type="submit" class="btn btn-success">Add Admin</button>
        </div>
    </form>
</div>

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
        <td>Action</td>

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
        <td>
            <form action="/remove/admin" method="post">
                <input type="hidden" value="<%=admin.getId()%>" name="id">
                <button class="btn btn-danger">Delete</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

</body>
</html>
