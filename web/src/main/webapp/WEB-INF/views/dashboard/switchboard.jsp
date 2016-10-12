<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>
<div class="container-fluid" id="main">

  <div class="row-fluid">
    <div class="span">
  	
	  <%@include file="/WEB-INF/views/includes/dashboardMenu.html"%>
	  	<h1 class="serif">${pageTitle}</h1>
	    
	    <form id="form-switchboard" class="" name="dashboard-switchboard" method="get" action="">
	    
			<br> 
		 	<table class="switchboard-medium-font books-table-medium">
			<tr><td>
			<a href="dashboardPage" id="a1">${messages['dashboard.dashboard1']}</a>
			</td></tr>
			<tr><td>
			<a href="dashboardPage2" id="a2">${messages['dashboard.dashboard2']}</a>
			</td></tr>
			<tr><td>
			<a href="dashboardPage3" id="a3">${messages['dashboard.dashboard3']}</a>
			</td></tr>
			</table>
		</form>
 
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>