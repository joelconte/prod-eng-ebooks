 <%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
<jsp:body>

<script>
window.onload=function(){
	var myTH = document.getElementsByTagName("th")[0];
	if(sorttable != null && sorttable.innerSortFunction != null)
		sorttable.innerSortFunction.apply(myTH, []);
	
	populateMapOfBooks();
	queryStatusOfDownloads();//call once and then it iterates
	//while(true){
		//alert("hell");
		//setTimeout(alert("hell0"), 5000000);
	//}
	//queryStatusOfDownloads();//get ajax querying started
} 

var mapOfBooks = {};

function populateMapOfBooks(){
	var exists = document.getElementById('cell0_0');
	
	if(exists == null){
		return;//no rows to work with
	}
	
	var iter = 0;
	while(true){
		exists = document.getElementById('cell' + iter + '_0');
		
		if(exists == null){
			return; //end of rows
		}
		
		var fieldState = document.getElementById('cell' + iter + '_0').innerHTML.trim();
		var fieldStart = document.getElementById('cell' + iter + '_1').innerHTML.trim();
		var fieldEnd = document.getElementById('cell' + iter + '_2').innerHTML.trim();
		var fieldFolder = document.getElementById('cell' + iter + '_3').innerHTML.trim();
		//var x1 = document.getElementById('cell' + iter + '_6');
		//var x2 = document.getElementById('cell' + iter + '_6').childNodes[1];
		var fieldIdentifier = document.getElementById('cell' + iter + '_6').childNodes[1].innerHTML;

		/*if(fieldIdentifier == 'Firestone_Company_Electric_Generating_Sets_Service_Manual_And_Parts_Catalog_' || fieldIdentifier == 'Firestone_Service_Manual_And_Parts_Catalog_For_Bendix_Westinghouse_Tire_Servic'  || fieldIdentifier == 'identifieryourkeytogodsban00humb'){

			console.log("!!SSSSTTTARTTTidentifier" + fieldIdentifier);
			 
			var okheredebug = 3;
		}*/
		
		mapOfBooks[fieldIdentifier] = { 'rowNum':iter, 'fieldState':fieldState, 'fieldStart':fieldStart, 'fieldEnd':fieldEnd, 'fieldFolder':fieldFolder};
		
		//console.log("!!SSSSTTTARTTTidentifier " + fieldIdentifier);
		 
		
		iter++;
	}
}
<c:set var="maxFieldLen" value="75"/>
var maxFieldLen = <c:out value = "${maxFieldLen}"/>;

