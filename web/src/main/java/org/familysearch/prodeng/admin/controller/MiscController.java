/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

@Controller("adminMiscController")
public class MiscController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public MiscController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value="admin/adminProblems", method=RequestMethod.GET)
	public String getadminProblems(Model model, Locale locale) {
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("step", null, locale));
		labels.add(messageSource.getMessage("problemStatus", null, locale));
		labels.add(messageSource.getMessage("problemReason", null, locale));
		labels.add(messageSource.getMessage("problemText", null, locale));	
		labels.add(messageSource.getMessage("problemDate", null, locale));
		labels.add(messageSource.getMessage("problemInitials", null, locale));		
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("scanDate", null, locale));
		labels.add(messageSource.getMessage("sentToOcr", null, locale));
		labels.add(messageSource.getMessage("solutionOwner", null, locale));
 
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.problems", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getAdminProblemTnsInfo()); 

		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "admin/miscButtonAndTableForm";
	} 

	@RequestMapping(value="admin/catalogProblems", method=RequestMethod.GET)
	public String getCatalogProblems(Model model, Locale locale) {
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("step", null, locale));
		labels.add(messageSource.getMessage("problemStatus", null, locale));
		labels.add(messageSource.getMessage("problemReason", null, locale));
		labels.add(messageSource.getMessage("problemText", null, locale));	
		labels.add(messageSource.getMessage("problemDate", null, locale));
		labels.add(messageSource.getMessage("problemInitials", null, locale));		
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("scanDate", null, locale));
		labels.add(messageSource.getMessage("sentToOcr", null, locale));
		labels.add(messageSource.getMessage("solutionOwner", null, locale));
 
		model.addAttribute("pageTitle", messageSource.getMessage("catalog.pageTitle.problems", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getCatalogProblemTnsInfo()); 
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "admin/miscButtonAndTableForm";
	}
	
	////////////receivedNotes start//////////////
	@RequestMapping(value="admin/receivedNotes", method=RequestMethod.GET)
	public String getReceivedNotes(Model model, Locale locale) {
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("notes", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.receivedNotes", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getAdminReceivedNotesTnsInfo()); 
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteExcel", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkTns");
		details.add(messageSource.getMessage("checkTns", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("otherNotes");
		details.add(messageSource.getMessage("otherNotes", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("move");
		details.add(messageSource.getMessage("move", null, locale));	
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("cancelAndDelete");
		details.add(messageSource.getMessage("cancelAndDelete", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "receivedNotes");
		model.addAttribute("overlayAction", "doReceivedNotesInsertTns");
		return "admin/miscButtonAndTableForm";
	}
	
	@RequestMapping(value="admin/receivedNotes", method=RequestMethod.POST)
	public String getReceivedNotesPost(String button, Model model, Locale locale) {
		if("checkTns".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.receivedImagesWithoutMatchingBooks", null, locale));
			model.addAttribute("colLabels", labels);
			model.addAttribute("allTnsInfo", bookService.getAdminReceivedImagesWithoutMatchingBooksTnsInfo()); 
			return "admin/miscBookList";
		}else if("otherNotes".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("notesFromSite", null, locale));
			labels.add(messageSource.getMessage("problemText", null, locale));
			labels.add(messageSource.getMessage("problemDate", null, locale));
			labels.add(messageSource.getMessage("remarksFromScanCenter", null, locale));
			labels.add(messageSource.getMessage("filesReceivedByOrem", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.receivedImagesWithDateAlreadyAdded", null, locale));
			model.addAttribute("colLabels", labels);
			model.addAttribute("allTnsInfo", bookService.getAdminReceivedImagesWithDateAlreadyTnsInfo()); 
			return "admin/miscBookList";
		}else if("move".equals(button)) {
			bookService.updateReceivedImages();
			bookService.deleteReceivedImagesEntry();
			return "redirect:receivedNotes";  //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if("cancelAndDelete".equals(button)) {
			 bookService.deleteReceivedImagesEntry();
			 return "redirect:switchboard"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}

		//should not happen
		return "redirect:receivedNotes"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	//do insert of pasted tn data for use in receivedNotes page
	@RequestMapping(value="admin/doReceivedNotesInsertTns", method=RequestMethod.POST)
	public String doInsertTnsReceivedNotesPost(String button, String tnData, Model model) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(tnData, -1);
			bookService.insertBatch("TF_RECEIVED_IMAGES_ENTRY", new String[]{"TN", "NOTES_FROM_SITE"}, new int[] {Types.VARCHAR, Types.VARCHAR}, rows); 
		}
		return "redirect:receivedNotes"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
	////////////receivedNotes end/////////////


	
	@RequestMapping(value="admin/pdfDateNoReleaseDate", method=RequestMethod.GET)
	public String getPdfDateNoReleaseDate(Model model, Locale locale) {
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("pdfReady", null, locale));
		labels.add(messageSource.getMessage("fileName", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));		
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("owningInstitution", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.pdfDateNoReleaseDate", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getAdminPdfDateNoReleaseDateTnsInfo()); 
		 

		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		 
		
		return "admin/miscButtonAndTableForm";
	}
	

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
 
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
}