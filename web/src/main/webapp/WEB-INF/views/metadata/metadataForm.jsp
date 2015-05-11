<%@include file="/WEB-INF/views/includes/init.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sw" uri="http://code.lds.org/security/web" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

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
	if(tnSearch!=tn)
		alert("Book " + tnSearch + " not in database.");
	
	 }
	catch(err)
	  {
	  //Handle errors here
	  }
}


$(function() {
	displayBookNotFoundMsg();
$( "#date_original" ).datepicker();

$( "#date_original" ).blur( validateDateData );
});
</script>
 


<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">

	  <%@include file="/WEB-INF/views/includes/metadataMenu.html"%>
      <h1 class="serif">${pageTitle}</h1>
	  
	    <c:if test="${mode=='read'}"><c:set var="isReadOnly" value="true"/></c:if>
	    <c:if test="${mode!='read'}"><c:set var="isReadOnly" value="false"/></c:if>
		<sf:form id="f1" style="margin: 0 0 60px 0;" class="" onsubmit="return isvalidform();"  name="admin-metadataform" method="post" action="" modelAttribute="book">
			<div class="trackingFormTop1">
			<table id="tnTable">
				<tr><td>
					<table id="buttonsTable">
					<tr>
					<td></td>
					<c:choose>
					<c:when test="${mode=='update'}">
					<td><sf:button id="save" name="save" value="save" onclick="updateUrl2('f1', 'metadataForm', 'save' );">${messages['save']}</sf:button></td>
					<td><sf:button id="cancel" name="cancel" value="cancel" onclick="updateUrl2('f1', 'metadataForm', 'cancel' );">${messages['cancel']}</sf:button></td>
					</c:when>
					<c:when test="${mode=='read' && book.titleno != ''}">
					<td><sf:button id="update" name="update" value="update" onclick="updateUrl2('f1', 'metadataForm', 'update' );">${messages['update']}</sf:button></td>
					</c:when>
					</c:choose>
					</tr>
					</table>
				</td></tr>
			
				
				<tr>
				<c:choose>
				<c:when test="${mode=='read'}">
					<td>${messages['trackingForm.titleNumber']} 
					<input id="tnPasteSearch" type="text" autocomplete="off" class="" value="${titleno}" onsubmit="setSubmit(false); fetchBook2Metadata('tnPasteSearch', 'read'); return false;" onchange="setSubmit(false); fetchBook2Metadata('tnPasteSearch', 'read'); return false;"/>
					<button   onclick="fetchBook2Metadata('tnPasteSearch', 'read'); return false;" >${messages['trackingForm.findTn']}</button>
					</td>
					<td>
					<c:if test="${allTns != null}">
					<sf:select  id="tnListBox"  path=""  ondblclick="fetchBookMetadata('tnListBox', 'read'); return false;" multiple="multiple">
								<sf:options items="${allTns}" />
					</sf:select>
					</c:if>
					<button onclick="fetchAllTnsMetadata('tnSelected', 'read'); return false;">${messages['metadataForm.findAllTn']}</button> 
					 
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
					<td><sf:input  id="tnSelected" path="titleno"  readonly="${isReadOnly}"   />
						<input type="hidden" value="${book.titleno}"  name="tnOriginal" id="tnOriginal" /></td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['metadataForm.scanning_location']}</td>
					<td>
						<c:if test="${isReadOnly == true}"><sf:input path="scanningLocation" readonly="${isReadOnly}" /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="scanningLocation" >
								<sf:option value=""/>
								<sf:options items="${allSites}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['metadataForm.subject']}</td>
					<td><sf:input  path="subject"  readonly="${isReadOnly}"   /></td>
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
					<tr>
					<td>${messages['trackingForm.callNumber']}</td>
					<td><sf:input  path="callno"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['trackingForm.partnerLibCallNumber']}</td>
					<td><sf:input  path="partnerLibCallno"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['metadataForm.recordno']}</td>
					<td><sf:input  path="recordNumber" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['metadataForm.filmNumber']}</td>
					<td><sf:input  path="filmno"  readonly="${isReadOnly}"   /></td>
					</tr>
					<tr>
					<td>${messages['metadataForm.physicalPages']}</td>
					<td><sf:input  path="pages" readonly="${isReadOnly}" /></td>
					</tr>
					<tr>
					<td>${messages['metadataForm.summary']}</td>
					<td><sf:textarea path="summary"  readonly="${isReadOnly}"  class="textarea3" /></td>
					</tr> 
					
					</table>
				</td>
				<td id="col2" class="colPadding" style="vertical-align: top;">
					<table>
					 
					<tr>
					<td>${messages['metadataForm.dgsno']}</td>
					<td><sf:input  path="dgsno" readonly="${isReadOnly}" /></td>
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
					<td>${messages['metadataForm.dateOriginal']}</td>
					<td><sf:input  id="date_original" path="dateOriginal"  readonly="${isReadOnly}"  />
					<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('date_original'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td>${messages['metadataForm.publisherOriginal']}</td>
					<td><sf:input  id="publisher_original" path="publisherOriginal"  readonly="${isReadOnly}"  />
					</td>
					</tr>
					
					<tr><td class="rowSpace"></td></tr>
				 	<tr><td class="rowSpace"></td></tr>
 
 
				 	
					<tr>
					<td>${messages['metadataForm.dateAdded']}</td>
					<td><sf:input  id="date_added" path="dateAdded"  readonly="true"  />
					</td>
					</tr>
					<tr>
					<td>${messages['metadataForm.metadataAdder']}</td>
					<td><sf:input  id="metadata_adder" path="metadataAdder"  readonly="true"  />
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
 
					<tr>
					<td>${messages['metadataForm.checkComplete']}</td>
					<td><sf:input  id="check_complete" path="checkComplete"  readonly="true"  />
					</td>
					</tr>
					<tr>
					<td>${messages['metadataForm.checker']}</td>
					<td><sf:input  id="checker" path="checker"  readonly="true"  />
					</td>
					</tr>
					<tr><td class="rowSpace"></td></tr>
 
					<tr>
					<td>${messages['metadataForm.sentToScan']}</td>
					<td><sf:input  id="sent_to_scan" path="sentToScan"  readonly="true"  />
					</td>
					</tr>
					<tr>
					<td>${messages['metadataForm.sender']}</td>
					<td><sf:input  id="sender" path="sender"  readonly="true"  />
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
			<td><sf:button id="save" name="save" value="save" onclick="updateUrl2('f1', 'metadataForm', 'save' );">${messages['save']}</sf:button></td>
			<td><sf:button id="cancel" name="cancel" value="cancel" onclick="updateUrl2('f1', 'metadataForm', 'cancel' );">${messages['cancel']}</sf:button></td>
			</c:when>
			<c:when test="${mode=='read' && book.titleno != ''}">
			<td><sf:button id="update" name="update" value="update" onclick="updateUrl2('f1', 'metadataForm', 'update' );">${messages['update']}</sf:button></td>
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