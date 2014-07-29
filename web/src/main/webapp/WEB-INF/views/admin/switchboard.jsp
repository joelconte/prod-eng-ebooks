<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>
<div class="container-fluid" id="main">

  <div class="row-fluid">
    <div class="span">
  	
	  <%@include file="/WEB-INF/views/includes/adminMenu.html"%>
	  	<h1 class="serif">${pageTitle}</h1>
	    <security:authorize access="hasAnyRole('admin', 'supervisor')">    
	    <form id="form-switchboard" class="" name="admin-switchboard" method="get" action="">
	    
			<br> 
		 	<table class="switchboard-medium-font books-table-medium">
			<tr><td>
			<a href="trackingForm?read" id="a1">${messages['admin.adminTrackingForm']}</a>
			</td></tr>
			<tr><td>
			<a href="adminProblems" id="a2">${messages['admin.problems']}</a>
			</td></tr>
			<tr><td>
			<a href="receivedNotes" id="a3">${messages['admin.received']}</a>
			</td></tr>
			<tr><td>
			<a href="pdfDateNoReleaseDate" id="a4">${messages['admin.pdf']}</a>
			</td></tr>
			<tr><td>
			<a href="postAdminSwitchboard" id="a5">${messages['admin.post']}</a>
			</td></tr>
			<tr><td>
			<a href="archiveAdminSwitchboard" id="a6">${messages['admin.archive']}</a>
			</td></tr>
			<tr><td>
			<a href="authorizationsAndSettingsAdminSwitchboard" id="a7">${messages['admin.authorizationsAndSettings']}</a>
			</td></tr>	
			<tr><td>
			<a href="batchUpdateAdmin" id="a8">${messages['admin.batchUpdateAdmin']}</a>
			</td></tr>
			<tr><td>
			<a href="metadataAdmin" id="a9">${messages['admin.metadataAdmin']}</a>
			</td></tr>
			<tr><td>
			<a href="backupAdmin" id="a10">${messages['admin.backupAdmin']}</a>
			</td></tr>
			</table>
		</form>
		</security:authorize>
 		<security:authorize access="hasAnyRole('admin', 'supervisor') == false">
 			<p>${messages['admin.notAuthorized']}</p>
 		</security:authorize>
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>