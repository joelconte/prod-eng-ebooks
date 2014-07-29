package org.familysearch.prodeng.admin.controller;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("adminPostMiscController")
public class PostMiscController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public PostMiscController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	////////////Released Books start////////////
	
	@RequestMapping(value="admin/releasedBooks", method=RequestMethod.GET)
	public String getReleasedBooks(Model model, Locale locale) {
		//title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("dateReleased", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.releasedBooksOnlineForm", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getAdminReleasedBooksTnsInfo()); 

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
		details.add("checkSiteRelatedDate");
		details.add(messageSource.getMessage("checkSiteRelatedDate", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkBatchClass");
		details.add(messageSource.getMessage("checkBatchClass", null, locale));	
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("move");
		details.add(messageSource.getMessage("move", null, locale));	
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("clearQueryFormAndClose");
		details.add(messageSource.getMessage("clearQueryFormAndClose", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "releasedBooks");
		model.addAttribute("overlayAction", "doReleasedBooksInsertTns");
		return "admin/miscButtonAndTableForm";
	}
	
	@RequestMapping(value="admin/releasedBooks", method=RequestMethod.POST)
	public String getReleasedBooksPost(String button, Model model, Locale locale) {
		if("checkTns".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.releaseEntryWithoutMatchingBooks", null, locale));
			model.addAttribute("colLabels", labels); 
			model.addAttribute("allTnsInfo", bookService.getAdminReleaseEntryWithoutMatchingBooksTnsInfo()); 
			return "admin/miscBookList";
		}else if("checkSiteRelatedDate".equals(button)) {
			List<String> labels = new ArrayList<String>();
			 
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("dateReleased", null, locale));
			labels.add(messageSource.getMessage("site", null, locale));
			labels.add(messageSource.getMessage("numOfPages", null, locale));
			labels.add(messageSource.getMessage("scannedBy", null, locale));
			labels.add(messageSource.getMessage("scannedCompleteDate", null, locale));
			labels.add(messageSource.getMessage("filesReceivedByOrem", null, locale));
			labels.add(messageSource.getMessage("imageAudit", null, locale));
			labels.add(messageSource.getMessage("iaStartDate", null, locale));
			labels.add(messageSource.getMessage("iaCompleteDate", null, locale));
			labels.add(messageSource.getMessage("importedBy", null, locale));
			labels.add(messageSource.getMessage("importedDate", null, locale));
			labels.add(messageSource.getMessage("pdfreviewBy", null, locale));
			labels.add(messageSource.getMessage("pdfreviewStartDate", null, locale));
			labels.add(messageSource.getMessage("pdfReady", null, locale));
			labels.add(messageSource.getMessage("pdfSentToLoad", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.releaseEntryWithDateAlreadyAdded", null, locale));
			model.addAttribute("colLabels", labels);
			model.addAttribute("allTnsInfo", bookService.getAdminReleaseEntryWithMatchingDateTnsInfo()); 
			return "admin/miscBookList";
		}else if("checkBatchClass".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("batchClass", null, locale));
			labels.add(messageSource.getMessage("scannedBy", null, locale));
			labels.add(messageSource.getMessage("numOfImages", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.releaseEntryBatchClass", null, locale));
			model.addAttribute("colLabels", labels);
			model.addAttribute("allTnsInfo", bookService.getAdminReleaseEntryBatchClassTnsInfo()); 
			return "admin/miscBookList";
		}else if("move".equals(button)) {
			bookService.updateReleaseEntry();
			bookService.deleteReleaseEntry();
			return "redirect:releasedBooks";  //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if("clearQueryFormAndClose".equals(button)) {
			 bookService.deleteReleaseEntry();
			 return "redirect:switchboard"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}

		//should not happen
		return "redirect:releasedBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doReleasedBooksInsertTns", method=RequestMethod.POST)
	public String doInsertTnsReleasedBooksPost(String button, String tnData, Model model) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(tnData, -1);
			bookService.insertBatch("TF_Released_ENTRY", new String[]{"TN", "DATE_RELEASED"}, new int[] {Types.VARCHAR, Types.TIMESTAMP}, rows); 
		}
		return "redirect:releasedBooks"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 


	////////////Released Books end/////////////
 
	
	////////////Books Loaded Online start////////////
	
	@RequestMapping(value="admin/booksLoadedOnline", method=RequestMethod.GET)
	public String getBooksLoadedOnline(Model model, Locale locale) {
		//title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("collection", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		labels.add(messageSource.getMessage("loadedBy", null, locale));
		labels.add(messageSource.getMessage("pagesOnline", null, locale));
		labels.add(messageSource.getMessage("url", null, locale));
		labels.add(messageSource.getMessage("pid", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.booksLoadedOnlineForm", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getAdminBooksLoadedOnlinTnsInfo());  

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
		details.add("checkPid");
		details.add(messageSource.getMessage("checkPid", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("update");
		details.add(messageSource.getMessage("updateDatabase", null, locale));	
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("clearQueryForm");
		details.add(messageSource.getMessage("clearQueryForm", null, locale));	
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("clearQueryFormAndClose");
		details.add(messageSource.getMessage("clearQueryFormAndClose", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "booksLoadedOnline");
		model.addAttribute("overlayAction", "doBooksLoadedOnlineInsertTns");
		return "admin/miscButtonAndTableForm";
	}
	
	@RequestMapping(value="admin/booksLoadedOnline", method=RequestMethod.POST)
	public String getBooksLoadedOnlinePost(String button, Model model, Locale locale) {
		if("checkTns".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.loadingEntryWithoutMatchingBooks", null, locale));
			model.addAttribute("colLabels", labels); 
			model.addAttribute("allTnsInfo", bookService.getAdminLoadingEntryWithoutMatchingBooksTnsInfo()); 
			return "admin/miscBookList";
		}else if("checkPid".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("dateLoaded", null, locale));
			labels.add(messageSource.getMessage("pid", null, locale));
			labels.add(messageSource.getMessage("loadingEntry", null, locale));
			labels.add(messageSource.getMessage("pidMatch", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.loadingEntryWithDateAlreadyAdded", null, locale));
			model.addAttribute("colLabels", labels);
			model.addAttribute("allTnsInfo", bookService.getAdminLoadingEntryWithMatchingDateTnsInfo()); 
			return "admin/miscBookList";
		}else if("update".equals(button)) {
			bookService.updateLoadingEntry();
			bookService.deleteLoadingEntry(); 
			return "redirect:booksLoadedOnline";  //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if("clearQueryForm".equals(button)) {
			bookService.deleteLoadingEntry();
			return "redirect:booksLoadedOnline";  //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if("clearQueryFormAndClose".equals(button)) {
			 bookService.deleteLoadingEntry();
			 return "redirect:switchboard"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}

		//should not happen
		return "redirect:booksLoadedOnline"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doBooksLoadedOnlineInsertTns", method=RequestMethod.POST)
	public String doInsertTnsbooksLoadedOnlinePost(String button, String tnData, Model model) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(tnData, -1);
			bookService.insertBatch("TF_LOADING_ENTRY", new String[]{"TN", "COLLECTION", "DATE_LOADED", "LOADED_BY", "PAGES_ONLINE", "URL", "PID"}, new int[] {Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, rows); 
		}
		return "redirect:booksLoadedOnline"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 


	////////////Books Loaded Online end/////////////
 
}
