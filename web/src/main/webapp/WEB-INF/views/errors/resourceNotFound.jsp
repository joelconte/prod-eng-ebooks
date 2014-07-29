<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@include file="/WEB-INF/views/includes/init.jsp" %>

<tags:template>
	<jsp:body>
	<div class="container-fluid" id="main">
 
			<h2>${messages['resourceNotFound.notfounderror']}</h2>
			<p/>
				${messages['resourceNotFound.sorry']}
			<p/>
 
			</div>
	</jsp:body>
</tags:template>