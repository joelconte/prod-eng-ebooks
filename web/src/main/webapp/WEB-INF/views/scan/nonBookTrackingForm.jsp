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
		var dnSearch = document.getElementById('tnPasteSearch').value;
		var dn = document.getElementById('tnSelected').value;
		 
		if(dnSearch!=dn){
			alert("Document " + dnSearch + " not in database.");
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
 
$( "#scan_start_date" ).blur( validateDateData );
$( "#scan_complete_date" ).blur( validateDateData );
$( "#scan_ia_start_date" ).blur( validateDateData );
$( "#scan_ia_complete_date" ).blur( validateDateData );
 

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

//prompt for DN and set in textbox and then can be handled like an update after that

function fetchNonBook(selectId, mode){

	var sel= document.getElementById(selectId);
	var dn = sel.options[sel.selectedIndex].text;
	
	//replace & with %26
	if(dn.indexOf("&") != -1){
		dn = dn.replace("&", "%26");
	}
	
	var url = "nonBookTrackingForm?" + mode + "&dn=" + dn; //mode read,update,create
	window.location.href=url;
	 
}
function fetchNonBook2(textId, mode){
 
	var sel= document.getElementById(textId);
	var dn = sel.value;
	
	//replace & with %26
	if(dn.indexOf("&") != -1){
		dn = dn.replace("&", "%26");
	}
	var url = "nonBookTrackingForm?" + mode+ "&dn=" + dn; //mode read,update,create
	window.location.href=url;
}

function getNewDn(){
	var dn = window.prompt("Please enter unique identifier for this document.");
	if(dn==null)
		return "no new dn";
	
	var elm = document.getElementById('tnPasteSearch');
	elm.value = dn;
		
	//clear existing dn from param
	var url = "nonBookTrackingForm?" + "read" + "&dn=" + dn; //mode read,update,create
	window.location.href=url;
		 
}

//todo hackec update url3 to delete whcn cancel if new book, but maybe a better way.
function updateUrl3(formId, page, mode){
	//if in middle of adding new nonbook, then need to delete dn since that is how we added it, just an empty dn...
	var newParm = location.href.indexOf('?new&');//will be non-nondefined
	if (newParm!=null && newParm!=-1){
		//todo delete dn here ..later after complaints
		mode = "newAndCancel"; 
		page = page + "NewAndCancel";//hack since gettign conflicting requestmappings
	}else{
		//mode = "delete";//already passed in
	}
	
	//var el = document.getElementById(selectId);
	//var tn = el.options[el.selectedIndex].text;//select box tn
	var f = document.getElementById(formId); //form
	var url = page + "?" + mode;//tn already in form data    + "&tn=" + tn; //mode read,update,create
	if( getUrlParm("returnTo")!= null &&  getUrlParm("returnTo")!= "")
		url = url + "&returnTo=" + getUrlParm("returnTo");
	
	//add dn, for some reason not getting picked up
	var sel= document.getElementById('tnSelected');
	var dn = sel.value;

 //replace & with %26
	if(dn.indexOf("&") != -1){
		dn = tn.replace("&", "%26");
	}

	/*if(dn != "")
		var url = url+ "&dn=" + dn;				
	 
		*/
	 
	 f.action = url;
}
 
	
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
					<td><button id="saveAndClose" name="saveAndClose" value="saveAndClose" onclick="if(validateFormData()==false)return false;  updateUrl2('f1', 'nonBookTrackingForm', 'saveAndClose' );">${messages['saveAndClose']}</button></td>
					<td><button id="save" name="save" value="save" onclick="if(validateFormData()==false)return false; updateUrl2('f1', 'nonBookTrackingForm', 'save' );">${messages['save']}</button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="updateUrl3('f1', 'nonBookTrackingForm', 'cancel' );">${messages['cancel']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && book.dn == ''}">
					<td><button id="new" name="new" value="new" onclick="if(getNewDn()!='no new dn') fetchNonBook2('tnPasteSearch', 'new'); return false;">${messages['new']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && book.dn != ''}">
					<td><button id="new" name="new" value="new" onclick="getNewDn();  fetchNonBook2('tnPasteSearch', 'new'); return false;">${messages['new']}</button></td>
					<td><button id="update" name="update" value="update" onclick="updateUrl2('f1', 'nonBookTrackingForm', 'update' );">${messages['update']}</button></td>
					<td><button id="delete" name="delete" value="delete" onclick="if(doubleCheckDelete()==false)return false; updateUrl3('f1', 'nonBookTrackingForm', 'delete' );">${messages['delete']}</button></td>
					</c:when>
					</c:choose>
					</tr>
					</table>
				</td></tr>
			
				<tr>
				<c:choose>
				<c:when test="${mode=='read'}">
					<td>${messages['nonBookTrackingForm.titleNumber']} 
					<input autofocus id="tnPasteSearch" type="text" autocomplete="off" class=""  value="${dn}" onsubmit="setSubmit(false); fetchNonBook2('tnPasteSearch', 'read'); return false;" onchange="setSubmit(false); fetchNonBook2('tnPasteSearch', 'read'); return false;"/>
					<button   onclick="fetchNonBook2('tnPasteSearch', 'read'); return false;" >${messages['trackingForm.findTn']}</button>
					</td>
					<td >
					<c:choose> 
						<c:when test="${allDns != null}">
						<sf:select  id="tnListBox"  path=""  ondblclick="fetchNonBook('tnListBox', 'read'); return false;" multiple="multiple">
									<sf:options items="${allDns}" />
						</sf:select>
						<button onclick="fetchNonBook('tnListBox', 'read'); return false;">${messages['trackingForm.getSelected']}</button>
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
				<td><sf:textarea path="title" readonly="${isReadOnly}"  class="textarea1" /></td>
				<td class="h-space1">${messages['trackingForm.author']}</td>
				<td><sf:textarea path="author" readonly="${isReadOnly}"   class="textarea1" /></td>
				</tr>
			</table>
			</div>


			 
			<table id="table-2cols">
				<tr>
				<td id="col1" style="vertical-align: top;">
					<table>
					<tr>
					<td class="labelColSize">${messages['trackingForm.dn']}</td>
					<td><sf:input  id="tnSelected" path="dn" readonly="true"  /></td>
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
					<td>${messages['trackingForm.filename']}</td>
					<td><sf:input  path="filename"  readonly="${isReadOnly}"   /></td>
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
					<td class="rowSpace"></td></tr>
					
					<tr>
					<td>${messages['trackingForm.scanningSiteNotes']}</td>
					<td><sf:textarea path="remarksFromScanCenter" readonly="${isReadOnly}" class="textarea3" /></td>
					</tr>
					</table>
				</td>
				<td id="col2" class="colPadding" style="vertical-align: top;">
					<table>
					
									 
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
					<td><sf:input id="scan_machine" path="scanMachineId" readonly="${isReadOnly}" /></td>
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
					<td><sf:input id="scan_num_of_pages" path="scanNumOfPages" readonly="${isReadOnly}" /></td>
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
					</table>
				</td>
				
					 
			
				</tr>
			</table>
				  
				
				
			<table id="buttonsTable">
			<tr>
			<td></td>
	    		<c:choose>
					<c:when test="${mode=='update'}">
					<td><button id="saveAndClose" name="saveAndClose" value="saveAndClose" onclick="if(validateFormData()==false)return false;  updateUrl2('f1', 'nonBookTrackingForm', 'saveAndClose' );">${messages['saveAndClose']}</button></td>
					<td><button id="save" name="save" value="save" onclick="if(validateFormData()==false)return false; updateUrl2('f1', 'nonBookTrackingForm', 'save' );">${messages['save']}</button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="updateUrl3('f1', 'nonBookTrackingForm', 'cancel' );">${messages['cancel']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && book.dn == ''}">
					<td><button id="new" name="new" value="new" onclick="if(getNewDn()!='no new dn') fetchNonBook2('tnPasteSearch', 'new'); return false;">${messages['new']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && book.dn != ''}">
					<td><button id="new" name="new" value="new" onclick="getNewDn();  fetchNonBook2('tnPasteSearch', 'new'); return false;">${messages['new']}</button></td>
					<td><button id="update" name="update" value="update" onclick="updateUrl2('f1', 'nonBookTrackingForm', 'update' );">${messages['update']}</button></td>
					<td><button id="delete" name="delete" value="delete" onclick="if(doubleCheckDelete()==false)return false; updateUrl3('f1', 'nonBookTrackingForm', 'delete' );">${messages['delete']}</button></td>
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