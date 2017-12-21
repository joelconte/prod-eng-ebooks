 <%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
<jsp:body>

<script>
window.onload=function(){
	var myTH = document.getElementsByTagName("th")[0];
	if(sorttable != null && sorttable.innerSortFunction != null)
		sorttable.innerSortFunction.apply(myTH, []);
} 


<c:set var="maxFieldLen" value="75"/>
var maxFieldLen = <c:out value = "${maxFieldLen}"/>;

var selectedHighlightedRow = null;
function showDetailsOverlay(selectedRow, identifier){
 	 
	//clear out fields in case prev book data is still there... ajax is slow to load, don't want confusion
	document.getElementById('ol_identifier').value = ''; 
	//var cb = document.getElementById('ol_selected'); 
	document.getElementById('ol_oclc').value = ''; 
	document.getElementById('ol_tn').value = ''; 
	//var dnp = document.getElementById('ol_dnp').checked;
	document.getElementById('ol_volume').value = '';
	document.getElementById('ol_imageCount').value = '';
	document.getElementById('ol_title').value = '';
	document.getElementById('ol_language').value= '';
	document.getElementById('ol_publishDate').value= '';
	document.getElementById('ol_subject').value='';
	document.getElementById('ol_description').value= '';
	document.getElementById('ol_publisher').value= '';
	document.getElementById('ol_licenseUrl').value= '';
	document.getElementById('ol_rights').value= '';
	document.getElementById('ol_author').value='';
	 
				
				
				
	//highlight row from previous click
	if(selectedHighlightedRow != null){
		selectedHighlightedRow.style.cssText="background-color: lightgreen;";
	}
	var rowIdName = "tablerow" + selectedRow;
	var rowElem = document.getElementById(rowIdName);
	rowElem.style.cssText="background-color: #BCD4EC;";
	selectedHighlightedRow = rowElem;
	 
	///////////set values
	var hiddenField = document.getElementById('ol_selectedId');
	hiddenField.value= 'cell' + selectedRow;// + '_0';//column 0 has 'add to fs' field //save in hidden area in order to update when closing overlay
	
	////////////ajax call and set html values
	queryAjaxBookData(identifier);//return map
	
	//////////////

	 
	///////////display
	var overlay = document.getElementById('overlay1');
	var specialBox = document.getElementById('specialBox1');
	overlay.style.opacity = .5;
	if(overlay.style.display == "block"){
		overlay.style.display = "none";
		specialBox.style.display = "none";
	} else {
		overlay.style.display = "block";
		specialBox.style.display = "block";
	}
	 

	//$('#overlay1').draggable();  
	$('#specialBox1').draggable();  
}


function closeDetailsOverlayAndSave( ){

	var bookId = document.getElementById('ol_identifier').value; 
	var cb = document.getElementById('ol_selected'); 
	var oclc = document.getElementById('ol_oclc').value; 
	var tn = document.getElementById('ol_tn').value; 
	var dnp = document.getElementById('ol_dnp').checked;
	var volume = document.getElementById('ol_volume').value;
	var imageCount = document.getElementById('ol_imageCount').value;
	var title = document.getElementById('ol_title').value;
	
	if(cb.checked == true){
		doUpdateAddToFs('', true, bookId, oclc, tn, dnp, volume, imageCount, title);//rest call to update in db
	}else{
		doUpdateAddToFs('', false, bookId, oclc, tn, dnp, volume, imageCount, title);//rest call to update in db
	}
	
	
	toggleOverlay();//close
}


function closeDetailsOverlay(){

    var bookId = document.getElementById('ol_identifier').value; 
	//do update to db that this book was reviewed (id clicked and canceled/saved)
	doUpdateAddToFsChecked( bookId);//rest call to update in db that book was checked
	
	toggleOverlay();//close
	
}

