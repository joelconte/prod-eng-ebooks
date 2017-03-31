/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

@Controller("batchUpdateAdminController")
public class BatchUpdateAdminController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public BatchUpdateAdminController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	 
	//show populated form - for read (id can be null and will display empty fields)
	@RequestMapping(value="admin/batchUpdateAdmin",   method=RequestMethod.GET)
	public String displayBatchUpdateRead(String newValue, String tnData, String prevTnData, String columnName,   Model model,  Locale locale) {
	    model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.batchUpdateAdmin", null, locale));
 

		model.addAttribute("allColumnNames", bookService.getAllBookColumnNames()); //query all book column names
		model.addAttribute("columnName", columnName);
		model.addAttribute("newValue", newValue);
		model.addAttribute("prevTnData", prevTnData);//for redisplay between any operations
		
	    List<String> l = null;
	    if(tnData != null) {
	    	l = bookService.parseExcelDataCol1(tnData);
	    }
	    String tns = bookService.generateQuotedListString(l);

		//title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		if(columnName == null || columnName.equals(""))
			columnName = "---";
		labels.add(columnName);//name of column selected to show label in table

		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getAllTnsAndColumn(columnName, tns));//query tn and column selected
 
		
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteTnsExcel", null, locale));
		buttons.add(details);
		 
	 
		model.addAttribute("buttons", buttons);
		
		//form actions
		//model.addAttribute("buttonsAction", "searchListSecondaryIds");
		model.addAttribute("overlayAction", "batchUpdateAdmin");
		return "admin/miscButtonAndTableFormBatch";
	 
		//return "admin/siteAdmin";
	}
	
	
	//do processing of pasted TNs 
	@RequestMapping(value="admin/batchUpdateAdmin", method=RequestMethod.POST)
	public String doInsertAddTnListPost(HttpServletRequest req,  Principal principal, String button, String newValue, String tnData, String prevTnData, String columnName,  Model model,  Locale locale) {
	
		if(button==null) {
			tnData = prevTnData; //for redisplay
			return displayBatchUpdateRead(newValue, tnData, prevTnData, columnName,  model, locale);
		}
		else if(button.equals("save")) {
			//copy/paste new tns into form..
			//replace prevTnData
			prevTnData = tnData;
			return displayBatchUpdateRead(newValue, tnData, prevTnData, columnName,  model, locale);
		 
		}
		else if(button.equals("saveNewValue")) {
			tnData = prevTnData; //for redisplay
			List<String> l = null;
			if(tnData != null) {
				l = bookService.parseExcelDataCol1(tnData);
			}
			String tns = bookService.generateQuotedListString(l);
			bookService.saveUpdatedColumnValue(principal.getName(), l, tns, columnName, newValue);//do update
			
			return displayBatchUpdateRead(newValue, tnData, prevTnData, columnName,  model, locale);
		}
		else if(button.equals("cancel")) {
			return "redirect:batchUpdateAdmin";
		}
		return "redirect:batchUpdateAdmin"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	
	 
	
	
	 
 
	 
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
 
	
}