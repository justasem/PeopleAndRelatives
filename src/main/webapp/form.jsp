<!DOCTYPE html>

<html lang="en">

<head>
    <title>Persons and Relatives</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix = "form" uri = "http://www.springframework.org/tags/form"%>
</head>
<body>
    <form:form method="POST" action="/person" modelAttribute="person">
         <table>
            <tr>
               <td><form:label path = "firstName">Vardas</form:label></td>
               <td><form:input path = "firstName" /></td>
               <td><form:errors path = "firstName" /></td>
            </tr>
            <tr>
               <td><form:label path = "lastName">Pavardė</form:label></td>
               <td><form:input path = "lastName" /></td>
               <td><form:errors path = "lastName" /></td>
            </tr>
            <tr>
               <td><form:label path = "birthDate">Gimimo data</form:label></td>
               <td><form:input path = "birthDate" /></td>
               <td><form:errors path = "birthDate" /></td>
            </tr>
            <tr>
               <td colspan = "2">
                  <input type = "submit"  value = "Patvirtinti"/>
               </td>
            </tr>
         </table>
     </form:form>
     <a href="/">Asmenų sąrašas</a>
</body>

</html>