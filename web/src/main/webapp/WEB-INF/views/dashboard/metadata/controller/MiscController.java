/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.metadata.controller;

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

@Controller("metadataMiscController")
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
	


	////////////NewBooks metadata start/////////////
	
	@RequestMapping(value="metadata/metadataNewBooks", method=RequestMethod.GET)
	public String getMetadataNewBooks(HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("subject", null, locale));
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNumber", null, locale));
		labels.add(messageSource.getMessage("filmNumber", null, locale));
		labels.add(messageSource.getMessage("pages", null, locale));
		labels.add(messageSource.getMessage("summary", null, locale));
		labels.add(messageSource.getMessage("dgsNumber", null, locale));
		labels.add(messageSource.getMessage("language", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("dateOriginal", null, locale));
		labels.add(messageSource.getMessage("publisherOriginal", null, locale));

		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.newBooks", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getMetadataNewBooksTnsInfo());  
		model.addAttribute("allSites", bookService.getAllSites()); 
		model.addAttribute("tnColumnNumber", "3");//column where tn is located for creating url
	 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteExcel", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkTns");
		details.add(messageSource.getMessage("checkDupeTns", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("deleteSelected");
		details.add(messageSource.getMessage("deleteSelectedMetadata", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("deleteAllNewBooks");
		details.add(messageSource.getMessage("deleteAllNewBooks", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "metadataNewBooks");
		model.addAttribute("overlayAction", "doMetadataNewBooksInsertTns");
		return "metadata/miscButtonAndTableFormWithCheckbox";
	}

	@RequestMapping(value="metadata/metadataNewBooks", method=RequestMethod.POST)
	public String getMetadataNewBooksPost(String button, HttpServletRequest request,  Model model, Locale locale) {
		if("checkTns".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("title", null, locale));
			labels.add(messageSource.getMessage("author", null, locale));
			labels.add(messageSource.getMessage("pages", null, locale));
			labels.add(messageSource.getMessage("scanningLocation", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.newBooksAlreadyInTrackingFormDatabase", null, locale));
			model.addAttribute("colLabels", labels); 
			model.addAttribute("allTnsInfo", bookService.getMetadataNewBooksAlreadyInTrackingFormDatabaseTnsInfo()); 
			model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url
			 
			//buttons
			List<List<String>> buttons = new ArrayList<List<String>>();
			List<String> details = new ArrayList<String>();
			details.add("deleteSelected2");
			details.add(messageSource.getMessage("deleteSelectedMetadata", null, locale));
			buttons.add(details); 
			/*details = new ArrayList<String>();
			details.add("deleteAllMetadataBooks");
			details.add(messageSource.getMessage("deleteAllMetadataBooks", null, locale));
			buttons.add(details); */
			model.addAttribute("buttons", buttons);
			

			//form actions
			model.addAttribute("buttonsAction", "metadataNewBooks");
		 
			return "metadata/miscButtonAndTableFormWithCheckbox";
		}else if(button.equals("deleteAllNewBooks")) {
			 
			bookService.deleteAllNewMetadata();
			
		}else if(button.equals("deleteSelected") || button.equals("deleteSelected2")) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String tnList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						tnList = tnList + ", " + "'" + val[0] + "'";
				}
			}
			if(tnList.length()>1)
				tnList = tnList.substring(1);//remove starting comma
			
			bookService.deleteSelectedMetadata(tnList);
			if(button.equals("deleteSelected2"))
				return  getMetadataNewBooksPost("checkTns", request, model, locale);//fake redirect, since this is a post of when a button "checkTns" is pressed, not a get
		}
 
		//should not happen
		return "redirect:metadataNewBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="metadata/doMetadataNewBooksInsertTns", method=RequestMethod.POST)
	public String doInsertTnsMetadataNewBooksPost(HttpServletRequest req, String doUpdates, String button, String site, String tnData, Principal principal,  Model model, Locale locale) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(tnData);
			String tnList = "";
			for(List<String> r : rows) {
				tnList += ", '" + r.get(3) + "'";
			}
			tnList = tnList.substring(2);
			
			String dupTnList = bookService.getDuplicateTnsInMetadata(tnList); //get list of tns already in bookmetadata col=titleno 3rd(0based) elem in rnData rows
			if(dupTnList != "" && doUpdates == null) {
				//redisplay page with dupTnList msg
				
				//model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.alreadyExistMetadata", null, locale) + "\n" + dupTnList);
				//return "errors/generalError";
				model.addAttribute("dupeTnsInfo", dupTnList); 
				model.addAttribute("site", site); //requesting_location 
				model.addAttribute("pastedData", tnData); 
				return getMetadataNewBooks(req, model, locale);//forward request but first pass in dupe list to show user
			}else if(dupTnList != "" && doUpdates != null) {
				//allow both updates and inserts
				//delete old data and then re-insert
				bookService.deleteSelectedMetadata(dupTnList);
			} 
			addMiscColumns(rows, principal.getName(), site); //site is requesting_Location
	
			bookService.insertBatch("BOOKMETADATA", new String[]{"title", "author", "subject", "titleno", "callno", "partner_lib_callno", "filmno", "pages", "summary", "dgsno", "language", "requesting_location", "scanning_location", "record_number", "date_original", "publisher_original", "current_timestamp_date_added", "metadata_adder"}, new int[] {Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR}, rows); 
		}
		return "redirect:metadataNewBooks"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 

	public void addMiscColumns(List<List<String>> rows, String userId, String requestingLocation) {
		for(List<String> r : rows) {
			//current_timstamp values are generated in the insertBatch(), so no value should be added here 
			r.add(11, requestingLocation);
			r.add("current_timestamp");//dummy to flag current time insertBatch
			r.add(userId);//userid in column
		}
	}

	////////////NewBooks metadata end/////////////


	///////////metadata check in progress start/////////////
	
	@RequestMapping(value="metadata/metadataCheckInProgress", method=RequestMethod.GET)
	public String getMetadataCheckInProgress(HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("subject", null, locale));
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNumber", null, locale));
		labels.add(messageSource.getMessage("filmNumber", null, locale));
		labels.add(messageSource.getMessage("pages", null, locale));
		labels.add(messageSource.getMessage("summary", null, locale));
		labels.add(messageSource.getMessage("dgsNumber", null, locale));
		labels.add(messageSource.getMessage("language", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("dateOriginal", null, locale));
		labels.add(messageSource.getMessage("publisherOriginal", null, locale));

		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.checkInProgress", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getMetadataNewBooksTnsInfo());  
		model.addAttribute("tnColumnNumber", "3");//column where tn is located for creating url
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("selectedMetadataCheckComplete");
		details.add(messageSource.getMessage("selectedMetadataCheckComplete", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("allMetadataCheckComplete");
		details.add(messageSource.getMessage("allMetadataCheckComplete", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("deleteSelected");
		details.add(messageSource.getMessage("deleteSelectedMetadata", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "metadataCheckInProgress");
		return "metadata/miscButtonAndTableFormWithCheckbox";
		
		 
	}

	@RequestMapping(value="metadata/metadataCheckInProgress", method=RequestMethod.POST)
	public String getMetadataCheckInProgressPost(String button, HttpServletRequest request,  Principal principal, Model model, Locale locale) {
		if("selectedMetadataCheckComplete".equals(button)) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String tnList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						tnList = tnList + ", " + "'" + val[0] + "'";
				}
			}
			if(tnList.length()>1)
				tnList = tnList.substring(1);//remove starting comma
			
			bookService.checkCompleteSelectedMetadata(tnList, principal.getName());
		
			return "redirect:metadataCheckInProgress"; //redirect - guard against refresh-multi-updates and also update displayed url
		}else if(button.equals("allMetadataCheckComplete")) {
			 
			bookService.checkCompleteAllMetadata(principal.getName());
			return "redirect:metadataCheckInProgress"; //redirect - guard against refresh-multi-updates and also update displayed url
		}else if(button.equals("deleteSelected")) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String tnList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						tnList = tnList + ", " + "'" + val[0] + "'";
				}
			}
			if(tnList.length()>1)
				tnList = tnList.substring(1);//remove starting comma
			
			bookService.deleteSelectedMetadata(tnList);
			return "redirect:metadataCheckInProgress"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}
 
		//should not happen
		return "redirect:metadataCheckInProgress"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	  

	///////////metadata check in progress end/////////////
	
	///////////metadata ready to send to scan start/////////////
	 
	@RequestMapping(value="metadata/metadataSendToScan", method=RequestMethod.GET)
	public String getMetadataSendToScan(HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("subject", null, locale));
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNumber", null, locale));
		labels.add(messageSource.getMessage("filmNumber", null, locale));
		labels.add(messageSource.getMessage("pages", null, locale));
		labels.add(messageSource.getMessage("summary", null, locale));
		labels.add(messageSource.getMessage("dgsNumber", null, locale));
		labels.add(messageSource.getMessage("language", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("dateOriginal", null, locale));
		labels.add(messageSource.getMessage("publisherOriginal", null, locale));

		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.readySendToScan", null, locale));
		model.addAttribute("colLabels", labels);
		List<List> md = bookService.getMetadataSendToScanTnsInfo();
		model.addAttribute("allTnsInfo", md); 
		String tnListStr = "";
		for(List r: md) {
			String tn = (String) r.get(3);
			tnListStr += ", '" + tn + "'";
		}
		if(tnListStr != "")
			tnListStr = tnListStr.substring(2);
		model.addAttribute("dupeTnsInfo", bookService.getDuplicateTnsInBook(tnListStr));   
		model.addAttribute("tnColumnNumber", "3");//column where tn is located for creating url
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("selectedMetadataSendToScan");
		details.add(messageSource.getMessage("selectedMetadataSendToScan", null, locale));
		details.add("checkForDupesSome");//!!javascript function to exec onclick
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("allMetadataSendToScan");
		details.add(messageSource.getMessage("allMetadataSendToScan", null, locale));
		details.add("checkForDupesAll");//!!javascript function to exec onclick
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("deleteSelected");
		details.add(messageSource.getMessage("deleteSelectedMetadata", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);
		 
		
		//form actions
		model.addAttribute("buttonsAction", "metadataSendToScan");
		return "metadata/miscButtonAndTableFormWithCheckbox";
		
	}

	@RequestMapping(value="metadata/metadataSendToScan", method=RequestMethod.POST)
	public String getMetadataSendToScanPost(String button, HttpServletRequest request,  Principal principal, Model model, Locale locale) {
		if("selectedMetadataSendToScan".equals(button)) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
		
			List<String> tnList = new ArrayList();
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						tnList.add(val[0]);
				}
			} 
			
			String dupListStr = bookService.sendToScanSelectedMetadata(tnList, principal.getName());
			//check for dupes already in Book table
			/*checked via js and insert/update if(dupListStr != "") {
				//display error with dupTnList
				model.addAttribute("bookErrorMessage",  messageSource.getMessage("metadata.alreadyExistBook", null, locale) + "\n" + dupListStr);
				return "errors/generalError";
			}*/
			
			return "redirect:metadataSendToScan"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if(button.equals("allMetadataSendToScan")) {
			 
			String dupListStr = bookService.sendToScanAllMetadata(principal.getName());
			//check for dupes already in Book table
			/*checked via js and insert/update 
			 	if(dupListStr != "") {
			 
				//display error with dupTnList
				model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.alreadyExistBook", null, locale) + "\n" + dupListStr);
				return "errors/generalError";
				}
			*/
			
			return "redirect:metadataSendToScan"; //redirect get - guard against refresh-multi-updates and also update displayed url
		} else if(button.equals("deleteSelected")) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String tnList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						tnList = tnList + ", " + "'" + val[0] + "'";
				}
			}
			if(tnList.length()>1)
				tnList = tnList.substring(1);//remove starting comma
			
			bookService.deleteSelectedMetadata(tnList);
			return "redirect:metadataSendToScan"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}
 
		//should not happen
		return "redirect:metadataSendToScan"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	///////////metadata ready to send to scan end/////////////
	
	///////////metadata complete and sent start/////////////
	@RequestMapping(value="metadata/metadataCompleteAndSent", method=RequestMethod.GET)
	public String getMetadataCompleteAndSentPost(  Model model, Locale locale) {
 
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("subject", null, locale));
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNumber", null, locale));
		labels.add(messageSource.getMessage("filmNumber", null, locale));
		labels.add(messageSource.getMessage("pages", null, locale));
		labels.add(messageSource.getMessage("summary", null, locale));
		labels.add(messageSource.getMessage("dgsNumber", null, locale));
		labels.add(messageSource.getMessage("language", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("dateOriginal", null, locale));
		labels.add(messageSource.getMessage("publisherOriginal", null, locale));

		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.metadataCompleteAndSent", null, locale));
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getMetadataCompleteAndSent()); 
		model.addAttribute("keyCol", 3);//use col 3 for request on TN 
		return "metadata/miscBookList";
	}
	///////////metadata complete and sent end/////////////
}