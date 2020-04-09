<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<div class="container">


<div>Exception:  ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</div>
<div>${param.auth}</div>
	 <c:if test="${'fail' eq param.auth}">
            <p>
            <div class="alert alert-danger">Login Failed!!! Reason :
                ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</div>
            </c:if>
</div>

 