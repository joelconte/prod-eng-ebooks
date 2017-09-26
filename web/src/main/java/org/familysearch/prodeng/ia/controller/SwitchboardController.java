/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.ia.controller;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("iaSwitchboardController")
public class SwitchboardController implements MessageSourceAware{
	private MessageSource messageSource;
	
	public SwitchboardController( ) {
		 
	}
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@RequestMapping(value="ia/switchboard", method=RequestMethod.GET)
	public String get(Model model, Locale locale) {
		
		model.addAttribute("pageTitle", messageSource.getMessage("ia.switchboardTitle", null, locale));
		return "ia/switchboard";
	} 
}