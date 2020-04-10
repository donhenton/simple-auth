<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
 

 <div class="container bg-faded">
    <h1 class="text-center">Demo Application</h1>
   
    <hr>
    <div class="row">
        <div class="col-xs-12">
            <div class="center-block">
                <div class="text-center">
                        <h3>  <a  class="label label-primary" href="<c:out value="${urlCodeAuth}"/>">Sign In With Code Authorization</a> </h3>
  		</div>
                <div class="text-center">
                        <h3>  <a  class="label label-primary" href="<c:out value="${urlPKCE}"/>">Sign In With PKCE</a> </h3>
  		</div>
 
          </div>
        </div>
    </div>
  
    
    </div>
   
   