function queryAjaxBookData(id){
	 	
	var u = document.URL;
	var i = u.indexOf('/ia/');
	u = u.substring(0, i) + "/ia/queryAjaxBookData";//"http://localhost:8180/BookScan-web/ia/localitySearch"
	var request = $.ajax({
	  url: u,
	  type: "POST",
	  dataType: "text",
	  data: {"identifier" : id}
	});

	request.done(function(data) {
	    if(data.indexOf('Info:') != -1){
	    	//if error, show it here and don't keep refreshing
	    	alert(data);
	    	//setRefreshingIndicator(false);
	    	return;
	    }
	    /*if(data != 'updated'){
	    	alert("Error while updating database...please try again.  Value returned from server update: " + data);
	    	return;
	    }*/
	    
	    try{
	    	/*	pend("<rows>");

		sb.append("<row><identifier>" + identifier + "</identifier>");
		sb.append("<IS_SELECTED>" + row.get(0) + "</IS_SELECTED>");
		sb.append("<BIBCHECK>" + row.get(1) + "</BIBCHECK>");
		sb.append("<TITLE>" + row.get(2) + "</TITLE>");
		sb.append("<IMAGE_COUNT>" + row.get(3) + "</IMAGE_COUNT>");
		sb.append("<LANGUAGE>" + row.get(4) + "</LANGUAGE>");
		sb.append("<PUBLISH_DATE>" + row.get(5) + "</PUBLISH_DATE>");
		sb.append("<SUBJECT>" + row.get(6) + "</SUBJECT>");
		sb.append("<DESCRIPTION>" + row.get(7) + "</DESCRIPTION>");
		sb.append("<PUBLISHER>" + row.get(8) + "</PUBLISHER>");
		sb.append("<LICENSEURL>" + row.get(9) + "</LICENSEURL>");
		sb.append("<RIGHTS>" + row.get(10) + "</RIGHTS>");
		sb.append("<AUTHOR>" + row.get(11) + "</AUTHOR>");
		sb.append("<OCLC>" + row.get(12) + "</OCLC>");
		sb.append("<TN>" + row.get(13) + "</TN>");
		sb.append("<SITE>" + row.get(14) + "</SITE>");
		sb.append("<BATCH_NUMBER >" + row.get(15) + "</BATCH_NUMBER >");
		sb.append("<OWNER_USERID >" + row.get(16) + "</OWNER_USERID >");
		sb.append("<STATE >" + row.get(17) + "</STATE >");
		sb.append("<STATE_ERROR >" + row.get(18) + "</STATE_ERROR >");
		sb.append("<START_DATE >" + row.get(19) + "</START_DATE >");
		sb.append("<END_DATE >" + row.get(20) + "</END_DATE >");
		sb.append("<FOLDER >" + row.get(21) + "</FOLDER >");
		sb.append("<COMPLETE_DATE >" + row.get(22) + "</COMPLETE_DATE >");
		sb.append("<DNP >" + row.get(23) + "</DNP >");
		sb.append("</row>");
		sb.append("</rows>");
				*/
	    	 
		//alert("ssssss" + data);
			xmlDoc = $.parseXML(data);
			$xml = $(xmlDoc);
			var $row = $xml.find("row");
			var retVal = {};
			$row.each(function(){
 
				var identifier = $(this).find('identifier').text().trim();
			
				//console.log('!!!!!!!!!!mapOfBooks in db ajax= ' + identifier);
/*
console.log("!!SSSSTTTARTTTidentifier" + identifier);
if(identifier == 'Firestone_Company_Electric_Generating_Sets_Service_Manual_And_Parts_Catalog_' || identifier == 'Firestone_Service_Manual_And_Parts_Catalog_For_Bendix_Westinghouse_Tire_Servic'  || identifier == 'identifieryourkeytogodsban00humb'){
	var okheredebug = 3;
}*/
				var IS_SELECTED = $(this).find('IS_SELECTED').text().trim();
				var BIBCHECK = $(this).find('BIBCHECK').text().trim();
				var TITLE = $(this).find('TITLE').text().trim();
				var IMAGE_COUNT = $(this).find('IMAGE_COUNT').text().trim();
				var LANGUAGE = $(this).find('LANGUAGE').text().trim();
				var PUBLISH_DATE = $(this).find('PUBLISH_DATE').text().trim();
				var SUBJECT = $(this).find('SUBJECT').text().trim();
				var DESCRIPTION = $(this).find('DESCRIPTION').text().trim();
				var PUBLISHER = $(this).find('PUBLISHER').text().trim();
				var LICENSEURL = $(this).find('LICENSEURL').text().trim();
				var RIGHTS = $(this).find('RIGHTS').text().trim();
				var AUTHOR = $(this).find('AUTHOR').text().trim();
				var OCLC = $(this).find('OCLC').text().trim();
				var TN = $(this).find('TN').text().trim();
				var SITE = $(this).find('SITE').text().trim();
				var BATCH_NUMBER = $(this).find('BATCH_NUMBER').text().trim();
				var OWNER_USERID = $(this).find('OWNER_USERID').text().trim();
				var STATE = $(this).find('STATE').text().trim();
				var STATE_ERROR = $(this).find('STATE_ERROR').text().trim();
				var START_DATE = $(this).find('START_DATE').text().trim();
				var END_DATE = $(this).find('END_DATE').text().trim();
				var FOLDER = $(this).find('FOLDER').text().trim();
				var COMPLETE_DATE = $(this).find('COMPLETE_DATE').text().trim();
				var DNP = $(this).find('DNP').text().trim();
				var VOLUME = $(this).find('VOLUME').text().trim();
				
				 
				//get selected value dynamically from TD html since it may have changed via ajax after page initially loaded.
				var selected = IS_SELECTED;
				if(selected.trim() == 'T'){
					document.getElementById('ol_selected').checked=true;
					pdfChecked = true;//set to true since already selected
				}else{
					document.getElementById('ol_selected').checked=false;
					pdfChecked = false;
				}
				 
				
				var dnp =  DNP;
				if(dnp.trim() == 'T'){
					document.getElementById('ol_dnp').checked=true;
				}else{
					document.getElementById('ol_dnp').checked=false;
				}
				
				
				
				document.getElementById('ol_bibCheck').value = BIBCHECK;
				document.getElementById('ol_identifier').value=identifier;
				document.getElementById('ol_tn').value= TN;
				document.getElementById('ol_oclc').value= OCLC;
				document.getElementById('ol_title').value= TITLE;
				document.getElementById('ol_imageCount').value= IMAGE_COUNT;
				document.getElementById('ol_language').value= LANGUAGE;
				document.getElementById('ol_publishDate').value= PUBLISH_DATE;
				document.getElementById('ol_subject').value= SUBJECT;
				document.getElementById('ol_description').value= DESCRIPTION;
				document.getElementById('ol_publisher').value= PUBLISHER;
				document.getElementById('ol_licenseUrl').value= LICENSEURL;
				document.getElementById('ol_rights').value= RIGHTS;
				document.getElementById('ol_author').value= AUTHOR;
				document.getElementById('ol_volume').value= VOLUME;
				
			});
	   	 //alert("eeeeeeee");

	    }catch(e){
	    	alert("Error while getting book data from server...");
	    	alert("Error msg: " + e);
	    }
	    

	  
	});

	request.fail(function(jqXHR, textStatus) {
	  alert( "Error while getting book data from server...: " + textStatus );
	});
}
 

