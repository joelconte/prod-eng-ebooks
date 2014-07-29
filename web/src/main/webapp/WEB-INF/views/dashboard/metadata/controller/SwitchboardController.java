/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.metadata.controller;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("metadataSwitchboardController")
public class SwitchboardController  implements MessageSourceAware{
	private MessageSource messageSource;
	
	public SwitchboardController( ) {
		 
	}
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	
	@RequestMapping(value="metadata/switchboard", method=RequestMethod.GET)
	public String get(Model model, Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("metadata.switchboardTitle", null, locale));
		return "metadata/switchboard";
	}
	 
}