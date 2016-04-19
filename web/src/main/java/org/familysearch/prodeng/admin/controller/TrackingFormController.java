/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import org.familysearch.prodeng.model.Book;
import org.familysearch.prodeng.model.SqlTimestampPropertyEditor;
import org.familysearch.prodeng.service.BookService;
import org.familysearch.prodeng.service.OcrService;
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

@Controller("adminTrackingFormController")
public class TrackingFormController implements MessageSourceAware{
	
	private BookService bookService;
	private OcrService ocrService;
	private MessageSource messageSource;

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	public TrackingFormController(BookService bookService, OcrService ocrService) {
		this.bookService = bookService;
		this.ocrService = ocrService;
	}
	
	
	 
	//show populated form - for read (tn can be null and will display empty fields)
	@RequestMapping(value="admin/trackingForm", params="read", method=RequestMethod.GET)
	public String displayBookRead(@RequestParam(value = "tn", required=false) String tn, String fetchAllTns, Model model, Locale locale) {
		 
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.trackingForm", null, locale));
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
		return "admin/trackingForm";
	} 

 
	
	//show populated form - for update - used with button "update"
	@RequestMapping(value="admin/trackingForm", params="update", method=RequestMethod.GET)
	public String displayBookUpdateGet(String tn, Model model, Locale locale, Principal principal ) {
		return displayBookUpdatePost(tn, model, locale, principal);
	} 

	//show populated form - for update - used with button "update"
	@RequestMapping(value="admin/trackingForm", params="update", method=RequestMethod.POST)
	public String displayBookUpdatePost(String tn, Model model, Locale locale, Principal principal ) {		
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.trackingForm", null, locale));
		String failLockMsg = bookService.getBookLock(tn, principal.getName());
		if(failLockMsg != null) {
			model.addAttribute("bookErrorMessage", messageSource.getMessage("trackingform.error.lockFail", null, locale) + "\n\n" + failLockMsg);
			return "errors/generalError";
		}
		
		model.addAttribute("mode", "update"); 
		model.addAttribute("book", bookService.getBook(tn)); 
		model.addAttribute("allSites", bookService.getAllSites()); 
		model.addAttribute("ocrSites", bookService.getAllOcrSites()); 
		model.addAttribute("allScanSitesIncludingInactive", bookService.getAllScanSitesIncludingInactive());
		model.addAttribute("allPropertyRights", bookService.getAllPropertyRights()); 
		model.addAttribute("allPublicationTypes", bookService.getAllPublicationTypes()); 
		model.addAttribute("allLanguages", bookService.getAllLanguageIds()); 
		model.addAttribute("problemOpenList", bookService.getBookOpenProblems(tn));
		model.addAttribute("problemClosedList", bookService.getBookClosedProblems(tn));
		return "admin/trackingForm";
	} 
	
	//show populated form - for update - used with button "delete"
	@RequestMapping(value="admin/trackingForm", params="delete", method=RequestMethod.POST)
	public String deleteBookPost(String tn, Model model) {
		bookService.deleteBook(tn);
		ocrService.deleteOcrBookMetadata(tn);
		bookService.deleteMetadata(tn);
		return "redirect:trackingForm?read"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
	//do update
	@RequestMapping(value="admin/trackingForm", params="saveAndClose", method=RequestMethod.POST)
	public String doBookSaveAndClose(Book book, String tn, String tnOriginal, Model model, Locale locale, Principal principal ) {
		String failReleaseLockMsg = bookService.checkAndReleaseBookLock(tnOriginal, principal.getName()); 
		if(failReleaseLockMsg != null) {
			model.addAttribute("bookErrorMessage", failReleaseLockMsg);
			return "errors/generalError";
			//return "process/trackingForm";
		}
		
		//save book object which was linked in displayBookUpdatePost and form modelAttribute attr

		bookService.updateBook(book, tnOriginal); //tn param can be updated in admin also
		//tn in url cannot contain &
		if(tn.contains("&")) {
			tn = tn.replace("&", "%26");
		}
				
		return "redirect:trackingForm?read&tn=" + tn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 

	//do update
	@RequestMapping(value="admin/trackingForm", params="save", method=RequestMethod.POST)
	public String doBookSave(Book book, String tn, String tnOriginal,  Model model, Principal principal ) {
		String failReleaseLockMsg = bookService.checkAndReleaseBookLock(tnOriginal, principal.getName()); 
		if(failReleaseLockMsg != null) {
			model.addAttribute("bookErrorMessage", failReleaseLockMsg);
			return "errors/generalError";
			//return "process/trackingForm";
		}
		
		//save book object which was linked in displayBookUpdatePost and form modelAttribute attr

		bookService.updateBook(book, tnOriginal); //tn param can be updated in admin also
		
		//tn in url cannot contain &
		if(tn.contains("&")) {
			tn = tn.replace("&", "%26");
		}
		return "redirect:trackingForm?update&tn=" + tn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do cancel
	@RequestMapping(value="admin/trackingForm", params="cancel", method=RequestMethod.POST)
	public String doBookCancel(@RequestParam("tn") String tn, Model model, Principal principal ) {
		bookService.checkAndReleaseBookLock(tn, principal.getName());//ignore any return error msg since it is just a cancel (ie even if admin unlocked it, it is ok)
		
		//do nothing
		//tn in url cannot contain &
		if(tn.contains("&")) {
			tn = tn.replace("&", "%26");
		}
		return "redirect:trackingForm?read&tn=" + tn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
 
	
}