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
     
		<h1 class="serif">${pageTitle}</h1>
	   
		<sf:form id="f1" class="" name="f1" method="post" action="" modelAttribute="book">
			 <table id="tnTable" class="sortable colSpace">
			 	<tr>
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th>${colLabel}</th>
			 		</c:forEach>
			 	</tr>
			 	<c:forEach var="tn" items="${allTnsInfo}">
			 	
			 	<c:set var="encodedTN" value="${tn.get(0)}"/>
				<c:if test="${encodedTN.contains('&')}">
						<c:set var="encodedTN" value='${encodedTN.replace("&", "%26")}'/>
				</c:if>
				
				<tr>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
					<td><a href="trackingForm?read&tn=${encodedTN}" ><c:out value="${tn.get(i)}"/></a></td>

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