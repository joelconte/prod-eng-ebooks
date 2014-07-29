<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head><title>Book Scanning and Processing - Login Page</title></head>

<body onload='document.f.j_username.focus();'>

<h2>FamilySearch Book Scan Login</h2> 
<h3> Login with lds.org Username and Password</h3><form name='f' action="${pageContext['request'].contextPath}/j_spring_security_check" method='POST'>
 <table>
    <tr><td>User:</td><td><input type='text' name='j_username' value=''></td></tr>
    <tr><td>Password:</td><td><input type='password' name='j_password'/></td></tr>
    <tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
  </table>
</form>
<p> 
 	<img src="${pageContext['request'].contextPath}/images/book.jpg"  height="300" width="450"/>
</p></body></html>