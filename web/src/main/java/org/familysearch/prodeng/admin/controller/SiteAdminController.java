/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

import java.sql.Timestamp;
import java.util.Locale;

import org.familysearch.prodeng.model.Site;
import org.familysearch.prodeng.model.SqlTimestampPropertyEditor;
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

@Controller("siteAdminController")
public class SiteAdminController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public SiteAdminController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	 
	//show populated form - for read (id can be null and will display empty fields)
	@RequestMapping(value="admin/siteAdmin", params="read", method=RequestMethod.GET)
	public String displaySiteRead(@RequestParam(value = "siteId", required=false) String siteId, Model model,  Locale locale) {
	    model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.siteAdmin", null, locale));
		model.addAttribute("mode", "read"); 
	
		model.addAttribute("allSiteIds", bookService.getAllSiteIdsIncludingInactive()); 
		 
		model.addAttribute("site", bookService.getSite(siteId)); 
		return "admin/siteAdmin";
	}
	
	//show populated form - for update/create - used with button "update"  (or "create")
	@RequestMapping(value="admin/siteAdmin", params="update", method=RequestMethod.POST)
	public String displaySiteUpdatePost(String siteId, Site site, String doCreate, Model model,  Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.siteAdmin", null, locale));
		model.addAttribute("mode", "update");  
		model.addAttribute("doCreate", doCreate); //if just update then null
		model.addAttribute("allAuthorities", bookService.getAllAuthorities());
		model.addAttribute("allLocations", bookService.getAllSitesIncludingInactive());
		model.addAttribute("site", bookService.getSite(siteId)); 
	 
		return "admin/siteAdmin";
	}

	//do update/create
	@RequestMapping(value="admin/siteAdmin", params="save", method=RequestMethod.POST)
	public String doSiteSave(Site site, String siteIdNew, String siteIdOriginal, String doCreate, Model model) {
		  
		site.setSiteId(siteIdNew);
		
		if(doCreate == null) {
			String failMsg = bookService.updateSite(site, siteIdOriginal); //userId can be changed
		 
			if(failMsg != null) {
				model.addAttribute("bookErrorMessage", failMsg);
				return "errors/generalError";
			}
		}else {
			bookService.createSite(site); //new site
		}
		return "redirect:siteAdmin?read&siteId=" + siteIdNew; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do delete
	@RequestMapping(value="admin/siteAdmin", params="delete", method=RequestMethod.POST)
	public String doSiteDelete(Site site, Model model, Locale locale) {

		String tnList = bookService.getListTNsUsingSite(site.getSiteId());
		String tnMetadataList = bookService.getListMetadataUsingSite(site.getSiteId());
		String userList = bookService.getListUsersUsingSite(site.getSiteId());
		String problemList = bookService.getListProblemsUsingSite(site.getSiteId());
	 
		if(!tnList.equals("") || !tnMetadataList.equals("") ||  !userList.equals("") ||  !problemList.equals("") ) {
			String failMsg =  messageSource.getMessage("site.deleteFailed0", null, locale)
					+  messageSource.getMessage("site.deleteFailed1", null, locale) + tnList + "<br>"
					+  messageSource.getMessage("site.deleteFailed2", null, locale) + tnMetadataList + "<br>"
					+  messageSource.getMessage("site.deleteFailed3", null, locale) + userList  + "<br>"
					+  messageSource.getMessage("site.deleteFailed4", null, locale) + problemList ;
			model.addAttribute("bookErrorMessage", failMsg);
			return "errors/generalError";
			
		}else {
			bookService.deleteSiteGoals(site.getSiteId());
			bookService.deleteSite(site.getSiteId());
		}
		
		return "redirect:siteAdmin?read&siteId="; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do cancel
	@RequestMapping(value="admin/siteAdmin", params="cancel", method=RequestMethod.POST)
	public String doSiteCancel( String siteIdNew, Model model) {
		//do nothing
		return "redirect:siteAdmin?read&userId=" + siteIdNew; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
	
	
	
	
	 
 
	 
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
 
	
}