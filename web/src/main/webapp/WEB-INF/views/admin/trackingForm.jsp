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
	displayBookNotFoundMsg();
$( "#scan_complete_date" ).datepicker();	 
$( "#scan_ia_complete_date" ).datepicker();
$( "#scan_start_date" ).datepicker();
$( "#scan_ia_start_date" ).datepicker();
$( "#files_sent_to_orem" ).datepicker();
$( "#ia_start_date" ).datepicker();
$( "#ia_complete_date" ).datepicker();
$( "#ocr_start_date" ).datepicker();
$( "#ocr_complete_date" ).datepicker();
$( "#pdfreview_start_date" ).datepicker();
$( "#pdf_ready" ).datepicker();
$( "#pdfDownload_date" ).datepicker();
$( "#metadata_complete" ).datepicker();
$( "#date_released" ).datepicker();
$( "#date_loaded" ).datepicker();
$( "#pdf_sent_to_load" ).datepicker();
$( "#files_received_by_orem" ).datepicker();
$( "#pdf_orem_archived_date" ).datepicker();
$( "#pdf_copy2_archived_date" ).datepicker();
$( "#tiff_orem_archived_date" ).datepicker();
$( "#tiff_copy2_archived_date" ).datepicker();
$( "#date_original" ).datepicker();
$( "#date_republished" ).datepicker();
$( "#pull_date" ).datepicker();

$( "#scan_complete_date" ).blur( validateDateData );	 
$( "#scan_ia_complete_date" ).blur( validateDateData );
$( "#scan_start_date" ).blur( validateDateData );
$( "#scan_ia_start_date" ).blur( validateDateData );
$( "#files_sent_to_orem" ).blur( validateDateData );
$( "#ia_start_date" ).blur( validateDateData );
$( "#ia_complete_date" ).blur( validateDateData );
$( "#ocr_start_date" ).blur( validateDateData );
$( "#ocr_complete_date" ).blur( validateDateData );
$( "#pdfreview_start_date" ).blur( validateDateData );
$( "#pdf_ready" ).blur( validateDateData );
$( "#pdfDownload_date" ).blur( validateDateData );
$( "#metadata_complete" ).blur( validateDateData );
$( "#date_released" ).blur( validateDateData );
$( "#date_loaded" ).blur( validateDateData );
$( "#pdf_sent_to_load" ).blur( validateDateData );
$( "#files_received_by_orem" ).blur( validateDateData );
$( "#pdf_orem_archived_date" ).blur( validateDateData );
$( "#pdf_copy2_archived_date" ).blur( validateDateData );
$( "#tiff_orem_archived_date" ).blur( validateDateData );
$( "#tiff_copy2_archived_date" ).blur( validateDateData );
$( "#date_original" ).blur( validateDateData );
$( "#date_republished" ).blur( validateDateData );
$( "#pull_date" ).blur( validateDateData );
});


 
</script>


