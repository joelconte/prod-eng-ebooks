<%@include file="/WEB-INF/views/includes/init.jsp"%>
 

<tags:template>
<jsp:body>


<div class="container-fluid" id="main">
 
  <div class="row-fluid">
    <div class="span">

	  <%@include file="/WEB-INF/views/includes/adminMenu.html"%>
        <h1 class="serif">${pageTitle}</h1>
 
	    <c:if test="${mode=='read'}"><c:set var="isReadOnly" value="true"/></c:if>
	    <c:if test="${mode!='read'}"><c:set var="isReadOnly" value="false"/></c:if>
		<sf:form id="f1" style="margin: 0 0 60px 0;" class="" name="" method="post" action="" modelAttribute="site">
			<div class="">
			<table id="siteTable">
	
				
				<tr>
				<c:choose>
				<c:when test="${mode=='read'}">
					<td>${messages['siteAdmin.siteIds']} 
						<c:if test="${allSiteIds != null}">
							<!--<sf:select id="siteSelect"  path="siteId" onchange="fetchSite('siteSelect', 'read'); return false;" >
								<sf:option value=""/>
								<c:forEach var="item" items="${allSiteIds}">
									<c:if test="${item==siteId}">
										<sf:option value="${item}" selected="true"/>
									</c:if>
									<c:if test="${item!=siteId}">
										<sf:option value="${item}"/>
									</c:if>
								</c:forEach>
							</sf:select>-->
							<sf:select id="siteSelect" value="${site.siteId}" path="siteId" onchange="fetchSite('siteSelect', 'read'); return false;" >
								<sf:option value=""/>
								<sf:options items="${allSiteIds}"/>
							</sf:select> 
						</c:if>
					</td> 
					
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
					<td class="labelColSize">${messages['siteAdmin.siteId']}</td>
					<td>
						<c:if test="${isReadOnly==true}">
							<input  type="text" id="siteIdNew" value="${site.siteId}" name="siteIdNew"  readonly="${isReadOnly}"   />
						</c:if>
						<c:if test="${isReadOnly==false}">
							<input  type="text" id="siteIdNew" value="${site.siteId}" name="siteIdNew" />
						</c:if>
						<input type="hidden" value="${site.siteId}"  name="siteIdOriginal" id="siteIdOriginal" /></td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['siteAdmin.publishName']}</td>
					<td>
					<sf:input  path="publishName" readonly="${isReadOnly}" /> 
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['siteAdmin.location']}</td>
					<td>
					<sf:input  path="location" readonly="${isReadOnly}"/> 
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['siteAdmin.isFhc']}</td>
					<td>
					<c:if test="${isReadOnly=='true'}">
						<sf:checkbox value="T" path="isFhc" readonly="${isReadOnly}"/> 
					</c:if>
					<c:if test="${isReadOnly=='false'}">
						<sf:checkbox value="T" path="isFhc"/> 
					</c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['siteAdmin.isPartnerInstitution']}</td>
					<td>
					<c:if test="${isReadOnly=='true'}">
						<sf:checkbox  value="T" path="isPartnerInstitution" readonly="${isReadOnly}"/> 
					</c:if>
					<c:if test="${isReadOnly=='false'}">
						<sf:checkbox  value="T" path="isPartnerInstitution"/> 
					</c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['siteAdmin.isScanSite']}</td>
					<td>
					<c:if test="${isReadOnly=='true'}">
						<sf:checkbox  value="T"  path="isScanSite" readonly="${isReadOnly}"/> 
					</c:if>
					<c:if test="${isReadOnly=='false'}">
						<sf:checkbox  value="T"  path="isScanSite"/> 
					</c:if>
					</td>
					</tr>				
					<tr>
					<td class="labelColSize">${messages['siteAdmin.isProcessSite']}</td>
					<td>
					<c:if test="${isReadOnly=='true'}">
						<sf:checkbox  value="T" path="isProcessSite" readonly="${isReadOnly}"/> 
					</c:if>
					<c:if test="${isReadOnly=='false'}">
						<sf:checkbox  value="T" path="isProcessSite"/> 
					</c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['siteAdmin.isNotActive']}</td>
					<td>
					<c:if test="${isReadOnly=='true'}">
						<sf:checkbox  value="T" path="isInactiveSite" readonly="${isReadOnly}"/> 
					</c:if>
					<c:if test="${isReadOnly=='false'}">
						<sf:checkbox  value="T" path="isInactiveSite"/> 
					</c:if>
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
						<c:set var="saveLabel" value="${messages['saveNewSite']}"/>
					</c:if>
					<c:choose>
					<c:when test="${mode=='update'}">
					<td><button id="save" name="save" value="save" onclick="updateUrl2('f1', 'siteAdmin', '${save}' );"><c:out value="${saveLabel}"/></button></td>
					<td><button id="cancel" name="cancel" value="cancel" onclick="updateUrl2('f1', 'siteAdmin', 'cancel' );">${messages['cancel']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && site.siteId != ''}">
					<td><button id="update" name="update" value="update" onclick="updateUrl2('f1', 'siteAdmin', 'update' );">${messages['update']}</button></td>
					<td><button id="create" name="create" value="create" onclick="updateUrl2('f1', 'siteAdmin', 'update&doCreate' );">${messages['createSite']}</button></td>
					<td><button id="delete" name="delete" value="delete" onclick="updateUrl2('f1', 'siteAdmin', 'delete' );">${messages['deleteSite']}</button></td>
					</c:when>
					<c:when test="${mode=='read'}">
					<td><button id="create" name="create" value="create" onclick="updateUrl2('f1', 'siteAdmin', 'update&doCreate' );">${messages['createSite']}</button></td>
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