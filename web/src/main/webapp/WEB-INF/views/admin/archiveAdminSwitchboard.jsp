<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>
<div class="container-fluid" id="main">

  <div class="row-fluid">
    <div class="span">
  	
	  <%@include file="/WEB-INF/views/includes/adminMenu.html"%>
	 
		<h1 class="serif">${pageTitle}</h1>
	    	
	  <form id="form-switchboard" class="" name="admin-switchboard" method="get" action="">
			<br>
		 	<table class="switchboard-medium-font books-table-medium">
			<tr><td>
			<a href="tiffsBackup" id="a1">${messages['admin.tiffsBackup']}</a>
			</td></tr>
			<tr><td>
			<a href="tiffsArchive" id="a2">${messages['admin.tiffsArchive']}</a>
			</td></tr>
			<tr><td>
			<a href="pdfBackup" id="a3">${messages['admin.pdfBackup']}</a>
			</td></tr>
			<tr><td>
			<a href="pdfArchive" id="a4">${messages['admin.pdfArchive']}</a>
			</td></tr>
			</table>
		</form>
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>