<%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
<jsp:body>
<div class="container-fluid" id="main">

  <div class="row-fluid">
    <div class="span">
      
	  <%@include file="/WEB-INF/views/includes/processMenu.html"%>
	  <h1 class="serif">${pageTitle}</h1>

		<security:authorize access="hasAnyRole('process', 'admin', 'supervisor')">
     	<form id="form-switchboard" class="" name="scan-switchboard" method="get" action="">
			<br> 
			<table class="switchboard-medium-font books-table-medium">
			<tr><td>
			<a href="waitingForFiles" id="a2">${messages['process.waitingforFiles']}</a>
			</td></tr>
			<tr><td>
			<a href="titleCheck" id="a3">${messages['process.title']}</a>
			</td></tr>
			<tr><td>
			<a href="titleCheckInProgress" id="a4">${messages['process.titleProgress']}</a>
			</td></tr>
			<tr><td>
			<a href="ocrReady" id="a5">${messages['process.import']}</a>
			</td></tr>
			<tr><td>
			<a href="ocrInProgress" id="a5a">${messages['process.ocrInProgress']}</a>
			</td></tr>
			<tr><td>
			<a href="pdfDownload" id="a5b">${messages['process.pdfDownload']}</a>
			</td></tr>
			<tr><td>
			<a href="ocrPdf" id="a6">${messages['process.ocrPdf']}</a>
			</td></tr>
			<tr><td>
			<a href="ocrPdfInProgress" id="a7">${messages['process.ocrPdfInProgress']}</a>
			</td></tr>
			<tr><td>
			<a href="processProblems" id="a8">${messages['process.problems']}</a>
			</td></tr>
			<tr><td>
			<a href="trackingForm?read" id="a1">${messages['process.trackingForm']}</a>
			</td></tr>
			</table>
		</form>
 		</security:authorize>
 		<security:authorize access="hasAnyRole('process', 'admin', 'supervisor') == false">
 			<p>${messages['process.notAuthorized']}</p>
 		</security:authorize>
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>