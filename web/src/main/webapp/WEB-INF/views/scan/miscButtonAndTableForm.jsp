<%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:templateScrollHide>
<jsp:body>

<script>
window.onload=function(){
	var myTH = document.getElementsByTagName("th")[0];
	if(sorttable != null && sorttable.innerSortFunction != null)
		sorttable.innerSortFunction.apply(myTH, []);
	showDupeMsgAfterPasteNewDataPost();//show confirm if already posted and data has dupes
}

function showDupeMsgAfterPasteNewDataPost(){
	
	var sel= document.getElementById("tnData");
	var pastedData = sel.innerHTML;
	sel= document.getElementById("notList");
	var notTns = sel.value;
 
	if(pastedData != "" && notTns != ""){
		toggleOverlay(); //show overlay when first open page since it contained dupe MD and need to confirm to do updates
		
		var updateit = confirm("The following book(s) are not in the list of ready to be scanned books.  \n" + notTns + "\n\nOnly books in the list will be updated.  Click 'OK' and then 'Save' to continue.\n");
		if(updateit == true){
			var f = document.getElementById("fPasteData"); //form
			
			var url = f.action + "?doUpdates";//append doUpdates to flag 
			f.action = url;
		}
	} 
	
}

$(function(){

	  $("#tnTable").tablesorter({
	    widthFixed : true,
	    showProcessing: true,
	    headerTemplate : '{content} {icon}', // Add icon for various themes

	    widgets: [ 'zebra', 'stickyHeaders', 'filter' ],

	    widgetOptions: {

	      // extra class name added to the sticky header row
	      stickyHeaders : '',
	      // number or jquery selector targeting the position:fixed element
	      stickyHeaders_offset : 0,
	      // added to table ID, if it exists
	      stickyHeaders_cloneId : '-sticky',
	      // trigger "resize" event on headers
	      stickyHeaders_addResizeEvent : true,
	      // if false and a caption exist, it won't be included in the sticky header
	      stickyHeaders_includeCaption : true,
	      // The zIndex of the stickyHeaders, allows the user to adjust this to their needs
	      stickyHeaders_zIndex : 2,
	      // jQuery selector or object to attach sticky header to
	      stickyHeaders_attachTo : null,
	      // jQuery selector or object to monitor horizontal scroll position (defaults: xScroll > attachTo > window)
	      stickyHeaders_xScroll : null,
	      // jQuery selector or object to monitor vertical scroll position (defaults: yScroll > attachTo > window)
	      stickyHeaders_yScroll : null,

	      // scroll table top into view after filtering
	      stickyHeaders_filteredToTop: true

	      // *** REMOVED jQuery UI theme due to adding an accordion on this demo page ***
	      // adding zebra striping, using content and default styles - the ui css removes the background from default
	      // even and odd class names included for this demo to allow switching themes
	      // , zebra   : ["ui-widget-content even", "ui-state-default odd"]
	      // use uitheme widget to apply defauly jquery ui (jui) class names
	      // see the uitheme demo for more details on how to change the class names
	      // , uitheme : 'jui'
	    }
	  });
 
	});
 
</script> 


<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/scanMenu.html"%>
		<h1 class="slow-hide serif">${pageTitle}</h1>
	   
		<sf:form id="f1"  name="f1" method="post" action="${buttonsAction}" modelAttribute="book">
			<div class="slow-hide">
			<input type="hidden" id="notList" value="${notTnsInfo}"/>
			
			<select id="siteDropdown" onchange="reloadScanListWithSite('f1', 'siteDropdown' );">
				<option value=""/>
				<c:forEach var="i" items="${allLocations}">
    				<c:if test="${i==location}"><option selected>${i}</option> </c:if>
					<c:if test="${i!=location}"><option>${i}</option> </c:if>
				</c:forEach>
			</select>
			
			<table id="buttonsTable">
			<tr>
				<td></td>
				<c:forEach var="b" items="${buttons}">
					<c:choose>
					<c:when test='${b.get(0).equals("overlayButton") == true}'>
						<td><button id="${b.get(0)}" name="overlayButton" value="${b.get(0)}" onclick="if(doubleCheckSkip('${b.get(2)}')==false)return false; toggleOverlay2('${b.get(2)}'); return false;">${b.get(1)}</button></td>
					</c:when>
					<c:when test='${b.get(0).equals("exportButton") == true}'>
						<td><button id="${b.get(0)}" name="exportButton" value="${b.get(0)}" onclick="tableToExcel('tnTable', 'booklist'); return false;">${b.get(1)}</button></td>
					</c:when>
					<c:otherwise>
						<td><button id="${b.get(0)}" name="button" value="${b.get(0)}" >${b.get(1)}</button></td>
					</c:otherwise>
					</c:choose>
				</c:forEach>
			</tr>
			</table>
			</div>
			 
			 <table id="tnTable" class="sortable colSpace">
			 	<thead id='tableHeader'>
			 	<tr class="tablesorter-headerRow">
			 		<th><p style="margin: 0px 0px 0px 16px;"> &nbsp;</p></th> <!-- column for row count -->
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 	</thead>
			 	
			 	<c:set var="rowNum" value="0"/>
			 	<c:forEach var="tn" items="${allTnsInfo}">
				<tr>
					<td valign="top" align="left" style="white-space: nowrap;">${rowNum+1}&nbsp;</td>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
				 
					<td><a href="trackingForm?update&tn=${tn.get(0)}&returnTo=${returnTo}" ><c:out value="${tn.get(i)}"/></a></td>

					</c:forEach>
					<c:set var="rowNum" value="${rowNum+1}"/>
				</tr>
				</c:forEach>
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
  <form id="fPasteData" name="fPasteData" action="${overlayAction}" method="post">
  	<textarea id="tnData" name="tnData" rows="9" cols="60" style="height: 220px; width: 466px;">${pastedData}</textarea>   <!-- pasted data exists if pasted dupes/nots then reshow page -->
  	<br>
  	<button id="save" name="button" value="save">${messages['save']}</button>
	<button id="cancel" name="button" value="cancel" >${messages['cancel']}</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:templateScrollHide>