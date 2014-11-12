<%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
<jsp:body>
<div class="container-fluid" id="main">

  <div class="row-fluid">
    <div class="span">
      
	  <%@include file="/WEB-INF/views/includes/searchMenu.html"%>
	  <h1 class="serif">${pageTitle}</h1>

		<security:authorize access="hasAnyRole('search', 'admin', 'supervisor')">
     	<form id="form-switchboard" class="" name="scan-switchboard" method="get" action="">
			<br> 
			<table class="switchboard-medium-font books-table-medium">
			<tr><td>
			<a href="searchMisc" id="a1">${messages['search.miscSearch']}</a>
			</td></tr>
			<tr><td>
			<a href="trackingForm?read" id="a2">${messages['search.searchTrackingForm']}</a>
			</td></tr>
			<tr><td>
			<a href="searchListTnsAllColumns" id="a3">${messages['search.searchListOfTnsAllColumns']}</a>
			</td></tr>
			<tr><td>
			<a href="searchListTns" id="a3">${messages['search.searchListOfTns']}</a>
			</td></tr>
			<tr><td>
			<a href="searchListSecondaryIds" id="a4">${messages['search.searchListOfSecondaryIds']}</a>
			</td></tr>
			<tr><td>
			<a href="searchForUrls" id="a5">${messages['search.searchForUrls']}</a>
			</td></tr>
			<tr><td>
			<a href="searchForPids" id="a6">${messages['search.searchForPids']}</a>
			</td></tr>
			</table>
		</form>
 		</security:authorize>
 		<security:authorize access="hasAnyRole('search', 'admin', 'supervisor') == false">
 			<p>${messages['process.notAuthorized']}</p>
 		</security:authorize>
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>