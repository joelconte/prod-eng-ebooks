/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.ia.controller;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.familysearch.prodeng.service.BookService;
import org.familysearch.prodeng.service.InternetArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//needs to be session scoped for future when we show ajax status of wgets
@Controller("iaSearchController")
public class IaSearchController implements MessageSourceAware{
	 	
	private BookService bookService;
	private MessageSource messageSource;
	private InternetArchiveService iaServ;//1 instance per instance of this controller per session.  iaServ created in request

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	public IaSearchController(BookService bookService) {
		this.bookService = bookService;

	}
	


	//search IA using keyword from localitySearch page and store in results in table
	@RequestMapping(value="ia/iaDoSearch",  method=RequestMethod.GET)
	public String doIaSearchGet( String searchKeyword, HttpServletRequest req, Locale locale, Principal principal ) {		
		 
		//delete existing in step2 for this userid
		bookService.deleteInternetArchiveWorkingBooksStateSelectBooks(principal.getName());
		 //search and insert into db results
		doIaSearchRestCall(searchKeyword, principal.getName(), false);//do search and insert into table 
		return "redirect:iaSelectBooks";//view results from table step 2 select books
	}  
	

	//view books that were previously inserted into ia_book_selection 
	@RequestMapping(value="ia/iaSelectBooks", method=RequestMethod.GET)
	public String displayIaSelectBooks( Model model, Locale locale, Principal principal) {
		 
		model.addAttribute("pageTitle", messageSource.getMessage("ia.selectBooks", null, locale));

		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add("Add to FS");
		labels.add("BibCheck");
		labels.add("Identifier");
		labels.add("Title");
		labels.add("ImageCount");
		labels.add("Language");
		labels.add("PublishDate");
		labels.add("Subject");
		labels.add("Description");
		labels.add("Publisher");
		labels.add("LicenseUrl");
		labels.add("Rights");
		labels.add("Author");
		labels.add("Oclc");
		labels.add("Tn");
		labels.add("DNP");
		labels.add("BookMiner");
		 
		List<List> allRows = bookService.getInternetArchiveWorkingBooksStateSelectBooks(principal.getName());
 
		model.addAttribute("colLabels", labels);
		model.addAttribute("allRows", allRows);
		
		List allIaScanSites = bookService.getAllIaScanSites();
		model.addAttribute("allIaScanSites", allIaScanSites);
		 
		//model.addAttribute("allSites", bookService.getAllSites()); 
		//model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url
	    
		
		//buttons
		/*List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("addViewingData", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("deleteSelected");
		details.add(messageSource.getMessage("deleteSelectedRows", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "viewingReports");
		model.addAttribute("overlayAction", "doViewingReportsInserts"); */
		
		return "ia/iaSelectBooks";
	} 

