<%@include file="/WEB-INF/views/includes/init.jsp"%>


<tags:template>
<jsp:body>


<!-- adding this version of jquery for calendar -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
<script>
//for firefox, when typing in tn and pressing enter key.  It should not submit and go to edit mode...
var doSubmit=true;
function setSubmit(){
	doSubmit=false;
}
function isvalidform(){
    return doSubmit;
}

function displayBookNotFoundMsg(){
	try{
		var tnSearch = document.getElementById('tnPasteSearch').value;
		var tn = document.getElementById('tnSelected').value;
		var secondaryIdentifier = document.getElementById('secondaryIdentifier').value;
	
	
		 if(tnSearch!=tn){
			 if(secondaryIdentifier==tnSearch){
				   alert("Book " + secondaryIdentifier + " was found with a matching secondary identifier.");
			 }else{

				   alert("Book " + tnSearch + " not in database.");
			 }
	 }
		

	}
	catch(err)
	{
	  //Handle errors here
	}
}

$(function() {
	$( "#date_released" ).datepicker();
	$( "#files_sent_to_orem" ).datepicker();
	$( "#ia_start_date" ).datepicker();
$( "#ia_complete_date" ).datepicker();
$( "#ocr_start_date" ).datepicker();
$( "#ocr_complete_date" ).datepicker();
$( "#metadata_complete" ).datepicker();
$( "#pdfreview_start_date" ).datepicker();
$( "#pdf_ready" ).datepicker();
$( "#pdfDownload_date" ).datepicker();
$( "#files_received_by_orem" ).datepicker();

$( "#date_released" ).blur( validateDateData );
$( "#files_sent_to_orem" ).blur( validateDateData );
$( "#ia_start_date" ).blur( validateDateData );
$( "#ia_complete_date" ).blur( validateDateData );
$( "#ocr_start_date" ).blur( validateDateData );
$( "#ocr_complete_date" ).blur( validateDateData );
$( "#metadata_complete" ).blur( validateDateData );
$( "#pdfreview_start_date" ).blur( validateDateData );
$( "#pdf_ready" ).blur( validateDateData );
$( "#pdfDownload_date" ).blur( validateDateData );
$( "#files_received_by_orem" ).blur( validateDateData );

setTimeout(processBookState, 100);
displayBookNotFoundMsg();
});


//later remove validateFormData since it is in project.js...after browsers' cache has chance to refresh
function validateFormData(){
	//temp return true since some books may have slipped through scan and in process can't update scanned_by
	return true;
	//if scan date is not null then scannedby must not be null - Jeri request
	var scanSite = $('#scanned_by').val();
	var scanStartDate = $('#scan_start_date').val();
	var scanEndDate = $('#scan_complete_date').val();
	
	if(scanStartDate != '' || scanEndDate != ''  ){
		if( scanSite == ''){
			alert("You must enter a 'Scanned by Site' value because Scan Date or Scan Complete Date have a value.");
			return false;
		}
	}
	
	return true;//valid data
};
</script>


