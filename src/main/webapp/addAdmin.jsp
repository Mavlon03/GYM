<%@ page import="uz.pdp.gym.repo.AdminRepo" %>
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
            <input type="text" class="form-control" name="firstname" placeholder="Enter firstname" required>
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

</body>
</html>
