<%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
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
	sel= document.getElementById("dupeList");
	var dupTns = sel.value;
 
	if(pastedData != "" && dupTns != ""){
		toggleOverlay(); //show overlay when first open page since it contained dupe MD and need to confirm to do updates
		
		var updateit = confirm("The following TNs already exist in the Book table.  \n" + dupTns + "\nThey will not be updated with this pasted metadata.  \nOnly non-duplicate TNs will be inserted.\n\n  Click 'OK' and then 'Save' to continue.\n");
		if(updateit == true){
			var f = document.getElementById("fPasteData"); //form
			
			var url = f.action + "?doUpdates";//append doUpdates to flag 
			f.action = url;
		}
	} 
}
  
</script> 


<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/metadataMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
	   
		<sf:form id="f1" class="" name="f1" method="post" action="${buttonsAction}" modelAttribute="book">
			<input type="hidden" id="dupeList" value="${dupeTnsInfo}"/>
			<table id="buttonsTable">
			<tr>
				<td></td><!-- space for checkboxes if I remember correctly -->
				<c:forEach var="b" items="${buttons}">
					<c:if test='${b.get(0).equals("overlayButton") == true}'>
						<td><button id="${b.get(0)}" name="overlayButton" value="${b.get(0)}" onclick="toggleOverlay(); return false;">${b.get(1)}</button></td>
					</c:if>
					<c:if test='${b.get(0).equals("overlayButton") == false}'>
						<td><button id="${b.get(0)}" name="button" value="${b.get(0)}"    <c:if test="${b.size()>2}"> onclick="if(${b.get(2)}()==false)return false;"</c:if> >${b.get(1)}</button></td>
					</c:if>
				</c:forEach>
			</tr>
			</table>
			
			 <input style="margin: 33px 0px -33px 0px;   " class="sorttable_nosort" type="checkbox" id="checkAll" onclick="selectAllCheckboxes('checkAll');" /> <!-- hack to get checkbox to not resort when clicked, move outside of table column --> 
			 <table id="tnTable" class="sortable colSpace">
			 	<tr>
				 	<th><p style="margin: 0px 0px 0px 16px;"> Select All</p></th> <!-- column for checkboxes -->
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 	
			 	<c:set var="rowNum" value="0"/>
			 	<c:forEach var="tn" items="${allTnsInfo}">
				<tr>
					<td valign="top" align="left" style="white-space: nowrap;">${rowNum+1}&nbsp;<input type="checkbox" name="rowNum${rowNum}" value="${tn.get(tnColumnNumber)}" /></td>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
					<td valign="top" align="left">
						<!-- <a title="${messages['metadataForm.hoverText']}" href="metadataForm?update&titleno=${tn.get(3)}&returnTo=${returnTo}" ><c:out value="${tn.get(i)}"/></a> -->
						 <c:out value="${tn.get(i)}"/>
					</td>

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
<div style="float:right;" id="overlay1"></div>
<!-- End Overlay -->
<!-- Start Special Centered Box -->
<div id="specialBox1">
  <p>${messages['pasteExcel']}</p> 
  <form id="fPasteData" name="fPasteData" action="${overlayAction}" method="post">
    ${messages['requestingLocation']}
    <select id="site" name="site">
    	<option></option>
    	<c:forEach var="i" items="${allSites}">
    	<c:if test="${i==site}"><option selected>${i}</option> </c:if>
		<c:if test="${i!=site}"><option>${i}</option> </c:if>
		</c:forEach>
	</select>
	<br>
	 ${messages['owningInstitution']}
    <select id="owningInstitution" name="owningInstitution">
   		<option></option>
    	<c:forEach var="i" items="${allSites}">
    	<c:if test="${i==owningInstitution}"><option selected>${i}</option> </c:if>
		<c:if test="${i!=owningInstitution}"><option>${i}</option> </c:if>
		</c:forEach>
	</select>
  	<textarea id="tnData" name="tnData" rows="9" cols="60" style="height: 220px; width: 466px;">${pastedData}</textarea>   <!-- pasted data exists if pasted dupes then reshow page -->
  	<br>
  	<button id="save" name="button" value="save">${messages['save']}</button>
	<button id="cancel" name="button" value="cancel" >${messages['cancel']}</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:template>