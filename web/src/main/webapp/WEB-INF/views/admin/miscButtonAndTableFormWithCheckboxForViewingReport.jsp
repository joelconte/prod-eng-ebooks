<%@include file="/WEB-INF/views/includes/init.jsp"%>
 
<tags:template>
<jsp:body>

<script>
window.onload=function(){
	var myTH = document.getElementsByTagName("th")[0];
	if(sorttable != null && sorttable.innerSortFunction != null)
		sorttable.innerSortFunction.apply(myTH, []);
 
	//showDupeMsgAfterPasteNewDataPost();//show confirm if already posted and data has dupes
} 
</script> 


<div id="wrapper1">
<div class="container-fluid" id="main" >
 
  <div class="row-fluid">
    <div class="span"> 
     
	  	<%@include file="/WEB-INF/views/includes/adminMenu.html"%>
		<h1 class="serif">${pageTitle}</h1>
	   
		<form id="f1" class="" name="f1" method="post" action="${buttonsAction}" >
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
			 	<c:forEach var="tn" items="${allVRInfo}">
				
				<tr>
					<td valign="top" align="left" style="white-space: nowrap;">${rowNum+1}&nbsp;<input type="checkbox" name="rowNum${rowNum}" value="${tn.get(0)}***${tn.get(1)}" /></td>
					<c:forEach var="i" begin="0" end="${colLabels.size()-1}">
					<td valign="top" align="left">
						<!-- <a title="${messages['metadataForm.hoverText']}" href="metadataForm?update&titleno=${encodedTN}&returnTo=${returnTo}" ><c:out value="${tn.get(i)}"/></a> -->
						  <c:out value="${tn.get(i)}"/>
					</td>

					</c:forEach>
					<c:set var="rowNum" value="${rowNum+1}"/>
				</tr>
				</c:forEach>
			</table>
		</form>
	  	     
    </div>
  </div>
</div>
</div>
			 
<!-- Start Overlay to get list of TNs to paste in table -->
<div style="float:right;" id="overlay1"></div>
<!-- End Overlay -->
<!-- Start Special Centered Box -->
<div id="specialBox1">
  <p>${messages['addViewingData']}</p> 
  <form id="fPasteData" name="fPasteData" action="${overlayAction}" method="post">
  
    
      ${messages['year']}&nbsp; 
         <select id="year" name="year">
			        <c:if test="${year == '2010'}"> <option selected="true">2010</option></c:if>
			        <c:if test="${year != '2010'}"> <option>2010</option></c:if>
			        <c:if test="${year == '2011'}"> <option selected="true">2011</option></c:if>
			        <c:if test="${year != '2011'}"> <option>2011</option></c:if>
			        <c:if test="${year == '2012'}"> <option selected="true">2012</option></c:if>
			        <c:if test="${year != '2012'}"> <option>2012</option></c:if>
			        <c:if test="${year == '2013'}"> <option selected="true">2013</option></c:if>
			        <c:if test="${year != '2013'}"> <option>2013</option></c:if>
			        <c:if test="${year == '2014'}"> <option selected="true">2014</option></c:if>
			        <c:if test="${year != '2014'}"> <option>2014</option></c:if>
			        <c:if test="${year == '2015'}"> <option selected="true">2015</option></c:if>
			        <c:if test="${year != '2015'}"> <option>2015</option></c:if>
			        <c:if test="${year == '2016'}"> <option selected="true">2016</option></c:if>
			        <c:if test="${year != '2016'}"> <option>2016</option></c:if>
			        <c:if test="${year == '2017'}"> <option selected="true">2017</option></c:if>
			        <c:if test="${year != '2017'}"> <option>2017</option></c:if>
			        <c:if test="${year == '2018'}"> <option selected="true">2018</option></c:if>
			        <c:if test="${year != '2018'}"> <option>2018</option></c:if>
			        <c:if test="${year == '2019'}"> <option selected="true">2019</option></c:if>
			        <c:if test="${year != '2019'}"> <option>2019</option></c:if>
			      
				</select>
			 
				
	<br>
	 ${messages['month']}  
 
			    <select id="month" name="month">
			   		 <c:if test="${month == '1'}"><option selected="true">1</option></c:if>
			   		 <c:if test="${month == '2'}"><option selected="true">2</option></c:if>
			   		 <c:if test="${month == '3'}"><option selected="true">3</option></c:if>
			   		 <c:if test="${month == '4'}"><option selected="true">4</option></c:if>
			   		 <c:if test="${month == '5'}"><option selected="true">5</option></c:if>
			   		 <c:if test="${month == '6'}"><option selected="true">6</option></c:if>
			   		 <c:if test="${month == '7'}"><option selected="true">7</option></c:if>
			   		 <c:if test="${month == '8'}"><option selected="true">8</option></c:if>
			   		 <c:if test="${month == '9'}"><option selected="true">9</option></c:if>
			   		 <c:if test="${month == '10'}"><option selected="true">10</option></c:if>
			   		 <c:if test="${month == '11'}"><option selected="true">11</option></c:if>
			   		 <c:if test="${month == '12'}"><option selected="true">12</option></c:if>
			   		 <c:if test="${month != '1'}"><option>1</option></c:if>
			   		 <c:if test="${month != '2'}"><option>2</option></c:if>
			   		 <c:if test="${month != '3'}"><option>3</option></c:if>
			   		 <c:if test="${month != '4'}"><option>4</option></c:if>
			   		 <c:if test="${month != '5'}"><option>5</option></c:if>
			   		 <c:if test="${month != '6'}"><option>6</option></c:if>
			   		 <c:if test="${month != '7'}"><option>7</option></c:if>
			   		 <c:if test="${month != '8'}"><option>8</option></c:if>
			   		 <c:if test="${month != '9'}"><option>9</option></c:if>
			   		 <c:if test="${month != '10'}"><option>10</option></c:if>
			   		 <c:if test="${month != '11'}"><option>11</option></c:if>
			   		 <c:if test="${month != '12'}"><option>12</option></c:if>
				</select> <br>
	Total Views &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="numViews" type="text" name="totalViews"/>  <br>
	Total Unique Book Views <input id="numUniqueViews" type="text" name="totalUniqueBookViews"/>  
  	<br> <br>
  	<button id="save" name="button" value="save">${messages['save']}</button>
	<button id="cancel" name="button" value="cancel" >${messages['cancel']}</button>					
  </form>
</div>
<!-- End Special Centered Box -->

		

</jsp:body>
</tags:template>