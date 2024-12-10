<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Add Subscriber page</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
  <style>
    body {
      background-color: #f8f9fa;
    }
    .container {
      margin-top: 50px;
    }
    .form-container {
      background-color: white;
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    .form-title {
      text-align: center;
      margin-bottom: 20px;
    }
    .form-button {
      width: 100%;
      text-align: center;
    }
  </style>
</head>
<body>

<div class="container">
  <div class="form-container">
    <h2 class="form-title">Add Subscriber</h2>

    <form action="/add/tgSubscribe" method="post" enctype="multipart/form-data">
      <!-- First Name -->
      <div class="mb-3">
        <input type="text" class="form-control" name="firstname" placeholder="Enter firstname" required>
      </div>

      <!-- Last Name -->
      <div class="mb-3">
        <input type="text" class="form-control" name="lastname" placeholder="Enter lastname" required>
      </div>

      <!-- Age -->
      <div class="mb-3">
        <input type="number" class="form-control" name="age" placeholder="Enter age" required>
      </div>

      <!-- Phone -->
      <div class="mb-3">
        <input type="text" class="form-control" name="phone" placeholder="Enter phone" required>
      </div>

      <!-- Photo -->
      <div class="mb-3">
        <input type="file" class="form-control" name="photo" accept="image/*" required>
      </div>

      <!-- Status Selection -->
      <div class="mb-3">
        <select name="status" class="form-select" required>
          <option value="" disabled selected>All</option>  <!-- "All" tanlanmasligi uchun disabled qo'yilgan -->
          <option value="true">On</option>
          <option value="false">Off</option>
        </select>
      </div>

      <!-- Training Time Selection -->
      <div class="mb-3">
        <select name="trainingTime" class="form-select" required>
          <option value="" disabled selected>All</option>  <!-- "All" tanlanmasligi uchun disabled qo'yilgan -->
          <option value="kunlik">Kunlik</option>
          <option value="oylik">Oylik</option>
          <option value="yillik">Yillik</option>
        </select>
      </div>

      <!-- Add Button -->
      <div class="form-button">
        <button type="submit" class="btn btn-success">Add</button>
      </div>

    </form>
  </div>
</div>


</body>
</html>
