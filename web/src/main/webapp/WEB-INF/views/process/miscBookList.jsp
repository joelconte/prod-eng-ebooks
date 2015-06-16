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
     
	  	<%@include file="/WEB-INF/views/includes/processMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
	   
		<sf:form id="f1" class="" name="f1" method="post" action="" modelAttribute="book">
			<select id="siteDropdown" onchange="reloadProcessListWithSite('f1', 'siteDropdown' );">
				<option value=""/>
				<c:forEach var="i" items="${allLocations}">
    				<c:if test="${i==location}"><option selected>${i}</option> </c:if>
					<c:if test="${i!=location}"><option>${i}</option> </c:if>
				</c:forEach>
			</select>
			 <table id="tnTable" class="sortable colSpace">
			 	<tr>
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 	<c:forEach var="tn" items="${allTnsInfo}">
				<tr>
					<c:set var="encodedTN" value="${tn.get(0)}"/>
				 	<c:if test="${encodedTN.contains('&')}">
				 		<c:set var="encodedTN" value="${fn:replace(encodedTN,'&','&#37;26')}"/>
				 	</c:if>
				 	
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
					<td><a href="trackingForm?update&tn=${encodedTN}&returnTo=${returnTo}" ><c:out value="${tn.get(i)}"/></a></td>

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