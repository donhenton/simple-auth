<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="container">
<div class="row">
	<div class="col-sm-11 well">
		<h3>Objects In the Security Session</h3>
		 <ul>
                    <li><b>javax.Principal:</b> ${principal} </li>
                    <li><b>Authorities from Token:</b> ${authorities} </li>
                    <li><b>Security Context Object</b>: ${secObject}</li>
                    <li><b>The class for principal in Sec Tags:</b> <sec:authentication property="principal.class.name"/></li>
                    <li><b>UserName from Sec Tags Principal:</b> <sec:authentication property="principal.username"/></li>
                     <li><b>Security Context Object Roles:</b> <sec:authentication property="principal.authorities"/></li>
                     

                 </ul>
	</div>
 </div>

<h3>Security Object Attributes</h3>
<div class="row ">
<ul>
<c:forEach items="${attribs.keySet()}" var="key" varStatus="status">
    <li><b>Key:</b> <c:out value="${key}" />   : <c:out value="${attribs.get(key)}" /></li>

</c:forEach>

</ul>

</div>

<h3>Security Tag Authorization Demonstrations</h3>


<sec:authorize access="hasAuthority('ADMINS')">
    <div class="row ">
        <div class="col-md-offset-1 col-md-6">
        This is content that can only be seen by users with the authority of "ADMINS" using the hasAuthority function
        </div>
    </div>

</sec:authorize>

<sec:authorize access="hasRole('ADMINS')">
    <p>&nbsp;</p>
    <div class="row ">
        <div class="col-md-offset-1 col-md-6">
        This is content that can only be seen by users with the role of "ADMINS" using the hasRole function. 
        A role is an Authority in Spring that starts with <b>ROLE_</b>.
        </div>
    </div>

</sec:authorize>

<sec:authorize access="hasRole('SERVICE')">
    <p>&nbsp;</p>
    <div class="row ">
        <div class="col-md-offset-1 col-md-6">
        This is content that can only be seen by users with the authority of "ROLE_SERVICE" using the hasRole function
        </div>
    </div>

</sec:authorize>



</div>

 
 
 