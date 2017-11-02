 <%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
<jsp:body>
<style>

div#overlay2{
	display: none;
	z-index: 2;
	opacity: 0.5;
	filter: alpha(opacity = 50);
	background: #000;
	position: fixed;
	width: 100%;
	height: 100%;
	top: 0px;
	left: 0px;
	text-align: center;
}
 
div#specialBox2{
    background: none repeat scroll 0 0 transparent;
    
	border-bottom : rgba(255, 255, 255, 0.2) 8px solid;
	border-top : rgba(255, 255, 255, 0.2) 8px solid;
	border-left : rgba(255, 255, 255, 0.2) 8px solid;
	border-right : rgba(255, 255, 255, 0.2) 8px solid;
    border-radius: 8px 8px 8px 8px;
    box-shadow: 0 0 20px #333333;
    overflow: visible;
    padding: 10px 10px 10px 10px;
	position:absolute;
	left:350px;
	top:155px;
	display: none;
	z-index: 3;
	width: 500px; 
	height: 400px;
	background: #FFF;
	color: #000;
	
}
div#overlay3{
	display: none;
	z-index: 2;
	opacity: 0.5;
	filter: alpha(opacity = 50);
	background: #000;
	position: fixed;
	width: 100%;
	height: 100%;
	top: 0px;
	left: 0px;
	text-align: center;
}

  div#specialBox3{
    background: none repeat scroll 0 0 transparent;
    
	border-bottom : rgba(255, 255, 255, 0.2) 8px solid;
	border-top : rgba(255, 255, 255, 0.2) 8px solid;
	border-left : rgba(255, 255, 255, 0.2) 8px solid;
	border-right : rgba(255, 255, 255, 0.2) 8px solid;
    border-radius: 8px 8px 8px 8px;
    box-shadow: 0 0 20px #333333;
    overflow: visible;
    padding: 10px 10px 10px 10px;
	position:absolute;
	left:350px;
	top:155px;
	display: none;
	z-index: 3;
	width: 600px; 
	height: 600px;
	background: #FFF;
	color: #000;
	
}
</style>
<script>
window.onload=function(){
	var myTH = document.getElementsByTagName("th")[0];
	if(sorttable != null && sorttable.innerSortFunction != null)
		sorttable.innerSortFunction.apply(myTH, []);
	
	showErrorMsgAfterPastePost();//if search archive.org has parse error of weird text, show ugly msg.
	showMsgAfterPastePost();//show any messages after paste of book list
}  

<c:set var="maxFieldLen" value="75"/>
var maxFieldLen = <c:out value = "${maxFieldLen}"/>;

