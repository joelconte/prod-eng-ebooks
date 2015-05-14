package org.familysearch.prodeng.admin.controller;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.familysearch.prodeng.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("adminArchiveMiscController")
public class ArchiveMiscController implements MessageSourceAware{

	private BookService bookService;
	private MessageSource messageSource;
	
	@Autowired
	public ArchiveMiscController(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	
	////////////tiffsBackup start////////////
	
	@RequestMapping(value="admin/tiffsBackup", method=RequestMethod.GET)
	public String getTiffsBackup(Model model, Locale locale) {
		//title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("dateArchived", null, locale));
		labels.add(messageSource.getMessage("driveSerialNumber", null, locale));
		labels.add(messageSource.getMessage("driveName", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.tiffsBackup", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getAdminTiffsBackupTnsInfo());  

		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteExcel", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkTns");
		details.add(messageSource.getMessage("checkTns", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkIfDateAdded");
		details.add(messageSource.getMessage("checkIfDateAdded", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("move");
		details.add(messageSource.getMessage("move", null, locale));	
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("cancelAndDelete");
		details.add(messageSource.getMessage("cancelAndDelete", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "tiffsBackup");
		model.addAttribute("overlayAction", "doTiffsBackupInsertTns");
		return "admin/miscButtonAndTableForm";
	}
	
	@RequestMapping(value="admin/tiffsBackup", method=RequestMethod.POST)
	public String getTiffsBackupPost(String button, Model model, Locale locale) {
		if("checkTns".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.tiffArchivingCopy1WithoutMatchingBooks", null, locale));
			model.addAttribute("colLabels", labels); 
			model.addAttribute("allTnsInfo", bookService.getAdminTiffArchivingWithoutMatchingBooksTnsInfo()); 
			return "admin/miscBookList";
		}else if("checkIfDateAdded".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("tiffOremArchivedDate", null, locale));
			labels.add(messageSource.getMessage("tiffOremDriveSerialNumber", null, locale));
			labels.add(messageSource.getMessage("tiffOremDriveName", null, locale));
			labels.add(messageSource.getMessage("tiffCopy2DriveName", null, locale));
			labels.add(messageSource.getMessage("dateReleased", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.tiffArchivingCopy1WithDateAlreadyAdded", null, locale));
			model.addAttribute("colLabels", labels);
			model.addAttribute("allTnsInfo", bookService.getAdminTiffArchivingWithMatchingDateTnsInfo()); 
			return "admin/miscBookList";
		}else if("move".equals(button)) {
			bookService.updateTiffArchivingCopy1(); 
			bookService.deleteTiffArchivingCopy1Entry();
			return "redirect:tiffsBackup";  //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if("cancelAndDelete".equals(button)) {
			 bookService.deleteTiffArchivingCopy1Entry();
			 return "redirect:switchboard"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}

		//should not happen
		return "redirect:tiffsBackup"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doTiffsBackupInsertTns", method=RequestMethod.POST)
	public String doInsertTnsTiffsBackupPost(String button, String tnData, Model model) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(tnData, -1);
			bookService.insertBatch("TIFF_ARCHIVING_COPY1_ENTRY", new String[]{"TN", "TIFF_OREM_ARCHIVED_DATE", "TIFF_OREM_SERIAL_num", "TIFF_OREM_DRIVE_NAME"}, new int[] {Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR}, rows); 
		}
		return "redirect:tiffsBackup"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 


	////////////tiffsBackup end/////////////

	////////////PDF Backup start////////////
	
	@RequestMapping(value="admin/pdfBackup", method=RequestMethod.GET)
	public String getPdfBackup(Model model, Locale locale) {
		//title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("dateArchived", null, locale));
		labels.add(messageSource.getMessage("driveSerialNumber", null, locale));
		labels.add(messageSource.getMessage("driveName", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.pdfArchivingOremForm", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getAdminPdfBackupTnsInfo());  

		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteExcel", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkTns");
		details.add(messageSource.getMessage("checkTns", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkIfArchived");
		details.add(messageSource.getMessage("checkIfArchived", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("move");
		details.add(messageSource.getMessage("move", null, locale));	
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("cancelAndDelete");
		details.add(messageSource.getMessage("cancelAndDelete", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "pdfBackup");
		model.addAttribute("overlayAction", "doPdfBackupInsertTns");
		return "admin/miscButtonAndTableForm";
	}
	
	@RequestMapping(value="admin/pdfBackup", method=RequestMethod.POST)
	public String getPdfBackupPost(String button, Model model, Locale locale) {
		if("checkTns".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.pdfArchivingCopy1WithoutMatchingBooks", null, locale));
			model.addAttribute("colLabels", labels); 
			model.addAttribute("allTnsInfo", bookService.getAdminPdfArchivingWithoutMatchingBooksTnsInfo()); 
			return "admin/miscBookList";
		}else if("checkIfArchived".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("site", null, locale));
			labels.add(messageSource.getMessage("pdfOremArchivedDate", null, locale));
			labels.add(messageSource.getMessage("pdfOremDriveSerialNumber", null, locale));
			labels.add(messageSource.getMessage("pdfOremDriveName", null, locale));
			labels.add(messageSource.getMessage("pdfCopy2DriveName", null, locale));
			labels.add(messageSource.getMessage("pdfCopy2ArchivedDate", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.pdfArchivingCopy1WithDateAlreadyAdded", null, locale));
			model.addAttribute("colLabels", labels);
			model.addAttribute("allTnsInfo", bookService.getAdminPdfArchivingWithMatchingDateTnsInfo()); 
			return "admin/miscBookList";
		}else if("move".equals(button)) {
			bookService.updatePdfArchivingCopy1(); 
			bookService.deletePdfArchivingCopy1Entry();
			return "redirect:pdfBackup";  //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if("cancelAndDelete".equals(button)) {
			 bookService.deletePdfArchivingCopy1Entry();
			 return "redirect:switchboard"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}

		//should not happen
		return "redirect:pdfBackup"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doPdfBackupInsertTns", method=RequestMethod.POST)
	public String doInsertTnsPdfBackupPost(String button, String tnData, Model model) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(tnData, -1);
			bookService.insertBatch("PDF_ARCHIVING_COPY1_ENTRY", new String[]{"TN", "PDF_OREM_ARCHIVED_DATE", "PDF_OREM_SERIAL_num", "PDF_OREM_DRIVE_NAME"}, new int[] {Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR}, rows); 
		}
		return "redirect:pdfBackup"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 


	////////////PDF Backup end/////////////

	////////////PDF Archive start////////////
	
	@RequestMapping(value="admin/pdfArchive", method=RequestMethod.GET)
	public String getPdfArchive(Model model, Locale locale) {
		//title and table labels
		List<String> labels = new ArrayList<String>();
		labels.add(messageSource.getMessage("titleNumber", null, locale));
		labels.add(messageSource.getMessage("dateArchived", null, locale));
		labels.add(messageSource.getMessage("driveSerialNumber", null, locale));
		labels.add(messageSource.getMessage("driveName", null, locale));
		model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.pdfArchivingCopy2Form", null, locale));
		model.addAttribute("colLabels", labels);
		model.addAttribute("allTnsInfo", bookService.getAdminPdfArchiveCopy2TnsInfo());  

		//buttons
		List<List<String>> buttons = new ArrayList<List<String>>();
		List<String> details = new ArrayList<String>();
		details.add("overlayButton");//flag
		details.add(messageSource.getMessage("pasteExcel", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkTns");
		details.add(messageSource.getMessage("checkTns", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("checkIfArchived");
		details.add(messageSource.getMessage("checkIfArchived", null, locale));
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("move");
		details.add(messageSource.getMessage("move", null, locale));	
		buttons.add(details);
		details = new ArrayList<String>();
		details.add("cancelAndDelete");
		details.add(messageSource.getMessage("cancelAndDelete", null, locale));
		buttons.add(details);
		model.addAttribute("buttons", buttons);
		
		//form actions
		model.addAttribute("buttonsAction", "pdfArchive");
		model.addAttribute("overlayAction", "doPdfArchiveInsertTns");
		return "admin/miscButtonAndTableForm";
	}
	
	@RequestMapping(value="admin/pdfArchive", method=RequestMethod.POST)
	public String getPdfArchivePost(String button, Model model, Locale locale) {
		if("checkTns".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.pdfArchivingCopy2WithoutMatchingBooks", null, locale));
			model.addAttribute("colLabels", labels); 
			model.addAttribute("allTnsInfo", bookService.getAdminPdfArchivingCopy2WithoutMatchingBooksTnsInfo()); 
			return "admin/miscBookList";
		}else if("checkIfArchived".equals(button)) {
			List<String> labels = new ArrayList<String>();
			labels.add(messageSource.getMessage("titleNumber", null, locale));
			labels.add(messageSource.getMessage("pdfCopy2DriveSerialNumber", null, locale));
			labels.add(messageSource.getMessage("pdfCopy2DriveName", null, locale));
			labels.add(messageSource.getMessage("pdfCopy2ArchivedDate", null, locale));
			labels.add(messageSource.getMessage("pdfOremDriveName", null, locale));
			labels.add(messageSource.getMessage("pdfOremArchivedDate", null, locale));
			model.addAttribute("pageTitle", messageSource.getMessage("admin.pageTitle.pdfArchivingCopy2WithDateAlreadyAdded", null, locale));
			model.addAttribute("colLabels", labels); 
			model.addAttribute("allTnsInfo", bookService.getAdminPdfArchivingCopy2WithMatchingDateTnsInfo()); 
			return "admin/miscBookList";
		}else if("move".equals(button)) {
			bookService.updatePdfArchivingCopy2(); 
			bookService.deletePdfArchivingCopy2Entry();
			return "redirect:pdfArchive";  //redirect get - guard against refresh-multi-updates and also update displayed url
		}else if("cancelAndDelete".equals(button)) {
			 bookService.deletePdfArchivingCopy2Entry();
			 return "redirect:switchboard"; //redirect get - guard against refresh-multi-updates and also update displayed url
		}

		//should not happen
		return "redirect:pdfArchive"; //redirect get - guard against refresh-multi-updates and also update displayed url

	}
	 
	//do insert of pasted tn data for use in this page
	@RequestMapping(value="admin/doPdfArchiveInsertTns", method=RequestMethod.POST)
	public String doInsertTnsPdfArchivePost(String button, String tnData, Model model) {
		if(button.equals("save")) {
			 
			List<List<String>> rows = bookService.parseExcelData(tnData, -1);
			bookService.insertBatch("PDF_ARCHIVING_COPY2_ENTRY", new String[]{"TN", "PDF_COPY2_ARCHIVED_DATE", "PDF_COPY2_SERIAL_num", "PDF_COPY2_DRIVE_NAME"}, new int[] {Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR}, rows); 
		}
		return "redirect:pdfArchive"; //redirect - guard against refresh-multi-updates and also update displayed url
	} 
	 


	////////////PDF Archive end/////////////
 
}
