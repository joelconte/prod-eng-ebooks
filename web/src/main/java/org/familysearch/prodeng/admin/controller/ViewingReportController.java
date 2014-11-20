/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.admin.controller;

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

@Controller("viewingReportController")
public class ViewingReportController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public ViewingReportController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	////////////NewBooks metadata start/////////////
 
	@RequestMapping(value="admin/viewingReports", method=RequestMethod.GET)
	public String getViewingReports(HttpServletRequest req, Model model,  String showRows, String year, String month,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		//title and table labels
		List<String> labels = new ArrayList<String>();
		
		labels.add(messageSource.getMessage("pid", null, locale));
		labels.add(messageSource.getMessage("numberOfViews", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("accessRights", null, locale));
		labels.add(messageSource.getMessage("collection", null, locale));
		labels.add(messageSource.getMessage("tn", null, locale));
		labels.add(messageSource.getMessage("publisher", null, locale));
		labels.add(messageSource.getMessage("owningInstitution", null, locale));
		labels.add(messageSource.getMessage("ieUrl", null, locale));
		labels.add(messageSource.getMessage("reportDate", null, locale));
		 
		 
		//////
		List<List> vr = null;
		if(showRows != null) {
			vr = bookService.getViewingReports( year, month);
		}
		/*if(!model.containsAttribute("dupeInfo")) {
			//if already dupeTnsInfo, then just pasted in data and had dupe 
		
			String pidLListStr = "";
			for(List r: vr) {
				String pid = (String) r.get(0);
				pidLListStr += ", '" + pid + "'";
			}
			if(pidLListStr != "")
				pidLListStr = pidLListStr.substring(2);
			model.addAttribute("dupePidInfo", bookService.getDuplicatePidInfo(pidLListStr));   
			 
		}*/
		//////

		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.viewingReports", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allVRInfo", vr);  
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("showRows", showRows);
		//model.addAttribute("allSites", bookService.getAllSites()); 
		//model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url
	 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteExcel", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("deleteSelected");
		details.add(messageSource.getMessage("deleteSelectedRows", null, locale));
		buttons.add(details); 
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "viewingReports");
		model.addAttribute("overlayAction", "doViewingReportsInserts");
		return "admin/miscButtonAndTableFormWithCheckboxForViewingReport";
	}

	@RequestMapping(value="admin/viewingReports", method=RequestMethod.POST)
	public String doViewingReports(String button, HttpServletRequest request, String showRows, String year, String month,   Model model, Locale locale) {
		if(button.equals("deleteSelected") ) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();

			ArrayList<String> pidList = new ArrayList();
			ArrayList<String> dateList = new ArrayList();
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) {
						String d = val[0];
						String pid =  d.substring(0, d.indexOf("***"));
						String date = d.substring(d.indexOf("***")+3,d.indexOf("***")+10 );
						pidList.add(pid);
						dateList.add(date);
					}
				}
			}
		
			
			bookService.deleteSelectedViewingReports(pidList, dateList);
		} else if(button.equals("load") ) {
			//noop
		}  
		//return "redirect:viewingReports?showRows=true"; //redirect get - guard against refresh-multi-updates and also update displayed url
		return getViewingReports(request, model, showRows,  year,   month, locale);
	}
	 
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doViewingReportsInserts", method=RequestMethod.POST)
	public String doViewingReportsInsertsPost(HttpServletRequest req, String doUpdates, String button,  String showRows, String year, String month, String pastedData, Principal principal,  Model model, Locale locale) {
		if(button.equals("save")) {

			List<List<String>> rows = bookService.parseExcelData(pastedData, 9);//!!need to update count when add new columnds
			String report_date = month + "/" + "01" + "/" + year;
			addDateColumn(rows, report_date);
		
			List<String> pidList = new ArrayList();
			List<String> dateList = new ArrayList();
			for(List<String> r : rows) {
				String pid =  r.get(0);
				String date =  report_date;
				pidList.add(pid);
				dateList.add(date);
			}
			 
			String dupPidList = bookService.getDuplicatesInViewingReport(pidList, dateList); //get list of pid/date already in table
			if(dupPidList != "") {
				//just display error
				model.addAttribute("bookErrorMessage",  messageSource.getMessage("viewingReportDupeError", null, locale) + "\n" + dupPidList);
				return "errors/generalError";
			}  
	
			bookService.insertBatch("BOOKviewingstats", new String[]{"pid", "num_of_views", "title", "access_rights", "collection", "tn", "publisher", "owning_institution", "ie_url", "report_date"}, 
					new int[] {Types.VARCHAR,  Types.NUMERIC, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE }, rows); 
		}
		return getViewingReports(req, model, showRows,  year,   month, locale);
//		return "redirect:viewingReports"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
	public void addDateColumn(List<List<String>>rows, String report_date) {
		for(List<String> r : rows) {
			//current_timstamp values are generated in the insertBatch(), so no value should be added here 
			r.add( report_date );


		}
	}


} 
 