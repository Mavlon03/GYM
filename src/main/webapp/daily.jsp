<%@ page import="java.util.List" %>
<%@ page import="uz.pdp.gym.repo.SubscriberRepo" %>

<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <title>Daily Subscribers</title>
</head>
<body>
<h2>Daily Subscribers</h2>

<%
    SubscriberRepo subscriberRepo = new SubscriberRepo();
    List<Object[]> subscribes = subscriberRepo.dailySubscribers();
%>

<table class="table">
    <tr>
        <th>Date</th>
        <th>Subscribers Count</th>
    </tr>
    <% for (Object[] row : subscribes) { %>
    <tr>
        <td><%= row[0] %></td>
        <td><%= row[1] %></td>
    </tr>
    <% } %>
</table>

</body>
</html>