function queryStatusOfDownloads(){
	setRefreshingIndicator(true);
	
	
	var u = document.URL;
	var i = u.indexOf('/ia/');
	u = u.substring(0, i) + "/ia/queryStatusOfDownloadsAjax";//"http://localhost:8180/BookScan-web/ia/localitySearch"
	var request = $.ajax({
	  url: u,
	  type: "GET",
	  dataType: "text"
	});

	request.done(function(data) {
	    if(data.indexOf('Info:') != -1){
	    	//if error, show it here and don't keep refreshing
	    	alert(data);
	    	setRefreshingIndicator(false);
	    	return;
	    }
	    /*if(data != 'updated'){
	    	alert("Error while updating database...please try again.  Value returned from server update: " + data);
	    	return;
	    }*/
	    
	    try{
	    	/*	sb.append("<rows><row><identifier>" + row.get(5) + "</identifier>");
				sb.append("<state>" + row.get(0) + "</state>");
				sb.append("<start_date>" + row.get(1) + "</start_date>");
				sb.append("<end_date>" + row.get(2) + "</end_date></row></rows>\r");*/
	    	 
		//alert("ssssss" + data);
			xmlDoc = $.parseXML(data);
			$xml = $(xmlDoc);
			var $row = $xml.find("row");
			
			$row.each(function(){
 
				var identifier = $(this).find('identifier').text().trim();
				if(identifier.length <= maxFieldLen){
					//
				}else{ 
					//identifier = identifier.substring(0, maxFieldLen-1) + '...';
				}
				//console.log('!!!!!!!!!!mapOfBooks in db ajax= ' + identifier);
/*
console.log("!!SSSSTTTARTTTidentifier" + identifier);
if(identifier == 'Firestone_Company_Electric_Generating_Sets_Service_Manual_And_Parts_Catalog_' || identifier == 'Firestone_Service_Manual_And_Parts_Catalog_For_Bendix_Westinghouse_Tire_Servic'  || identifier == 'identifieryourkeytogodsban00humb'){
	var okheredebug = 3;
}*/
				var fieldStateNew = $(this).find('state').text().trim();
				var fieldStartNew = $(this).find('start_date').text().trim();
				var fieldEndNew = $(this).find('end_date').text().trim();
				var fieldFolderNew = $(this).find('folder').text().trim();
				
				if(mapOfBooks == undefined){
					console.log('mapOfBooks is undefined ' + identifier);
					alert('mapOfBooks is undefined ' + identifier);
				}
				if(mapOfBooks[identifier] == undefined){
					console.log('mapOfBooks[id] is undefined ' + identifier);
					alert('mapOfBooks[id] is undefined ' + identifier);
				}
				
				if(mapOfBooks[identifier] != undefined){
					var x=mapOfBooks[identifier];
					var rowNum = mapOfBooks[identifier].rowNum;//rownum to get to td fields in html table
					/*if(mapOfBooks == undefined || rowNum == undefined){
						alert("UNDEV");
					}*/
					var fieldStateExisting = mapOfBooks[identifier].fieldState;
					var fieldStartExisting = mapOfBooks[identifier].fieldStart;
					var fieldEndExisting = mapOfBooks[identifier].fieldEnd;
					var fieldFolderExisting = mapOfBooks[identifier].fieldFolder;
/*
console.log("!!identifier" + identifier);
	console.log("!!fieldStateExisting" + fieldStateExisting);
	console.log("!!fieldStartExisting" + fieldStartExisting);
	console.log("!!fieldEndExisting" + fieldEndExisting);
	*/
					//if any 3 fields changed, then update html
					if(fieldStateNew != fieldStateExisting || fieldStartNew != fieldStartExisting || fieldEndNew != fieldEndExisting || fieldFolderNew != fieldFolderExisting){
						//console.log("!!!!!rownum=" + rowNum);
						document.getElementById('cell' + rowNum + '_0').innerHTML = fieldStateNew;//state
						document.getElementById('cell' + rowNum + '_1').innerHTML = fieldStartNew;//start date
						document.getElementById('cell' + rowNum + '_2').innerHTML = fieldEndNew;//end date
						document.getElementById('cell' + rowNum + '_3').innerHTML = fieldFolderNew;//folder
						
						mapOfBooks[identifier].fieldState = fieldStateNew;
						mapOfBooks[identifier].fieldStart = fieldStartNew;
						mapOfBooks[identifier].fieldEnd = fieldEndNew;
						mapOfBooks[identifier].fieldFolder = fieldFolderNew;
					}
				}
				
				 
			});
	   	 //alert("eeeeeeee");

	    }catch(e){
	    	alert("Error while getting status from server...");
	    	alert("Error msg: " + e);
	    }
	    

	    
	    //if all are complete stop refreshing page
		var keys = Object.keys(mapOfBooks);
		var allComplete = true;
		for(var i = 0; i< keys.length; i++){
	 
	    	if(mapOfBooks[keys[i]].fieldState != 'download and xml complete'){
	    	 	allComplete = false;
	    		break;
	    	}
		}
    
    	if(allComplete == true){
    		setRefreshingIndicator(false);
    		
    		title = document.getElementById('title').innerHTML;
    		document.getElementById('title').innerHTML =  title + ' - (all complete)';
        			
    		return false;//done
    	}
    	
	    //queryStatusOfDownloads();//restart
	  	setRefreshingIndicator(false);
	    setTimeout(queryStatusOfDownloads, 5000);
	});

	request.fail(function(jqXHR, textStatus) {
	  alert( "Error while getting status from server...: " + textStatus );
	});
}
 
function setRefreshingIndicator(enableIt){
		if(enableIt){
			
			$("body").css("cursor", "progress")
		}else{
			$("body").css("cursor", "default");
		}
	   
	   
	    title = document.getElementById('title').innerHTML;
		if(title.length < 35){
			document.getElementById('title').innerHTML =  title + '.';
		}else{
			document.getElementById('title').innerHTML =  title.substring(0, title.length - 9);
		}
}
/*
function setRefreshingIndicator(enableIt){
	   $("body").css("cursor", "default");
	    title = document.getElementById('title').innerHTML;
		if(enableIt){
			document.getElementById('title').innerHTML =  title + ' *refreshing*';
		}else{
			document.getElementById('title').innerHTML =  title.substring(0, title.length - 13);
		}
}*/
 

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
				document.getElementById('ol_error').value= STATE_ERROR;
				
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
 

