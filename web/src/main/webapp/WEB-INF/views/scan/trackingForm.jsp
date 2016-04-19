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

$( "#scan_start_date" ).datepicker();
$( "#scan_complete_date" ).datepicker();
$( "#scan_ia_start_date" ).datepicker();
$( "#scan_ia_complete_date" ).datepicker();
$( "#scan_ia_start_date2" ).datepicker();
$( "#scan_ia_complete_date2" ).datepicker();
$( "#metadata_complete" ).datepicker();
$( "#files_sent_to_orem" ).datepicker();
$( "#pull_date" ).datepicker();

$( "#scan_start_date" ).blur( validateDateData );
$( "#scan_complete_date" ).blur( validateDateData );
$( "#scan_ia_start_date" ).blur( validateDateData );
$( "#scan_ia_complete_date" ).blur( validateDateData );
$( "#scan_ia_start_date2" ).blur( validateDateData );
$( "#scan_ia_complete_date2" ).blur( validateDateData );
$( "#metadata_complete" ).blur( validateDateData );
$( "#files_sent_to_orem" ).blur( validateDateData );
$( "#pull_date" ).blur( validateDateData );
setTimeout(processBookState, 100);
displayBookNotFoundMsg();
});


//later remove validateFormData since it is in project.js...after browsers' cache has chance to refresh
function validateFormData(){
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
      
	  <%@include file="/WEB-INF/views/includes/scanMenu.html"%>
	  <h1 class="serif">${pageTitle}</h1>
	  
	    <c:if test="${mode=='read'}"><c:set var="isReadOnly" value="true"/></c:if>
	    <c:if test="${mode!='read'}"><c:set var="isReadOnly" value="false"/></c:if>
		<sf:form id="f1" class=""  onsubmit="return isvalidform();"  style="margin: 0 0 60px 0;"  name="scan-trackingform" method="post" action="" modelAttribute="book">
			<div class="trackingFormTop1">
			<table id="tnTable">
				 
				<tr><td>
					<table id="buttonsTable">
					<tr>
					<td></td>
					<c:choose>
					<c:when test="${mode=='update'}">
					<td><button id="saveAndClose" name="saveAndClose" value="saveAndClose" onclick="if(validateFormData()==false)return false;  updateUrl2('f1', 'trackingForm', 'saveAndClose' );">${messages['saveAndClose']}</button></td>
					<td><button id="save" name="save" value="save" onclick="if(validateFormData()==false)return false; updateUrl2('f1', 'trackingForm', 'save' );">${messages['save']}</button></td>
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
					<td >
					<c:choose> 
						<c:when test="${allTns != null}">
						<sf:select  id="tnListBox"  path=""  ondblclick="fetchBook('tnListBox', 'read'); return false;" multiple="multiple">
									<sf:options items="${allTns}" />
						</sf:select>
						<button onclick="fetchBook('tnListBox', 'read'); return false;">${messages['trackingForm.getSelected']}</button>
						</c:when>
						<c:otherwise>
						<!-- disabled  <button onclick="fetchAllTns('tnSelected', 'read'); return false;">${messages['trackingForm.findAllTn']}</button> -->
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
					<td><sf:input  path="secondaryIdentifier"  readonly="${isReadOnly}"   />
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
					<td>${messages['trackingForm.scanMetadataComplete']}</td>
					<td><sf:input id="metadata_complete"  path="scanMetadataComplete"  readonly="${isReadOnly}" title="${messages['trackingForm.scanMetadataCompleteHover']}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('metadata_complete'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['trackingForm.batchClass']}</td>
					<td><sf:input  path="batchClass" readonly="true"  /></td>
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
					<td>${messages['trackingForm.recordNumber']}</td>
					<td><sf:input  path="recordNumber" readonly="true"  /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.url']}</td>
					<td><sf:input  path="url"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.ocrSite']}</td>
					<td>
						<c:if test="${isReadOnly == true}"><sf:input  path="site"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="site" >
								<sf:option value=""/>
								<sf:options items="${ocrSites}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanningSiteNotes']}</td>
					<td><sf:textarea path="remarksFromScanCenter" readonly="${isReadOnly}" class="textarea3" /></td>
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
						<c:if test="${true}"><sf:input path="requestingLocation"  readonly="true"   /></c:if>
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
						<c:if test="${isReadOnly == true}"><sf:input id="scanned_by" path="scannedBy"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select id="scanned_by" path="scannedBy" >
								<sf:option value=""/>
								<sf:options items="${allScanSitesIncludingInactive}" />
							</sf:select>
						</c:if>
						<input id="scannedByHidden" value="${userLocation}"  type="hidden" />
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
					<td>${messages['trackingForm.scanImageAuditor2']}</td>
					<td><sf:input path="scanImageAuditor2" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanAuditDate2']}</td>
					<td><sf:input id="scan_ia_start_date2" path="scanIaStartDate2" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('scan_ia_start_date2'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['trackingForm.scanAuditCompleteDate2']}</td>
					<td><sf:input id="scan_ia_complete_date2" path="scanIaCompleteDate2" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('scan_ia_complete_date2'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					
					<tr><td class="rowSpace"></td></tr>
					<tr>
					<td>${messages['trackingForm.filesSentToOrem']}</td>
					<td><sf:input id="files_sent_to_orem" path="filesSentToOrem" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('files_sent_to_orem'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
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