function viewPdf(){
	var bookId = document.getElementById('ol_identifier').value; 
	window.open('https://archive.org/details/' + bookId, '_blank');
	
}

//if selectedRow != "" then only update isSelected of bookId
function doUpdateAddToFs( selectedRow, isSelected, bookId, oclc, tn, dnp, volume, imageCount, title){
	
	if(isSelected == true){
		isSelected = "true";
	}else{
		isSelected = "false";
	}
	/*if(cbElem != null)
		cbElem.checked = !cbElem.checked;//update gui
		*/

	//this method gets called in two ways.  If selectedRos is passed in, then need to set hidden field with selected row number in order to find row to update in code below
	var onlyAddToFsParam = "false"; 
	if(selectedRow != ''){
		var hiddenField = document.getElementById('ol_selectedId');
		hiddenField.value= 'cell' + selectedRow;// + '_0';//column 0 has 'add to fs' field //save in hidden area in order to update when closing overlay
		onlyAddToFsParam = "true";//change val for ajax call
	}
	
   	var u = document.URL;
	var i = u.indexOf('/ia/');
	u = u.substring(0, i) + "/ia/updateAddToFsAjax";//"http://localhost:8180/BookScan-web/ia/localitySearch"
	var request = $.ajax({
	  url: u,
	  type: "POST",
	  data: {"onlyAddToFsParam":onlyAddToFsParam, "addToFs" : isSelected, "bookId" : bookId, "oclc" : oclc, "tn" : tn, "dnp" : dnp, "volume" : volume, "imageCount": imageCount, "title": title},
	  dataType: "html"
	});

	request.done(function(data) {
	    //alert(data);
	    if(data != 'updated'){
	    	alert("Error while updating database...please try again.  \n" + data);
	    	return;
	    }
	    
	    try{
	
	    	var hidden = document.getElementById('ol_selectedId');
	   		var selectedId = hidden.value;//id of td field containing yn value to update
	    	var cellToUpdateElem = null;//document.getElementById(selectedId + '_0');//td cell
	    	if(isSelected=='true'){
	    		cellToUpdateElem = document.getElementById("checkbox" + selectedId  + '_0');//div elem
	    		cellToUpdateElem.checked = true;
	    		cellToUpdateElem = document.getElementById("colorDiv" + selectedId  + '_0');//div elem
	    		cellToUpdateElem.innerHTML = 'T';
	    		cellToUpdateElem.style= 'Color: green';
	    	}else{	    	
	    		cellToUpdateElem = document.getElementById("checkbox" + selectedId  + '_0');//div elem
	    		cellToUpdateElem.checked = false;
	    		cellToUpdateElem = document.getElementById("colorDiv" + selectedId  + '_0');//div elem
	    		cellToUpdateElem.innerHTML = 'F';
	    		cellToUpdateElem.style= 'Color: red';
	    	}
	    	
	    	if(onlyAddToFsParam == "false"){
                cellToUpdateElem = document.getElementById(selectedId + '_3');//td cell
	    	    cellToUpdateElem.innerHTML = title;
		    	cellToUpdateElem = document.getElementById(selectedId + '_4');//td cell
	    	    cellToUpdateElem.innerHTML = volume;
	    	    cellToUpdateElem = document.getElementById(selectedId + '_5');//td cell
	    	    cellToUpdateElem.innerHTML = imageCount;
		    	cellToUpdateElem = document.getElementById(selectedId + '_14');//td cell
		    	cellToUpdateElem.innerHTML = oclc;
		    	cellToUpdateElem = document.getElementById(selectedId + '_15');//td cell
		    	cellToUpdateElem.innerHTML = tn;
		    	cellToUpdateElem = document.getElementById(selectedId + '_16');//td cell
			    if(dnp==true){
			    	cellToUpdateElem.innerHTML = 'T';
			    }else{
			    	cellToUpdateElem.innerHTML = 'F';
			    }
			    cellToUpdateElem = document.getElementById(selectedId + '_17');//viewed
			    cellToUpdateElem.innerHTML = 'T';
	    	}
	    }catch(e){
	    	alert("Error while updating database...please try again.");
	    	alert("Error msg: " + e);
	    }
	});

	request.fail(function(jqXHR, textStatus) {
	  alert( "Request to update Add to FamilySearch checkbox failed: " + textStatus );
	});
} 

