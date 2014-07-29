<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>


<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">
 
	  <%@include file="/WEB-INF/views/includes/searchMenu.html"%>
      <h1 class="serif">${messages['search.miscSearch']}</h1>
	  
	    <c:if test="${mode=='read'}"><c:set var="isReadOnly" value="true"/></c:if>
	    <c:if test="${mode!='read'}"><c:set var="isReadOnly" value="false"/></c:if>
		<sf:form id="f1" style="margin: 0 0 60px 0;" class="" name="" method="post" action="" target="_self" modelAttribute="search">
			<div class="">
			<table id="searchTable">
	
				
				<tr>
				<c:choose>
				<c:when test="${mode=='read'}">
					<td>${messages['search.selectName']} 
						<c:if test="${allSearchIds != null}">
							<sf:select id="searchSelect" value="${search.searchId}"  path="searchId" onchange="fetchSearch('searchSelect', 'read'); return false;" >
								<sf:option value=""/>
								<sf:options items="${allSearchIds}" />
							</sf:select>
						</c:if>
					</td> 
					<td><button  onclick="updateUrlAndTarget('f1', 'searchMiscResults', 'runQuery', '_blank' );">${messages['runQuery']}</button></td>
					
				</c:when>
				</c:choose>
				</tr>
			</table>
			</div>
			 
		 
			<table id="table-2cols">
				<tr>
				<td id="col1">
					<table>
					<tr>
					<td class="labelColSize">${messages['search.name']}</td>
					<td>
						<c:if test="${isReadOnly==true}">
							<input  type="text" id="searchIdNew" value="${search.searchId}" name="searchIdNew"  readonly="${isReadOnly}"   />
						</c:if>
						<c:if test="${isReadOnly==false}">
							<input  type="text" id="searchIdNew" value="${search.searchId}" name="searchIdNew" />
						</c:if>
						<input type="hidden" value="${search.searchId}"  name="searchIdOriginal" id="searchIdOriginal" /></td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['search.description']}</td>
					<td>
					<sf:input  path="description" readonly="${isReadOnly}"/> 
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['search.queryText']}</td>
					<td>
					<sf:textarea  path="queryText" class="textareaLarge"/> 
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['search.owner']}</td>
					<td>
					<sf:input  path="owner" readonly="${isReadOnly}"/> 
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['search.dateUpdated']}</td>
					<td>
					<sf:input  path="dateUpdated" readonly="true"/> 
					</td>
					</tr>
				  
					
					</table>
				</td>
				
				
				<td id="col2" class="colPadding" style="vertical-align: top;">
					<table>
					 
					
					</table>
				</td>
			
				</tr>
			</table>
				
				
			<table id="buttonsTable">
				<tr>
					<td></td>
					<c:set var="save" value="save"/>
					<c:set var="saveLabel" value="${messages['save']}"/>
					<c:if test="${doCreate!=null}">
						<c:set var="save" value="save&doCreate"/>
						<c:set var="saveLabel" value="${messages['saveNewSearch']}"/>
					</c:if>
					<c:choose>
					<c:when test="${mode=='update'}">
					<td><button id="save" name="save" value="save" onclick="updateUrlAndTarget('f1', 'searchMisc', '${save}', '_self' );"><c:out value="${saveLabel}"/></button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="updateUrlAndTarget('f1', 'searchMisc', 'cancel', '_self' );">${messages['cancel']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && search.searchId != ''}">
					<td><button id="update" name="update" value="update" onclick="updateUrlAndTarget('f1', 'searchMisc', 'update', '_self' );">${messages['update']}</button></td>
					<td><button id="create" name="create" value="create" onclick="updateUrlAndTarget('f1', 'searchMisc', 'update&doCreate', '_self');">${messages['createSearch']}</button></td>
					</c:when>
					<c:when test="${mode=='read'}">
					<td><button id="create" name="create" value="create" onclick="updateUrlAndTarget('f1', 'searchMisc', 'update&doCreate', '_self' );">${messages['createSearch']}</button></td>
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