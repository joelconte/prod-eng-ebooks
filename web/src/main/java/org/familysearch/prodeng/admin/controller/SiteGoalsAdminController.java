/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

import java.security.Principal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("siteGoalsAdminController")
public class SiteGoalsAdminController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public SiteGoalsAdminController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	////////////Site Goals Admin /////////////
 
	@RequestMapping(value="admin/siteGoalsAdmin", method=RequestMethod.GET)
	public String getSiteGoalsAdmin(  String site, Model model,  Locale locale) {
	 
		//title and table labels
		List<String> labels = new ArrayList<String>();
		
		labels.add(messageSource.getMessage("site", null, locale));
		labels.add(messageSource.getMessage("year", null, locale));
		labels.add(messageSource.getMessage("imageCountGoal", null, locale));
				
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.siteGoals", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allSites", bookService.getAllSiteIds()); 
		model.addAttribute("allSiteGoals", bookService.getSiteGoals(site)); 
		model.addAttribute("site", site);//site to reselect from dropdown
		model.addAttribute("keyColumnNumber", "0");//column where key is located for creating url
	 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteExcel", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("deleteSelected");
		details.add(messageSource.getMessage("deleteSelected", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("overlayAction", "doAddGoals");
		model.addAttribute("buttonsAction", "siteGoalsAdmin");
		return "admin/miscButtonAndTableFormWithCheckboxSiteGoals";
	}

	@RequestMapping(value="admin/siteGoalsAdmin", method=RequestMethod.POST)
	public String getSiteGoalsAdminPost(String button, HttpServletRequest request, Principal principal,  Model model, Locale locale) {
		if(button.equals("deleteSelected")) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String tnList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) {
						//tnList = tnList + ", " + "'" + val[0] + "'";
						String allName = val[0];
						int yIndex = allName.indexOf("yearflag");
						String siteName = allName.substring(0, yIndex);
						String year = allName.substring(yIndex+8);//
						tnList = tnList + " or (site = '" + siteName + "' and year = '" + year + "')";
					}
				}
			}
			if(tnList.length()>1)
				tnList = tnList.substring(3);//remove starting OR
			
			bookService.deleteSelectedSiteGoals(tnList);
		} 
 
		//should not happen
		return "redirect:siteGoalsAdmin"; //redirect get - guard against refresh-multi-updates and also update displayed url
	}
	
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doAddGoals", method=RequestMethod.POST)
	public String doAddGoalsPost(HttpServletRequest req, String site, String doUpdates, String button, String textData, Principal principal, Model model, Locale locale) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(textData, -1);
			String siteList = "";
			for(List<String> r : rows) {
				siteList += "or (site = '" + r.get(0) + "' and year = '" + r.get(1) + "') ";
			}
			siteList = siteList.substring(2);
			
			String dupSiteYearList = bookService.getDuplicateSiteYearGoals(siteList);  
			
 
			if(dupSiteYearList != "" && doUpdates == null) {
				//redisplay page with dupTnList msg
				
				//model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.alreadyExistMetadata", null, locale) + "\n" + dupTnList);
				//return "errors/generalError";
				model.addAttribute("dupeSiteInfo", dupSiteYearList);
				model.addAttribute("pastedData", textData); 
				return getSiteGoalsAdmin(site, model, locale);//forward request but first pass in dupe list to show user
			}else if(dupSiteYearList != "" && doUpdates != null) {
				//allow both updates and inserts
				//delete old data and then re-insert
				bookService.deleteSelectedSiteGoals(dupSiteYearList);
			}
	
			bookService.insertBatch("site_goal", new String[]{"site", "year", "goal_images_yearly"}, 
					  							new int[] {Types.VARCHAR,  Types.VARCHAR, Types.INTEGER}, rows); 
		}
		
		 
		return "redirect:siteGoalsAdmin?site=" + site; //redirect get - guard against refresh-multi-updates and also update displayed url
	} 
} 
 