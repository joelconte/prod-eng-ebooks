/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.process.controller;

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

@Controller("processMiscController")
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
	
	@RequestMapping(value="process/waitingForFiles", method=RequestMethod.GET)
	public String getWaitingForFiles( HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		/*if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();*/
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("ocrSite", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("filesSentToOrem", null, locale));
		labels.add(messageSource.getMessage("imagesReceived", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.waitingForFiles", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getProcessWaitingForFilesTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		

		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteTnsFilesReceived", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("overlayAction", "doTnListReceivedFiles");
		
		return "process/miscButtonAndTableForm";
	}
	
	//do processing of pasted TNs from Waiting for Files to be Received page
	@RequestMapping(value="process/doTnListReceivedFiles", method=RequestMethod.POST)
	public String doInsertTnsReceivedNotesPost(HttpServletRequest req, Principal principal, String doUpdates, String button, String tnData, Model model,  Locale locale) {
	
		if(button.equals("save")) {
			List<String> l = bookService.parseExcelDataCol1(tnData);
			//String tns = bookService.generateQuotedListString(l);
			
			List<List> readyQueue = bookService.getProcessWaitingForFilesTnsInfo(""); 
			Object notInTnListArray[] = getTnsListsEtc(readyQueue, l); //get list of tns not in the ready to scan queue
			String notInTnList = (String) notInTnListArray[0];
			String inTnList = (String) notInTnListArray[1];
			List<String> inTnListList = (List<String>) notInTnListArray[2];
			if(notInTnList != "" && doUpdates == null) {
				//redisplay page with TnList msg
				model.addAttribute("notTnsInfo", notInTnList); 
				model.addAttribute("pastedData", tnData); 
				return getWaitingForFiles(req, model, locale);//forward request but first pass in tn list to show user
			}else if(inTnList != "" ) {
				
				//String tns = bookService.generateQuotedListString(inTnListList);
				bookService.updateBooksFilesReceived(inTnList);
				//bookService.insertBatch("TF_RECEIVED_IMAGES_ENTRY", new String[]{"TN", "NOTES_FROM_SITE"}, new int[] {Types.VARCHAR, Types.VARCHAR}, rows); 
			}
		}
		return "redirect:waitingForFiles"; //redirect - guard against refresh-multi-updates and also update displayed url
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
				 notInReturnList += ",\n '" + tn + "'"; //added newline for ROS so list is vertical 
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
	 
	 
	@RequestMapping(value="process/titleCheck", method=RequestMethod.GET)
	public String getTitleCheck( Principal principal, HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("ocrSite", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("location", null, locale));
		labels.add(messageSource.getMessage("imagesReceived", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.titleCheck", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getProcessTitleCheckTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
				
		return "process/miscButtonAndTableForm";
	}
	
	 
	@RequestMapping(value="process/titleCheckInProgress", method=RequestMethod.GET)
	public String getTitleCheckInProgress(Principal principal,  HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("ocrSite", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("auditingBy", null, locale));
		labels.add(messageSource.getMessage("startDate", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.titleCheckInProgress", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getProcessTitleCheckInProgressTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "process/miscButtonAndTableForm";
	}

	@RequestMapping(value="process/ocrReady", method=RequestMethod.GET)
	public String getOcrReady(Principal principal,  HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("ocrSite", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("fileName", null, locale));
		labels.add(messageSource.getMessage("backupHardrive", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("importedBy", null, locale));
		labels.add(messageSource.getMessage("dateImported", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.readyToImport", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getProcessOcrReadyTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("startSelectedOcr");
		details.add(messageSource.getMessage("startSelectedOcr", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		return "process/miscButtonAndTableFormWithCheckbox";
	}
	@RequestMapping(value="process/ocrReady", method=RequestMethod.POST)
	public String getOcrReadyPost( HttpServletRequest req, String button,  Model model, Principal principal, Locale locale) {
		if ("startSelectedOcr".equals(button)) {
			Map<String, String[]> parameters = req.getParameterMap();
			Set<String> keys = parameters.keySet();

			List<String> tnList = new ArrayList();
			for (String k : keys) {
				if (k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if (val != null && val.length >= 1)
						tnList.add(val[0]);
				}
			}
			String msg = bookService.markStartedOcr(tnList, principal.getName());
			if(msg != null) {
				model.addAttribute("infoMsg", msg);
			}
			 
		}
		return getOcrReady(principal, req, model, locale);
		//return "redirect:ocrReady";
	}
	

	@RequestMapping(value="process/ocrInProgress", method=RequestMethod.GET)
	public String getOcrInProgress( Principal principal, HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("ocrSite", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("fileName", null, locale));
		labels.add(messageSource.getMessage("backupHardrive", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("importedBy", null, locale));
		labels.add(messageSource.getMessage("dateImported", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.ocrInProgress", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getProcessOcrInProgressTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("completeSelectedOcr");
		details.add(messageSource.getMessage("completeSelectedOcr", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		return "process/miscButtonAndTableFormWithCheckbox";
	}
	
	@RequestMapping(value="process/ocrInProgress", method=RequestMethod.POST)
	public String getOcrInProgressPost( HttpServletRequest req, String button,  Model model, Principal principal, Locale locale) {
		if ("completeSelectedOcr".equals(button)) {
			Map<String, String[]> parameters = req.getParameterMap();
			Set<String> keys = parameters.keySet();

			List<String> tnList = new ArrayList();
			for (String k : keys) {
				if (k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if (val != null && val.length >= 1)
						tnList.add(val[0]);
				}
			}
			String msg = bookService.markCompleteOcr(tnList);
			if(msg != null) {
				model.addAttribute("infoMsg", msg);
			}
			 
		}
		return getOcrInProgress(principal, req, model, locale);
		//return "redirect:ocrInProgress";
	}
	
	@RequestMapping(value="process/pdfDownload", method=RequestMethod.GET)
	public String getPdfDownload( Principal principal, HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("ocrSite", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("fileName", null, locale));
		labels.add(messageSource.getMessage("backupHardrive", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("importedBy", null, locale));
		labels.add(messageSource.getMessage("dateImported", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.pdfDownload", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getProcessPdfDownloadTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url

		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("completeSelectedPdf");
		details.add(messageSource.getMessage("completeSelectedPdf", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		return "process/miscButtonAndTableFormWithCheckbox";
	}

	@RequestMapping(value="process/pdfDownload", method=RequestMethod.POST)
	public String getPdfDownloadPost( HttpServletRequest req, String button,  Model model, Principal principal, Locale locale) {
		if ("completeSelectedPdf".equals(button)) {
			Map<String, String[]> parameters = req.getParameterMap();
			Set<String> keys = parameters.keySet();

			List<String> tnList = new ArrayList();
			for (String k : keys) {
				if (k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if (val != null && val.length >= 1)
						tnList.add(val[0]);
				}
			}
			String msg = bookService.markCompletePdfDownload(tnList, principal.getName());
			if(msg != null) {
				model.addAttribute("infoMsg", msg);
			}
			 
		}
		return getPdfDownload(principal, req, model, locale);
		//return "redirect:pdfDownload";
	}

	@RequestMapping(value="process/ocrPdf", method=RequestMethod.GET)
	public String getOcrPdf( Principal principal, HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		labels.add(messageSource.getMessage("ocrSite", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("fileName", null, locale));
		labels.add(messageSource.getMessage("dateImported", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.ocrPdf", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getProcessPdfTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
	
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "process/miscButtonAndTableForm";
	}


	@RequestMapping(value="process/ocrPdfInProgress", method=RequestMethod.GET)
	public String getOcrPdfInProgress( Principal principal, HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("ocrSite", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("processingBy", null, locale));
		labels.add(messageSource.getMessage("startDate", null, locale));
		labels.add(messageSource.getMessage("numImages", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.ocrPdfInProcess", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getProcessPdfInProgressTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "process/miscButtonAndTableForm";
	}

	@RequestMapping(value="process/processProblems", method=RequestMethod.GET)
	public String getProcessProblems(Principal principal, HttpServletRequest req, Model model, Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		String location = req.getParameter("site"); //from dropdown on page
		if(location == null)
			location = bookService.getUser(principal.getName()).getPrimaryLocation();
		model.addAttribute("location", location);
		
		//String location = bookService.getUser(principal.getName()).getPrimaryLocation();
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("step", null, locale));
		labels.add(messageSource.getMessage("ocrSite", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("problemStatus", null, locale));
		labels.add(messageSource.getMessage("problemReason", null, locale));
		labels.add(messageSource.getMessage("problemText", null, locale));
		labels.add(messageSource.getMessage("problemDate", null, locale));
		labels.add(messageSource.getMessage("problemInitials", null, locale));		
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("solutionOwner", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("process.pageTitle.problems", null, locale) + " (" + ((location==null||location=="")?"All Locations":location) + ")");
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getProcessProblemTnsInfo(location)); 
		model.addAttribute("allLocations", bookService.getAllSites());
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("exportButton");
		details.add(messageSource.getMessage("createExcelFile", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);

		return "process/miscButtonAndTableForm";
	}
	
}