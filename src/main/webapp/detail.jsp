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
				<c:out value = "${person.firstName} ${person.lastName} ${person.birthDate}"/>
		<a href="/person/edit/${person.id}">Redaguoti asmenį</a>

                <c:forEach items="${relativesMapped}" var="relativeMapped" varStatus="status">
                    <ul>
                        <li>${relativeMapped.value}</li>
                        <li>${relativeMapped.key.firstName}</li>
                        <li>${relativeMapped.key.lastName}</li>
                        <li>${relativeMapped.key.birthDate}</li>
                    </ul>
                </c:forEach>
        <a href="/">Asmenų sąrašas</a>
</body>

</html>