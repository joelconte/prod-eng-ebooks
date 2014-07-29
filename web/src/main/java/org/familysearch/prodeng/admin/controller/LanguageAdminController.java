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

@Controller("languageAdminController")
public class LanguageAdminController  implements MessageSourceAware{
	/*
	 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
	 * This notice may not be removed.
	 */


	private BookService bookService;
	private MessageSource messageSource;

	@Autowired
	public LanguageAdminController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


	//show populated form - for read (languageId can be null and will display empty fields)
	@RequestMapping(value="admin/languageAdmin", method=RequestMethod.GET)
	public String displayLanguages(Model model, Locale locale) {

		// title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("language", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.languages", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allInfo", bookService.getAllLanguageIdsAsRows());

		// buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");// flag
		details.add(messageSource.getMessage("pasteExcelLanguage", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("deleteSelectedLanguage");
		details.add(messageSource.getMessage("deleteSelectedLanguage", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);

		// form actions
		model.addAttribute("buttonsAction", "languageAdmin");
		model.addAttribute("overlayAction", "doLanguageAdminInsert");
		return "admin/miscButtonAndTableFormWithCheckboxSingleColumn";
	}

	@RequestMapping(value="admin/languageAdmin", method=RequestMethod.POST)
	public String getLanguageAdminPost(String button, HttpServletRequest request,  Model model, Locale locale) {
		if("deleteSelectedLanguage".equals(button)) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String languageList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						languageList = languageList + ", " + "'" + val[0] + "'";
				}
			}
			if(languageList.length()>1)
				languageList = languageList.substring(1);//remove starting comma

			bookService.deleteLanguages(languageList);

			return "redirect:languageAdmin";
		} 

		//should not happen
		return "redirect:languageAdmin"; //redirect get - guard against refresh-multi-updates and also update displayed url
	}

	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doLanguageAdminInsert", method=RequestMethod.POST)
	public String doLanguageAdminInsertPost(String button, String pastedData, Model model) {
		if(button.equals("save")) {

			List<List<String>> rows = bookService.parseExcelData(pastedData, -1);
			for(List<String> l: rows) {
				String val = l.get(0);
				bookService.createLanguage(val);
			}
			//bookService.insertBatch("languages", new String[]{"ID"}, new int[] {Types.VARCHAR}, rows); 
		}
		return "redirect:languageAdmin"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
		binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}



}