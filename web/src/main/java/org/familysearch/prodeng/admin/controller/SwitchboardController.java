/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("adminSwitchboardController")
public class SwitchboardController implements MessageSourceAware{
	private MessageSource messageSource;
	
	public SwitchboardController( ) {
		 
	}
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@RequestMapping(value="admin/switchboard", method=RequestMethod.GET)
	public String get(Model model, Locale locale) {
		
		model.addAttribute("pageTitle", messageSource.getMessage("admin.switchboardTitle", null, locale));
		return "admin/switchboard";
	}

	@RequestMapping(value="admin/postAdminSwitchboard", method=RequestMethod.GET)
	public String getPostAdminSwitchboard(Model model, Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.postSwitchboardTitle", null, locale));
		return "admin/postAdminSwitchboard";
	}
 
	@RequestMapping(value="admin/archiveAdminSwitchboard", method=RequestMethod.GET)
	public String getArchiveAdminSwitchboard(Model model, Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.archiveSwitchboardTitle", null, locale));
		return "admin/archiveAdminSwitchboard";
	}
	 
	@RequestMapping(value="admin/authorizationsAndSettingsAdminSwitchboard", method=RequestMethod.GET)
	public String getauthorizationsAndSettingsAdminSwitchboard(Model model, Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.authorizationsAndSettingsSwitchboardTitle", null, locale));
		return "admin/authorizationsAndSettingsAdminSwitchboard";
	}
	 
}