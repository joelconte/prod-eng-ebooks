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

function showDetailsOverlay(selectedRow, selected, bibCheck, identifier, title, imageCount, language, publishDate, subject, description, publisher, licenseUrl, rights, author, oclc, tn, dnp){
	  
	 
	///////////set values
	var hiddenField = document.getElementById('ol_selectedId');
	hiddenField.value= 'cell' + selectedRow;// + '_0';//column 0 has 'add to fs' field //save in hidden area in order to update when closing overlay
	
	//get selected value dynamically from TD html since it may have changed via ajax after page initially loaded.
	var selected = document.getElementById('cell' + selectedRow + '_0').innerHTML;
	if(selected.trim() == 'T'){
		document.getElementById('ol_selected').checked=true;
	}else{
		document.getElementById('ol_selected').checked=false;
	}
	

	var oclc = document.getElementById('cell' + selectedRow + '_13').innerHTML.trim();
	var tn = document.getElementById('cell' + selectedRow + '_14').innerHTML.trim();
	
	var dnp = document.getElementById('cell' + selectedRow + '_15').innerHTML;
	if(dnp.trim() == 'T'){
		document.getElementById('ol_dnp').checked=true;
	}else{
		document.getElementById('ol_dnp').checked=false;
	}
	
	
	document.getElementById('ol_bibCheck').value=bibCheck;
	document.getElementById('ol_identifier').value=identifier;
	document.getElementById('ol_tn').value=tn;
	document.getElementById('ol_oclc').value=oclc;
	document.getElementById('ol_title').value=title;
	document.getElementById('ol_imageCount').value=imageCount;
	document.getElementById('ol_language').value=language;
	document.getElementById('ol_publishDate').value=publishDate;
	document.getElementById('ol_subject').value=subject;
	document.getElementById('ol_description').value=description;
	document.getElementById('ol_publisher').value=publisher;
	document.getElementById('ol_licenseUrl').value=licenseUrl;
	document.getElementById('ol_rights').value=rights;
	document.getElementById('ol_author').value=author;
	 
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
				
				<tr>
					<td valign="top" align="left" style="white-space: nowrap;">${rowNum+1}&nbsp; </td>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
						<td id="cell${rowNum}_${i}" valign="top" align="left">
							
							<c:set var="fieldValue" value="${row.get(i)}"/>
							<c:choose>
							<c:when test='${i==2}'>
								<c:choose>
								<c:when test="${ fn:length( fieldValue ) le 35}" >
									<a title="View Details" href="javascript:void(0);" onclick="showDetailsOverlay( '${rowNum}', '${row.get(0)}', '${row.get(1)}', '${row.get(2)}', '${row.get(3)}','${row.get(4)}', '${row.get(5)}', '${row.get(6)}', '${row.get(7)}', '${row.get(8)}', '${row.get(9)}', '${row.get(10)}', '${row.get(11)}', '${row.get(12)}', '${row.get(13)}', '${row.get(14)}', '${row.get(15)}'); return false;" ><c:out value="${fieldValue}"/></a>
								</c:when>
								<c:otherwise>
									<a title="View Details" href="javascript:void(0);" onclick="showDetailsOverlay( '${rowNum}', '${row.get(0)}', '${row.get(1)}', '${row.get(2)}', '${row.get(3)}', '${row.get(4)}', '${row.get(5)}', '${row.get(6)}', '${row.get(7)}', '${row.get(8)}', '${row.get(9)}', '${row.get(10)}', '${row.get(11)}', '${row.get(12)}',  '${row.get(13)}',  '${row.get(14)}', '${row.get(15)}'); return false;" ><c:out value="${fn:substring(fieldValue, 0, 34)}..."/></a>
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
		<td><textarea id="ol_description" type="text" value=""  rows="3"  cols="50" style="width: 100%">></textarea></td>
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
	<button onclick="viewPdf(); return false;" id="btnViewPdf" name="button" value="viewPdf" >View PDF</button>	
	<button onclick="toggleOverlay(); return false;" id="btnCancel" name="button" value="cancel" >Cancel</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:template>