<!DOCTYPE html>

<html lang="en">

<head>
    <title>Persons and Relatives</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

</head>
<body>
        <div>
                    <a href="/person/add">PRIDĖTI ASMENĮ</a>
        </div>
    <div>
        <table>
            <tr>
                <th>Vardas</th>
                <th>Pavardė</th>
                <th>Gimimo data</th>
            </tr>
            <c:forEach items="${persons}" var="person" varStatus="status">
                <tr>
                    <td><a href="/person/${person.id}">${person.firstName}</a></td>
                    <td><a href="/person/${person.id}">${person.lastName}</a></td>
                    <td>${person.birthDate}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>


</html>