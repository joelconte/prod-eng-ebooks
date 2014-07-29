/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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

@Controller("bookLockAdminController")
public class BookLockAdminController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public BookLockAdminController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	 
	 
	//show populated form - for read (bookLockId can be null and will display empty fields)
	@RequestMapping(value="admin/bookLockAdmin", method=RequestMethod.GET)
	public String displayBookLocks(Model model, Locale locale) {

		// title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("operator", null, locale));
		labels.add(messageSource.getMessage("dateLocked", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.bookLocks", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allInfo", bookService.getAllBookLocks());

		// buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("deleteSelectedBookLocks");
		details.add(messageSource.getMessage("deleteSelectedBookLocks", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);

		// form actions
		model.addAttribute("buttonsAction", "bookLockAdmin");
		return "admin/miscButtonAndTableFormWithCheckboxSingleColumn";
	}
	 
	@RequestMapping(value="admin/bookLockAdmin", method=RequestMethod.POST)
	public String getBookLockAdminPost(String button, HttpServletRequest request,  Model model, Locale locale) {
		if("deleteSelectedBookLocks".equals(button)) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String bookLockList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						bookLockList = bookLockList + ", " + "'" + val[0] + "'";
				}
			}
			if(bookLockList.length()>1)
				bookLockList = bookLockList.substring(1);//remove starting comma
			
			bookService.deleteBookLocks(bookLockList);
		
			return "redirect:bookLockAdmin";
		} 
		
		//should not happen
		return "redirect:bookLockAdmin"; //redirect get - guard against refresh-multi-updates and also update displayed url
	}
	  
	 
 
	 
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
 
	
}