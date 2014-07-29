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
				<td></td><!-- column for checkboxes -->
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
			 
			 <input style="margin: 33px 0px -33px 0px;   " class="sorttable_nosort" type="checkbox" id="checkAll" onclick="selectAllCheckboxes('checkAll');" /> <!-- hack to get checkbox to not resort when clicked, move outside of table column --> 
			 <table id="tnTable" class="sortable colSpace">
			 	<tr>
				 	<th><p style="margin: 0px 0px 0px 16px;"> Select All</p></th> <!-- column for checkboxes -->
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th   class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 	
			 	<c:set var="rowNum" value="0"/>
			 	<c:forEach var="tn" items="${allInfo}">
				<tr>
					<td style="white-space: nowrap;">${rowNum+1}&nbsp;<input type="checkbox" name="rowNum${rowNum}" value="${tn.get(0)}"/></td><!-- site and problemreason is only use now, so 0 index for value -->
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
					<td> <c:out value="${tn.get(i)}"/></td>

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
  <p>${messages['pasteData']}</p> 
  <form id="f2" name="f2" action="${overlayAction}" method="post">
  	<textarea id="pastedData" name="pastedData" rows="9" cols="60" style="height: 220px; width: 466px;"></textarea>
  	<br>
  	<button id="save" name="button" value="save">${messages['save']}</button>
	<button id="cancel" name="button" value="cancel" >${messages['cancel']}</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:template>