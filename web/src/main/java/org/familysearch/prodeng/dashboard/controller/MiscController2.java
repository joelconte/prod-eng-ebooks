/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.dashboard.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("dashboardMiscController2")
public class MiscController2 implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public MiscController2(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value="dashboard/dashboardPage2", method=RequestMethod.GET)
	public String get(HttpServletRequest req, Model model, Locale locale) {
		
		String year = req.getParameter("year");
		String month = req.getParameter("month");
		String startDate = "";
		String endDate = "";
		int daysDiff = 0;//
		if(year != null && month != null) {
			int yearInt = Integer.parseInt(year);
			int monthInt = Integer.parseInt(month);
			startDate = "01/01/" + year;
			GregorianCalendar gcStartOfYear = new GregorianCalendar(yearInt, 0, 1);//jan 1 of year selected
			
			GregorianCalendar gc = new GregorianCalendar(yearInt, monthInt-1, 1);
			gc.add(Calendar.MONTH, 1);//add 1 month
			gc.add(Calendar.DAY_OF_MONTH, -1);//subtract 1 day since endDate is inclusive
			int endMonthInt = gc.get(Calendar.MONTH)+1;
			int endDayInt = gc.get(Calendar.DAY_OF_MONTH);
			int endYearInt = gc.get(Calendar.YEAR);
			endDate = String.valueOf(endMonthInt) + "/" + String.valueOf(endDayInt) + "/" + String.valueOf(endYearInt);
			
			GregorianCalendar gcStart = new GregorianCalendar(yearInt, monthInt-1, 1);
			daysDiff = Math.abs(gc.get(Calendar.DAY_OF_YEAR)-gcStartOfYear.get(Calendar.DAY_OF_YEAR)) + 1;//inclusive  
		} else {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			   //get current date time  
			Calendar cal = Calendar.getInstance();
			int yearInt = cal.get(Calendar.YEAR);
			int monthInt = cal.get(Calendar.MONTH) + 1;
			startDate = "01/01/" + yearInt;
			GregorianCalendar gcStartOfYear = new GregorianCalendar(yearInt, 0, 1);//jan 1 of year current since no year selected yet
			
			GregorianCalendar gc = new GregorianCalendar(yearInt, monthInt-1, 1);
			gc.add(Calendar.MONTH, 1);//add 1 month
			gc.add(Calendar.DAY_OF_MONTH, -1);//subtract 1 day since endDate is inclusive
			int endMonthInt = gc.get(Calendar.MONTH)+1;
			int endDayInt = gc.get(Calendar.DAY_OF_MONTH);
			int endYearInt = gc.get(Calendar.YEAR);
			endDate = String.valueOf(endMonthInt) + "/" + String.valueOf(endDayInt) + "/" + String.valueOf(endYearInt);
			
			GregorianCalendar gcStart = new GregorianCalendar(yearInt, monthInt-1, 1);
			daysDiff = Math.abs(gc.get(Calendar.DAY_OF_YEAR)-gcStartOfYear.get(Calendar.DAY_OF_YEAR)) + 1;//inclusive  
			
			//set jsp vars for current year/month
			year = String.valueOf(yearInt);
			month = String.valueOf(monthInt);
		}
		
		model.addAttribute("pageTitle", messageSource.getMessage("dashboard.dashboardPageTitle", null, locale));
		List<String> sites =  bookService.getAllSites();
		model.addAttribute("allLocations",sites);
		String site = req.getParameter("site");
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("site", site);
		String productionData[][];
		
	 
	

		/////Goal Actual Pie Charts
		List data = bookService.getGoalsAndActuals(year, site);//4 elements goal, scan, process, publish
		if(data.size() == 4) {
			//some can be 0
			int goal =  Integer.parseInt((String)data.get(0));
			int scanActual =  Integer.parseInt((String)data.get(1));
			int processActual =  Integer.parseInt((String)data.get(2));
			int publishActual =  Integer.parseInt((String)data.get(3));
			int scanToDo = goal - scanActual;
			int processToDo = goal - processActual;
			int publishToDo = goal - publishActual;
			if(scanToDo < 0)
				scanToDo = 0;
			if(processToDo < 0)
				processToDo = 0;
			if(publishToDo < 0)
				publishToDo = 0;
			model.addAttribute("goal", goal);
			model.addAttribute("scanToDo", scanToDo);
			model.addAttribute("scanActual", scanActual);
			model.addAttribute("processToDo", processToDo);
			model.addAttribute("processActual", processActual);
			model.addAttribute("publishToDo", publishToDo);
			model.addAttribute("publishActual", publishActual);
			
			//actual results awaiting to be processed
			//can be calculated from above data
			int processedOfScannedToDo = scanActual;
			int processedOfScannedActual = processActual;
			model.addAttribute("processedOfScannedToDo", processedOfScannedToDo);//count of images scanned
			model.addAttribute("processedOfScannedActual", processedOfScannedActual);//count of images processed/ocr'd
			
			//actual results  awaiting to be publish
			//can be calculted from above data
			int publishedOfProcessedToDo = processActual;
			int publishedOfProcessedActual = publishActual;
			model.addAttribute("publishedOfProcessedToDo", publishedOfProcessedToDo);//count of images processed/ocr'd
			model.addAttribute("publishedOfProcessedActual", publishedOfProcessedActual);//count of images published
		}
 

		/////Open Issues
		List<List> openIssues = bookService.getDashboardOpenIssues();
		model.addAttribute("openIssues", openIssues);
		 
		
		/////Turnaround Time
		String[][] turnaroundData = bookService.getDashboardTurnaroundAverages(startDate, endDate, site);
		
		model.addAttribute("turnarounda0", turnaroundData[0][0]);
		model.addAttribute("turnarounda1", turnaroundData[0][1]);
		model.addAttribute("turnarounda2", turnaroundData[0][2]);
		model.addAttribute("turnarounda3", turnaroundData[0][3]);
		

		model.addAttribute("turnaroundb0", turnaroundData[1][0]);
		model.addAttribute("turnaroundb1", turnaroundData[1][1]);
		model.addAttribute("turnaroundb2", turnaroundData[1][2]);
		model.addAttribute("turnaroundb3", turnaroundData[1][3]);
		

		model.addAttribute("turnaroundc0", turnaroundData[2][0]);
		model.addAttribute("turnaroundc1", turnaroundData[2][1]);
		model.addAttribute("turnaroundc2", turnaroundData[2][2]);
		model.addAttribute("turnaroundc3", turnaroundData[2][3]);
		

		model.addAttribute("turnaroundd0", turnaroundData[3][0]);
		model.addAttribute("turnaroundd1", turnaroundData[3][1]);
		model.addAttribute("turnaroundd2", turnaroundData[3][2]);
		model.addAttribute("turnaroundd3", turnaroundData[3][3]);
		

		model.addAttribute("turnarounde0", turnaroundData[4][0]);
		model.addAttribute("turnarounde1", turnaroundData[4][1]);
		model.addAttribute("turnarounde2", turnaroundData[4][2]);
		model.addAttribute("turnarounde3", turnaroundData[4][3]);
		
 
		//big chart
		String[][] goalData = bookService.getDashboardByMonthDataScanProcessPublish(startDate, endDate, site, daysDiff);
		//model.addAttribute("goalsLabels", "[ \"3/4\", \"February\", \"March\", \"April\", \"May\", \"une\", \"July\" ]");
		//model.addAttribute("goals","[ 65, 59, 90, 81, 56, 55, 40 ]");
		//model.addAttribute("actuals","[ 23, 59, 10, 81, 56, 5, 40 ]");
		model.addAttribute("xAxisLabels", goalData[0][0]);
//goalData[0][1] = "[ 65, 59, 90, 81, 56, 65, 59, 90, 81, 56,65, 59, 90, 81, 56,65, 59, 0, 81, 56,65, 59, 90, 81, 56,65, 59, 90, 81, 56, 40 ]";
//goalData[0][1] = "[  0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ]";
		model.addAttribute("scanActuals", goalData[0][1]);
		model.addAttribute("processActuals", goalData[0][2]);
		model.addAttribute("publishActuals", goalData[0][3]);

		
		return "dashboard/dashboardPage2";
	}

	@RequestMapping(value="dashboard/dashboardPage2", method=RequestMethod.POST)
	public String post(HttpServletRequest req, Model model, Locale locale) {
		return get(req, model, locale);
	}
	
}