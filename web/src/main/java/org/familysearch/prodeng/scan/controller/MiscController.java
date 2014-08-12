/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.scan.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.familysearch.prodeng.model.Book;
import org.familysearch.prodeng.model.BookMetadata;
import org.familysearch.prodeng.service.BookService;
import org.familysearch.prodeng.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller("scanMiscController")
public class MiscController implements MessageSourceAware{

	private BookService bookService;
	private OcrService ocrService;
	private MessageSource messageSource;
	
	@Autowired
	public MiscController(BookService bookService,  OcrService ocrService) {
		this.bookService = bookService;
		this.ocrService = ocrService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value="scan/scanReady", method=RequestMethod.GET)
	public String getScanReady(Principal principal, HttpServletRequest req,  Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
				
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNum", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("dateEntered", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("scan.pageTitle.scanReady", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getScanScanReadyTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());

		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteTnsSkipScan", null, locale));
		details.add("doTnListSkipScan");//action
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("overlayButton");
		details.add(messageSource.getMessage("pasteTnsSkipScanAndProcess", null, locale));
		details.add("doTnListSkipScanAndProcess");//action
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		//form actions
		//move to action for each button model.addAttribute("overlayAction", "doTnListSkipScan");
		
		return "scan/miscButtonAndTableForm";
	}
	