//just record fact that book was checked
function doUpdateAddToFsChecked( bookId ){
	 
	var u = document.URL;
	var i = u.indexOf('/ia/');
	u = u.substring(0, i) + "/ia/updateAddToFsAjax";//"http://localhost:8180/BookScan-web/ia/localitySearch"
	var request = $.ajax({
	  url: u,
	  type: "POST",
	  data: {"checked" : "true", "bookId" : bookId},
	  dataType: "html"
	});

	request.done(function(data) {
	    //alert(data);
	    if(data != 'updated'){
	    	alert("Error while updating database...please try again.  \n" + data);
	    	return;
	    }
	    var hidden = document.getElementById('ol_selectedId');
   		var selectedId = hidden.value;//id of td field containing yn value to update
   		cellToUpdateElem = document.getElementById(selectedId + '_17');//viewed
	    cellToUpdateElem.innerHTML = 'T';
	     
	});

	request.fail(function(jqXHR, textStatus) {
	  alert( "Request to update Add to FamilySearch checkbox failed: " + textStatus );
	});
}
function releaseBooksToPreDownload(){
	rc = confirm("Books that you have specified to put into FamilySearch will now be moved to the next step '4- Import Books'. \nBooks that are not flagged for FamilySearch will be cleared from table.");
	if(rc == false){
		return false;
	}
	var url = "iaMoveToPreDownload";//in IaSearchController
	//window.location.href=url;
	doPost(url);
}
function doPost(url){
	var form= document.createElement('form');
	form.method= 'post';
    form.action= url;
 	document.body.appendChild(form);
    form.submit();
}

