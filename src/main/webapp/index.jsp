<!DOCTYPE html>

<html lang="en">

<head>
    <title>Persons and Relatives</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix = "form" uri = "http://www.springframework.org/tags/form"%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="/">Giminaičiai</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="/"><small><span class="glyphicon glyphicon-list-alt"></span></small> &nbsp; Asmenų sąrašas</a></li>
      <li><a href="/person/add"><small><span class="glyphicon glyphicon-plus"></span></small> &nbsp; Pridėti asmenį</a> </li>
    </ul>
  </div>
</nav>
    <div class="container">
        <table class="table table-hover">
            <tr>
                <th>Vardas</th>
                <th>Pavardė</th>
                <th>Gimimo data</th>
            </tr>
            <c:forEach items="${persons}" var="person" varStatus="status">
                <tr>
                    <td><a href='/person/${person.id}'>${person.firstName}</a></td>
                    <td><a href='/person/${person.id}'>${person.lastName}</a></td>
                    <td><a href='/person/${person.id}'>${person.birthDate}</a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>


</html>