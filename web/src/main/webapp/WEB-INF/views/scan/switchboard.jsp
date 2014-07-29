<%@include file="/WEB-INF/views/includes/init.jsp"%>

<tags:template>
<jsp:body>

<div class="container-fluid" id="main">

  <div class="row-fluid">
    <div class="span">
      
	  <%@include file="/WEB-INF/views/includes/scanMenu.html"%>
	  <h1 class="serif">${pageTitle}</h1> 
 
		 
 		<security:authorize access="hasAnyRole('scan', 'admin', 'supervisor')">

		<form id="form-switchboard" class="" name="scan-switchboard" method="get" action="">
			<br> 
		 	<table class="switchboard-medium-font books-table-medium">
			<tr><td>
			<a href="scanReady" id="a2">${messages['scan.scanReady']}</a>
			</td></tr>
			<tr><td>
			<a href="scanInProgress" id="a3">${messages['scan.scanInProgress']}</a>
			</td></tr>
			<tr><td>
			<a href="auditReady" id="a4">${messages['scan.ready']}</a>
			</td></tr>
			<tr><td>
			<a href="auditInProgress" id="a5">${messages['scan.image']}</a>
			</td></tr>
			<tr><td>
			<a href="processedReadyForOrem" id="a6">${messages['scan.processed']}</a>
			</td></tr>
			<tr><td>
			<a href="scanProblems" id="a7">${messages['scan.problems']}</a>
			</td></tr>
			<tr><td>
			<a href="trackingForm?read" id="a1">${messages['scan.trackingForm']}</a>
			</td></tr>
			</table>
		</form>
    	</security:authorize>
 		<security:authorize access="hasAnyRole('scan', 'admin', 'supervisor') == false">
 			<p>${messages['scan.notAuthorized']}</p>
 		</security:authorize>
 
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>