<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>

<script>

window.onload=function(){
	//not used.  do in jsp for quicker display
	//doSelects("${year}", "${month}", "${site}");
}

function doSelects(year, month, site){
	var y = document.getElementById('year');
	selectIt(y, year);
	
	var m = document.getElementById('month');
	selectIt(m, month);
	
	var s = document.getElementById('site');
	selectIt(s, site);
}

//set selection in select dropdown
function selectIt(obj, val){
	for(var i, j = 0; i = obj.options[j]; j++) {
	    if(i.value == val) {
	        obj.selectedIndex = j;
	        break;
	    }
	}
}


</script>

<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">

	  <%@include file="/WEB-INF/views/includes/adminMenu.html"%>
      <h1 class="serif">${messages['report.pageTitle.monthlyReport']}</h1>
	  
	 
		<form id="f1" style="margin: 0 0 60px 0;" class="" name="" method="post" action="" >
			<div class="trackingFormTop1">
				
				${messages['report.year']}
				<select  id="year"  name="year" style="margin: 9px;" >
					<option value="All"   <c:if test="${year=='All'}">selected</c:if> >All</option>
				    <c:forEach var="i" items="${allYears}">
						<option value="${i}"  <c:if test="${year==i}">selected</c:if> > ${i} </option>
					</c:forEach>
				</select>
				${messages['report.month']}		
				<select  id="month"  name="month" style="margin: 9px;">
				 	<option value="All"   <c:if test="${year=='All'}">selected</c:if> >All</option>
					<option value="January" <c:if test="${month=='January'}">selected</c:if> >January</option>
				    <option value="February" <c:if test="${month=='February'}">selected</c:if> >February</option>
				    <option value="March" <c:if test="${month=='March'}">selected</c:if>  >March</option>
				    <option value="April" <c:if test="${month=='April'}">selected</c:if>  >April</option>
				    <option value="May" <c:if test="${month=='May'}">selected</c:if>  >May</option>
				    <option value="June" <c:if test="${month=='June'}">selected</c:if> >June</option>
				    <option value="July" <c:if test="${month=='July'}">selected</c:if> >July</option>
				    <option value="August" <c:if test="${month=='August'}">selected</c:if> >August</option>
				    <option value="September" <c:if test="${month=='September'}">selected</c:if> >September</option>
				    <option value="October" <c:if test="${month=='October'}">selected</c:if> >October</option>
				    <option value="November" <c:if test="${month=='November'}">selected</c:if> >November</option>
				    <option value="December" <c:if test="${month=='December'}">selected</c:if> >December</option>
				</select>
				${messages['report.scannedBy']}	
				<select id="scannedBy"  name="scannedBy"  style="margin: 9px;" >
					<option value="All"   <c:if test="${year=='All'}">selected</c:if> >All</option>
					<c:forEach var="i" items="${allSites}">
						<option value="${i}"  <c:if test="${site==i}">selected</c:if> > ${i} </option>
					</c:forEach>
				</select>
							
				<button id="load" name="load" value="load">${messages['load']}</button> 
				
			</div>
			<div class="trackingFormTop">
			</div>
		</form>
		
		<form id="f2"  class="" name="" method="post" action="" >
		<table id="table-2cols">
			<tr>
			<td id="col1">
			<table>
				<tr>
				<td class="labelColSize">${messages['report.titlesScanned']}</td>
				<td>${titles_scanned}</td>
				</tr>
				<tr>
				<td class="labelColSize">${messages['report.imagesScanned']}</td>
				<td>${pages_scanned}</td>
				</tr>
				<tr><td class="rowSpace"></td></tr>
				
				<tr>
				<td class="labelColSize">${messages['report.titlesReleased']}</td>
				<td>${titles_kofaxed}</td>
				</tr>
				<tr>
				<td class="labelColSize">${messages['report.imagesReleased']}</td>
				<td>${pages_kofaxed}</td>
				</tr>
				<tr><td class="rowSpace"></td></tr>
				
				<tr>
				<td class="labelColSize">${messages['report.titlesLoaded']}</td>
				<td>${titles_loaded}</td>
				</tr>
				<tr>
				<td class="labelColSize">${messages['report.imagesLoaded']}</td>
				<td>${pages_loaded}</td>
				</tr>
				 
	  	
		      </table>
		      </td>
		      
		      <td id="col2" class="colPadding2" style="vertical-align: top;">
				 <table>
				 	<tr><td>
				 		<button id="showTns"  onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'showTns', '_blank' );" >${messages['report.showTns']}</button> 
				 	</td></tr>
				 	<tr><td>
				 		<button id="urlList"  onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'urlList', '_blank' );">${messages['report.urlList']}</button>  
				 	</td></tr>
				 	<tr><td>
				 		<button id="titleList"   onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'titleList', '_blank' );">${messages['report.titleList']}</button> 
				 	</td></tr>
				 	<tr><td>
				 		<button id="genealogyToday"   onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'genealogyToday', '_blank' );">${messages['report.genealogyToday']}</button> 
				 	</td></tr>
				 	<tr><td>
				 		<button id="siteReport"   onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'scannedByReport', '_blank' );">${messages['report.scannedByReport']}</button> 
				 	</td></tr>
				 	<tr><td class="rowSpace"></td></tr>
				 	
				 	<tr><td>
				 		<button id="monthlyUrls"   onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'monthlyUrls', '_blank' );">${messages['report.monthlyUrls']}</button> 
				 	</td></tr>
				 	<tr><td>
				 		<button id="siteTns"   onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'scannedByTns', '_blank' );">${messages['report.scannedByTns']}</button>  
				 	</td></tr>
				 	<tr><td>
				 		<button id="tnsThey"   onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'tnsThey', '_blank' );">${messages['report.tnsThey']}</button> 
				 	</td></tr>
				 	<tr><td>
				 		<button id="tnsWe"   onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'tnsWe', '_blank' );">${messages['report.tnsWe']}</button> 
				 	</td></tr>
				 	<tr><td>
				 		<button id="countPerMonth"   onclick="updateUrlAndTargetForReport('f2', 'miscReport', 'countPerMonth', '_blank' );">${messages['report.countPerMonth']}</button> 
				 	</td></tr>
				 	
				 	
				 </table>
			  </td>
			  </tr>
		</table>
		</form>
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>