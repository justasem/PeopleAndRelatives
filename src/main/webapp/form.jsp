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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.css">

</head>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="/">Giminaičiai</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="/"><small><span class="glyphicon glyphicon-list-alt"></span></small> &nbsp; Asmenų sąrašas</a></li>
      <li><a href="/person/add"><small><span class="glyphicon glyphicon-plus"></span></small> &nbsp; Pridėti asmenį</small></a></li>
    </ul>
  </div>
</nav>
<div class="container">
    <form:form  id="form" data-toggle="validator" role="form" class="form-horizontal" method="POST" action="/person" modelAttribute="person">
             <div class="form-group">
                 <form:label path = "firstName" class="control-label">Vardas:</form:label>
                 <form:input path = "firstName" class="form-control" placeholder="Vardas arba Vardas AntrasVardas" name="firstName"/>
             </div>
             <div><form:errors path = "firstName" class="text-danger" /></div>

             <div class="form-group">
                 <form:label path = "lastName" class="control-label">Pavardė:</form:label>
                 <form:input path = "lastName" class="form-control" placeholder="Pavardė arba Pavardytė-Pavardienė" name="lastName"/>
             </div>
             <div><form:errors path = "lastName" class="text-danger" /></div>

             <div class="form-group">
                 <form:label path = "birthDate" class="control-label">Gimimo data:</form:label>
                 <div>
                    <div class="input-group input-append date" id="datepicker">
                         <form:input path = "birthDate" class="form-control" type="text" name="date" value="1990-01-30"/>
                         <span class="input-group-addon add-on"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                 </div>
             </div>
             <div><form:errors path = "birthDate" class="text-danger" /></div>
             <br>
             <div class="form-group">
                <button type="submit" class="btn btn-primary">Patvirtinti</button>
             </div>
             <br>
             <br>
	</form:form>
</div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
    <script src="/js/script.js"></script>
</body>

</html>
