/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.report.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import org.springframework.web.bind.annotation.RequestParam;

@Controller("reportMiscController")
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

	@RequestMapping(value="report/monthlyReport", method=RequestMethod.GET)
	public String getMonthlyReport( String year, String month, String scannedBy, Model model, Locale locale) {
		model.addAttribute("month", month); 
		model.addAttribute("year", year); 
		model.addAttribute("site", scannedBy); 
		
		model.addAttribute("allYears", bookService.getAllYears()); 
		model.addAttribute("allSites", bookService.getAllSites()); 
		String titles_scanned = "0";
		String pages_scanned = "0";
		String titles_kofaxed = "0";
		String pages_kofaxed = "0";
		String titles_loaded = "0";
		String pages_loaded = "0";
		if(month!=null && year!=null && scannedBy!=null)
		{
			List<List> stats = bookService.getStatsFinal( year, month, scannedBy);
			if(stats != null && stats.size()>0 && stats.get(0).get(0) != null ) {
				titles_scanned = (String)stats.get(0).get(0);
				pages_scanned = (String)stats.get(0).get(1);
				titles_kofaxed = (String)stats.get(0).get(2);
				pages_kofaxed = (String)stats.get(0).get(3);
				titles_loaded = (String)stats.get(0).get(4);
				pages_loaded = (String)stats.get(0).get(5);
			}
		}
		model.addAttribute("titles_scanned", titles_scanned); 
		model.addAttribute("pages_scanned", pages_scanned); 
		model.addAttribute("titles_kofaxed", titles_kofaxed); 
		model.addAttribute("pages_kofaxed", pages_kofaxed); 
		model.addAttribute("titles_loaded", titles_loaded); 
		model.addAttribute("pages_loaded", pages_loaded); 
		return "report/monthlyReport";
	}

	@RequestMapping(value="report/monthlyReport", method=RequestMethod.POST)
	public String getMonthlyReportPost(String year, String month, String scannedBy, Model model, Locale locale) {
		return getMonthlyReport(year, month, scannedBy, model, locale);
	}
	  
	
	////////////button click reports//////////////
	@RequestMapping(value="report/miscReport", params="showTns", method=RequestMethod.POST)
	public String getMiscReport1(String year, String month,  String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		 
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.showTns", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsTn(year, month, scannedBy)); 
		return "report/miscBookListReport";
	}
	
	@RequestMapping(value="report/miscReport", params="urlList", method=RequestMethod.POST)
	public String getMiscReport2(String year, String month, String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("url", null, locale));
		labels.add(messageSource.getMessage("pid", null, locale));
		 
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.urlList", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsUrlList(year, month, scannedBy)); 
		return "report/miscBookListReport";
	}
 
	@RequestMapping(value="report/miscReport", params="titleList", method=RequestMethod.POST)
	public String getMiscReport3(String year, String month, String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("url", null, locale));
		labels.add(messageSource.getMessage("collection", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.titleList", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsTitleList(year, month, scannedBy)); 
		return "report/miscBookListReport";
	}
	@RequestMapping(value="report/miscReport", params="genealogyToday", method=RequestMethod.POST)
	public String getMiscReport4(String year, String month, String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("url", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		
		 
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.genealogyToday", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsGenealogyToday(year, month, scannedBy)); 
		return "report/miscBookListReport";
	}
	
	@RequestMapping(value="report/miscReport", params="scannedByReport", method=RequestMethod.POST)
	public String getMiscReport5(String year, String month, String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		labels.add(messageSource.getMessage("batchClass", null, locale));
		labels.add(messageSource.getMessage("url", null, locale));
		labels.add(messageSource.getMessage("pid", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		 
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.scannedByReport", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsScannedByReport(year, month, scannedBy)); 
		return "report/miscBookListReport";
	}

	@RequestMapping(value="report/miscReport", params="monthlyUrls", method=RequestMethod.POST)
	public String getMiscReport6(String year, String month, String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("url", null, locale));
		labels.add(messageSource.getMessage("site", null, locale)); 
		labels.add(messageSource.getMessage("scannedBy", null, locale));
		 
		 
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.monthlyUrls", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsMonthlyUrls(year, month, scannedBy)); 
		return "report/miscBookListReport";
	}
			

	@RequestMapping(value="report/miscReport", params="scannedByTns", method=RequestMethod.POST)
	public String getMiscReport7(String year, String month, String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		 
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.scannedByTns", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsScannedByTns()); 
		return "report/miscBookListReport";
	}

	@RequestMapping(value="report/miscReport", params="tnsThey", method=RequestMethod.POST)
	public String getMiscReport8(String year, String month, String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		 
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.tnsThey", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsTnsThey(year, month, scannedBy)); 
		return "report/miscBookListReport";
	}

	@RequestMapping(value="report/miscReport", params="tnsWe", method=RequestMethod.POST)
	public String getMiscReport9(String year, String month, String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("dateLoaded", null, locale));
		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("url", null, locale));
		labels.add(messageSource.getMessage("dateReleased", null, locale));
 
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.tnsWe", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsTnsWe(year, month, scannedBy)); 
		return "report/miscBookListReport";
	}
			 
	@RequestMapping(value="report/miscReport", params="countPerMonth", method=RequestMethod.POST)
	public String getMiscReport10(String year, String month, String scannedBy, Model model, Locale locale) {
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("numOfPages", null, locale));
		labels.add(messageSource.getMessage("numOfItems", null, locale));
		labels.add(messageSource.getMessage("year", null, locale));
		labels.add(messageSource.getMessage("month", null, locale));
		 
		model.addAttribute("pageTitle", messageSource.getMessage("report.pageTitle.countPerMonth", null, locale) + " (" + year + " " + month + " " + scannedBy + ")");
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getStatsCountPerMonth(year, month, scannedBy)); 
		return "report/miscBookListReport";
	}
			 
	
	
	////////////button click reports end //////////////
 

	 
	//show populated form - for read (tn can be null and will display empty fields)
	@RequestMapping(value="report/trackingForm", params="read", method=RequestMethod.GET)
	public String displayBookRead(@RequestParam(value = "tn", required=false) String tn, String fetchAllTns, Model model) {
		 
		return "redirect:../admin/trackingForm?read&tn=" + tn;
	} 
	
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
 
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
}