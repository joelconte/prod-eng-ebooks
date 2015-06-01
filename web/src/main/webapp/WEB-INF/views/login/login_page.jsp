<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
<head>
<link rel="stylesheet" href="${pageContext['request'].contextPath}/styles/login.css" type="text/css" media="all" title="no title"   />
	
<!-- Project specific CSS -->
<link rel="stylesheet" href="${pageContext['request'].contextPath}/styles/project.css" type="text/css" media="all" title="no title"   />
<link rel="stylesheet" href="${pageContext['request'].contextPath}/styles/layout.css" type="text/css" media="all" title="no title"   />
<link rel="stylesheet" href="${pageContext['request'].contextPath}/styles/reset.css" type="text/css" media="all" title="no title"   />



<!-- Start of links from familysearch.org -->

	 <!-- <link rel="stylesheet" href="https://ident.familysearch.org/cis-web/css/build/sign-in.min-bef2d710.css">-->
        
	
   
      <link  href="${pageContext['request'].contextPath}/styles/hf-95cfda2f659c4fe62dde556df446c29f.css" rel='stylesheet' media='all'/>
      <link  href="${pageContext['request'].contextPath}/styles/hf-responsive-fb19ffc7fc9dd3c1574773dfebde4e2f.css" rel='stylesheet' media='all'/>
      <link  href="${pageContext['request'].contextPath}/styles/home-e84f9688752b5c8a04968cf81726de3b.styl.css" rel='stylesheet' media='screen'/>
      
   
<title>Book Scanning and Processing - Login Page</title></head>

<body class="env-production" onload='document.f.j_username.focus();'>
<div class="container" id="wrapper">

<header class="header" layout="" horizontal="" center="">

  <h1 class="logo-container">
   <a href="https://familysearch.org/" data-test="header-logo" data-control="SCCustomLinkTracker" data-config="{&quot;type&quot;: &quot;o&quot;, &quot;name&quot;: &quot;lo_hdr_logo&quot;}">
            
              <img src="${pageContext['request'].contextPath}/images/tree-logotype-1x-94806fd4d3214ea1ab7ce7eac7310d2c.png">
            
          </a>
  </h1>
  <span flex=""></span>
  
    <div class="account-wrapper" layout="" horizontal="" center="">
      <a class="helplink" href="https://familysearch.org/ask">Get help ▾</a>
       <a href="https://familysearch.org/register/?state=https://familysearch.org/" class="new_account btn">Create an Account</a>
    </div>
  

</header>
<!-- 
<header>
    <h1 class="logo-container">
       <span>
       <a class="logo" href="https://familysearch.org">FamilySearch</a>
       </span>
       <a href="https://familysearch.org/register/?state=https://familysearch.org/" class="new_account btn">Create an Account</a>
        <a class="helplink" href="https://familysearch.org/ask">Get help ▾</a>
      
       
    </h1>

</header> -->
<div class="padding-md overflow">
<h2 style="font-size: 150%">FamilySearch Book Digitization</h2> 
 
<!--   <form name='f' action="${pageContext['request'].contextPath}/j_spring_security_check" method='POST'>
 <table>
    <tr><td>User:</td><td><input type='text' name='j_username' value=''></td></tr>
    <tr><td>Password:</td><td><input type='password' name='j_password'/></td></tr>
    <tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
  </table>
</form>
-->
<div class="sign-in">
<div class="sign-in-box">
      

        <!-- <p></p> -->
        <h2>  Sign In to FamilySearch Book Digitization
        </h2>
        
        <!-- insert error messages in a <p> with a class of "form-error" -->
        
          
          
        
        <div class="sign-in-form-wrapper">
          <form name='f' action="${pageContext['request'].contextPath}/j_spring_security_check" method='POST' class="clearfix sign-in-form form">
            <input name="params" value="TX1h6Hnid3xt48HkGriAxQYTvgQ1Z83Sy9fpP2QeWQ99YOG4z7S9wGLZzIIADhrtLjTnU79L26Va27f1JeYVD1Ok70YCdAzDXRmVD1hRHT52oWTOmDtWrKmteTZb1JsSc3nyUwXCoSwwfYt9rLmqtiSkQofvo633EMR_d1HGuJlpO-rtDXRk69tI2wbpKNVZ5ipIVmXsiCgSB3CRGFgY7N4ILDv0aNUlWWWxhenkdKy1WLqPBPlWCKAWvFsA7GBpUMlqH2bMtIyaQEiicMiEvYXC2u2NzjX3Zu79XP55zOIOqj1T5CVHeFMTN-TB1Kzjh-N2LtwpNVScAK4OubRXHmf2IQIMHb-_plVmZHbddK1F9WS_y-Z7qm1vUsCjfbIXvI-6sRu0ptOdq-VyNLi7Ds5hb4g3vXHojnCN8jprGj9b-tTylZ_pr_I5OndP2PoWjk_s5BGqlKWbZCCBC5oTp0BQCWlZA2CSO9KBCrtnmhYw8zFPnAEcWoYmc9vQOthuTmI0D0ojezMD6WGtnIl9jOoM4x44-syorU4qOVzf_RRRONOXW0oE4zBphiNPGA3AGZdJawoWzs2RVezBgFnKG_ZuzFyJYMDM5G_E_-Imgu-CYVmzeOkLt5BCsO1IGoatvuJRIivrUPr1H0OhIS7ViEuE3z7nr117Qkpojk6LTL8d0CwD8Wx12Mn--6IU2oeIgUyvol2K_OwIkpNFaHuwdgKnzoJGlBlt6IdfcbEJ8q_NuNpKLgqeZI9B-GUhpSzqZKFZmZ-Vo1VR_TYFob6qKJaSLHC4TB0eKfqiQdrBHVO4w2DXfJCURSURBVNh-o7BMj5wx9ReWdwK-2fs-e3Izw==" type="hidden">
            <fieldset>
              <ul class="account-fields">
               <li>
                  <!--  start field for user name-->
                  <!--div class="form-item" id="edit-name-1-wrapper"-->
                  <label for="j_username" class="fieldLabel">User Name</label>
                  <input type='text' name='j_username' value='' id="j_username"/>
                </li>
                <br><br>
                <li>
                  <!--div class="form-item" id="edit-pass-1-wrapper"-->
                  <label class="fieldLabel" for="j_password">Password</label>
                  <input type='password' name='j_password' id='j_password'/>
                </li>
                 
                
              </ul>
            </fieldset>
            <div class="form-actions" layout="" horizontal="" center-justified="" center="">
              <button type="submit" name="target" id="login"  class="btn btn-blue  ">
                <span class="btn-label">Sign In</span>
                <span class="spinner"></span>
              </button>
            </div>
            <p class="form-forgot-password">Forgot your <a href="https://familysearch.org/recover/username?state=https://familysearch.org/">username</a> or <a href="https://familysearch.org/recover/password?state=https://familysearch.org/">password</a>?</p>
          </form>
        </div>
       
      
      <div class="form-terms-wrapper">
        <p class="form-terms">By clicking the <b>Sign in</b> button, I agree to the <a href="https://familysearch.org/terms">FamilySearch Rights and Use Information</a> (Updated 2/3/2015) and have read the <a href="https://familysearch.org/privacy">Privacy Policy</a>  (Updated 3/18/2014).</p>
      </div>
    </div>
</div>
  
<p style="height: 600px;"> 
 	<!--  <img src="${pageContext['request'].contextPath}/images/book.jpg"  height="300" width="450"/> -->
</p>


<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
		</div>
</div></body></html>