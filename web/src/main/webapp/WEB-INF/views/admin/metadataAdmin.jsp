<%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
<jsp:body>

<script>
window.onload=function(){
	var myTH = document.getElementsByTagName("th")[0];
	if(sorttable != null && sorttable.innerSortFunction != null)
		sorttable.innerSortFunction.apply(myTH, []);
}
  
</script>
<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/adminMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
	   
		<sf:form id="f1" class="" name="f1" method="post" action="${buttonsAction}" modelAttribute="book">
			<table id="buttonsTable">
			<tr>
				<td></td>
				<td><button id="overlayButton" name="overlayButton" value="overlayButton" onclick="toggleOverlay(); return false;">${messages['pasteExcelToTabBelow']}</button></td>
		  		<td><button id="metadata" name="button" value="metadata"  onclick="updateUrlAndTarget('f1', 'metadataAdmin', 'metadata', '_blank' );">${messages['adminMetadata.metadata']}</button></td>
		  		<td><button id="update06" name="button" value="update06" onclick="alert('Metadata is already in BookMetadata table.  Go to metadata menu to access details.'); return false;">${messages['adminMetadata.update06']}</button></td>
		  		<td><button id="compare02Tiff" name="button" value="compare02Tiff" onclick="updateUrlAndTarget('f1', 'metadataAdmin', 'compare02Tiff', '_blank' );">${messages['adminMetadata.compare02']}</button></td> 
		  		<td><button id="backups" name="button" value="backups" onclick="updateUrlAndTarget('f1', 'metadataAdmin', 'backups', '_blank' );">${messages['adminMetadata.backups']}</button></td>
		  		<td>
		  		<table>
		  			<tr><td><button id="previewTf" name="button" value="previewTf"  onclick="updateUrlAndTarget('f1', 'metadataAdmin', 'previewTf', '_blank' );">${messages['adminMetadata.preview']}</button></td></tr>
		  			<tr><td><button id="emptyTf" name="button" value="emptyTf"  onclick="if(doubleCheckUpdateTitleCheckNull()==false) return false;">${messages['adminMetadata.empty']}</button></td></tr>
		  		</table>
		  		</td>
		  		<td>
		  		<table> 
		  			<tr><td><button id="siteStatistics" name="button" value="siteStatistics"  onclick="updateUrlAndTarget('f1', 'metadataAdmin', 'siteStatistics', '_blank' );">${messages['adminMetadata.siteStats']}</button></td></tr>
		  			<tr><td><button id="trackingForm" name="button" value="trackingForm"  onclick="updateUrlAndTarget('f1', 'metadataAdmin', 'trackingForm', '_blank' );">${messages['adminMetadata.trackingForm']}</button></td></tr>
	 			</table>
	 			</td>
				
			</tr>
			</table>
			 
			<table>
			 <tr><td>
			 <table id="tnTable" class="sortable colSpace">
			 	<tr>
			 		<th>W/O Prefix</th>
			 	</tr>
			 	<c:forEach var="tn" items="${allTnsInfo}">
				<tr>
					<td><a href="trackingForm?read&tn=${tn.get(0)}" ><c:out value="${tn.get(i)}"/></a></td>
				</tr>
				</c:forEach>
			</table>
			</td>
			<td style="padding-left: 211px;">
			 <table id="copyStuff" class=""  frame="box">
			 	<tr>
			 		<td>Backup Drive Name</td>
			 		<td>	
			 			<select id="drive"  name="drive">
							<option value=""/>
							<c:forEach var="i" items="${allDriveNames}">
    							<option>${i}</option> 
    						</c:forEach>
						</select> 
					</td>
			 	</tr>
			 	<tr>
			 		<td>Backup Drive Letter</td>
			 		<td>	
			 			<select id="driveLetter" name="driveLetter">
							<option value="E">E</option>
						 	<option value="F">F</option>
							<option value="G">G</option>
							<option value="H">H</option>
							<option value="I">I</option>
						</select> 
					</td>
			 	</tr>
			 	<tr>
			 		<td><button id="doCopy" name="button" value="doCopy"  onclick="alert('This function is obsolite.  Backups are done externally.'); return false;">${messages['adminMetadata.doCopy']}</button></td>
			  		<td><button id="doLogFile" name="button" value="doLogFile" >${messages['adminMetadata.logFile']}</button></td>
		  		
			 	</tr>
			</table>
			</td>
			</table>
		</sf:form>
		      
    </div>
 
  </div>
</div>
</div>
			 
<!-- Start Overlay to get list of TNs to paste in table -->
<div id="overlay1"></div>
<!-- End Overlay -->
<!-- Start Special Centered Box -->
<div id="specialBox1">
  <p>${messages['pasteExcel']}</p> 
  <form id="f2" name="f2" action="doDataInsert" method="post">
  	<textarea id="tnData" name="tnData" rows="9" cols="60" style="height: 220px; width: 466px;"></textarea>
  	<br>
  	<button id="save" name="button" value="save">${messages['save']}</button>
	<button id="cancel" name="button" value="cancel" >${messages['cancel']}</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:template>