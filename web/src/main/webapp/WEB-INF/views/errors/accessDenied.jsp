 
<%@include file="/WEB-INF/views/includes/init.jsp" %>

<tags:template>
	<jsp:body>
		<div class="container-fluid" id="main">
			<h2>${messages['accessDenied.accessdenied']}</h2>
			<p/>
				${messages['accessDenied.accessdeniedinfo']}
			<p/>
		</div>
	</jsp:body>
</tags:template>