function showDetailsOverlay(selectedRow, identifier){
 	 
	
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
	if(validatePdfCheck() == false){
		alert("In order to flag this book for import into FamilySearch, please review the validity of the PDF.");
		return;
	}
	var bookId = document.getElementById('ol_identifier').value; 
	var cb = document.getElementById('ol_selected'); 
	var oclc = document.getElementById('ol_oclc').value; 
	var tn = document.getElementById('ol_tn').value; 
	var dnp = document.getElementById('ol_dnp').checked;
	
	if(cb.checked == true){
		doUpdateAddToFs('true', bookId, oclc, tn, dnp);//rest call to update in db
	}else{
		doUpdateAddToFs('false', bookId, oclc, tn, dnp);//rest call to update in db
	}
	
	
	
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

function validatePdfCheck(){
	var cb = document.getElementById('ol_selected'); 
	if(cb.checked == true && pdfChecked == false){
		return false;
	}else{
		return true;
	}
}
var pdfChecked = false;

function viewPdf(){
	var bookId = document.getElementById('ol_identifier').value; 
	window.open('https://archive.org/details/' + bookId, '_blank');
	pdfChecked = true;
}

function doUpdateAddToFs(isSelected, bookId, oclc, tn, dnp){
	 
	var u = document.URL;
	var i = u.indexOf('/ia/');
	u = u.substring(0, i) + "/ia/updateAddToFsAjax";//"http://localhost:8180/BookScan-web/ia/localitySearch"
	var request = $.ajax({
	  url: u,
	  type: "POST",
	  data: {"addToFs" : isSelected, "bookId" : bookId, "oclc" : oclc, "tn" : tn, "dnp" : dnp},
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
	    	var cellToUpdateElem = document.getElementById(selectedId + '_0');//td cell
	    	if(isSelected=='true'){
	    		cellToUpdateElem.innerHTML = 'T';
	    	}else{
	    		cellToUpdateElem.innerHTML = 'F';
	    	}
	    	
	    	cellToUpdateElem = document.getElementById(selectedId + '_13');//td cell
	    	cellToUpdateElem.innerHTML = oclc;
	    	cellToUpdateElem = document.getElementById(selectedId + '_14');//td cell
	    	cellToUpdateElem.innerHTML = tn;
	    	cellToUpdateElem = document.getElementById(selectedId + '_15');//td cell
		    if(dnp==true){
		    	cellToUpdateElem.innerHTML = 'T';
		    }else{
		    	cellToUpdateElem.innerHTML = 'F';
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

function doPost(url){
	var form= document.createElement('form');
	form.method= 'post';
    form.action= url;
 	document.body.appendChild(form);
    form.submit();
}

//gets called twice.  first to get site and then to continue post
function releaseBooksToVerify(firstShowOverlay){
	if(firstShowOverlay == true){
		showSiteOverlay();
		return false;
	}
	
	var site = document.getElementById('ol_siteSelected').value; //if opening overlay, then will be null
	 
	if(site == null || site == ""){
		alert("Please select a site");
		return false;//not selected yet
	}
	
	rc = confirm("Site selected: " + site + "\n\nBooks that you have specified to put into FamilySearch will now be moved to the next step '3- Verify Books'. \nBooks that are not flagged for FamilySearch will be cleared from table.");
	if(rc == false){
		return false;
	}
	var url = "iaMoveToVerify";//in IaSearchController
	////window.location.href=url;
	doPost(url + "?site=" + site);
	
}


function showSiteOverlay( ){
	   
	 
	///////////display
	toggleSiteOverlay();
	 

	//$('#overlay1').draggable();  
	$('#specialBox2').draggable();  
}

function closeSiteOverlay( continueWithSave ){

	//get site from other function via:   document.getElementById('ol_siteSelected').value; 
	  
	
	toggleSiteOverlay();//close
	
	//continue with post
	if(continueWithSave == true){
		releaseBooksToVerify(false);
	}else{
		return false;
	}
}


function toggleSiteOverlay(){
	var overlay = document.getElementById('overlay2');
	var specialBox = document.getElementById('specialBox2');
	overlay.style.opacity = .5;
	if(overlay.style.display == "block"){
	overlay.style.display = "none";
	specialBox.style.display = "none";
	} else {
	overlay.style.display = "block";
	specialBox.style.display = "block";
	}
}

function toggleCopyPasteListOverlay() {
	var overlay = document.getElementById('overlay3');
	var specialBox = document.getElementById('specialBox3');
	overlay.style.opacity = .5;
	if(overlay.style.display == "block"){
	overlay.style.display = "none";
	specialBox.style.display = "none";
	} else {
	overlay.style.display = "block";
	specialBox.style.display = "block";
	}
}
function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}
function showErrorMsgAfterPastePost(){
 
    var msg = getURLParameter('msg');  
    if(msg != null && msg != ""){
        alert(msg);
    }
}
function showMsgAfterPastePost(){
	
	var sel= document.getElementById("textData");
	var pastedData = sel.innerHTML;
	sel= document.getElementById("dupeList");
	var dupTns = sel.value;
 
	if(pastedData != "" && dupTns != ""){
		toggleCopyPasteListOverlay(); //show overlay when first open page since it contained dupe MD and need to confirm to do updates
		
		alert("The following books already exist in TFDB.  \n" + dupTns );
		/*(if(updateit == true){
			var f = document.getElementById("fPasteData"); //form
			
			var url = f.action + "?doUpdates";//append doUpdates to flag 
			f.action = url;
		}*/
	} 
} 
function closeCopyPasteList(){
	toggleCopyPasteListOverlay();
}
function doCopyPasteList(){
	
}
</script> 


<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/iaMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
	   <security:authorize access="hasAnyRole('ia', 'ia_admin', 'admin')">  
		<form id="f1" class="" name="f1" method="post" >
			<input type="hidden" id="dupeList" value="${dupeTnsInfo}"/>
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
			 
			   <td><button id="release" name="button" value="releaseToVerify"  onclick="releaseBooksToVerify(true); return false; ">DONE - Release Books Selected for FamilySearch</button></td>
			   <td><button id="copyPasteList" name="button" value="copyPasteList"  onclick="toggleCopyPasteListOverlay(); return false; ">CopyPaste List of Identifiers</button></td>
			 
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
				
				<tr>
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
							<c:when test='${i==13}'>
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
	    <security:authorize access="hasAnyRole('ia', 'ia_admin', 'admin') == false">
 			<p>${messages['ia.notAuthorized']}</p>
 		</security:authorize>
    </div>
  </div>
</div>
</div>
		
			 
<!-- Start Overlay to get list of items to paste in table -->
<div style="float:right; position:fixed; display: none;"  id="overlay3"></div>
<!-- End Overlay -->
<!-- Start Special Centered Box -->
<div id="specialBox3"  style="position:fixed; width:480px; height: 300px; left:20px; top:20px;  display: none;">
  <p>${messages['pasteExcel']}</p> 
  <form id="fPasteData" name="fPasteData" action="doCopyPasteList" method="post">
  	<input type="hidden" id="site" name="site" value="${site}"/>
  	<textarea id="textData" name="textData" rows="9" cols="60" style="height: 220px; width: 466px;">${pastedData}</textarea>   <!-- pasted data exists if pasted dupes then reshow page -->
  	<br>
  	<button id="save" name="button" value="save" >${messages['save']}</button>
	<button id="cancel" name="button" value="cancel" onclick="closeCopyPasteList(); return false;" >${messages['cancel']}</button>					
  </form>
</div>
<!-- End Special Centered Box -->
		
		
		
		 
<!-- Start Overlay 2 -->
<div style="float:right; position:fixed; display: none;"  id="overlay2"></div>
<!-- End Overlay -->
<!-- Start Special Centered Box -->
<div id="specialBox2"  style="position:fixed; width:450px; height: 150px; left:20px; top:20px;  display: none;">
  <p>Select Site Performing InternetArchive Search</p> 
  <form id="olf2" name="olf2" action="" method="post">
    Site
    <select id="ol_siteSelected" name="site">
    	<option></option>
    	<c:forEach var="i" items="${allIaScanSites}">
    	<c:if test="${i==site}"><option selected>${i}</option> </c:if>
		<c:if test="${i!=site}"><option>${i}</option> </c:if>
		</c:forEach>
	</select>
	<br>
  	<button onclick="closeSiteOverlay( true  ); return false;" id="btnSave" name="button" value="closeSave" >Save Selection</button>	
	<button onclick="closeSiteOverlay( false ); return false;" id="btnCancel" name="button" value="cancel" >Cancel</button>				
  </form>
</div>
<!-- End Special Centered Box -->

		
		
		
			 
<!-- Start Overlay 1 -->
<div style="float:right; position:fixed;" id="overlay1"></div>
<!-- End Overlay -->
<!-- Start Special Centered Box -->
<div id="specialBox1"  style="position:fixed; width:90%; height: 90%; left:20px; top:20px;" >
   
  <form id="overlay_form" name="overlay_form" style="width: 90%; height: 90%;" >

	<input type="hidden" id="ol_selectedId"/>
	
	<table style="width: 100%; height: 100%;">
		<tr>
		<td style="width: 110px;">Add to FS* </td>
		<td><input id="ol_selected" type="checkbox" value="" style="xwidth: 100%"></td>
		</tr>
		<tr>
		<td>BibCheck </td>
		<td><input id="ol_bibCheck" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>Identifier </td>
		<td><input id="ol_identifier" type="text" value="" style="width: 100%"></td>
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
		<td>Title</td>
		<td><input id="ol_title" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>ImageCount </td>
		<td><input id="ol_imageCount" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>Language </td>
		<td><input id="ol_language" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>PublishDate </td>
		<td><input id="ol_publishDate" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>Subject </td>
		<td><textarea id="ol_subject" type="text" value="" rows="3" cols="50" style="width: 100%"></textarea></td>
		</tr>
		<tr>
		<td>Description</td>
		<td><textarea id="ol_description" type="text" value=""  rows="3"  cols="50" style="width: 100%"></textarea></td>
		</tr>
		<tr>
		<td>Publisher </td>
		<td><input id="ol_publisher" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>LicenseUrl </td>
		<td><input id="ol_licenseUrl" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>Rights </td>
		<td><input id="ol_rights" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>Author</td>
		<td><input id="ol_author" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
		<td>DNP*</td>
		<td><input id="ol_dnp" type="checkbox" value="" style="xwidth: 100%"></td>
		</tr>
	</table>  
   <br>
	<button onclick="closeDetailsOverlayAndSave(   ); return false;" id="btnSave" name="button" value="closeSave" >Save Checkbox Selection and Close</button>	
	<button onclick="viewPdf(); return false;" id="btnViewPdf" name="button" value="viewPdf" style="color: red;">View PDF</button>			
	<button onclick="toggleOverlay(); return false;" id="btnCancel" name="button" value="cancel" >Cancel</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		 

</jsp:body>
</tags:template>