/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

import java.security.Principal;
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

@Controller("viewingReportController")
public class ViewingReportController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public ViewingReportController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	////////////NewBooks metadata start/////////////
 
	@RequestMapping(value="admin/viewingReports", method=RequestMethod.GET)
	public String getViewingReports(HttpServletRequest req, Model model,   String year, String month,  String totalViews, String totalUniqueBookViews, Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		//title and table labels
		List<String> labels = new ArrayList<String>();
		
		labels.add(messageSource.getMessage("year", null, locale));
		labels.add(messageSource.getMessage("month", null, locale));
		labels.add(messageSource.getMessage("numberOfViews", null, locale));
		labels.add(messageSource.getMessage("numberOfBooksViewed", null, locale));
	
		 
		 
		//////
		List<List> vr = bookService.getViewingReports( );
	 

		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.viewingReports", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allVRInfo", vr);  
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		 
		//model.addAttribute("allSites", bookService.getAllSites()); 
		//model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url
	 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("addViewingData", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("deleteSelected");
		details.add(messageSource.getMessage("deleteSelectedRows", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "viewingReports");
		model.addAttribute("overlayAction", "doViewingReportsInserts");
		return "admin/miscButtonAndTableFormWithCheckboxForViewingReport";
	}

	@RequestMapping(value="admin/viewingReports", method=RequestMethod.POST)
	public String doViewingReports(String button, HttpServletRequest request, String year, String month, String  totalViews, String totalUniqueBookViews,  Model model, Locale locale) {
		if(button.equals("deleteSelected") ) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();

			ArrayList<String> yearList = new ArrayList();		
			ArrayList<String> monthList = new ArrayList();
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) {
						String d = val[0];
						String delYear =  d.substring(0, d.indexOf("***"));
						String delMonth = d.substring(d.indexOf("***")+3);
						 //todo here
						yearList.add(delYear);
						monthList.add(delMonth);
					}
				}
			}
		
			
			bookService.deleteSelectedViewingReports(yearList, monthList);
		} else if(button.equals("load") ) {
			//noop
		}  
		//return "redirect:viewingReports?showRows=true"; //redirect get - guard against refresh-multi-updates and also update displayed url
		return getViewingReports(request, model, year,   month,  totalViews, totalUniqueBookViews, locale);
	}
	 
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doViewingReportsInserts", method=RequestMethod.POST)
	public String doViewingReportsInsertsPost(HttpServletRequest req,   String button,  String year, String month,  String totalViews, String totalUniqueBookViews, Principal principal,  Model model, Locale locale) {
		if(button.equals("save")) {
			 
			boolean hasDupe = bookService.getDuplicatesInViewingReport(year, month); //get list of pid/date already in table
			if(hasDupe == true) {
				//just display error
				model.addAttribute("bookErrorMessage",  messageSource.getMessage("viewingReportDupeError", null, locale) + "\n" );
				return "errors/generalError";
			}  
			
			bookService.insertBookViewingStats(year, month,  totalViews, totalUniqueBookViews); 
		}
		return getViewingReports(req, model, year, month, totalViews, totalUniqueBookViews, locale);
 
	} 
	  


} 
 