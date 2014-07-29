<%@include file="/WEB-INF/views/includes/init.jsp"%>


 
	<header   class="fix-header-footer clearfix" id="global-engage-header" data-control="ddPopover" data-config='{"trigger":".mobile-actions-trigger", "target": ".mobile-actions-dd"}'>
				<div class="global-util-nav-container clearfix">
	        <ul class="unstyled utilities">
	        
		        <!--<security:authentication property="principal.username" var="userId"/>-->
		        
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
	  
           
	        <!-- TODO: Add working Church websites menu -->
	          
	  
	          <li class="hidden-phone hide">
	            <a href="#" class="utility-trigger nav-item" id="church-sites">Church Websites</a>
	          </li>
	          
	          <li class="menu visible-phone">
	            <a href="#" class="header-icon utility-trigger mobile-actions-trigger" id="main-nav-trigger">Menu</a>
	          </li>
	  
	        </ul>
 
	      </div><!-- /.sec-nav-container -->
 
	      <div class="fix-header-footer  global-pri-nav-container clearfix">
	        <h1   class="fix-header-footer serif logo"><a class="fix-header-footer" href="http://familysearch.org">FamilySearch</a></h1>
 
 
	  
	        
 
          <!-- iPhone view converts this to dd -->
	        <nav class="mobile-actions-dd">
	          <ul class="unstyled clearfix " >
	            <!-- <li><a href="/">Home</a></li> -->
	            <li class=""><a class="nav-item tree" href="http://familysearch.org/tree">Family Tree</a></li>
	            <li class=""><a class="nav-item photos" href="http://familysearch.org/photos">Photos</a></li>
	            <!-- <li class=""><a class="nav-item stories" href="http://familysearch.org/stories">Stories</a></li> -->
	            <li class=""><a class="nav-item search" href="http://familysearch.org/search">Search</a></li>
             
	          
	            <li class="account" data-control="ddPopover"  data-config='{"trigger": ".utility-trigger", "target": ".utility-menu" }'>
 
				
	              <!-- <a href="http://familysearch.org/login?fhf=true" id="sign-in" class="nav-item sign-in header-icon">Sign In</a>  -->
	          
	            </li>
	          </ul>
	        </nav>
	      </div><!-- /.global-pri-nav-container -->
	    </header>
 

