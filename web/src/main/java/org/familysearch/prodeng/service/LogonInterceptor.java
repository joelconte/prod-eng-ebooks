package org.familysearch.prodeng.service;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
 
public class LogonInterceptor extends HandlerInterceptorAdapter  {

	private BookService bookService;
	 	
	@Autowired
	public LogonInterceptor(BookService bookService ) {
		this.bookService = bookService;
	 
	}
	/*
   @Override
   public boolean preHandle(  HttpServletRequest request,
           HttpServletResponse response, Object handler) throws Exception {
	  
       System.out.println("Pre-handle test");
        
       return true;
   }
    */
	
   @Override
   public void postHandle(HttpServletRequest request,
           HttpServletResponse response, Object handler,
           ModelAndView modelAndView) throws Exception {
	   		Principal principal = request.getUserPrincipal();
	   		if(principal != null) {
	   			request.setAttribute( "loggedOnUserName", bookService.getUserName(principal.getName(), request.getSession())); 
	   			
	   		}
	   		else {
	   			//could return dialog to enter user id and store in session
	   		}
      
   }
   
    /*
   @Override
   public void afterCompletion(HttpServletRequest request,
           HttpServletResponse response, Object handler, Exception ex)
           throws Exception {
       System.out.println("After completion handle test");
   }*/
}