function releaseBooksToInsert(){
	rc = confirm("Books that are complete will be sent to next step.  Books in error or books selected to not add to TFDB will be removed.");
	if(rc == false){
		return false;
	}
	
	//check that all books are complete
	 
	var keys = Object.keys(mapOfBooks);
	for(var i = 0; i< keys.length; i++){
  	
		var fState = mapOfBooks[keys[i]].fieldState;
    	if(fState != 'download and xml complete'  && fState != "pdf or xml error"  && fState != 'copyright - not public domain'){
    		alert('One or more books have not completed downloading.  ex: ' + keys[i]);
    		return false;
    	}
	}
	 
	var url = "iaMoveToInsertTfdb";//in IaSearchController
	//window.location.href=url;
	doPost(url);
}

function retryDownloads(){
	cont = confirm("All books will be re-downloaded from Internet Archive.");
	if(cont == true){
		
		var url = "iaDoBooksDownloadRedo";//in IaSearchController
		//window.location.href=url;
		doPost(url);
	}else{
		return false;
	}
}

function retryDownloads2(){
	cont = confirm("Books not completed will be re-downloaded from Internet Archive.");
	if(cont == true){
		
		var url = "iaDoBooksDownloadRedo2";//in IaSearchController
		//window.location.href=url;
		doPost(url);
	}else{
		return false;
	}
}


function stopDownloads(){
	cont = confirm("All downloads and xml processing will be stopped.\n\nDue to nature of OS processes, please wait a few minutes before RETRYing downloads.");
	if(cont == true){
		
		var url = "iaDoBooksDownloadStop";//in IaSearchController
		//window.location.href=url;
		doPost(url);
	}else{
		return false;
	}
}

function doPost(url){
	var form= document.createElement('form');
	form.method= 'post';
    form.action= url;
 	document.body.appendChild(form);
    form.submit();
}

 

function closeDetailsOverlayAndSave( ){

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


function viewPdf(){
	var bookId = document.getElementById('ol_identifier').value; 
	window.open('https://archive.org/details/' + bookId, '_blank');
	
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
	    	var cellToUpdateElem = document.getElementById(selectedId + '_4');//td cell
	    	if(isSelected=='true'){
	    		cellToUpdateElem.innerHTML = 'T';
	    	}else{
	    		cellToUpdateElem.innerHTML = 'F';
	    	}
	    	
	    	cellToUpdateElem = document.getElementById(selectedId + '_17');//td cell
	    	cellToUpdateElem.innerHTML = oclc;
	    	cellToUpdateElem = document.getElementById(selectedId + '_18');//td cell
	    	cellToUpdateElem.innerHTML = tn;
	    	cellToUpdateElem = document.getElementById(selectedId + '_19');//td cell
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

</script> 


<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/iaMenu.html"%>
		<h1 id="title" class="serif">${pageTitle}</h1>
	    <security:authorize access="hasAnyRole( 'ia_admin', 'admin')">  
		<form id="f1" class="" name="f1" method="post" action="" >
			
			<table id="buttonsTable">
			<tr>
				<td></td><!-- space for checkboxes if I remember correctly -->
				 
			  <!--  <td><button id="release" name="button" value="release"  onclick="queryStatusOfDownloads(); return false; ">Refresh</button></td>  -->
			    <td><button id="" name="button" value=""  onclick="releaseBooksToInsert(); return false; ">DONE - All downloaded books and xml are ready for next step</button></td>
			  	<td><button id="" name="button" value=""  onclick="stopDownloads(); return false; ">STOP Downloads - In case of error</button></td>
				<td><button id="" name="button" value=""  onclick="retryDownloads(); return false; ">RETRY ALL Downloads - In case of error</button></td>
				<td><button id="" name="button" value=""  onclick="retryDownloads2(); return false; ">RETRY Non-Complete Downloads - In case of error</button></td>
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
							<c:when test='${i==6}'>
								 
							<a title="View Details" href="javascript:void(0);" onclick="showDetailsOverlay( '${rowNum}', '${row.get(6)}' ); return false;" ><c:out value="${fieldValue}"/></a>
							 
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
		<tr>
		<td>ErrorMsg</td>
		<td><input id="ol_error" type="text" value="" style="width: 100%"></td>
		</tr>
		<tr>
	</table>  
   <br>
	<button onclick="closeDetailsOverlayAndSave(   ); return false;" id="btnSave" name="button" value="closeSave" >Save Checkbox Selection and Close</button>	
	<button onclick="viewPdf(); return false;" id="btnViewPdf" name="button" value="viewPdf" >View PDF</button>	
	<button onclick="toggleOverlay(); return false;" id="btnCancel" name="button" value="cancel" >Cancel</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:template>