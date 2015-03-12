<!DOCTYPE html>
<%@include file="/WEB-INF/views/includes/init.jsp" %>
<jsp:directive.attribute name="head" required="false" fragment="true"/>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8">
		<title>${pageTitle}</title>
		<jsp:include page="/WEB-INF/views/includes/brains.jsp"/>
		<jsp:invoke fragment="head"/>
		
	</head>
	<body  class="env-production">
		<div class="container" id="wrapper">
	
		<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
		<div class="padding-md overflow">
			<jsp:doBody />
		</div>
	
		<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
		</div>
	</body>
</html>