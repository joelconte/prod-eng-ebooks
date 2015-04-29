/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.dashboard.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

@Controller("dashboardMiscController")
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

	@RequestMapping(value="dashboard/dashboardPage", method=RequestMethod.GET)
	public String get(HttpServletRequest req, Model model, Locale locale) {
		
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		if(startDate != null && endDate != null) {
			//trim time off
			int timeStart = startDate.indexOf(" ");//end of date
			if(timeStart != -1)
				startDate = startDate.substring(0, timeStart);
			
			timeStart = endDate.indexOf(" ");//end of date
			if(timeStart != -1)
				endDate = endDate.substring(0, timeStart); 
		}
		if(startDate==null || endDate==null) {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			   //get current date time with Date()
			java.util.Date now = new java.util.Date();
			endDate = dateFormat.format(now);

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,  -30);//30 days diff
			startDate = dateFormat.format(cal.getTime());
		}
		model.addAttribute("pageTitle", messageSource.getMessage("dashboard.dashboardPageTitle", null, locale));
		List<String> sites =  bookService.getAllSites();
		model.addAttribute("allLocations",sites);
		String site = req.getParameter("site");
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("site", site);
		String productionData[][];
		
	
		/////production
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
		
		model.addAttribute("g0", productionData[6][0]);
		model.addAttribute("g1", productionData[6][1]);
		model.addAttribute("g2", productionData[6][2]);
		model.addAttribute("g3", productionData[6][3]); 
		
		model.addAttribute("h0", productionData[7][0]);
		model.addAttribute("h1", productionData[7][1]);
		model.addAttribute("h2", productionData[7][2]);
		model.addAttribute("h3", productionData[7][3]); 
		
		model.addAttribute("i0", productionData[8][0]);
		model.addAttribute("i1", productionData[8][1]);
		model.addAttribute("i2", productionData[8][2]);
		model.addAttribute("i3", productionData[8][3]); 
		
		model.addAttribute("j0", productionData[9][0]);
		model.addAttribute("j1", productionData[9][1]);
		model.addAttribute("j2", productionData[9][2]);
		model.addAttribute("j3", productionData[9][3]); 
		
		model.addAttribute("k0", productionData[10][0]);
		model.addAttribute("k1", productionData[10][1]);
		model.addAttribute("k2", productionData[10][2]);
		model.addAttribute("k3", productionData[10][3]); 
		
		model.addAttribute("l0", productionData[11][0]);
		model.addAttribute("l1", productionData[11][1]);
		model.addAttribute("l2", productionData[11][2]);
		model.addAttribute("l3", productionData[11][3]); 
		 
		 
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
		

		model.addAttribute("quald0", qualityData[3][0]);
		model.addAttribute("quald1", qualityData[3][1]);
		model.addAttribute("quald2", qualityData[3][2]);
		model.addAttribute("quald3", qualityData[3][3]);

		model.addAttribute("quale0", qualityData[4][0]);
		model.addAttribute("quale1", qualityData[4][1]);
		model.addAttribute("quale2", qualityData[4][2]);
		model.addAttribute("quale3", qualityData[4][3]);
		

		model.addAttribute("qualf0", qualityData[5][0]);
		model.addAttribute("qualf1", qualityData[5][1]);
		model.addAttribute("qualf2", qualityData[5][2]);
		model.addAttribute("qualf3", qualityData[5][3]);
	

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
 
		
		/////Aged metrics
		/* Removing Aged Metrice charts.  True data, but not very useful
		String[][] agedData = bookService.getDashboardAgedAverages(startDate, endDate, site);
		 
		model.addAttribute("ageda0", agedData[0][0]);
		model.addAttribute("ageda1", agedData[0][1]);
		model.addAttribute("ageda2", agedData[0][2]);
		model.addAttribute("ageda3", agedData[0][3]);

		model.addAttribute("agedb0", agedData[1][0]);
		model.addAttribute("agedb1", agedData[1][1]);
		model.addAttribute("agedb2", agedData[1][2]);
		model.addAttribute("agedb3", agedData[1][3]);
		
		model.addAttribute("agedc0", agedData[2][0]);
		model.addAttribute("agedc1", agedData[2][1]);
		model.addAttribute("agedc2", agedData[2][2]);
		model.addAttribute("agedc3", agedData[2][3]);
		

		model.addAttribute("agedd0", agedData[3][0]);
		model.addAttribute("agedd1", agedData[3][1]);
		model.addAttribute("agedd2", agedData[3][2]);
		model.addAttribute("agedd3", agedData[3][3]);
		

		model.addAttribute("agede0", agedData[4][0]);
		model.addAttribute("agede1", agedData[4][1]);
		model.addAttribute("agede2", agedData[4][2]);
		model.addAttribute("agede3", agedData[4][3]);
*/
		

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
		String[][] goalData = bookService.getDashboardGoalData(startDate, endDate, site);
		//model.addAttribute("goalsLabels", "[ \"3/4\", \"February\", \"March\", \"April\", \"May\", \"une\", \"July\" ]");
		//model.addAttribute("goals","[ 65, 59, 90, 81, 56, 55, 40 ]");
		//model.addAttribute("actuals","[ 23, 59, 10, 81, 56, 5, 40 ]");
		model.addAttribute("goalsLabels", goalData[0][0]);
		model.addAttribute("goals", goalData[0][1]);
		model.addAttribute("actuals", goalData[0][2]);
		
		return "dashboard/dashboardPage";
	}

	@RequestMapping(value="dashboard/dashboardPage", method=RequestMethod.POST)
	public String post(HttpServletRequest req, Model model, Locale locale) {
		return get(req, model, locale);
	}
	
}