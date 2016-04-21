/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("jacobsToolController")
public class JacobsToolController {
 
	
	////////////NewBooks metadata start/////////////
 
	@RequestMapping(value="admin/jacobsTool", method=RequestMethod.GET)
	public String getJacobsTool(HttpServletRequest req, Model model, Locale locale) {
	 
		return "admin/jacobsTool";//launch jsp that contains applet
	}
 


} 
 