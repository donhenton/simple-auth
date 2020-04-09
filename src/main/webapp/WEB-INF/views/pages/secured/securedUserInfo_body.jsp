<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/spring.tld" prefix="spring"%>
<%@ taglib uri="/WEB-INF/tld/spring-form.tld" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:url var="baseURL" value="/" />
<%

    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

%>

 

<div>
<ol>
    <li>ClientName:  ${clientName}</li>
    <li>RegId: ${regId}</li>
    <li>AuthUri: ${authUri}</li>
 <li>Client: ${client}</li>
</ol>
<div>
    <textarea>
    ${accessToken}
    </textarea>
 </div>
<div>
    <textarea>
    ${tRequest}
    </textarea>
 </div>
</div>