</script> 


<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/iaMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
	    <security:authorize access="hasAnyRole('ia_admin', 'admin')">  
		<form id="f1" class="" name="f1" method="post" action="${buttonsAction}" >
			 
			<table id="buttonsTable">
			<tr>
				<td></td><!-- space for checkboxes if I remember correctly -->
				<!-- no buttons  
				<c:forEach var="b" items="${buttons}">
					<c:if test='${b.get(0).equals("overlayButton") == true}'>
						<td><button id="${b.get(0)}" name="overlayButton" value="${b.get(0)}" onclick="toggleOverlay(); return false;">${b.get(1)}</button></td>
					</c:if>
					<c:if test='${b.get(0).equals("overlayButton") == false}'>
						<td><button id="${b.get(0)}" name="button" value="${b.get(0)}"    <c:if test="${b.size()>2}"> onclick="if(${b.get(2)}()==false)return false;"</c:if> >${b.get(1)}</button></td>
					</c:if>
				</c:forEach>
				-->
			 
			   <td><button id="release" name="button" value="release"  onclick="releaseBooksToPreDownload(); return false; ">DONE - Release Books Selected for Import into FamilySearch</button></td>
			 
			</tr>
					
			</table>
			
			<table id="tnTable" class="sortable colSpace">
			 	<tr>
				 	<th><p style="margin: 0px 0px 0px 16px;"> </p></th> <!-- column for button -->
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 	
			 	<c:set var="rowNum" value="0"/>
			 	<c:forEach var="row" items="${allRows}">
				
			 	<c:set var="rowColor" value=""/>
				<c:if test="${row.get(17)=='T'}">
					<c:set var="rowColor" value="lightgreen"/>
				</c:if>
				
				<tr id="tablerow${rowNum}" style="background-color: ${rowColor};">
					<td valign="top" align="left" style="white-space: nowrap;">${rowNum+1}&nbsp; </td>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
						<td id="cell${rowNum}_${i}" valign="top" align="left">
						 
							<c:set var="fieldValue" value="${row.get(i)}"/>
							<c:choose>
							<c:when test='${i==2}'>
								<c:choose>
								<c:when test="${ fn:length( fieldValue ) le 35}" >
									<a title="View Details" href="javascript:void(0);" onclick="showDetailsOverlay( '${rowNum}', '${row.get(2)}' ); return false;" ><c:out value="${fieldValue}"/></a>
								</c:when> 
								<c:otherwise>
									<a title="View Details" href="javascript:void(0);" onclick="showDetailsOverlay( '${rowNum}', '${row.get(2)}' ); return false;" ><c:out value="${fn:substring(fieldValue, 0, 34)}..."/></a>
								</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test='${i==0}'>
							   
								<c:choose>
								<c:when test="${ fieldValue == 'F'}" >
								    <input type="checkbox" id="checkboxcell${rowNum}_${i}" style="zoom: 2; " onChange="doUpdateAddToFs( '${rowNum}', this.checked, '${row.get(2)}', null, null, null, null)">
									<div  id="colorDivcell${rowNum}_${i}" style="Color: red;"/><c:out value="${fieldValue}"/></div>
								</c:when>
								<c:otherwise>
								    <input type="checkbox" id="checkboxcell${rowNum}_${i}" checked="true" style="zoom: 2; " onChange="doUpdateAddToFs( '${rowNum}', this.checked, '${row.get(2)}', null, null, null, null)">
									<div id="colorDivcell${rowNum}_${i}" style="Color: green;"/><c:out value="${fieldValue}"/></div>
								</c:otherwise>
							    </c:choose>
							</c:when>
							<c:when test='${i==14}'>
								<c:choose>
								<c:when test="${ fn:length( fieldValue ) le 35}" >
									<c:out value="${fieldValue}"/>
								</c:when>
								<c:otherwise>
									<c:out value="${fn:substring(fieldValue, 0, 35-1)}..."/>
								</c:otherwise>
							    </c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
								<c:when test="${ fn:length( fieldValue ) le maxFieldLen}" >
									<c:out value="${fieldValue}"/>
								</c:when>
								<c:otherwise>
									<c:out value="${fn:substring(fieldValue, 0, maxFieldLen-1)}..."/>
								</c:otherwise>
							    </c:choose>
							</c:otherwise>
							</c:choose>
							
						</td>
					</c:forEach>
					<c:set var="rowNum" value="${rowNum+1}"/>
				</tr>
				</c:forEach>
			</table>
		</form>
	    </security:authorize>
	    <security:authorize access="hasAnyRole( 'ia_admin', 'admin') == false">
 			<p>${messages['ia.notAuthorized']}</p>
 		</security:authorize>
    </div>
  </div>
