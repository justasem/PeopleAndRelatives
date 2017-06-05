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
		<a href="/person/edit/${person.id}">Redaguoti asmens duomenis</a>

                <c:forEach items="${relativesWithType}" var="relativesWithType" varStatus="status">
                    <ul>
                        <li>${relativesWithType.relativeType}</li>
                        <li>${relativesWithType.firstName}</li>
                        <li>${relativesWithType.lastName}</li>
                        <li>${relativesWithType.birthDate}</li>
                    </ul>
                </c:forEach>
        <a href="/">Asmenų sąrašas</a>
</body>

</html>