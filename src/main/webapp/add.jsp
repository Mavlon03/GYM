<%@ page import="java.util.List" %>
<%@ page import="uz.pdp.gym.config.TgSubscribe" %>
<%@ page import="uz.pdp.gym.repo.SubscriberRepo" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 12/7/2024
  Time: 9:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <title>Add page</title>
</head>
<body>

<a class="btn btn-primary" href="/addSubscriber.jsp">+SUBSCRIBER</a>
<a class="btn btn-black" href="/addAdmin.jsp">+ADMIN</a>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.List" %>
<%@ page import="uz.pdp.gym.config.TgSubscribe" %>
<%@ page import="uz.pdp.gym.repo.SubscriberRepo" %>
<%@ page import="java.util.Objects" %>
<%@ page import="uz.pdp.gym.config.TgSubscribe" %>

<%
    String search = Objects.requireNonNullElse(request.getParameter("search"), "");
    int page1 = Integer.parseInt(Objects.requireNonNullElse(request.getParameter("page"), "1"));
    List<TgSubscribe> tgSubscribes = SubscriberRepo.getSubscriberList(page1, search);
%>

<div class="w-50 p-4">
    <form action="">
        <div class="input-group">
            <input name="search" class = "form control"type="text" placeholder="Search...">
            <button class="btn btn-success">Search</button>
        </div>
    </form>
</div>

<a href="/report.jsp" class="btn btn success">Report</a>

<table class="table mt-3">
    <thead>
    <tr>
         <td>Id</td>
        <td>Photo</td>
        <td>Firstname</td>
        <td>Lastname</td>
        <td>Age</td>
        <td>Role</td>
        <td>Status</td>
        <td>Phone Number</td>
        <td>Created time</td>
        <td>Subscription end time</td>
        <td>Telegram chatId </td>
        <td>Daily</td>
        <td>Monthly</td>
        <td>Yearly</td>
        <td>Action</td>
    </tr>
    </thead>
    <tbody>
    <%
        for (TgSubscribe tgSubscribe : tgSubscribes) {
            String base64Image = Base64.getEncoder().encodeToString(tgSubscribe.getPhoto());
    %>
    <tr>
        <td><%= tgSubscribe.getId() %></td>
        <td>
            <img src="data:image/jpeg;base64,<%= base64Image %>" alt="Subscriber Photo" width="100" height="100" />
        </td>
        <td><%= tgSubscribe.getFirstname() %></td>
        <td><%= tgSubscribe.getLastname() %></td>
        <td><%= tgSubscribe.getAge() %></td>
        <td><%= tgSubscribe.getRoles() %></td>
        <td><%= tgSubscribe.getStatus() ? "On✅" : "Off❌" %></td>
        <td><%= tgSubscribe.getPhone() %></td>
        <td><%=tgSubscribe.getCreatedAt() %></td>
        <td><%= tgSubscribe.getSubscriptionEnd() %></td>
        <td><%= tgSubscribe.getChat_id() %></td>


        <td><%= tgSubscribe.getTrainingTime() != null && tgSubscribe.getTrainingTime().getKunlik() != null ? tgSubscribe.getTrainingTime().getKunlik() : "❌ " %></td>
        <td><%= tgSubscribe.getTrainingTime() != null && tgSubscribe.getTrainingTime().getOylik() != null ? tgSubscribe.getTrainingTime().getOylik() : "❌ " %></td>
        <td><%= tgSubscribe.getTrainingTime() != null && tgSubscribe.getTrainingTime().getYillik() != null ? tgSubscribe.getTrainingTime().getYillik() : "❌             " %></td>
        <td>
            <form action="/remove" method="post">
                <input type="hidden" value="<%=tgSubscribe.getId()%>" name="id">
                <button class="btn btn-danger">Delete</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

<%
    long count = SubscriberRepo.count(search);
    int pageCount = (int) Math.ceil(count / 5.0);
    for (int i = 1; i <= pageCount; i++) {
%>
<a href="?page=<%=i%>&search=<%=search%>" class="btn btn dark"><%=i%></a>
<%
    }
%>
</body>
</html>
