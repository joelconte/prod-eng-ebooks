/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.scan.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import org.familysearch.prodeng.model.Book;
import org.familysearch.prodeng.model.BookMetadata;
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

@Controller("scanTrackingFormController")
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
	@RequestMapping(value="scan/trackingForm", params="read", method=RequestMethod.GET)
	public String displayBookRead(@RequestParam(value = "tn", required=false) String tn, String fetchAllTns, Model model, Locale locale) {
 
		
		model.addAttribute("pageTitle", messageSource.getMessage("scan.pageTitle.trackingForm", null, locale));
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
		return "scan/trackingForm";
	} 
	 


	//show populated form - for update - used with anchor link "update" - 
	//same as below but GET for use in list of book links
	@RequestMapping(value="scan/trackingForm",  params="update", method=RequestMethod.GET)
	public String displayBookUpdate(String tn,  String returnTo, Model model, Locale locale, Principal principal ) {
		return displayBookUpdatePost(  tn, returnTo, model, locale, principal);
	} 
	
	//show populated form - for update - used with button "update"
	@RequestMapping(value="scan/trackingForm", params="update", method=RequestMethod.POST)
	public String displayBookUpdatePost(  String tn,  String returnTo, Model model, Locale locale, Principal principal ) {
		//messageSource.getMessage("scan.pageTitle.trackingForm", null, locale);
		//messageSource.getMessage("x", null, locale);
		model.addAttribute("pageTitle", getTrackingFormTitle(returnTo, locale));//messageSource.getMessage("scan.pageTitle.trackingForm", null, locale));
 
		String failLockMsg = bookService.getBookLock(tn, principal.getName());
		if(failLockMsg != null) {
			model.addAttribute("bookErrorMessage", messageSource.getMessage("trackingForm.error.lockFail", null, locale) + "\n\n" + failLockMsg);
			return "errors/generalError";
		}
		
		model.addAttribute("mode", "update"); 
		model.addAttribute("book", bookService.getBook(tn)); 
		model.addAttribute("allSites", bookService.getAllSites()); 
		model.addAttribute("allScanSitesIncludingInactive", bookService.getAllScanSitesIncludingInactive());
		model.addAttribute("ocrSites", bookService.getAllOcrSites()); 
		model.addAttribute("allPropertyRights", bookService.getAllPropertyRights()); 
		model.addAttribute("allPublicationTypes", bookService.getAllPublicationTypes()); 
		model.addAttribute("allLanguages", bookService.getAllLanguageIds()); 
		model.addAttribute("problemOpenList", bookService.getBookOpenProblems(tn));
		model.addAttribute("problemClosedList", bookService.getBookClosedProblems(tn));
		model.addAttribute("userLocation",   bookService.getUser(principal.getName()).getPrimaryLocation());
		
		return "scan/trackingForm";
	} 
	 
	

	//do update
	@RequestMapping(value="scan/trackingForm", params="saveAndClose", method=RequestMethod.POST)
	public String doBookSaveAndClose(Book book, String tn, String returnTo,  Model model, Locale locale, Principal principal ) {
		String failReleaseLockMsg = bookService.checkAndReleaseBookLock(tn, principal.getName()); 
		if(failReleaseLockMsg != null) {
			model.addAttribute("bookErrorMessage", failReleaseLockMsg);
			return "errors/generalError";
			//return "process/trackingForm";
		}
		
		//save book object which was linked in displayBookUpdatePost and form modelAttribute attr
		//get scanned number of pages as initial value for num of pages when "sent to Orem" is set for first time
		Book oldBook = bookService.getBook(tn);
		boolean transitionScanToProcess = bookService.isTransitionScanToProcess(book, oldBook, tn);
		
		if(transitionScanToProcess) {
			//prepare method will populate new num of pages
			book = bookService.prepareBookForScanToProcessUpdate(book, oldBook, tn); 
			
			BookMetadata bookMetaData = bookService.getBookMetadataFromBookTable(tn);//returns empty BookMetadata object if non-existing in table
			//if metadata is empty then this is a book that was inserted before web trackingform or it is an error
			//todo: in future what if book metadata is deleted and book is sent back to scan?  
			//Is this an error or do we just force admin to insert metadata into kofax sqlserver manually? yes I think.  Actually now metadata row can be released and the book will be updated rather than being created as a new row.
			//aug-2013 I changed my mind to get metadata from book table since all data is now put in it.  So metadata rows can even be deleted after put into scan.
			if(bookMetaData.isTitlenoSet()==true) {
				ocrService.insertOcrBookMetadata(bookMetaData);//propagate metadata to ocr db (kofax)
				java.util.Date date= new java.util.Date();
				book.setMetadataComplete(new Timestamp(date.getTime()));//current timestamp (in future could create 99999 timestamp flag and then use sql current_timestamp
			}else {
				//don't do anything since it may already be set manually on old pre-webapp books.
				//book.setMetadataComplete(metadataComplete);
			}
			
		}
		
		bookService.updateBook(principal.getName(), book);
	 
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		
		//tn in url cannot contain &
		if(tn.contains("&")) {
			tn = tn.replace("&", "%26");
		}
		
		return "redirect:trackingForm?read&tn=" + tn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do update
	@RequestMapping(value="scan/trackingForm", params="save", method=RequestMethod.POST)
	public String doBookSave(Book book, String tn, String returnTo,  Model model, Principal principal ) {
		String failReleaseLockMsg = bookService.checkAndReleaseBookLock(tn, principal.getName()); 
		if(failReleaseLockMsg != null) {
			model.addAttribute("bookErrorMessage", failReleaseLockMsg);
			return "errors/generalError";
			//return "process/trackingForm";
		}
		
	
		//save book object which was linked in displayBookUpdatePost and form modelAttribute attr
		//get scanned number of pages as initial value for num of pages when "sent to Orem" is set for first time
		Book oldBook = bookService.getBook(tn);
		boolean transitionScanToProcess = bookService.isTransitionScanToProcess(book, oldBook, tn);
		
		if(transitionScanToProcess) {
			//prepare method will populate new num of pages, batchclass, etc
			book = bookService.prepareBookForScanToProcessUpdate(book, oldBook, tn); 
			
			BookMetadata bookMetaData = bookService.getBookMetadataFromBookTable(tn);//returns empty BookMetadata object if non-existing in table
			//if metadata is empty then this is a book that was inserted before web trackingform or it is an error
			//todo: in future what if book metadata is deleted and book is sent back to scan?  
			//Is this an error or do we just force admin to insert metadata into kofax sqlserver manually? yes I think.  Actually now metadata row can be released and the book will be updated rather than being created as a new row.
			//aug-2013 I changed my mind to get metadata from book table since all data is now put in it.  So metadata rows can even be deleted after put into scan.
			if(bookMetaData.isTitlenoSet()==true) {
				ocrService.insertOcrBookMetadata(bookMetaData);//propagate metadata to ocr db (kofax)
				java.util.Date date= new java.util.Date();
				book.setMetadataComplete(new Timestamp(date.getTime()));//current timestamp (in future could create 99999 timestamp flag and then use sql current_timestamp
			}else {
				//don't do anything since it may already be set manually on old pre-webapp books.
				//book.setMetadataComplete(metadataComplete);
			}
			
		}
		
		bookService.updateBook(principal.getName(), book);
	 
		//tn in url cannot contain &
		if(tn.contains("&")) {
			tn = tn.replace("&", "%26");
		}
		
		if(returnTo != null && returnTo != "") 
			return "redirect:trackingForm?update&tn=" + tn + "&returnTo=" + returnTo; //keep on same page with returnTo parm added again
		
		return "redirect:trackingForm?update&tn=" + tn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do cancel
	@RequestMapping(value="scan/trackingForm", params="cancel", method=RequestMethod.POST)
	public String doBookCancel(@RequestParam("tn") String tn, String returnTo, Model model, Principal principal ) {
		bookService.checkAndReleaseBookLock(tn, principal.getName());//ignore any return error msg since it is just a cancel (ie even if admin unlocked it, it is ok)
		
		//do nothing
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		
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
	
	public String getTrackingFormTitle(String returnTo, Locale locale){
		if("/scan/scanReady".equals(returnTo))
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
		else
			return messageSource.getMessage("scan.pageTitle.trackingForm", null, locale);
	  
	}
}