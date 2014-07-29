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
 
		<sf:form id="f1" style="margin: 0 0 60px 0;" class="" name="" method="post" action="" modelAttribute="user">
 
			<div class="">
			<table id="userTable">
	
				
				<tr>
				<c:choose>
				<c:when test="${mode=='read'}">
					<td>${messages['userAdmin.userIds']} 
						<c:if test="${allUserIds != null}">
							<sf:select id="userSelect" value="${user.userId}"  path="userId" onchange="fetchUser('userSelect', 'read'); return false;" >
								<sf:option value=""/>
								<sf:options items="${allUserIds}" />
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
					<td class="labelColSize">${messages['userAdmin.userId']}</td>
					<td>
						<c:if test="${isReadOnly==true}">
							<input  type="text" id="userIdNew" value="${user.userId}" name="userIdNew"  readonly="${isReadOnly}"   />
						</c:if>
						<c:if test="${isReadOnly==false}">
							<input  type="text" id="userIdNew" value="${user.userId}" name="userIdNew" />
						</c:if>
						<input type="hidden" value="${user.userId}"  name="userIdOriginal" id="userIdOriginal" /></td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['userAdmin.name']}</td>
					<td>
					<sf:input  path="name" readonly="${isReadOnly}"/> 
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['userAdmin.entryPage']}</td>
					<td>
						<c:if test="${isReadOnly == true}"><sf:input  path="entryPage"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="entryPage" >
								<sf:option value=""/>
								<sf:options items="${allAuthorities}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['userAdmin.primaryLocation']}</td>
					<td>
					 
						<c:if test="${isReadOnly == true}"><sf:input  path="primaryLocation"  readonly="${isReadOnly}"   /></c:if>
						<c:if test="${isReadOnly == false}">
							<sf:select path="primaryLocation" >
								<sf:option value=""/>
								<sf:options items="${allLocations}" />
							</sf:select>
						</c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['userAdmin.sendScanNotice']}</td>
					<td>
						<c:if test="${isReadOnly==true}">
						<sf:checkbox  value="T" path="sendScanNotice" readonly="${isReadOnly}"/>
						</c:if>
						<c:if test="${isReadOnly==false}">
						<sf:checkbox  value="T" path="sendScanNotice"/>
						</c:if>
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['userAdmin.email']}</td>
					<td>
						<sf:input path="email" readonly="${isReadOnly}"/> 
					</td>
					</tr>
					
					<tr>
					
					<td class="labelColSize">${messages['userAdmin.authorities']}</td>
				 
					<td>
					<c:set var="hasAdmin" value="false"/>
					<security:authorize access="hasAnyRole('admin')">    
						<c:set var="hasAdmin" value="true"/>
					</security:authorize>
					<c:forEach var="auth" items="${allAuthorities}">
						<c:if test="${hasAdmin=='true' || (auth!='supervisor' && auth!='admin')}">
							<c:choose>
							<c:when test="${user.authorities.contains(auth)}">
								<input type="checkbox" name="${auth}" value="checked" checked="true" /> ${auth} &nbsp;&nbsp;
							</c:when>
							<c:otherwise>
								<input type="checkbox" name="${auth}"  /> ${auth} &nbsp;&nbsp;
							</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${hasAdmin!='true' && (auth=='supervisor' || auth=='admin')}">
							<c:choose>
							<c:when test="${user.authorities.contains(auth)}">
								<input type="hidden" name="${auth}" value="checked" />
								<input type="checkbox" name="${auth}dummy" value="checked" checked="true"  disabled="disabled"/> ${auth} &nbsp;&nbsp;
							</c:when>
							<c:otherwise>
								<input type="hidden" name="${auth}"   />
								<input type="checkbox" name="${auth}dummy"  disabled="disabled" /> ${auth} &nbsp;&nbsp;
							</c:otherwise>
							</c:choose>
						</c:if>
					 </c:forEach>
							 
				 
					</td>
					
					</tr>
					<tr>
					<td class="labelColSize">${messages['userAdmin.lastLoggedIn']}</td>
					<td>
					<sf:input  path="lastLoggedIn" readonly="true"/> 
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['userAdmin.dateAdded']}</td>
					<td>
					<sf:input  path="dateAdded" readonly="true"/> 
					</td>
					</tr>
					<tr>
					<td class="labelColSize">${messages['userAdmin.dateUpdated']}</td>
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
						<c:set var="saveLabel" value="${messages['saveNewUser']}"/>
					</c:if>
					<c:choose>
					<c:when test="${mode=='update'}">
						<td><button id="save" name="save" value="save" onclick="updateUrl2('f1', 'userAdmin', '${save}' );"><c:out value="${saveLabel}"/></button></td>
						<td><button id="cancel" name="cancel" value="cancel" onclick="updateUrl2('f1', 'userAdmin', 'cancel' );">${messages['cancel']}</button></td>
					</c:when>
					<c:when test="${mode=='read' && user.userId != ''}">
						<td><button id="update" name="update" value="update" onclick="updateUrl2('f1', 'userAdmin', 'update' );">${messages['update']}</button></td>
						<td><button id="create" name="create" value="create" onclick="updateUrl2('f1', 'userAdmin', 'update&doCreate' );">${messages['createUser']}</button></td>
						<c:if test="${hasAdmin=='true'}">
							<td><button id="delete" name="deletete" value="delete" onclick="updateUrl2('f1', 'userAdmin', 'delete' );">${messages['deleteUser']}</button></td>
						</c:if>
					</c:when>
					<c:when test="${mode=='read'}">
						<td><button id="create" name="create" value="create" onclick="updateUrl2('f1', 'userAdmin', 'update&doCreate' );">${messages['createUser']}</button></td>
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