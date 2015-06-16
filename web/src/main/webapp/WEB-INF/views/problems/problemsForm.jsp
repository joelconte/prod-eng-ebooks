<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>

<!-- adding this version of jquery for calendar -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
 
<script type="text/javascript"> 
 

$(function() {
	populateProblemInitials();
	populateProblemDate();
	populateSolutionOwner();
  $( "#problem_date" ).datepicker();
  $( "#solution_date" ).datepicker();
});

function checkAuthority(){
	var sel= document.getElementById("problemStatus");
	var statusNew = sel.options[sel.selectedIndex].text;
	
	var auth= document.getElementById("authority").value;
	var statusOrig = document.getElementById("statusOrig").value;
	
	if(auth != "true" && (statusOrig == "Problem" || statusOrig == "Solution Found" || statusOrig == "Propose to Close" )
			&& (statusNew != "Problem" && statusNew != "Solution Found" && statusNew != "Propose to Close" ))
	{
		//only allow user to change back to problem, solution found, or propose to close
		
		alert("You must be a supervisor to change to this problem status.");
		sel= document.getElementById("problemStatus");
		sel.value = statusOrig;
	} 
}

function checkSolutionOwner(){
	var solutionOwner = $('#solutionOwner')[0];
	var site = solutionOwner.options[solutionOwner.selectedIndex].text;
	if(site == null || site === "" || site === "All Sites"){
		alert("A Solution Owner must be selected.");
		return false;
	}
	return true;
}

function populateProblemInitials(){
	var loggedInUser = $('#loggedInUser')[0];	
 
	var userId = loggedInUser.innerHTML.substring(14);
	var problemInitials = $('#problemInitials')[0];	
	var saveButton = $('#save')[0];	
	if(saveButton.innerHTML === "Save New Problem"){
		//new issue
		if(problemInitials.value == null  || problemInitials.value === ""){
			problemInitials.value = userId;
		}
		
	}
	
}
function populateProblemDate(){
	var problemDate = $('#problem_date')[0];	
	var saveButton = $('#save')[0];	
	if(saveButton.innerHTML === "Save New Problem"){
		//new issue
		if(problemDate.value == null  || problemDate.value === ""){
			setValueInDom("problem_date", getCurrentTimestamp());
		}
	}
	
}
function populateSolutionOwner(){
	//done on server side
}

