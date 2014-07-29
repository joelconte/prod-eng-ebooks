/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.metadata.controller;

import java.sql.Timestamp;
import java.util.Locale;

import org.familysearch.prodeng.model.BookMetadata;
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

@Controller("metadataFormController")
public class MetadataFormController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	public MetadataFormController(BookService bookService) {
		this.bookService = bookService;
	}
	 
	 
	
	///////////tracking form displayed when checking TN of metadata already in trackingform BOOK db////////
	//show populated form - for read (tn can be null and will display empty fields)
	@RequestMapping(value="metadata/trackingForm", params="read", method=RequestMethod.GET)
	public String displayBookRead(@RequestParam(value = "tn", required=false) String tn, String fetchAllTns, Model model, Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.trackingForm", null, locale));
		model.addAttribute("mode", "read"); 
		if(fetchAllTns != null && fetchAllTns.equals("true")) {
			model.addAttribute("allTns", bookService.getAllTns()); 
		}
		model.addAttribute("book", bookService.getBook(tn)); 
		model.addAttribute("tn", tn);
		model.addAttribute("problemOpenList", bookService.getBookOpenProblems(tn));
		model.addAttribute("problemClosedList", bookService.getBookClosedProblems(tn));
		
		return "metadata/trackingForm";
	} 
	 

	///////////metadata trackingform start///////
	
	//show populated form - for read (tn can be null and will display empty fields) (tn is in url param)
	@RequestMapping(value="metadata/metadataForm", params="read", method=RequestMethod.GET)
	public String displayBookMetadataRead(@RequestParam(value = "titleno", required=false) String titleno, String fetchAllTns, Model model,  Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.metadataForm", null, locale));
		model.addAttribute("mode", "read"); 
		if(fetchAllTns != null && fetchAllTns.equals("true")) {
			model.addAttribute("allTns", bookService.getAllTnsMetadata()); 
		}
		model.addAttribute("book", bookService.getBookMetadata(titleno)); 
		model.addAttribute("titleno", titleno);
		return "metadata/metadataForm";
	} 
	

	//show populated form - for update - used with anchor link "update" - 
	//same as below but GET for use in list of book links
	@RequestMapping(value="metadata/metadataForm",  params="update", method=RequestMethod.GET)
	public String displayBookUpdate(String titleno, Model model,  Locale locale) {
		return displayBookUpdatePost(titleno, model, locale);
	} 
 	
	//show populated form - for update - used with button "update"
	@RequestMapping(value="metadata/metadataForm", params="update", method=RequestMethod.POST)
	public String displayBookUpdatePost(String titleno, Model model,  Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.metadataForm", null, locale));
		model.addAttribute("mode", "update"); 
		model.addAttribute("allTns", bookService.getAllTnsMetadata()); 
		model.addAttribute("book", bookService.getBookMetadata(titleno)); 
		model.addAttribute("allSites", bookService.getAllSites()); 
		model.addAttribute("allLanguages", bookService.getAllLanguageIds()); 
		return "metadata/metadataForm";
	} 
	 

	//do update
	@RequestMapping(value="metadata/metadataForm", params="save", method=RequestMethod.POST)
	public String doBookSave(BookMetadata book, String titleno, String tnOriginal, String returnTo, Model model) {
		//save book object which was linked in displayBookUpdatePost and form modelAttribute attr

		bookService.updateBookMetadata(book, tnOriginal); //tn param can be updated in metadata also
		
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		return "redirect:metadataForm?read&titleno=" + titleno; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do cancel
	@RequestMapping(value="metadata/metadataForm", params="cancel", method=RequestMethod.POST)
	public String doBookCancel(@RequestParam("titleno") String titleno,  String returnTo, Model model) {
		//do nothing
		
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		return "redirect:metadataForm?read&titleno=" + titleno; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	

	///////////metadata trackingform end///////
	 
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");BATCH_PATTERN2
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
 
	
}