<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">
    
	 	<%@include file="/WEB-INF/views/includes/processMenu.html"%>
	   	<h1 class="serif">${pageTitle}</h1>
	   	
	    <c:if test="${mode=='read'}"><c:set var="isReadOnly" value="true"/></c:if>
	    <c:if test="${mode!='read'}"><c:set var="isReadOnly" value="false"/></c:if>
		<sf:form id="f1" class=""  onsubmit="return isvalidform();" style="margin: 0 0 60px 0;"  name="process-trackingform" method="post" action="" modelAttribute="book">
			<div class="trackingFormTop1">
			<table id="tnTable">
				<tr><td>
					<table id="buttonsTable">
					<tr>
					<td></td>
					<c:choose>
					<c:when test="${mode=='update'}">	
					<td><button id="saveAndClose" name="saveAndClose" value="saveAndClose" onclick="updateUrl2('f1', 'trackingForm', 'saveAndClose' );">${messages['saveAndClose']}</button></td>
					<td><button id="save" name="save" value="save" onclick="updateUrl2('f1', 'trackingForm', 'save' );">${messages['save']}</button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="updateUrl2('f1', 'trackingForm', 'cancel' );">${messages['cancel']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && book.tn != ''}">
					<td><button id="update" name="update" value="update" onclick="updateUrl2('f1', 'trackingForm', 'update' );">${messages['update']}</button></td>
					</c:when>
					</c:choose>
					</tr>
					</table>
				</td></tr>
			
				<tr>
				<c:choose>
				<c:when test="${mode=='read'}">
					<td>${messages['trackingForm.titleNumber']} 
					<input id="tnPasteSearch" type="text" autocomplete="off" class="" value="${tn}" onsubmit="setSubmit(false); fetchBook2('tnPasteSearch', 'read'); return false;" onchange="setSubmit(false); fetchBook2('tnPasteSearch', 'read'); return false;"/>
					<button   onclick="fetchBook2('tnPasteSearch', 'read'); return false;" >${messages['trackingForm.findTn']}</button>
					</td>
					<td>
					<c:choose> 
						<c:when test="${allTns != null}">
						<sf:select  id="tnListBox"  path=""  ondblclick="fetchBook('tnListBox', 'read'); return false;" multiple="multiple">
									<sf:options items="${allTns}" />
						</sf:select>
						<button onclick="fetchBook('tnListBox', 'read'); return false;">${messages['trackingForm.getSelected']}</button>
						</c:when>
						<c:otherwise>
						<!-- disabled  <button onclick="fetchAllTns('tnSelected', 'read'); return false;">${messages['trackingForm.findAllTn']}</button>-->
						</c:otherwise>
					</c:choose>
					</td> 
					
				</c:when>
				</c:choose>
				</tr>
			</table>
			</div>
			<div class="trackingFormTop">
			<table id="titleAuthTable">
				<tr>
				<td>${messages['trackingForm.title']}</td>
				<td><sf:textarea path="title" readonly="true" class="textarea1" /></td>
				<td class="h-space1">${messages['trackingForm.author']}</td>
				<td><sf:textarea path="author" readonly="true" class="textarea1" /></td>
				</tr>
			</table>
			</div>


		 
			<table id="table-2cols">
				<tr>
				<td id="col1" style="vertical-align: top;">
					<table>
					<tr>
					<td class="labelColSize">${messages['trackingForm.tn']}</td>
					<td><sf:input  id="tnSelected" path="tn" readonly="true"  /></td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.tn-ext']}</td>
					<td><sf:input path="secondaryIdentifier"  readonly="${isReadOnly}"   />
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.problems']}</td>
					<td> 
						<c:if test="${book.tn != ''}">
							<c:set var="encodedTN" value="${book.tn}"/>
				 			<c:if test="${encodedTN.contains('&')}">
				 				<c:set var="encodedTN" value="${fn:replace(encodedTN,'&','&#37;26')}"/>
				 			</c:if>
						
							<c:if test="${isReadOnly==false}">
							<c:if test="${problemOpenList == null || problemOpenList.size() == 0}">
								<a href="../problems/problemsForm?update&doCreate&tn=${encodedTN}&pn=-1" target="_blank">${messages['newIssue']}</a>
							</c:if>
							</c:if>			
								<c:if test="${problemOpenList != null && problemOpenList.size() > 0}">
								<div  style="margin-top: 10px; margin-right: 20px; color: blue; border-color: red;  xfont-weight:bold; box-shadow: 0 0 0 red inset, 0 0 8px red;   outline: 0 none;" >
								</c:if>	
								<c:forEach var="prob" items="${problemOpenList}">
									<a href="../problems/problemsForm?read&tn=${encodedTN}&pn=${prob.get(0)}" target="_blank">${prob.get(0)}</a>
								</c:forEach>
								<c:if test="${problemOpenList != null && problemOpenList.size() > 0}">
								</div>
								</c:if>	
						</c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.problemsClosed']}</td>
					<td> 
						<c:if test="${problemClosedList == null || problemClosedList.size() == 0}">
							None
						</c:if>
						
						<c:set var="encodedTN" value="${book.tn}"/>
				 		<c:if test="${encodedTN.contains('&')}">
				 				<c:set var="encodedTN" value="${fn:replace(encodedTN,'&','&#37;26')}"/>
				 		</c:if>
				 		
						<c:forEach var="prob" items="${problemClosedList}">
							<a href="../problems/problemsForm?read&tn=${encodedTN}&pn=${prob.get(0)}" target="_blank">${prob.get(0)}</a>
						</c:forEach>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.owningInstitution']}</td>
					<td>
						<c:if test="${isReadOnly == true}"><sf:input  path="owningInstitution"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="owningInstitution" >
								<sf:option value=""/>
								<sf:options items="${allSites}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.metadataComplete']}</td>
					<td><sf:input id="metadata_complete"  path="metadataComplete"  readonly="true"  title="${messages['trackingForm.metadataCompleteHover']}" />
						<!-- <c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('metadata_complete'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if> -->
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.batchClass']}</td>
					<td><sf:input  path="batchClass"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.filename']}</td>
					<td><sf:input  path="filename"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.propertyRight']}</td>
					<td> 
						<c:if test="${isReadOnly == true}"><sf:input  path="propertyRight"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="propertyRight" >
								<sf:option value=""/>
								<sf:options items="${allPropertyRights}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.publicationType']}</td>
					<td> 
						<c:if test="${isReadOnly == true}"><sf:input  path="publicationType"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="publicationType" >
								<sf:option value=""/>
								<sf:options items="${allPublicationTypes}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.callNumber']}</td>
					<td><sf:input  path="callNumber" readonly="true"  /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.partnerLibCallNumber']}</td>
					<td><sf:input  path="partnerLibCallNumber"  readonly="true"   /></td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.numOfPages']}</td>
					<td><sf:input  path="numOfPages" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.language']}</td>
					<td>
						<c:if test="${isReadOnly == true}"><sf:input path="language" readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="language" >
								<sf:option value=""/>
								<sf:options items="${allLanguages}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.subject']}</td>
					<td><sf:input  id="subject" path="subject"  readonly="${isReadOnly}"  />
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.requestingLocation']}</td>
					<td>
						<c:if test="${true}"><sf:input  path="requestingLocation"  readonly="${true}"   /></c:if>
						<c:if test="${false}">
							<sf:select path="requestingLocation" >
								<sf:option value=""/>
								<sf:options items="${allSites}" />
							</sf:select>
						</c:if>
						</td>
					</tr>

					<tr>
					<td>${messages['trackingForm.scannedBy']}</td>
					<td>
						<c:if test="${true}"><sf:input  path="scannedBy"  readonly="${true}"   /></c:if>
						<c:if test="${false}">
							<sf:select path="scannedBy" >
								<sf:option value=""/>
								<sf:options items="${allScanSitesIncludingInactive}" />
							</sf:select>
						</c:if>
						</td>
					</tr>

					<tr>
					<td>${messages['trackingForm.scanNumOfPages']}</td>
					<td><sf:input  path="scanNumOfPages" readonly="true" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.filesSentToOrem']}</td>
					<td><sf:input  id="files_sent_to_orem" path="filesSentToOrem" readonly="true" />
					</td>
					</tr>
					
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.location']}</td>
					<td><sf:input  path="location" readonly="true" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.filesReceivedByOrem']}</td>
					<td><sf:input  id="files_received_by_orem" path="filesReceivedByOrem" readonly="true" />
						<!-- <c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('files_received_by_orem'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if> -->
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.scanningSiteNotes']}</td>
					<td><sf:textarea path="remarksFromScanCenter" readonly="true" class="textarea3" /></td>
					</tr>
					</table>
				</td>
				<td id="col2" class="colPadding" valign="top">
					<table>
					<tr>
					<td>${messages['trackingForm.titleCheckBy']}</td>
					<td><sf:input path="imageAudit" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.iaStartDate']}</td>
					<td><sf:input id="ia_start_date" path="iaStartDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('ia_start_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.iaCompleteDate']}</td>
					<td><sf:input id="ia_complete_date" path="iaCompleteDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('ia_complete_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.ocrBy']}</td>
					<td><sf:input path="ocrBy" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.ocrStartDate']}</td>
					<td><sf:input id="ocr_start_date" path="ocrStartDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('ocr_start_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.ocrCompleteDate']}</td>
					<td><sf:input id="ocr_complete_date" path="ocrCompleteDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('ocr_complete_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.pdfDownloadBy']}</td>
					<td><sf:input path="pdfDownloadBy" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pdfDownloadDate']}</td>
					<td><sf:input id="pdfDownload_date" path="pdfDownloadDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('pdfDownload_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.pdfreviewBy']}</td>
					<td><sf:input path="pdfreviewBy" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pdfreviewStartDate']}</td>
					<td><sf:input id="pdfreview_start_date" path="pdfreviewStartDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('pdfreview_start_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pdfReady']}</td>
					<td><sf:input id="pdf_ready" path="pdfReady" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('pdf_ready'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.tiffOremDriveName']}</td>
					<td><sf:input id="tiff_orem_drive_name" path="tiffOremDriveName" readonly="true" />
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.dateReleased']}</td>
					<td><sf:input id="date_released" path="dateReleased" readonly="true" />
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.remarksAboutBook']}</td>
					<td><sf:textarea path="remarksAboutBook" readonly="${isReadOnly}" class="textarea3" /></td>
					</tr>
					</table>
				</td>
			
				</tr>
			</table>
				  
				
				
			<table id="buttonsTable">
			<tr>
			<td></td>
			<c:choose>
			<c:when test="${mode=='update'}">
			<td><button id="saveAndClose" name="saveAndClose" value="saveAndClose" onclick="if(validateFormData()==false)return false; updateUrl2('f1', 'trackingForm', 'saveAndClose' );">${messages['saveAndClose']}</button></td>
			<td><button id="save" name="save" value="save" onclick="if(validateFormData()==false)return false; updateUrl2('f1', 'trackingForm', 'save' );">${messages['save']}</button></td>
			<td><button id="cancel" name="cancel" value="cancel" onclick="updateUrl2('f1', 'trackingForm', 'cancel' );">${messages['cancel']}</button></td>
			</c:when>
			<c:when test="${mode=='read' && book.tn != ''}">
			<td><button id="update" name="update" value="update" onclick="updateUrl2('f1', 'trackingForm', 'update' );">${messages['update']}</button></td>
			</c:when>
			</c:choose>
			</tr>
			</table>
			
		</sf:form>
	  		
		      
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>