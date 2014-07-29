<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="web-spring" uri="http://code.lds.org/web/spring" %>

<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sw" uri="http://code.lds.org/security/web" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %> 

<web-spring:message-source var="messages" scope="request"/>

<spring:message code="init.siteName" text="BookScan" var="siteName" scope="request" />