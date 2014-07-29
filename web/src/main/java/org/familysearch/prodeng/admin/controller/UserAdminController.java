/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.familysearch.prodeng.model.SqlTimestampPropertyEditor;
import org.familysearch.prodeng.model.User;
import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("userAdminController")
public class UserAdminController  implements MessageSourceAware{

	private MessageSource messageSource;
	private BookService bookService;

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Autowired
	public UserAdminController(BookService bookService) {
		this.bookService = bookService;
	}
	 
	//show populated form - for read (userId can be null and will display empty fields)
	@RequestMapping(value="admin/userAdmin", params="read", method=RequestMethod.GET)
	public String displayUserRead(@RequestParam(value = "userId", required=false) String userId, Model model,  Locale locale) {
	    model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.userAdmin", null, locale));
		model.addAttribute("mode", "read"); 
		//if(fetchAllUserIds != null && fetchAllUserIds.equals("true")) {
		model.addAttribute("allUserIds", bookService.getAllUserIds()); 
		//}
		model.addAttribute("allAuthorities", bookService.getAllAuthorities());
		model.addAttribute("user", bookService.getUser(userId)); 
		return "admin/userAdmin";
	}
 
	//show populated form - for update/create - used with button "update"  (or "create")
	@RequestMapping(value="admin/userAdmin", params="update", method=RequestMethod.POST)
	public String displayUserUpdatePost(String userId, User user, String doCreate, Model model,  Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.userAdmin", null, locale));
		model.addAttribute("mode", "update");  
		model.addAttribute("doCreate", doCreate); //if just update then null
		model.addAttribute("allAuthorities", bookService.getAllAuthorities());
		model.addAttribute("allLocations", bookService.getAllSites());
		model.addAttribute("user", bookService.getUser(userId)); 
	 
		return "admin/userAdmin";
	}

	//do update/create
	@RequestMapping(value="admin/userAdmin", params="save", method=RequestMethod.POST)
	public String doUserSave(User user, String userIdNew, String userIdOriginal, String search, String metadata, String scan, String process, String admin, String supervisor, String doCreate, Model model) {
		
		List<String> auths = new ArrayList<String>();
		 
		if(search != null && !search.equals(""))
			auths.add("search");
		if(metadata != null && ! metadata.equals(""))
			auths.add("metadata");
		if(scan != null && !scan.equals(""))
			auths.add("scan");
		if(process != null && !process.equals(""))
			auths.add("process");
		if(supervisor != null && !supervisor.equals(""))
			auths.add("supervisor");
		if(admin != null && !admin.equals(""))
			auths.add("admin");
			
		user.setAuthorities(auths);
		user.setUserId(userIdNew);//to get dropdown to show current user, had to not use spring mvc path in tag, set var here
		
		if(doCreate == null) {
			bookService.updateUser(user, userIdOriginal); //userId can be changed
		}else {
			bookService.createUser(user); //new user
		}
		return "redirect:userAdmin?read&userId=" + userIdNew; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//delete
	@RequestMapping(value="admin/userAdmin", params="delete", method=RequestMethod.POST)
	public String displayUserUpdatePost(String userId, User user, Model model,  Locale locale) {
		bookService.deleteUser(userId);
	 
		return "redirect:userAdmin?read&userId="; //redirect  
	}
	
	//do cancel
	@RequestMapping(value="admin/userAdmin", params="cancel", method=RequestMethod.POST)
	public String doUserCancel( String userIdNew, Model model) {
		//do nothing
		return "redirect:userAdmin?read&userId=" + userIdNew; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
 
	
}