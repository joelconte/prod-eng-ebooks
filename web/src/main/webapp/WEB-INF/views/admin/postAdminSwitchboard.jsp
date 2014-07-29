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
			<a href="releasedBooks" id="a1">${messages['admin.releasedBooks']}</a>
			</td></tr>
			<tr><td>
			<a href="addPageNumbers" id="a2">${messages['admin.addPageNumbers']}</a>
			</td></tr>
			<tr><td>
			<a href="bookScanDatesAdded" id="a3">${messages['admin.bookScanDatesAdded']}</a>
			</td></tr>
			<tr><td>
			<a href="compressionCodeEntry" id="a4">${messages['admin.compressionCodeEntry']}</a>
			</td></tr>
			<tr><td>
			<a href="booksLoadedOnline" id="a5">${messages['admin.booksLoadedOnline']}</a>
			</td></tr>
			<tr><td>
			<a href="../report/monthlyReport" id="a6">${messages['admin.monthlyReports']}</a>
			</td></tr>
			</table>
		</form>
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>