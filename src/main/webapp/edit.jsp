<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Edit</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h3><a href="meals">Meals</a></h3>
<hr>
<h2 align="center">Edit Meal</h2>

<table width="800px" align="center">
    <tr>
        <td>Date/Time</td>
        <td>Description</td>
        <td>Calories</td>
        <td>Edit</td>
    </tr>
    <tr>
        <form action="edit?action=save" method="post">
            <input name="id" type="hidden" value="${Id}">
            <td><input autofocus type="datetime" name="DataTime" value="${fn:replace(meal.getDateTime(),"T"," ")}"></td>
            <td><input type="text" name="description" id="description" value="${meal.getDescription()}"></td>
            <td><input type="number" name="calories" value="${meal.getCalories()}"></td>
            <td><input type="submit" value="Submit"></td>
        </form>

    </tr>
</table>

</body>
</html>
