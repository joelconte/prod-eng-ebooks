/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.search.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.familysearch.prodeng.model.Book;
import org.familysearch.prodeng.model.Search;
import org.familysearch.prodeng.model.SqlTimestampPropertyEditor;
import org.familysearch.prodeng.service.BookService;
import org.familysearch.prodeng.service.BookServiceReadOnly;
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


@Controller("searchMiscController")
public class SearchMiscController implements MessageSourceAware{

	private BookService bookService;
	private BookServiceReadOnly bookServiceReadOnly;
	private MessageSource messageSource;
	
	@Autowired
	public SearchMiscController(BookService bookService, BookServiceReadOnly bookServiceReadOnly ) {
		this.bookService = bookService;
		this.bookServiceReadOnly = bookServiceReadOnly;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/////start Misc Searches page//////////
	@RequestMapping(value="search/searchMisc", method=RequestMethod.GET)
	public String displaySearchRead(@RequestParam(value = "searchId", required=false) String searchId, Principal principal, HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("search.pageTitle.search", null, locale));
		model.addAttribute("mode", "read"); 
		model.addAttribute("allSearchIds",  bookService.getAllSearchIds()); 
		model.addAttribute("search", bookService.getSearch(searchId)); 
		return "search/searchMisc";
	} 

 
	//show populated form - for update/create - used with button "update"  (or "create")
	@RequestMapping(value="search/searchMisc", params="update", method=RequestMethod.POST)
	public String displaySearchUpdatePost(String searchId, Search search, String doCreate, Model model, Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("search.pageTitle.search", null, locale));
		model.addAttribute("mode", "update");  
		model.addAttribute("doCreate", doCreate); //if just update then null
		model.addAttribute("allSearchIds",  bookService.getAllSearchIds()); 
		model.addAttribute("search", bookService.getSearch(searchId)); 
	 