<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">

	  <%@include file="/WEB-INF/views/includes/adminMenu.html"%>
      <h1 class="serif">${pageTitle}</h1>
	  
	    <c:if test="${mode=='read'}"><c:set var="isReadOnly" value="true"/></c:if>
	    <c:if test="${mode!='read'}"><c:set var="isReadOnly" value="false"/></c:if>
		<sf:form id="f1" style="margin: 0 0 60px 0;" class="" name="admin-trackingform"  onsubmit="return isvalidform();"  method="post" action="" modelAttribute="book">
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
					<td><button id="delete" name="delete" value="delete" onclick="if(doubleCheckDelete()==false)return false; updateUrl2('f1', 'trackingForm', 'delete' );">${messages['delete']}</button></td>
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
						<!-- disabled  <button onclick="fetchAllTns('tnSelected', 'read'); return false;">${messages['trackingForm.findAllTn']}</button>  -->
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
				<td><sf:textarea path="title"  readonly="${isReadOnly}"  class="textarea1" /></td>
				<td class="h-space1">${messages['trackingForm.author']}</td>
				<td><sf:textarea path="author"  readonly="${isReadOnly}"  class="textarea1" /></td>
				</tr>
			</table>
			</div>


			<table id="table-2cols">
				<tr>
				<td id="col1">
					<table>
					<tr>
					<td class="labelColSize">${messages['trackingForm.tn']}</td>
					<td><sf:input  id="tnSelected" path="tn"  readonly="${isReadOnly}"   />
						<input type="hidden" value="${book.tn}"  name="tnOriginal" id="tnOriginal" /></td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.tn-ext']}</td>
					<td><sf:input  path="secondaryIdentifier"  readonly="${isReadOnly}"   />
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.problems']}</td>
					<td> 
						<c:if test="${book.tn != ''}">
							<c:if test="${isReadOnly==false}">
							<c:if test="${problemOpenList == null || problemOpenList.size() == 0}">
								<a href="../problems/problemsForm?update&doCreate&tn=${book.tn}&pn=-1" target="_blank">${messages['newIssue']}</a>
							</c:if>
							</c:if>
								<c:if test="${problemOpenList != null && problemOpenList.size() > 0}">
								<div  style="margin-top: 10px; margin-right: 20px; color: blue; border-color: red;  xfont-weight:bold; box-shadow: 0 0 0 red inset, 0 0 8px red;   outline: 0 none;" >
								</c:if>	
								<c:forEach var="prob" items="${problemOpenList}">
									<a href="../problems/problemsForm?read&tn=${prob.get(1)}&pn=${prob.get(0)}" target="_blank">${prob.get(0)}</a>
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
						<c:forEach var="prob" items="${problemClosedList}">
							<a href="../problems/problemsForm?read&tn=${prob.get(1)}&pn=${prob.get(0)}" target="_blank">${prob.get(0)}</a>
						</c:forEach>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.fhc_title']}</td>
					<td><sf:input  path="fhcTitle"  readonly="${isReadOnly}"   />
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.fhc_tn']}</td>
					<td><sf:input  path="fhcTn"  readonly="${isReadOnly}"   />
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.oclcNumber']}</td>
					<td><sf:input  path="oclcNumber"  readonly="${isReadOnly}"   />
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.isbnIssn']}</td>
					<td><sf:input  path="isbnIssn"  readonly="${isReadOnly}"   />
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
					<td class="labelColSize">${messages['trackingForm.ocrSite']}</td>
					<td>
						<c:if test="${isReadOnly == true}"><sf:input  path="site"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="site" >
								<sf:option value=""/>
								<sf:options items="${allSites}" />
							</sf:select>
						</c:if>
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
					<td><sf:input  path="callNumber"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.partnerLibCallNumber']}</td>
					<td><sf:input  path="partnerLibCallNumber"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.recordNumber']}</td>
					<td><sf:input  path="recordNumber"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.metadataComplete']}</td>
					<td><sf:input id="metadata_complete" path="metadataComplete"  readonly="${isReadOnly}" title="${messages['trackingForm.metadataCompleteHover']}"  />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('metadata_complete'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.url']}</td>
					<td><sf:input  path="url"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.compressionCode']}</td>
					<td><sf:input  path="compressionCode"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pid']}</td>
					<td><sf:input  path="pid"  readonly="${isReadOnly}"   /></td>
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
					<td>${messages['trackingForm.filmno']}</td>
					<td><sf:input  id="filmno" path="filmno"  readonly="${isReadOnly}"  />
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pagesPhysicalDescription']}</td>
					<td><sf:input  id="pagesPhysicalDescription" path="pagesPhysicalDescription"  readonly="${isReadOnly}"  />
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.summary']}</td>
					<td><sf:input  id="summary" path="summary"  readonly="${isReadOnly}"  />
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.dgsno']}</td>
					<td><sf:input  id="dgsno" path="dgsno"  readonly="${isReadOnly}"  />
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.dateOriginal']}</td>
					<td><sf:input  id="date_original" path="dateOriginal"  readonly="${isReadOnly}"  />
					<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('date_original'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.publisherOriginal']}</td>
					<td><sf:input  id="publisher_original" path="publisherOriginal"  readonly="${isReadOnly}"  />
					</td>
					</tr>
					
					
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.dnp']}</td>
					<td><sf:input  path="dnp" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.tnChangeHistory']}</td>
					<td><sf:textarea path="tnChangeHistory" readonly="${isReadOnly}" class="textarea3" /></td>
					</tr>
					
					<tr><td class="rowSpace"></td></tr>
				 	<tr><td class="rowSpace"></td></tr>
				 	
					<tr>
					<td>${messages['trackingForm.dateReleased']}</td>
					<td><sf:input id="date_released" path="dateReleased"  readonly="${isReadOnly}"  />
					<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('date_released'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.dateLoaded']}</td>
					<td><sf:input id="date_loaded" path="dateLoaded"  readonly="${isReadOnly}"  />
					<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('date_loaded'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.loadedBy']}</td>
					<td><sf:input  path="loadedBy"  readonly="${isReadOnly}"  /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.dateRepublished']}</td>
					<td><sf:input id="date_republished" path="dateRepublished"  readonly="${isReadOnly}"  />
					<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('date_republished'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.collection']}</td>
					<td><sf:input path="collection"  readonly="${isReadOnly}"  /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pdfSentToLoad']}</td>
					<td><sf:input id="pdf_sent_to_load" path="pdfSentToLoad"  readonly="${isReadOnly}"  />
					<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('pdf_sent_to_load'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pagesOnline']}</td>
					<td><sf:input path="pagesOnline"  readonly="${isReadOnly}"  /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanningSiteNotes']}</td>
					<td><sf:textarea path="remarksFromScanCenter"  readonly="${isReadOnly}"  class="textarea3" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.remarksAboutBook']}</td>
					<td><sf:textarea path="remarksAboutBook" readonly="${isReadOnly}" class="textarea3" /></td>
					</tr>
					
					</table>
				</td>
				
				
				<td id="col2" class="colPadding" style="vertical-align: top;">
					<table>
					<tr>
					<td>${messages['trackingForm.pullDate']}</td>
					<td><sf:input id="pull_date" path="pullDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('pull_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.requestingLocation']}</td>
					<td>
						<c:if test="${isReadOnly == true}"><sf:input  path="requestingLocation"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
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
						<c:if test="${isReadOnly == true}"><sf:input  path="scannedBy"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="scannedBy" >
								<sf:option value=""/>
								<sf:options items="${allSites}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanOperator']}</td>
					<td><sf:input path="scanOperator" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanMachineId']}</td>
					<td><sf:input path="scanMachineId" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanDate']}</td>
					<td><sf:input id="scan_start_date" path="scanStartDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('scan_start_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanCompleteDate']}</td>
					<td><sf:input id="scan_complete_date" path="scanCompleteDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('scan_complete_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.scanNumOfPages']}</td>
					<td><sf:input  path="scanNumOfPages" readonly="${isReadOnly}" /></td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.scanImageAuditor']}</td>
					<td><sf:input path="scanImageAuditor" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanAuditDate']}</td>
					<td><sf:input id="scan_ia_start_date" path="scanIaStartDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('scan_ia_start_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanAuditCompleteDate']}</td>
					<td><sf:input id="scan_ia_complete_date" path="scanIaCompleteDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('scan_ia_complete_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.filesSentToOrem']}</td>
					<td><sf:input id="files_sent_to_orem" path="filesSentToOrem" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('files_sent_to_orem'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.filesReceivedByOrem']}</td>
					<td><sf:input  id="files_received_by_orem" path="filesReceivedByOrem"  readonly="${isReadOnly}"  />
					<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('files_received_by_orem'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.location']}</td>
					<td><sf:input  path="location"  readonly="${isReadOnly}"  /></td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
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
					<tr>
					<td>${messages['trackingForm.numOfPages']}</td>
					<td><sf:input  path="numOfPages" readonly="${isReadOnly}" /></td>
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
					<tr><td class="rowSpace"></td></tr>
					
					
					<tr>
					<td>${messages['trackingForm.pdfOremArchivedDate']}</td>
					<td><sf:input id="pdf_orem_archived_date" path="pdfOremArchivedDate"  readonly="${isReadOnly}"  />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('pdf_orem_archived_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pdfOremDriveSerialNumber']}</td>
					<td><sf:input id="pdf_orem_drive_serial_number" path="pdfOremDriveSerialNumber"  readonly="${isReadOnly}"  /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pdfOremDriveName']}</td>
					<td><sf:input id="pdf_orem_drive_name" path="pdfOremDriveName"  readonly="${isReadOnly}"  /></td>
					</tr>
					
					<tr><td class="rowSpace"></td></tr>
					
					
					<tr>
					<td>${messages['trackingForm.pdfCopy2ArchivedDate']}</td>
					<td><sf:input id="pdf_copy2_archived_date" path="pdfCopy2ArchivedDate"  readonly="${isReadOnly}"  />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('pdf_copy2_archived_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pdfCopy2DriveSerialNumber']}</td>
					<td><sf:input id="pdf_copy2_drive_serial_number" path="pdfCopy2DriveSerialNumber"  readonly="${isReadOnly}"  /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.pdfCopy2DriveName']}</td>
					<td><sf:input id="pdf_copy2_drive_name" path="pdfCopy2DriveName"  readonly="${isReadOnly}"  /></td>
					</tr>
					
					<tr><td class="rowSpace"></td></tr>
					
					<tr>
					<td>${messages['trackingForm.tiffOremArchivedDate']}</td>
					<td><sf:input id="tiff_orem_archived_date" path="tiffOremArchivedDate"  readonly="${isReadOnly}"  />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('tiff_orem_archived_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.tiffOremDriveSerialNumber']}</td>
					<td><sf:input id="tiff_orem_drive_serial_number" path="tiffOremDriveSerialNumber"  readonly="${isReadOnly}"  /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.tiffOremDriveName']}</td>
					<td><sf:input id="tiff_orem_drive_name" path="tiffOremDriveName"  readonly="${isReadOnly}"  /></td>
					</tr>
					
					<tr><td class="rowSpace"></td></tr>
					
				 	<tr>
					<td>${messages['trackingForm.tiffCopy2ArchivedDate']}</td>
					<td><sf:input id="tiff_copy2_archived_date" path="tiffCopy2ArchivedDate"  readonly="${isReadOnly}"  />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('tiff_copy2_archived_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.tiffCopy2DriveSerialNumber']}</td>
					<td><sf:input id="tiff_copy2_drive_serial_number" path="tiffCopy2DriveSerialNumber"  readonly="${isReadOnly}"  /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.tiffCopy2DriveName']}</td>
					<td><sf:input id="tiff_copy2_drive_name" path="tiffCopy2DriveName"  readonly="${isReadOnly}"  /></td>
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
		 
		</sf:form>
	  		
		      
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>