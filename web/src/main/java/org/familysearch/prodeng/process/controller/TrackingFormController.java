/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.process.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import org.familysearch.prodeng.model.Book;
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

@Controller("processTrackingFormController")
public class TrackingFormController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public TrackingFormController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	 
	//show populated form - for read (tn can be null and will display empty fields)
	@RequestMapping(value="process/trackingForm", params="read", method=RequestMethod.GET)
	public String displayBookRead(@RequestParam(value = "tn", required=false) String tn, String fetchAllTns, Model model, Locale locale) {
 		
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.trackingForm", null, locale));
		model.addAttribute("mode", "read"); 
		if(fetchAllTns != null && fetchAllTns.equals("true")) {
			model.addAttribute("allTns", bookService.getAllTns()); 
		}
		Book b = bookService.getBook(tn);
		if(tn != null && b.getTn().equals(""))
		{
			b = bookService.getBookBySecondaryIdentifier(tn);
		}
		List<String> wildcardBooks = null;
		if(tn != null && b.getTn().equals(""))
		{
			//if no tn or secondary id then search with wildcard
			wildcardBooks = bookService.getBooksByWildcard(tn);
		}

		model.addAttribute("book", b); 
		model.addAttribute("tn",  tn);
		model.addAttribute("allTns", wildcardBooks); //use same select widget as above
		model.addAttribute("problemOpenList", bookService.getBookOpenProblems( b.getTn()));
		model.addAttribute("problemClosedList", bookService.getBookClosedProblems( b.getTn()));
		return "process/trackingForm";
	} 
	 


	//show populated form - for update - used with anchor link "update" - 
	//same as below but GET for use in list of book links
	@RequestMapping(value="process/trackingForm",  params="update", method=RequestMethod.GET)
	public String displayBookUpdate(String tn, String returnTo, Model model, Locale locale, Principal principal ) {
		return displayBookUpdatePost(tn, returnTo, model, locale, principal );
	} 
	
	//show populated form - for update - used with button "update"
	@RequestMapping(value="process/trackingForm", params="update", method=RequestMethod.POST)
	public String displayBookUpdatePost(   String tn,  String returnTo,  Model model, Locale locale, Principal principal ) {
		
		model.addAttribute("pageTitle", getTrackingFormTitle(returnTo, locale));//messageSource.getMessage("process.pageTitle.trackingForm", null, locale));
		// messageSource.getMessage("trackingForm.error.lockFail", null, locale);
		String failLockMsg = bookService.getBookLock(tn, principal.getName());
		if(failLockMsg != null) {
			model.addAttribute("bookErrorMessage", messageSource.getMessage("trackingForm.error.lockFail", null, locale) + "\n\n" + failLockMsg);
			return "errors/generalError";
		}
		
		model.addAttribute("mode", "update"); 
		model.addAttribute("book", bookService.getBook(tn)); 
		model.addAttribute("allSites", bookService.getAllSites()); 
		model.addAttribute("allScanSitesIncludingInactive", bookService.getAllScanSitesIncludingInactive());
		model.addAttribute("allPropertyRights", bookService.getAllPropertyRights()); 
		model.addAttribute("allPublicationTypes", bookService.getAllPublicationTypes()); 
		model.addAttribute("allLanguages", bookService.getAllLanguageIds()); 
		model.addAttribute("problemOpenList", bookService.getBookOpenProblems(tn));
		model.addAttribute("problemClosedList", bookService.getBookClosedProblems(tn));
		model.addAttribute("returnTo", returnTo);//key to show saveAndClose
		return "process/trackingForm";
	} 
	 


	//do update - and close
	@RequestMapping(value="process/trackingForm", params="saveAndClose", method=RequestMethod.POST)
	public String doBookSaveAndClose(Book book, String tn,  String returnTo,  Model model, Principal principal) {
		//save book object which was linked in displayBookUpdatePost and form modelAttribute attr
		 
		String failReleaseLockMsg = bookService.checkAndReleaseBookLock(tn, principal.getName()); 
		if(failReleaseLockMsg != null) {
			model.addAttribute("bookErrorMessage", failReleaseLockMsg);
			return "errors/generalError";
			//return "process/trackingForm";
		}
		 
		
		bookService.updateBook(book);
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		
		//tn in url cannot contain &
		if(tn.contains("&")) {
			tn = tn.replace("&", "%26");
		}
		
		return "redirect:trackingForm?read&tn=" + tn; //if no returnTo, then just put back in read state
	}
	
	//do update 
	@RequestMapping(value="process/trackingForm", params="save", method=RequestMethod.POST)
	public String doBookSave(Book book, String tn,  String returnTo,  Model model, Principal principal) {
		String failReleaseLockMsg = bookService.checkAndReleaseBookLock(tn, principal.getName()); 
		if(failReleaseLockMsg != null) {
			model.addAttribute("bookErrorMessage", failReleaseLockMsg);
			return "errors/generalError";
			//return "process/trackingForm";
		}
		
		bookService.updateBook(book);
		
		//tn in url cannot contain &
		if(tn.contains("&")) {
			tn = tn.replace("&", "%26");
		}
		
		if(returnTo != null && returnTo != "") 
			return "redirect:trackingForm?update&tn=" + tn + "&returnTo=" + returnTo; //keep on same page with returnTo parm added again
		return "redirect:trackingForm?update&tn=" + tn; //redirect - guard against refresh-multi-updates and also update displayed url
	}
		
 
	//do cancel
	@RequestMapping(value="process/trackingForm", params="cancel", method=RequestMethod.POST)
	public String doBookCancel(@RequestParam("tn") String tn, String returnTo, Model model, Principal principal) {
		bookService.checkAndReleaseBookLock(tn, principal.getName());//ignore any return error msg since it is just a cancel (ie even if admin unlocked it, it is ok)
		
		//do nothing
		
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		return "redirect:trackingForm?read&tn=" + tn; //redirect - guard against refresh-multi-updates and also update displayed url
	}
	 
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	

	public String getTrackingFormTitle(String returnTo, Locale locale){
	
		if("/process/waitingForFiles".equals(returnTo))
			return messageSource.getMessage("process.waitingforFiles", null, locale);
		else if("/process/titleCheck".equals(returnTo))
			return messageSource.getMessage("process.title", null, locale);
		else if("/process/titleCheckInProgress".equals(returnTo))
			return messageSource.getMessage("process.titleProgress", null, locale);
		else if("/process/ocrReady".equals(returnTo))
			return messageSource.getMessage("process.import", null, locale);
		else if("/process/ocrInProgress".equals(returnTo))
			return messageSource.getMessage("process.ocrInProgress", null, locale);
		else if("/process/pdfDownload".equals(returnTo))
			return messageSource.getMessage("process.pdfDownload", null, locale);
		else if("/process/ocrPdf".equals(returnTo))
			return messageSource.getMessage("process.ocrPdf", null, locale);
		else if("/process/ocrPdfInProgress".equals(returnTo))
			return messageSource.getMessage("process.ocrPdfInProgress", null, locale);
		else if("/process/processProblems".equals(returnTo))
			return messageSource.getMessage("process.problems", null, locale);
		else
			return messageSource.getMessage("scan.pageTitle.trackingForm", null, locale);
	  
	}
	
}