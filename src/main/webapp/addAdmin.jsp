<%@ page import="java.util.List" %>
<%@ page import="uz.pdp.gym.repo.SubscriberRepo" %>
<%@ page import="uz.pdp.gym.config.TgSubscribe" %>
<%@ page import="uz.pdp.gym.config.Roles" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Adminlikka tayinlash </title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container mt-5">
    <h2>Manage Subscriber Role</h2>

    <table class="table table-bordered mt-4">
        <thead>
        <tr>
            <th>ID</th>
            <th>Firstname</th>
            <th>Lastname</th>
            <th>Chat ID</th>
            <th>Role</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            SubscriberRepo subscriberRepo = new SubscriberRepo();
            List<TgSubscribe> subscribers = subscriberRepo.findAll();
            for (TgSubscribe subscriber : subscribers) {
        %>
        <tr>
            <td><%= subscriber.getId() %></td>
            <td><%= subscriber.getFirstname() %></td>
            <td><%= subscriber.getLastname() %></td>
            <td><%= subscriber.getChat_id() %></td>
            <td><%= subscriber.getRoles() %></td>
            <td>
                <form action="/set/admin" method="post">
                    <input type="hidden" name="id" value="<%= subscriber.getId() %>">
                    <button class="btn <%= Roles.ADMIN.equals(subscriber.getRoles()) ? "btn-danger" : "btn-success" %>">
                        <%= Roles.ADMIN.equals(subscriber.getRoles()) ? "Remove Admin" : "Set as Admin" %>
                    </button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>

</div>

</body>
</html>
