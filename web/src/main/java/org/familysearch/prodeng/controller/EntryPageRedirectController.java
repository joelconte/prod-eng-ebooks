/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.controller;
import java.security.Principal;

import org.familysearch.prodeng.model.User;
import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("entrySwitchboardController")
public class EntryPageRedirectController {

	private BookService bookService;
	
	@Autowired
	public EntryPageRedirectController(BookService bookService) {
		this.bookService = bookService;
	}
	public EntryPageRedirectController( ) {
		 
	}
	
	@RequestMapping(value="entryPageRedirect", method=RequestMethod.GET)
	public String get( Principal principal ) {
		User u = bookService.getUser(principal.getName());	
	
		if("metadata".equals(u.getEntryPage()))
			return "redirect:metadata/switchboard"; 
		else if("scan".equals(u.getEntryPage()))
			return "redirect:scan/switchboard"; 
		else if("process".equals(u.getEntryPage()))
			return "redirect:process/switchboard"; 
		else if("admin".equals(u.getEntryPage()))
			return "redirect:admin/switchboard"; 
		else if("supervisor".equals(u.getEntryPage()))
			return "redirect:admin/switchboard"; 
		else if("asearch".equals(u.getEntryPage()))
			return "redirect:search/switchboard"; 
		else	
			return "redirect:admin/switchboard"; 
	}
 
	 
 
 
	 
}