		return "search/searchMisc";
	}

	//do update/create
	@RequestMapping(value="search/searchMisc", params="save", method=RequestMethod.POST)
	public String doSearchSave(Search search, String searchIdNew, String searchIdOriginal, String doCreate, Model model) {

		search.setSearchId(searchIdNew);//to get dropdown to show current search, had to not use spring mvc path in tag, set var here
		
		if(doCreate == null) {
			bookService.updateSearch(search, searchIdOriginal); //searchId can be changed
		}else {
			bookService.createSearch(search); //new search
		}
		return "redirect:searchMisc?read&searchId=" + searchIdNew; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do cancel
	@RequestMapping(value="search/searchMisc", params="cancel", method=RequestMethod.POST)
	public String doSearchCancel( String searchIdNew, Model model) {
		//do nothing
		return "redirect:searchMisc?read&searchId=" + searchIdNew; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
	//run query - used with button "run" - opens in new window 
	@RequestMapping(value="search/searchMiscResults", params="runQuery", method=RequestMethod.POST)
	public String runSearchPost(Principal principal, String searchId, String queryText, Model model, Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("search.pageTitle.searchResult", null, locale));
		String word = null;
		String theUser = "pauldev";
		if(theUser.equals(principal.getName())){
			word = null;
		}else{
			word = bookService.checkQuery(queryText);
		}
		if(word != null) {
			///todo remove comments here after I run test to make sure i get back db message
		//todo remove	model.addAttribute("bookErrorMessage",  messageSource.getMessage("search.securityBadWord", null, locale) + "\n" + word);
		//todo remove	return "errors/generalError";
		}
		model.addAttribute("queryText", queryText); 
		if(theUser.equals(principal.getName())){
			bookService.doBookAudit(principal.getName(), "misc query", queryText);
			model.addAttribute("result", bookService.runQuery(queryText)); 
		}else {
			bookService.doBookAudit(principal.getName(), "misc query", queryText);
			model.addAttribute("result", bookServiceReadOnly.runQuery(queryText));
		}
	 
		return "search/searchMiscResults";
	}

	/////start Misc Searches page//////////
	
	/////start readonly TF search page//////////

	//show populated form - for read (tn can be null and will display empty fields)
	@RequestMapping(value="search/trackingForm", params="read", method=RequestMethod.GET)
	public String displayBookReadSearch(@RequestParam(value = "tn", required=false) String tn, String fetchAllTns, Model model, Locale locale) {
		model.addAttribute("pageTitle", messageSource.getMessage("search.pageTitle.trackingForm", null, locale));
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
		return "search/trackingForm";
	} 
	 
	/////end readonly TF search page//////////

	/////start search list tns page//////////
 
	//This request mapping is for both GET and POST (ie before pasting data and after showing data)	
	@RequestMapping(value="search/searchListTnsAllColumns")
	public String getSearchListTnsAllColumns(String button, String tnData, Model model,  Locale locale) {
		List<List> bookList = null;
		List<String> l = new ArrayList();
		if("save".equals(button)) {
			//paste tn list
			l = bookService.parseExcelDataCol1(tnData);
			l = removeSpaces(l);
			String tns = bookService.generateQuotedListString(l);
			
			bookList = bookService.getSearchTnsListAllColumns(tns);  		 
	 	}
		
		//add outer join tns in code
		List<List> finalList = new ArrayList<List>();
		if(bookList != null && bookList.get(0) != null)
			finalList.add(bookList.get(0)); //add column headers
		
		boolean foundit = false;
		for(String tn: l) {
			for(List<String> row: bookList) {
				if(row.get(0).equals(tn)) {
					finalList.add(row);//just add original for returned from sql if it is there
					foundit = true;
					break;
				}
			}
			if(foundit == false) {
				List<String> temp = new ArrayList<String>();
				temp.add(tn);
				//need to add extra nulls columns?
				temp.add("NOT FOUND IN DB");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				 
				finalList.add(temp);
			}
			
			foundit = false;
		}
	 

		model.addAttribute("pageTitle", messageSource.getMessage("search.searchListOfTnsAllColumns", null, locale));
		//model.addAttribute("colLabels", labels);
		if( bookList == null ||  bookList.get(0) == null)
			model.addAttribute("colCount", 0);
		else
			model.addAttribute("colCount", bookList.get(0).size());
		model.addAttribute("allTnsInfo", finalList);  
 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteTnsExcel", null, locale));
		buttons.add(details);
	 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "searchListTns");//not used
		model.addAttribute("overlayAction", "searchListTnsAllColumns");
		return "search/miscButtonAndTableFormSearchTnList";
		
	}
	//This request mapping is for both GET and POST (ie before pasting data and after showing data)	
	@RequestMapping(value="search/searchListTns")
	public String getSearchListTns(String button, String tnData, Model model,  Locale locale) {
		List<List> bookList = null;
		List<String> l = new ArrayList();
		if("save".equals(button)) {
			//paste tn list
			l = bookService.parseExcelDataCol1(tnData);
			l = removeSpaces(l);
			String tns = bookService.generateQuotedListString(l);
			
			bookList = bookService.getSearchTnsList(tns);  		 
	 	}
		
		//add outer join tns in code
		List<List> finalList = new ArrayList<List>();
		boolean foundit = false;
		for(String tn: l) {
			for(List<String> row: bookList) {
				if(row.get(0).equals(tn)) {
					finalList.add(row);//just add original for returned from sql if it is there
					foundit = true;
					break;
				}
			}
			if(foundit == false) {
				List<String> temp = new ArrayList<String>();
				temp.add(tn);
				//need to add extra nulls columns?
				temp.add("NOT FOUND IN DB");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				temp.add("");
				 
				finalList.add(temp);
			}
			
			foundit = false;
		}
		
		//title and table labels
		List<String> labels = new ArrayList<String>();
		
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("secondaryIdentifier", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("subject", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale)); 
		labels.add(messageSource.getMessage("scanComplete", null, locale)); 
 
		labels.add(messageSource.getMessage("filesSentToOrem", null, locale)); 
		labels.add(messageSource.getMessage("filesReceivedByOrem", null, locale)); 
		labels.add(messageSource.getMessage("tiffOremDriveName", null, locale)); 
		labels.add(messageSource.getMessage("pdfReady", null, locale)); 
		labels.add(messageSource.getMessage("dateReleased", null, locale)); 
		labels.add(messageSource.getMessage("dateLoaded", null, locale)); 
		labels.add(messageSource.getMessage("url", null, locale)); 

		model.addAttribute("pageTitle", messageSource.getMessage("search.searchListOfTns", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", finalList);  
 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteTnsExcel", null, locale));
		buttons.add(details);
	 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "searchListTns");//not used
		model.addAttribute("overlayAction", "searchListTns");
		return "search/miscButtonAndTableForm";
		
	}
		
	List<String> removeSpaces(List<String> l){
		List<String> newList = new ArrayList<String>();
		for(String s: l) {
			if(s.endsWith(" ")) {
				s = s.trim();
			}
			newList.add(s);
		}
		return newList;
	}
 
	/////end search list tns page//////////


	/////start search list secondaryIdentifier page//////////
 
	//This request mapping is for both GET and POST (ie before pasting data and after showing data)	
	@RequestMapping(value="search/searchListSecondaryIds")
	public String getSearchListSecondaryIds(String button, String tnData, Model model,  Locale locale) {
		List<List> bookList = null;
		if("save".equals(button)) {
			//paste tn list
			List<String> l = bookService.parseExcelDataCol1(tnData);
			l = removeSpaces(l);
			String tns = bookService.generateQuotedListString(l);
			
			bookList = bookService.getSearchSecondaryIdsList(tns);  		 
	 	}
		
		//title and table labels
		List<String> labels = new ArrayList<String>();
		
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("secondaryIdentifier", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("subject", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale)); 
		labels.add(messageSource.getMessage("scanComplete", null, locale)); 

		model.addAttribute("pageTitle", messageSource.getMessage("search.searchListOfSecondaryIds", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookList);  
 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteTnsExcel", null, locale));
		buttons.add(details);
	 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "searchListSecondaryIds");//not used
		model.addAttribute("overlayAction", "searchListSecondaryIds");
		return "search/miscButtonAndTableForm";
		
	}
 
	/////end search list secondaryIdentifier page//////////

	/////start search URLs//////////
 
	//This request mapping is for both GET and POST (ie before pasting data and after showing data)
	@RequestMapping(value="search/searchForUrls")
	public String getSearchUrls(String button, String tnData, Model model,  Locale locale) {
		List<List> bookList = null;
		if("save".equals(button)) {
			//paste tn list
			List<String> l = bookService.parseExcelDataCol1(tnData);
			String tns = bookService.generateQuotedListString(l);
			
			bookList = bookService.getSearchUrlsList(tns);  		 
	 	}
		
		//title and table labels
		List<String> labels = new ArrayList<String>();
		
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("secondaryIdentifier", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("url", null, locale));

		model.addAttribute("pageTitle", messageSource.getMessage("search.searchListOfUrls", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookList);  
 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteTnsOrSecondarysExcel", null, locale));
		buttons.add(details);
	 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "searchForUrls");//not used
		model.addAttribute("overlayAction", "searchForUrls");
		
		model.addAttribute("urlColNum", "3");//flag for href from column 2 of data
		return "search/miscButtonAndTableForm";
		
	}
 
	/////end search URLs//////////

	/////start search PIDs//////////
 
	//This request mapping is for both GET and POST (ie before pasting data and after showing data)
	@RequestMapping(value="search/searchForPids")
	public String getSearchPids(String button, String tnData, Model model,  Locale locale) {
		List<List> bookList = null;
		if("save".equals(button)) {
			//paste tn list
			List<String> l = bookService.parseExcelDataCol1(tnData);
			String tns = bookService.generateQuotedListString(l);
			
			bookList = bookService.getSearchPidsList(tns);  		 
	 	}
		
		//title and table labels
		List<String> labels = new ArrayList<String>();
		
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("pid", null, locale));

		model.addAttribute("pageTitle", messageSource.getMessage("search.searchListOfPids", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookList);  
 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteTnsExcel", null, locale));
		buttons.add(details);
	 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "searchForPids");//not used
		model.addAttribute("overlayAction", "searchForPids");
		
		model.addAttribute("urlColNum", "2");//flag for href from column 2 of data
		return "search/miscButtonAndTableForm";
		
	}
 
	/////end search PIDs//////////
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
}