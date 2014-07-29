<%@include file="/WEB-INF/views/includes/init.jsp"%>

<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>


<tags:template>
<jsp:body>
<div class="container-fluid" id="main">

  <div class="row-fluid">
    <div class="span">
  	
	  <%@include file="/WEB-INF/views/includes/metadataMenu.html"%>

		<h1 class="serif">${pageTitle}</h1>
	
		<security:authorize access="hasAnyRole('metadata', 'admin',  'supervisor')">    
	 	 <form id="form-switchboard" class="" name="metadata-switchboard" method="get" action="">
			<br> 
		 	<table class="switchboard-medium-font books-table-medium">
			<tr><td>
			<a href="metadataNewBooks" id="a2">${messages['metadata.newBooks']}</a>
			</td></tr>
			<tr><td>
			<a href="metadataUpdateBooks" id="a2b">${messages['metadata.updateBooks']}</a>
			</td></tr>
			<tr><td>
			<a href="metadataInternetArchiveNewBooks" id="a3">${messages['metadata.newInternetArchiveBooks']}</a>
			</td></tr>
			<!-- <tr><td>
			<a href="metadataSendToScan" id="a4">${messages['metadata.readySendToScan']}</a>
			</td></tr> -->
			<tr><td>
			<a href="metadataCompleteAndSent" id="a5">${messages['metadata.completeAndSent']}</a>
			</td></tr>
			<tr><td>
			<a href="metadataForm?read" id="a1">${messages['metadata.metadataForm']}</a>
			</td></tr>
			</table>
		</form>
		</security:authorize>
 		<security:authorize access="hasAnyRole('metadata', 'admin', 'supervisor') == false">
 			<p>${messages['metadata.notAuthorized']}</p>
 		</security:authorize>
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>