<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>

<!-- adding this version of jquery for calendar -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
 

<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">

	   
      <h1 class="serif">${messages['problems.pageTitle.problems']} - ${problem.tn} </h1>
	  
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
							<sf:select path="status" >
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
							<sf:select path="problemReason" >
								<sf:option value=""/>
								<sf:options items="${allProblemReasons}" />
							</sf:select>
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
					<td><button id="save" name="save" value="save" onclick="updateUrl2('f1', 'problemsForm', '${save}' );"><c:out value="${saveLabel}"/></button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="updateUrl2('f1', 'problemsForm', '${cancel}' );">${messages['cancel']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && problem.pn != ''}">
					<td><button id="update" name="update" value="update" onclick="updateUrl2('f1', 'problemsForm', 'update' );">${messages['update']}</button></td>
					<td><button id="create" name="create" value="create" onclick="updateUrl2('f1', 'problemsForm', 'update&doCreate' );">${messages['createProblem']}</button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="JavaScript:window.close(); return false;">${messages['close']}</button></td></td>
					</c:when>
					<c:when test="${mode=='read'}">
					<td><button id="create" name="create" value="create" onclick="updateUrl2('f1', 'problemsForm', 'update&doCreate' );">${messages['createProblem']}</button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="JavaScript:window.close(); return false;">${messages['close']}</button></td></td>
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