/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.search.controller;

import java.security.Principal;
import java.util.Locale;

import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("searchSwitchboardController")
public class SwitchboardController  implements MessageSourceAware{
	
	private BookService bookService;
	private MessageSource messageSource;
	 
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	  
	@Autowired
	public SwitchboardController(BookService bookService) {
		this.bookService = bookService;
	}
	
	@RequestMapping(value="search/switchboard", method=RequestMethod.GET)
	public String get( Principal principal, Model model,  Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("search.switchboardTitle", null, locale));

		//String location = bookService.getUser(principal.getName()).getPrimaryLocation();
		//model.addAttribute("location",   ((location==null||location=="")?"All Locations":location) );
		return "search/switchboard";
	}
	 
}