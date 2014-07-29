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

@Controller("problemReasonAdminController")
public class ProblemReasonAdminController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public ProblemReasonAdminController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	 
	 
	//show populated form - for read (problemReasonId can be null and will display empty fields)
	@RequestMapping(value="admin/problemReasonAdmin", method=RequestMethod.GET)
	public String displayproblemReasons(Model model, Locale locale) {

		// title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("problemReason", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.problemReasons", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allInfo", bookService.getAllProblemReasons());

		// buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");// flag
		details.add(messageSource.getMessage("pasteExcelProblemReason", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("deleteSelectedProblemReason");
		details.add(messageSource.getMessage("deleteSelectedProblemReason", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);

		// form actions
		model.addAttribute("buttonsAction", "problemReasonAdmin");
		model.addAttribute("overlayAction", "doProblemReasonAdminInsert");
		return "admin/miscButtonAndTableFormWithCheckboxSingleColumn";
	}
	 
	@RequestMapping(value="admin/problemReasonAdmin", method=RequestMethod.POST)
	public String getproblemReasonAdminPost(String button, HttpServletRequest request,  Model model, Locale locale) {
		if("deleteSelectedProblemReason".equals(button)) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String problemReasonList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						problemReasonList = problemReasonList + ", " + "'" + val[0] + "'";
				}
			}
			if(problemReasonList.length()>1)
				problemReasonList = problemReasonList.substring(1);//remove starting comma
			
			bookService.deleteProblemReasons(problemReasonList);
		
			return "redirect:problemReasonAdmin";
		} 
		
		//should not happen
		return "redirect:problemReasonAdmin"; //redirect get - guard against refresh-multi-updates and also update displayed url
	}
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doProblemReasonAdminInsert", method=RequestMethod.POST)
	public String doProblemReasonAdminInsertPost(String button, String pastedData, Model model) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(pastedData, -1);
			bookService.insertBatch("problemReason", new String[]{"ID"}, new int[] {Types.VARCHAR}, rows); 
		}
		return "redirect:problemReasonAdmin"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
 
	 
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
 
	
}