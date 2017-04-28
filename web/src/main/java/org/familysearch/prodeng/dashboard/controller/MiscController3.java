/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.dashboard.controller;

import java.util.ArrayList;
import java.util.Calendar;
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

@Controller("dashboardMiscController3")
public class MiscController3 implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public MiscController3(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value="dashboard/dashboardPage3", method=RequestMethod.GET)
	public String get(HttpServletRequest req, Model model, Locale locale) {
		 
		
		model.addAttribute("pageTitle", messageSource.getMessage("dashboard.dashboard3", null, locale) );
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		
		/* upper left usage table for 5 years */ 
		model.addAttribute("y1", String.valueOf(currentYear - 4));
		model.addAttribute("y2", String.valueOf(currentYear - 3));
		model.addAttribute("y3", String.valueOf(currentYear - 2));
		model.addAttribute("y4", String.valueOf(currentYear - 1));
		model.addAttribute("y5", String.valueOf(currentYear));
		
		List<List> viewingStats = bookService.getViewingStats5Years(currentYear-4);// year, month, num_of_views, num_of_book_views - ordered by year,month
		 
		Map<Integer,Integer> yearlyTotals = new HashMap<Integer, Integer>(); 
		yearlyTotals.put(currentYear-4, 0);
		yearlyTotals.put(currentYear-3, 0);
		yearlyTotals.put(currentYear-2, 0);
		yearlyTotals.put(currentYear-1, 0);
		yearlyTotals.put(currentYear, 0);
		
		for(List r : viewingStats) {
			Integer year = Integer.parseInt((String)r.get(0));
			Integer existingTotal = yearlyTotals.get(year);
			Integer numViews = Integer.parseInt((String)r.get(2));
			yearlyTotals.put(year, existingTotal + numViews);
		}
 
		model.addAttribute("total1", String.valueOf(yearlyTotals.get(currentYear-4)));
		model.addAttribute("total2", String.valueOf(yearlyTotals.get(currentYear-3)));
		model.addAttribute("total3", String.valueOf(yearlyTotals.get(currentYear-2)));
		model.addAttribute("total4", String.valueOf(yearlyTotals.get(currentYear-1)));
		model.addAttribute("total5", String.valueOf(yearlyTotals.get(currentYear)));

		model.addAttribute("monthlyAvg1", String.valueOf(yearlyTotals.get(currentYear-4) / 12));
		model.addAttribute("monthlyAvg2", String.valueOf(yearlyTotals.get(currentYear-3) / 12));
		model.addAttribute("monthlyAvg3", String.valueOf(yearlyTotals.get(currentYear-2) / 12));
		model.addAttribute("monthlyAvg4", String.valueOf(yearlyTotals.get(currentYear-1) / 12));
		model.addAttribute("monthlyAvg5", String.valueOf(yearlyTotals.get(currentYear) / currentMonth));//current part year avg
		
		
		/*lower left chart total downloads for pas 12 months */
		
		List<List> past12Months = bookService.getPast12MonthViews();//return 3 lists 1- month lables, 2- total views,  3- unique book views
		model.addAttribute("past12Months", past12Months.get(0)); 
		model.addAttribute("past12MonthsViews", past12Months.get(1)); 
		model.addAttribute("past12MonthsUnique", past12Months.get(2)); 


		
		/*Right side chart total downloads for pas 5 years */
		//queried above viewingStats = bookService.getViewingStats5Years(currentYear-4);// year, month, num_of_views, num_of_book_views - ordered by year,month
		int[] yearMinus4 = new int[12];//init all to 0
		int[] yearMinus3 = new int[12];
		int[] yearMinus2 = new int[12];
		int[] yearMinus1 = new int[12];
		int[] yearMinus0 = new int[12]; 
		
		boolean todoGetNext = true;
		for(List r : viewingStats) {
			int y = Integer.parseInt((String)r.get(0));
			int m = Integer.parseInt((String)r.get(1));
			int views = Integer.parseInt((String)r.get(2));
			if(y == currentYear-4)
				yearMinus4[m-1] = views;
			else if(y == currentYear-3)
				yearMinus3[m-1] = views;
			else if(y == currentYear-2)
				yearMinus2[m-1] = views;
			else if(y == currentYear-1)
				yearMinus1[m-1] = views;
			else 
				yearMinus0[m-1] = views;
		}
		//convert arrays to arraylist of strings for view
		List yearMinus4Views = new ArrayList<String>(); 
		List yearMinus3Views = new ArrayList<String>(); 
		List yearMinus2Views = new ArrayList<String>(); 
		List yearMinus1Views = new ArrayList<String>(); 
		List yearMinus0Views = new ArrayList<String>(); 
		for(int x = 0; x< 12; x++) {
			yearMinus4Views.add(String.valueOf(yearMinus4[x]));
			yearMinus3Views.add(String.valueOf(yearMinus3[x]));
			yearMinus2Views.add(String.valueOf(yearMinus2[x]));
			yearMinus1Views.add(String.valueOf(yearMinus1[x]));
			yearMinus0Views.add(String.valueOf(yearMinus0[x]));
		}
		
		 
		List years = new ArrayList(5);
		years.add(String.valueOf(currentYear-4));
		years.add(String.valueOf(currentYear-3));
		years.add(String.valueOf(currentYear-2));
		years.add(String.valueOf(currentYear-1));
		years.add(String.valueOf(currentYear));
		
	
		model.addAttribute("years", years); 
		model.addAttribute("yearMinus4Views", yearMinus4Views); 
		model.addAttribute("yearMinus3Views", yearMinus3Views); 
		model.addAttribute("yearMinus2Views", yearMinus2Views); 
		model.addAttribute("yearMinus1Views", yearMinus1Views); 
		model.addAttribute("yearMinus0Views", yearMinus0Views); 
		  
		return "dashboard/dashboardPage3";
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
	
	@RequestMapping(value="dashboard/dashboardPage3", method=RequestMethod.POST)
	public String post(HttpServletRequest req, Model model, Locale locale) {
		return get(req, model, locale);
	}
	
}