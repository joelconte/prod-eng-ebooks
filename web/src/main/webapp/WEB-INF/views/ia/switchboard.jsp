<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>
<div class="container-fluid" id="main">

  <div class="row-fluid">
    <div class="span">
  	
	  <%@include file="/WEB-INF/views/includes/iaMenu.html"%>
	  	<h1 class="serif">${pageTitle}</h1>
	    <security:authorize access="hasAnyRole('ia', 'ia_admin', 'admin')">    
	    <form id="form-switchboard" class="" name="admin-switchboard" method="get" action="">
	    
			<br> 
		 	<table class="switchboard-medium-font books-table-medium">
			<tr><td>
			<a href="localitySearch" id="a1">${messages['ia.localitySearch']}</a>
			</td></tr>
			<tr><td>
			<a href="iaSelectBooks" id="a2">${messages['ia.selectBooks']}</a>
			</td></tr>
			<tr><td>
			<a href="iaVerifyBooks" id="a3">${messages['ia.verifyBooks']}</a>
			</td></tr>
			<tr><td>
			<a href="iaPreDownloadBooks" id="a4">${messages['ia.importBooks']}</a>
			</td></tr>
			
			<tr><td>
			<a href="iaMonitorDownloadBooks" id="a5">${messages['ia.monitorDownloadBooks']}</a>
			</td></tr>
			<tr><td>
			<a href="iaInsertTfdbBooks" id="a6">${messages['ia.insertTfdbBooks']}</a>
			</td></tr>
			
			</table>
		</form>
		</security:authorize>
 		<security:authorize access="hasAnyRole('ia', 'ia_admin', 'admin') == false">
 			<p>${messages['ia.notAuthorized']}</p>
 		</security:authorize>
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>