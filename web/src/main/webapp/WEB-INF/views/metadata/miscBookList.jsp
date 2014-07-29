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

<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/metadataMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
	   
	    
	    <c:if test="${keyCol == null}">
	    	<c:set var="keyCol" value="0"/>
	    </c:if>
		<sf:form id="f1" class="" name="f1" method="post" action="" modelAttribute="book">
			 <table id="tnTable" class="sortable colSpace">
			 	<tr>
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 	<c:forEach var="tn" items="${allTnsInfo}">
				<tr>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
					<td valign="top" align="left"><a title="${messages['metadataForm.viewTrackingFormhoverText']}"  href="trackingForm?read&tn=${tn.get(keyCol)}" ><c:out value="${tn.get(i)}"/></a></td>

					</c:forEach>
				</tr>
				</c:forEach>
			</table>
		</sf:form>
	  		
		      
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>