/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.process.controller;

import java.util.Locale;

import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("processSwitchboardController")
public class SwitchboardController   implements MessageSourceAware{
	
	private BookService bookService;
	private MessageSource messageSource;
	 
	public SwitchboardController( ) {
		 
	}
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	  
	@Autowired
	public SwitchboardController(BookService bookService) {
		this.bookService = bookService;
	}
	
	
	@RequestMapping(value="process/switchboard", method=RequestMethod.GET)
	public String get(Model model, Locale locale) {
		
		model.addAttribute("pageTitle", messageSource.getMessage("process.switchboardTitle", null, locale));
	 
		return "process/switchboard";
	}
	 
}