/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.dashboard.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
		
		String startDate = req.getParameter("startDate");//"year");
		String endDate = req.getParameter("endDate");
		String endDateYMD = null;
		
		//if no dates passed in, then just use beginning of year and current date
		if(startDate==null || startDate == "") {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			   //get current date time  
			Calendar cal = Calendar.getInstance();
			int yearInt = cal.get(Calendar.YEAR);
			int monthInt = cal.get(Calendar.MONTH) + 1;
			startDate = "01/01/" + yearInt;
		}
		if(endDate==null || endDate == "") {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			   //get current date time  
			Calendar cal = Calendar.getInstance();
			int yearInt = cal.get(Calendar.YEAR);
			int monthInt = cal.get(Calendar.MONTH) + 1;
			int dayInt = cal.get(Calendar.DAY_OF_MONTH);
			endDate = monthInt + "/" + dayInt + "/" + yearInt;
		}
		
		//fomEndDate
		int i1 = endDate.indexOf("/");
		String mE = endDate.substring(0, i1);
		String rem = endDate.substring(i1 + 1 );
		i1 = rem.indexOf("/");
		String dE = rem.substring(0, i1);
		rem = rem.substring(i1 + 1 );
		String yE = rem;
		if(dE.length()==1)
			dE = "0" + dE;
		if(mE.length()==1)
			mE = "0" + mE;
		endDateYMD = yE + "/"+ mE + "/" + dE;
				
		int endMonthIntPieChart = Integer.parseInt(mE);
				
		
		String year = null;
		if(endDate != null && endDate.length()>7) {
			int i = endDate.indexOf("/");
			i = endDate. indexOf( "/", i+1);
			year = endDate.substring(i+1, i+5);//All YTD graphs based on this year
		}
		
		//String month = req.getParameter("month");
		//fom is first of month
		String fomStartDate = "";
		String fomEndDate = "";
 		String fomEndDateYMD = "";
 		String fomStartDateCurrentMonth = "";
		int daysDiff = 0;
		int endMonthInt = 0;
		String endingMonthLabel = "";
		
		if(year != null) {		
			int yearInt = Integer.parseInt(year);
			Calendar cal = Calendar.getInstance();
			int monthInt = cal.get(Calendar.MONTH) + 1;
			fomStartDate = "01/01/" + year;
			GregorianCalendar gcStartOfYear = new GregorianCalendar(yearInt, 0, 1);//jan 1 of year selected
			
			GregorianCalendar gc = new GregorianCalendar(yearInt, Integer.parseInt(mE)-1, 1);
			gc.add(Calendar.MONTH, 1);//add 1 month
			gc.add(Calendar.DAY_OF_MONTH, -1);//subtract 1 day since fomEndDate is inclusive
			endingMonthLabel = gc.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.getDefault());
 
			endMonthInt = gc.get(Calendar.MONTH)+1;
			int endDayInt = gc.get(Calendar.DAY_OF_MONTH);
			int endYearInt = gc.get(Calendar.YEAR);
			String dd = String.valueOf(endDayInt);
			String mm = String.valueOf(endMonthInt);
			dd = dd.length() == 1 ? "0" + dd : dd;
			mm = mm.length() == 1 ? "0" + mm : mm;
			fomEndDate = mm + "/" + dd + "/" + String.valueOf(endYearInt);
			fomStartDateCurrentMonth = mm + "/" + "01" + "/" + String.valueOf(endYearInt);//first of current month starting 
			fomEndDateYMD =  String.valueOf(endYearInt) + "/" + mm + "/" + dd;
			
			GregorianCalendar gcStart = new GregorianCalendar(yearInt, monthInt-1, 1);
			daysDiff = Math.abs(gc.get(Calendar.DAY_OF_YEAR)-gcStartOfYear.get(Calendar.DAY_OF_YEAR)) + 1;//inclusive  
		} else {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			   //get current date time  
			Calendar cal = Calendar.getInstance();
			int yearInt = cal.get(Calendar.YEAR);
			int monthInt = cal.get(Calendar.MONTH) + 1;
			fomStartDate = "01/01/" + yearInt;
			GregorianCalendar gcStartOfYear = new GregorianCalendar(yearInt, 0, 1);//jan 1 of year current since no year selected yet
			
			GregorianCalendar gc = new GregorianCalendar(yearInt, monthInt-1, 1);
			gc.add(Calendar.MONTH, 1);//add 1 month
			gc.add(Calendar.DAY_OF_MONTH, -1);//subtract 1 day since fomEndDate is inclusive
			endingMonthLabel = gc.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			endMonthInt = gc.get(Calendar.MONTH)+1;
			int endDayInt = gc.get(Calendar.DAY_OF_MONTH);
			int endYearInt = gc.get(Calendar.YEAR);
			String dd = String.valueOf(endDayInt);
			String mm = String.valueOf(endMonthInt);
			dd = dd.length() == 1 ? "0" + dd : dd;
			mm = mm.length() == 1 ? "0" + mm : mm;
			fomEndDate = mm + "/" + dd + "/" + String.valueOf(endYearInt);
			fomEndDateYMD =  String.valueOf(endYearInt) + "/" + mm + "/" + dd;
			fomStartDateCurrentMonth = mm + "/" + "01" + "/" + String.valueOf(endYearInt);//first of current month starting 
			
			GregorianCalendar gcStart = new GregorianCalendar(yearInt, monthInt-1, 1);
			daysDiff = Math.abs(gc.get(Calendar.DAY_OF_YEAR)-gcStartOfYear.get(Calendar.DAY_OF_YEAR)) + 1;//inclusive  
			
			//set jsp vars for current year/month
			year = String.valueOf(yearInt);
			//month = String.valueOf(monthInt);
		}
		
		model.addAttribute("pageTitle", messageSource.getMessage("dashboard.dashboardPageTitle", null, locale) + " - through " + endingMonthLabel );
		List<String> sites =  bookService.getAllScanSites();
		sites.add(0, "All Sites");
		model.addAttribute("allScanLocations",sites);
		String site = req.getParameter("site");
		if(site==null) {
			site = "All Sites";
		}
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("year", year);
		//model.addAttribute("month", month);
		model.addAttribute("site", site);
		model.addAttribute("endingMonthLabel", endingMonthLabel);
		String productionData[][];
		
	 
	

		/////PIE charts - YTDGoal and YTDscan and YTDpublish Actual Pie Charts
		List<List>  data = bookService.getGoalsAndActuals(year, endMonthIntPieChart, endDateYMD, site);// list of rows(site,YTDGoal,scanYTD,scanYTDTodo,publishYTD,publishYTDTodo) 
	 	//convert List<List> to js 2-dim array string
		String twoDimArrayStr = "[";
		for(List r : data) {
			String a = "['" + r.get(0) + "'," + r.get(1) + "," + r.get(2) + "," + r.get(3) + "," + r.get(4) + "," + r.get(5) + "],";
			twoDimArrayStr += a;
		}
		twoDimArrayStr = twoDimArrayStr.substring(0, twoDimArrayStr.length()-1) + "]";//trim comma
		
		model.addAttribute("twoDimArrayStr",  twoDimArrayStr); 
		model.addAttribute("ytdPiesCount",  data.size()); 
 

		/////Open Issues
		List<List> openIssues = bookService.getDashboardOpenIssues();
		model.addAttribute("openIssues", openIssues);
		 
		/*
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
		*/
 
		//big chart	 
		String[][] goalData = bookService.getDashboardGoalData(fomStartDate, fomEndDate, site);
		//model.addAttribute("goalsLabels", "[ \"3/4\", \"Feb\", \"Ma\", \"Apr\", \"May\", \"une\", \"July\" ]");
		//model.addAttribute("goals","[ 65, 59, 90, 81, 56, 55, 40 ]");
		//model.addAttribute("actuals","[ 23, 59, 10, 81, 56, 5, 40 ]");
		model.addAttribute("goalsLabels", goalData[0][0]);
		model.addAttribute("goals", goalData[0][1]);
		model.addAttribute("scan", goalData[0][2]);
		model.addAttribute("publish", goalData[0][3]);
		
		
		

		////////////////////////production sparklines
		productionData = bookService.getDashboardAverages(startDate, endDate, site);
		
	 
		model.addAttribute("a0", productionData[0][0]);
		model.addAttribute("a1", productionData[0][1]);
		model.addAttribute("a2", productionData[0][2]);
		model.addAttribute("a3", productionData[0][3]);
		
		model.addAttribute("b0", productionData[1][0]);
		model.addAttribute("b1", productionData[1][1]);
		model.addAttribute("b2", productionData[1][2]);
		model.addAttribute("b3", productionData[1][3]);
		
		model.addAttribute("c0", productionData[2][0]);
		model.addAttribute("c1", productionData[2][1]);
		model.addAttribute("c2", productionData[2][2]);
		model.addAttribute("c3", productionData[2][3]);
		
		model.addAttribute("d0", productionData[3][0]);
		model.addAttribute("d1", productionData[3][1]);
		model.addAttribute("d2", productionData[3][2]);
		model.addAttribute("d3", productionData[3][3]);
		
		model.addAttribute("e0", productionData[4][0]);
		model.addAttribute("e1", productionData[4][1]);
		model.addAttribute("e2", productionData[4][2]);
		model.addAttribute("e3", productionData[4][3]);
		
		model.addAttribute("f0", productionData[5][0]);
		model.addAttribute("f1", productionData[5][1]);
		model.addAttribute("f2", productionData[5][2]);
		model.addAttribute("f3", productionData[5][3]); 
		 
		
		 
		/////Quality
		String[][] qualityData = bookService.getDashboardQualityAverages(startDate, endDate, site);
		
		model.addAttribute("quala0", qualityData[0][0]);
		model.addAttribute("quala1", qualityData[0][1]);
		model.addAttribute("quala2", qualityData[0][2]);
		model.addAttribute("quala3", qualityData[0][3]);
		

		model.addAttribute("qualb0", qualityData[1][0]);
		model.addAttribute("qualb1", qualityData[1][1]);
		model.addAttribute("qualb2", qualityData[1][2]);
		model.addAttribute("qualb3", qualityData[1][3]);
		

		model.addAttribute("qualc0", qualityData[2][0]);
		model.addAttribute("qualc1", qualityData[2][1]);
		model.addAttribute("qualc2", qualityData[2][2]);
		model.addAttribute("qualc3", qualityData[2][3]);
		

		/////Image Auditor Averages
		String[][] auditData = bookService.getDashboardAuditor(startDate, endDate, site);
		
		model.addAttribute("audita0", auditData[0][0]);
		model.addAttribute("audita1", auditData[0][1]);
		model.addAttribute("audita2", auditData[0][2]);
		model.addAttribute("audita3", auditData[0][3]);
		

		model.addAttribute("auditb0", auditData[1][0]);
		model.addAttribute("auditb1", auditData[1][1]);
		model.addAttribute("auditb2", auditData[1][2]);
		model.addAttribute("auditb3", auditData[1][3]);
		

		model.addAttribute("auditc0", auditData[2][0]);
		model.addAttribute("auditc1", auditData[2][1]);
		model.addAttribute("auditc2", auditData[2][2]);
		model.addAttribute("auditc3", auditData[2][3]);
		

		model.addAttribute("auditd0", auditData[3][0]);
		model.addAttribute("auditd1", auditData[3][1]);
		model.addAttribute("auditd2", auditData[3][2]);
		model.addAttribute("auditd3", auditData[3][3]);
		
 
 

		/////Top 5 issues
		String[][] topData = bookService.getDashboardTop5(startDate, endDate, site);
		
		model.addAttribute("topa0", topData[0][0]);
		model.addAttribute("topa1", topData[0][1]);
		model.addAttribute("topa2", topData[0][2]);
		model.addAttribute("topa3", topData[0][3]);
		

		model.addAttribute("topb0", topData[1][0]);
		model.addAttribute("topb1", topData[1][1]);
		model.addAttribute("topb2", topData[1][2]);
		model.addAttribute("topb3", topData[1][3]);
		

		model.addAttribute("topc0", topData[2][0]);
		model.addAttribute("topc1", topData[2][1]);
		model.addAttribute("topc2", topData[2][2]);
		model.addAttribute("topc3", topData[2][3]);
		

		model.addAttribute("topd0", topData[3][0]);
		model.addAttribute("topd1", topData[3][1]);
		model.addAttribute("topd2", topData[3][2]);
		model.addAttribute("topd3", topData[3][3]);
		

		model.addAttribute("tope0", topData[4][0]);
		model.addAttribute("tope1", topData[4][1]);
		model.addAttribute("tope2", topData[4][2]);
		model.addAttribute("tope3", topData[4][3]);
 
		


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
		
 
		
		
		///////////HORIZONTAL LINE MINI CHARTS////////////////////////////////////////////
		//calls valid for: Site, "all sites", "allFhc", "allPartnerLibs", "allInternetArchives"
		String[][] horizontalLineDataOneSite;
		String[][] horizontalLineDataFHL;
		String[][] horizontalLineDataPartnerLibraries;
		String[][] horizontalLineDataInternetArchive;
		String[][] horizontalLineDataInternetArchiveRt;
		String[][] horizontalLineDataGmrv;
		String horizontalLineDataOneSiteString = null;
		String horizontalLineDataFHLString = null;
		String horizontalLineDataPartnerLibrariesString = null;
		String horizontalLineDataInternetArchiveString = null;
		String horizontalLineDataInternetArchiveRtString = null;
		String horizontalLineDataGmrvString = null;
		
		if("All Sites".equals(site)) {
			 
			horizontalLineDataFHL = bookService.getDashboarDataYTDScanPublish(fomStartDate, fomEndDate, "allFhc", daysDiff);
			horizontalLineDataPartnerLibraries = bookService.getDashboarDataYTDScanPublish(fomStartDate, fomEndDate, "allPartnerLibs", daysDiff);
			horizontalLineDataInternetArchive = bookService.getDashboarDataYTDScanPublish(fomStartDate, fomEndDate, "allInternetArchives", daysDiff);
			horizontalLineDataInternetArchiveRt = bookService.getDashboarDataYTDScanPublish(fomStartDate, fomEndDate, "rtInternetArchives", daysDiff);
			horizontalLineDataGmrv = bookService.getDashboarDataYTDScanPublish(fomStartDate, fomEndDate, "gmrv", daysDiff);

			horizontalLineDataFHLString = arraysToString(horizontalLineDataFHL);
			horizontalLineDataPartnerLibrariesString = arraysToString(horizontalLineDataPartnerLibraries);
			horizontalLineDataInternetArchiveString = arraysToString(horizontalLineDataInternetArchive);
			horizontalLineDataInternetArchiveRtString = arraysToString(horizontalLineDataInternetArchiveRt);
			horizontalLineDataGmrvString = arraysToString(horizontalLineDataGmrv);
			String totalScanPublish[] = bookService.getDashboarDataTotalYTDScanPublish(startDate, endDate, fomStartDate, fomStartDateCurrentMonth, fomEndDate, "All Sites");
			
			model.addAttribute("horizontalLineDataFHL", horizontalLineDataFHLString);//"[['Month', 'Scan', 'Publish', 'Goal'], ['J',  165, 450, 214.6],  ['F',  135, 288, 214.6],      ['M',  157, 397, 214.6],     ['A',  139, 215, 214.6],       ['M',  136, 366, 214.6] ]");
			model.addAttribute("horizontalLineDataPartnerLibraries", horizontalLineDataPartnerLibrariesString);//"[['Month', 'Scan', 'Publish', 'Goal'], ['J',  165, 450, 214.6],  ['F',  135, 288, 214.6],      ['M',  157, 397, 214.6],     ['A',  139, 215, 214.6],       ['M',  136, 366, 214.6] ]");
			model.addAttribute("horizontalLineDataInternetArchive", horizontalLineDataInternetArchiveString);//"[['Month', 'Scan', 'Publish', 'Goal'], ['J',  165, 450, 214.6],  ['F',  135, 288, 214.6],      ['M',  157, 397, 214.6],     ['A',  139, 215, 214.6],       ['M',  136, 366, 214.6] ]");
			model.addAttribute("horizontalLineDataInternetArchiveRt", horizontalLineDataInternetArchiveRtString);
			model.addAttribute("horizontalLineDataGmrv", horizontalLineDataGmrvString);
			model.addAttribute("horizontalLineDataOneSite", "[]");//send flag //dummy for js
			
			//text counts in upper left
			model.addAttribute("aboveHorizontalLineTotalDateRangeScan", totalScanPublish[0]);
			//model.addAttribute("aboveHorizontalLineTotalIaRtDateRangeScan", totalScanPublish[1]);
			model.addAttribute("aboveHorizontalLineTotalDateRangePublish", totalScanPublish[1]);
			model.addAttribute("aboveHorizontalLineTotalYTDScan", totalScanPublish[2]);
			//model.addAttribute("aboveHorizontalLineTotalIaRtYTDScan", totalScanPublish[4]);
			model.addAttribute("aboveHorizontalLineTotalYTDPublish", totalScanPublish[3]);
		}else {
			horizontalLineDataOneSite = bookService.getDashboarDataYTDScanPublish(fomStartDate, fomEndDate, site, daysDiff);
			horizontalLineDataOneSiteString = arraysToString(horizontalLineDataOneSite);
			String totalScanPublish[] = bookService.getDashboarDataTotalYTDScanPublish(startDate, endDate, fomStartDate, fomStartDateCurrentMonth, fomEndDate, site);
			
			model.addAttribute("horizontalLineDataOneSite", horizontalLineDataOneSiteString);//"[['Month', 'Scan', 'Publish', 'Goal'], ['J',  165, 450, 214.6],  ['F',  135, 288, 214.6],      ['M',  157, 397, 214.6],     ['A',  139, 215, 214.6],       ['M',  136, 366, 214.6] ]");
			model.addAttribute("horizontalLineDataFHL", "[]"); //send flag //dummy for js
			model.addAttribute("horizontalLineDataPartnerLibraries", "[]"); //send flag //dummy for js
			model.addAttribute("horizontalLineDataInternetArchive", "[]");//send flag //dummy for js
			model.addAttribute("horizontalLineDataInternetArchiveRt",  "[]");//send flag //dummy for js
			model.addAttribute("horizontalLineDataGmrv",  "[]");//send flag //dummy for js
			
			//text counts in upper left
			model.addAttribute("aboveHorizontalLineTotalDateRangeScan", totalScanPublish[0]);
			//model.addAttribute("aboveHorizontalLineTotalIaRtDateRangeScan", totalScanPublish[1]);
			model.addAttribute("aboveHorizontalLineTotalDateRangePublish", totalScanPublish[1]);
			model.addAttribute("aboveHorizontalLineTotalYTDScan", totalScanPublish[2]);
			//model.addAttribute("aboveHorizontalLineTotalIaRtYTDScan", totalScanPublish[4]);
			model.addAttribute("aboveHorizontalLineTotalYTDPublish", totalScanPublish[3]);
		}
		
		return "dashboard/dashboardPage2";
	}

	public String arraysToString(String[][] a) {
		Map<String, String> months = new HashMap<String,String>();
		months.put("1", "'Jan'");
		months.put("2", "'Feb'");
		months.put("3", "'Mar'");
		months.put("4", "'Apr'");
		months.put("5", "'May'");
		months.put("6", "'Jun'");
		months.put("7", "'Jul'");
		months.put("8", "'Aug'");
		months.put("9", "'Sep'");
		months.put("10", "'Oct'");
		months.put("11", "'Nov'");
		months.put("12", "'Dec'");
		//first array is just dummy labels that are not currently displayed, but required to be in array
		String retVal = "[";//['xMonth', 'xScan', 'xPublish', 'xGoal'], ";
		int aLen = a.length;
		for(int y = 0; y<aLen; y++) {
			if(a[y][0]==null) {
				break;//array is padded with nulls at end
			}
			retVal += "[";
			for(int x=0; x< 4; x++) {
				if(x==0 && y!=0) {//y=0 are labels
					retVal += months.get(a[y][x]) + ",";
				}else {
					/*if(a[y][x].equals("0"))
					{
						retVal += "1,";//in order to show a line, use 1 as count
					}else {*/
						retVal += a[y][x] + ",";
					 
				}
			}
			retVal = retVal.substring(0, retVal.length()-1);//rem comma
			retVal += "],";
		}
		retVal = retVal.substring(0, retVal.length()-1);//rem comma
		retVal += "]";
		
		return retVal;
		
	}
	
	@RequestMapping(value="dashboard/dashboardPage2", method=RequestMethod.POST)
	public String post(HttpServletRequest req, Model model, Locale locale) {
		return get(req, model, locale);
	}
	
}