	//do processing of pasted TNs to skip scanning
	@RequestMapping(value="scan/doTnListSkipScan", method=RequestMethod.POST)
	public String doInsertTnsSkipScanPost(HttpServletRequest req, Principal principal, String doUpdates, String button, String tnData, Model model,  Locale locale) {
		String location = bookService.getUser(principal.getName()).getPrimaryLocation();
		
		if(button.equals("save")) {
			List<String> l = bookService.parseExcelDataCol1(tnData);
			//String tns = bookService.generateQuotedListString(l);
			
			List<List> scanReadyQueue = bookService.getScanScanReadyTnsInfo(location); 
			Object notInTnListArray[] = getTnsListsEtc(scanReadyQueue, l); //get list of tns not in the ready to scan queue
			String notInTnList = (String) notInTnListArray[0];
			String inTnList = (String) notInTnListArray[1];
			List<String> inTnListList = (List<String>) notInTnListArray[2];
			if(notInTnList != "" && doUpdates == null) {
				//redisplay page with TnList msg
				model.addAttribute("notTnsInfo", notInTnList); 
				model.addAttribute("pastedData", tnData); 
				return getScanReady(principal, req, model, locale);//forward request but first pass in tn list to show user
			}else if(inTnList != "") {// && doUpdates != null) {
				/////logic to auto-update various fields that normally happen when book transitions from scan to process
				for (String tn : inTnListList) {
				    Book oldBook = bookService.getBook(tn);
				    Book book = oldBook;//this happens to work so logic is same as in trackingFormController of scan
					boolean transitionScanToProcess = true;// bookService.isTransitionScanToProcess(book, oldBook, tn);
					
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
				
					bookService.updateBook(book);
				}
				
				bookService.updateBooksSkipScan(inTnList);//sets file_sent_to_orem as flag to move on to processing
			}
		}
		return "redirect:scanReady"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
	
	 public Object[] getTnsListsEtc(List<List> scanReadyQueue, List<String> toDoTns){
		 String notInReturnList= "";
		 String inReturnList= "";
		 List<String> inReturnListList = new ArrayList<String>();
		 
		 for(String tn : toDoTns) {
			 boolean found = false;
			 for(List row : scanReadyQueue) {
				 if(tn.equals((String)row.get(0))) {
					found = true; 
				 }	  
			 }
			 if(found == false)
				 notInReturnList += ", '" + tn + "'";
			 if(found == true) {
				 inReturnList += ", '" + tn + "'";
				 inReturnListList.add(tn);
			 }
		 }
		 
		 if(notInReturnList != "")
			 notInReturnList = notInReturnList.substring(2);
		 if(inReturnList != "")
			 inReturnList = inReturnList.substring(2);
		 
		 return new Object[]{notInReturnList, inReturnList, inReturnListList};
	 }
	 
	//do processing of pasted TNs to skip scanning and kofaxing
	@RequestMapping(value="scan/doTnListSkipScanAndProcess", method=RequestMethod.POST)
	public String doInsertTnsSkipScanAndProcessPost(HttpServletRequest req, Principal principal, String doUpdates, String button, String tnData, Model model,  Locale locale) {
		String location = bookService.getUser(principal.getName()).getPrimaryLocation();
		
		if(button.equals("save")) {
			List<String> l = bookService.parseExcelDataCol1(tnData);
			//String tns = bookService.generateQuotedListString(l);
			
			List<List> scanReadyQueue = bookService.getScanScanReadyTnsInfo(location); 
			Object notInTnListArray[] = getTnsListsEtc(scanReadyQueue, l); //get list of tns not in the ready to scan queue
			String notInTnList = (String) notInTnListArray[0];
			String inTnList = (String) notInTnListArray[1];
			List<String> inTnListList = (List<String>) notInTnListArray[2];
			if(notInTnList != "" && doUpdates == null) {
				//redisplay page with TnList msg
				model.addAttribute("notTnsInfo", notInTnList); 
				model.addAttribute("pastedData", tnData); 
				return getScanReady(principal, req, model, locale);//forward request but first pass in tn list to show user
			}else if(inTnList != "" ) {//&& doUpdates != null) {
				/////logic to auto-update various fields that normally happen when book transitions from scan to process
				for (String tn : inTnListList) {
				    Book oldBook = bookService.getBook(tn);
				    Book book = oldBook;//this happens to work so logic is same as in trackingFormController of scan
					boolean transitionScanToProcess = true;// bookService.isTransitionScanToProcess(book, oldBook, tn);
					
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
				
					bookService.updateBook(book);
				}
				
				bookService.updateBooksSkipScanAndProcess(inTnList);//sets file_sent_to_orem as flag to move on to processing
			}
		}
		return "redirect:scanReady"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	@RequestMapping(value="scan/scanInProgress", method=RequestMethod.GET)
	public String getScanInProgress(Principal principal, HttpServletRequest req,  Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
	
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNum", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("scan.pageTitle.scanInProgress", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getScanScanInProgressTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "scan/miscButtonAndTableForm";
	}
	
	@RequestMapping(value="scan/auditReady", method=RequestMethod.GET)
	public String getAuditReady(Principal principal, HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);

		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNum", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("scan.pageTitle.auditReady", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getScanAuditReadyTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());

		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "scan/miscButtonAndTableForm";
	}
	 
	@RequestMapping(value="scan/auditInProgress", method=RequestMethod.GET)
	public String getAuditInProgress(Principal principal, HttpServletRequest req, Model model, Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNum", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("startDate", null, locale));
		labels.add(messageSource.getMessage("auditingBy", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("scan.pageTitle.auditInProgress", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getScanAuditInProgressTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "scan/miscButtonAndTableForm";
	}
	
	@RequestMapping(value="scan/scanProblems", method=RequestMethod.GET)
	public String getScanProblems(Principal principal, HttpServletRequest req, Model model, Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
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
		labels.add(messageSource.getMessage("solutionOwner", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("scan.pageTitle.problems", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getScanProblemTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "scan/miscButtonAndTableForm";
	}
	

	@RequestMapping(value="scan/processedReadyForOrem", method=RequestMethod.GET)
	public String getProcessedReadyForOrem(Principal principal, HttpServletRequest req, Model model, Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNum", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("sendOrem", null, locale));		
		labels.add(messageSource.getMessage("notes", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("scan.pageTitle.readySendToOrem", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getScanProcessedReadyForOremTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "scan/miscButtonAndTableForm";
	}

}