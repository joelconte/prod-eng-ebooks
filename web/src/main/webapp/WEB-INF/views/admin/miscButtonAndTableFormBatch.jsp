<%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
<jsp:body>

<script>
window.onload=function(){
	var myTH = document.getElementsByTagName("th")[0];
	if(sorttable != null && sorttable.innerSortFunction != null)
		sorttable.innerSortFunction.apply(myTH, []);
}
  
</script> 
<security:authorize access="hasAnyRole('admin')">    
<sf:form id="f1" class="" name="f1" method="post" action="${overlayAction}" modelAttribute="book">
<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/adminMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
	   
		
		    ${messages['columnToUpdate']}
    		<select id="columnName" name="columnName" onchange="document.getElementById('f1').submit(); ">
    			<option></option>
    			<c:forEach var="i" items="${allColumnNames}">
    			<c:if test="${i==columnName}"><option selected>${i}</option> </c:if>
				<c:if test="${i!=columnName}"><option>${i}</option> </c:if>
				</c:forEach>
			</select>
			${messages['newValue']}
 			<input type="text" id="newValue" name="newValue" value="${newValue}"/>
		
			<c:if test='${columnName!=""}'>
		  	  	<button name="button" id="saveNewValue" value="saveNewValue">${messages['saveNewValue']}</button>
		    </c:if>
		    
			<table id="buttonsTable">
			<tr>
				<td></td>
				<c:forEach var="b" items="${buttons}">
					<c:if test='${b.get(0).equals("overlayButton") == true}'>
						<td><button id="${b.get(0)}" name="overlayButton" value="${b.get(0)}" onclick="toggleOverlay(); return false;">${b.get(1)}</button></td>
					</c:if>
					<c:if test='${b.get(0).equals("overlayButton") == false}'>
						<td><button id="${b.get(0)}" name="button" value="${b.get(0)}" >${b.get(1)}</button></td>
					</c:if>
				</c:forEach>
			</tr>
			</table>
			 
			 <table id="tnTable" class="sortable colSpace">
			 	<tr>
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th   class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 	<c:forEach var="tn" items="${allTnsInfo}">
			 	
			 	<c:set var="encodedTN" value="${tn.get(0)}"/>
				<c:if test="${encodedTN.contains('&')}">
						<c:set var="encodedTN" value="${fn:replace(encodedTN,'&','&#37;26')}"/>
				</c:if>
				
				<tr>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
					<td><a href="trackingForm?read&tn=${encodedTN}" ><c:out value="${tn.get(i)}"/></a></td>

					</c:forEach>
				</tr>
				</c:forEach>
			</table>
 
			
		      
    </div>
 
  </div>
</div>
</div>
			 
<!-- Start Overlay to get list of TNs to paste in table -->
<div id="overlay1"></div>
<!-- End Overlay -->
<!-- Start Special Centered Box -->
<div id="specialBox1">
  <p>${messages['pasteExcel']}</p> 
   
  	<textarea id="tnData" name="tnData" rows="9" cols="60" style="height: 220px; width: 466px;"></textarea>
  	<input type="hidden" name="prevTnData" value="${prevTnData}" id="prevTnData"></input>
  	<br>
  	<button id="save" name="button" value="save">${messages['save']}</button>
	<button id="cancel" name="button" value="cancel"  onclick="toggleOverlay(); return false;" >${messages['cancel']}</button>					
 
</div>
</sf:form>
</security:authorize>
<security:authorize access="hasAnyRole('admin') == false">
	<p>${messages['admin.notAuthorized']}</p>
</security:authorize>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:template>