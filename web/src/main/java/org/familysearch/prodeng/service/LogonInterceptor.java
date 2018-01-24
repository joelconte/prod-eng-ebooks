package org.familysearch.prodeng.service;
import java.net.InetAddress;
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
			
	   		String hostname = "";
			try
			{
			    InetAddress addr;
			    addr = InetAddress.getLocalHost();
			    hostname = addr.getHostName();
			    if(hostname.contains("bookprod-wf-app01"))
			    	hostname = "";//prod1
			    else if(hostname.contains("bookstage-wf-app01"))
			    	hostname = "Stage (test) Server";
			    else if(hostname.contains("bookprod-wf-app02"))
			    	hostname = "Mining (production) Server";
			    else
			    	hostname = "Local Server";
			    
			   
			}
			catch (Exception ex)
			{
			    System.out.println("Hostname can not be resolved");
			}
			request.setAttribute( "hostname", hostname); 
   			

      
   }
   
    /*
   @Override
   public void afterCompletion(HttpServletRequest request,
           HttpServletResponse response, Object handler, Exception ex)
           throws Exception {
       System.out.println("After completion handle test");
   }*/
}