<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
 

 <div class="container bg-faded">
        <h1 class="text-center">Demo Application</h1>

        <hr>

        <div class="row">
            <div class="col-xs-6">
                   <h2>Access Token</h2>
            </div>
            <div class="col-xs-6">
               <h2>Id Token</h2>
            </div>
        </div>


        <div class="row">
            <div class="col-xs-6">

                    <div>
                            <textarea cols="70" rows="10">${accessToken}</textarea>
                    </div>

            </div>
            <div class="col-xs-6">

                    <div>


                            <textarea cols="70" rows="10">${idToken}</textarea>
                    </div>

            </div>
        </div>


        <div class="row">
            <ul>
            <c:forEach items="${userInfo.keySet()}" var="key">
                <li><c:out value="${key}" /> --->  <c:out value="${userInfo.get(key)}" /> </li>

           
            </c:forEach>

 </ul>
        </div>

</div>
 
  