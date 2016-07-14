/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.metadata.controller;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("metadataMiscController")
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
	


	////////////NewBooks metadata start/////////////
	
	@RequestMapping(value="metadata/metadataNewBooks", method=RequestMethod.GET)
	public String getMetadataNewBooks(HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("subject", null, locale));
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNumber", null, locale));
		labels.add(messageSource.getMessage("filmNumber", null, locale));
		labels.add(messageSource.getMessage("pages", null, locale));
		labels.add(messageSource.getMessage("summary", null, locale));
		labels.add(messageSource.getMessage("dgsNumber", null, locale));
		labels.add(messageSource.getMessage("language", null, locale));
		labels.add(messageSource.getMessage("owningInstitution", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("dateOriginal", null, locale));
		labels.add(messageSource.getMessage("publisherOriginal", null, locale));
		labels.add(messageSource.getMessage("fileName", null, locale));
		//////
		List<List> md = bookService.getMetadataSendToScanTnsInfo();
		if(!model.containsAttribute("dupeTnsInfo")) {
			//if already dupeTnsInfo, then just pasted in data and had dupe md

			String tnListStr = "";
			for(List r: md) {
				String tn = (String) r.get(3);
				tnListStr += ", '" + tn + "'";
			}
			if(tnListStr != "")
				tnListStr = tnListStr.substring(2);
			model.addAttribute("dupeTnsInfo", bookService.getDuplicateTnsInBook(tnListStr));   
			 
		}
		String mojibakeStr = checkMojibake(md, locale);
		if( mojibakeStr != null) {
			model.addAttribute("mojibakeMessage", mojibakeStr);
		}
		//////

		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.newBooks", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", md);  
		model.addAttribute("allSites", bookService.getAllSites()); 
		model.addAttribute("allScanSites", bookService.getAllScanSites()); 
		model.addAttribute("tnColumnNumber", "3");//column where tn is located for creating url
	 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteExcel", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkTns");
		details.add(messageSource.getMessage("checkDupeTns", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("deleteSelected");
		details.add(messageSource.getMessage("deleteSelectedMetadata", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("deleteAllNewBooks");
		details.add(messageSource.getMessage("deleteAllNewBooks", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("selectedMetadataSendToScan");
		details.add(messageSource.getMessage("selectedMetadataSendToScan", null, locale));
		details.add("checkForDupesSome");//!!javascript function to exec onclick
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("allMetadataSendToScan");
		details.add(messageSource.getMessage("allMetadataSendToScan", null, locale));
		details.add("checkForDupesAll");//!!javascript function to exec onclick
		buttons.add(details);  
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "metadataNewBooks");
		model.addAttribute("overlayAction", "doMetadataNewBooksInsertTns");
		return "metadata/miscButtonAndTableFormWithCheckbox";
	}

	//check md if it has any of the following strings
	public String checkMojibake(List<List> md, Locale locale) {
		String[] moji = {"??", "Ã©", "¦", "¤", "¬", "¶", "¥", "¼", "Â¿", "¿¿", "ª", "¨", "«", "§", "Š" , "³", "¡", "�", "",  "ÃstÃ"}; 
		//fyi ""  contains an invisible char often seen between "Än".  If you copypaste it to notepad and save as ansi, it looks different.  In the trackingform in the title field for example it appears as a rectangle with symbols inside. 
		for(List<String> row : md) {
			for(String field : row) {
				for(String x : moji) {
					if(field != null && field.indexOf(x) != -1) {
						String[] messageArgs =  {row.get(3), field, x};
						return messageSource.getMessage("mojibakeMessage", messageArgs, locale);
					}
				}
			}
		}
		return null;
	}
	
	@RequestMapping(value="metadata/metadataNewBooks", method=RequestMethod.POST)
	public String getMetadataNewBooksPost(String button, HttpServletRequest request, Principal principal,  Model model, Locale locale) {
		if("checkTns".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("title", null, locale));
			labels.add(messageSource.getMessage("author", null, locale));
			labels.add(messageSource.getMessage("pages", null, locale));
			labels.add(messageSource.getMessage("scanningLocation", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.newBooksAlreadyInTrackingFormDatabase", null, locale));
			model.addAttribute("colLabels", labels); 
			model.addAttribute("allTnsInfo", bookService.getMetadataNewBooksAlreadyInTrackingFormDatabaseTnsInfo()); 
			model.addAttribute("tnColumnNumber", "0");//column where tn is located for creating url
			 
			//buttons
			List<List<String>> buttons = new ArrayList<List<String>>();
			List<String> details = new ArrayList<String>();
			details.add("deleteSelected2");
			details.add(messageSource.getMessage("deleteSelectedMetadata", null, locale));
			buttons.add(details); 
			details = new ArrayList<String>();
			details.add("goBack");
			details.add(messageSource.getMessage("goBack", null, locale));
			details.add("goBackToMetadata");//!!javascript function to exec onclick
			buttons.add(details); 
			/*details = new ArrayList<String>();
			details.add("deleteAllMetadataBooks");
			details.add(messageSource.getMessage("deleteAllMetadataBooks", null, locale));
			buttons.add(details); */
			model.addAttribute("buttons", buttons);
			
			//form actions
			model.addAttribute("buttonsAction", "metadataNewBooks");
		 
			return "metadata/miscButtonAndTableFormWithCheckbox";
		}else if(button.equals("deleteAllNewBooks")) {
			 
			bookService.deleteAllNewMetadata();
			
		}else if(button.equals("deleteSelected") || button.equals("deleteSelected2")) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String tnList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						tnList = tnList + ", " + "'" + val[0] + "'";
				}
			}
			if(tnList.length()>1)
				tnList = tnList.substring(1);//remove starting comma
			
			bookService.deleteSelectedMetadata(tnList);
			if(button.equals("deleteSelected2"))
				return  getMetadataNewBooksPost("checkTns", request, principal, model, locale);//fake redirect, since this is a post of when a button "checkTns" is pressed, not a get
		}else if("selectedMetadataSendToScan".equals(button)) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
		
			List<String> tnList = new ArrayList();
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						tnList.add(val[0]);
				}
			} 

			//send email alerts
			Object[] datas = bookService.getReportForSendToScanSelectedMetadata(tnList);
			List<List> bookSendList = (List<List>) datas[0];
			List<List> emailSiteList = (List<List>) datas[1];
			List<List> emailSiteListAllSites = (List<List>) datas[2];
			
		
			Map<String, String> textReports = null;//generateTextReport(bookSendList,   emailSiteList);
			Map<String, Workbook> wbReports = null;//generateExcelReport(bookSendList,  emailSiteList );
			Map<String, byte[]> csvReports = generateCsvReport(bookSendList);
		 
			//sendEmails(textReports, emailSiteList);
			//sendEmailsWithAttachment(wbReports, emailSiteList);
			sendEmailsWithCsvAttachment(csvReports, emailSiteList);
			sendEmailsWithCsvAttachment(csvReports, emailSiteListAllSites);
			
			String dupListStr = bookService.sendToScanSelectedMetadata(tnList, principal.getName());
		 
			return "redirect:metadataNewBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if(button.equals("allMetadataSendToScan")) {
			 
			//send email alerts 
			Object[] datas = bookService.getReportForSendToScanSelectedMetadataAll();
			List<List> bookSendList = (List<List>) datas[0];
			List<List> emailSiteList = (List<List>) datas[1];//list of people who want just report for one site
			List<List> emailSiteListAllSites = (List<List>) datas[2];//list of people who want all
			
			Map<String, String> textReports = null;// generateTextReport(bookSendList,   emailSiteList );
			Map<String, Workbook> wbReports = null;//generateExcelReport(bookSendList,   emailSiteList);
			Map<String, byte[]> csvReports = generateCsvReport(bookSendList);
			 
			//sendEmails(textReports, emailSiteList);
			//sendEmailsWithAttachment(wbReports, emailSiteList);
			sendEmailsWithCsvAttachment(csvReports, emailSiteList);
			sendEmailsWithCsvAttachment(csvReports, emailSiteListAllSites);
			
			String dupListStr = bookService.sendToScanAllMetadata(principal.getName());
			
			return "redirect:metadataNewBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url
		} 
 
		//should not happen
		return "redirect:metadataNewBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	
	public Map<String, Workbook> generateExcelReport(List<List> tnData, List<List> emailSiteList) {

		//get unique list of sites
		Map<String, Workbook> map = new HashMap<String, Workbook>();
        int numCols = 10;
        
         
		for(List l : emailSiteList) {
			String site = (String)l.get(3);
			if(map.get(site) == null) {
				Workbook workbook = null;
		        workbook = new XSSFWorkbook();

		        Sheet worksheet = workbook.createSheet();
		        Row rw = worksheet.createRow(0);

		        String[] columns = {"TN", "Title", "Author", "Requesting_location", "Owning_institution", "Scanning_location", "Num_of_pages", "scan_complete_date", "files_received_by_orem", "URL"};
		        for (int x = 0; x < numCols; x++) {
		            Cell cell = rw.createCell(x);
		            cell.setCellValue(columns[x]);
		        }
				map.put(site, workbook); //just put empty workbook and populate it below
			}
		}
		
		//HACK, copy FOR loop logic above
		//START allsites
		Workbook workbook = null;
		workbook = new XSSFWorkbook();

		Sheet worksheet = workbook.createSheet();
		Row rw = worksheet.createRow(0);

		String[] columns = {"TN", "Title", "Author", "Requesting_location", "Owning_institution", "Scanning_location", "Num_of_pages", "scan_complete_date", "files_received_by_orem", "URL"};
		for (int x = 0; x < numCols; x++) {
			Cell cell = rw.createCell(x);
			cell.setCellValue(columns[x]);
		}
		map.put("ALLSITES", workbook); //just put empty workbook and populate it below
		//END allsites

		
		
        
        Set sites = map.keySet();
        for(String site : (Set<String>) sites) {
        	int newCreateRow = 1;
        	workbook = map.get(site);
            worksheet = workbook.getSheetAt(0);
        	
			for (List<String> r : tnData) {
				//check if requesting or owning matches site
				if(site.equals(r.get(3)) || site.equals(r.get(4))) {
					
				    rw = worksheet.createRow(newCreateRow);
					for (int t = 0; t < numCols; t++) {
						Cell cell = rw.createCell(t);
						cell.setCellValue(r.get(t));
					}
					newCreateRow++;
				}
			}
		}
        return map;
	}
	
	public Map<String, byte[]> generateCsvReport(List<List> tnData) {

		//get unique list of sites
		List<String> siteList = bookService.getAllSites();
		List<String> siteList2 = new ArrayList();

		Map<String, byte[]> map = new HashMap<String, byte[]>();
		
		for(String s: siteList) {
			siteList2.add(s);
		}
		
		map.put("ALLSITES", null); //put all sites in map (eventhough they may not have a report later)
		 
		for(String  s: siteList) {
			for(String s2: siteList2) {
				map.put(s + " - "+ s2, null);//all combinations of 2 sites concatinated
			}
		}
		
        int numCols = 12;
        String csvColumns = "";
		  
        String[] columns = {"TN", "Title", "Author", "Call_Num", "Partner_Lib_Call_Num", "Requesting_location", "Owning_institution", "Scanning_location", "Num_of_pages", "scan_complete_date", "files_received_by_orem", "URL"};
        for (int x = 0; x < numCols; x++) {
            csvColumns += columns[x] + ", ";
        }
        csvColumns += "\r\n";
        
        Set sites = map.keySet();
      
        for(String site : (Set<String>) sites) {
        	boolean doThisSite = false;
        	//byte[] csvBytes = map.get(site);
        	String csvStr = "";
        	for (List<String> r : tnData) {
        		//check if requesting or owning matches site
        		if("ALLSITES".equals(site) || site.equals(r.get(5) + " - " + r.get(6))) {
        			doThisSite = true;

        			for (int t = 0; t < numCols; t++) {
        				String val = r.get(t);
        				if(val == null || val.equals("null")){
        					val = "";
        				}else{
        					val = doDoubleQuotes(val);//""
        					if(val.contains(",") || val.contains("\n") || val.contains("\r"))
        						val = "\"" + val + "\"";
        				} 
        				
        				csvStr += val +",";//add column to row in report string
        			}
        			csvStr += "\r\n";

        		}
        	}
        	csvStr = csvColumns + csvStr;//add column headers
        	try {
        		if(doThisSite == true) {
        			map.put(site, csvStr.getBytes("UTF-8")); //just put empty workbook and populate it below
        		}
        	}catch(Exception e) {
        		System.out.println(e);
        		map.put(site, null);
        	}
        }
        return map;
	}
	public String doDoubleQuotes(String val){
		val  = val.replaceAll("\"", "\"\"");
		return val;
	}
	public Map<String, String> generateTextReport(List<List> tnData, List<List> emailSiteList) {
		 
		//get unique list of sites
		Map<String, String> map = new HashMap<String, String>();
        int numCols = 10;
        
        String msgCols = "TN\tTitle\tAuthor\tRequesting_location\tOwning_institution\tScanning_location\tNum_of_pages\tscan_complete_date\tfiles_received_by_orem\tURL\n";
        map.put("ALLSITES", msgCols); //report of users who want all data
		for(List l : emailSiteList) {
			String site = (String)l.get(3);
			if(map.get(site) == null) {
				map.put(site, msgCols); //just put columns in and populate it below
			}
		}
		
      //  int newCreateRow = 1;
        Set sites = map.keySet();
        for(String site : (Set<String>) sites) {
        	String msg = map.get(site);
         
			for (List<String> row : tnData) {
				//check if requesting or owning matches site
				if("ALLSITES".equals(site) || site.equals(row.get(3)) || site.equals(row.get(4))) {
					
					msg = msg +  row.get(0)  + "\t" +  row.get(1) +  "\t" + row.get(2) + "\t" +  row.get(3) +  "\t" + row.get(4) +  "\t" + row.get(5) +  "\t" + row.get(6) +  "\t" + row.get(7) +  "\t" + row.get(8) +  "\t" + row.get(9) + "\t\n";
					//newCreateRow++;
				}
			}
			map.put(site, msg); 
		}
        return map;
	}

	public void sendEmails(Map<String, String> dataList, List<List> emailList) {
		//"books@ldschurch.org", "pauldev@ldschurch.org", "FamilySearch BookScan Metadata", "Hello,\nNew books have been sent to your location to scan.  \nPlease see attached report.\n\n" + msg, principal.getName());
	    //2=email 3=site
		
		for(List<String> row : emailList) {
			String toEmailAddr = row.get(2);
			if(toEmailAddr == null || toEmailAddr.equals(""))
				continue;
			String toSite = row.get(3);
			String fromEmailAddr = "bookscanadmin@ldschurch.org";
			String subject =  "FamilySearch BookScan Metadata - " + toSite;
			String body = "Hello,\n\n\nNew books have been sent to your location (Requesting_location or Owning_institution) to scan.  \nPlease see attached report.\n\n";
			body = body + dataList.get(toSite);//append text report
System.out.println("EMAIL: 			" + toEmailAddr + "  " + toSite);
			MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
			try {
				// the "from" address may be set in code, or set in the
				// config file under "mail.from" ; here, the latter style is used
				message.setFrom(new InternetAddress(fromEmailAddr));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddr));
				message.setSubject(subject);
				message.setText(body);
								
				Transport.send(message);
			} catch (MessagingException ex) {
				/*if("pauldev".equals(username)) {
				throw ex;
			}*/
				System.err.println("Cannot send email. " + ex);
			} 
		}
	}
	


	public void sendEmailsWithAttachment(Map<String, Workbook> wb, List<List> emailList) {
		//"books@ldschurch.org", "pauldev@ldschurch.org", "FamilySearch BookScan Metadata", "Hello,\nNew books have been sent to your location to scan.  \nPlease see attached report.\n\n" + msg, principal.getName());
	    //2=email 3=site
		
		for(List<String> row : emailList) {
			String toEmailAddr = row.get(2);
			if(toEmailAddr == null || toEmailAddr.equals(""))
				continue;
			String toSite = row.get(3);
			String fromEmailAddr = "bookscanadmin@ldschurch.org";
			String subject =  "FamilySearch BookScan Metadata - " + toSite;
			String body = "Hello,\n\n\nNew books have been sent to your location (Requesting_location or Owning_institution) to scan.  \nPlease see attached report.\n\n";
			
			// Here, no Authenticator argument is used (it is null).
			// Authenticators are used to prompt the user for user
			// name and password.
			//Session session = Session.getDefaultInstance(fMailServerConfig, null);
			MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
			try {
				// the "from" address may be set in code, or set in the
				// config file under "mail.from" ; here, the latter style is used
				message.setFrom(new InternetAddress(fromEmailAddr));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddr));
				message.setSubject(subject);
				//set below message.setText(body);
				Workbook w = wb.get(toSite);//attach w to email
	System.out.println("EMAILEXCEL: 			" + toEmailAddr + "  " + toSite);			
				/////////
				try {
				    Multipart mp = new MimeMultipart();
			        MimeBodyPart htmlPart = new MimeBodyPart();        
			        htmlPart.setContent(body, "text/html");
			        mp.addBodyPart(htmlPart);
			
			        MimeBodyPart attachment = new MimeBodyPart();
			        String filename = "BookScan MD-" + toSite + ".xlsx";
			        filename = filename.replace(" ",  "_");
			        attachment.setFileName(filename);
			        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    w.write(baos);
                    byte[] poiBytes = baos.toByteArray();  
			        attachment.setContent(poiBytes, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//"application/vnd.ms-excel");              
			        mp.addBodyPart(attachment);
			
			        message.setContent(mp);
			       ////////////////
				 
				
					/*test write to file
					 OutputStream os = new FileOutputStream(new File("c:/temp/excelfile.xlsx"));
	                 w.write(os);
	                 os.close();
	                 
	                 */
				}catch(Exception e) {
					System.out.println(e);
				}
				
				Transport.send(message);
			} catch (MessagingException ex) {
				/*if("pauldev".equals(username)) {
				throw ex;
			}*/
				System.err.println("Cannot send email. " + ex);
			} 
		}
	}

	public void sendEmailsWithCsvAttachment(Map<String, byte[]>  csvMap, List<List> emailList ) {
 
		Set<String> allSites = csvMap.keySet();//some may be null reports
		 
		for(String site : allSites) {
		 
			if(csvMap.get(site) == null)//no report for this site
				continue;

			for(List<String> user: emailList) {
				String toSite = user.get(3);
				if(toSite != null && (site.indexOf(toSite)!=-1 || toSite.equals("ALLSITES"))) {
					String toEmailAddr = user.get(2);
					if(toEmailAddr == null || toEmailAddr.equals(""))
						continue;

					String fromEmailAddr = "bookscanadmin@ldschurch.org";
					String subject = "FamilySearch BookScan Metadata - " + site;
					String body = "Hello,\n\n\nNew books have been sent to your location (Requesting_location or Owning_institution) to scan.  \nPlease see attached report.\n\n";

					// Here, no Authenticator argument is used (it is null).
					// Authenticators are used to prompt the user for user
					// name and password.
					//Session session = Session.getDefaultInstance(fMailServerConfig, null);
					MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
					try {
						// the "from" address may be set in code, or set in the
						// config file under "mail.from" ; here, the latter style is used
						message.setFrom(new InternetAddress(fromEmailAddr));
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddr));
						message.setSubject(subject);
						//set below message.setText(body);
						byte[] csvBytes = csvMap.get(site);//attach w to email
						System.out.println("EMAILCSV: 			" + toEmailAddr + "  " + site);			
						/////////
						try {
							Multipart mp = new MimeMultipart();
							MimeBodyPart htmlPart = new MimeBodyPart();        
							htmlPart.setContent(body, "text/html");
							mp.addBodyPart(htmlPart);

							MimeBodyPart attachment = new MimeBodyPart();
							String filename = "BookScan MD-" + site + ".csv";
							filename = filename.replace(" ",  "_");
							attachment.setFileName(filename);

							byte[] poiBytes = csvBytes; 
							attachment.setContent(poiBytes, "text/csv");//"application/vnd.ms-excel");              
							mp.addBodyPart(attachment);

							message.setContent(mp);
							////////////////


							/*test write to file
					 OutputStream os = new FileOutputStream(new File("c:/temp/excelfile.xlsx"));
	                 w.write(os);
	                 os.close();

							 */
						}catch(Exception e) {
							System.out.println(e);
						}

						Transport.send(message);
					} catch (MessagingException ex) {

						System.err.println("Cannot send email. " + ex);
					} 
				}
			}
		}
	}
	public void oldsendEmailsWithCsvAttachment(Map<String, byte[]>  csvMap, List<List> emailList ) {
		 
		for(List<String> row : emailList) {
			String toEmailAddr = row.get(2);
			if(toEmailAddr == null || toEmailAddr.equals(""))
				continue;
			String toSite = row.get(3);
			String fromEmailAddr = "bookscanadmin@ldschurch.org";
			String subject = "FamilySearch BookScan Metadata - " + toSite;
			String body = "Hello,\n\n\nNew books have been sent to your location (Requesting_location or Owning_institution) to scan.  \nPlease see attached report.\n\n";
			
			// Here, no Authenticator argument is used (it is null).
			// Authenticators are used to prompt the user for user
			// name and password.
			//Session session = Session.getDefaultInstance(fMailServerConfig, null);
			MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
			try {
				// the "from" address may be set in code, or set in the
				// config file under "mail.from" ; here, the latter style is used
				message.setFrom(new InternetAddress(fromEmailAddr));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddr));
				message.setSubject(subject);
				//set below message.setText(body);
				byte[] csvBytes = csvMap.get(toSite);//attach w to email
	System.out.println("EMAILCSV: 			" + toEmailAddr + "  " + toSite);			
				/////////
				try {
				    Multipart mp = new MimeMultipart();
			        MimeBodyPart htmlPart = new MimeBodyPart();        
			        htmlPart.setContent(body, "text/html");
			        mp.addBodyPart(htmlPart);
			
			        MimeBodyPart attachment = new MimeBodyPart();
			        String filename = "BookScan MD-" + toSite + ".csv";
			        filename = filename.replace(" ",  "_");
			        attachment.setFileName(filename);
			         
                    byte[] poiBytes = csvBytes; 
			        attachment.setContent(poiBytes, "text/csv");//"application/vnd.ms-excel");              
			        mp.addBodyPart(attachment);
			
			        message.setContent(mp);
			       ////////////////
				 
				
					/*test write to file
					 OutputStream os = new FileOutputStream(new File("c:/temp/excelfile.xlsx"));
	                 w.write(os);
	                 os.close();
	                 
	                 */
				}catch(Exception e) {
					System.out.println(e);
				}
				
				Transport.send(message);
			} catch (MessagingException ex) {
				/*if("pauldev".equals(username)) {
				throw ex;
			}*/
				System.err.println("Cannot send email. " + ex);
			} 
		}
	}
	public void xsendEmail(String aFromEmailAddr, String aToEmailAddr, String aSubject, String aBody, String username) {
		
		// Here, no Authenticator argument is used (it is null).
		// Authenticators are used to prompt the user for user
		// name and password.
		//Session session = Session.getDefaultInstance(fMailServerConfig, null);
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		try {
			// the "from" address may be set in code, or set in the
			// config file under "mail.from" ; here, the latter style is used
			message.setFrom(new InternetAddress(aFromEmailAddr));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
			message.setSubject(aSubject);
			message.setText(aBody);
			Transport.send(message);
		} catch (MessagingException ex) {
			/*if("pauldev".equals(username)) {
				throw ex;
			}*/
			System.err.println("Cannot send email. " + ex);
		} 
		
		/*broken old gmail code
		final String username = "pauldevibm@gmail.com";
		final String password = "googlepass";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");// "587");
 ///

props.put("mail.smtp.user", "pauldevibm@gmail.com");
 
props.put("mail.smtp.debug", "true");
 
props.put("mail.smtp.socketFactory.port", "465");
props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
props.put("mail.smtp.socketFactory.fallback", "false");

///


		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("pauldevibm@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("pauldev@ldschurch.org"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	*/
	}
	
	
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="metadata/doMetadataNewBooksInsertTns", method=RequestMethod.POST)
	public String doInsertTnsMetadataNewBooksPost(HttpServletRequest req, String doUpdates, String button, String site, String owningInstitution, String tnData, Principal principal,  Model model, Locale locale) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(tnData, 16);//!!!have to update when add new row to parsable metadata (this was for excel bug with ending missing /t sometimgs
			String tnList = "";
			for(List<String> r : rows) {
				tnList += ", '" + r.get(3) + "'";
			}
			tnList = tnList.substring(2);
			
			String validateFlag = validateData(model, locale, rows, owningInstitution, site);
			if(validateFlag != null)
				return validateFlag;
			
			String dupTnMDList = bookService.getDuplicateTnsInMetadata(tnList); //get list of tns already in bookmetadata col=titleno 3rd(0based) elem in rnData rows
			//String dupTnList = bookService.getDuplicateTnsInBook(tnList);//dec 2013 check for tns in book table only.  if tn in metadata table, just update
			List<String> dupTnListList = bookService.getDuplicateTnsInBookList(tnList);
			String dupTnList = bookService.generateQuotedListString(dupTnListList);
			
			if(dupTnList != "" && doUpdates == null) {
				//redisplay page with dupTnList msg
				
				//model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.alreadyExistMetadata", null, locale) + "\n" + dupTnList);
				//return "errors/generalError";
				model.addAttribute("dupeTnsInfo", dupTnList); //dec2013 this is now book tn dupes
				model.addAttribute("site", site); //requesting_location 
				model.addAttribute("owningInstitution", owningInstitution); //owning_institution 
				model.addAttribute("pastedData", tnData); 
				return getMetadataNewBooks(req, model, locale);//forward request but first pass in dupe list to show user
			}
			//delete old metadata and then re-insert
			bookService.deleteSelectedMetadata(dupTnMDList);//delete if any dup in md table
			String userName = bookService.getUser(principal.getName()).getName();
			if(userName == null)
				userName = "";//hack since for some reason was empty feb2014
			addMiscColumns(rows, userName, site, owningInstitution); //site is requesting_Location
	
			filterOutDupes(rows, dupTnListList);
			bookService.insertBatch("BOOKMETADATA", new String[]{"title", "author", "subject", "titleno", "callno", "partner_lib_callno", "filmno", "pages", "summary", "dgsno", "language", "owning_institution", "requesting_location", "scanning_location", "record_number", "date_original", "publisher_original", "filename", "current_timestamp_date_added", "metadata_adder"}, new int[] {Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR,Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR}, rows); 
		}
		return "redirect:metadataNewBooks"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 
	public String validateData(Model model, Locale locale, List<List<String>> rows, String owningInstitution, String requestingLocation) {
		
		if(owningInstitution == null || "".equals(owningInstitution)) {
			 
			model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.error.badOwningInstitution", null, locale));
			return "errors/generalError";
		}
		if(requestingLocation == null || "".equals(requestingLocation)) {
		 
			model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.error.badRequestingLocation", null, locale));
			return "errors/generalError";
		}
		//subjecte is required
		for(List<String> r: rows) {
			 
			if(null == r.get(2)) {
				model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.error.missingSubject", null, locale));
				return "errors/generalError";
			}		
		}
		return null;
	}

	public void filterOutDupes(List<List<String>> rows, List<String> dupTnList) {
		for(String tn: dupTnList) {
			for(List<String> r: rows) {
				if(tn.equals(r.get(3))) {
					rows.remove(r);
					break;
				}
			}
		}
	}
	
	public void addMiscColumns(List<List<String>> rows, String userId, String requestingLocation, String owningInstitution) {
		for(List<String> r : rows) {
			//current_timstamp values are generated in the insertBatch(), so no value should be added here 
			r.add(11, owningInstitution);
			r.add(12, requestingLocation);
			r.add("current_timestamp");//dummy to flag current time insertBatch
			r.add(userId);//userid in column
			
		}
	}

	////////////NewBooks metadata end/////////////

	////////////UPDATE Books metadata start/////////////
	@RequestMapping(value="metadata/metadataUpdateBooks", method=RequestMethod.GET)
	public String getMetadataUpdateBooks(HttpServletRequest req, Model model,  Locale locale) {
		model.addAttribute("returnTo", req.getServletPath());
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("subject", null, locale));
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNumber", null, locale));
		labels.add(messageSource.getMessage("filmNumber", null, locale));
		labels.add(messageSource.getMessage("pages", null, locale));
		labels.add(messageSource.getMessage("summary", null, locale));
		labels.add(messageSource.getMessage("dgsNumber", null, locale));
		labels.add(messageSource.getMessage("language", null, locale));
		labels.add(messageSource.getMessage("owningInstitution", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("dateOriginal", null, locale));
		labels.add(messageSource.getMessage("publisherOriginal", null, locale));
		labels.add(messageSource.getMessage("fileName", null, locale));
	 
		//////

		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.updateBooks", null, locale));
		model.addAttribute("colLabels", labels);
		List<List> md = bookService.getMetadataUpdateTnsInfo();
		model.addAttribute("allTnsInfo", md);  
		model.addAttribute("allSites", bookService.getAllSites()); 
		model.addAttribute("tnColumnNumber", "3");//column where tn is located for creating url
	 
		 
		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteExcel", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("deleteSelected");
		details.add(messageSource.getMessage("deleteSelectedMetadata", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("deleteAllUpdateBooks");
		details.add(messageSource.getMessage("deleteAllMetadataBooks", null, locale));
		buttons.add(details); 
		details = new ArrayList<String>();
		details.add("selectedMetadataDoUpdate");
		details.add(messageSource.getMessage("selectedMetadataDoUpdate", null, locale));
		details.add("checkForDupesSome");//!!javascript function to exec onclick
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("allMetadataDoUpdate");
		details.add(messageSource.getMessage("allMetadataDoUpdate", null, locale));
		details.add("checkForDupesAll");//!!javascript function to exec onclick
		buttons.add(details);  
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "metadataUpdateBooks");
		model.addAttribute("overlayAction", "doMetadataUpdateBooksInsertTns");
		return "metadata/miscButtonAndTableFormWithCheckboxForUpdate";
	}

	//do updates of pasted tn data for use in this page
	@RequestMapping(value="metadata/doMetadataUpdateBooksInsertTns", method=RequestMethod.POST)
	public String doUpdateTnsMetadataUpdateBooksPost(HttpServletRequest req, String doUpdates, String button, String site, String owningInstitution, String tnData, Principal principal,  Model model, Locale locale) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(tnData, 16);//!!!have to update when add new row to parsable metadata (this was for excel bug with ending missing /t sometimgs
			String tnList = "";
			for(List<String> r : rows) {
				tnList += ", '" + r.get(3) + "'";
			}
			tnList = tnList.substring(2);
			
			String validateFlag = validateData(model, locale, rows, owningInstitution, site);
			if(validateFlag != null)
				return validateFlag;
			
			//String dupTnMDList = bookService.getDuplicateTnsInMetadata(tnList); //get list of tns already in bookmetadata col=titleno 3rd(0based) elem in rnData rows
			//String dupTnList = bookService.getDuplicateTnsInBook(tnList);//dec 2013 check for tns in book table only.  if tn in metadata table, just update
			//List<String> dupTnListList = bookService.getDuplicateTnsInBookList(tnList);
			//String dupTnList = bookService.generateQuotedListString(dupTnListList);
			
		/*	if(dupTnList != "" && doUpdates == null) {
				//redisplay page with dupTnList msg
				
				//model.addAttribute("bookErrorMessage", messageSource.getMessage("metadata.alreadyExistMetadata", null, locale) + "\n" + dupTnList);
				//return "errors/generalError";
				model.addAttribute("dupeTnsInfo", dupTnList); //dec2013 this is now book tn dupes
				model.addAttribute("site", site); //requesting_location 
				model.addAttribute("owningInstitution", owningInstitution); //owning_institution 
				model.addAttribute("pastedData", tnData); 
				return getMetadataNewBooks(req, model, locale);//forward request but first pass in dupe list to show user
			}*/
			 
			String userName = bookService.getUser(principal.getName()).getName();
			addMiscColumns(rows, userName, site, owningInstitution); //site is requesting_Location
	

			bookService.insertBatch("BOOKMETADATAUPDATE", new String[]{"title", "author", "subject", "titleno", "callno", "partner_lib_callno", "filmno", "pages", "summary", "dgsno", "language", "owning_institution", "requesting_location", "scanning_location", "record_number", "date_original", "publisher_original", "filename", "current_timestamp_date_added", "metadata_adder"}, new int[] {Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR,Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR}, rows); 
		}
		return "redirect:metadataUpdateBooks"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	

	@RequestMapping(value="metadata/metadataUpdateBooks", method=RequestMethod.POST)
	public String getMetadataUpdateBooksPost(String button, HttpServletRequest request, Principal principal,  Model model, Locale locale) {
		if(button.equals("deleteAllUpdateBooks")) {
			 
			bookService.deleteAllUpdateMetadata();
		
			
		}else if(button.equals("deleteSelected") || button.equals("deleteSelected2")) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
			String tnList = "";
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						tnList = tnList + ", " + "'" + val[0] + "'";
				}
			}
			if(tnList.length()>1)
				tnList = tnList.substring(1);//remove starting comma
			
			bookService.deleteSelectedMetadataForUpdate(tnList);
			if(button.equals("deleteSelected2"))
				return  getMetadataUpdateBooksPost("checkTns", request, principal, model, locale);//fake redirect, since this is a post of when a button "checkTns" is pressed, not a get
		}else if("selectedMetadataDoUpdate".equals(button)) {
			Map<String, String[]> parameters = request.getParameterMap();
			Set<String> keys = parameters.keySet();
		
			List<String> tnList = new ArrayList();
			for(String k : keys) {
				if(k.startsWith("rowNum")) {
					String[] val = parameters.get(k);
					if(val != null && val.length >= 1) 
						tnList.add(val[0]);
				}
			} 

			 
			String dupListStr = bookService.sendToDoUpdateSelectedMetadata(tnList, principal.getName());
		 
			return "redirect:metadataUpdateBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if(button.equals("allMetadataDoUpdate")) {
			 
			String dupListStr = bookService.sendToDoUpdateAllMetadata(principal.getName());
			
			return "redirect:metadataUpdateBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url
		} 
 
		//should not happen
		return "redirect:metadataUpdateBooks"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	
	//////////////////////////////////////////
	
	///////////metadata complete and sent start/////////////
	@RequestMapping(value="metadata/metadataCompleteAndSent", method=RequestMethod.GET)
	public String getMetadataCompleteAndSentPost(  Model model, Locale locale) {
 
		
		//title and table labels
		List<String> labels = new ArrayList<String>();

		labels.add(messageSource.getMessage("title", null, locale));
		labels.add(messageSource.getMessage("author", null, locale));
		labels.add(messageSource.getMessage("subject", null, locale));
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("callNumber", null, locale));
		labels.add(messageSource.getMessage("partnerLibCallNumber", null, locale));
		labels.add(messageSource.getMessage("filmNumber", null, locale));
		labels.add(messageSource.getMessage("pages", null, locale));
		labels.add(messageSource.getMessage("summary", null, locale));
		labels.add(messageSource.getMessage("dgsNumber", null, locale));
		labels.add(messageSource.getMessage("language", null, locale));
		labels.add(messageSource.getMessage("owningInstitution", null, locale));
		labels.add(messageSource.getMessage("requestingLocation", null, locale));
		labels.add(messageSource.getMessage("scanningLocation", null, locale));
		labels.add(messageSource.getMessage("recordNumber", null, locale));
		labels.add(messageSource.getMessage("dateOriginal", null, locale));
		labels.add(messageSource.getMessage("publisherOriginal", null, locale));
		labels.add(messageSource.getMessage("fileName", null, locale));
		labels.add(messageSource.getMessage("sentToScan", null, locale));
		
		model.addAttribute("pageTitle", messageSource.getMessage("metadata.pageTitle.metadataCompleteAndSent", null, locale));
		model.addAttribute("colLabels", labels); 
		model.addAttribute("allTnsInfo", bookService.getMetadataCompleteAndSent()); 
		model.addAttribute("keyCol", 3);//use col 3 for request on TN 
		return "metadata/miscBookList";
	}
	///////////metadata complete and sent end/////////////
}