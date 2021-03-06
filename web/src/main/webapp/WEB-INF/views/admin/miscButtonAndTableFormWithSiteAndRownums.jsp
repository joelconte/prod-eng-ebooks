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


<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/adminMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
	   
		<!-- admin only access waiting for files to be feceived page -->
	   	<security:authorize access="hasAnyRole( 'admin', 'supervisor')">
	   		
		<sf:form id="f1" class="" name="f1" method="post" action="${buttonsAction}" modelAttribute="book">
			<table id="buttonsTable">
			<tr>
				<td></td>
				<c:forEach var="b" items="${buttons}">
					<c:choose>
					<c:when test='${b.get(0).equals("overlayButton") == true}'>
						<td><button id="${b.get(0)}" name="overlayButton" value="${b.get(0)}" onclick="toggleOverlay(); return false;">${b.get(1)}</button></td>
					</c:when>
					<c:when test='${b.get(0).equals("exportButton") == true}'>
						<td><button id="${b.get(0)}" name="exportButton" value="${b.get(0)}" onclick="tableToExcel('tnTable', 'booklist'); return false;">${b.get(1)}</button></td>
					</c:when>
					<c:otherwise>
						<td><button id="${b.get(0)}" name="button" value="${b.get(0)}" >${b.get(1)}</button></td>
					</c:otherwise>
					</c:choose>
				</c:forEach>
				<td>
					<select id="siteDropdown" onchange="reloadProcessListWithSite('f1', 'siteDropdown' );">
					<option value=""/>
					<c:forEach var="i" items="${allLocations}">
	    				<c:if test="${i==location}"><option selected>${i}</option> </c:if>
						<c:if test="${i!=location}"><option>${i}</option> </c:if>
					</c:forEach>
					</select>
				</td>
			</tr>
			</table>
			 
			 <table id="tnTable" class="sortable colSpace">
			 	<tr>
			 		<th><p style="margin: 0px 0px 0px 16px;"> &nbsp;</p></th> <!-- column for row count -->
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th   class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 				 	
			 	<c:set var="rowNum" value="0"/>
			 	<c:forEach var="tn" items="${allTnsInfo}">
				<tr>
					<td valign="top" align="left" style="white-space: nowrap;">${rowNum+1}&nbsp;</td>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
				 
				 	<c:set var="encodedTN" value="${tn.get(0)}"/>
				 	<c:if test="${encodedTN.contains('&')}">
				 		<c:set var="encodedTN" value="${fn:replace(encodedTN,'&','&#37;26')}"/>
				 	</c:if>
				 	
					<td><a href="trackingForm?update&tn=${encodedTN}&returnTo=${returnTo}&siteSelected=${location}" ><c:out value="${tn.get(i)}"/></a></td>

					</c:forEach>
					<c:set var="rowNum" value="${rowNum+1}"/>
				</tr>
				</c:forEach>
			</table>
		</sf:form>
	  	
	  	</security:authorize>
 		<security:authorize access="hasAnyRole( 'admin', 'supervisor') == false">
			<p>${messages['process.notAuthorizedWaitingForFiles']}</p>
 		</security:authorize>
		      
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
  <form id="f2" name="f2" action="${overlayAction}" method="post">
  	<textarea id="tnData" name="tnData" rows="9" cols="60" style="height: 220px; width: 466px;"></textarea>
  	<br>
  	<button id="save" name="button" value="save">${messages['save']}</button>
	<button id="cancel" name="button" value="cancel" >${messages['cancel']}</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:template>