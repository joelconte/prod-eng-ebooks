<%@include file="/WEB-INF/views/includes/init.jsp"%>



<header id="global-engage-header" class="fix-header-footer clearfix" style="z-index: 0;">
    <div class="global-util-nav-container clearfix">
      <ul class="unstyled utilities">

        
                <!--<security:authentication property="principal.username" var="userId"/>-->
		        <c:if test="${hostname!='prod'}">
	        		<li id="host" style="padding-top: 3px; padding-right: 20px; color: orange;">${hostname}
	  	  	  		</li>
	        	</c:if>
	        	<c:if test="${loggedOnUserName==null}">
	        		<li style="padding-top: 3px;"><a href="${pageContext.request.contextPath}">Login</a> 
	  	  	  		</li>
	        	</c:if>
	        	<c:if test="${loggedOnUserName!=null}">
	        		 <li id="loggedInUser" style="padding-top: 3px;">Logged in as: ${loggedOnUserName}
	  	  	  		</li>
	        	</c:if>
	  		   
	  	  	  
	  	  	 	<c:if test="${loggedOnUserName!=null}">
	        	  <li class="">
	        	
	          	  	<a   href="${pageContext['request'].contextPath}/j_spring_security_logout" class="header-icon nav-item"> Logout</a>
	        	  </li>
	  			</c:if>
        
     
 <li class="help" data-control="ddPopover"  data-config='{"trigger": ".utility-trigger", "target": ".utility-menu", "closeTrigger": ".close-menu", "openEvent": "openHelpMenuEvent", "closeEvent": "closeHelpMenuEvent" }'>
	            <a href="http://familysearch.org/help" class="utility-trigger header-icon nav-item">Get Help</a>
	            <ul class="utility-menu double-wide unstyled" data-open-event="" data-close-event="">
	              <li>
	                <a href="javascript:;" class="close-menu"><i class="icon-remove-sign icon-white"></i></a>
	                <div class="row-fluid">
	                  <div class="span4">
	                    <h4>HELP OPTIONS</h4>
	                    <ul class="unstyled" data-control="Tabs">
                      
                        <li><a href="#" id="liveChatMenuContainer-link" class="menu-icon live-chat active">Live Chat</a></li>
                      
	                      <li><a href="#" id="callSupportContainer-link" class="menu-icon phone">Call Us</a></li>
                        <li><a href="#" id="inPersonHelpContainer-link" class="menu-icon phone">In-person Help</a></li>
	                      <li><a href="http://familysearch.org/ask/" class="menu-icon help-center">Help Center</a></li>
	                    </ul>
	                  </div>
	                  <div class="span8">
	                    <div id="callSupportContainer" class="helpMenuContainer">
	                      <h4>CALL SUPPORT</h4>
                        <div class="helpMenuContent" data-control="ProductSupportPhoneNumbers" data-config='{"startEvent":"openHelpMenuEvent"}'></div>
	                    </div>
                    
	                    <div id="liveChatMenuContainer" class="helpMenuContainer">
	                      <h4 id="helpMenuTitle">LIVE CHAT</h4>
	                      <div class="helpMenuContent" data-control="LiveHelp" data-config='{"chatOnly":true, "helpType": "ALL", "startEvent":"openHelpMenuEvent", "stopEvent":"closeHelpMenuEvent"}'></div>
	                    </div>
                    
                      <div id="inPersonHelpContainer" class="helpMenuContainer">
                        <h4 id="helpMenuTitle">In-person Help</h4>
                        <div class="helpMenuContent" data-control="InPersonHelp" data-config='{"isLoggedIn": false}'></div>
                      </div>
	                  </div>
	                </div>
	              </li>
	           </ul>
	          </li>
      </ul>

    </div><!-- /.sec-nav-container -->

    <div class="global-pri-nav-container clearfix">

      
        <h1 class="serif logo-container">
          <a href="https://familysearch.org/" data-test="header-logo" data-control="SCCustomLinkTracker" data-config='{"type": "o", "name": "lo_hdr_logo"}'>
            
              <img src="https://edge.fscdn.org/assets/img/theme-engage/assets/images/tree-logotype-1x-94806fd4d3214ea1ab7ce7eac7310d2c.png" />
            
          </a>
        </h1>
      

      
      

          <!-- iPhone view converts this to dd -->
          <nav class="mobile-actions-dd">
	          <div>&nbsp;</div>
	        </nav>
    </div><!-- /.global-pri-nav-container -->
</header>



