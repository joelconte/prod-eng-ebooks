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
		
		String year = req.getParameter("year");
		//String month = req.getParameter("month");
		String startDate = "";
		String endDate = "";
 		String endDateYMD = "";
 		String startDateCurrentMonth = "";
		int daysDiff = 0;//
		int endMonthInt = 0;
		String endingMonthLabel = "";
		if(year != null) {		
			int yearInt = Integer.parseInt(year);
			Calendar cal = Calendar.getInstance();
			int monthInt = cal.get(Calendar.MONTH) + 1;
			startDate = "01/01/" + year;
			GregorianCalendar gcStartOfYear = new GregorianCalendar(yearInt, 0, 1);//jan 1 of year selected
			
			GregorianCalendar gc = new GregorianCalendar(yearInt, monthInt-1, 1);
			gc.add(Calendar.MONTH, 1);//add 1 month
			gc.add(Calendar.DAY_OF_MONTH, -1);//subtract 1 day since endDate is inclusive
			endingMonthLabel = gc.getDisplayName(Calendar.MONTH, Calendar.LONG,  Locale.getDefault());
 
			endMonthInt = gc.get(Calendar.MONTH)+1;
			int endDayInt = gc.get(Calendar.DAY_OF_MONTH);
			int endYearInt = gc.get(Calendar.YEAR);
			String dd = String.valueOf(endDayInt);
			String mm = String.valueOf(endMonthInt);
			dd = dd.length() == 1 ? "0" + dd : dd;
			mm = mm.length() == 1 ? "0" + mm : mm;
			endDate = mm + "/" + dd + "/" + String.valueOf(endYearInt);
			startDateCurrentMonth = mm + "/" + "01" + "/" + String.valueOf(endYearInt);//first of current month starting 
			endDateYMD =  String.valueOf(endYearInt) + "/" + mm + "/" + dd;
			
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
			endingMonthLabel = gc.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			endMonthInt = gc.get(Calendar.MONTH)+1;
			int endDayInt = gc.get(Calendar.DAY_OF_MONTH);
			int endYearInt = gc.get(Calendar.YEAR);
			String dd = String.valueOf(endDayInt);
			String mm = String.valueOf(endMonthInt);
			dd = dd.length() == 1 ? "0" + dd : dd;
			mm = mm.length() == 1 ? "0" + mm : mm;
			endDate = mm + "/" + dd + "/" + String.valueOf(endYearInt);
			endDateYMD =  String.valueOf(endYearInt) + "/" + mm + "/" + dd;
			startDateCurrentMonth = mm + "/" + "01" + "/" + String.valueOf(endYearInt);//first of current month starting 
			
			GregorianCalendar gcStart = new GregorianCalendar(yearInt, monthInt-1, 1);
			daysDiff = Math.abs(gc.get(Calendar.DAY_OF_YEAR)-gcStartOfYear.get(Calendar.DAY_OF_YEAR)) + 1;//inclusive  
			
			//set jsp vars for current year/month
			year = String.valueOf(yearInt);
			//month = String.valueOf(monthInt);
		}
		
		model.addAttribute("pageTitle", messageSource.getMessage("dashboard.dashboardPageTitle", null, locale) + " - through " + endingMonthLabel );
		List<String> sites =  bookService.getAllSites();
		model.addAttribute("allLocations",sites);
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
		
	 
	

		/////YTDGoal and YTDscan and YTDpublish Actual Pie Charts
		List<List>  data = bookService.getGoalsAndActuals(year, endMonthInt, endDateYMD, site);// list of rows(site,YTDGoal,scanYTD,scanYTDTodo,publishYTD,publishYTDTodo) 
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
		/* removed
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
		*/
		
		 //calls valid for: Site, "all sites", "allFhc", "allPartnerLibs", "allInternetArchives"
		String[][] horizontalLineDataOneSite;
		String[][] horizontalLineDataFHL;
		String[][] horizontalLineDataPartnerLibraries;
		String[][] horizontalLineDataInternetArchive;
		String horizontalLineDataOneSiteString = null;
		String horizontalLineDataFHLString = null;
		String horizontalLineDataPartnerLibrariesString = null;
		String horizontalLineDataInternetArchiveString = null;
		
		if("All Sites".equals(site)) {
			 
			horizontalLineDataFHL = bookService.getDashboarDataYTDScanPublish(startDate, endDate, "allFhc", daysDiff);
			horizontalLineDataPartnerLibraries = bookService.getDashboarDataYTDScanPublish(startDate, endDate, "allPartnerLibs", daysDiff);
			horizontalLineDataInternetArchive = bookService.getDashboarDataYTDScanPublish(startDate, endDate, "allInternetArchives", daysDiff);

			horizontalLineDataFHLString = arraysToString(horizontalLineDataFHL);
			horizontalLineDataPartnerLibrariesString = arraysToString(horizontalLineDataPartnerLibraries);
			horizontalLineDataInternetArchiveString = arraysToString(horizontalLineDataInternetArchive);
			String totalScanPublish[] = bookService.getDashboarDataTotalYTDScanPublish(startDate, startDateCurrentMonth, endDate, "all");
			
			model.addAttribute("horizontalLineDataFHL", horizontalLineDataFHLString);//"[['Month', 'Scan', 'Publish', 'Goal'], ['J',  165, 450, 214.6],  ['F',  135, 288, 214.6],      ['M',  157, 397, 214.6],     ['A',  139, 215, 214.6],       ['M',  136, 366, 214.6] ]");
			model.addAttribute("horizontalLineDataPartnerLibraries", horizontalLineDataPartnerLibrariesString);//"[['Month', 'Scan', 'Publish', 'Goal'], ['J',  165, 450, 214.6],  ['F',  135, 288, 214.6],      ['M',  157, 397, 214.6],     ['A',  139, 215, 214.6],       ['M',  136, 366, 214.6] ]");
			model.addAttribute("horizontalLineDataInternetArchive", horizontalLineDataInternetArchiveString);//"[['Month', 'Scan', 'Publish', 'Goal'], ['J',  165, 450, 214.6],  ['F',  135, 288, 214.6],      ['M',  157, 397, 214.6],     ['A',  139, 215, 214.6],       ['M',  136, 366, 214.6] ]");
			model.addAttribute("horizontalLineDataOneSite", "[]");//send flag //dummy for js
			model.addAttribute("aboveHorizontalLineTotalMTDScan", totalScanPublish[0]);
			model.addAttribute("aboveHorizontalLineTotalMTDPublish", totalScanPublish[1]);
			model.addAttribute("aboveHorizontalLineTotalYTDScan", totalScanPublish[2]);
			model.addAttribute("aboveHorizontalLineTotalYTDPublish", totalScanPublish[3]);
		}else {
			horizontalLineDataOneSite = bookService.getDashboarDataYTDScanPublish(startDate, endDate, site, daysDiff);
			horizontalLineDataOneSiteString = arraysToString(horizontalLineDataOneSite);
			String totalScanPublish[] = bookService.getDashboarDataTotalYTDScanPublish(startDate, startDateCurrentMonth, endDate, "all");
			
			model.addAttribute("horizontalLineDataOneSite", horizontalLineDataOneSiteString);//"[['Month', 'Scan', 'Publish', 'Goal'], ['J',  165, 450, 214.6],  ['F',  135, 288, 214.6],      ['M',  157, 397, 214.6],     ['A',  139, 215, 214.6],       ['M',  136, 366, 214.6] ]");
			model.addAttribute("horizontalLineDataFHL", "[]"); //send flag //dummy for js
			model.addAttribute("horizontalLineDataPartnerLibraries", "[]"); //send flag //dummy for js
			model.addAttribute("horizontalLineDataInternetArchive", "[]");//send flag //dummy for js
			model.addAttribute("aboveHorizontalLineTotalMTDScan", totalScanPublish[0]);
			model.addAttribute("aboveHorizontalLineTotalMTDPublish", totalScanPublish[1]);
			model.addAttribute("aboveHorizontalLineTotalYTDScan", totalScanPublish[2]);
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