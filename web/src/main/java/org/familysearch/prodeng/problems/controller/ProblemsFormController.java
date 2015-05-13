/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.problems.controller;

import java.security.Principal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.familysearch.prodeng.model.Problem;
import org.familysearch.prodeng.model.SqlTimestampPropertyEditor;
import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("problemsFormController")
public class ProblemsFormController {

	private BookService bookService;
	
	@Autowired
	public ProblemsFormController(BookService bookService) {
		this.bookService = bookService;
	}
	 
	 
	 

	///////////tracking form displayed when checking TN of metadata already in trackingform BOOK db////////
	//show populated form - for read (tn can be null and will display empty fields)
	@RequestMapping(value="problems/problemsForm", params="read", method=RequestMethod.GET)
	public String displayProblemsRead(String tn, String pn, Model model) {
		model.addAttribute("mode", "read"); 
		model.addAttribute("problem", bookService.getProblem(tn, pn)); //autoselect
		model.addAttribute("allProblems", bookService.getBookProblemPns(tn)); 
		model.addAttribute("allStatuses", bookService.getAllStatuses()); 
		model.addAttribute("allSites", bookService.getAllSites()); 
		return "problems/problemsForm";
	}

	///////////metadata trackingform start///////

	//show populated form - for update - used with anchor link "update" - 
	//same as below but GET for use in list of book links
	@RequestMapping(value="problems/problemsForm",  params="update", method=RequestMethod.GET)
	public String displayBookUpdate(String tn, String pn, String doCreate, HttpServletRequest request, Model model) {
		return displayProblemUpdatePost(tn, pn, doCreate, request, model);
	}
 	
	//show populated form - for update - used with button "update"
	@RequestMapping(value="problems/problemsForm", params="update", method=RequestMethod.POST)
	public String displayProblemUpdatePost(String tn, String pn, String doCreate,  HttpServletRequest request, Model model) {
		model.addAttribute("mode", "update"); 
		model.addAttribute("doCreate", doCreate); //if just update then null
		if(doCreate != null) {
			//HttpSession s = request.getSession();
			Principal principal = request.getUserPrincipal();
			 
			String userId = "";
			if(principal != null) {
				userId = principal.getName();//userid
		  	}
			Problem newProblem = bookService.getNewProblem(tn, userId);
			model.addAttribute("problem", newProblem);  //create new empty problem

		}
		else {
			model.addAttribute("problem", bookService.getProblem(tn, pn));  
		}
		model.addAttribute("allStatuses", bookService.getAllStatuses()); 
		model.addAttribute("allProblemReasons", bookService.getAllProblemReasons()); 
		model.addAttribute("allProblemReasons2", bookService.getAllProblemReasons2()); 
		model.addAttribute("allSites", bookService.getAllSites()); 
		return "problems/problemsForm";
	} 
	 

	//do update
	@RequestMapping(value="problems/problemsForm", params="save", method=RequestMethod.POST)
	public String doBookSave(Problem problem, String tn, String pn, String doCreate, String returnTo, Model model) {
		//save book object which was linked in displayBookUpdatePost and form modelAttribute attr
		
		if(doCreate == null) {
			bookService.updateProblem(problem);  
		}else{
			bookService.createProblem(problem);  	
		}
		
		//tn in url cannot contain &
		if(tn.contains("&")) {
			tn = tn.replace("&", "%26");
		}
		
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		return "redirect:problemsForm?read&tn=" + tn + "&pn=" + pn; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	//do cancel
	@RequestMapping(value="problems/problemsForm", params="cancel", method=RequestMethod.POST)
	public String doBookCancel(  String tn, String pn,  String doCreate, String returnTo, Model model) {
		//do nothing
		if(doCreate != null) {
			int oldPn = Integer.parseInt(pn);
			oldPn--;
			pn = String.valueOf(oldPn);
		}
		
		//tn in url cannot contain &
		if(tn.contains("&")) {
			tn = tn.replace("&", "%26");
		}
		
		if(returnTo != null && returnTo != "") 
			return "redirect:" + returnTo;
		return "redirect:problemsForm?read&tn=" + tn + "&pn=" + pn;//redirect - guard against refresh-multi-updates and also update displayed url
	} 
	
	///////////metadata trackingform end///////
	 
 
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	   // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:m a");
	    binder.registerCustomEditor(Timestamp.class, new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a"));
	}
	
 
	
}