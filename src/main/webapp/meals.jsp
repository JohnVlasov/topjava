<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2 align="center">Meals</h2>

<table width="800px" align="center">
    <tr>
        <td>Id</td>
        <td>Date/Time</td>
        <td>Description</td>
        <td>Calories</td>
        <td>Edit</td>
        <td>Delete</td>
    </tr>

    <c:forEach items="${list}" var="meal">

        <tr ${meal.isExcess()? 'bgcolor="orange"' : 'bgcolor="#f0f8ff"'}>
            <td>${meal.getId()}</td>
            <td>${fn:replace(meal.getDateTime(),"T"," ")}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
            <td><a href="edit?action=edit&mealId=${meal.getId()}">Edit</a></td>


            <td><a href="/delete/${meal.getId()}">delete</a></td>
        </tr>

    </c:forEach>

</table>

</body>
</html>