	//update addToFs checkbox selection (add to familysearch)
	@RequestMapping(value="ia/updateAddToFsAjax",  method=RequestMethod.POST)
	public  HttpEntity<byte[]> updateAddToFsAjaxPost(String bookId, String addToFs, String oclc, String tn, String dnp, HttpServletRequest req, Locale locale, Principal principal ) {		
		 
		String rc = bookService.updateInternetArchiveWorkingBook(bookId, addToFs, oclc, tn, dnp, principal.getName());
 
		byte[] documentBody = "updated".getBytes();
		if(rc != null) {
			String retVal =  "Problem updating book.  " + rc;
			 documentBody = retVal.getBytes();
		}
		
		//byte[] documentBody = null;
		String localityStr = null;
		 
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "xml"));
		header.setContentLength(documentBody.length);
		return new HttpEntity<byte[]>(documentBody, header);
	 
	}  
	  

	//do insert of pasted tn data for use in this page
	@RequestMapping(value="ia/doCopyPasteList", method=RequestMethod.POST)
	public String doCopyPasteListInsert(HttpServletRequest req, String button, String textData, Principal principal,  Model model, Locale locale) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(textData, 1);//!!!have to update when add new row to parsable metadata (this was for excel bug with ending missing /t sometimgs
			String tnList = "";
			for(List<String> r : rows) {
				tnList += ", '" + r.get(0) + "'";
			}
			tnList = tnList.substring(2);
			 
			String dupTnBookList = bookService.getDuplicateTnsInBook(tnList); //get list of tns already in bookmetadata col=titleno 3rd(0based) elem in rnData rows
			Set<String>  dupInternetArchiveWorkingList = bookService.getIneternetArchiveBooksInProcess(tnList);
			List<String> dupTnListList = bookService.getDuplicateTnsInBookList(tnList);
			String dupTnList = bookService.generateQuotedListString(dupTnListList);

			if(dupTnList != "" ) { 
				//redisplay page with dupTnList msg
				
				//model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.alreadyExistMetadata", null, locale) + "\n" + dupTnList);
				//return "errors/generalError";
				model.addAttribute("dupeTnsInfo", dupTnList);  
				model.addAttribute("pastedData", textData); 
				return displayIaSelectBooks( model, locale, principal);//forward request but first pass in dupe list to show user
			}
		 
			filterOutDupes(rows, dupTnListList);
			bookService.deleteInternetArchiveWorkingBooks(new ArrayList( dupInternetArchiveWorkingList)); 
			//filterOutDupes(rows, new ArrayList(dupInternetArchiveWorkingList));
			
			//do call to archive.org to get data
			for(List<String> idl : rows) {
				String id = idl.get(0);
				doIaSearchRestCall(id, principal.getName(), true);//also does insert into internetarchive_woring
			}
			
			//bookService.insertInternetArchiveSearchedBooks(rows, principal.getName());
			//bookService.insertBatch("INTERNETARCHIVE_WORKING", new String[]{"title", "author", "subject", "titleno", "callno", "partner_lib_callno", "filmno", "pages", "summary", "dgsno", "language", "owning_institution", "requesting_location", "scanning_location", "record_number", "date_original", "publisher_original", "filename", "current_timestamp_date_added", "metadata_adder"}, new int[] {Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR,Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR}, rows); */
		}
		return "redirect:iaSelectBooks"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 

	public void filterOutDupes(List<List<String>> rows, List<String> dupTnList) {
		for(String tn: dupTnList) {
			for(List<String> r: rows) {
				if(tn.equals(r.get(3))) {
					rows.remove(r);
					break;
				}
			}
		}
	}
	 

	//move books to be Verified.  IE. change state and delete books with 'n' add to fs flag
	@RequestMapping(value="ia/iaMoveToVerify",  method=RequestMethod.POST)
	public String doIaMoveToVerifyGet( String site, HttpServletRequest req, Locale locale, Principal principal ) {		
		 //move to next verify state
		bookService.updateInternetArchiveWorkingBooksChangeStateVerifyBooks(principal.getName(), site);//change books flagged for familysearch to verify books state
		bookService.deleteInternetArchiveWorkingBooksStateSelectBooks(principal.getName());//delete all remaining non-flagged books in select books state
	
		
		return "redirect:iaVerifyBooks";//view results from table step 3 verify books
	}  
	
	

	//view books that were previously inserted into ia_book_selection 
	@RequestMapping(value="ia/iaVerifyBooks", method=RequestMethod.GET)
	public String displayIaVerifyBooks( Model model, Locale locale, Principal principal) {
	
		model.addAttribute("pageTitle", messageSource.getMessage("ia.verifyBooks", null, locale));

		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add("Add to FS");
		labels.add("BibCheck");
		labels.add("Identifier");
		labels.add("Title");
		labels.add("ImageCount");
		labels.add("Language");
		labels.add("PublishDate");
		labels.add("Subject");
		labels.add("Description");
		labels.add("Publisher");
		labels.add("LicenseUrl");
		labels.add("Rights");
		labels.add("Author");
		labels.add("Oclc");
		labels.add("Tn");
		labels.add("DNP");
		labels.add("MiningSite");
		labels.add("BookMiner");
		 
		List<List> allRows = bookService.getInternetArchiveWorkingBooksStateVerifyBooks(null);//for now all users' books
 
		model.addAttribute("colLabels", labels);
		model.addAttribute("allRows", allRows);
		 
		
		return "ia/iaVerifyBooks";
	} 
 

	//move books to next state on step 4
	@RequestMapping(value="ia/iaMoveToPreDownload",  method=RequestMethod.POST)
	public String doIaMoveToPreDownloadGet( HttpServletRequest req, Locale locale, Principal principal ) {		
		 //move to next import state
		bookService.updateInternetArchiveWorkingBooksChangeStatePreDownloadBooks(null);//all users' books for now
		bookService.deleteInternetArchiveWorkingBooksStateVerifyBooks(null);//delete all remaining non-flagged books in select books state
	
		
		return "redirect:iaPreDownloadBooks";//view results from table step 3 verify books
	}  
 
	

	//view books in state 4
	@RequestMapping(value="ia/iaPreDownloadBooks", method=RequestMethod.GET)
	public String displayIaPreDownloadBooks( Model model, Locale locale, Principal principal) {
	
		model.addAttribute("pageTitle", messageSource.getMessage("ia.importBooks", null, locale));

		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add("Add to FS");
		labels.add("BibCheck");
		labels.add("Identifier");
		labels.add("Title");
		labels.add("ImageCount");
		labels.add("Language");
		labels.add("PublishDate");
		labels.add("Subject");
		labels.add("Description");
		labels.add("Publisher");
		labels.add("LicenseUrl");
		labels.add("Rights");
		labels.add("Author");
		labels.add("Oclc");
		labels.add("Tn");
		labels.add("DNP");
		labels.add("MiningSite");
		labels.add("BookMiner");
		 
		List<List> allRows = bookService.getInternetArchiveWorkingBooksStatePreDownloadBooks(null);//for now all users' books
 
		model.addAttribute("colLabels", labels);
		model.addAttribute("allRows", allRows);
		
		  
		
		return "ia/iaPreDownloadBooks";
	} 


	//process downloads and xml
	@RequestMapping(value="ia/iaDoBooksDownload",  method=RequestMethod.POST)
	public String doIaDoBooksDownloadsGet( HttpServletRequest req, Locale locale, Principal principal ) {		
	 
		//first move to next downlaod not yet started state, so they move to next page in gui and show state 
		bookService.updateInternetArchiveWorkingBooksChangeStateDownloadNotStartedBooks(null);//all users' books for now.  These are books that WGET code will pickup and process in todoList below
		bookService.deleteInternetArchiveWorkingBooksStatePreDownloadBooks(null);//delete all remaining non-flagged books  
		bookService.updateInternetArchiveWorkingBooksStateDownloadNotStartedBooksErrorMsg(null);
		
		if(iaServ!=null) {
			iaServ.stopWorkerThreadsAndDownloadProcesses();
		}
		iaServ = new InternetArchiveService(bookService);
		 
		List<List> todoList = bookService.getInternetArchiveWorkingBooksStateDownloadNotStartedBooks(null);//get for all users' books for now in any of the download states
	 	
		List<String> todoListIdentifiers = new ArrayList<String>();
		for(List<String> row : todoList) {
			todoListIdentifiers.add(row.get(6));
		}
		iaServ.setTodoList(todoListIdentifiers);
	 	
		iaServ.doInternetArchiveWorkingBooksDownloadWgetAndXml(req, null);//do WGETs etc in threaded code async
		 
		
		//todo move to step 6 bookService.doInternetArchiveWorkingBooksTfdbInsert(null);
	    //todo move to step 6 bookService.setInternetArchiveWorkingBooksImportHistory(todoList, nogoList, null);//move from working table to history table and delete nogolist
	
		return "redirect:iaMonitorDownloadBooks";//view results step 5 
		//return "redirect:iaImportBooks";
	}  
 
	

	//view books in state 5 - show initial page that then updates via ajax below
	@RequestMapping(value="ia/iaMonitorDownloadBooks", method=RequestMethod.GET)
	public String displayIaMonitorDownloadBooks( Model model, Locale locale, Principal principal) {
	
		model.addAttribute("pageTitle", messageSource.getMessage("ia.monitorDownloadBooks", null, locale));
 

		//title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add("STATUS");
		labels.add("START");
		labels.add("COMPLETE");
		labels.add("FOLDER");
		labels.add("Add to FS");
		labels.add("BibCheck");
		labels.add("Identifier");
		labels.add("Title");
		labels.add("ImageCount");
		labels.add("Language");
		labels.add("PublishDate");
		labels.add("Subject");
		labels.add("Description");
		labels.add("Publisher");
		labels.add("LicenseUrl");
		labels.add("Rights");
		labels.add("Author");
		labels.add("Oclc");
		labels.add("Tn");
		labels.add("DNP");
		labels.add("MiningSite");
		labels.add("BookMiner");
		 
		List<List> allRows = bookService.getInternetArchiveWorkingBooksStateAnyDownloadBooks(null);//all download related states
 
		model.addAttribute("colLabels", labels);
		model.addAttribute("allRows", allRows);
		
		  
		
		
		return "ia/iaMonitorDownloadBooks";
	} 


	//ajax query status of books being downloaded (wget)
	@RequestMapping(value="ia/queryStatusOfDownloadsAjax",  method=RequestMethod.GET)
	public  HttpEntity<byte[]> queryStatusOfDownloads( HttpServletRequest req, Locale locale, Principal principal ) {		
		String err = null;
		StringBuffer sb = new StringBuffer();
		
		 
		if(iaServ == null) {
			//err = "Info:  No books appear to be downloading";
			iaServ = new InternetArchiveService(bookService); //create it just so we have an object to work with...i think
		}
	
		List<List> allRows = bookService.getInternetArchiveWorkingBooksStateAnyDownloadBooks(null);//for now all users' books
		//List<List<String>> allRows = iaServ.getTodoList();//contains list of books and status

		

		//convert list to xml string
		sb.append("<rows>");
		for(List<String> row : allRows) {
			sb.append("<row><identifier>" + row.get(6) + "</identifier>");
			sb.append("<state>" + row.get(0) + "</state>");
			sb.append("<start_date>" + row.get(1) + "</start_date>");
			sb.append("<end_date>" + row.get(2) + "</end_date>");
			sb.append("<folder>" + row.get(3) + "</folder></row>");
		}
		sb.append("</rows>");

		/*
		sb.append("<rows><row><identifier>1111</identifier>");
		sb.append("<state>2222</state>");
		sb.append("<start_date>3333</start_date>");
		sb.append("<end_date>4444</end_date></row>");
		sb.append("<row><identifier>bb1111</identifier>");
		sb.append("<state>bb2222</state>");
		sb.append("<start_date>bb3333</start_date>");
		sb.append("<end_date>bb4444</end_date></row></rows>");
*/
		
		byte[] documentBody;
		if(err !=  null) {
			documentBody = err.getBytes();
		}else {
			documentBody =   sb.toString().getBytes();
		}
		
		//byte[] documentBody = null;
		String localityStr = null;
		 
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "xml"));
		header.setContentLength(documentBody.length);
		return new HttpEntity<byte[]>(documentBody, header);
	 
	}  
		

	//ajax query book data for overlay
	@RequestMapping(value="ia/queryAjaxBookData",  method=RequestMethod.POST)
	public  HttpEntity<byte[]> queryAjaxBookData( String identifier, HttpServletRequest req, Locale locale, Principal principal ) {		
		String err = null;
		StringBuffer sb = new StringBuffer();
		 
		List<String> row = bookService.getInternetArchiveWorkingBookById(identifier);//for now all users' books
		  
		//convert list to xml string
		sb.append("<rows>");
		
		//escape values for html
		sb.append("<row><identifier>" + identifier + "</identifier>");
		sb.append("<IS_SELECTED>" + row.get(0) + "</IS_SELECTED>");
		sb.append("<BIBCHECK>" + row.get(1) + "</BIBCHECK>");
		sb.append("<TITLE>" + StringEscapeUtils.escapeXml( row.get(2)) +  "</TITLE>"); 
		sb.append("<IMAGE_COUNT>" + row.get(3) + "</IMAGE_COUNT>");
		sb.append("<LANGUAGE>" + StringEscapeUtils.escapeXml(row.get(4)) + "</LANGUAGE>");
		sb.append("<PUBLISH_DATE>" + row.get(5) + "</PUBLISH_DATE>");
		sb.append("<SUBJECT>" +  StringEscapeUtils.escapeXml(row.get(6)) + "</SUBJECT>");
		sb.append("<DESCRIPTION>" + StringEscapeUtils.escapeXml(row.get(7)) + "</DESCRIPTION>");
		sb.append("<PUBLISHER>" + StringEscapeUtils.escapeXml(row.get(8)) + "</PUBLISHER>");
		sb.append("<LICENSEURL>" + StringEscapeUtils.escapeXml(row.get(9)) + "</LICENSEURL>");
		sb.append("<RIGHTS>" + StringEscapeUtils.escapeXml(row.get(10)) + "</RIGHTS>");
		sb.append("<AUTHOR>" + StringEscapeUtils.escapeXml(row.get(11)) + "</AUTHOR>");
		sb.append("<OCLC>" + StringEscapeUtils.escapeXml(row.get(12)) + "</OCLC>");
		sb.append("<TN>" + StringEscapeUtils.escapeXml(row.get(13)) + "</TN>");
		sb.append("<SITE>" + StringEscapeUtils.escapeXml(row.get(14)) + "</SITE>");
		sb.append("<BATCH_NUMBER>" + StringEscapeUtils.escapeXml(row.get(15)) + "</BATCH_NUMBER>");
		sb.append("<OWNER_USERID>" + StringEscapeUtils.escapeXml(row.get(16)) + "</OWNER_USERID>");
		sb.append("<STATE>" + row.get(17) + "</STATE>");
		sb.append("<STATE_ERROR>" + row.get(18) + "</STATE_ERROR>");
		sb.append("<START_DATE>" + row.get(19) + "</START_DATE>");
		sb.append("<END_DATE>" + row.get(20) + "</END_DATE>");
		sb.append("<FOLDER>" + StringEscapeUtils.escapeXml(row.get(21)) + "</FOLDER>");
		sb.append("<COMPLETE_DATE>" + row.get(22) + "</COMPLETE_DATE>");
		sb.append("<DNP>" + row.get(23) + "</DNP>");

		sb.append("</row>");
		sb.append("</rows>");

		/*
		sb.append("<rows><row><identifier>1111</identifier>");
		sb.append("<state>2222</state>");
		sb.append("<start_date>3333</start_date>");
		sb.append("<end_date>4444</end_date></row>");
		sb.append("<row><identifier>bb1111</identifier>");
		sb.append("<state>bb2222</state>");
		sb.append("<start_date>bb3333</start_date>");
		sb.append("<end_date>bb4444</end_date></row></rows>");
*/
		
		byte[] documentBody;
		if(err !=  null) {
			documentBody = err.getBytes();
		}else {
			documentBody =   sb.toString().getBytes();
		}
		
		//byte[] documentBody = null;
		String localityStr = null;
		 
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "xml"));
		header.setContentLength(documentBody.length);
		return new HttpEntity<byte[]>(documentBody, header);
	 
	}  
		

	//re-run downloads in Monitor page in case of some error
	@RequestMapping(value="ia/iaDoBooksDownloadRedo",  method=RequestMethod.POST)
	public String doIaDoBooksDownloadRedoGet( HttpServletRequest req, Locale locale, Principal principal ) {		
	 
		//first find way to stop current downlaod process if exists????todo
		
		 
		if(iaServ!=null) {
			iaServ.stopWorkerThreadsAndDownloadProcesses();
		}

		bookService.updateInternetArchiveWorkingBooksChangeStateDownloadNotStartedBooksFromAnyDownloadingState(null);//all users' books for now.  These are books that WGET code will pickup and process in todoList below
		bookService.updateInternetArchiveWorkingBooksStateDownloadNotStartedBooksErrorMsg(null);//clear out messages of books in start state
		
		iaServ = new InternetArchiveService(bookService);
		 
		List<List> todoList = bookService.getInternetArchiveWorkingBooksStateDownloadNotStartedBooks(null);//get for all users' books for now in any of the download states
	 	
		List<String> todoListIdentifiers = new ArrayList<String>();
		for(List<String> row : todoList) {
			todoListIdentifiers.add(row.get(6));
		}
		iaServ.setTodoList(todoListIdentifiers);
	 	
		iaServ.doInternetArchiveWorkingBooksDownloadWgetAndXml(req, null);//do WGETs etc in threaded code async
		 
		
		//todo move to step 6 bookService.doInternetArchiveWorkingBooksTfdbInsert(null);
	    //todo move to step 6 bookService.setInternetArchiveWorkingBooksImportHistory(todoList, nogoList, null);//move from working table to history table and delete nogolist
	
		return "redirect:iaMonitorDownloadBooks";//view results step 5 
		//return "redirect:iaImportBooks";
	}  

	//re-run downloads - don't restart any in "xml and pdf complete" state
	@RequestMapping(value="ia/iaDoBooksDownloadRedo2",  method=RequestMethod.POST)
	public String doIaDoBooksDownloadRedo2Get( HttpServletRequest req, Locale locale, Principal principal ) {		
	 
		//first find way to stop current downlaod process if exists????todo
		
		 
		if(iaServ!=null) {
			iaServ.stopWorkerThreadsAndDownloadProcesses();
		}

		bookService.updateInternetArchiveWorkingBooksChangeStateDownloadNotStartedBooksFromAnyDownloadingStateExceptComplete(null);//all users' books for now.  These are books that WGET code will pickup and process in todoList below
		bookService.updateInternetArchiveWorkingBooksStateDownloadNotStartedBooksErrorMsg(null);//clear out messages of books in start state
		
		iaServ = new InternetArchiveService(bookService);
		 
		List<List> todoList = bookService.getInternetArchiveWorkingBooksStateDownloadNotStartedBooks(null);//get for all users' books for now in any of the download states
	 	
		List<String> todoListIdentifiers = new ArrayList<String>();
		for(List<String> row : todoList) {
			todoListIdentifiers.add(row.get(6));
		}
		iaServ.setTodoList(todoListIdentifiers);
	 	
		iaServ.doInternetArchiveWorkingBooksDownloadWgetAndXml(req, null);//do WGETs etc in threaded code async
		 
		
		//todo move to step 6 bookService.doInternetArchiveWorkingBooksTfdbInsert(null);
	    //todo move to step 6 bookService.setInternetArchiveWorkingBooksImportHistory(todoList, nogoList, null);//move from working table to history table and delete nogolist
	
		return "redirect:iaMonitorDownloadBooks";//view results step 5 
		//return "redirect:iaImportBooks";
	}  
	

	//stop downloads in Monitor page in case of some error
	@RequestMapping(value="ia/iaDoBooksDownloadStop",  method=RequestMethod.POST)
	public String doIaDoBooksDownloadStopGet( HttpServletRequest req, Locale locale, Principal principal ) {		
	 
		//first find way to stop current downlaod process if exists????todo
		
		 
			
		if(iaServ!=null) {
			iaServ.stopWorkerThreadsAndDownloadProcesses();
		}  
		
		bookService.updateInternetArchiveWorkingBooksChangeStateDownloadNotStartedBooksFromAnyDownloadingState(null);//all users' books for now.  These are books that WGET code will pickup and process in todoList below
		
		
		return "redirect:iaMonitorDownloadBooks";//view results step 5 
		//return "redirect:iaImportBooks";
	}  


	//move books in download-xml complete state to next page for insert to tfdb
	@RequestMapping(value="ia/iaMoveToInsertTfdb",  method=RequestMethod.POST)
	public String doIaMoveToInsertTfdbGet( HttpServletRequest req, Locale locale, Principal principal ) {		
	 
		//first check to see if any not done
		List<List> notDone = bookService.getInternetArchiveWorkingBooksStateAnyDownloadBooksExceptComplete(null);
		if(notDone.size() > 0) {
			//return "redirect:iaMonitorDownloadBooks";//view results step 5 
		}
		//move to next state, so they move to next page in gui  
		bookService.updateInternetArchiveWorkingBooksChangeStateReadyInsertTfdbBooks(null);//return id if not all in complete state
		//delete any not flagged to import (xml metadata copyright check...only public domain books).  Flag is set to F by xml generating code.
		bookService.deleteInternetArchiveWorkingBooksAnyDownloadingState();//delete all remaining non-flagged books  
			
			    
		return "redirect:iaInsertTfdbBooks";//view results step 6
	 
	}  
 
	

	//view books in state 6 - ready to insert into tfdb
	@RequestMapping(value="ia/iaInsertTfdbBooks", method=RequestMethod.GET)
	public String displayIaInsertTfdbBooks( Model model, Locale locale, Principal principal) {
	
		model.addAttribute("pageTitle", messageSource.getMessage("ia.insertTfdbBooks", null, locale));
 

		//title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add("STATUS");
		labels.add("START");
		labels.add("COMPLETE");
		labels.add("FOLDER");
		labels.add("Add to FS");
		labels.add("BibCheck");
		labels.add("Identifier");
		labels.add("Title");
		labels.add("ImageCount");
		labels.add("Language");
		labels.add("PublishDate");
		labels.add("Subject");
		labels.add("Description");
		labels.add("Publisher");
		labels.add("LicenseUrl");
		labels.add("Rights");
		labels.add("Author");
		labels.add("Oclc");
		labels.add("Tn");
		labels.add("DNP");
		labels.add("MiningSite");
		labels.add("BookMiner");
										 
		List<List> allRows = bookService.getInternetArchiveWorkingBooksStateInsertTfdb(null); 

		model.addAttribute("colLabels", labels);
		model.addAttribute("allRows", allRows);
		
		return "ia/iaInsertTfdbBooks";
	} 

	//insert into tfdb
	@RequestMapping(value="ia/iaDoInsertTfdb",  method=RequestMethod.POST)
	public String doIaDoInsertTfdbGet(String driveName, String driveNumber, HttpServletRequest req, Locale locale, Principal principal ) {		
	 
		 
	 
		bookService.updateInternetArchiveWorkingBooksChangeStateCompleteBooks(null, driveName, driveNumber,  new InternetArchiveService(null) );//todo later return id if not all in complete state
		 	    
		return "redirect:iaInsertTfdbBooks";//view results step 6
	 
	}  
	
	private String doIaSearchRestCall(String searchKey, String ownerUserId, boolean isSelected ) {                                            
		  
		List<List<String>> searchResults = new ArrayList<List<String>>();
         
        try{
        	 String uri ="http://archive.org/advancedsearch.php?q=(" + searchKey + ")%20AND%20mediatype%3A(texts)&fl%5B%5D=date&fl%5B%5D=description&fl%5B%5D=identifier&fl%5B%5D=imagecount&fl%5B%5D=language&fl%5B%5D=mediatype&fl%5B%5D=rights&fl%5B%5D=subject&fl%5B%5D=title&fl%5B%5D=type&fl%5B%5D=creator&fl%5B%5D=licenseurl&fl%5B%5D=oclc-id&fl%5B%5D=publisher&sort%5B%5D=&sort%5B%5D=&sort%5B%5D=&rows=10000&page=1&callback=callback&output=json";
             //String uri = "https://archive.org/search.php?query=(" + searchKey + ")&and[]=mediatype%3A%22texts%22";
        	 System.out.println("Internet Archive REST: " + uri);
             URL url = new URL(uri);
//             HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
             connection.setRequestMethod("GET");
             connection.setRequestProperty("Accept", "application/json");
             java.io.InputStream istr = connection.getInputStream();
             byte[] b = IOUtils.toByteArray(istr);
             
            
             
         ///////////////////////tmp 
        /* Scanner s = new Scanner(istr);
         String xmlStr = "";
         while (s.hasNext()) {
             xmlStr += s.nextLine() + "\r\n";
         }
         System.out.println(xmlStr);  */
         ///////////////////////////
        
        /*return from search
     
           callback({
    "responseHeader":{
       "status":0,
       "QTime":130,
       "params":{
          "q":"( (title:korea^100 OR description:korea^15 OR collection:korea^10 OR language:korea^10 OR text:korea^1) ) AND mediatype:(texts )",
          "qin":"(korea) AND mediatype:(texts)",
          "fl":"date,description,identifier,imagecount,language,mediatype,rights,subject,title,type",
          "wt":"json",
          "rows":"5",
          "json.wrf":"callback",
          "start":0
       }
    },
    "response":{
       "numFound":10032,
       "start":0,
       "docs":[
          {
             "creator":"Defense Technical Information Center",
             "date":"1954-07-26T00:00:00Z",
             "identifier":"DTIC_AD0045284",
             "imagecount":27,
             "language":"english",
             "mediatype":"texts",
             "subject":[
                "DTIC Archive",
                "KUHN, RICHARD B.",
                "GOODYEAR AEROSPACE CORP AKRON OH",
                "*CATHODE RAY TUBES",
                "ERRORS",
                "MEASUREMENT",
                "TEST EQUIPMENT"
             ],
             "title":"DTIC AD0045284: AN INSTRUMENT FOR MEASURING SPOT SIZE"
          },
          {
  
        */
             JsonFactory jf = new JsonFactory();
             JsonParser parser = jf.createJsonParser(b, "callback(".length(), b.length-10);//remove garbage callback str
              
             //skip headerResponse
             JsonToken tok;
             while (!parser.isClosed()) {
                 tok = parser.nextToken();
               
                 
                 if("responseHeader".equalsIgnoreCase( parser.getText())) { // .getValueAsString())){
                     parser.nextToken();//go to OJBECT part and skip
                     parser.skipChildren();
                     break;
                 }
             }
       
             int count = 0;
             while (!parser.isClosed()) {
                 tok = parser.nextToken();
               
                 
                 if("numfound".equalsIgnoreCase( parser.getText())){
                     parser.nextToken();
                     count = parser.getIntValue();
                     
                     break;
                 }
             }
             if(count <= 0){
                 return "Warning: no results found";
             }
             
             boolean foundDocs = false;
             while (!parser.isClosed()) {
                 tok = parser.nextToken();
              
                 if("docs".equalsIgnoreCase( parser.getText())){
                     parser.nextToken();//should be array of len count
                     foundDocs = true;
                     break;
                 }
             }
  
             if(foundDocs==false){
                     return "Error, no DOCS in json returned, but got '" + parser.getText() + "' instead.   Array count=" + count;
             }

                 
             String bibcheckInClause = "";
             List<String> attrList = null;
             for(int x = 0; x < count; x++){
//                 System.out.println("hhhh"+x);
                 attrList = parseJsonValues( parser, x+1, isSelected);
                 if(attrList==null)
                     break;//ended early.  ie count specified in url was smaller than actual total count on server (only count get sent back)
                
                 searchResults.add(attrList);
                 
                 bibcheckInClause += ", '" + attrList.get(6) + "'";//list of TNs for bibchecking
             }

 
             //bibcheck - filter out non T bibchecks
             Set<String> dupes = bookService.doBibcheck(bibcheckInClause.substring(1, bibcheckInClause.length()));//returns any dupes in tn or secondary_identifier  
             
             for(List<String> row : searchResults){
                 if(dupes.contains(row.get(6))){
                     row.remove(1);
                     row.add(1, "F");//duplicate
                     //note that we will just remove this row below in the iterator loop, so only T bibcheck books should show up in the queue
                 }else{
                     row.remove(1);
                     row.add(1, "T");//dupe check ok- not dup
                 }
             }    
             
             
             // Language check.  IA languages don't always match our naming.  
             //if language in languages.publish_name, use languages.id
             List<List> langs = bookService.getAllLanguages();
             for(List<String> row : searchResults){
            	 int langIndex = 4;
            	 String thisLang = row.get(langIndex);
            	
            	 //System.out.println(thisLang);
            	 
            	 //eng and english and "" are common
            	 if(thisLang.startsWith("eng")) {
            		 row.remove(langIndex);
                     row.add(langIndex, "English");//id in languages table
            	 }else if( thisLang.equals("")) {
            		 //row.remove(langIndex);
                     //row.add(langIndex, "");//null 
            	 }else {
            		 //in db 3 Norwegian langs, so for example, we want to find the best match (so if only one candidatematch then use it)
            		 //"Norwegian-Bokmal";"Norwegian-Bokmal;nor;no"
            		 //"Norwegian-Nynorsk";"Norwegian-Nynorsk;nor;no"
            		 //"Norwegian";"Norwegian;nor;no"

            		 String candidateMatch1 = null;
            		 String candidateMatch2 = null;
            		 //String candidateMatch3 = null;
            		 String match = null;
            		 for(List l : langs) {
            			 String dbLangId = (String) l.get(0);
            			 String dbLangPublish = (String) l.get(1);
            			 if(thisLang.equalsIgnoreCase(dbLangId)) {
            				 match = dbLangId;
            				 break;
            			 }else if(dbLangPublish.contains(thisLang)) {
            				 if(candidateMatch1 == null) {
            					 candidateMatch1 = dbLangId;
            				 }else if(candidateMatch2 == null) {
            					 candidateMatch2 = dbLangId;
            				 } 
            			 }
            		 }
            		 
            		 if(match != null) {
            			 row.remove(langIndex);
                         row.add(langIndex, match);
            		 }else if(candidateMatch1 != null && candidateMatch2 == null) {
            			 row.remove(langIndex);
                         row.add(langIndex, candidateMatch1);
            		 }else {
            			 row.remove(langIndex);
                         row.add(langIndex, "");//have to use null..later todo flag it for manual update???
            		 }
            	 }
             }
             
             
             
             //remove books already being processed by anyone
             //remove non public domain
             Set<String> inProcess = bookService.getIneternetArchiveBooksInProcess(bibcheckInClause.substring(1, bibcheckInClause.length()));
             Iterator<List<String>> iter = searchResults.iterator();
             while(iter.hasNext()){
            	 List<String> bookRow = iter.next();
            	 String licenseUrl = bookRow.get(10).toLowerCase();
            	 String identifier = bookRow.get(6);
            	 String rights = bookRow.get(11).toLowerCase();
            	 
                 if(bookRow.get(1).equals("T") == false){
                     //remove bibcheck dupes
                	 iter.remove();
                 }else if(inProcess.contains(identifier)){
                     //remove in-process IA 
                	 iter.remove();
                 }else if(licenseUrl != null 
                		 && licenseUrl.equals("") == false 
  //temp allow copyprotected to test... && licenseUrl.contains("/") == false
                		 && licenseUrl.contains("publicdomain") == false 
                		 && licenseUrl.contains("www.usa.gov/government-works") == false ){
                     //remove non- public domain books (licenseUrl = [null, publicdomain, government], or Rights="public domain" are ok for initial filtering out of books to display to missionary)
                	 //ie rights = "The copyright of this item has not been evaluated. Please refer to the original publisher/creator of this item for more information. You are free to use this item in any way that is permitted by the copyright and related rights legislation that applies to your use."
                	 
                	 iter.remove();
                 }else if( rights != null 
                		 && rights.equals("") == false
                		 && rights.startsWith("public domain") == false ){
                     //remove non- public domain books (licenseUrl = [null, publicdomain, government], or Rights="public domain" are ok for initial filtering out of books to display to missionary)
                	 //ie rights = "The copyright of this item has not been evaluated. Please refer to the original publisher/creator of this item for more information. You are free to use this item in any way that is permitted by the copyright and related rights legislation that applies to your use."
                	 //"public domain.", so search String.startsWith()
                	 
                	 iter.remove();
                 }
             }   
             
             bookService.insertInternetArchiveSearchedBooks(searchResults, ownerUserId); 
             
             istr.close();
             return null;//no errors
         
        }
        catch (Exception e){
            return e.toString();
        }
    }      
      

    //get values out of json object for each book
    private List<String> parseJsonValues(JsonParser parser, int number, boolean isSelectedBool) throws Exception{
       
        String isSelected = "";//TF
        String bibcheck = "";//TFU
        String description = "";
        String date = "";//publish date
        String title = "";
        String identifier = "";
        String imageCount = "";
        String language = "";
        String subject = "";
        String publisher = "";
        String licenseurl = "";
        String rights = "";
        String creator = "";//author
        String tn = "";//same as identifier
        String oclc = "";
        
        List<String> attrList = new ArrayList<String>();
        
        JsonToken tok = parser.nextToken();// {  or ] if end of list
        if(JsonToken.END_ARRAY.equals(tok)){
            //end of objects
            return null;
        }
        //System.out.println("tok0= " + tok);
        while(true){
            tok = parser.nextToken();//label
            //System.out.println("tok1= " + tok);
            String name = parser.getCurrentName();
         //   System.out.println("val= " + name);
            if(JsonToken.END_OBJECT.equals(tok))
                break;
            
            
            tok = parser.nextToken();//value
            //System.out.println("tok2= " + tok);
            String val = null;
            String nameTmp =parser.getCurrentName();
            if(JsonToken.START_ARRAY.equals(tok)){
                //val is array
                val = "";
                while(true){
                    tok = parser.nextToken();
                    //System.out.println("tok3= " + tok);
                    if(JsonToken.END_ARRAY.equals(tok)){
                        break;
                    }
                    
                    val += parser.getText() + "\n";
                }
            }else{

                val = parser.getText();
            }
            
            if("title".equalsIgnoreCase(name)){
                title = val;
            }else if("imageCount".equalsIgnoreCase(name)){
                imageCount = val;
            }else if("language".equalsIgnoreCase(name)){
                language = val;
            }else if("date".equalsIgnoreCase(name)){
                date = val;
            }else if("identifier".equalsIgnoreCase(name)){
                identifier = val;
                tn = val;
            }else if("subject".equalsIgnoreCase(name)){
                subject = val;
            }else if("description".equalsIgnoreCase(name)){
                description = val;
            }else if("publisher".equalsIgnoreCase(name)){
                publisher = val;
            }else if("licenseurl".equalsIgnoreCase(name)){
            	//if(licenseurl.equals(""))//could be set as loan below - actually it is not till later step when generating xml metadata file that this "loan" tag is visible
            		licenseurl = val;
            }else if("rights".equalsIgnoreCase(name)){
                rights = val;
            }else if("creator".equalsIgnoreCase(name)){
                creator = val;
            }else if("oclc-id".equalsIgnoreCase(name)){
                oclc = val;
            }
            
        }    
        
        if(isSelectedBool)
        	attrList.add("T");//0
        else
        	attrList.add("F");//0
        attrList.add("U");//1 y=ok n=dupe u=unknown
        attrList.add(title);//2
        attrList.add(imageCount);//3
        attrList.add(language);//4
        attrList.add(date);//5
        attrList.add(identifier);//6
        attrList.add(subject);//7
        attrList.add(description);//8
        attrList.add(publisher);//9
        attrList.add(licenseurl);//10
        attrList.add(rights);//11
        attrList.add(creator);//12
        attrList.add(tn);//13
        attrList.add(oclc);//14
        

        return attrList;
    }

   
    
}