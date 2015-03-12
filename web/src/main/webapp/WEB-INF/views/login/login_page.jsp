<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
<head>
		<jsp:include page="/WEB-INF/views/includes/brains.jsp"/>
  <link rel="stylesheet" href="https://ident.familysearch.org/cis-web/css/build/sign-in-36fd5353.css" media="screen">
 
<title>Book Scanning and Processing - Login Page</title></head>

<body class="env-production" onload='document.f.j_username.focus();'>
<div class="container" id="wrapper">

<header>
    <h1 class="logo-container">
      
        <a href="https://familysearch.org/register/?state=https://familysearch.org/" class="new_account btn">Create an Account</a>
        <a class="helplink" href="https://familysearch.org/ask">Get help ▾</a>
      
      <a class="logo" href="https://familysearch.org">FamilySearch</a>
    </h1>

</header>
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



<div class="sign-in-box">
      

        <!-- <p></p> -->
        <h2>
        
        
          Sign In to FamilySearch Book Digitization
        
        </h2>
        
        <!-- insert error messages in a <p> with a class of "form-error" -->
                
        <div class="sign-in-form-wrapper">
          <form name='f' action="${pageContext['request'].contextPath}/j_spring_security_check" method='POST' class="clearfix sign-in-form form">
     
            <input name="params" value="u5a9VjSgvpfKf0RNcJ_W1TW0A6ihU36tdxeIaDvUn29VXeWsquqjRaFCFMuBPtGS-9zbFf5DOgbQKjaznB-0B74w2aDh0S2gAEYapubYPLRL2X0u0Z7d-M568UopimxO3tVxiUVzEArGuGlFCKWCQq8RA7jn1IevRzzKsduSfVH3_mGEPH_FtuIfnR837CBCpOrFt400Y1o5kXXCsEtp9qTlaNvuYPgRrhVcl1YQZ_GJ46qvjvDCKC8hbwRh4UzIthgDkWFrQOWn2waQ3GgqCq5YUSPltAlY_AEsoc6YURCDnffehXHF_BoSfTOEaW40X1zuPqhHXZz_aST89vRTykLlJVrng6jLGWUP-qxxRf_fO-c-wSTaKRPbH5IsDQhJ6zg9dOzkmCciGtw8kwwfHvLCqyyRMaDZ7v9jfw01CUuRURwFx9F8WoCbUcOy5kEe" type="hidden">
            <fieldset>
              <ul class="account-fields">
                <li>
                  <!--  start field for user name-->
                  <!--div class="form-item" id="edit-name-1-wrapper"-->
                  <label for="j_username" class="fieldLabel">User Name</label>
                  <input type='text' name='j_username' value='' id="j_username"/>
                </li>
                <li>
                  <!--div class="form-item" id="edit-pass-1-wrapper"-->
                  <label class="fieldLabel" for="j_password">Password</label>
                  <input type='password' name='j_password' id='j_password'/>
                </li>
                 
                
              </ul>
            </fieldset>
            <fieldset class="form-actions">
              <button type="submit" name="target" id="login" class="btn btn-blue">
                Sign In
              </button>
            </fieldset>
            <p class="form-forgot-password">Forgot your <a href="https://familysearch.org/recover/username?state=/">user name</a> or <a href="https://familysearch.org/recover/password?state=/">password</a>?</p>
          </form>
        </div>
      
       
      
      <div class="form-terms-wrapper">
        <p class="form-terms">By clicking the "Sign in" button, I agree to the <a href="https://familysearch.org/terms">Rights and Use Information</a> (Updated 6/4/2012) and have read the <a href="https://familysearch.org/privacy">Privacy Policy</a>  (Updated 3/18/2014).</p>
      </div>
    </div>
    
    
<p style="height: 600px;"> 
 	<!--  <img src="${pageContext['request'].contextPath}/images/book.jpg"  height="300" width="450"/> -->
</p>


<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
		</div>
</div></body></html>