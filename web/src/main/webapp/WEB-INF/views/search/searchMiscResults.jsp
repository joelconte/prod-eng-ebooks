<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>


<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">
 
 
      <h4 class="serif"><c:out value="${queryText}"/> </h4>
	   <button id="exportButton" name="exportButton" value="exportButton" onclick="tableToExcel('tnTable', 'booklist'); return false;">${messages['createExcelFile']}</button> 
		<table id="tnTable" class="sortable colSpace">
			<!--<tr>
			 		<c:forEach var="colLabel" items="${colLabels}">
			 		<th   class="sorttable_alpha">${colLabel}</th>
			 		</c:forEach>
			 	</tr> -->
			 	<c:forEach var="r" items="${result}">
				<tr>
					<c:forEach var="i" begin="0" end="${r.size()-1}">
					<td  class="sorttable_alpha" valign="top" align="left"><c:out value="${r.get(i)}"/></td>

					</c:forEach>
				</tr>
				</c:forEach>
		</table>
			
			
	</div>
  </div>
</div>
	  
</jsp:body>
</tags:template>