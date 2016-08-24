/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.scan.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import org.familysearch.prodeng.model.NonBook;
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

@Controller("scanNonBookTrackingFormController")
public class NonBookTrackingFormController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
 

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	public NonBookTrackingFormController(BookService bookService ) {
		this.bookService = bookService;
		 
	}
	 
	 
	//show populated form - for read (tn can be null and will display empty fields)
	@RequestMapping(value="scan/nonBookTrackingForm", params="read", method=RequestMethod.GET)
	public String displayNonBookRead(@RequestParam(value = "dn", required=false) String dn, String fetchAllTns, Model model, Locale locale) {
 
		model.addAttribute("pageTitle", messageSource.getMessage("scan.pageTitle.nonBooktrackingForm", null, locale));
		model.addAttribute("mode", "read"); 
		if(fetchAllTns != null && fetchAllTns.equals("true")) {
			model.addAttribute("allDns", bookService.getAllDns());
		}
		
		NonBook b = bookService.getNonBook(dn);
		 
		List<String> wildcardBooks = null;
		if(dn != null && b.getDn().equals(""))
		{
			//if no tn or secondary id then search with wildcard
			wildcardBooks = bookService.getNonBooksByWildcard(dn);
		}

		model.addAttribute("book", b); 
		model.addAttribute("dn",  dn);
		model.addAttribute("allDns", wildcardBooks); //use same select widget as above
		 
		return "scan/nonBookTrackingForm";
	} 
	 


	//show populated form - for new - used with anchor link "update" - same as update, just get DN from js popup 
	//same as below but GET for use in list of book links
	@RequestMapping(value="scan/nonBookTrackingForm",  params="new", method=RequestMethod.GET)
	public String displayBookNew(String dn,  String returnTo, Model model, Locale locale, Principal principal ) {
		NonBook tmp = bookService.getNonBook(dn);
		if(tmp.getDn().equals("")) {
			tmp.setDn(dn);//new empty book to display for new data
			bookService.createNonBook(tmp);
			
		}else {
			//error already exists
			model.addAttribute("bookErrorMessage", messageSource.getMessage("trackingForm.error.nonBookAlreadyExists", null, locale));
			return "errors/generalError";
		}
		return displayBookUpdatePost(  dn, returnTo, model, locale, principal);
	} 

	//show populated form - for update - used with anchor link "update" - 
	//same as below but GET for use in list of book links
	@RequestMapping(value="scan/nonBookTrackingForm",  params="update", method=RequestMethod.GET)
	public String displayBookUpdate(String dn,  String returnTo, Model model, Locale locale, Principal principal ) {
		return displayBookUpdatePost(  dn, returnTo, model, locale, principal);
	} 
	
	//show populated form - for update - used with button "update" 
	@RequestMapping(value="scan/nonBookTrackingForm", params="update", method=RequestMethod.POST)
	public String displayBookUpdatePost(   String dn,  String returnTo, Model model, Locale locale, Principal principal ) {
		//messageSource.getMessage("scan.pageTitle.trackingForm", null, locale);
		//messageSource.getMessage("x", null, locale);
		model.addAttribute("pageTitle", getTrackingFormTitle(returnTo, locale));//messageSource.getMessage("scan.pageTitle.trackingForm", null, locale));
 
		String failLockMsg = bookService.getBookLock(dn, principal.getName());
		if(failLockMsg != null) {
			model.addAttribute("bookErrorMessage", messageSource.getMessage("trackingForm.error.lockFail", null, locale) + "\n\n" + failLockMsg);
			return "errors/generalError";
		}
		
		model.addAttribute("mode", "update"); 
		NonBook tmp = bookService.getNonBook(dn);
		if(tmp.getDn().equals(""))
			tmp.setDn(dn);//new empty book to display for new data
		model.addAttribute("book", tmp); 
		model.addAttribute("allSites", bookService.getAllSites()); 
		model.addAttribute("allScanSitesIncludingInactive", bookService.getAllScanSitesIncludingInactive());
		  
		model.addAttribute("allLanguages", bookService.getAllLanguageIds()); 
	 
		model.addAttribute("userLocation",   bookService.getUser(principal.getName()).getPrimaryLocation());
		
		return "scan/nonBookTrackingForm";
	} 
	 
	

	//do update
	@RequestMapping(value="scan/nonBookTrackingForm", params="saveAndClose", method=RequestMethod.POST)
	public String doBookSaveAndClose(NonBook book, String dn, String returnTo,  Model model, Locale locale, Principal principal ) {
		String failReleaseLockMsg = bookService.checkAndReleaseBookLock(dn, principal.getName()); 
		if(failReleaseLockMsg != null) {
			model.addAttribute("bookErrorMessage", failReleaseLockMsg);
			return "errors/generalError";
			//return "process/trackingForm";
		}
		
		bookService.updateNonBook(book);
	 
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		
		//dn in url cannot contain &
		if(dn.contains("&")) {
			dn = dn.replace("&", "%26");
		}
		
		return "redirect:nonBookTrackingForm?read&dn=" + dn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do update
	@RequestMapping(value="scan/nonBookTrackingForm", params="save", method=RequestMethod.POST)
	public String doBookSave(NonBook book, String dn, String returnTo,  Model model, Principal principal ) {
		String failReleaseLockMsg = bookService.checkAndReleaseBookLock(dn, principal.getName()); 
		if(failReleaseLockMsg != null) {
			model.addAttribute("bookErrorMessage", failReleaseLockMsg);
			return "errors/generalError";
			//return "process/trackingForm";
		}
		
	 
		bookService.updateNonBook(book);
	 
		//dn in url cannot contain &
		if(dn.contains("&")) {
			dn = dn.replace("&", "%26");
		}
		
		if(returnTo != null && returnTo != "") 
			return "redirect:nonBookTrackingForm?update&dn=" + dn + "&returnTo=" + returnTo; //keep on same page with returnTo parm added again
		
		return "redirect:nonBookTrackingForm?update&dn=" + dn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 

	//do save new DN without any other data just to get it into system and validate unique dn
	@RequestMapping(value="scan/nonBookTrackingForm", params="saveNew", method=RequestMethod.POST)
	public String doBookSaveNew(NonBook book,@RequestParam(value = "dn", required=false)  String dn, String returnTo,  Model model, Locale locale, Principal principal ) {
		  
		//check unique dn
		NonBook exists = bookService.getNonBook(dn);
		if(exists.getDn() ==null || exists.getDn().equals("")) {
			//already exists
			//String nonBookAlreadyExistsMsg = messageSource.getMessage("nonBookTrackingForm.error.nonBookAlreadyExistsMsg", null, locale) ;
			//model.addAttribute("bookErrorMessage", nonBookAlreadyExistsMsg + " -- "  + dn);
			return "redirect:nonBookTrackingForm?read&dn=" + dn; 
		}
		
		//else add lock
		String failLockMsg = bookService.getBookLock(dn, principal.getName());
		if(failLockMsg != null) {
			model.addAttribute("bookErrorMessage", messageSource.getMessage("trackingForm.error.lockFail", null, locale) + "\n\n" + failLockMsg);
			return "errors/generalError";
		}
		
		//dont release lock just yet since they just added DN and intend to add more data
		/*
		String failReleaseLockMsg = bookService.checkAndReleaseBookLock(dn, principal.getName()); 
		if(failReleaseLockMsg != null) {
			model.addAttribute("bookErrorMessage", failReleaseLockMsg);
			return "errors/generalError";
			//return "process/trackingForm";
		}*/
		//bookService.checkAndReleaseBookLock(dn, principal.getName());/ 
		
	 
		bookService.updateNonBook(book);
	 
		//dn in url cannot contain &
		if(dn.contains("&")) {
			dn = dn.replace("&", "%26");
		}
		
		if(returnTo != null && returnTo != "") 
			return "redirect:nonBookTrackingForm?update&dn=" + dn + "&returnTo=" + returnTo; //keep on same page with returnTo parm added again
		
		return "redirect:nonBookTrackingForm?update&dn=" + dn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do cancel
	@RequestMapping(value="scan/nonBookTrackingForm", params="cancel", method=RequestMethod.POST)
	public String doBookCancel(@RequestParam("dn") String dn, String returnTo, Model model, Principal principal ) {
		bookService.checkAndReleaseBookLock(dn, principal.getName());//ignore any return error msg since it is just a cancel (ie even if admin unlocked it, it is ok)
		
		//do nothing
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		
		//dn in url cannot contain &
		if(dn.contains("&")) {
			dn = dn.replace("&", "%26");
		}
		
		return "redirect:nonBookTrackingForm?read&dn=" + dn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 

	//do cancel of new book (ie delete dn)
	//not sure why, but had to renamne to nonBookTrackingFormNewAndCancel to not get conflict with "cancel" param above????
	@RequestMapping(value="scan/nonBookTrackingFormNewAndCancel", params="newAndCancel", method=RequestMethod.POST)
	public String doBookCancelAndDelete( String dn,   Model model, Principal principal ) {
		bookService.checkAndReleaseBookLock(dn, principal.getName());//ignore any return error msg since it is just a cancel (ie even if admin unlocked it, it is ok)
		bookService.deleteNonBook(dn);
		//do nothing
	/*	if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		*/
		//dn in url cannot contain &
		if(dn.contains("&")) {
			dn = dn.replace("&", "%26");
		}
		
		return "redirect:nonBookTrackingForm?read"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
	 
	//show populated form - for update - used with button "delete"
	//hack {delete} to avoid conflicts with same request names
	@RequestMapping(value="scan/nonBookTrackingForm", params="delete", method=RequestMethod.POST)
	public String deleteBookPost(String dn, Model model) {
		bookService.deleteNonBook(dn);
		 
		return "redirect:nonBookTrackingForm?read"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
	public String getTrackingFormTitle(String returnTo, Locale locale){
		/*if("/scan/scanReady".equals(returnTo))
			return messageSource.getMessage("scan.scanReady", null, locale);
		else if("/scan/scanInProgress".equals(returnTo))
			return messageSource.getMessage("scan.scanInProgress", null, locale);
		else if("/scan/auditReady".equals(returnTo))
			return messageSource.getMessage("scan.ready", null, locale);
		else if("/scan/auditInProgress".equals(returnTo))
			return messageSource.getMessage("scan.image", null, locale);
		else if("/scan/processedReadyForOrem".equals(returnTo))
			return messageSource.getMessage("scan.processed", null, locale);
		else if("/scan/scanProblems".equals(returnTo))
			return messageSource.getMessage("scan.problems", null, locale);
		else */
		return messageSource.getMessage("scan.pageTitle.nonBooktrackingForm", null, locale);
	}
}