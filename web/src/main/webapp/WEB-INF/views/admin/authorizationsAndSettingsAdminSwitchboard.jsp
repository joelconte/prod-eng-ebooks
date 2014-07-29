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
			<a href="userAdmin?read" id="a1">${messages['admin.userAdmin']}</a>
			</td></tr>
			<tr><td>
			<a href="siteAdmin?read" id="a2">${messages['admin.sites']}</a>
			</td></tr>
			<tr><td>
			<a href="siteGoalsAdmin?read" id="a2a">${messages['admin.siteGoals']}</a>
			</td></tr>
			<tr><td>
			<a href="languageAdmin?read" id="a2b">${messages['admin.languages']}</a>
			</td></tr>
			<tr><td>
			<a href="problemReasonAdmin" id="a3">${messages['admin.problemReasons']}</a>
			</td></tr>
			<tr><td>
			<a href="bookLockAdmin" id="a4">${messages['admin.bookLocks']}</a>
			</td></tr>
			</table>
		</form>
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>