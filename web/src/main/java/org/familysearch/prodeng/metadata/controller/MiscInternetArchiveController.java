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

@Controller("miscInternetArchiveController")
public class MiscInternetArchiveController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public MiscInternetArchiveController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	////////////NewBooks metadata start/////////////
 
	@RequestMapping(value="metadata/metadataInternetArchiveNewBooks", method=RequestMethod.GET)
	public String getMetadataInternetArchiveNewBooks(HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		//title and table labels
		List<String> labels = new ArrayList<String>();
		
		labels.add(messageSource.getMessage("tn", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("priorityItem", null, locale));
		labels.add(messageSource.getMessage("withdrawn", null, locale));
		labels.add(messageSource.getMessage("digitalCopyOnly", null, locale));
		labels.add(messageSource.getMessage("mediaType", null, locale));
		labels.add(messageSource.getMessage("metadataComplete", null, locale));
		labels.add(messageSource.getMessage("batchClass", null, locale));
		labels.add(messageSource.getMessage("language", null, locale));
		labels.add(messageSource.getMessage("remarksFromScanCenter", null, locale));
		labels.add(messageSource.getMessage("remarksAboutBook", null, locale));	
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("location", null, locale));
		labels.add(messageSource.getMessage("scanCompleteDate", null, locale));
		labels.add(messageSource.getMessage("pages", null, locale));
		labels.add(messageSource.getMessage("filesReceivedByOrem", null, locale));
		labels.add(messageSource.getMessage("imageAudit", null, locale));
		labels.add(messageSource.getMessage("isStartDate", null, locale));
		labels.add(messageSource.getMessage("iaCompleteDate", null, locale));
		labels.add(messageSource.getMessage("importedBy", null, locale));
		labels.add(messageSource.getMessage("importedDate", null, locale));
		labels.add(messageSource.getMessage("pdfreviewBy", null, locale));
		labels.add(messageSource.getMessage("pdfreviewStartDate", null, locale));
		labels.add(messageSource.getMessage("pdfReady", null, locale));
		labels.add(messageSource.getMessage("dateReleased", null, locale));
		labels.add(messageSource.getMessage("compressionCode", null, locale));
		labels.add(messageSource.getMessage("loadedBy", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		labels.add(messageSource.getMessage("collection", null, locale));
		labels.add(messageSource.getMessage("dnp", null, locale));
		labels.add(messageSource.getMessage("tnChangeHistory", null, locale));
		labels.add(messageSource.getMessage("pdfOremArchivedDate", null, locale));
		labels.add(messageSource.getMessage("pdfOremDriveSerial", null, locale));
		labels.add(messageSource.getMessage("pdfOremDriveName", null, locale));
		labels.add(messageSource.getMessage("pdfCopy2ArchivedDate", null, locale));
		labels.add(messageSource.getMessage("pdfCopy2DriveSerial", null, locale));
		labels.add(messageSource.getMessage("pdfCopy2DriveName", null, locale));
		labels.add(messageSource.getMessage("tiffOremArchivedDate", null, locale));
		labels.add(messageSource.getMessage("tiffOremDriveSerial", null, locale));
		labels.add(messageSource.getMessage("tiffOremDriveName", null, locale));
		labels.add(messageSource.getMessage("tiffCopy2ArchivedDate", null, locale));
		labels.add(messageSource.getMessage("tiffCopy2DriveSerial", null, locale));
		labels.add(messageSource.getMessage("tiffCopy2DriveName", null, locale));
		labels.add(messageSource.getMessage("pdfSenttoLoad", null, locale));
		labels.add(messageSource.getMessage("site", null, locale));
		labels.add(messageSource.getMessage("url", null, locale));
		labels.add(messageSource.getMessage("pid", null, locale));
		labels.add(messageSource.getMessage("pagesOnline", null, locale));
		labels.add(messageSource.getMessage("secondaryIdentifier", null, locale));
		labels.add(messageSource.getMessage("oclcNumber", null, locale));
		labels.add(messageSource.getMessage("fhcTitle", null, locale));
		labels.add(messageSource.getMessage("fhcTn", null, locale));
		labels.add(messageSource.getMessage("owningInstitution", null, locale));
		labels.add(messageSource.getMessage("publisher", null, locale));
		 
		//////
		List<List> md = bookService.getInternetArchiveMetadataSendToScanTnsInfo();
		if(!model.containsAttribute("dupeTnsInfo")) {
			//if already dupeTnsInfo, then just pasted in data and had dupe md
		
			String tnListStr = "";
			for(List r: md) {
				String tn = (String) r.get(0);
				tnListStr += ", '" + tn + "'";
			}
			if(tnListStr != "")
				tnListStr = tnListStr.substring(2);
			model.addAttribute("dupeTnsInfo", bookService.getDuplicateTnsInBook(tnListStr));   
			 
		}
		//////

		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.newIABooks", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", md);  
		model.addAttribute("allSites", bookService.getAllSites()); 
		model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url
	 
		 
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
		details = new ArrayList<String>();
		details.add("selectedMetadataSendToScan");
		details.add(messageSource.getMessage("selectedMetadataSendToScan", null, locale));
		details.add("checkForDupesSome");//!!javascript function to exec onclick
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("allMetadataSendToScan");
		details.add(messageSource.getMessage("allMetadataSendToScan", null, locale));
		details.add("checkForDupesAll");//!!javascript function to exec onclick
		buttons.add(details);  
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "metadataInternetArchiveNewBooks");
		model.addAttribute("overlayAction", "doMetadataInternetArchiveNewBooksInsertTns");
		return "metadata/miscButtonAndTableFormWithCheckboxForIA";
	}

	@RequestMapping(value="metadata/metadataInternetArchiveNewBooks", method=RequestMethod.POST)
	public String getMetadataInternetArchiveNewBooksPost(String button, HttpServletRequest request, Principal principal,  Model model, Locale locale) {
		if("checkTns".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("title", null, locale));
			labels.add(messageSource.getMessage("author", null, locale));
			labels.add(messageSource.getMessage("pages", null, locale));
			labels.add(messageSource.getMessage("scanningLocation", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.newBooksAlreadyInTrackingFormDatabase", null, locale));
			model.addAttribute("colLabels", labels); 
			model.addAttribute("allTnsInfo", bookService.getIAMetadataNewBooksAlreadyInTrackingFormDatabaseTnsInfo()); 
			model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url
			 
			//buttons
			List<List<String>> buttons = new ArrayList<List<String>>();
			List<String> details = new ArrayList<String>();
			details.add("deleteSelected2");
			details.add(messageSource.getMessage("deleteSelectedMetadata", null, locale));
			buttons.add(details); 
			details = new ArrayList<String>();
			details.add("goBack");
			details.add(messageSource.getMessage("goBack", null, locale));
			details.add("goBackToInternetArchiveMetadata");//!!javascript function to exec onclick
			buttons.add(details); 
			/*details = new ArrayList<String>();
			details.add("deleteAllMetadataBooks");
			details.add(messageSource.getMessage("deleteAllMetadataBooks", null, locale));
			buttons.add(details); */
			model.addAttribute("buttons", buttons);
			
			//form actions
			model.addAttribute("buttonsAction", "metadataInternetArchiveNewBooks");
		 
			return "metadata/miscButtonAndTableFormWithCheckboxForIA";
		}else if(button.equals("deleteAllNewBooks")) {
			 
			bookService.deleteAllInternetArchiveNewMetadata();
			
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
			
			bookService.deleteSelectedInternetArchiveMetadata(tnList);
			if(button.equals("deleteSelected2"))
				return  getMetadataInternetArchiveNewBooksPost("checkTns", request, principal, model, locale);//fake redirect, since this is a post of when a button "checkTns" is pressed, not a get
		}else if("selectedMetadataSendToScan".equals(button)) {
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
			
			String dupListStr = bookService.sendToScanSelectedInternetArchiveMetadata(tnList, principal.getName());
			//check for dupes already in Book table
			/*checked via js and insert/update if(dupListStr != "") {
				//display error with dupTnList
				model.addAttribute("bookErrorMessage",  messageSource.getMessage("metadata.alreadyExistBook", null, locale) + "\n" + dupListStr);
				return "errors/generalError";
			}*/
			
			return "redirect:metadataInternetArchiveNewBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if(button.equals("allMetadataSendToScan")) {
			 
			String dupListStr = bookService.sendToScanAllInternetArchiveMetadata(principal.getName());
			//check for dupes already in Book table
			/*checked via js and insert/update 
			 	if(dupListStr != "") {
			 
				//display error with dupTnList
				model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.alreadyExistBook", null, locale) + "\n" + dupListStr);
				return "errors/generalError";
				}
			*/
			
			return "redirect:metadataInternetArchiveNewBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url
		} 
 
		//should not happen
		return "redirect:metadataInternetArchiveNewBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	boolean validateValueToList(List<String> valueList, String value){
		if(value == null)
			return true;
		for(String l: valueList){
			if(value.equals(l)){
				//match
				return true;
			}
		}
		return false;
	}
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="metadata/doMetadataInternetArchiveNewBooksInsertTns", method=RequestMethod.POST)
	public String doInsertTnsMetadataInternetArchiveNewBooksPost(HttpServletRequest req, String doUpdates, String button, String tnData, Principal principal,  Model model, Locale locale) {
		if(button.equals("save")) {
			List<String> langList = bookService.getAllLanguageIds();
			List<String> siteList = bookService.getAllSiteIds();
			
			List<List<String>> rows = bookService.parseExcelData(tnData, 56);//!!need to update count when add new columnds
			String tnList = "";
			for(List<String> r : rows) {
				tnList += ", '" + r.get(0) + "'";
				
				//validate some data
				//sites
				//scanned_by colN
				//language is colK
				//site is col AU
				//owning_inst  col BC
				boolean v1 = validateValueToList(siteList, r.get(13));//N
				boolean v2 = validateValueToList(siteList, r.get(46));//AU
				boolean v3 = validateValueToList(siteList, r.get(54));//BC
				boolean v4 = validateValueToList(langList, r.get(10));
				
				if(v1 == false) {
					model.addAttribute("bookErrorMessage", messageSource.getMessage("validationError1" , null, locale) +  "  " +  r.get(13));
					return "errors/generalError";
				}else if(v2 == false) {
					model.addAttribute("bookErrorMessage", messageSource.getMessage("validationError1" , null, locale)+  "  " +  r.get(46));
					return "errors/generalError";
				}else if(v3 == false) {
					model.addAttribute("bookErrorMessage", messageSource.getMessage("validationError1", null, locale) +  "  " +  r.get(54));
					return "errors/generalError";
				}else if(v4 == false) {
					model.addAttribute("bookErrorMessage", messageSource.getMessage("validationError2" , null, locale)+  "  " +  r.get(10));
					return "errors/generalError";
				}
			}
			tnList = tnList.substring(2);
			
			String dupTnList = bookService.getDuplicateTnsInInternetArchiveMetadata(tnList); //get list of tns already in bookmetadata col=titleno 3rd(0based) elem in rnData rows
			if(dupTnList != "" && doUpdates == null) {
				//redisplay page with dupTnList msg
				
				//model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.alreadyExistMetadata", null, locale) + "\n" + dupTnList);
				//return "errors/generalError";
				model.addAttribute("dupeTnsInfo", dupTnList);
				model.addAttribute("pastedData", tnData); 
				return getMetadataInternetArchiveNewBooks(req, model, locale);//forward request but first pass in dupe list to show user
			}else if(dupTnList != "" && doUpdates != null) {
				//allow both updates and inserts
				//delete old data and then re-insert
				bookService.deleteSelectedInternetArchiveMetadata(dupTnList);
			} 
			 
	
			bookService.insertBatch("iaBOOKMETADATA", new String[]{"tn", "title", "author", "call_#", "priority_Item", "withdrawn", "digital_Copy_Only", "media_Type", "metadata_Complete", "batch_Class", 
					"language", "remarks_From_Scan_Center", "remarks_About_Book", "scanned_By", "location", "scan_Complete_Date", "num_of_pages", "files_Received_By_Orem", "image_Audit", "ia_Start_Date", 
					"ia_Complete_Date", "OCR_by", "OCR_complete_date", "pdfreview_By", "pdfreview_Start_Date", "pdf_Ready", "date_Released", "compression_Code", "loaded_By", "date_Loaded", 
					"collection", "dnp", "tn_Change_History", "pdf_Orem_Archived_Date", "pdf_Orem_Drive_Serial_#", "pdf_Orem_Drive_Name", "pdf_Copy2_Archived_Date", "pdf_Copy2_Drive_Serial_#", "pdf_Copy2_Drive_Name", "tiff_Orem_Archived_Date", 
					"tiff_Orem_Drive_Serial_#", "tiff_Orem_Drive_Name", "tiff_Copy2_Archived_Date", "tiff_Copy2_Drive_Serial_#", "tiff_Copy2_Drive_Name", "pdf_Sent_to_Load", "site", "url", "pid", "pages_Online", 
					"secondary_Identifier", "oclc_Number", "fhc_title", "fhc_tn", "owning_institution", "publisher_original"}, 
					  							new int[] {Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, 
					Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP,  Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR,Types.TIMESTAMP,
					Types.TIMESTAMP, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR,  Types.TIMESTAMP,Types.TIMESTAMP,Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP,
					Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR,Types.TIMESTAMP, 
					Types.VARCHAR, Types.VARCHAR,  Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, rows); 
		}
		return "redirect:metadataInternetArchiveNewBooks"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
 
} 
 