</div>
</div>
			 
<!-- Start Overlay to get list of TNs to paste in table -->
<div style="float:right; position:fixed;" id="overlay1"></div>
<!-- End Overlay -->
<!-- Start Special Centered Box -->
<div id="specialBox1"  style="position:fixed; width:90%; height: 90%; left:20px; top:20px;" >
   
  <form id="overlay_form" name="overlay_form" style="width: 90%; height: 90%;" >

	<input type="hidden" id="ol_selectedId"/>
	
	<table style="width: 100%; height: 100%;">
		<tr>
		<td style="width: 110px;">Add to FS* </td>
		<td><input id="ol_selected" type="checkbox" value="" style="zoom: 2; xwidth: 100%"></td>
		</tr>
		<tr>
		<td>BibCheck </td>
		<td><input id="ol_bibCheck" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>Identifier </td>
		<td><input id="ol_identifier" type="text" value="" style="width: 100%" readonly></td>
		</tr>
		<tr>
		<td>TN*</td>
		<td><input id="ol_tn" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>OCLC*</td>
		<td><input id="ol_oclc" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>Title*</td>
		<td><input id="ol_title" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>Volume*</td>
		<td><input id="ol_volume" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>ImageCount*</td>
		<td><input id="ol_imageCount" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>Language </td>
		<td><input id="ol_language" type="text" value="" style="width: 100%" readonly></td>
		</tr>
		<tr>
		<td>PublishDate </td>
		<td><input id="ol_publishDate" type="text" value="" style="width: 100%" readonly></td>
		</tr>
		<tr>
		<td>Subject </td>
		<td><textarea id="ol_subject" type="text" value="" rows="3" cols="50" style="width: 100%" readonly></textarea></td>
		</tr>
		<tr>
		<td>Description</td>
		<td><textarea id="ol_description" type="text" value=""  rows="3"  cols="50" style="width: 100%" readonly></textarea></td>
		</tr>
		<tr>
		<td>Publisher </td>
		<td><input id="ol_publisher" type="text" value="" style="width: 100%" readonly></td>
		</tr>
		<tr>
		<td>LicenseUrl </td>
		<td><input id="ol_licenseUrl" type="text" value="" style="width: 100%" readonly></td>
		</tr>
		<tr>
		<td>Rights </td>
		<td><input id="ol_rights" type="text" value="" style="width: 100%" readonly></td>
		</tr>
		<tr>
		<td>Author</td>
		<td><input id="ol_author" type="text" value="" style="width: 100%" readonly></td>
		</tr>
		<tr>
		<td>DNP*</td>
		<td><input id="ol_dnp" type="checkbox" value="" style="zoom: 2; xwidth: 100%"></td>
		</tr>
	</table>  
   <br>
	<button onclick="closeDetailsOverlayAndSave(   ); return false;" id="btnSave" name="button" value="closeSave" >Save Checkbox Selection and Close</button>	
	<button onclick="viewPdf(); return false;" id="btnViewPdf" name="button" value="viewPdf" >View PDF</button>	
	<button onclick="closeDetailsOverlay(); return false;" id="btnCancel" name="button" value="cancel" >Cancel</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:template>