</script>
<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">

	   
      <h1 class="serif">${messages['problems.pageTitle.problems']} - ${problem.tn} </h1>
      
	  <c:set var="hasAdmin" value="false"/>
	  <security:authorize access="hasAnyRole('admin', 'supervisor')">    
			<c:set var="hasAdmin" value="true"/>
	  </security:authorize>
					
	    <c:if test="${mode=='read'}"><c:set var="isReadOnly" value="true"/></c:if>
	    <c:if test="${mode!='read'}"><c:set var="isReadOnly" value="false"/></c:if>
		<sf:form id="f1" style="margin: 0 0 60px 0;" class="" name="" method="post" action="" modelAttribute="problem">
			<div class="">
			<table id="problemTable">
	
				<tr>
				<c:choose>
				<c:when test="${mode=='read'}">
					<td>${messages['problems.problemIds']} 
						<c:if test="${allProblems != null}">
							<sf:select id="problemSelect" value="${problem.pn}" path="pn" onchange="fetchProblem('problemSelect', 'read', ${problem.tn} ); return false;" >
								<!--<sf:option value=""/>-->
								<sf:options items="${allProblems}" />
							</sf:select>
						</c:if>
					</td> 
				</c:when>
				<c:otherwise>
					<tr>
						<td class="labelColSize">${messages['problems.pn']}</td>
						<td>
						<sf:input  path="pn" readonly="true"/> 
						</td>
					</tr>
				</c:otherwise>
				</c:choose>
				</tr>
			</table>
			</div>
		 
			<table id="table-2cols">
				<tr>
				<td id="col1">
					<table>
				 	<tr>
						<td class="labelColSize">${messages['problems.tn']}</td>
						<td>
						<sf:input  path="tn" readonly="true"/> 
						</td>
					</tr>
		
					<tr>
					<td class="labelColSize">${messages['problems.status']}</td>
					<td>
						<c:if test="${isReadOnly == true}"><sf:input path="status" readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<input type="hidden" id="authority" value="${hasAdmin}">
							<input type="hidden" id="statusOrig" value="${problem.getStatus()}" />
							<sf:select path="status" id="problemStatus" onchange="checkAuthority()">
								<sf:option value=""/>
								<sf:options items="${allStatuses}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					
					 
					
					<tr>
					<td class="labelColSize">${messages['problems.reason']}</td>
					<td>
						<c:if test="${isReadOnly == true}"><sf:input path="problemReason" readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select id="problemReason"  path="problemReason" >
								<sf:option value=""/>
								<sf:options items="${allProblemReasons2}" />
							</sf:select>
							<!--fixed by retuning List<Sting> from queyr
							<sf:select id="problemReason"  path="problemReason"  >
								<sf:option value=""/>
								<c:forEach var="item" items="${allProblemReasons2}">
									<c:if test="${item==problem.problemReason}">
										<sf:option value="${item}" selected="true"/>
									</c:if>
									<c:if test="${item!=siteId}">
										<sf:option value="${item}"/>
									</c:if>
								</c:forEach>
							</sf:select>-->
						</c:if>
					</td>
					</tr>
					
					<tr>
					<td class="labelColSize">${messages['problems.problemText']}</td>
					<td>
					<sf:textarea class="textarea3" path="problemText" readonly="${isReadOnly}"/> 
					</td>
					</tr>
					<tr>
					<td>${messages['problems.problemDate']}</td>
					<td><sf:input id="problem_date" path="problemDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('problem_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
						
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['problems.problemInitials']}</td>
					<td>
					<sf:input  path="problemInitials" readonly="${isReadOnly}"/> 
					</td>
					</tr>
				
					 
					</table>
				</td>
				
				
				<td id="col2" class="colPadding" style="vertical-align: top;">
					<table>
	
								 
					<tr>
					<td class="labelColSize">${messages['problems.solutionText']}</td>
					<td>
					<sf:textarea class="textarea3" path="solutionText" readonly="${isReadOnly}"/> 
					</td>
					</tr>
					<tr>
					<td>${messages['problems.solutionDate']}</td>
					<td><sf:input id="solution_date" path="solutionDate" readonly="${isReadOnly}" />
						<c:if test="${isReadOnly == false}"><button  class="dtUp" onclick="js:currentTimestamp('solution_date'); return false;">&larr;&nbsp; ${messages['now']}</button></c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['problems.solutionInitials']}</td>
					<td>
					<sf:input  path="solutionInitials" readonly="${isReadOnly}"/> 
					</td>
					</tr>
					
					<tr>
					<td class="labelColSize">${messages['problems.solutionOwner']}</td>
					<td>
					<c:if test="${isReadOnly == true}"><sf:input  path="solutionOwner"  readonly="${isReadOnly}"   /></c:if>
					<c:if test="${isReadOnly == false}">
					<sf:select path="solutionOwner" >
								<sf:option value=""/>
								<sf:options items="${allSites}" />
					</sf:select>
					</c:if>
					</td>
					</tr>
					
					</table>
				</td>
			
				</tr>
			</table>
				  
				
				
			<table id="buttonsTable">
				<tr>
					<td> </td>
					<c:set var="save" value="save"/>
					<c:set var="saveLabel" value="${messages['save']}"/>
					<c:set var="cancel" value="cancel"/>
					<c:if test="${doCreate!=null}">
						<c:set var="save" value="save&doCreate"/>
						<c:set var="saveLabel" value="${messages['saveNewProblem']}"/>
						<c:set var="cancel" value="cancel&doCreate"/>
					</c:if>
					
					<c:choose>
					<c:when test="${mode=='update'}">
					<td><button id="save" name="save" value="save" onclick="if(checkSolutionOwner() == false) return false; updateUrl2('f1', 'problemsForm', '${save}' );"><c:out value="${saveLabel}"/></button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="updateUrl2('f1', 'problemsForm', '${cancel}' );">${messages['cancel']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && problem.pn != ''}">
					<td><button id="update" name="update" value="update" onclick="updateUrl2('f1', 'problemsForm', 'update' );">${messages['update']}</button></td>
					<td><button id="create" name="create" value="create" onclick="updateUrl2('f1', 'problemsForm', 'update&doCreate' );">${messages['createProblem']}</button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="JavaScript:window.close(); return false;">${messages['close']}</button></td>
					</c:when>
					<c:when test="${mode=='read'}">
					<td><button id="create" name="create" value="create" onclick="updateUrl2('f1', 'problemsForm', 'update&doCreate' );">${messages['createProblem']}</button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="JavaScript:window.close(); return false;">${messages['close']}</button></td>
					</c:when>
					</c:choose>
					
					</tr>
			</table>
		 
		</sf:form>
	  		
		      
    </div>
 
  </div>
</div>
</jsp:body>
</tags:template>