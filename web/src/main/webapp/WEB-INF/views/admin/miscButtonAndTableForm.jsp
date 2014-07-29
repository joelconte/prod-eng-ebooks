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
			</tr>
			</table>
			 
			 <table id="tnTable" class="sortable colSpace">
			 	<tr>
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th   class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 	<c:forEach var="tn" items="${allTnsInfo}">
				<tr>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
					<td><a href="trackingForm?read&tn=${tn.get(0)}" ><c:out value="${tn.get(i)}"/></a></td>

					</c:forEach>
				</tr>
				</c:forEach>
			</table>
		</sf:form>
	  	
			
		      
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