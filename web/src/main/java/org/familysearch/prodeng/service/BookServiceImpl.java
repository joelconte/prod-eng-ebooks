/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.service;

import java.io.File;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.familysearch.prodeng.model.Book;
import org.familysearch.prodeng.model.BookMetadata;
import org.familysearch.prodeng.model.Problem;
import org.familysearch.prodeng.model.Search;
import org.familysearch.prodeng.model.Site;
import org.familysearch.prodeng.model.SqlTimestampPropertyEditor;
import org.familysearch.prodeng.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//not import org.springframework.beans.factory.parsing.Problem;

@Service("bookService")
public class BookServiceImpl extends NamedParameterJdbcDaoSupport implements BookService {
 
	private DataSource ds;
	private SimpleJdbcInsert bookModelJdbcInsert; 
	private Validator validator; 
	private SqlTimestampPropertyEditor tsConvert = new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a");
	private List<String> badWords = new ArrayList<String>();
	{
		badWords.add("insert");
		badWords.add("update");
		badWords.add("delete");
		badWords.add("drop");
		badWords.add("alter");
	}
	
	BookServiceImpl() {
		//In case cglib is being used.
	}

	@Inject
	public BookServiceImpl(Validator validator, DataSource dataSource ) {
		this.ds = dataSource;
		this.validator = validator;
		setDataSource(dataSource);//required to get jdbctemplate from jdbcDaoSupport..
	}
	
	/* (non-Javadoc)
	 * @see org.familysearch.prodeng.service.BookService#createBook(org.familysearch.prodeng.model.Book)
	 */
	@Override
	@Transactional
	public void createBook(Book book) throws ConstraintViolationException {
	
	} 
  

	/* (non-Javadoc)
	 * @see org.familysearch.prodeng.service.BookService#getAllTns)
	 */
	@Override
	public List<String> getAllTns() {
		 
		List<String> tnList = getJdbcTemplate().query("select tn from BOOK", new StringRowMapper());
		//nList.add(0, " ");//dummy since spring mvc puts '[' at first and ']' at end if not using spring form
		//tnList.add("");//empty selection for null value
		//tnList.add(" ");//dummy
	
		return tnList;
	}
 
	@Override
	public List<String> getBooksByWildcard(String searchBy) {
		 
		List<String> tnList = getJdbcTemplate().query("select tn from BOOK where tn like '"+searchBy+"%'", new StringRowMapper());
		//nList.add(0, " ");//dummy since spring mvc puts '[' at first and ']' at end if not using spring form
		//tnList.add("");//empty selection for null value
		//tnList.add(" ");//dummy
	
		return tnList;
	}
	
	@Override
	public List<List> getScanScanReadyTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select a.tn,  a.partner_Lib_Call_#,  a.record_number,  a.title,  a.scan_num_of_Pages,  a.requesting_location,  a.scanned_by,  to_char(b.sent_to_scan, 'mm/dd/yyyy')  from s_01_ready_scan a left outer join bookmetadata b on  a.tn=b.titleno where  a.tn not in (select tn from s_tf_problems) ", new StringX8RowMapper());
		else
			tnList = getJdbcTemplate().query("select  a.tn,  a.partner_Lib_Call_#,  a.record_number,  a.title,  a.scan_num_of_Pages,  a.requesting_location,  a.scanned_by,  to_char(b.sent_to_scan, 'mm/dd/yyyy')  from s_01_ready_scan a  left outer join bookmetadata b on  a.tn=b.titleno  where  a.requesting_location = ? and  a.tn not in (select tn from s_tf_problems)", new Object[]{location}, new StringX8RowMapper());
		
		return tnList;
	}
	
	@Override
	public List<List> getScanScanInProgressTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn, partner_Lib_Call_#, record_number, title, scan_num_of_Pages, scanned_by from s_01B_scan_in_prog  where tn not in (select tn from s_tf_problems)", new StringX6RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn, partner_Lib_Call_#, record_number, title, scan_num_of_Pages, scanned_by from s_01B_scan_in_prog where  scanned_by = ? and tn not in (select tn from s_tf_problems)", new Object[]{location}, new StringX6RowMapper());
		
		return tnList;
	}
	
	@Override
	public List<List> getScanAuditReadyTnsInfo(String location) {
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn, partner_Lib_Call_#, record_number, title, scan_num_of_Pages, scanned_by from s_02_ready_image_audit where tn not in (select tn from s_tf_problems)", new StringX6RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn, partner_Lib_Call_#, record_number, title, scan_num_of_Pages, scanned_by from s_02_ready_image_audit  where  scanned_by = ?  and tn not in (select tn from s_tf_problems)", new Object[]{location}, new StringX6RowMapper());
		return tnList;
	}

	@Override
	public List<List> getScanAuditInProgressTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn, partner_Lib_Call_#, record_number, title, scan_num_of_Pages, scan_ia_start_date, scan_image_auditor, scanned_by from s_03_image_auditing_in_prog  where tn not in (select tn from s_tf_problems) ", new StringX8RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn, partner_Lib_Call_#, record_number, title, scan_num_of_Pages, scan_ia_start_date, scan_image_auditor, scanned_by from s_03_image_auditing_in_prog  where scanned_by  = ? and tn not in (select tn from s_tf_problems)", new Object[]{location}, new StringX8RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getScanProcessedReadyForOremTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn,  partner_Lib_Call_#, record_number, title, scan_num_of_Pages,  files_sent_to_orem, remarks_from_scan_center, scanned_by from s_04_processed_ready_orem  where tn not in (select tn from s_tf_problems) ", new StringX8RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn,  partner_Lib_Call_#, record_number, title, scan_num_of_Pages,  files_sent_to_orem, remarks_from_scan_center, scanned_by from s_04_processed_ready_orem   where scanned_by  = ? and    tn not in (select tn from s_tf_problems)", new Object[]{location}, new StringX8RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getScanProblemTnsInfo(String location){	
		List tnList;
		if(location == null || location.equals("") || location.equals("All Problems"))
			tnList = getJdbcTemplate().query("select a.tn, q.step,  status,  problem_reason,  problem_text,  TO_CHAR(problem_date, 'mm/dd/yyyy'), problem_initials, call_#, scanned_by, a.solution_owner from TF_AllProblems a, TFALL_0x_All_queues q where a.tn = q.tn ", new StringX10RowMapper());
		else
			tnList = getJdbcTemplate().query("select a.tn, q.step, status, problem_reason,  problem_text,  TO_CHAR(problem_date, 'mm/dd/yyyy'), problem_initials, call_#, scanned_by  , a.solution_owner from TF_AllProblems  a , TFALL_0x_All_queues q  where a.solution_owner = ? and a.tn = q.tn", new Object[]{location},  new StringX10RowMapper());
		return tnList;
	}
	
	
	
	
	
	@Override
	public List<List> getProcessWaitingForFilesTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn, num_of_pages, scanned_by, TO_CHAR(files_sent_to_orem, 'mm/dd/yyyy') , TO_CHAR(files_received_by_orem, 'mm/dd/yyyy') from tf_1_waiting_for_files  where tn not in (select tn from tf_problems)", new StringX5RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn, num_of_pages, scanned_by,  TO_CHAR(files_sent_to_orem, 'mm/dd/yyyy') , TO_CHAR(files_received_by_orem, 'mm/dd/yyyy') from tf_1_waiting_for_files  where scanned_by  = ? and   tn not in (select tn from tf_problems)", new Object[]{location},  new StringX5RowMapper());
		return tnList;
	}
	
	
	@Override
	public List<List> getProcessTitleCheckTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn, num_of_pages, scanned_by,location, TO_CHAR(files_received_by_orem, 'mm/dd/yyyy') from tf_2_ready_to_title_check where tn not in (select tn from tf_problems)", new StringX5RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn, num_of_pages, scanned_by, location, TO_CHAR(files_received_by_orem, 'mm/dd/yyyy') from tf_2_ready_to_title_check  where scanned_by  = ? and  tn not in (select tn from tf_problems)", new Object[]{location},  new StringX5RowMapper());
		
		return tnList;
	}
	
	@Override
	public List<List> getProcessTitleCheckInProgressTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn,  scanned_by, image_audit,TO_CHAR(ia_start_date, 'mm/dd/yyyy'), num_of_pages from tf_3_title_check_in_process where tn not in (select tn from tf_problems) ", new StringX5RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn,  scanned_by, image_audit,TO_CHAR(ia_start_date, 'mm/dd/yyyy'), num_of_pages from tf_3_title_check_in_process  where scanned_by  = ? and  tn not in (select tn from tf_problems)", new Object[]{location},  new StringX5RowMapper());
		
		return tnList;
	}

	@Override
	public List<List> getProcessOcrReadyTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn,  scanned_by, filename, tiff_orem_drive_name, num_of_pages, OCR_by, TO_CHAR(OCR_complete_date, 'mm/dd/yyyy') from  TF_4_Ready_to_OCR where tn not in (select tn from tf_problems)", new StringX7RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn,  scanned_by, filename, tiff_orem_drive_name, num_of_pages, OCR_by, TO_CHAR(OCR_complete_date, 'mm/dd/yyyy') from  TF_4_Ready_to_OCR  where scanned_by  = ? and tn not in (select tn from tf_problems)", new Object[]{location},  new StringX7RowMapper());
		
		return tnList;
	}
	@Override
	public List<List> getProcessOcrInProgressTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn,  scanned_by, filename, tiff_orem_drive_name, num_of_pages, OCR_by, TO_CHAR(OCR_complete_date, 'mm/dd/yyyy') from  TF_4a_OCR where tn not in (select tn from tf_problems) ", new StringX7RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn,  scanned_by, filename, tiff_orem_drive_name, num_of_pages, OCR_by, TO_CHAR(OCR_complete_date, 'mm/dd/yyyy') from  TF_4a_OCR  where scanned_by  = ? and tn not in (select tn from tf_problems)", new Object[]{location},  new StringX7RowMapper());
		
		return tnList;
	}
	
	@Override
	public List<List> getProcessPdfDownloadTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn,  scanned_by, filename, tiff_orem_drive_name, num_of_pages, OCR_by, TO_CHAR(OCR_complete_date, 'mm/dd/yyyy') from tf_4b_pdf_download where tn not in (select tn from tf_problems)", new StringX7RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn,  scanned_by, filename, tiff_orem_drive_name, num_of_pages, OCR_by, TO_CHAR(OCR_complete_date, 'mm/dd/yyyy') from tf_4b_pdf_download  where scanned_by  = ? and tn not in (select tn from tf_problems)", new Object[]{location},  new StringX7RowMapper());
		
		return tnList;
	}
	
	
	@Override
	public List<List> getProcessPdfTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn,   num_of_pages, scanned_by, filename, TO_CHAR(OCR_complete_date, 'mm/dd/yyyy') from tf_5_ready_to_pdfreview where tn not in (select tn from tf_problems)", new StringX5RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn,   num_of_pages, scanned_by, filename, TO_CHAR(OCR_complete_date, 'mm/dd/yyyy') from tf_5_ready_to_pdfreview  where scanned_by  = ? and  tn not in (select tn from tf_problems)", new Object[]{location},  new StringX5RowMapper());
		
		return tnList;
	}
	
	@Override
	public List<List> getProcessPdfInProgressTnsInfo(String location){
		List tnList;
		if(location == null || location.equals(""))
			tnList = getJdbcTemplate().query("select tn, scanned_by,  pdfreview_by, TO_CHAR(pdfreview_start_date, 'mm/dd/yyyy'), num_of_pages from tf_6_pdfreview_in_process where tn not in (select tn from tf_problems) ", new StringX5RowMapper());
		else
			tnList = getJdbcTemplate().query("select tn, scanned_by,  pdfreview_by, TO_CHAR(pdfreview_start_date, 'mm/dd/yyyy'), num_of_pages from tf_6_pdfreview_in_process where scanned_by  = ? and  tn not in (select tn from tf_problems)", new Object[]{location},  new StringX5RowMapper());
		
		return tnList;
	}
	
	@Override
	public List<List> getProcessProblemTnsInfo(String location){
		List tnList;
		if(location == null || location.equals("") || location.equals("All Sites"))
			tnList = getJdbcTemplate().query("select a.tn, q.step, scanned_by, status, problem_reason, problem_text,  TO_CHAR(problem_date, 'mm/dd/yyyy'), problem_initials, call_# , a.solution_owner from TF_AllProblems a , TFALL_0x_All_queues q where a.tn = q.tn ", new StringX10RowMapper());
		else
			tnList = getJdbcTemplate().query("select a.tn, q.step, scanned_by, status, problem_reason, problem_text,  TO_CHAR(problem_date, 'mm/dd/yyyy'), problem_initials, call_# ,  a.solution_owner from TF_AllProblems a, TFALL_0x_All_queues q where a.tn = q.tn and a.solution_owner = ?", new Object[]{location},  new StringX10RowMapper());
		
		return tnList;
	}
	
	
	
	@Override
	public List<List> getAdminProblemTnsInfo(){
		List tnList = getJdbcTemplate().query("select a.tn,   q.step,  status,  problem_reason,  problem_text,  TO_CHAR(problem_date, 'mm/dd/yyyy'), problem_initials, call_#,  a.solution_owner from tf_allproblems a, TFALL_0x_All_queues q where a.tn = q.tn ", new StringX9RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getAdminPdfDateNoReleaseDateTnsInfo(){
		List tnList = getJdbcTemplate().query("select tn,   pdf_ready, filename, num_of_pages, scanned_by, owning_institution from TF_IN_PROC_PDF_DATE_NO_REL_DAT ", new StringX6RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getAdminReceivedNotesTnsInfo(){
		List tnList = getJdbcTemplate().query("select tn, notes_from_site from TF_RECEIVED_IMAGES_ENTRY ", new StringX2RowMapper());
		return tnList;
	}
	 
	@Override
	public List<List> getAdminReceivedImagesWithoutMatchingBooksTnsInfo(){
		List tnList = getJdbcTemplate().query("select tn from TF_Received_Images_Wo_Mch_Bks ", new StringX1RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getAdminReceivedImagesWithDateAlreadyTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_RECEIVED_IMAGES_WITH_DATE ", new StringX6RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getAdminTiffsBackupTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TIFF_ARCHIVING_COPY1_ENTRY ", new StringX4RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getAdminTiffArchivingWithoutMatchingBooksTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_TIFF_ARCHIVING_COPY1_WO_M_B ", new StringX1RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getAdminTiffArchivingWithMatchingDateTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_TIFF_ARCHIVING_COPY1_W_DATE ", new StringX6RowMapper());
		return tnList;
	}


	@Override
	public List<List> getAdminPdfBackupTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from PDF_ARCHIVING_COPY1_ENTRY ", new StringX4RowMapper());
		return tnList;
	}

	@Override
	public List<List> getAdminPdfArchivingWithoutMatchingBooksTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_PDF_ARCHIVING_COPY1_WO_M_B ", new StringX1RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getAdminPdfArchivingWithMatchingDateTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_PDF_ARCHIVING_COPY1_W_DATE ", new StringX7RowMapper());
		return tnList;
	}
	
	
	@Override
	public List<List> getAdminPdfArchiveCopy2TnsInfo(){
		List tnList = getJdbcTemplate().query("select * from PDF_ARCHIVING_COPY2_ENTRY ", new StringX4RowMapper());
		return tnList;
	}
	@Override
	public List<List> getAdminPdfArchivingCopy2WithoutMatchingBooksTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_PDF_ARCHIVING_COPY2_WO_M_B ", new StringX1RowMapper());
		return tnList;
	}
	@Override
	public List<List> getAdminPdfArchivingCopy2WithMatchingDateTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_PDF_ARCHIVING_COPY2_W_DATE ", new StringX6RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getAdminBooksLoadedOnlinTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_LOADING_ENTRY ", new StringX7RowMapper());
		return tnList;
	}
	@Override
	public List<List>  getAdminLoadingEntryWithoutMatchingBooksTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_LOADING_ENTRY_WITHOUT_MATCH ", new StringX2RowMapper());
		return tnList;
	}
	@Override
	public List<List>  getAdminLoadingEntryWithMatchingDateTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_LOADING_ENTRY_WITH_DATE_ALR ", new StringX5RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getAdminReleasedBooksTnsInfo(){
		List tnList = getJdbcTemplate().query("SELECT TN, Date_Released	FROM TF_Released_entry", new StringX2RowMapper());
		return tnList;
	}
	@Override
	public List<List> getAdminReleaseEntryWithoutMatchingBooksTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_RELEASE_ENTRY_WITHOUT_MATCH ", new StringX1RowMapper());
		return tnList;
	}
	@Override
	public List<List>  getAdminReleaseEntryWithMatchingDateTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_RELEASE_ENTRY_WITH_DATE_ALR ", new StringX16RowMapper());
		return tnList;
	}
	@Override
	public List<List> getAdminReleaseEntryBatchClassTnsInfo(){
		List tnList = getJdbcTemplate().query("select * from TF_RELEASE_ENTRY_BATCH_CLASS ", new StringX4RowMapper());
		return tnList;
	}

	@Override
	public List<List> getSearchTnsList(String tnStrList){
		String inClause = generateInClause("tn", tnStrList);
		List tnList = getJdbcTemplate().query("select tn, secondary_identifier, title, author, subject, requesting_location, scan_complete_date, files_sent_to_orem, files_received_by_orem, tiff_orem_drive_name, pdf_ready, date_released, date_loaded, url from BOOK where " + inClause, new StringX14RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getSearchSecondaryIdsList(String tnStrList){
		String inClause = generateInClause("secondary_identifier", tnStrList);
		List tnList = getJdbcTemplate().query("select tn, secondary_identifier, title, author, subject, requesting_location, scan_complete_date from BOOK where " + inClause, new StringX7RowMapper());
		return tnList;
	}
	
	@Override
	public List<List> getSearchUrlsList(String tnStrList){
		String inClause = generateInClause("tn", tnStrList);
		String inClause2 = generateInClause("secondary_identifier", tnStrList);
		List tnList= getJdbcTemplate().query("select tn, secondary_identifier, title, url from BOOK where " + inClause + " or " + inClause2, new StringX4RowMapper());
		return tnList;
	}
	@Override
	public List<List> getSearchPidsList(String tnStrList){
		String inClause = generateInClause("tn", tnStrList);
		List tnList= getJdbcTemplate().query("select tn, title, PID from BOOK where " + inClause, new StringX3RowMapper());
		return tnList;
	}
	@Override
	public List<String> getAllSitesIncludingInactive() {
		List<String> sList = getJdbcTemplate().query("select id from SITE order by id", new StringRowMapper());
		return sList;
	}
	@Override
	public List<String> getAllSites() {
		List<String> sList = getJdbcTemplate().query("select id from SITE  where  ( is_inactive_site !='T' or is_inactive_site is null) order by id", new StringRowMapper());
		return sList;
	}
	@Override
	public List<String> getAllScanSites() {
		List<String> sList = getJdbcTemplate().query("select id from SITE where is_scan_site = 'T' and  ( is_inactive_site !='T' or is_inactive_site is null)  order by id", new StringRowMapper());
		return sList;
	}
	
	
	@Override
	public List<String> getAllPropertyRights() {
		List<String> sList = getJdbcTemplate().query("select id from PROPERTYRIGHT", new StringRowMapper());
		return sList;
	}
	
	@Override
	public List<String> getAllPublicationTypes() {
		List<String> sList = getJdbcTemplate().query("select id from publication_type", new StringRowMapper());
		return sList;
	}
	
	@Override
	public List<List> getAllBatchClasses() {
		List<List> sList = getJdbcTemplate().query("select id, propertyright, start_char, end_char from batchclass", new StringX4RowMapper());
		return sList;
	}
	 
	
	@Override
	public Book getBook(String tn) {
		try {
			return getJdbcTemplate().queryForObject("select * from BOOK where TN=?", new BookRowMapper(), tn);
		}catch(EmptyResultDataAccessException e) 
		{ 
			return new Book(); //empty for backing bean
		}
	} 

	@Override
	public Book getBookBySecondaryIdentifier(String id) {
		try {
			return getJdbcTemplate().queryForObject("select * from BOOK where secondary_identifier=?", new BookRowMapper(), id);
		}catch(EmptyResultDataAccessException e) 
		{ 
			return new Book(); //empty for backing bean
		}
	} 

	private static class BookRowMapper implements RowMapper<Book> {
		@Override
		public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
			Book book = new Book();
			book.setTn(rs.getString("TN"));
			book.setSecondaryIdentifier(rs.getString("SECONDARY_IDENTIFIER"));
			book.setOclcNumber(rs.getString("OCLC_NUMBER"));
			book.setIsbnIssn(rs.getString("ISBN_ISSN"));
			book.setTitle(rs.getString("TITLE"));
			book.setAuthor(rs.getString("AUTHOR"));
			book.setPropertyRight(rs.getString("PROPERTY_RIGHT"));
			book.setPublicationType(rs.getString("PUBLICATION_TYPE"));
			book.setFilename(rs.getString("FILENAME"));
			book.setCallNumber(rs.getString("CALL_#"));
			book.setPartnerLibCallNumber(rs.getString("PARTNER_LIB_CALL_#"));
			book.setPriorityItem(rs.getString("PRIORITY_ITEM"));
			book.setWithdrawn(rs.getString("WITHDRAWN"));
			book.setDigitalCopyOnly(rs.getString("DIGITAL_COPY_ONLY"));
			book.setMediaType(rs.getString("MEDIA_TYPE"));
			book.setMetadataComplete(rs.getTimestamp("METADATA_COMPLETE"));
			book.setBatchClass(rs.getString("BATCH_CLASS"));
			book.setLanguage(rs.getString("LANGUAGE"));
			book.setRemarksFromScanCenter(rs.getString("REMARKS_FROM_SCAN_CENTER"));
			book.setRemarksAboutBook(rs.getString("REMARKS_ABOUT_BOOK"));
			book.setRecordNumber(rs.getString("RECORD_NUMBER"));
			book.setRequestingLocation(rs.getString("REQUESTING_LOCATION"));
			book.setOwningInstitution(rs.getString("OWNING_INSTITUTION"));
			book.setScannedBy(rs.getString("SCANNED_BY"));
			book.setScanOperator(rs.getString("SCAN_OPERATOR"));
			book.setScanMachineId(rs.getString("SCAN_MACHINE_ID"));
			book.setScanMetadataComplete(rs.getTimestamp("SCAN_METADATA_COMPLETE"));
			book.setLocation(rs.getString("LOCATION"));
			book.setScanStartDate(rs.getTimestamp("SCAN_START_DATE"));
			book.setScanCompleteDate(rs.getTimestamp("SCAN_COMPLETE_DATE"));
			book.setScanImageAuditor(rs.getString("SCAN_IMAGE_AUDITOR"));
			book.setScanIaStartDate(rs.getTimestamp("SCAN_IA_START_DATE"));
			book.setScanIaCompleteDate(rs.getTimestamp("SCAN_IA_COMPLETE_DATE"));
			book.setFilesSentToOrem(rs.getTimestamp("FILES_SENT_TO_OREM"));
			book.setScanNumOfPages(rs.getString("SCAN_NUM_OF_PAGES"));
			book.setNumOfPages(rs.getString("NUM_OF_PAGES"));
			book.setFilesReceivedByOrem(rs.getTimestamp("FILES_RECEIVED_BY_OREM"));
			book.setImageAudit(rs.getString("IMAGE_AUDIT"));
			book.setIaStartDate(rs.getTimestamp("IA_START_DATE"));
			book.setIaCompleteDate(rs.getTimestamp("IA_COMPLETE_DATE"));
			book.setOcrBy(rs.getString("OCR_by"));
			book.setOcrStartDate(rs.getTimestamp("OCR_start_date"));
			book.setOcrCompleteDate(rs.getTimestamp("OCR_complete_date"));
			book.setPdfDownloadBy(rs.getString("PDF_DOWNLOAD_BY"));
			book.setPdfDownloadDate(rs.getTimestamp("PDF_DOWNLOAD_DATE"));
			book.setPdfreviewBy(rs.getString("Pdfreview_BY"));
			book.setPdfreviewStartDate(rs.getTimestamp("Pdfreview_START_DATE"));
			book.setPdfReady(rs.getTimestamp("PDF_READY"));
			book.setDateReleased(rs.getTimestamp("DATE_RELEASED"));
			book.setCompressionCode(rs.getString("COMPRESSION_CODE"));
			book.setLoadedBy(rs.getString("LOADED_BY"));
			book.setDateLoaded(rs.getTimestamp("DATE_LOADED"));
			book.setCollection(rs.getString("COLLECTION"));
			book.setDnp(rs.getString("DNP"));
			book.setDnpDeletedOffLine(rs.getString("DNP_DELETED_OFF_LINE"));
			book.setTnChangeHistory(rs.getString("TN_CHANGE_HISTORY"));
			book.setPdfOremArchivedDate(rs.getTimestamp("PDF_OREM_ARCHIVED_DATE"));
			book.setPdfOremDriveSerialNumber(rs.getString("PDF_OREM_DRIVE_SERIAL_#"));
			book.setPdfOremDriveName(rs.getString("PDF_OREM_DRIVE_NAME"));
			book.setPdfCopy2ArchivedDate(rs.getTimestamp("PDF_COPY2_ARCHIVED_DATE"));
			book.setPdfCopy2DriveSerialNumber(rs.getString("PDF_COPY2_DRIVE_SERIAL_#"));
			book.setPdfCopy2DriveName(rs.getString("PDF_COPY2_DRIVE_NAME"));
			book.setTiffOremArchivedDate(rs.getTimestamp("TIFF_OREM_ARCHIVED_DATE"));
			book.setTiffOremDriveSerialNumber(rs.getString("TIFF_OREM_DRIVE_SERIAL_#"));
			book.setTiffOremDriveName(rs.getString("TIFF_OREM_DRIVE_NAME"));
			book.setTiffCopy2ArchivedDate(rs.getTimestamp("TIFF_COPY2_ARCHIVED_DATE"));
			book.setTiffCopy2DriveSerialNumber(rs.getString("TIFF_COPY2_DRIVE_SERIAL_#"));
			book.setTiffCopy2DriveName(rs.getString("TIFF_COPY2_DRIVE_NAME"));
			book.setPdfSentToLoad(rs.getTimestamp("PDF_SENT_TO_LOAD"));
			book.setSite(rs.getString("SITE"));
			book.setUrl(rs.getString("URL"));
			book.setPid(rs.getString("PID"));
			book.setPagesOnline(rs.getString("PAGES_ONLINE"));
			book.setSubject(rs.getString("subject"));
			book.setFilmno(rs.getString("filmno"));
			book.setPagesPhysicalDescription(rs.getString("pages_Physical_Description"));
			book.setSummary(rs.getString("summary"));
			book.setDgsno(rs.getString("dgsno"));
			book.setDateOriginal(rs.getTimestamp("date_Original"));
			book.setPublisherOriginal(rs.getString("publisher_Original"));
			book.setFhcTitle(rs.getString("fhc_title"));
			book.setFhcTn(rs.getString("fhc_tn"));
			book.setDateRepublished(rs.getTimestamp("date_republished"));
			book.setPullDate(rs.getTimestamp("pull_date"));

			return book;
		}
	}
	
	//same as getBook, but gets numOfPages from scan page count if not set
	//not used anymore
	@Override
	public Book getBookForProcess(String tn) {
		Book b = this.getBook(tn);
		return b;
	}
	
	
	//prep book when moving from scan to process: numOfPages from scan page count if not set
	@Override
	public Book prepareBookForScanToProcessUpdate(Book b, Book oldBook, String tn) {
		
		//get scanned number of pages as initial value for num of pages when "sent to Orem" is set for first time
		if(oldBook.getNumOfPages() == null || oldBook.getNumOfPages().equals(""))
		{
			b.setNumOfPages(b.getScanNumOfPages());
		}
		
		//set batchclass
		String lang = b.getLanguage(); 
		String firstLetter = "";
		if(lang != null && !lang.equals(""))
			firstLetter = lang.substring(0,1);
		
		String propertyRight = b.getPropertyRight();
		
		List<String> sList = getJdbcTemplate().query("select id from batchclass where propertyright=? and '"+firstLetter.toUpperCase()+"' between upper(start_char) and upper(end_char)", new StringRowMapper(), propertyRight);
		if(sList.size() > 0)
			b.setBatchClass(sList.get(0));

		return b;
	}

	@Override
	public boolean isTransitionScanToProcess(Book b, Book oldBook, String tn) {
	
		//check if files sent to orem has new date for first time
		if((oldBook.getFilesSentToOrem() == null || oldBook.getFilesSentToOrem().equals(""))
				&& (b.getFilesSentToOrem() != null && b.getFilesSentToOrem().equals("") == false))
		{
			//we get here on last update in scan workflow right before book goes to process workflow
			return true;
		}
		return false;
	}
	
	/* General insert.  You may pass in fewer column data than exist in the table.  If so, we assume
	 * that the first X columns are the columns to be used in the insert sql. (ie if user copies/pastes only 1 column of 
	 * data to insert, but table has 2 columns...)
	 */
	@Override
	public void insertBatch(String tableName, String[] columnNames, int[] colTypes, List<List<String>> rows) {

		int colCount = columnNames.length;//rows.get(0).size(); //actual data, which may be a subset of columns in table
		int rowCount = rows.size();
		
		String columns = "(";
		String values = "(";
		for(int x = 0; x < colCount; x++) {
			if(x != 0)
			{
				columns += ", ";
				values +=  ", ";
			}
			//check for special flag values concatinated to column names
			if(columnNames[x].startsWith("current_timestamp_")) {
				columns += columnNames[x].substring(18);
				values += "current_timestamp"; 
			}else {
				columns += columnNames[x];
				values += ":" + columnNames[x]; 
			}
		}
		columns += ") ";
		values +=  ") ";
		
		String sql = "insert into " + tableName + columns +
				" values " + values;
		
		Map<String, Object> params = new HashMap<String, Object>(); //one row of params
		Map<String, Object> paramsArray[] = new HashMap[rowCount]; //multiple rows of params

		int iRow = 0;
		for (List<String> row : rows) {
			int iCol = 0;
			for (String val : row) {
				if(colTypes[iCol] == Types.TIMESTAMP && val != null){
					//if "current_timestamp" then already hardcoded it above as value
					if(!val.equals("current_timestamp"))
						params.put(columnNames[iCol], tsConvert.textToTimestamp(val));//only put in value if not current_timestamp
				}else {
					params.put(columnNames[iCol], val);
				}
				iCol++; // = (iCol == colCount ? 0 : ++iCol);
			}
			paramsArray[iRow++] = params; 
			params = new HashMap<String, Object>();
		}
		getNamedParameterJdbcTemplate().batchUpdate(sql, paramsArray);
	}

	@Override
	public void updateBatchMetatdataUpdates(String tableName, String[] columnNames,  List<List> rows) {
		int colCount = columnNames.length;//rows.get(0).size(); //actual data, which may be a subset of columns in table
		int rowCount = rows.size();
		 
		int iRow = 0;
		for (List<String> row : rows) {
			Book b = this.getBook(row.get(3));
 
			String val = row.get(0);
			if(val != null && "".equals(val) == false) {
				b.setTitle(val);
			}
			val = row.get(1);
			if(val != null && "".equals(val) == false) {
				b.setAuthor(val);
			}
			val = row.get(2);
			if(val != null && "".equals(val) == false) {
				b.setSubject(val);
			}
			val = row.get(3);
			if(val != null && "".equals(val) == false) {
				b.setTn(val);
			}
			val = row.get(4);
			if(val != null && "".equals(val) == false) {
				b.setCallNumber(val);
			}
			val = row.get(5);
			if(val != null && "".equals(val) == false) {
				b.setPartnerLibCallNumber(val);
			}
			val = row.get(6);
			if(val != null && "".equals(val) == false) {
				b.setFilmno(val);
			}
			val = row.get(7);
			if(val != null && "".equals(val) == false) {
				b.setPagesPhysicalDescription(val);
			}
			val = row.get(8);
			if(val != null && "".equals(val) == false) {
				b.setSummary(val);
			}
			val = row.get(9);
			if(val != null && "".equals(val) == false) {
				b.setDgsno(val);
			}
			val = row.get(10);
			if(val != null && "".equals(val) == false) {
				b.setLanguage(val);
			}
			val = row.get(11);
			if(val != null && "".equals(val) == false) {
				b.setOwningInstitution(val);
			} 
			val = row.get(12);
			if(val != null && "".equals(val) == false) {
				b.setRequestingLocation(val);
			} 
			val = row.get(13);
			if(val != null && "".equals(val) == false) {
				b.setScannedBy(val);
			} 
			val = row.get(14);
			if(val != null && "".equals(val) == false) {
				b.setRecordNumber(val);
			} 
			val = row.get(15);
			if(val != null && "".equals(val) == false) {
			    SqlTimestampPropertyEditor tsConvert = new SqlTimestampPropertyEditor(SqlTimestampPropertyEditor.BATCH_PATTERN2);
				b.setDateOriginal(tsConvert.textToTimestamp(val));
			} 
			val = row.get(16);
			if(val != null && "".equals(val) == false) {
				b.setPublisherOriginal(val);
			} 
			val = row.get(17);
			if(val != null && "".equals(val) == false) {
				b.setFilename(val);
			} 
		 
			updateBook(b);
		}

	}


	@Override
	public void deleteReceivedImagesEntry() {
		String sql = "DELETE FROM TF_Received_Images_Entry";
	    getJdbcTemplate().update(sql);
	}
	
	@Override
	public void updateReceivedImages() {
		//this moves notes from TF_Received_Images_entry table to Book table
		String sql = "UPDATE (select a.Remarks_from_scan_center old, b.Notes_from_Site new from book a, TF_Received_Images_entry b  where a.tn = b.tn)   set  old = new ";
		getJdbcTemplate().update(sql);
	}
	
	@Override
	public void deleteTiffArchivingCopy1Entry() {
		String sql = "DELETE FROM TIFF_ARCHIVING_COPY1_ENTRY";
	    getJdbcTemplate().update(sql);
	}
	
	@Override
	public void updateTiffArchivingCopy1() {
		//this moves notes from x table to Book table
		String sql = "UPDATE (select a.TIFF_OREM_ARCHIVED_DATE old1, a.TIFF_OREM_DRIVE_SERIAL_# old2, a.TIFF_OREM_DRIVE_NAME old3, b.TIFF_OREM_ARCHIVED_DATE new1, b.TIFF_OREM_SERIAL_# new2, b.TIFF_OREM_DRIVE_NAME new3 from book a, TIFF_ARCHIVING_COPY1_ENTRY b  where a.tn = b.tn)   set  old1 = new1,  old2 = new2,  old3 = new3  ";
		 
		getJdbcTemplate().update(sql);
	}
	 
	@Override
	public void deletePdfArchivingCopy1Entry() {
		String sql = "DELETE FROM PDF_ARCHIVING_COPY1_ENTRY";
	    getJdbcTemplate().update(sql);
	}
	
	@Override
	public void updatePdfArchivingCopy1() {
		//this moves notes from x table to Book table
		String sql = "UPDATE (select a.PDF_OREM_ARCHIVED_DATE old1, a.PDF_OREM_DRIVE_SERIAL_# old2, a.PDF_OREM_DRIVE_NAME old3, b.PDF_OREM_ARCHIVED_DATE new1, b.PDF_OREM_SERIAL_# new2, b.PDF_OREM_DRIVE_NAME new3 from book a, PDF_ARCHIVING_COPY1_ENTRY b  where a.tn = b.tn)   set  old1 = new1,  old2 = new2,  old3 = new3  ";
		 
		getJdbcTemplate().update(sql);
	}
	
	@Override
	public void deletePdfArchivingCopy2Entry() {
		String sql = "DELETE FROM PDF_ARCHIVING_COPY2_ENTRY";
	    getJdbcTemplate().update(sql);
	}
	
	@Override
	public void updatePdfArchivingCopy2() {
		//this moves notes from x table to Book table
		String sql = "UPDATE (select a.PDF_COPY2_ARCHIVED_DATE old1, a.PDF_COPY2_DRIVE_SERIAL_# old2, a.PDF_COPY2_DRIVE_NAME old3, b.PDF_COPY2_ARCHIVED_DATE new1, b.PDF_COPY2_SERIAL_# new2, b.PDF_COPY2_DRIVE_NAME new3 from book a, PDF_ARCHIVING_COPY2_ENTRY b  where a.tn = b.tn)   set  old1 = new1,  old2 = new2,  old3 = new3  ";
		 
		getJdbcTemplate().update(sql);
	}
	
	@Override
	public void updateLoadingEntry() {
		//this moves notes from x table to Book table
		String sql = "UPDATE (select a.Collection old1, a.date_loaded old2, a.loaded_by old3, a.pages_online old4, a.url old5, a.pid old6,  b.collection new1, b.date_loaded new2, b.Loaded_by new3, b.pages_online new4, b.url new5, b.pid new6 from book a, TF_Loading_entry b  where a.tn = b.tn)   set  old1 = new1,  old2 = new2,  old3 = new3,  old4 = new4,  old5 = new5,  old6 = new6  ";
				
	    getJdbcTemplate().update(sql);
	}
	@Override
	public void deleteLoadingEntry() {
		String sql = "DELETE FROM TF_Loading_entry";
	    getJdbcTemplate().update(sql);
	}
	
	@Override
	public void updateReleaseEntry() {
		//this moves notes from x table to Book table
		String sql = "UPDATE (select a.date_released old1,  b.date_released new1 from book a, TF_Released_entry b  where a.tn = b.tn)  set  old1 = new1  ";
				
	    getJdbcTemplate().update(sql);
	}
	
	@Override
	public void deleteReleaseEntry() {
		String sql = "DELETE FROM TF_Released_entry";
	    getJdbcTemplate().update(sql);
	} 
	

	@Override
	public List<String> parseExcelDataCol1(String tnData) {
		List<String> l = new ArrayList<String>();
		List<List<String>> listListStr = parseExcelData(tnData, 1);//changed to 1 since Jeri was getting some strange behavior on some copy/pastes
		for(List<String> listStr : listListStr) {
			l.add(listStr.get(0));
		}
		return l;
	}
	
	@Override
	public List<List<String>>  parseExcelData(String tnData, int colCount) {
		//for some reason some excel rows (always seems to be an ending row if it happens) in Chrome have missing /r/n at end...
		if(tnData.endsWith("\r\n") == false)
			tnData += "\r\n";
		
		List<List<String>> rows = new ArrayList<List<String>>();
		List<String> row = new ArrayList<String>();
		
		StringTokenizer data = new StringTokenizer(tnData, "\t\r\n", true);
		 
		boolean wasNull = true;
		String token = null;
		int cIndex = 0;
		while(data.hasMoreTokens()) {
			token = data.nextToken();
			
			if(token.equals("\t")) {
				if(wasNull == true) {
					row.add(null);
					cIndex++;
				}
				wasNull = true;
				continue; //go to next column
			}else if(token.equals("\r")) {
				data.nextToken(); //skip \n char
				if(wasNull == true) {
					row.add(null);
					cIndex++;
				}
				while(cIndex < colCount && colCount != -1) {
					//add extra null at end due to excel bug of sometimes leaving out /t   when in front of /r/n
					row.add(null);
					cIndex++;
				}
				rows.add(row);
				cIndex = 0;
				row = new ArrayList<String>();
				wasNull = true;
			}else if(token.equals("\n")) {
				//should not happen
			}else {
				//normal text value
				
				if(token.startsWith("\"") && !token.endsWith("\"") && !token.substring(1).contains("\"")) {
					//any text that starts with a quote and does not ahve an ending quote could be a possible problem case
					//excel places quotes around text that contains special chars (ie tab which is the cell separator)
					//first check for matching quote not at the end - case of just normal quotes in data
					//(one future case could be abc"def<tab>ghi"jkl  (if we get titles with quotes and tabs, then may need to tell them no tabs allowed)
					//assume text like "abc"def   (start quote and quote in middle  ie no tab in text) -  !token.substring(1).contains("\"")
					
					token = token.substring(1, token.length());//skip starting quote
					
					while(true) {
						String token2 = data.nextToken();
						if(token2.endsWith("\"")) {
							token2 = token2.substring(0, token2.length()-1);//skip ending quote
							token = token + token2;//include special chars in text
							break;
						}
						token = token + token2;//include special chars in text
					}						
				}
				wasNull = false;
				row.add(token);
				cIndex++;
			}
		}
		if(token.equals("\r") == false){
			//last line ended without \r\n
			if(cIndex < colCount && colCount != -1) {
				//add extra null at end due to excel bug of sometimes leaving out /t   when in front of /r/n
				row.add(null);
				cIndex++;
			}
			rows.add(row);
			cIndex = 0;
		}
		return rows;
	}
	
	@Override
	public void updateBook(Book book) {
		updateBook(book, book.getTn());
	}
	
	@Override
	public void updateBook(Book book, String oldTn) {
		// TODO add some data validation and display nice error message (ie dup tn and secondaryIdentifier
		
		//check for problems
		boolean hasProblems = false;
		String newTempTn = "";
		if(oldTn.equals(book.getTn()) == false) {
			List<String> problems = getBookProblemPns(oldTn);
			if(problems != null || problems.size() > 0) {
				hasProblems = true;
				newTempTn = book.getTn() + "_newtemp";
				String sql = "INSERT into book (tn) values ('" + newTempTn + "')";
			    getJdbcTemplate().update(sql);
				
			    sql = "UPDATE tf_notes set tn = '" + newTempTn + "' where tn = '" + oldTn +"'";
			    getJdbcTemplate().update(sql);
				
			}
		}
		
		
		String sql = "update book set ";
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		//tn will always updated even if not change to simplify sql generation		
		sql += "tn = :tn, ";
		params.put("tn", book.getTn());
		
		if (book.isSecondaryIdentifierSet()) {
			sql += "secondary_identifier =  :secondaryIdentifier, ";
			params.put("secondaryIdentifier", book.getSecondaryIdentifier());
		}
		if (book.isOclcNumberSet()) {
			sql += "OCLC_NUMBER =  :oclcNumber, ";
			params.put("oclcNumber", book.getOclcNumber());
		}
		if (book.isIsbnIssnSet()) {
			sql += "ISBN_ISSN =  :isbnIssn, ";
			params.put("isbnIssn", book.getIsbnIssn());
		}
		if (book.isTitleSet()) {
			sql += "title =  :title, ";
			params.put("title", book.getTitle());
		}
		if (book.isAuthorSet()) {
			sql += "author = :author, ";
			params.put("author", book.getAuthor());
		}
		if (book.isPropertyRightSet()) {
			sql += "property_right = :property_right, ";
			params.put("property_right", book.getPropertyRight()==""?null:book.getPropertyRight());  //dropdown select "" change to null
		}
		if (book.isPublicationTypeSet()) {
			sql += "publication_type = :publication_type, ";
			params.put("publication_type", book.getPublicationType()==""?null:book.getPublicationType());  //dropdown select "" change to null
		}
		if (book.isFilenameSet()) {
			sql += "filename = :filename, ";
			params.put("filename", book.getFilename()==""?null:book.getFilename());  //dropdown select "" change to null
		}
		
		if (book.isScanningSiteNotesSet()) {
			sql += "scanning_Site_Notes = :scanningSiteNotes, ";
			params.put("scanningSiteNotes", book.getScanningSiteNotes());
		}
		if (book.isCallNumberSet()) {
			sql += "call_# = :callNumber, ";
			params.put("callNumber", book.getCallNumber());
		}
		if (book.isPartnerLibCallNumberSet()) {
			sql += "partner_lib_call_# = :partnerLibCallNumber, ";
			params.put("partnerLibCallNumber", book.getPartnerLibCallNumber());
		}
		if (book.isPriorityItemSet()) {
			sql += "priority_Item = :priorityItem, ";
			params.put("priorityItem", book.getPriorityItem());
		}
		if (book.isWithdrawnSet()) {
			sql += "withdrawn = :withdrawn, ";
			params.put("withdrawn", book.getWithdrawn());
		}
		if (book.isDigitalCopyOnlySet()) {
			sql += "digital_Copy_Only = :digitalCopyOnly, ";
			params.put("digitalCopyOnly", book.getDigitalCopyOnly());
		}
		if (book.isMediaTypeSet()) {
			sql += "media_Type = :mediaType, ";
			params.put("mediaType", book.getMediaType());
		}
		if (book.isMetaDataCompleteSet()) {
			sql += "metadata_Complete = :metadataComplete, ";
			params.put("metadataComplete", book.getMetadataComplete());
		}
		if (book.isBatchClassSet()) {
			sql += "batch_Class = :batchClass, ";
			params.put("batchClass", book.getBatchClass());
		}
		if (book.isLanguageSet()) {
			sql += "language = :language, ";
			params.put("language", book.getLanguage()==""?null:book.getLanguage());  //dropdown select "" change to null
			
		}
		if (book.isRemarksFromScanCenterSet()) {
			sql += "remarks_From_Scan_Center = :remarksFromScanCenter, ";
			params.put("remarksFromScanCenter", book.getRemarksFromScanCenter());
		}
		if (book.isRemarksAboutBookSet()) {
			sql += "remarks_About_Book = :remarksAboutBook, ";
			params.put("remarksAboutBook", book.getRemarksAboutBook());
		}
		if (book.isRequestingLocationSet()) {
			sql += "requesting_Location = :requestingLocation, ";
			params.put("requestingLocation", book.getRequestingLocation()==""?null:book.getRequestingLocation());  //dropdown select "" change to null
		}
		if (book.isRecordNumberSet()) {
			sql += "record_number = :recordNumber, ";
			params.put("recordNumber", book.getRecordNumber());
		}
		if (book.isOwningInstitutionSet()) {
			sql += "owning_institution = :owningInstitution, ";
			params.put("owningInstitution", book.getOwningInstitution()==""?null:book.getOwningInstitution());  //dropdown select "" change to null
		}
		if (book.isScannedBySet()) {
			sql += "scanned_By = :scannedBy, ";
			params.put("scannedBy", book.getScannedBy()==""?null:book.getScannedBy());  //dropdown select "" change to null
		}
		if (book.isScanOperatorSet()) {
			sql += "scan_Operator = :scanOperator, ";
			params.put("scanOperator", book.getScanOperator());
		}
		if (book.isScanMachineIdSet()) {
			sql += "scan_Machine_Id = :scanMachineId, ";
			params.put("scanMachineId", book.getScanMachineId());
		}
		if (book.isScanMetadataCompleteSet()) {
			sql += "scan_Metadata_Complete = :scanMetadataComplete, ";
			params.put("scanMetadataComplete", book.getScanMetadataComplete());
		}
		if (book.isLocationSet()) {
			sql += "location = :location, ";
			params.put("location", book.getLocation());
		}
		if (book.isScanStartDateSet()) {
			sql += "scan_Start_Date = :scanStartDate, ";
			params.put("scanStartDate", book.getScanStartDate());
		}
		
		if (book.isScanCompleteDateSet()) {
			sql += "scan_Complete_Date = :scanCompleteDate, ";
			params.put("scanCompleteDate", book.getScanCompleteDate());
		}
		if (book.isScanImageAuditorSet()) {
			sql += "scan_Image_Auditor = :scanImageAuditor, ";
			params.put("scanImageAuditor", book.getScanImageAuditor());
		}
		if (book.isScanIaStartDateSet()) {
			sql += "scan_Ia_Start_Date = :scanIaStartDate, ";
			params.put("scanIaStartDate", book.getScanIaStartDate());
		}
		if (book.isScanIaCompleteDateSet()) {
			sql += "scan_Ia_Complete_Date = :scanIaCompleteDate, ";
			params.put("scanIaCompleteDate", book.getScanIaCompleteDate());
		}
		if (book.isFilesSentToOremSet()) {
			sql += "files_Sent_To_Orem = :filesSentToOrem, ";
			params.put("filesSentToOrem", book.getFilesSentToOrem());
		}
		if (book.isScanNumOfPagesSet()) {
			sql += "scan_Num_Of_Pages = :scanNumOfPages, ";
			params.put("scanNumOfPages", book.getScanNumOfPages());
		}
		if (book.isNumOfPagesSet()) {
			sql += "num_Of_Pages = :numOfPages, ";
			params.put("numOfPages", book.getNumOfPages());
		}
		if (book.isFilesReceivedByOremSet()) {
			sql += "files_Received_By_Orem = :filesReceivedByOrem, ";
			params.put("filesReceivedByOrem", book.getFilesReceivedByOrem());
		}
		
		if (book.isImageAuditSet()) {
			sql += "image_Audit = :imageAudit, ";
			params.put("imageAudit", book.getImageAudit());
		}
		if (book.isIaStartDateSet()) {
			sql += "ia_Start_Date = :iaStartDate, ";
			params.put("iaStartDate", book.getIaStartDate());
		}
		if (book.isIaCompleteDateSet()) {
			sql += "ia_Complete_Date = :iaCompleteDate, ";
			params.put("iaCompleteDate", book.getIaCompleteDate());
		}
		if (book.isOcrBySet()) {
			sql += "OCR_by = :ocrBy, ";
			params.put("ocrBy", book.getOcrBy());
		}
		if (book.isOcrStartDateSet()) {
			sql += "OCR_start_date = :ocrStartDate, ";
			params.put("ocrStartDate", book.getOcrStartDate());
		}
		if (book.isOcrCompleteDateSet()) {
			sql += "OCR_complete_date = :ocrCompleteDate, ";
			params.put("ocrCompleteDate", book.getOcrCompleteDate());
		}
		if (book.isPdfDownloadBySet()) {
			sql += "pdf_download_by = :pdfDownloadBy, ";
			params.put("pdfDownloadBy", book.getPdfDownloadBy());
		}
		if (book.isPdfDownloadDateSet()) {
			sql += "pdf_download_date = :pdfDownloadDate, ";
			params.put("pdfDownloadDate", book.getPdfDownloadDate());
		}
		if (book.isPdfreviewBySet()) {
			sql += "Pdfreview_By = :pdfreviewBy, ";
			params.put("pdfreviewBy", book.getPdfreviewBy());
		}
		if (book.isPdfreviewStartDateSet()) {
			sql += "Pdfreview_Start_Date = :pdfreviewStartDate, ";
			params.put("pdfreviewStartDate", book.getPdfreviewStartDate());
		}
		if (book.isPdfReadySet()) {
			sql += "pdf_Ready = :pdfReady, ";
			params.put("pdfReady", book.getPdfReady());
		}
		if (book.isDateReleasedSet()) {
			sql += "date_Released = :dateReleased, ";
			params.put("dateReleased", book.getDateReleased());
		}
		if (book.isCompressionCodeSet()) {
			sql += "compression_Code = :compressionCode, ";
			params.put("compressionCode", book.getCompressionCode());
		}
		if (book.isLoadedBySet()) {
			sql += "loaded_By = :loadedBy, ";
			params.put("loadedBy", book.getLoadedBy());
		}
		if (book.isDateLoadedSet()) {
			sql += "date_Loaded = :dateLoaded, ";
			params.put("dateLoaded", book.getDateLoaded());
		}
		if (book.isCollectionSet()) {
			sql += "collection = :collection, ";
			params.put("collection", book.getCollection());
		}
		if (book.isDnpSet()) {
			sql += "dnp = :dnp, ";
			params.put("dnp", book.getDnp());
		}
		if (book.isDnpDeletedOffLineSet()) {
			sql += "dnp_Deleted_Off_Line = :dnpDeletedOffLine, ";
			params.put("dnpDeletedOffLine", book.getDnpDeletedOffLine());
		}
		if (book.isTnChangeHistorySet()) {
			sql += "tn_Change_History = :tnChangeHistory, ";
			params.put("tnChangeHistory", book.getTnChangeHistory());
		}
		if (book.isPdfOremArchivedDateSet()) {
			sql += "pdf_Orem_Archived_Date = :pdfOremArchivedDate, ";
			params.put("pdfOremArchivedDate", book.getPdfOremArchivedDate());
		}
		if (book.isPdfOremDriveSerialNumberSet()) {
			sql += "pdf_Orem_Drive_Serial_# = :pdfOremDriveSerialNumber, ";
			params.put("pdfOremDriveSerialNumber",
					book.getPdfOremDriveSerialNumber());
		}
		if (book.isPdfOremDriveNameSet()) {
			sql += "pdf_Orem_Drive_Name = :pdfOremDriveName, ";
			params.put("pdfOremDriveName", book.getPdfOremDriveName());
		}
		if (book.isPdfCopy2ArchivedDateSet()) {
			sql += "pdf_Copy2_Archived_Date = :pdfCopy2ArchivedDate, ";
			params.put("pdfCopy2ArchivedDate", book.getPdfCopy2ArchivedDate());
		}
		if (book.isPdfCopy2DriveSerialNumberSet()) {
			sql += "pdf_Copy2_Drive_Serial_# = :pdfCopy2DriveSerialNumber, ";
			params.put("pdfCopy2DriveSerialNumber",
					book.getPdfCopy2DriveSerialNumber());
		}
		if (book.isPdfCopy2DriveNameSet()) {
			sql += "pdf_Copy2_Drive_Name = :pdfCopy2DriveName, ";
			params.put("pdfCopy2DriveName", book.getPdfCopy2DriveName());
		}
		if (book.isTiffOremArchivedDateSet()) {
			sql += "tiff_Orem_Archived_Date = :tiffOremArchivedDate, ";
			params.put("tiffOremArchivedDate", book.getTiffOremArchivedDate());
		}
		if (book.isTiffOremDriveSerialNumberSet()) {
			sql += "tiff_Orem_Drive_Serial_# = :tiffOremDriveSerialNumber, ";
			params.put("tiffOremDriveSerialNumber",
					book.getTiffOremDriveSerialNumber());
		}
		if (book.isTiffOremDriveNameSet()) {
			sql += "tiff_Orem_Drive_Name = :tiffOremDriveName, ";
			params.put("tiffOremDriveName", book.getTiffOremDriveName());
		}
		if (book.isTiffCopy2ArchivedDateSet()) {
			sql += "tiff_Copy2_Archived_Date = :tiffCopy2ArchivedDate, ";
			params.put("tiffCopy2ArchivedDate", book.getTiffCopy2ArchivedDate());
		}
		if (book.isTiffCopy2DriveSerialNumberSet()) {
			sql += "tiff_Copy2_Drive_Serial_# = :tiffCopy2DriveSerialNumber, ";
			params.put("tiffCopy2DriveSerialNumber",
					book.getTiffCopy2DriveSerialNumber());
		}
		if (book.isTiffCopy2DriveNameSet()) {
			sql += "tiff_Copy2_Drive_Name = :tiffCopy2DriveName, ";
			params.put("tiffCopy2DriveName", book.getTiffCopy2DriveName());
		}
		if (book.isPdfSentToLoadSet()) {
			sql += "pdf_Sent_To_Load = :pdfSentToLoad, ";
			params.put("pdfSentToLoad", book.getPdfSentToLoad());
		}
		if (book.isSiteSet()) {
			sql += "site = :site, ";
			params.put("site", book.getSite()==""?null:book.getSite());  //dropdown select "" change to null
		}
		if (book.isUrlSet()) {
			sql += "url = :url, ";
			params.put("url", book.getUrl());
		}
		if (book.isPidSet()) {
			sql += "pid = :pid, ";
			params.put("pid", book.getPid());
		}
		if (book.isPagesOnlineSet()) {
			sql += "pages_Online = :pagesOnline, ";
			params.put("pagesOnline", book.getPagesOnline());
		}		
		if (book.isSubjectSet()) {
			sql += "subject = :subject, ";
			params.put("subject", book.getSubject());
		}
		if (book.isFilmnoSet()) {
			sql += "filmno = :filmno, ";
			params.put("filmno", book.getFilmno());
		}
		if (book.isPagesPhysicalDescriptionSet()) {
			sql += "pages_Physical_Description = :pagesPhysicalDescription, ";
			params.put("pagesPhysicalDescription", book.getPagesPhysicalDescription());
		}
		if (book.isSummarySet()) {
			sql += "summary = :summary, ";
			params.put("summary", book.getSummary());
		}
		if (book.isDgsnoSet()) {
			sql += "dgsno = :dgsno, ";
			params.put("dgsno", book.getDgsno());
		}
		if (book.isDateOriginalSet()) {
			sql += "date_Original = :dateOriginal, ";
			params.put("dateOriginal", book.getDateOriginal());
		}
		if (book.isPublisherOriginalSet()) {
			sql += "publisher_Original = :publisherOriginal, ";
			params.put("publisherOriginal", book.getPublisherOriginal());
		}
		if (book.isFhcTitleSet()) {
			sql += "fhc_title = :fhcTitle, ";
			params.put("fhcTitle", book.getFhcTitle());
		}
		if (book.isFhcTnSet()) {
			sql += "fhc_tn = :fhcTn, ";
			params.put("fhcTn", book.getFhcTn());
		}
		if (book.isDateRepublishedSet()) {
			sql += "date_republished = :dateRepublished, ";
			params.put("dateRepublished", book.getDateRepublished());
		}
		if (book.isPullDateSet()) {
			sql += "pull_date = :pullDate, ";
			params.put("pullDate", book.getPullDate());
		}
		

		String tn;
		if(oldTn != null)
			tn = oldTn;
		else
			tn = book.getTn();
		//remove final comma
		sql = sql.substring(0, sql.length() - 2) + " where tn = '" + tn + "'";
		
		 
		getNamedParameterJdbcTemplate().update(sql, params);
	 
		
		//check for problems
		if(oldTn.equals(book.getTn()) == false) {
			 
			if(hasProblems == true) {
				sql = "UPDATE tf_notes set tn = '" + book.getTn() + "' where tn = '" + newTempTn + "'";
				getJdbcTemplate().update(sql);
				    
				sql = "DELETE FROM book where tn = '" + newTempTn + "'";
			    getJdbcTemplate().update(sql);
				
			  
				
			}
		}
	}
	
	@Override
	public void deleteBook(String tn) {
		String sql = "DELETE FROM BOOK where tn = ?";
	    getJdbcTemplate().update(sql, tn);
	}
	
	@Override 
	public void updateBooksFilesReceived(String tnList){
		String sql = "UPDATE book SET FILES_RECEIVED_BY_OREM = CURRENT_TIMESTAMP where TN IN (" + tnList + ")";
	    getJdbcTemplate().update(sql);
	}
	
	@Override public void updateBooksSkipScan(String tnList){
		//similar logic also in trackingFormController of scan
		String sql = "UPDATE book SET files_sent_to_orem = CURRENT_TIMESTAMP where TN IN (" + tnList + ")";
	    getJdbcTemplate().update(sql);
	    
	   
	}
	

	@Override public void updateBooksSkipScanAndProcess(String tnList){
		//similar logic also in trackingFormController of scan
		String sql = "UPDATE book SET files_sent_to_orem = CURRENT_TIMESTAMP, date_released = CURRENT_TIMESTAMP where TN IN (" + tnList + ")";
	    getJdbcTemplate().update(sql);
	    
	}
	
	////user admin start////
	public List<String> getAllUserIds() {
		List<String> userIdList = getJdbcTemplate().query("select id from USERS", new StringRowMapper());	
		return userIdList;
	}
	
	@Override
	public void createUser(User user) {
		String sql = "insert into users ";
		String colList  = "";
		String valList = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		//always insert id
		colList += "id, ";
		valList += ":id, ";
		params.put("id", user.getUserId());
		
		if (user.isNameSet()) {
			colList += "name, ";
			valList += ":name, ";
			params.put("name", user.getName());  
		}

		if (user.isPrimaryLocationSet()) {
			colList += "primary_location, ";
			valList += ":primary_location, ";
			params.put("primary_location", user.getPrimaryLocation()==""?null:user.getPrimaryLocation());  //dropdown select "" change to null
		}

		if (user.isEntryPageSet()) {
			colList += "entry_page, ";
			valList += ":entry_page, ";
			params.put("entry_page", user.getEntryPage()==""?null:user.getEntryPage());  //dropdown select "" change to null
		}
		if (user.isEntryPageSet()) {
			colList += "email, ";
			valList += ":email, ";
			params.put("email", user.getEmail()==""?null:user.getEmail());
		}
		if (user.isSendScanNoticeSet()) {
			colList += "send_scan_notice, ";
			valList += ":send_scan_notice, ";
			params.put("send_scan_notice", user.getSendScanNotice()==""?null:user.getSendScanNotice()); 
		}
		
		//always insert dates
		colList += "date_Added, ";
		valList += "current_timestamp, ";
		//params.put("date_Added", "current_timestamp");
		
		colList += "date_Updated ";
		valList += "current_timestamp ";
		//params.put("date_Updated", "current_timestamp");
		
		sql += " (" + colList + ") values (" + valList + ")";
		getNamedParameterJdbcTemplate().update(sql, params);
		
		
		//just iterate on insert of authorities
		if(user.isAuthoritiesSet()) {
			for(String auth : user.getAuthorities()) {
				insertAuthority(user.getUserId(), auth);
			}
		}
	}
	@Override
	public void insertAuthority(String userId, String auth) {
		String sql = "insert into user_authorities values ( '" + userId + "', '" + auth +"' )";
	    getJdbcTemplate().update(sql);
	}
	@Override
	public void deleteAuthorities(String userId) {
		String sql = "DELETE FROM USER_authorities where lower(id) = '" + userId.toLowerCase() + "' ";
	    getJdbcTemplate().update(sql);
	}
	@Override
	public User getUser(String userId) {
		try {
			String lowerUser = "";
			if(userId != null)
				lowerUser = userId.toLowerCase();
			
			User u = getJdbcTemplate().queryForObject("select * from USERS where lower(ID)=?", new UserRowMapper(), lowerUser);
			List<String> authList = getJdbcTemplate().query("select role from USER_AUTHORITIES where lower(id) = ?", new StringRowMapper(), lowerUser);
			u.setAuthorities(authList);
			 
			return u;
		}catch(EmptyResultDataAccessException e) 
		{ 
			return new User(); //empty for backing bean
		}
	} 

	//get username to diplay on top of pages
	@Override
	public String getUserName(String userId, HttpSession session) {
		Object userObj = session.getAttribute("userName");
		String userName = "";
		if(userObj != null) {
			return (String) userObj;
		}
		
		String sql = "SELECT name FROM users where lower(id) = '" + userId.toLowerCase() + "'"; 
		List<String> name = getJdbcTemplate().query(sql, new StringRowMapper());
		if(name.size()>0) {
			session.setAttribute("userName", name.get(0));
			return name.get(0);
		} else {
			return "";
		}
	}

	@Override
	public void updateUser(User user) {
		updateUser(user, user.getUserId());
	}
	@Override
	public void updateUser(User user, String oldUserId) {
		String userId;
		if(oldUserId != null)
			userId = oldUserId;
		else
			userId = user.getUserId();
		
		deleteAuthorities(userId);//delete then re-insert all
		
		String sql = "update users set ";
		String valList = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		//always insert id
		 
		valList += "id = :id, ";
		params.put("id", user.getUserId());
		
		if (user.isNameSet()) {
			valList += "name = :name, ";
			params.put("name", user.getName()==""?null:user.getName());  //dropdown select "" change to null
		}

		if (user.isEntryPageSet()) {
			valList += "entry_page = :entry_page, ";
			params.put("entry_page", user.getEntryPage()==""?null:user.getEntryPage());  //dropdown select "" change to null
		}

		if (user.isEmailSet()) {
			valList += "email = :email, ";
			params.put("email", user.getEmail()==""?null:user.getEmail());  
		}

		if (user.isSendScanNoticeSet()) {
			valList += "send_scan_notice = :send_scan_notice, ";
			params.put("send_scan_notice", user.getSendScanNotice()==""?null:user.getSendScanNotice()); 
		}
		
		if (user.isPrimaryLocationSet()) {
			valList += "primary_location = :primary_location, ";
			params.put("primary_location", user.getPrimaryLocation()==""?null:user.getPrimaryLocation());  //dropdown select "" change to null
		}
		
		//always update date
		valList += "date_Updated = current_timestamp";
		
		
		  
		sql += valList + " where id = '" + userId + "'";
		try {
			getNamedParameterJdbcTemplate().update(sql, params);
		}catch(Exception e) {
			System.out.println(e); //allow to continue so auths get re-inserted
		}
		
		//just iterate on insert of authorities
		if(user.isAuthoritiesSet()) {
			for(String auth : user.getAuthorities()) {
				insertAuthority(user.getUserId(), auth);
			}
		}
	}
	
	@Override
	public void deleteUser(String userId) {
	 
 
		deleteAuthorities(userId);//delete then re-insert all
		
		String sql = "delete from users where id = ?";
	 
	 
		
	 
		try {
			getJdbcTemplate().update(sql, userId);
		 
		}catch(Exception e) {
			System.out.println(e); //allow to continue so auths get re-inserted
		}
		
	 
	}

	@Override
	public List<String> getAllAuthorities() {
		ArrayList<String> auths = new ArrayList<String>();
		List sList = getJdbcTemplate().query("select * from authority ", new StringRowMapper());
		return sList;
	}
	
	private static class UserRowMapper implements RowMapper<User> {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setUserId(rs.getString("ID"));
			user.setName(rs.getString("name"));
			user.setPrimaryLocation(rs.getString("primary_location"));
			user.setEntryPage(rs.getString("entry_page"));
			user.setEmail(rs.getString("email"));
			user.setSendScanNotice(rs.getString("send_scan_notice"));
			user.setLastLoggedIn(rs.getTimestamp("last_logged_in"));
			user.setDateAdded(rs.getTimestamp("date_added"));
			user.setDateUpdated(rs.getTimestamp("date_updated"));
			return user;
		}
	}

	////user admin end////

	////search start////
	@Override
	public List<String> getAllSearchIds() {
		List<String> idList = getJdbcTemplate().query("select id from search", new StringRowMapper());	
		return idList;
	}
	
	@Override
	public void createSearch(Search search) {
		String sql = "insert into search ";
		String colList  = "";
		String valList = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		//always insert id
		colList += "id, ";
		valList += ":id, ";
		params.put("id", search.getSearchId());
		
		if (search.isDescriptionSet()) {
			colList += "description, ";
			valList += ":description, ";
			params.put("description", search.getDescription());  
		}

		if (search.isQueryTextSet()) {
			colList += "query_text, ";
			valList += ":query_text, ";
			params.put("query_text", search.getQueryText());
		}

		if (search.isOwnerSet()) {
			colList += "owner, ";
			valList += ":owner, ";
			params.put("owner", search.getOwner());
		}
	
		colList += "date_Updated ";
		valList += "current_timestamp ";
		//params.put("date_Updated", "current_timestamp");
		
		sql += " (" + colList + ") values (" + valList + ")";
		getNamedParameterJdbcTemplate().update(sql, params);
		
	}
	 
	@Override
	public Search getSearch(String searchId) {
		try {
			Search s = getJdbcTemplate().queryForObject("select * from SEARCH where ID=?", new SearchRowMapper(), searchId);
			 
			return s;
		}catch(EmptyResultDataAccessException e) 
		{ 
			return new Search(); //empty for backing bean
		}
	} 


	@Override
	public void updateSearch(Search search) {
		updateSearch(search, search.getSearchId());
	}
	
	@Override
	public void updateSearch(Search search, String oldSearchId) {
		String searchId;
		if(oldSearchId != null)
			searchId = oldSearchId;
		else
			searchId = search.getSearchId();
		 
		String sql = "update search set ";
		String valList = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		//always insert id
		 
		valList += "id = :id, ";
		params.put("id", search.getSearchId());
		
		if (search.isDescriptionSet()) {
			valList += "description = :description, ";
			params.put("description", search.getDescription());
		}
		if (search.isQueryTextSet()) {
			valList += "query_text = :query_text, ";
			params.put("query_text", search.getQueryText());
		}
		if (search.isOwnerSet()) {
			valList += "owner = :owner, ";
			params.put("owner", search.getOwner());
		}
		 

		//always update date
		valList += "date_Updated = current_timestamp";
		 
		sql += valList + " where id = '" + searchId + "'";
		try {
			getNamedParameterJdbcTemplate().update(sql, params);
		}catch(Exception e) {
			System.out.println(e);  
		}
	}

	@Override
	public void deleteSearch(String searchId) {
		String sql = "DELETE FROM site  where id = '" + searchId + "'";
	    getJdbcTemplate().update(sql);
	} 
	
	private static class SearchRowMapper implements RowMapper<Search> {
		@Override
		public Search mapRow(ResultSet rs, int rowNum) throws SQLException {
			Search search = new Search();
			search.setSearchId(rs.getString("ID"));
			search.setDescription(rs.getString("description"));
			search.setQueryText(rs.getString("query_text"));
			search.setOwner(rs.getString("owner"));
			search.setDateUpdated(rs.getTimestamp("date_updated"));
			 
			return search;
		}
	}

	@Override 
	public List<List<String>> runQuery(String queryText){
		String lowerQueryText = queryText.toLowerCase();
		if(lowerQueryText.contains("update") || lowerQueryText.contains("insert") || lowerQueryText.contains("delete") || lowerQueryText.contains("alter")) {
			//allow many
			String[] commands = queryText.split(";end");

			for(int x = 0; x < commands.length; x++) {
				String command = commands[x];
				if(command.endsWith(";"))
					command = command.substring(0, command.length()-1);
				
				if(command.equals("\r\n") == false){
					getJdbcTemplate().update(command);
				}
			}
		}else {
			
			int ind = queryText.indexOf(";");
			if(ind != -1)
				queryText = queryText.substring(0, ind );
			
			List<List<String>> sList = getJdbcTemplate().query(queryText, new ExtractorWithColumnHeaders());
			
			return sList;
		}
		return null;
	}
	
	@Override 
	public String checkQuery(String queryText){
		String lowerQueryText = queryText.toLowerCase();
		for(String w: badWords) {
			if(lowerQueryText.indexOf(w) != -1)
				return w;
		}
		
		return null;
	}
	
	////search end////
	
	////language admin start////
	
	@Override 
	public List<List>  getAllLanguageIdsAsRows() {
		//method returns list of lists for common miscButtonAndTableFormWithCheckBox
		List sList = getJdbcTemplate().query("select id from languages", new StringXRowMapper());
		return sList;
	}
	@Override 
	public List<String>  getAllLanguageIds() {
		//method returns list of lists for common miscButtonAndTableFormWithCheckBox
		List sList = getJdbcTemplate().query("select id from languages", new StringRowMapper());
		return sList;
	}
	
	@Override 
	public void createLanguage(String id) {
		String pubName = id + ";" + id.toLowerCase().substring(0, 3) + ";" + id.toLowerCase().substring(0, 2);
		String sql = "INSERT into languages (id, publish_Name) values ( ?,?)";
		getJdbcTemplate().update(sql, id, pubName);
	    
	}
	
	@Override 
	public void deleteLanguages(String idList) {
		String sql = "DELETE from languages where id in ( " + idList + ")"; 
		getJdbcTemplate().update(sql);
	    
	}

	////language admin start////
	
	////site admin start////
	@Override 
	public List<String>  getAllSiteIdsIncludingInactive() {
		//method returns list of lists for common miscButtonAndTableFormWithCheckBox
		List sList = getJdbcTemplate().query("select id from SITE order by id", new StringRowMapper());
		return sList;
	}
	@Override 
	public List<String>  getAllSiteIds() {
		//method returns list of lists for common miscButtonAndTableFormWithCheckBox
		List sList = getJdbcTemplate().query("select id from SITE  where  ( is_inactive_site !='T' or is_inactive_site is null)  order by id", new StringRowMapper());
		return sList;
	}
	
	@Override 
	public void createSite(Site site) {
		 	    
		String sql = "insert into site ";
		String colList  = "";
		String valList = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		//always insert id
		colList += "id, ";
		valList += ":id, ";
		params.put("id", site.getSiteId());
		
		if (site.isContactSet()) {
			colList += "contact, ";
			valList += ":contact, ";
			params.put("contact", site.getContact());  
		}

		if (site.isNumberOfOperatorsSet()) {
			colList += "number_Of_Operators, ";
			valList += ":numberOfOperators, ";
			params.put("numberOfOperators", site.getNumberOfOperators());
		}

		if (site.isLocationSet()) {
			colList += "location, ";
			valList += ":location, ";
			params.put("location", site.getLocation());
		}

		if (site.isPublishNameSet()) {
			colList += "publish_name, ";
			valList += ":publishName, ";
			params.put("publishName", site.getPublishName());
		}
		
		if (site.isIsFhcSet()) {
			colList += "is_Fhc, ";
			valList += ":isFhc, ";
			params.put("isFhc", site.getIsFhc());
		}
		
		if (site.isIsPartnerInstitutionSet()) {
			colList += "is_Partner_Institution, ";
			valList += ":isPartnerInstitution, ";
			params.put("isPartnerInstitution", site.getIsPartnerInstitution());
		}
		if (site.isIsScanSiteSet()) {
			colList += "is_Scan_Site, ";
			valList += ":isScanSite, ";
			params.put("isScanSite", site.getIsScanSite());
		}

		if (site.isIsProcessSiteSet()) {
			colList += "is_Process_Site, ";
			valList += ":isProcessSite, ";
			params.put("isProcessSite", site.getIsProcessSite());
		}
		

		if (site.isIsPhysicalBookSiteSet()) {
			colList += "is_Physical_Book_Site, ";
			valList += ":isPhysicalBookSite, ";
			params.put("isPhysicalBookSite", site.getIsPhysicalBookSite());
		}
		
		//remove final comma
		valList = valList.substring(0, valList.length() - 2);
		colList = colList.substring(0, colList.length() - 2);
		
		sql += " (" + colList + ") values (" + valList + ")";
		getNamedParameterJdbcTemplate().update(sql, params);
		
		
		//set site goals
		if (site.isIsGoalsSet()) {	
			List<List<String>> goals = site.getGoals();
			for(List<String> goal : goals) {
				String year = goal.get(0);
				String imageCount = goal.get(1);
				sql = "insert into site_goal (site, year, goal_images_yearly) values(?, ?, ?) ";
				getJdbcTemplate().update(sql, site, year, imageCount);
			}
		}
	}
	

	
	@Override 
	public String updateSite(Site site, String oldId) {
		String id;
		if(oldId.equals(site.getSiteId()) == false) {
			//id was also changed
		 
			//first check for locked books
			List<String> lockedBooks= getJdbcTemplate().query("select a.tn from book_lock a, book b where a.tn = b.tn and (b.site = ? or  b.owning_institution = ? or b.requesting_location = ? or b.scanned_by = ?) ", new StringRowMapper(), oldId, oldId, oldId, oldId);
			if(lockedBooks.size() != 0) {
				return getMessageString("site.updateFailed") + generateQuotedListString(lockedBooks);
			}
			
			createSite(site);//with new id in site obj
		 
			
			//update all books, metadata, and user fields that have this site
			String newId = site.getSiteId();
		
			String sql = "update book set  Book.site = '"+newId+"' where site = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			sql = "update book set  Book.owning_institution = '"+newId+"' where owning_institution = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			sql = "update book set  Book.Requesting_Location = '"+newId+"' where Requesting_Location = '" + oldId +"'";
			getJdbcTemplate().update(sql);
		
			sql = "update book set  Book.Scanned_by = '"+newId+"' where Scanned_by = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			
			
			sql = "update iaBookmetadata set  iaBookmetadata.site = '"+newId+"' where site = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			sql = "update iaBookmetadata set  iaBookmetadata.owning_institution = '"+newId+"' where owning_institution = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			sql = "update iaBookmetadata set  iaBookmetadata.Requesting_Location = '"+newId+"' where Requesting_Location = '" + oldId +"'";
			getJdbcTemplate().update(sql);
		
			sql = "update iaBookmetadata set  iaBookmetadata.Scanned_by = '"+newId+"' where Scanned_by = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			
			
			sql = "update Bookmetadata set  Bookmetadata.scanning_location = '"+newId+"' where scanning_location  = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			sql = "update Bookmetadata set  Bookmetadata.Requesting_Location = '"+newId+"' where Requesting_Location = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			sql = "update Bookmetadata set  Bookmetadata.owning_institution = '"+newId+"' where owning_institution = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			
			
			sql = "update Bookmetadataupdate set  Bookmetadataupdate.scanning_location = '"+newId+"' where scanning_location  = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			sql = "update Bookmetadataupdate set  Bookmetadataupdate.Requesting_Location = '"+newId+"' where Requesting_Location = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			sql = "update Bookmetadataupdate set  Bookmetadataupdate.owning_institution = '"+newId+"' where owning_institution = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			
			sql = "update site_goal set  site_goal.site = '"+newId+"' where site = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			
			
			sql = "update users set  users.primary_Location = '"+newId+"' where primary_Location = '" + oldId +"'";
			getJdbcTemplate().update(sql);
			
			sql = "update tf_notes set solution_owner = '"+newId+"' where solution_owner = '" + oldId +"'";
			getJdbcTemplate().update(sql);
		
			deleteSite(oldId);
		}else {
			//id is not changed..normal update
			id = site.getSiteId();
			
			String sql = "update site set ";
			String valList = "";
			Map<String, Object> params = new HashMap<String, Object>();
			
			//always insert id
			 
			valList += "id = :id, ";
			params.put("id", site.getSiteId());
			
			if (site.isPublishNameSet()) {
				valList += "publish_name = :publishName, ";
				params.put("publishName", site.getPublishName());
			}
			if (site.isLocationSet()) {
				valList += "location = :location, ";
				params.put("location", site.getLocation());
			}
	
			if (site.isContactSet()) {
				valList += "contact = :contact, ";
				params.put("contact", site.getContact());
			}
			
			if (site.isNumberOfOperatorsSet()) {
				valList += "number_Of_Operators = :numberOfOperators, ";
				params.put("numberOfOperators", site.getNumberOfOperators());
			}
			if (site.isIsFhcSet()) {
				valList += "is_Fhc = :isFhc, ";
				params.put("isFhc", site.getIsFhc());
			}
	
			if (site.isIsPartnerInstitutionSet()) {
				valList += "is_Partner_Institution = :isPartnerInstitution, ";
				params.put("isPartnerInstitution", site.getIsPartnerInstitution());
			}
	
			if (site.isIsScanSiteSet()) {
				valList += "is_Scan_Site = :isScanSite, ";
				params.put("isScanSite", site.getIsScanSite());
			}
			 
			
			if (site.isIsProcessSiteSet()) {
				valList += "is_Process_Site = :isProcessSite, ";
				params.put("isProcessSite", site.getIsProcessSite());
			}
			 
			if (site.isIsInactiveSiteSet()) {
				valList += "is_inactive_Site = :isInactiveSite, ";
				params.put("isInactiveSite", site.getIsInactiveSite());
			}
			 		
			if (site.isIsPhysicalBookSiteSet()) {
				valList += "is_Physical_Book_Site = :is_Physical_Book_Site, ";
				params.put("isPhysicalBookSite", site.getIsPhysicalBookSite());
			}
			
			//remove final comma
			valList = valList.substring(0, valList.length() - 2);
			
			sql += valList + " where id = '" + id + "'";
			try {
				getNamedParameterJdbcTemplate().update(sql, params);

				deleteSiteGoals(id);
				
				//set site goals
				if (site.isIsGoalsSet()) {	
					List<List<String>> goals = site.getGoals();
					for(List<String> goal : goals) {
						String year = goal.get(0);
						String imageCount = goal.get(1);
						sql = "insert into site_goal (site, year, goal_images_yearly) values(?, ?, ?) ";
						getJdbcTemplate().update(sql, site, year, imageCount);
					}
				}
			}catch(Exception e) {
				System.out.println(e);  
			}
		}
		return null;
	}
	
	 
	@Override 
	public void deleteSite(String id) {
		String sql = "DELETE FROM site  where id = '" + id + "'";
	    getJdbcTemplate().update(sql);
	}
	
	@Override 
	public Site getSite(String id) {
		try {
			return getJdbcTemplate().queryForObject("select * from site where id=?", new SiteRowMapper(), id);
		}catch(EmptyResultDataAccessException e) 
		{ 
			return new Site(); //empty for backing bean
		}
	}

	@Override 
	public String getListTNsUsingSite(String id) {
		/*
		update book set scanned_by = 'Larsen-Sant Public Library' where scanned_by = 'Larseon-Sant Public Library';end 
		update book set site  = 'Larsen-Sant Public Library' where site  = 'Larseon-Sant Public Library';end
		update book set owning_institution =  'Larsen-Sant Public Library' where owning_institution = 'Larseon-Sant Public Library';end
		update book set Requesting_Location =  'Larsen-Sant Public Library'  where Requesting_Location = 'Larseon-Sant Public Library';end
		update bookMETADATA  set owning_institution = 'Larsen-Sant Public Library'  where owning_institution = 'Larseon-Sant Public Library';end
		update bookMETADATA  set Requesting_Location = 'Larsen-Sant Public Library'  where Requesting_Location = 'Larseon-Sant Public Library';end
		update bookMETADATA  set scanning_Location = 'Larsen-Sant Public Library'  where scanning_Location = 'Larseon-Sant Public Library';end
		update users set primary_location = 'Larsen-Sant Public Library' where  primary_location = 'Larseon-Sant Public Library'
		*/
		List<String> tnList= getJdbcTemplate().query("select tn from book a where site = ? or  owning_institution = ? or requesting_location = ? or scanned_by = ? ", new StringRowMapper(), id, id, id, id);
	 
		String tnListStr = generateQuotedListString(tnList);
		return tnListStr;
	}
	 
	@Override 
	public String getListProblemsUsingSite(String id) {
		
		List<String> tnList= getJdbcTemplate().query("select tn from tf_notes a where solution_owner = ? ", new StringRowMapper(), id);
	 
		String tnListStr = generateQuotedListString(tnList);
		return tnListStr;
	}
	
	
	@Override
	public String getListMetadataUsingSite(String id) {
		
		List<String> tnMetadataList= getJdbcTemplate().query("select titleno from bookMETADATA  where   owning_institution = ? or requesting_location = ? or scanning_location = ? ", new StringRowMapper(),  id, id, id);
		String tnMetadataListStr = generateQuotedListString(tnMetadataList);
		return tnMetadataListStr;
	}

	@Override
	public String getListUsersUsingSite(String id) {
		
		List<String> userList= getJdbcTemplate().query("select id from users where primary_location = ? ", new StringRowMapper(), id);
		String userListStr = generateQuotedListString(userList);
		return userListStr;
	}

	
	private static class SiteRowMapper implements RowMapper<Site> {
		@Override
		public Site mapRow(ResultSet rs, int rowNum) throws SQLException {
			Site site = new Site();
			site.setSiteId(rs.getString("id"));
			site.setLocation(rs.getString("location"));
			site.setPublishName(rs.getString("publish_Name"));
			site.setContact(rs.getString("contact"));
			site.setNumberOfOperators(rs.getString("number_Of_Operators"));
			site.setIsFhc(rs.getString("is_fhc"));
			site.setIsPartnerInstitution(rs.getString("is_partner_institution"));
			site.setIsScanSite(rs.getString("is_scan_site"));
			site.setIsProcessSite(rs.getString("is_process_site"));
			site.setIsInactiveSite(rs.getString("is_inactive_site"));
			site.setIsPhysicalBookSite(rs.getString("is_physical_book_site"));
			return site;
		}
	}
	
	@Override
	public List<List> getSiteGoals(String site) {
		List list;
		if(site == null || site.equals("")) {
			list = getJdbcTemplate().query("select site, year, goal_images_yearly from SITE_GOAL ", new StringX3RowMapper());
		}else {
			list = getJdbcTemplate().query("select site, year, goal_images_yearly from SITE_GOAL where site = ? ", new StringX3RowMapper(), site);
		}
			
		return list;
	}
	
	@Override
	public void deleteSiteGoals(String id) {
		String sql = "DELETE FROM site_goal where lower(site) = '" + id.toLowerCase() + "' ";
	    getJdbcTemplate().update(sql);
	}
	
	@Override 
	public String getDuplicateSiteYearGoals(String siteList){

		List<List> dupeList =   getJdbcTemplate().query("select site, year from site_goal where " + siteList, new StringX2RowMapper() );
		if(dupeList.size()==0)
			return "";
		String dupeSiteList = "";
		for(List<String> r : dupeList) {
			dupeSiteList += "or (site = '" + r.get(0) + "' and year = '" + r.get(1) + "') ";
		}
		dupeSiteList = dupeSiteList.substring(2);
		
		return dupeSiteList;
	}
	

	@Override 
	public void deleteSelectedSiteGoals(String dupSiteYearList) {
		String sql = "DELETE FROM site_goal  where " + dupSiteYearList;
	    getJdbcTemplate().update(sql);
	    
	}
	
	////site admin end////
	
	////elder kern metadata admin start////

	
	@Override 
	public List<List>  getAllNtfBlankWoPrefix() {
		//method returns list of lists for common miscButtonAndTableFormWithCheckBox
		List sList = getJdbcTemplate().query("select w_o_prefix from tblNtf_Blank", new StringX1RowMapper());
		return sList;
	}
	
	@Override
	public String getDuplicateTnsInTblNtfBlank(String tnList) {
		List<String> tnDupeList = (List<String>) getJdbcTemplate().query("select w_o_prefix from tblNtf_blank where w_o_prefix in ( " + tnList +" )", new StringRowMapper() );
		String tnListStr = "";
		for(String tn: tnDupeList) {
			tnListStr += ", '" + tn + "'";
		}
		if(tnListStr != "")
			tnListStr = tnListStr.substring(2);
		return tnListStr;
	}
	
	@Override
	public int getMaxIdTblWoPrefix() {
		List sList = getJdbcTemplate().query("select max(id) from tblNtf_Blank ", new StringRowMapper());
		if(sList.size()>=1) {
			String val = (String) sList.get(0);
			return Integer.valueOf(val);
		}
		return 1;
	}

	@Override
	public void deleteTblWoPrefixRows(String list) {
		String sql = "DELETE FROM tblNtf_Blank  where  w_o_prefix in ( " +  list + " ) ";
	    getJdbcTemplate().update(sql);
	}
	
	@Override 
	public List<List>  getNtfBlankAndBook() {
		//method returns list of join of ntfbland and book
		List sList = getJdbcTemplate().query("SELECT tblNTF_Blank.w_o_prefix, Book.TN, Book.Title, Book.Author, Book.Batch_Class, Book.Image_Audit, Book.PDF_Ready, Book.Tiff_Orem_Drive_Name, Book.URL "
				+ " FROM tblNTF_Blank LEFT JOIN Book ON tblNTF_Blank.w_o_prefix = Book.TN", new StringX9RowMapper());
		return sList;
	}
	
	@Override 
	public List<List>  getNtfBlankAndMetadata() {
		//method returns list of join of ntfbland and metadata
		List sList = getJdbcTemplate().query("SELECT tblNTF_Blank.w_o_prefix, bookMetadata.title, bookMetadata.author, bookMetadata.subject, bookMetadata.titleno, bookMetadata.callno, bookMetadata.partner_lib_callno, bookMetadata.filmno, bookMetadata.pages, bookMetadata.summary, bookMetadata.dgsno, bookMetadata.language, bookMetadata.requesting_location, bookMetadata.scanning_location, bookMetadata.record_number, bookMetadata.date_original, bookMetadata.publisher_original, tblNTF_Blank.Full_TN, tblNTF_Blank.Date_scanned, tblNTF_Blank.pages, tblNTF_Blank.w_o_extension, tblNTF_Blank.Site, tblNTF_Blank.Drive "
				+ " FROM tblNTF_Blank LEFT JOIN bookMetadata ON tblNTF_Blank.w_o_prefix = bookMetadata.titleno", new StringXRowMapper());
		return sList;
	}
	
	@Override 
	public void process02TiffsData() {
		Properties p = new Properties();
		String tiffDir = "";
		try {
			InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("messages.properties");
			p.load(propStream);
			tiffDir = p.getProperty("file.02TiffDir");
			
		}catch(Exception e) {
			tiffDir = "";
		}
		
		File parentDir = new File(tiffDir);
		File[] dirs = parentDir.listFiles();
		if(dirs == null)
			dirs = new File[0];
		
		List<List<String>> insertData = new ArrayList();
		
		for(int x = 0; x< dirs.length; x++) {
			if(dirs[x].isDirectory()) {
				String tn = "";
				String folder = dirs[x].getName();
				if(folder.startsWith("!") == false) {
					Date d = new Date(dirs[x].lastModified());
					int i=0;
					for(i=0; i<12; i++) {
						if(folder.length() > i) {
							char ch = folder.charAt(i);
							if(ch >= '0' && ch <= '9'){
								tn = folder.substring(i);
								break;
							}
						}
					}
					if(tn!= "") {
						List<String> row = new ArrayList();
						row.add(folder);
						row.add(tn);
						row.add(d.toString());
						insertData.add(row);
					}
				}
			}
		}
		
		String sql = "DELETE FROM tblRoS_02Tif";
	    getJdbcTemplate().update(sql);
	    
	    insertBatch("tblRoS_02Tif", new String[]{"folder", "tn", "created"}, new int[] {Types.VARCHAR,  Types.VARCHAR, Types.TIMESTAMP}, insertData); 
	}
	
	@Override 
	public List<List> get02TiffsNotInTrackingForm() {
		
		List sList = getJdbcTemplate().query("SELECT tblRoS_02Tif.TN, tblRoS_02Tif.Folder, Book.TN, Book.Title "
				+ " FROM Book RIGHT JOIN tblRoS_02Tif ON Book.TN = tblRoS_02Tif.TN "
				+ " WHERE Book.TN Is Null", new StringX4RowMapper());
		return sList;
	}

	@Override 
	public List<List> getComparedSearchBackup() {

		List sList = getJdbcTemplate().query("SELECT tblNTF_Blank.w_o_prefix, tblSearchBackups.EHDFolder, tblSearchBackups.TN, tblSearchBackups.Drive_Name, tblSearchBackups.ParentFolder "
				+ " FROM tblNTF_Blank LEFT JOIN tblSearchBackups ON tblNTF_Blank.w_o_prefix = tblSearchBackups.TN", new StringX5RowMapper());
				return sList;
	}
	@Override 
	public List<List> getListToNullTitleCheckPlusReview() {

		List sList = getJdbcTemplate().query("SELECT tblNTF_Blank.w_o_prefix, Book.TN, Book.FILES_RECEIVED_BY_OREM, Book.Image_Audit, Book.IA_Start_Date, Book.IA_Complete_Date, "
			+ " Book.OCR_by, Book.OCR_complete_date, Book.Pdfreview_by, Book.Pdfreview_Start_date, Book.PDF_Ready "
			+ " FROM tblNTF_Blank LEFT JOIN Book ON tblNTF_Blank.w_o_prefix = Book.TN", new StringX11RowMapper());
				return sList;
	}
	

	@Override
	public void updateListToNullTitleCheck( ) {
		String sql = "update book set  Book.Image_Audit = Null, Book.IA_Start_Date = Null, Book.IA_Complete_Date = Null, Book.OCR_by = Null, "
			+ " Book.OCR_complete_date = Null, Book.Pdfreview_by = Null, Book.Pdfreview_Start_date = Null, Book.PDF_Ready = Null  " 
			 + " where tn in (select tblNTF_Blank.w_o_prefix from tblNTF_Blank)";
	    getJdbcTemplate().update(sql);
	}
	 
	@Override
	public List<String> getAllDriveNames() {

		List sList = getJdbcTemplate().query("SELECT tblSearchBackups.Drive_Name FROM tblSearchBackups GROUP BY tblSearchBackups.Drive_Name "
					+ " HAVING tblSearchBackups.Drive_Name Like 'Tiffs%'", new StringRowMapper());
		return sList;		
	}
	
	@Override
	public List<List> getQueryReprocess(String drive) {
		//this method is now not used since the copy is done by Dustin externally
		//"qryReprocess"
		List sList = getJdbcTemplate().query("SELECT  TN,  EHDFolder,  ParentFolder,  Drive_Name, " 
				+ "  Files,  w_o_prefix FROM qryReprocess where Drive_Name = '" + drive + "' " 
				+ " ORDER BY TN", new StringX6RowMapper());
		return sList;
	}
	
	@Override 
	public void doCopy(List<List> rowsQryReprocess, String drive, String driveLetter) {
		//this method is now not used since the copy is done by Dustin externally
		Properties p = new Properties();
		String tiffDir = "";
		try {
			InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("messages.properties");
			p.load(propStream);
			tiffDir = p.getProperty("file.02TiffDir");
			
		}catch(Exception e) {
			tiffDir = "";
		}
		
		
		//qryTN_Not_on_BackupDrive
		List<List> rowsTnNotOnBackup = getJdbcTemplate().query("SELECT tblNTF_Blank.w_o_prefix, qryReprocess.TN "
					+ " FROM tblNTF_Blank LEFT JOIN qryReprocess ON tblNTF_Blank.w_o_prefix = qryReprocess.TN "
					+ " WHERE qryReprocess.TN Is Null and Drive_Name = '" + drive + "'  ORDER BY tblNTF_Blank.w_o_prefix", new StringX2RowMapper());

		if(rowsTnNotOnBackup.size() == 0)
		{
			//todo return msg "All these TNs are on this backup drive."
		}else {
			//todo return to user list of these tns which are not yet backedup
		}
		
		
		//qryReprocess
		List<List> rowsReprocess = rowsQryReprocess;

		if(rowsReprocess.size() == 0)
		{
			//todo return msg "Please double check.  No TNs show-up on this Backup (Scraped) table. "
			//todo end program
		}else {
			//todo return to user "There are " & rst.RecordCount & " record(s) that will be processed."
			
			 // Call Copy_02Tif_Folder(strBackupDriveLetter & Mid(rst!ParentFolder, 2) & "\" & rst!EHDFolder, rst!EHDFolder, tiffDir)
			
			for(List row: rowsReprocess) {
				String EHDFolder =  (String) row.get(1);
				String parentFolder = (String) row.get(2);
				parentFolder = driveLetter + parentFolder.substring(1) + "/" + EHDFolder;
			
				copy_02Tif_Folder(parentFolder, EHDFolder, tiffDir);
			}
		}
		
	}
	
	public void copy_02Tif_Folder(String EHDPath, String EHDFolder, String Tiff02Path) {
		String fromPath = EHDPath;
		String toPath = Tiff02Path + "/" + EHDFolder;
		
		File fromDir = new File(fromPath);
		File toDir = new File(toPath);
		
		if(toDir.exists()) {
			//todo return to user "destination path already exists" and exit
		}
		if(fromDir.exists()==false) {
			//todo return to user "sourse path does not exists" and exit
		}
	
		FileUtils fUtil = new FileUtils();
		try {
			//fUtil.copyDirectory(fromDir, toDir);
			System.out.println(fromDir + "      "+toDir);
		}catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
			//todo return exception to user
		}
	}
	
	////elder kern metadata admin end////
	
	////problems start////
	@Override 
	public List<String>  getAllStatuses() {
		List sList = getJdbcTemplate().query("select * from problem_status ", new StringRowMapper());
		return sList;
	}	
	
	@Override 
	public List<List> getAllProblemReasons(){
		List sList = getJdbcTemplate().query("select id from problemreason ", new StringX1RowMapper());
		return sList;
	}	
	@Override 
	public List<String> getAllProblemReasons2(){
		List sList = getJdbcTemplate().query("select id from problemreason ", new StringRowMapper());
		return sList;
	}	
	
	@Override 
	public void deleteProblemReasons(String list) {
		String sql = "DELETE FROM problemReason  where id in ( " +  list + " ) ";
	    getJdbcTemplate().update(sql);
	}
	
	public List<String>  getBookProblemPns(String tn) {
		//method returns list of problem numbers (ids)
		List sList = getJdbcTemplate().query("select id from tf_notes where tn = ?", new StringRowMapper(), tn);
		return sList;
	}
	
	@Override 
	public List<List>  getBookProblems(String tn) {
		//method returns list of problems
		List sList = getJdbcTemplate().query("select id, tn, status, problem_text, problem_date, problem_initials, solution_text, solution_date, solution_initials from tf_notes where tn = ?", new StringX9RowMapper(), tn);
		return sList;
	}

	@Override 
	public List<List>  getBookOpenProblems(String tn) {
		//method returns list of problems
		List sList = getJdbcTemplate().query("select id, tn, status, problem_text, problem_date, problem_initials, solution_text, solution_date, solution_initials from tf_notes where tn = ? and ( status not in ('Notes', 'Problem Fixed') and status is not null)", new StringX9RowMapper(), tn);
		return sList;
	}

	@Override 
	public List<List>  getBookClosedProblems(String tn) {
		//method returns list of problems
		List sList = getJdbcTemplate().query("select id, tn, status, problem_text, problem_date, problem_initials, solution_text, solution_date, solution_initials from tf_notes where tn = ?  and (status in ('Notes', 'Problem Fixed')  or status is null)", new StringX9RowMapper(), tn);
		return sList;
	}
	
	@Override 
	public void createProblem(Problem problem) {
		String sql = "insert into tf_notes ";
		String colList  = "";
		String valList = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		//always insert id
		colList += "id, ";
		valList += ":pn, ";
		params.put("pn", problem.getPn());
		
		 
			colList += "tn, ";
			valList += ":tn, ";
			params.put("tn", problem.getTn());   
 

		if (problem.isStatusSet()) {
			colList += "status, ";
			valList += ":status, ";
			params.put("status", problem.getStatus());
		} 

		if (problem.isProblemReasonSet()) {
			colList += "problem_reason, ";
			valList += ":problemReason, ";
			params.put("problemReason", problem.getProblemReason());
		} 

		if (problem.isProblemTextSet()) {
			colList += "problem_Text, ";
			valList += ":problemText, ";
			params.put("problemText", problem.getProblemText());
		} 
		
		if (problem.isProblemDateSet()) {
			colList += "problem_Date, ";
			valList += ":problemDate, ";
			params.put("problemDate", problem.getProblemDate());
		} 
		
		if (problem.isProblemInitialsSet()) {
			colList += "problem_Initials, ";
			valList += ":problemInitials, ";
			params.put("problemInitials", problem.getProblemInitials());
		} 
		
		if (problem.isSolutionTextSet()) {
			colList += "solution_Text, ";
			valList += ":solutionText, ";
			params.put("solutionText", problem.getSolutionText());
		} 
		
		if (problem.isSolutionDateSet()) {
			colList += "solution_Date, ";
			valList += ":solutionDate, ";
			params.put("solutionDate", problem.getSolutionDate());
		} 
		
		if (problem.isSolutionInitialsSet()) {
			colList += "solution_Initials, ";
			valList += ":solutionInitials, ";
			params.put("solutionInitials", problem.getSolutionInitials());
		} 

		if (problem.isSolutionOwnerSet()) {
			colList += "solution_Owner ";
			valList += ":solutionOwner ";
			params.put("solutionOwner", problem.getSolutionOwner());
		} 
		 
		sql += " (" + colList + ") values (" + valList + ")";
		getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	@Override 
	public Problem getProblem(String tn, String pn) {
		try {
			Problem p = getJdbcTemplate().queryForObject("select * from tf_notes where ID=? and tn=?", new ProblemRowMapper(), pn, tn);
			return p;
		}catch(EmptyResultDataAccessException e) 
		{
			//if no match then 
			Problem p = new Problem();
			p.setTn(tn);
			/*String maxPn = 
			p.setPn(maxPn);*/
			return p; //empty for backing bean
		}
	}
	

	@Override 
	public Problem getNewProblem(String tn){
		try {
			List<String> vals = getJdbcTemplate().query("select max(id) from tf_notes where tn=?", new StringRowMapper(), tn);
			int nextPn = 1;
			if(vals != null && vals.size()>0 && vals.get(0) != null) {
				nextPn = Integer.parseInt(vals.get(0));
				nextPn++;
			}
			Problem p = new Problem();
			p.setPn(String.valueOf(nextPn));
			p.setTn(tn);
			return p;
		}catch(EmptyResultDataAccessException e) 
		{
			//if no match then it is a bad problem that we will deal with later
			Problem p = new Problem();

			return p; //empty for backing bean
		}
	}
 
	
	@Override 
	public void updateProblem(Problem problem) {
  		
		String sql = "update tf_notes set ";
		String valList = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		//always insert id
		 
		 
 

		if (problem.isStatusSet()) {
			valList += "status = :status, ";
			params.put("status", problem.getStatus()); 
		}

		if (problem.isProblemReasonSet()) {
			valList += "problem_Reason = :problemReason, ";
			params.put("problemReason", problem.getProblemReason()); 
		}
		if (problem.isProblemTextSet()) {
			valList += "problem_Text = :problemText, ";
			params.put("problemText", problem.getProblemText()); 
		}
		 
		if (problem.isProblemDateSet()) {
			valList += "problem_Date = :problemDate, ";
			params.put("problemDate", problem.getProblemDate()); 
		}

		if (problem.isProblemInitialsSet()) {
			valList += "problem_Initials = :problemInitials, ";
			params.put("problemInitials", problem.getProblemInitials()); 
		}		 

		if (problem.isSolutionTextSet()) {
			valList += "solution_Text = :solutionText, ";
			params.put("solutionText", problem.getSolutionText()); 
		}
		 
		if (problem.isSolutionDateSet()) {
			valList += "solution_Date = :solutionDate, ";
			params.put("solutionDate", problem.getSolutionDate()); 
		}

		if (problem.isSolutionInitialsSet()) {
			valList += "solution_Initials = :solutionInitials, ";
			params.put("solutionInitials", problem.getSolutionInitials()); 
		}
		
		if (problem.isSolutionOwnerSet()) {
			valList += "solution_owner = :solutionOwner ";
			params.put("solutionOwner", problem.getSolutionOwner()); 
		}
		 
 
		  
		sql += valList + " where tn = '" + problem.getTn() + "' and id = " + problem.getPn();
		try {
			getNamedParameterJdbcTemplate().update(sql, params);
		}catch(Exception e) {
			System.out.println(e); //allow to continue so auths get re-inserted
		}
		 
	}
	

	@Override 
	public String getProblemReasonInUseList(String problemReasonList) {
		 
		List<String> tnList= getJdbcTemplate().query("select tn from tf_notes where problem_reason in ( "+ problemReasonList + " ) ", new StringRowMapper());
	 
		String tnListStr = generateQuotedListString(tnList);
		return tnListStr;
	}

	
	
	private static class ProblemRowMapper implements RowMapper<Problem> {
		@Override
		public Problem mapRow(ResultSet rs, int rowNum) throws SQLException {
			Problem problem = new Problem();
			problem.setPn(rs.getString("ID"));
			problem.setTn(rs.getString("tn"));
			problem.setStatus(rs.getString("status"));
			problem.setProblemText(rs.getString("problem_text"));
			problem.setProblemReason(rs.getString("problem_reason"));
			problem.setProblemDate(rs.getTimestamp("problem_date"));
			problem.setProblemInitials(rs.getString("problem_initials"));

			problem.setSolutionText(rs.getString("solution_text"));
			problem.setSolutionDate(rs.getTimestamp("solution_date"));
			problem.setSolutionInitials(rs.getString("solution_initials"));
			problem.setSolutionOwner(rs.getString("solution_owner"));
			return problem;
		}
	}


	////problems end////
	
	
	
	////metadata start////
	@Override
	public List<String> getAllTnsMetadata() {
		 
		List<String> tnList = getJdbcTemplate().query("select titleno from BOOKMETADATA", new StringRowMapper());
		 
		return tnList;
	} 
	
	@Override 
	public List<List> getMetadataNewBooksAlreadyInTrackingFormDatabaseTnsInfo() {
		List tnList = getJdbcTemplate().query("select a.titleno, a.title, a.author,  a.pages, a.requesting_location, a.scanning_location from  bookmetadata a, book b where a.titleno = b.tn and check_complete is null ", new StringX6RowMapper() );
		return tnList;
	}

	@Override 
	public List<List> getIAMetadataNewBooksAlreadyInTrackingFormDatabaseTnsInfo() {
		List tnList = getJdbcTemplate().query("select a.tn, a.title, a.author,  a.num_of_pages, a.requesting_location, a.scanned_by from  IAbookmetadata a, book b where a.tn = b.tn and sent_to_scan is null ", new StringX6RowMapper() );
		return tnList;
	}

	@Override 
	public List<List> getMetadataSendToScanTnsInfo() {
		List tnList = getJdbcTemplate().query("select title, author, subject, titleno, callno,  partner_lib_callno, filmno, pages, summary, dgsno, language, owning_institution, requesting_location, scanning_location, record_number, date_original, publisher_original, fileName  from  bookmetadata " 
				+ " where   sent_to_scan is null ", new StringX18RowMapper() );
		return tnList;
	}
	@Override 
	public List<List> getMetadataUpdateTnsInfo() {
		List tnList = getJdbcTemplate().query("select title, author, subject, titleno, callno,  partner_lib_callno, filmno, pages, summary, dgsno, language, owning_institution, requesting_location, scanning_location, record_number, date_original, publisher_original, fileName  from  bookmetadataupdate  ", new StringX18RowMapper() );
		return tnList;
	}

	@Override 
	public List<List> getMetadataUpdateTnsInfo(String tnList) {
		String inClause = generateInClause("titleno", tnList);
		List mdList = getJdbcTemplate().query("select title, author, subject, titleno, callno,  partner_lib_callno, filmno, pages, summary, dgsno, language, owning_institution, requesting_location, scanning_location, record_number, date_original, publisher_original, fileName  from  bookmetadataupdate where " + inClause , new StringX18RowMapper() );
		return mdList;
	}
	
	@Override 
	public List<List> getInternetArchiveMetadataSendToScanTnsInfo() {
		List tnList = getJdbcTemplate().query("select tn, title, author, call_#, priority_Item, withdrawn, digital_Copy_Only, media_Type, metadata_Complete, batch_Class, language, remarks_From_Scan_Center, remarks_About_Book, "
				+ " scanned_By, location, scan_Complete_Date, num_of_pages, files_Received_By_Orem, image_Audit, ia_Start_Date, ia_Complete_Date, OCR_by, OCR_complete_date, Pdfreview_By, Pdfreview_Start_Date, pdf_Ready, date_Released, compression_Code, "
				+ " loaded_By, date_Loaded, collection, dnp, tn_Change_History, pdf_Orem_Archived_Date, pdf_Orem_Drive_Serial_#, pdf_Orem_Drive_Name, pdf_Copy2_Archived_Date, pdf_Copy2_Drive_Serial_#, pdf_Copy2_Drive_Name, tiff_Orem_Archived_Date, "
				+ " tiff_Orem_Drive_Serial_#, tiff_Orem_Drive_Name, tiff_Copy2_Archived_Date, tiff_Copy2_Drive_Serial_#, tiff_Copy2_Drive_Name, pdf_Sent_to_Load, site, url, pid, pages_Online, secondary_Identifier, oclc_Number, fhc_title, fhc_tn, owning_institution, publisher_original " 
				+ " from  iabookmetadata " 
				+ " where sent_to_scan is null ", new StringXRowMapper() );//56 columns

		return tnList;
	}
	
	@Override 
	public List<List> getDuplicateTnsInBook(){
		return null;
	}
	
	@Override 
	public String getDuplicateTnsInInternetArchiveMetadata(String tnList){

		String inClause = generateInClause("tn", tnList);
		List<String> tnDupeList = (List<String>) getJdbcTemplate().query("select tn from iaBookmetadata where " + inClause, new StringRowMapper() );
		String tnListStr = "";
		for(String tn: tnDupeList) {
			tnListStr += ", '" + tn + "'";
		}
		if(tnListStr != "")
			tnListStr = tnListStr.substring(2);
		return tnListStr;
	}

	@Override 
	public void deleteInternetArchiveAllNewMetadata() {
		String sql = "DELETE FROM iaBookmetadata  where sent_to_scan is null";
	    getJdbcTemplate().update(sql);
	}

	@Override 
	public void deleteInternetArchiveSelectedMetadata(String tnList) {
		String inClause = generateInClause("tn", tnList);
		String sql = "DELETE FROM iaBookmetadata  where  " + inClause;
	    getJdbcTemplate().update(sql);
	}

	@Override 
	public void deleteInternetArchiveMetadata(String tn) {
		deleteInternetArchiveSelectedMetadata("'" + tn + "'");
	}
	 

	
	@Override 
	public String getDuplicateTnsInMetadata(String tnList){

		String inClause = generateInClause("titleno", tnList);
		List<String> tnDupeList = (List<String>) getJdbcTemplate().query("select titleno from bookmetadata where " + inClause, new StringRowMapper() );
		String tnListStr = "";
		for(String tn: tnDupeList) {
			tnListStr += ", '" + tn + "'";
		}
		if(tnListStr != "")
			tnListStr = tnListStr.substring(2);
		return tnListStr;
	}

	//helper to break up >1000 elements in IN clause since Oracle has 1000 limit
	public String generateInClause(String inColumn, String elemList) {
		StringTokenizer data = new StringTokenizer(elemList, ",", false);
		String[] clauses = new String[300];
		int clauseCount = 0;
		StringBuffer sb = new StringBuffer();
		int count = 0;
		while(data.hasMoreTokens()) {
			count++;
			sb.append(data.nextToken());

			if (count == 1000) {
				clauses[clauseCount] = sb.toString();
				count = 0;
				clauseCount++;
				sb = new StringBuffer();
			} else {
				sb.append(",");
			}
		}
		if(count != 0) {
			//add remaining sub-set less than 1000
			clauses[clauseCount] = sb.toString();
			clauses[clauseCount] = clauses[clauseCount].substring(0, clauses[clauseCount].length()-1);//trim comma
		}
		
		String all = "";
		for(int x = 0; x<= clauseCount; x++) {
			if(all.equals("") == false)
				all = all + " or ";
			all = all + inColumn + " in ( " + clauses[x] + " ) ";
		}
		return all;
	}

 
	@Override 
	public String getDuplicateTnsInBook(String tnList){
		if(tnList == "")
			return "";
		
		String inClause = generateInClause("tn", tnList);
		List<String> tnDupeList = (List<String>) getJdbcTemplate().query("select tn from book where " + inClause, new StringRowMapper() );
		String tnListStr = "";
		for(String tn: tnDupeList) {
			tnListStr += ", '" + tn + "'";
		}
		if(tnListStr != "")
			tnListStr = tnListStr.substring(2);
		return tnListStr;
	}
	@Override 
	public List<String> getDuplicateTnsInBookList(String tnList){
		if(tnList == "")
			return new ArrayList();
		
		String inClause = generateInClause("tn", tnList);
		
		List<String> tnDupeList = (List<String>) getJdbcTemplate().query("select tn from book where " + inClause, new StringRowMapper() );
		return tnDupeList;
	}
	
	@Override 
	public List<String> getMetadataCompleteAndSent(){
		List tnList = getJdbcTemplate().query("select title, author, subject, titleno, callno, partner_lib_callno, filmno, pages, summary, dgsno, language, owning_institution, requesting_location, scanning_location, record_number, date_original, publisher_original, filename, sent_to_scan  from  bookmetadata " 
				+ " where check_complete is not null ", new StringX19RowMapper() );
		return tnList;
	}
	
	
	
	@Override 
	public void deleteAllNewMetadata() {
		String sql = "DELETE FROM bookmetadata  where check_complete is null";
	    getJdbcTemplate().update(sql);
	}
	

	@Override 
	public void deleteAllUpdateMetadata() {
		String sql = "DELETE FROM bookmetadataupdate";
	    getJdbcTemplate().update(sql);
	}
	

	@Override 
	public void deleteAllInternetArchiveNewMetadata() {
		String sql = "DELETE FROM iaBookmetadata  where sent_to_scan is null";
	    getJdbcTemplate().update(sql);
	}
	
	@Override 
	public void deleteSelectedMetadata(String tnList) {
		String inClause = generateInClause("titleno", tnList);
		String sql = "DELETE FROM bookmetadata  where  " + inClause;
	    getJdbcTemplate().update(sql);
	}

	@Override 
	public void deleteSelectedMetadataForUpdate(String tnList) {
		String inClause = generateInClause("titleno", tnList);
		String sql = "DELETE FROM bookmetadataupdate  where  " + inClause;
	    getJdbcTemplate().update(sql);
	}
	@Override 
	public void deleteSelectedInternetArchiveMetadata(String tnList) {
		String inClause = generateInClause("tn", tnList);
		String sql = "DELETE FROM IAbookmetadata  where  " + inClause;
	    getJdbcTemplate().update(sql);
	}
	@Override 
	public void deleteMetadata(String tn) {
		deleteSelectedMetadata("'" + tn + "'");
	}
	 
	
	@Override 
	public void checkCompleteSelectedMetadata(String tnList, String checker) {
		String inClause = generateInClause("titleno", tnList);
		String sql = "UPDATE bookmetadata SET CHECK_COMPLETE = CURRENT_TIMESTAMP, CHECKER = '" + checker + "' where " + inClause;
	    getJdbcTemplate().update(sql);
	}
	
	@Override 
	public void checkCompleteAllMetadata(String checker){
		String sql = "UPDATE bookmetadata SET CHECK_COMPLETE = CURRENT_TIMESTAMP, CHECKER = '" + checker + "' where check_complete is null";
	    getJdbcTemplate().update(sql);
	}
	
	@Override
	public String sendToScanSelectedMetadata(List<String> allTnList, String sender) {
		//try {
		
			//check for duplicates
			String allTnListStr = generateQuotedListString(allTnList);
			List<String> dupList = getDuplicateTnsInBookList(allTnListStr);
			
			List<String> insertTnList = filterOutUpdateTns(allTnList, dupList);
			String dupListStr = generateQuotedListString(dupList);
			String tnInsertListStr = generateQuotedListString(insertTnList);
			
			// next insert/update into trackingform db 
			migrateMetadataToBookInsert(tnInsertListStr);
			migrateMetadataToBookUpdate(dupListStr);
			autoUpdateCopyrightSerialEtc(allTnListStr);
						
			//next update timestamp in metadata table								
			String inClause = generateInClause("titleno", allTnListStr);
			String sql = "UPDATE bookmetadata SET CHECK_COMPLETE = CURRENT_TIMESTAMP, SENT_TO_SCAN = CURRENT_TIMESTAMP, sender =  (select name from users where lower(id) = '" + sender.toLowerCase() + "') where " + inClause;//UPDATE CHECK_COMPLETE also eventhough it is not used anymore

			getJdbcTemplate().update(sql);
 
			return "";
	/*} catch (Exception e) {
			throw e;
		}*/
	}

	@Override
	public String sendToDoUpdateSelectedMetadata(List<String> allTnList, String sender) {
		 
		 	String[] columnNames = {"title", "author", "subject", "titleno", "callno", "partner_lib_callno", "filmno", "pages", "summary", "dgsno", "language", "owning_institution", "requesting_location", "scanning_location", "record_number", "date_original", "publisher_original", "filename", "current_timestamp_date_added", "metadata_adder"};

		 	String tnList = generateQuotedListString(allTnList);
		 	List<List> allMd = getMetadataUpdateTnsInfo(tnList);
			//next update timestamp in metadata table								
			 
		 	updateBatchMetatdataUpdates("bookmetadataupdate", columnNames, allMd);
		 	deleteSelectedMetadataForUpdate(tnList);
			return "";
 
	}
	

	//returns array of two List<List>
	//0 element is book data
	//1 element is email and site data
	@Override 
	public Object[] getReportForSendToScanSelectedMetadata(List<String> allTnList){
		String allTnListStr = generateQuotedListString(allTnList);

		//next update timestamp in metadata table								
		String inClause1 = generateInClause("a.titleno", allTnListStr);
		String inClause = generateInClause("titleno", allTnListStr);
		 
		List tnList = getJdbcTemplate().query("select a.titleno, a.title, a.author, a.requesting_location, a.owning_institution, a.scanning_location, a.pages, b.scan_complete_date, b.files_received_by_orem, b.url from BOOKmetadata a " 
							+ " left outer join book b on a.titleno = b.tn  where  " + inClause1 , new StringX10RowMapper());
		List emailList = getJdbcTemplate().query("select id, send_scan_notice, email, primary_location from users where send_scan_notice = 'T' and primary_location in ( select distinct(requesting_location) from bookmetadata where " + inClause + " union select distinct(owning_institution) from BOOKmetadata  where " + inClause + ")", new StringX4RowMapper());
		List emailList2 = getJdbcTemplate().query("select id, send_scan_notice, email, 'ALLSITES' from users where send_scan_notice = 'T' and primary_location is null", new StringX4RowMapper());

		Object[] retVal = new Object[3];
		retVal[0] = tnList; 
		retVal[1] = emailList;
		retVal[2] = emailList2;
		return retVal;
	}
	

	//returns array of rows
	//0 element is book data
	//1 element is email and site data
	@Override 
	public Object[] getReportForSendToScanSelectedMetadataAll(){
  
		List tnList = getJdbcTemplate().query("select a.titleno, a.title, a.author, a.requesting_location, a.owning_institution, a.scanning_location, a.pages, b.scan_complete_date, b.files_received_by_orem, b.url  " 
										+ " from BOOKmetadata a left outer join book b on a.titleno = b.tn  where a.titleno in (select c.titleno from BookMetadata c where c.sent_to_scan is null)", new StringX10RowMapper());
		List emailList = getJdbcTemplate().query("select id, send_scan_notice, email, primary_location from users where send_scan_notice = 'T' and primary_location " 
				+ " in ( select distinct (requesting_location) from bookmetadata where  titleno "
				+ " in (select titleno from BookMetadata where sent_to_scan is null ) "
				+ " union "
				+ " select distinct (owning_institution) from bookmetadata where titleno "
				+ " in (select titleno from BookMetadata where sent_to_scan is null ))", new StringX4RowMapper());
		List emailList2 = getJdbcTemplate().query("select id, send_scan_notice, email, 'ALLSITES' from users where send_scan_notice = 'T' and primary_location is null", new StringX4RowMapper());
		Object[] retVal = new Object[3];
		retVal[0] = tnList;
		retVal[1] = emailList;
		retVal[2] = emailList2;
		return retVal;
	}
 
	
	@Override 
	public String sendToScanAllMetadata(String sender){
		 
			List<String> allTnList = (List<String>) getJdbcTemplate()
					.query("select titleno from bookmetadata where sent_to_scan is null",
							new StringRowMapper());
			
			//check for duplicates
			String allTnListStr = generateQuotedListString(allTnList);
			List<String> dupList = getDuplicateTnsInBookList(allTnListStr);
			
			List<String> insertTnList = filterOutUpdateTns(allTnList, dupList);
			String dupListStr = generateQuotedListString(dupList);
			String tnInsertListStr = generateQuotedListString(insertTnList);
			
			// next insert/update into trackingform db 
			migrateMetadataToBookInsert(tnInsertListStr);
			migrateMetadataToBookUpdate(dupListStr);
			autoUpdateCopyrightSerialEtc(allTnListStr);
			
			
			//next update timestamp in metadata table
			String sql = "UPDATE bookmetadata SET  CHECK_COMPLETE = CURRENT_TIMESTAMP, SENT_TO_SCAN = CURRENT_TIMESTAMP, sender =  (select name from users where lower(id) = '" + sender.toLowerCase() + "') where sent_to_scan is null";
			getJdbcTemplate().update(sql);

			return "";
	}

	@Override 
	public String sendToDoUpdateAllMetadata(String sender){

	 	String[] columnNames = {"title", "author", "subject", "titleno", "callno", "partner_lib_callno", "filmno", "pages", "summary", "dgsno", "language", "owning_institution", "requesting_location", "scanning_location", "record_number", "date_original", "publisher_original", "filename", "current_timestamp_date_added", "metadata_adder"};
	 	 
	 	List<List> allMd = getMetadataUpdateTnsInfo( );
		//next update timestamp in metadata table								
		 
	 	updateBatchMetatdataUpdates("bookmetadataupdate", columnNames, allMd);

	 	deleteAllUpdateMetadata();
	 	
		return "";
	}

	@Override 
	public String sendToScanSelectedInternetArchiveMetadata(List<String> allTnList, String sender) {
		//try {
		
			//check for duplicates
			String allTnListStr = generateQuotedListString(allTnList);
			List<String> dupList = getDuplicateTnsInBookList(allTnListStr);
			
			List<String> insertTnList = filterOutUpdateTns(allTnList, dupList);
			String dupListStr = generateQuotedListString(dupList);
			String tnInsertListStr = generateQuotedListString(insertTnList);
			
			// next insert/update into trackingform db 
			migrateInternetArchiveMetadataToBookInsert(tnInsertListStr);
			migrateInternetArchiveMetadataToBookUpdate(dupListStr);
			//no filename to do this, but later todo have ia books go throu xml md creation without tesseract autoUpdateCopyrightSerialEtc(allTnListStr);
						
			//next update timestamp in metadata table								
			String inClause = generateInClause("tn", allTnListStr);
			String sql = "UPDATE iaBookmetadata SET SENT_TO_SCAN = CURRENT_TIMESTAMP where " + inClause; 

			getJdbcTemplate().update(sql);
 
			return "";
	/*} catch (Exception e) {
			throw e;
		}*/
	}
	
	@Override 
	public String sendToScanAllInternetArchiveMetadata(String sender){
		 
			List<String> allTnList = (List<String>) getJdbcTemplate()
					.query("select tn from iaBookmetadata where sent_to_scan is null",
							new StringRowMapper());
			
			//check for duplicates
			String allTnListStr = generateQuotedListString(allTnList);
			List<String> dupList = getDuplicateTnsInBookList(allTnListStr);
			
			List<String> insertTnList = filterOutUpdateTns(allTnList, dupList);
			String dupListStr = generateQuotedListString(dupList);
			String tnInsertListStr = generateQuotedListString(insertTnList);
			
			// next insert/update into trackingform db 
			migrateInternetArchiveMetadataToBookInsert(tnInsertListStr);
			migrateInternetArchiveMetadataToBookUpdate(dupListStr);
			//no filename to do this, but later todo have ia books go throu xml md creation without tesseract  autoUpdateCopyrightSerialEtc(allTnListStr);
			
			
			//next update timestamp in metadata table
			String sql = "UPDATE iaBookmetadata SET  SENT_TO_SCAN = CURRENT_TIMESTAMP where sent_to_scan is null";
			getJdbcTemplate().update(sql);

			return "";
	 
	}
	

	@Override 
	public void autoUpdateCopyrightSerialEtc(String tnList) {
		String sql1 = "UPDATE book "
				+ " SET publication_type = "
				+ " CASE "
				+ "  WHEN filename like 'SE%' "
				+ "   THEN 'Serial' "
				+ "  WHEN filename like 'CP_SE%' "
		        + "   THEN 'Serial' "
		        + "  ELSE 'Book' "
		        + " END where tn IN ( " + tnList + " )";	  
		
		String sql2 = "UPDATE book " 
				+ " SET property_right = "
				+ "  CASE "
				+ "   WHEN filename like 'CP%' "
				+ "     THEN 'Copyright Protected' "
				+ "   ELSE  'Public Domain' "
				+ "   END where tn IN  ( " + tnList + " )";	  
		 
	    getJdbcTemplate().update(sql1);
	    getJdbcTemplate().update(sql2);
	}
	
	public List<String> filterOutUpdateTns(List<String> tnList, List<String> dupList) {
		List<String> insertTns = new ArrayList();
		for(String tn: tnList) {
			if(dupList.contains(tn) == false)
				insertTns.add(tn);
				
		}
		return insertTns;
	}
	
	@Override
	public String generateQuotedListString(List<String> l) {
		if(l==null)
			return null;
		String tnListStr = "";
		for (String tn : l) {
			tnListStr += ", '" + tn + "'";
		}
		if(tnListStr == "")
			return "";
		tnListStr = tnListStr.substring(2);
		
		return tnListStr;
	}
	 
	 
	public String generateQuotedListStringFromList(List<List> l) {
		if(l==null)
			return null;
		String tnListStr = "";
		for (List row : l) {
			tnListStr += ", '" + row.get(0) + "'";
		}
		if(tnListStr == "")
			return "";
		tnListStr = tnListStr.substring(2);
		
		return tnListStr;
	}
	 
	public void migrateMetadataToBookInsert( String tnList ){
		if(tnList.equals(""))
			return;
		//move metadata table data to book table.  (scan_metadata_complete is now set when book enters scan)
		String inClause = generateInClause("titleno", tnList);
		String sql = "INSERT into book (tn, oclc_number, isbn_issn, title, author, call_#,  partner_lib_call_#, language, owning_institution, requesting_location, scanned_by, record_number, subject, filmno, pages_Physical_Description, summary, dgsno, date_Original, publisher_Original, filename,  priority_item, withdrawn, digital_copy_only, media_type, scan_metadata_complete, location,  site) " +
				"select titleno, oclc_number, isbn_issn,  title, author, callno, partner_lib_callno, language, owning_institution,  requesting_location, scanning_location, record_number, subject, filmno, pages, summary, dgsno, date_Original, publisher_Original, filename,   'F', 'F', 'F', 'Book', current_timestamp, '02 Tiffs', 'Orem Digital Processing Center' from bookmetadata where " + inClause;
	    getJdbcTemplate().update(sql);
	    
 
	}

	//note:  we took out UPDATE via metata of a book per Jeri's request since some books were getting update by mistake (so this method is outdated if there are any new columns in bookmetadata table)
	public void migrateMetadataToBookUpdate( String tnList ){
		if(tnList.equals(""))
			return;
		//move metadata table data to book table.  (scan_metadata_complete is now set when book enters scan)
		String inClause = generateInClause("b.titleno", tnList);
		String sql = " UPDATE   (select " +
				" a.title old1,  " +
				" a.author old2,  " +
				" a.call_# old3,  " +
				" a.language old4,  " +
				" a.requesting_location old5,  " +
				" a.scanned_by old6,  " +
				" a.record_number old7,  " +
				" a.priority_item old8,  " +
				" a.withdrawn  old9,  " +
				" a.digital_copy_only old10,  " +
				" a.media_type old11,  " +
				" a.scan_metadata_complete old12, " +
				" a.location old13, " +
				" a.site old14, " +
				" a.partner_lib_call_# old15, " +
				" a.subject old16, " +
				" a.filmno old17, " +
				" a.pages_Physical_Description old18, " +
				" a.summary old19, " +
				" a.dgsno old20, " +
				" a.date_Original old21, " +
				" a.publisher_Original old22, " +
				" a.oclc_number old23, " +
				" a.isbn_issn old24, " +
				" a.owning_institution old25, " +
				" a.filename old26, " +
				" b.title new1,  " +
				" b.author new2,  " +
				" b.callno new3,  " +
				" b.language new4,  " +
				" b.requesting_location new5,  " +
				" b.scanning_location new6,  " +
				" b.record_number new7,  " +
				" b.partner_lib_callno new15,  " +
				" b.subject new16, " +
				" b.filmno new17, " +
				" b.pages new18, " +
				" b.summary new19, " +
				" b.dgsno new20, " +
				" b.date_Original new21, " +
				" b.publisher_Original new22, " +
				" b.oclc_number new23, " +
				" b.isbn_issn new24, " +
				" b.owning_institution new25, " +
				" b.filename new26 " +
				" from book a, bookmetadata b where a.tn=b.titleno and " + inClause + " )  " +
				" set old1 = new1,  " +
				" old2 = new2, " +
				" old3 = new3, " +
				" old4 = new4, " +
				" old5 = new5, " +
				" old6 = new6, " +
				" old7 = new7, " +
				" old8 = 'F',  " +
				" old9 = 'F',  " +
				" old10= 'F',  " +
				" old11 = 'Book',  " +
				" old12 = current_timestamp,  " +
				" old13 = '02 Tiffs',  " +
				" old14 = 'Orem Digital Processing Center', " +
				" old15 = new15, " +
				" old16 = new16, " +
				" old17 = new17, " +
				" old18 = new18, " +
				" old19 = new19, " +
				" old20 = new20, " +
				" old21 = new21, " +
				" old22 = new22, " +
				" old23 = new23, " +
				" old24 = new24, " +
				" old25 = new25, " +
				" old26 = new26 ";
	
	    getJdbcTemplate().update(sql);
	}


	public void migrateInternetArchiveMetadataToBookInsert( String tnList ){
		if(tnList.equals(""))
			return;
		//move metadata table data to book table.  (scan_metadata_complete is now set when book enters scan)
		String inClause = generateInClause("tn", tnList);
		String sql =  "INSERT into book ( tn ,  title ,  author ,  call_# ,  priority_Item ,  withdrawn ,  digital_Copy_Only ,  media_Type ,  metadata_Complete ,  batch_Class , "
					+" language ,  remarks_From_Scan_Center ,  remarks_About_Book ,  scanned_By ,  location ,  scan_Complete_Date ,  num_of_pages ,  files_Received_By_Orem ,  image_Audit ,  ia_Start_Date , "
					+"  ia_Complete_Date ,  OCR_by ,  OCR_complete_date ,  Pdfreview_By ,  Pdfreview_Start_Date ,  pdf_Ready ,  date_Released ,  compression_Code ,  loaded_By ,  date_Loaded , "
					+"  collection ,  dnp ,  tn_Change_History ,  pdf_Orem_Archived_Date ,  pdf_Orem_Drive_Serial_# ,  pdf_Orem_Drive_Name ,  pdf_Copy2_Archived_Date ,  pdf_Copy2_Drive_Serial_# ,  pdf_Copy2_Drive_Name ,  tiff_Orem_Archived_Date , "
					+"  tiff_Orem_Drive_Serial_# ,  tiff_Orem_Drive_Name ,  tiff_Copy2_Archived_Date ,  tiff_Copy2_Drive_Serial_# ,  tiff_Copy2_Drive_Name ,  pdf_Sent_to_Load ,  site ,  url ,  pid ,  pages_Online , "
					+"  secondary_Identifier ,  oclc_Number, fhc_title, fhc_tn, owning_institution, publisher_original) " 
				    + " select  tn ,  title ,  author ,  call_# ,  priority_Item ,  withdrawn ,  digital_Copy_Only ,  media_Type ,  metadata_Complete ,  batch_Class , "
					+ " language ,  remarks_From_Scan_Center ,  remarks_About_Book ,  scanned_By ,  location ,  scan_Complete_Date ,  num_of_pages ,  files_Received_By_Orem ,  image_Audit ,  ia_Start_Date , "
					+ " ia_Complete_Date ,  OCR_by ,  OCR_complete_date ,  Pdfreview_By ,  Pdfreview_Start_Date ,  pdf_Ready ,  date_Released ,  compression_Code ,  loaded_By ,  date_Loaded , "
					+ " collection ,  dnp ,  tn_Change_History ,  pdf_Orem_Archived_Date ,  pdf_Orem_Drive_Serial_# ,  pdf_Orem_Drive_Name ,  pdf_Copy2_Archived_Date ,  pdf_Copy2_Drive_Serial_# ,  pdf_Copy2_Drive_Name ,  tiff_Orem_Archived_Date , "
					+ " tiff_Orem_Drive_Serial_# ,  tiff_Orem_Drive_Name ,  tiff_Copy2_Archived_Date ,  tiff_Copy2_Drive_Serial_# ,  tiff_Copy2_Drive_Name ,  pdf_Sent_to_Load ,  site ,  url ,  pid ,  pages_Online , "
					+ " secondary_Identifier,  oclc_Number, fhc_title, fhc_tn, owning_institution, publisher_original from iaBookmetadata where " + inClause;
	    getJdbcTemplate().update(sql);
	    
	}

	public void migrateInternetArchiveMetadataToBookUpdate( String tnList ){
		if(tnList.equals(""))
			return;
		//move metadata table data to book table.  (scan_metadata_complete is now set when book enters scan)
		String inClause = generateInClause("b.tn", tnList);
		String sql = " UPDATE (select " +
				"a.tn old1 , a.title old2 , a.author old3 , a.call_# old4 , a.priority_Item old5 , a.withdrawn old6 , a.digital_Copy_Only old7 , a.media_Type old8 , a.metadata_Complete old9 , a.batch_Class old10 ,  " +
				"a.language old11 , a.remarks_From_Scan_Center old12 , a.remarks_About_Book old13 , a.scanned_By old14 , a.location old15 , a.scan_Complete_Date old16 , a.num_of_pages old17 , a.files_Received_By_Orem old18 , a.image_Audit old19 , a.ia_Start_Date old20 ,  " +
				"a.ia_Complete_Date old21 , a.OCR_by old22 , a.OCR_complete_date old23 , a.Pdfreview_By old24 , a.Pdfreview_Start_Date old25 , a.pdf_Ready old26 , a.date_Released old27 , a.compression_Code old28 , a.loaded_By old29 , a.date_Loaded old30 ,  " +
				"a.collection old31 , a.dnp old32 , a.tn_Change_History old33 , a.pdf_Orem_Archived_Date old34 , a.pdf_Orem_Drive_Serial_# old35 , a.pdf_Orem_Drive_Name old36 , a.pdf_Copy2_Archived_Date old37 , a.pdf_Copy2_Drive_Serial_# old38 , a.pdf_Copy2_Drive_Name old39 , a.tiff_Orem_Archived_Date old40 ,  " +
				"a.tiff_Orem_Drive_Serial_# old41 , a.tiff_Orem_Drive_Name old42 , a.tiff_Copy2_Archived_Date old43 , a.tiff_Copy2_Drive_Serial_# old44 , a.tiff_Copy2_Drive_Name old45 , a.pdf_Sent_to_Load old46 , a.site old47 , a.url old48 , a.pid old49 , a.pages_Online old50 ,  " +
				"a.secondary_Identifier old51 , a.oclc_Number old52, a.fhc_title old53, a.fhc_tn old54, a.owning_institution old55, a.publisher_original old56, " +
				"b.tn new1 , b.title new2 , b.author new3 , b.call_# new4 , b.priority_Item new5 , b.withdrawn new6 , b.digital_Copy_Only new7 , b.media_Type new8 , b.metadata_Complete new9 , b.batch_Class new10 ,  " +
				"b.language new11 , b.remarks_From_Scan_Center new12 , b.remarks_About_Book new13 , b.scanned_By new14 , b.location new15 , b.scan_Complete_Date new16 , b.num_of_pages new17 , b.files_Received_By_Orem new18 , b.image_Audit new19 , b.ia_Start_Date new20 ,  " +
				"b.ia_Complete_Date new21 , b.OCR_by new22 , b.OCR_complete_date new23 , b.Pdfreview_By new24 , b.Pdfreview_Start_Date new25 , b.pdf_Ready new26 , b.date_Released new27 , b.compression_Code new28 , b.loaded_By new29 , b.date_Loaded new30 ,  " +
				"b.collection new31 , b.dnp new32 , b.tn_Change_History new33 , b.pdf_Orem_Archived_Date new34 , b.pdf_Orem_Drive_Serial_# new35 , b.pdf_Orem_Drive_Name new36 , b.pdf_Copy2_Archived_Date new37 , b.pdf_Copy2_Drive_Serial_# new38 , b.pdf_Copy2_Drive_Name new39 , b.tiff_Orem_Archived_Date new40 ,  " +
				"b.tiff_Orem_Drive_Serial_# new41 , b.tiff_Orem_Drive_Name new42 , b.tiff_Copy2_Archived_Date new43 , b.tiff_Copy2_Drive_Serial_# new44 , b.tiff_Copy2_Drive_Name new45 , b.pdf_Sent_to_Load new46 , b.site new47 , b.url new48 , b.pid new49 , b.pages_Online new50 ,  " +
				"b.secondary_Identifier new51 , b.oclc_Number new52,  b.fhc_title new53,  b.fhc_tn new54,  b.owning_institution new55, b.publisher_original new56 " +
				" from book a, iaBookmetadata b where a.tn=b.tn and " + inClause + " ) " +
				" set old1 = new1,  " +
				" old2 = new2, " +
				" old3 = new3, " +
				" old4 = new4, " +
				" old5 = new5, " +
				" old6 = new6, " +
				" old7 = new7, " +
				" old8 = new8, " +
				" old9 = new9, " +
				" old10 = new10, " +
				" old11 = new11, " +
				" old12 = new12, " +
				" old13 = new13, " +
				" old14 = new14, " +
				" old15 = new15, " +
				" old16 = new16, " +
				" old17 = new17, " +
				" old18 = new18, " +
				" old19 = new19, " +
				" old20 = new20, " +
				" old21 = new21,  " +
				" old22 = new22, " +
				" old23 = new23, " +
				" old24 = new24, " +
				" old25 = new25, " +
				" old26 = new26, " +
				" old27 = new27, " +
				" old28 = new28, " +
				" old29 = new29, " +
				" old30 = new30, " +
				" old31 = new31,  " +
				" old32 = new32, " +
				" old33 = new33, " +
				" old34 = new34, " +
				" old35 = new35, " +
				" old36 = new36, " +
				" old37 = new37, " +
				" old38 = new38, " +
				" old39 = new39, " +
				" old40 = new40, " +
				" old41 = new41, " +
				" old42 = new42, " +
				" old43 = new43, " +
				" old44 = new44, " +
				" old45 = new45, " +
				" old46 = new46, " +
				" old47 = new47, " +
				" old48 = new48, " +
				" old49 = new49, " +
				" old50 = new50, " +
				" old51 = new51, " +
				" old52 = new52, " +
				" old53 = new53, " +
				" old54 = new54, " +
				" old55 = new55, " +
				" old56 = new56 ";
	
	    getJdbcTemplate().update(sql);
	}
	
	@Override 
	public BookMetadata getBookMetadata(String tn) {
		try {
			return getJdbcTemplate().queryForObject("select * from BOOKMETADATA where titleno=?", new BookMetadataRowMapper(), tn);
		}catch(EmptyResultDataAccessException e) 
		{ 
			return new BookMetadata(); //empty for backing bean
		}
	} 

	private static class BookMetadataRowMapper implements RowMapper<BookMetadata> {
		@Override
		public BookMetadata mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookMetadata book = new BookMetadata();
			book.setTitle(rs.getString("TITLE"));
			book.setAuthor(rs.getString("AUTHOR"));
			book.setSubject(rs.getString("SUBJECT"));
			book.setTitleno(rs.getString("TITLENO"));
			book.setOclcNumber(rs.getString("OCLC_NUMBER"));
			book.setIsbnIssn(rs.getString("ISBN_ISSN"));
			book.setCallno(rs.getString("CALLNO"));
			book.setPartnerLibCallno(rs.getString("PARTNER_LIB_CALLNO"));
			book.setFilmno(rs.getString("FILMNO"));
			book.setPages(rs.getString("PAGES"));
			book.setSummary(rs.getString("SUMMARY"));
			book.setDgsno(rs.getString("DGSNO"));
			book.setLanguage(rs.getString("LANGUAGE"));
			book.setOwningInstitution(rs.getString("OWNING_INSTITUTION"));
			book.setRequestingLocation(rs.getString("REQUESTING_LOCATION"));
			book.setScanningLocation(rs.getString("SCANNING_LOCATION"));
			book.setRecordNumber(rs.getString("RECORD_NUMBER"));
			book.setDateOriginal(rs.getTimestamp("DATE_ORIGINAL"));
			book.setPublisherOriginal(rs.getString("PUBLISHER_ORIGINAL"));
			book.setFilename(rs.getString("FILENAME"));
			book.setDateAdded(rs.getTimestamp("DATE_ADDED"));
			book.setMetadataAdder(rs.getString("METADATA_ADDER"));
			book.setCheckComplete(rs.getTimestamp("CHECK_COMPLETE"));
			book.setChecker(rs.getString("CHECKER"));
			book.setSentToScan(rs.getTimestamp("SENT_TO_SCAN"));
			book.setSender(rs.getString("SENDER"));
			 		
			return book;
		}
	}

	@Override 
	public BookMetadata getBookMetadataFromBookTable(String tn) {
		try {
			return getJdbcTemplate().queryForObject("select title, author, subject, tn, oclc_number, isbn_issn, call_#, partner_lib_call_#, filmno, pages_physical_description, summary, dgsno, language, owning_institution, requesting_location, scanned_by, record_number,date_original, publisher_original, filename from BOOK where tn=?", new BookMetadataFromBookTableRowMapper(), tn);
		}catch(EmptyResultDataAccessException e) 
		{ 
			return new BookMetadata(); //empty for backing bean
		}
	} 

	public String getMessageString(String key) {
		Properties p = new Properties();
		String val = "";
		try {
			InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("messages.properties");
			p.load(propStream);
			val = p.getProperty(key);

		}catch(Exception e) {
			val = null;
		}
		return val;
	}
	
	private static class BookMetadataFromBookTableRowMapper implements RowMapper<BookMetadata> {
		@Override
		public BookMetadata mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookMetadata book = new BookMetadata();
			book.setTitle(rs.getString("TITLE"));
			book.setAuthor(rs.getString("AUTHOR"));
			book.setSubject(rs.getString("SUBJECT"));
			book.setTitleno(rs.getString("tn"));
			book.setOclcNumber(rs.getString("OCLC_NUMBER"));
			book.setIsbnIssn(rs.getString("ISBN_ISSN"));
			book.setCallno(rs.getString("call_#"));
			book.setPartnerLibCallno(rs.getString("partner_lib_call_#"));
			book.setFilmno(rs.getString("FILMNO"));
			book.setPages(rs.getString("pages_physical_description"));
			book.setSummary(rs.getString("SUMMARY"));
			book.setDgsno(rs.getString("DGSNO"));
			book.setLanguage(rs.getString("LANGUAGE"));
			book.setOwningInstitution(rs.getString("OWNING_INSTITUTION"));
			book.setRequestingLocation(rs.getString("REQUESTING_LOCATION"));
			book.setScanningLocation(rs.getString("scanned_by"));
			book.setRecordNumber(rs.getString("RECORD_NUMBER"));
			book.setDateOriginal(rs.getTimestamp("DATE_ORIGINAL"));
			book.setPublisherOriginal(rs.getString("PUBLISHER_ORIGINAL"));
			book.setFilename(rs.getString("FILENAME"));
			//na book.setDateAdded(rs.getTimestamp("DATE_ADDED"));
			//na book.setMetadataAdder(rs.getString("METADATA_ADDER"));
			//na book.setCheckComplete(rs.getTimestamp("CHECK_COMPLETE"));
			//na book.setChecker(rs.getString("CHECKER"));
			//na book.setSentToScan(rs.getTimestamp("SENT_TO_SCAN"));
			//na book.setSender(rs.getString("SENDER"));
			 		
			return book;
		}
	}

	

	@Override
	public void updateBookMetadata(BookMetadata book) {
		updateBookMetadata(book, book.getTitleno());
	}
	
	@Override
	public void updateBookMetadata(BookMetadata book, String oldTn) {
		// TODO Auto-generated method stub
		String sql = "update bookmetadata set ";
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		//tn will always updated even if not change to simplify sql generation		
		sql += "titleno = :titleno, ";
		params.put("titleno", book.getTitleno());
		if (book.isOclcNumberSet()) {
			sql += "oclc_number =  :oclcNumber, ";
			params.put("oclcNumber", book.getOclcNumber());
		}		
		if (book.isIsbnIssnSet()) {
			sql += "isbn_issn =  :isbnIssn, ";
			params.put("isbnIssn", book.getIsbnIssn());
		}	
		if (book.isTitleSet()) {
			sql += "title =  :title, ";
			params.put("title", book.getTitle());
		}
		if (book.isAuthorSet()) {
			sql += "author = :author, ";
			params.put("author", book.getAuthor());
		}
		 
		if (book.isSubjectSet()) {
			sql += "subject = :subject, ";
			params.put("subject", book.getSubject());
		}
		if (book.isCallnoSet()) {
			sql += "callno = :callno, ";
			params.put("callno", book.getCallno());
		}
		if (book.isPartnerLibCallnoSet()) {
			sql += "partner_Lib_Callno = :partnerLibCallno, ";
			params.put("partnerLibCallno", book.getPartnerLibCallno());
		}
		if (book.isFilmnoSet()) {
			sql += "filmno = :filmno, ";
			params.put("filmno", book.getFilmno());
		}
		if (book.isPagesSet()) {
			sql += "pages = :pages, ";
			params.put("pages", book.getPages());
		}
		if (book.isSummarySet()) {
			sql += "summary = :summary, ";
			params.put("summary", book.getSummary());
		}
		if (book.isDgsnoSet()) {
			sql += "dgsno = :dgsno, ";
			params.put("dgsno", book.getDgsno());
		}
		if (book.isLanguageSet()) {
			sql += "language = :language, ";
			params.put("language", book.getLanguage()==""?null:book.getLanguage());  //dropdown select "" change to nul
		}
		if (book.isOwningInstitutionSet()) {
			sql += "owning_institution = :owning_institution, ";
			params.put("owning_institution", book.getOwningInstitution()==""?null:book.getOwningInstitution());  //dropdown select "" change to null
		}
		if (book.isRequestingLocationSet()) {
			sql += "requesting_location = :requesting_location, ";
			params.put("requesting_location", book.getRequestingLocation()==""?null:book.getRequestingLocation());  //dropdown select "" change to null
		}
		if (book.isScanningLocationSet()) {
			sql += "scanning_location = :scanning_location, ";
			params.put("scanning_location", book.getScanningLocation()==""?null:book.getScanningLocation());  //dropdown select "" change to null
		}
		if (book.isRecordNumberSet()) {
			sql += "record_number = :record_number, ";
			params.put("record_number", book.getRecordNumber()==""?null:book.getRecordNumber());  //dropdown select "" change to null
		}
		if (book.isDateOriginalSet()) {
			sql += "date_original = :dateOriginal, ";
			params.put("dateOriginal", book.getDateOriginal());
		}
		if (book.isPublisherOriginalSet()) {
			sql += "publisher_original = :publisherOriginal, ";
			params.put("publisherOriginal", book.getPublisherOriginal());  
		}
		if (book.isFilenameSet()) {
			sql += "filename = :filename, ";
			params.put("filename", book.getFilename());  
		}
		if (book.isDateAddedSet()) {
			sql += "date_added = :dateAdded, ";
			params.put("dateAdded", book.getDateAdded());
		}
		if (book.isMetadataAdderSet()) {
			sql += "metadata_adder = :metadataAdder, ";
			params.put("metadataAdder", book.getMetadataAdder());
		}
		if (book.isCheckCompleteSet()) {
			sql += "check_complete = :checkComplete, ";
			params.put("checkComplete", book.getCheckComplete());
		}
		if (book.isCheckerSet()) {
			sql += "checker = :checker, ";
			params.put("checker", book.getChecker());
		}
		if (book.isSentToScanSet()) {
			sql += "sent_to_scan = :sentToScan, ";
			params.put("sentToScan", book.getSentToScan());
		}
		if (book.isSenderSet()) {
			sql += "sender = :sender, ";
			params.put("sender", book.getSender());
		}

		String tn;
		if(oldTn != null)
			tn = oldTn;
		else
			tn = book.getTitleno();
		//remove final comma
		sql = sql.substring(0, sql.length() - 2) + " where titleno = '" + tn + "'";
		
		 
		getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	////metadata end////
	///reports///
	public String generateStatsFilter(String year, String month, String scannedBy) {
		List tnList;
		String allFilter = "";
	 
		
		if(!year.equals("All")){
			allFilter = " year = '" + year + "' " ;
		}
		if(!month.equals("All")) {
			if(allFilter.equals("")) {
				allFilter = " month = '" + month + "' " ;
			}else {
				allFilter = allFilter + " and month = '" + month + "' " ;
			}
		}
		if(!scannedBy.equals("All")) {
			if(allFilter.equals("")) {
				allFilter = " scanned_By = '" + scannedBy + "' " ;
			}else {
				allFilter = allFilter + " and scanned_By = '" + scannedBy + "' " ;
			}
		}
		 
		if(!allFilter.equals("")) {
			allFilter = " where " + allFilter;
		}
		return allFilter;
	}
	public String generateStatsFilter2(String year, String month, String scannedBy) {
		List tnList;
		String allFilter = "";
	 
		 
		if(!year.equals("All")){
			allFilter = " trim(to_char(book.Date_Loaded,  'yyyy')) = '" + year + "' " ;
		}
		if(!month.equals("All")) {
			if(allFilter.equals("")) {
				allFilter = " trim(to_char(book.Date_Loaded,  'Month')) = '" + month + "' " ;
			}else {
				allFilter = allFilter + " and trim(to_char(book.Date_Loaded,  'Month')) = '" + month + "' " ;
			}
		}
		if(!scannedBy.equals("All")) {
			if(allFilter.equals("")) {
				allFilter = " book.scanned_By = '" + scannedBy + "' " ;
			}else {
				allFilter = allFilter + " and book.scanned_By = '" + scannedBy + "' " ;
			}
		}
		 
		if(!allFilter.equals("")) {
			allFilter = " and " + allFilter;
		}
		return allFilter;
	}
	
	
	@Override 
	public List<List> getStatsFinal(String year, String month,  String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter(year, month, scannedBy);
		
		tnList = getJdbcTemplate().query("SELECT  sum(titles_scanned), sum(pages_scanned), sum(titles_kofaxed), sum(pages_kofaxed), sum(titles_loaded), sum( pages_loaded)  from stats_final " + allFilter , new StringX6RowMapper());
		
		return tnList;

	}
	
	/*these stat method sql was taken from Acess queries - no views generated*/
	@Override
	public List<List> getStatsTn( String year, String month, String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter2(year, month, scannedBy);
		
		tnList = getJdbcTemplate().query("SELECT TN, Date_Loaded " +
				" FROM Book WHERE (Date_Loaded Is Not Null) " + allFilter, new StringX2RowMapper() );
		
		return tnList;
	}
	@Override
	public List<List> getStatsUrlList( String year, String month, String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter2(year, month, scannedBy);
		
		tnList = getJdbcTemplate().query("SELECT TN, title, Date_Loaded, scanned_By, url, pid " +
				" FROM Book WHERE (Date_Loaded Is Not Null) " + allFilter, new StringX6RowMapper() );
		
		return tnList;
	}
	@Override
	public List<List> getStatsTitleList( String year, String month, String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter2(year, month, scannedBy);
		
		tnList = getJdbcTemplate().query("SELECT TN, url, collection, title, author, Date_Loaded " +
				" FROM Book WHERE (Date_Loaded Is Not Null) " + allFilter, new StringX6RowMapper() );
		
		return tnList;
	}
	
	@Override
	public List<List> getStatsGenealogyToday( String year, String month, String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter2(year, month, scannedBy);
		
		tnList = getJdbcTemplate().query("SELECT TN, url, title, author, Date_Loaded " +
				" FROM Book WHERE (Date_Loaded Is Not Null) " + allFilter, new StringX5RowMapper() );
		 

		
		return tnList;
	}
	@Override
	public List<List> getStatsScannedByReport( String year, String month, String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter2(year, month, scannedBy);
		
		tnList = getJdbcTemplate().query("SELECT TN, scanned_by, batch_class, url, pid, title, Date_Loaded " +
				" FROM Book WHERE (Date_Loaded Is Not Null) " + allFilter, new StringX7RowMapper() );
		 

		
		return tnList;
	}
 
	
	
 

	@Override
	public List<List> getStatsMonthlyUrls( String year, String month, String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter2(year, month, scannedBy);
		
		tnList = getJdbcTemplate().query("SELECT TN, url, site, scanned_by " +
				" FROM Book WHERE (Date_Loaded Is Not Null) " + allFilter, new StringX4RowMapper() );
		 
		return tnList;
	} 
	
	@Override
	public List<List> getStatsScannedByTns( ){
		List tnList;
		//String allFilter = generateStatsFilter2("All", "All", site);
		
		tnList = getJdbcTemplate().query("SELECT TN,  Date_Loaded " +
				" FROM scanned_By_tn_report WHERE (Date_Loaded Is Not Null) " , new StringX2RowMapper() );
		 
		return tnList;
	}
	@Override
	public List<List> getStatsTnsThey(String year, String month, String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter2(year, month, scannedBy);
		
		/*stats_site_report SQL: = SELECT Books.TN, Books.[Scanned by], Books.[Batch Class], Books.URL, Books.PID, Books.Title, Books.[Date Loaded]
FROM Books
WHERE (((Year([Date Loaded]))=[Forms]![Stats_Report]![Year]) AND ((Books.[Date Loaded]) Is Not Null) AND ((MonthName(Month([Date Loaded]))) Like [Forms]![Stats_Report]![Month]) AND ((Books.Site) Like [Forms]![Stats_Report]![Site]))
ORDER BY Year([Date Loaded]), Books.[Date Loaded], Month([Date Loaded]);
*/
		
 
		tnList = getJdbcTemplate().query("SELECT scanned_By_tn_report.TN, scanned_By_tn_report.Date_Loaded " +
				" FROM scanned_By_tn_report LEFT JOIN book ON scanned_By_TN_Report.TN=book.TN WHERE book.Date_Loaded Is Not Null  and book.tn Is Null " + allFilter , new StringX2RowMapper() );
		/*SELECT [Site TN Report].TN, [Site TN Report].[Date Loaded]
				FROM [Site TN Report] LEFT JOIN [Stats_Site Report] ON [Site TN Report].TN=[Stats_Site Report].TN
				WHERE ((([Stats_Site Report].TN) Is Null));
*/
		return tnList;
	}
	@Override
	public List<List> getStatsTnsWe( String year, String month, String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter2(year, month, scannedBy);
		
		tnList = getJdbcTemplate().query("SELECT book.TN, book.Date_Loaded, book.title, book.url, book.date_released " +
				" FROM book LEFT JOIN scanned_By_tn_report ON book.TN=scanned_By_tn_Report.TN  " +
				" WHERE (scanned_By_tn_report.tn Is Null) " + allFilter , new StringX5RowMapper() );
		/*SELECT [Stats_Site Report].TN, [Stats_Site Report].[Date Loaded], [Stats_Site Report].Title, [Stats_Site Report].URL, Books.[Date Released]
				FROM ([Stats_Site_Report] LEFT JOIN [Site TN Report] ON [Stats_Site Report].TN = [Site TN Report].TN) INNER JOIN Books ON [Stats_Site Report].TN = Books.TN
				WHERE ((([Site TN Report].TN) Is Null));
*/
		return tnList;
	}
	@Override
	public List<List> getStatsCountPerMonth( String year, String month, String scannedBy){
		List tnList;
		String allFilter = generateStatsFilter2(year, month, scannedBy);
	
		tnList = getJdbcTemplate().query("SELECT  sum(Pages_Online) as num_of_pages, count(tn) as num_of_items,   to_char(Date_Loaded,  'yyyy') as year,  to_char(Date_Loaded,  'Month') as month "
				+ " from book where Date_Loaded Is Not Null "
				+ " GROUP BY to_char(Date_Loaded,  'yyyy') , to_char(Date_Loaded,  'Month') HAVING trim(to_char(Date_Loaded,  'yyyy')) = ?  "   , new StringX4RowMapper(), year);
 /*
		SELECT Sum(Books.[Pages Online]) AS [# of pages], Count(Books.TN) AS [# of items], Year([Date Loaded]) AS [Year], Month([Date Loaded]) AS [Month]
				FROM Books
				WHERE (((Books.[Date Loaded]) Is Not Null))
				GROUP BY Year([Date Loaded]), Month([Date Loaded])
				HAVING (((Year([Date Loaded]))=[Forms]![Stats_Report]![Year]))
				ORDER BY Year([Date Loaded]), Month([Date Loaded]);
*/
		return tnList;
	}
	
 
	@Override
	public List<String> getAllYears(){
		List sList = getJdbcTemplate().query("select year from stats_years", new StringRowMapper());
		return sList;	
	}
	///reports end///
	////locking start////
	@Override
	public String getBookLock(String tn, String operator){
		//temp fix for double submit in FF
		if(tn==null)
			return "";
		
		//1 try inserting
		//2 if fail, then already locked
		//  2.b get operator and time
		boolean alreadyLocked = false;
		try {
			String sql = "INSERT into book_lock (TN, operator, time_locked) " +
					" values (?, ?, current_timestamp) ";
		    getJdbcTemplate().update(sql, tn, operator);
		}catch(Exception e) {
			alreadyLocked = true;
		}
		
		if(alreadyLocked == true) {
			List<List> lock = getJdbcTemplate().query("SELECT TN, operator, time_locked " +
					" FROM book_lock WHERE tn = ? "  , new StringXRowMapper() , tn);
		
			if(lock.get(0).get(1).equals(operator)) {
				//already locked by same operator...ok
				//update timestamp
				String sql = "UPDATE book_lock set time_locked = current_timestamp where tn = ?";
			    getJdbcTemplate().update(sql, tn);
				return null;
			}
			String msg =  lock.get(0).get(1) + "    Locked at: " + lock.get(0).get(2);
			msg += "<br>You may open the book as read-only in the tracking form." +
					"<br>Click the browser back arrow to continue...";
			return msg;
		}
		 
		return null;//flag that lock succeeded 
	}
	
	@Override
	public String checkAndReleaseBookLock(String tn, String operator) {
		 		 
		String sql = "DELETE FROM book_lock WHERE tn=? and operator=?";
		int hadLock = getJdbcTemplate().update(sql, tn, operator);
	  
		if(hadLock != 1) {
			
		
			String msg = "Unable to save changes.  You do not have this book locked.  Lock may have expired, or an administrator may have unlocked this book.\n" +
						"<br>Please re-open book to lock it and then save.  \nPlease note that another operator may have made changes to the book since you last opened it.  \n" +
						"<br><br>(Hint:  If you have numerous changes and do not want redo your changes, you may lock this book by opening book in another browser.  Then click this browser back arrow and then retry save in this browser)" +
						"<br>Click the browser back arrow to continue...";
			return msg;
		}
		 
		return null;//flag that unlock succeeded 
	}
	
	//admin methods
	@Override 
	public List<List> getAllBookLocks() {
		//method returns list of lists for common miscButtonAndTableFormWithCheckBox
		List sList = getJdbcTemplate().query("select TN, operator, time_locked from book_lock", new StringXRowMapper());
		return sList;
	}
	
	@Override 
	public void deleteBookLocks(String lockList) {
		String sql = "DELETE FROM book_lock where tn in ( " + lockList + " ) ";
	    getJdbcTemplate().update(sql);
	}
	
	@Override
	public void unlockOldLocks() {
		//delete older than 24 hours
		String sql = "DELETE FROM book_lock where   time_locked  < current_timestamp -1  ";
	    getJdbcTemplate().update(sql);
	}
	///locking end///
	//xml metadata///
	

	@Override
	public boolean queryXmlMetadataOracle( String tn, String[][] mdValues, String[][] recordValues){
		try{
			//ResultSet rs = s.executeQuery("select title, author, to_char(current_date, 'MM/DD/YYYY'), pages_physical_description, coalesce(b.publish_name , e.publish_name), subject, c.publish_name as language, publisher_original, publication_type as serial, call_#, filmno, dgsno, property_right, num_of_pages, summary, filename from book a  left outer join  site b on (a.owning_institution = b.id )  left outer join site e on (a.scanned_by = e.id )  left outer join  languages c on ( a.language = c.id) where tn = '"+tn+"'");
			List tnList = getJdbcTemplate().query("select title, author, to_char(current_date, 'MM/DD/YYYY'), pages_physical_description, coalesce(b.publish_name , e.publish_name), subject, c.publish_name as language, publisher_original, publication_type as serial, call_#, filmno, dgsno, property_right, num_of_pages, summary, filename from book a  left outer join  site b on (a.owning_institution = b.id )  left outer join site e on (a.scanned_by = e.id )  left outer join  languages c on ( a.language = c.id) where tn = '"+tn+"'", new StringXRowMapper() );
			
			String dateStamp = "";
			String softwareList = "Tesseract; SIP 12.3.0, Rosetta 2.2.1";
			String fileName = "";
			if(tnList.size()>0){
				List<String> row = (List<String>) tnList.get(0);
				//mdValues = new String[37][2];
				//recordValues = new String[8][2];			//put in id attr names and key inner-html in this array
				mdValues[0][0] = "dc:identifier"; mdValues[0][1] = tn;
				mdValues[1][0] = "dc:title"; mdValues[1][1] = row.get(0);
				mdValues[2][0] = "dc:creator"; mdValues[2][1] = row.get(1);
				mdValues[3][0] = "dc:contributor"; mdValues[3][1] = "";
				mdValues[4][0] = "dc:description"; mdValues[4][1] =  row.get(14);
				mdValues[5][0] = "dcterms:hasVersion"; mdValues[5][1] = "Electronic Reproduction";
				mdValues[6][0] = "ldsterms:pubdigital"; mdValues[6][1] = "FamilySearch International";
				dateStamp =  row.get(2);
				mdValues[7][0] = "dc:date"; mdValues[7][1] = dateStamp;
				mdValues[8][0] = "dc:extent"; mdValues[8][1] = row.get(3);
				mdValues[9][0] = "ldsterms:owninst"; mdValues[9][1] = row.get(4); 
				mdValues[10][0] = "dc:subject"; mdValues[10][1] = row.get(5);
				mdValues[11][0] = "dc:language"; mdValues[11][1] = row.get(6);
				mdValues[12][0] = "dc:publisher"; mdValues[12][1] = row.get(7);
				mdValues[13][0] = "dcterms:created"; mdValues[13][1] = "";
				String publicationType = row.get(8);
				String propertyRight = row.get(12);
				String isPartOf = "Family History Archive";
				if("Serial".equals(publicationType)){
					if("Public Domain".equals(propertyRight)){
						//dcterms:isPartOf if Serial protected: Family History Archive - Serials
						isPartOf = "Family History Archive - Serials";
					}else if("Copyright Protected".equals(propertyRight)){
						//dcterms:isPartOf if Serial protected: Family History Archive - Serials - Copyright Protected
						isPartOf = "Family History Archive - Serials - Copyright Protected";
					}
				}else{
					//non-serials
					if("Public Domain".equals(propertyRight)){
						//dcterms:isPartOf: Family History Archive 
						isPartOf = "Family History Archive";
					}else if("Copyright Protected".equals(propertyRight)){
						//dcterms:isPartOf if protected: Family History Archive - Copyright Protected
						isPartOf = "Family History Archive - Copyright Protected";
					}
				}

				mdValues[14][0] = "dcterms:isPartOf"; mdValues[14][1] = isPartOf;
				mdValues[15][0] = "dc:rightsHolder"; mdValues[15][1] = "Refer to document for copyright information";
				mdValues[16][0] = "dc:type"; mdValues[16][1] = "text";
				mdValues[17][0] = "dc:format"; mdValues[17][1] = "text/pdf";
				mdValues[18][0] = "ldsterms:callno"; mdValues[18][1] = row.get(9);
				mdValues[19][0] = "ldsterms:callno2"; mdValues[19][1] = "";
				mdValues[20][0] = "ldsterms:callno3"; mdValues[20][1] = "";
				mdValues[21][0] = "ldsterms:filmno"; mdValues[21][1] =  row.get(10);
				mdValues[22][0] = "ldsterms:dgsno"; mdValues[22][1] = row.get(11);
				mdValues[23][0] = "ldsterms:editor"; mdValues[23][1] = "";
				if("Copyright Protected".equals(propertyRight)){
					mdValues[24][0] = "dcterms:accessRights"; mdValues[24][1] = "Protected";
				}else{
					mdValues[24][0] = "dcterms:accessRights"; mdValues[24][1] = "Public";
				}
				mdValues[25][0] = "dcterms:requires"; mdValues[25][1] = "Internet Connectivity. Worldwide Web browser. Adobe Acrobat reader.";
				mdValues[26][0] = "ldsterms:subinst"; mdValues[26][1] = "FHD";
				mdValues[27][0] = "dcterms:provenance"; mdValues[27][1] = "Digitization of manuscript";
				mdValues[28][0] = "ldsterms:mdentry"; mdValues[28][1] = "";
				mdValues[29][0] = "ldsterms:mdentrydate"; mdValues[29][1] = "";
				mdValues[30][0] = "ldsterms:mdentrytool"; mdValues[30][1] = softwareList;
				mdValues[31][0] = "dc:rights"; mdValues[31][1] = "https://www.familysearch.org/terms";
				mdValues[32][0] = "ldsterms:sirsiid"; mdValues[32][1] = "";
				fileName =  row.get(15);//if needed, but now we just use TN
				//01202014 make file name witout pre/post text
				fileName = tn;
				/*01202014 make file name witout pre/post text
				 if(fileName== null || fileName.equals("")){
					if("Serial".equals(publicationType)){
						if("Copyright Protected".equals(propertyRight)){
							fileName = "CP_SE-" + tn;
						}else{
							fileName = "SE-" + tn;
						}

					}else{
						//non serial
						if("Copyright Protected".equals(propertyRight)){
							fileName = "CP_TN-" + tn;
						}else{
							fileName = "TN-" + tn;
						}
					}
				}*/
				if(fileName.endsWith("pdf") == false){
					fileName = fileName + ".pdf";
				}
				mdValues[33][0] = "ldsterms:filename"; mdValues[33][1] = fileName; 
				mdValues[34][0] = "ldsterms:filesize"; mdValues[34][1] = "";
				mdValues[35][0] = "ldsterms:pagecount"; mdValues[35][1] = row.get(13);
				if("Copyright Protected".equals(propertyRight)){
					mdValues[36][0] = "ldsterms:user"; mdValues[36][1] = "1";
				}else{
					//tag not needed if public
					mdValues[36][0] = null;
				}
				 
			}else{
				return false;//not in oracle
			}


			recordValues[0][0] = "eventIdentifierType"; recordValues[0][1] = "Provenance Event";
			recordValues[1][0] = "eventType"; recordValues[1][1] = "Scan";
			recordValues[2][0] = "eventDescription"; recordValues[2][1] = "Creation of image";
			recordValues[3][0] = "eventDateTime"; recordValues[3][1] = dateStamp;
			recordValues[4][0] = "eventOutcome1"; recordValues[4][1] = "codepending";
			recordValues[5][0] = "eventOutcomeDetail1"; recordValues[5][1] = "Original image capture";
			recordValues[6][0] = "linkingAgentIdentifierType1"; recordValues[6][1] = "Image Capture";
			recordValues[7][0] = "linkingAgentIdentifierValue1"; recordValues[7][1] = softwareList;
			return true;
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}

	}
	///xml metadata end//
	private static class ExtractorWithColumnHeaders implements ResultSetExtractor<List<List<String>>> {
		  @Override
		  public List<List<String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
			  
			  List<List<String>> all = new ArrayList();
			  ResultSetMetaData rsmd = rs.getMetaData();
			  int colCount = rsmd.getColumnCount();
			  List retList = new ArrayList(colCount);

			  
			  for(int x = 1; x<=colCount; x++) {
				  String s = rsmd.getColumnName(x);
				  retList.add(s);
			  }
			  all.add(retList);//add column headers
			  
			  while(rs.next()) {
				  retList = new ArrayList(colCount);
				  for(int x = 1; x<=colCount; x++) {
					  String s = rs.getString(x);
					  retList.add(s);
				  }
				  all.add(retList);//add each row of data
			  }
				 
			  return all;
		  }
	}
	
	
	////locking end////
	
	////start dashboard/////
	public int daysBetweenInclusive(String yS, String mS, String dS, String yE, String mE, String dE){
   
		mS = String.valueOf(Integer.parseInt(mS) - 1); //0 based in Date class
		mE = String.valueOf(Integer.parseInt(mE) - 1);
		final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
		 
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		   //get current date time with Date()
		Calendar startC = new GregorianCalendar(Integer.valueOf(yS) , Integer.valueOf(mS), Integer.valueOf(dS));  
		Calendar endC = new GregorianCalendar(Integer.valueOf(yE) , Integer.valueOf(mE), Integer.valueOf(dE));
		double x = (double)((double)endC.getTimeInMillis() - (double)startC.getTimeInMillis())/ (double)DAY_IN_MILLIS ;//cast to double before doing math or it rounds down even if .95
		int diffInDays = (int)Math.round(x);//next round
		//not work sice .95 rounds down with int cast int diffInDays = (int) ((endC.getTimeInMillis() - startC.getTimeInMillis())/ DAY_IN_MILLIS );//3.10.2014 i think returns x.95... so do a round to nearest int

		return diffInDays + 1;//really inclusive
     
	}
	  

	 public List<String> getDateSlices(String yS, String mS, String dS, int daysBetween, int width) {
		 ArrayList<String> retList = new ArrayList<String>();
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		 
		 if(daysBetween < width)
			 width = daysBetween;
		 
		 int deltaDays = daysBetween / width;
		 int extraDays =  daysBetween % width;
		 Calendar calStart = Calendar.getInstance();
		  
		 calStart.set(Integer.parseInt(yS), Integer.parseInt(mS)-1, Integer.parseInt(dS));//month is 0 based
	  
		 retList.add( dateFormat.format(calStart.getTime()));//first date as always
		  
		 for(int x = 1; x <= width; x++) {
			 calStart.add(Calendar.DATE,  deltaDays);//add delta days to get next slice

			 if(extraDays >= x)
				 calStart.add(Calendar.DATE,  1); //add 1 extra day in this slice
			 
			 if(width == x)
				 calStart.add(Calendar.DATE,  1); //add 1 extra day in this slice for last sql query (make last date exclusive)

			 retList.add( dateFormat.format(calStart.getTime()));
		 }
	
		 
		 return retList;
	 }

	 public List<String> getDateSlicesByMonthly(String yS, String mS, String dS, String yE, String mE, String dE ) {
		 ArrayList<String> retList = new ArrayList<String>();
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
		//up month by 1 since query uses <.
		 if( Integer.parseInt(mE) == 12 ) {
			 mE = "01"; //jan of next year
			 yE = String.valueOf(Integer.parseInt(yE)+1);
		 }else {
			 mE = String.valueOf(Integer.parseInt(mE) +1); //up month by 1 since query uses <
		 }
		  
		 int yearS = Integer.parseInt(yS);
		 int yearE = Integer.parseInt(yE);
		 
		 
		 for(int y = yearS; y<=yearE; y++) {
			 if(y == yearS) {
				 int endMonth = 12;//end month in first year only
				 if(yearS == yearE) {
					 endMonth = Integer.valueOf(mE);
				 }
				 for(int x = Integer.parseInt(mS); x<= endMonth; x++) {
					 String m = String.valueOf(x);
					 if(x < 10)
						 m = "0" + m;
					 retList.add(y + "/" + m + "/" + "01" );//always start and end on 01 since use < in query range
				 }
			 }else if(y == yearE) {
				 //loop one month past since using < in query
				 for(int x = 1; x <= Integer.parseInt(mE); x++) {
					 String m = String.valueOf(x);
					 if(x < 10)
						 m = "0" + m;
					 retList.add(y + "/" + m + "/" + "01" );//always start and end on 01 since use < in query range
				 }
			 }else {
				 //middle full year
				 for(int x = 1; x <= 12; x++) {
					 String m = String.valueOf(x);
					 if(x < 10)
						 m = "0" + m;
					 retList.add(y + "/" + m + "/" + "01" );//always start and end on 01 since use < in query range
				 }
			 }
		 }
	
		 
		 return retList;
	 }
	 
	@Override 
	public String[][] getDashboardAverages( String startDate, String endDate, String site){
	
		String startDateYearFirst, endDateYearFirst;
		
	
			//order for alphabetical ordering of strings
			//startDate
			String yS, mS, dS;
			int i1 = startDate.indexOf("/");
			mS = startDate.substring(0, i1);
			String rem = startDate.substring(i1 + 1 );
			i1 = rem.indexOf("/");
			dS = rem.substring(0, i1);
			rem = rem.substring(i1 + 1 );
			yS = rem;
			if(dS.length()==1)
				dS = "0" + dS;
			if(mS.length()==1)
				mS = "0" + mS;
			
			startDateYearFirst = yS + "/"+ mS + "/" + dS;
			
			
			//order for alphabetical ordering of strings
			//endDate
			String yE, mE, dE;
			i1 = endDate.indexOf("/");
			mE = endDate.substring(0, i1);
			rem = endDate.substring(i1 + 1 );
			i1 = rem.indexOf("/");
			dE = rem.substring(0, i1);
			rem = rem.substring(i1 + 1 );
			yE = rem;
			if(dE.length()==1)
				dE = "0" + dE;
			if(mE.length()==1)
				mE = "0" + mE;
			endDateYearFirst = yE + "/"+ mE + "/" + dE;
		
			
		int daysBetween = daysBetweenInclusive(yS, mS, dS, yE, mE, dE);
		int width = 10;//number of date slices
		int deltaDays = daysBetween / width;
		int extraDays =  daysBetween % width;
		double x =  1.0/((double)deltaDays+1.0);
		double reduction = 1.0 - x;
		//next get date slices (10 for now) make sure even number for start/end matching
		List<String> dates = getDateSlices(yS, mS, dS, daysBetween, width); //daysBetween is inclusive, but returned dates will contain ending date exclusive
		String[][] ret = new String[12][4];//6 fhc and 6 partner libs
		
		//allFhc allPartnerLibs
		if(site == null || site.equals("All Sites"))
		{
			

			//1/////////////////////////////////////////////////
			runQueryForDashboard(dates, startDateYearFirst, endDateYearFirst, "scan_ia_complete_date", "allFhc",   extraDays, reduction, ret, 0);
			ret[0][0] = "Books Scanned";
			ret[1][0] = "Images Scanned"; 



			//2 /////////////////////////////////////////////////////// 
			runQueryForDashboard(dates, startDateYearFirst, endDateYearFirst, "date_released", "allFhc",   extraDays, reduction, ret, 2);
			ret[2][0] = "Books Processed";
			ret[3][0] = "Images Processed"; 


			//3////////////////////////////////////////////////////////
			runQueryForDashboard(dates, startDateYearFirst, endDateYearFirst, "date_loaded", "allFhc",   extraDays, reduction, ret, 4);
			ret[4][0] = "Books Published";
			ret[5][0] = "Images Published"; 
			

			//4/////////////////////////////////////////////////
			runQueryForDashboard(dates, startDateYearFirst, endDateYearFirst, "scan_ia_complete_date", "allPartnerLibs",   extraDays, reduction, ret, 6);
			ret[6][0] = "Books Scanned";
			ret[7][0] = "Images Scanned"; 



			//5 /////////////////////////////////////////////////////// 
			runQueryForDashboard(dates, startDateYearFirst, endDateYearFirst, "date_released", "allPartnerLibs",   extraDays, reduction, ret, 8);
			ret[8][0] = "Books Processed";
			ret[9][0] = "Images Processed"; 


			//6////////////////////////////////////////////////////////
			runQueryForDashboard(dates, startDateYearFirst, endDateYearFirst, "date_loaded", "allPartnerLibs",   extraDays, reduction, ret, 10);
			ret[10][0] = "Books Published";
			ret[11][0] = "Images Published"; 

		}else {
			
			//1/////////////////////////////////////////////////
			runQueryForDashboard(dates, startDateYearFirst, endDateYearFirst, "scan_ia_complete_date", site,   extraDays, reduction, ret, 0);
			ret[0][0] = "Books Scanned";
			ret[1][0] = "Images Scanned"; 


			//2 /////////////////////////////////////////////////////// 
			runQueryForDashboard(dates, startDateYearFirst, endDateYearFirst, "date_released", site,   extraDays, reduction, ret, 2);
			ret[2][0] = "Books Processed";
			ret[3][0] = "Images Processed"; 


			//3////////////////////////////////////////////////////////
			runQueryForDashboard(dates, startDateYearFirst, endDateYearFirst, "date_loaded", site,   extraDays, reduction, ret, 4);
			ret[4][0] = "Books Published";
			ret[5][0] = "Images Published"; 
			

			//4/////////////////////////////////////////////////
			//for now right side is duplicat of left when selecting a site
			ret[6][0] = ret[0][0];
			ret[6][1] = ret[0][1];
			ret[6][2] = ret[0][2];
			ret[6][3] = ret[0][3];
			ret[7][0] = ret[1][0];
			ret[7][1] = ret[1][1];
			ret[7][2] = ret[1][2];
			ret[7][3] = ret[1][3];
			ret[8][0] = ret[2][0];
			ret[8][1] = ret[2][1];
			ret[8][2] = ret[2][2];
			ret[8][3] = ret[2][3];
			ret[9][0] = ret[3][0];
			ret[9][1] = ret[3][1];
			ret[9][2] = ret[3][2];
			ret[9][3] = ret[3][3];
			ret[10][0] = ret[4][0];
			ret[10][1] = ret[4][1];
			ret[10][2] = ret[4][2];
			ret[10][3] = ret[4][3];
			ret[11][0] = ret[5][0];
			ret[11][1] = ret[5][1];
			ret[11][2] = ret[5][2];
			ret[11][3] = ret[5][3];
			 
		}
		
		return ret;
	}
	

	
	 
	@Override 
	public String[][] getDashboardQualityAverages( String startDate, String endDate, String site){
	
		String startDateYearFirst, endDateYearFirst;
		
	
		//order for alphabetical ordering of strings
		//startDate
		String yS, mS, dS;
		int i1 = startDate.indexOf("/");
		mS = startDate.substring(0, i1);
		String rem = startDate.substring(i1 + 1 );
		i1 = rem.indexOf("/");
		dS = rem.substring(0, i1);
		rem = rem.substring(i1 + 1 );
		yS = rem;
		if(dS.length()==1)
			dS = "0" + dS;
		if(mS.length()==1)
			mS = "0" + mS;
		
		startDateYearFirst = yS + "/"+ mS + "/" + dS;
		
		
		//order for alphabetical ordering of strings
		//endDate
		String yE, mE, dE;
		i1 = endDate.indexOf("/");
		mE = endDate.substring(0, i1);
		rem = endDate.substring(i1 + 1 );
		i1 = rem.indexOf("/");
		dE = rem.substring(0, i1);
		rem = rem.substring(i1 + 1 );
		yE = rem;
		if(dE.length()==1)
			dE = "0" + dE;
		if(mE.length()==1)
			mE = "0" + mE;
		endDateYearFirst = yE + "/"+ mE + "/" + dE;
	
			
		int daysBetween = daysBetweenInclusive(yS, mS, dS, yE, mE, dE);
		int width = 10;//number of date slices
		int deltaDays = daysBetween / width;
		int extraDays =  daysBetween % width;
		double x =  1.0/((double)deltaDays+1.0);
		double reduction = 1.0 - x;
		//next get date slices (10 for now) make sure even number for start/end matching
		List<String> dates = getDateSlices(yS, mS, dS, daysBetween, width); //daysBetween is inclusive, but returned dates will contain ending date exclusive
		String[][] ret = new String[6][4];//6 fhc and 6 partner libs
		
		//allFhc allPartnerLibs
		if(site == null || site.equals("All Sites"))
		{
			
	
			//1/////////////////////////////////////////////////
			runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, "allFhc", "a.status = 'Problem'",  extraDays, reduction, ret, 0);
			ret[0][0] = "Books Failed";
	
			runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, "allFhc", "a.status = 'Problem Fixed'" ,extraDays, reduction, ret, 1);
			ret[1][0] = "Books reworked"; 
			
			 
			runQueryForQualityRepublishedDashboard(dates, startDateYearFirst, endDateYearFirst, "allFhc",   extraDays, reduction, ret, 2);
			ret[2][0] = "Books republished";
			//ret[2][1] = "?"; //dummy vals until figure out how to get this in runQueryForQualityDashboard()
			//ret[2][2] = "[0]";
			//ret[2][3] = "equal";
			
			runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, "allPartnerLibs", "a.status = 'Problem' ", extraDays, reduction, ret, 3);
			ret[3][0] = "Books Failed";
	
			runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, "allPartnerLibs", "a.status = 'Problem Fixed' ",  extraDays, reduction, ret, 4);
			ret[4][0] = "Books reworked"; 
			
			//books republished 
			runQueryForQualityRepublishedDashboard(dates, startDateYearFirst, endDateYearFirst, "allPartnerLibs",   extraDays, reduction, ret, 5);
			ret[5][0] = "Books republished";
			//ret[5][1] = "?"; //dummy vals until figure out how to get this in runQueryForQualityDashboard()
			//ret[5][2] = "[0]";
			//ret[5][3] = "equal";
	 
		}else {
					//1/////////////////////////////////////////////////
			runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, site,    "a.status = 'Problem' ", extraDays, reduction, ret, 0);
			ret[0][0] = "Books Failed";
	
			runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, site, "a.status = 'Problem Fixed' ", extraDays, reduction, ret, 1);
			ret[1][0] = "Books reworked"; 
			
			//books republished runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, site,   extraDays, reduction, ret, 2);
			runQueryForQualityRepublishedDashboard(dates, startDateYearFirst, endDateYearFirst, site,   extraDays, reduction, ret, 2);
			ret[2][0] = "Books republished";
			//ret[2][1] = "?"; //dummy vals until figure out how to get this in runQueryForQualityDashboard()
			//ret[2][2] = "[0]";
			//ret[2][3] = "equal";
			
			runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, site,    "a.status = 'Problem' ", extraDays, reduction, ret, 3);
			ret[3][0] = "Books Failed";
	
			runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, site,   "a.status = 'Problem Fixed' ",   extraDays, reduction, ret, 4);
			ret[4][0] = "Books reworked"; 
			
			//books republished runQueryForQualityDashboard(dates, startDateYearFirst, endDateYearFirst, site,   extraDays, reduction, ret, 5);
			runQueryForQualityRepublishedDashboard(dates, startDateYearFirst, endDateYearFirst, site,   extraDays, reduction, ret, 5);
			ret[5][0] = "Books republished";
			//ret[5][1] = "?"; //dummy vals until figure out how to get this in runQueryForQualityDashboard()
			//ret[5][2] = "[0]";
			//ret[5][3] = "equal";
	 
		}
				
		return ret;
	}
	 
	@Override 
	public String[][] getDashboardAuditor( String startDate, String endDate, String site){
	
		String startDateYearFirst, endDateYearFirst;
		
	
			//order for alphabetical ordering of strings
			//startDate
			String yS, mS, dS;
			int i1 = startDate.indexOf("/");
			mS = startDate.substring(0, i1);
			String rem = startDate.substring(i1 + 1 );
			i1 = rem.indexOf("/");
			dS = rem.substring(0, i1);
			rem = rem.substring(i1 + 1 );
			yS = rem;
			if(dS.length()==1)
				dS = "0" + dS;
			if(mS.length()==1)
				mS = "0" + mS;
			
			startDateYearFirst = yS + "/"+ mS + "/" + dS;
			
			
			//order for alphabetical ordering of strings
			//endDate
			String yE, mE, dE;
			i1 = endDate.indexOf("/");
			mE = endDate.substring(0, i1);
			rem = endDate.substring(i1 + 1 );
			i1 = rem.indexOf("/");
			dE = rem.substring(0, i1);
			rem = rem.substring(i1 + 1 );
			yE = rem;
			if(dE.length()==1)
				dE = "0" + dE;
			if(mE.length()==1)
				mE = "0" + mE;
			endDateYearFirst = yE + "/"+ mE + "/" + dE;
		
			
		int daysBetween = daysBetweenInclusive(yS, mS, dS, yE, mE, dE);
		int width = 10;//number of date slices
		int deltaDays = daysBetween / width;
		int extraDays =  daysBetween % width;
		double x =  1.0/((double)deltaDays+1.0);
		double reduction = 1.0 - x;
		//next get date slices (10 for now) make sure even number for start/end matching
		List<String> dates = getDateSlices(yS, mS, dS, daysBetween, width); //daysBetween is inclusive, but returned dates will contain ending date exclusive
		String[][] ret = new String[12][4];//6 fhc and 6 partner libs
		
		//allFhc allPartnerLibs
		if(site == null || site.equals("All Sites"))
		{			

			//1/////////////////////////////////////////////////
			runQueryForAuditor(dates, startDateYearFirst, endDateYearFirst, "scan_ia_complete_date", "scan_image_auditor", "all",   extraDays, reduction, ret, 0);
			ret[0][0] = "Books Audited (scan)";
			ret[1][0] = "Images Audited (scan)"; 

			//2 /////////////////////////////////////////////////////// 
			runQueryForAuditor(dates, startDateYearFirst, endDateYearFirst, "ia_complete_date", "image_audit", "all",   extraDays, reduction, ret, 2);
			ret[2][0] = "Books Audited (process)";
			ret[3][0] = "Images Audited (process)"; 
		}else {
			
			//1/////////////////////////////////////////////////
			runQueryForAuditor(dates, startDateYearFirst, endDateYearFirst, "scan_ia_complete_date", "scan_image_auditor", site,   extraDays, reduction, ret, 0);
			ret[0][0] = "Books Audited (scan)";
			ret[1][0] = "Images Audited (scan)"; 

			//2 /////////////////////////////////////////////////////// 
			runQueryForAuditor(dates, startDateYearFirst, endDateYearFirst, "ia_complete_date", "image_audit", site,   extraDays, reduction, ret, 2);
			ret[2][0] = "Books Audited (process)";
			ret[3][0] = "Images Audited (process)"; 
		}
		
		return ret;
	}
	
	@Override 
	public String[][] getDashboardTop5( String startDate, String endDate, String site){
	
		String startDateYearFirst, endDateYearFirst;
		
	
		//order for alphabetical ordering of strings
		//startDate
		String yS, mS, dS;
		int i1 = startDate.indexOf("/");
		mS = startDate.substring(0, i1);
		String rem = startDate.substring(i1 + 1 );
		i1 = rem.indexOf("/");
		dS = rem.substring(0, i1);
		rem = rem.substring(i1 + 1 );
		yS = rem;
		if(dS.length()==1)
			dS = "0" + dS;
		if(mS.length()==1)
			mS = "0" + mS;
		
		startDateYearFirst = yS + "/"+ mS + "/" + dS;
		
		
		//order for alphabetical ordering of strings
		//endDate
		String yE, mE, dE;
		i1 = endDate.indexOf("/");
		mE = endDate.substring(0, i1);
		rem = endDate.substring(i1 + 1 );
		i1 = rem.indexOf("/");
		dE = rem.substring(0, i1);
		rem = rem.substring(i1 + 1 );
		yE = rem;
		if(dE.length()==1)
			dE = "0" + dE;
		if(mE.length()==1)
			mE = "0" + mE;
		endDateYearFirst = yE + "/"+ mE + "/" + dE;
	
			
		int daysBetween = daysBetweenInclusive(yS, mS, dS, yE, mE, dE);
		int width = 10;//number of date slices
		int deltaDays = daysBetween / width;
		int extraDays =  daysBetween % width;
		double x =  1.0/((double)deltaDays+1.0);
		double reduction = 1.0 - x;
		//next get date slices (10 for now) make sure even number for start/end matching
		List<String> dates = getDateSlices(yS, mS, dS, daysBetween, width); //daysBetween is inclusive, but returned dates will contain ending date exclusive
		String[][] ret = new String[6][4]; 
		
		//allFhc allPartnerLibs
		if(site == null || site.equals("All Sites"))
		{
			runQueryForTopDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "all", extraDays, reduction, ret);
		}else {
		
			runQueryForTopDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, site, extraDays, reduction, ret);
		}
		return ret;
	}
	
	 
	@Override 
	public String[][] getDashboardAgedAverages( String startDate, String endDate, String site){
	
		String startDateYearFirst, endDateYearFirst;
		
	
		//order for alphabetical ordering of strings
		//startDate
		String yS, mS, dS;
		int i1 = startDate.indexOf("/");
		mS = startDate.substring(0, i1);
		String rem = startDate.substring(i1 + 1 );
		i1 = rem.indexOf("/");
		dS = rem.substring(0, i1);
		rem = rem.substring(i1 + 1 );
		yS = rem;
		if(dS.length()==1)
			dS = "0" + dS;
		if(mS.length()==1)
			mS = "0" + mS;
		
		startDateYearFirst = yS + "/"+ mS + "/" + dS;
		
		
		//order for alphabetical ordering of strings
		//endDate
		String yE, mE, dE;
		i1 = endDate.indexOf("/");
		mE = endDate.substring(0, i1);
		rem = endDate.substring(i1 + 1 );
		i1 = rem.indexOf("/");
		dE = rem.substring(0, i1);
		rem = rem.substring(i1 + 1 );
		yE = rem;
		if(dE.length()==1)
			dE = "0" + dE;
		if(mE.length()==1)
			mE = "0" + mE;
		endDateYearFirst = yE + "/"+ mE + "/" + dE;
	
			
		int daysBetween = daysBetweenInclusive(yS, mS, dS, yE, mE, dE);
		int width = 10;//number of date slices
		int deltaDays = daysBetween / width;//this is num days in each time slice
		int extraDays =  daysBetween % width;
		double x =  1.0/((double)deltaDays+1.0);//delta days plus the 1 extra day in this period
		double reduction = 1.0 - x;
		//next get date slices (10 for now) make sure even number for start/end matching
		List<String> dates = getDateSlices(yS, mS, dS, daysBetween, width); //daysBetween is inclusive, but returned dates will contain ending date exclusive
		String[][] ret = new String[5][4]; 
		
		//allFhc allPartnerLibs
		if(site == null || site.equals("All Sites"))
		{
	
			//1/////////////////////////////////////////////////
			//between bookmetadata start and bookmetadata end
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "bookmetadata", "date_added", "sent_to_scan", "all",   extraDays, reduction, ret, 0);
			ret[0][0] = "Phase 1 (book pull/metadata)";//catalog metadata
	
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "book", "scan_start_date", "scan_ia_complete_date", "all", extraDays, reduction, ret, 1);
			ret[1][0] = "Phase 2 (scan)"; //scan
			
			 
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "book", "files_received_by_orem", "date_released", "all",   extraDays, reduction, ret, 2);
			ret[2][0] = "Phase 3 (ocr processing)";//orem ocr
			
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "book", "date_released", "date_loaded", "all",   extraDays, reduction, ret, 3);
			ret[3][0] = "Phase 4 (publish)";//publish-load

			
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "bookmetadata book", "date_added", "date_loaded", "all", extraDays, reduction, ret, 4);
			ret[4][0] = "Phase 1-4 (all)";
			ret[4][1] = String.format("%.3f", Double.parseDouble(ret[0][1]) + Double.parseDouble(ret[1][1]) + Double.parseDouble(ret[2][1]) + Double.parseDouble(ret[3][1]));
			ret[4][2] = addStringArrays(ret[0][2], ret[1][2], ret[2][2], ret[3][2]);
			
			String tmp0 = ret[4][2].substring(1, ret[4][2].length()-1);
			StringTokenizer st = new StringTokenizer(tmp0, ",");
			String firstVal = st.nextToken().trim();
			String lastVal = "";
			while(st.hasMoreTokens()) {
				lastVal = st.nextToken().trim();
			}
			ret[4][3] = calculateSlope(firstVal, lastVal);
	 
		}else {
			//1/////////////////////////////////////////////////
			//between bookmetadata start and bookmetadata end
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "bookmetadata", "date_added", "sent_to_scan", site,   extraDays, reduction, ret, 0);
			ret[0][0] = "Phase 1 (book pull/metadata)";//catalog metadata
	
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "book", "scan_start_date", "scan_ia_complete_date", site, extraDays, reduction, ret, 1);
			ret[1][0] = "Phase 2 (scan)"; //scan
			
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "book", "files_received_by_orem", "date_released", site,   extraDays, reduction, ret, 2);
			ret[2][0] = "Phase 3 (ocr processing)";//orem ocr
			
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "book", "date_released", "date_loaded", site,   extraDays, reduction, ret, 3);
			ret[3][0] = "Phase 4 (publish)";//publish-load

			
			runQueryForAgedDashboard(daysBetween, dates, startDateYearFirst, endDateYearFirst, "bookmetadata book", "date_added", "date_loaded", site, extraDays, reduction, ret, 4);
			ret[4][0] = "Phase 1-4 (all)";
			ret[4][1] = String.format("%.3f", Double.parseDouble(ret[0][1]) + Double.parseDouble(ret[1][1]) + Double.parseDouble(ret[2][1]) + Double.parseDouble(ret[3][1]));
			ret[4][2] = addStringArrays(ret[0][2], ret[1][2], ret[2][2], ret[3][2]);
			
			String tmp0 = ret[4][2].substring(1, ret[4][2].length()-1);
			StringTokenizer st = new StringTokenizer(tmp0, ",");
			String firstVal = st.nextToken().trim();
			String lastVal = "";
			while(st.hasMoreTokens()) {
				lastVal = st.nextToken().trim();
			}
			ret[4][3] = calculateSlope(firstVal, lastVal);
	 
		}
				
		return ret;
	}
	

	@Override 
	public String[][] getDashboardGoalData( String startDate, String endDate, String site){
		//model.addAttribute("goalsLabels", "[ \"3/4\", \"February\", \"March\", \"April\", \"May\", \"une\", \"July\" ]");
				//model.addAttribute("goals","[ 65, 59, 90, 81, 56, 55, 40 ]");
				//model.addAttribute("actuals","[ 23, 59, 10, 81, 56, 5, 40 ]");
		String startDateYearFirst, endDateYearFirst;
		
	
			//order for alphabetical ordering of strings
			//startDate
			String yS, mS, dS;
			int i1 = startDate.indexOf("/");
			mS = startDate.substring(0, i1);
			String rem = startDate.substring(i1 + 1 );
			i1 = rem.indexOf("/");
			dS = rem.substring(0, i1);
			rem = rem.substring(i1 + 1 );
			yS = rem;
			if(dS.length()==1)
				dS = "0" + dS;
			if(mS.length()==1)
				mS = "0" + mS;
			
			startDateYearFirst = yS + "/"+ mS + "/" + dS;
			
			
			//order for alphabetical ordering of strings
			//endDate
			String yE, mE, dE;
			i1 = endDate.indexOf("/");
			mE = endDate.substring(0, i1);
			rem = endDate.substring(i1 + 1 );
			i1 = rem.indexOf("/");
			dE = rem.substring(0, i1);
			rem = rem.substring(i1 + 1 );
			yE = rem;
			if(dE.length()==1)
				dE = "0" + dE;
			if(mE.length()==1)
				mE = "0" + mE;
			endDateYearFirst = yE + "/"+ mE + "/" + dE;
		
			
		int daysBetween = daysBetweenInclusive(yS, mS, dS, yE, mE, dE);
		int width = 10;//number of date slices
		int deltaDays = daysBetween / width;
		int extraDays =  daysBetween % width;
		double x =  1.0/((double)deltaDays+1.0);
		double reduction = 1.0 - x;
		//next get date slices (10 for now) make sure even number for start/end matching
		List<String> dates = getDateSlicesByMonthly(yS, mS, dS, yE, mE, dE); //daysBetween is inclusive, but returned dates will contain ending date exclusive (bumped up to the next month)
		String[][] ret = new String[1][3];//labels, goals, actuals (monthly)
		
		//allFhc allPartnerLibs
		if(site == null || site.equals("All Sites"))
		{			
			//1/////////////////////////////////////////////////
			runQueryForGoals(dates, startDateYearFirst, endDateYearFirst, "all", ret, 0);

		}else {
			
			//1/////////////////////////////////////////////////
			runQueryForGoals(dates, startDateYearFirst, endDateYearFirst, site, ret, 0);
		}
		
		//in meeting decided to make chart a comulative graph.  (ie goal is a diagonal line from 0 to x and results will "s" around the diagonal line)
		ret[0][1] =  makeComulitive(ret[0][1]);
		ret[0][2] =  makeComulitive(ret[0][2]);
		 
		return ret;
	}
	
	public String makeComulitive(String arrayOfNumbers) {
		arrayOfNumbers = arrayOfNumbers.substring(1 );//trim off []
		arrayOfNumbers = arrayOfNumbers.substring(0, arrayOfNumbers.length()-1);//trim off []
		StringTokenizer st = new StringTokenizer(arrayOfNumbers, ",");
		
		String newArray = "[";
		int total = 0;
		while(st.hasMoreTokens()) {
			String val = st.nextToken();
			val = val.trim();
			if(val.equals(""))
				break;
			int iVal = Integer.parseInt(val);
			iVal += total;
			total = iVal;
			newArray = newArray + String.valueOf(iVal) + ",";
		}
		newArray = newArray.substring(0, newArray.length() - 1) + "]";
		return newArray;
	}
	
	public String addStringArrays(String a0, String a1, String a2, String a3) {
		a0 = a0.substring(1, a0.length()-1);
		a1 = a1.substring(1, a1.length()-1);
		a2 = a2.substring(1, a2.length()-1);
		a3 = a3.substring(1, a3.length()-1);
		
		int count = 0;
		int[] ai0 = new int[20];
		int[] ai1 = new int[20];
		int[] ai2 = new int[20];
		int[] ai3 = new int[20];
		StringTokenizer st0 = new StringTokenizer(a0, ",");
		StringTokenizer st1 = new StringTokenizer(a1, ",");
		StringTokenizer st2 = new StringTokenizer(a2, ",");
		StringTokenizer st3 = new StringTokenizer(a3, ",");
		int x = 0;
		while(st0.hasMoreTokens()) {
			ai0[x] = Integer.parseInt(st0.nextToken().trim());
			x++;
			count++;
		}
		x = 0;
		while(st1.hasMoreTokens()) {
			ai1[x] = Integer.parseInt(st1.nextToken().trim());
			x++;
		}
		x = 0;
		while(st2.hasMoreTokens()) {
			ai2[x] = Integer.parseInt(st2.nextToken().trim());
			x++;
		}
		x = 0;
		while(st3.hasMoreTokens()) {
			ai3[x] = Integer.parseInt(st3.nextToken().trim());
			x++;
		}
		
		String ret = "[";
		for(x = 0; x<count;x++) {
			ret = ret + String.valueOf(ai0[x] + ai1[x] + ai2[x] + ai3[x]) + ", ";
		}
		ret = ret.substring(0, ret.length() - 2) + "]";
		return ret;
	}
	 
	public String calculateSlope(String firstPeriodCount, String lastPeriodCount) {
		int f = Integer.valueOf(firstPeriodCount);
		int l = Integer.valueOf(lastPeriodCount);
		
		if (f < l)
			return "up";
		else if (f > l)
			return "down";
		else
			return "equal";
					
	}
	
	
	public void runQueryForDashboard(List<String> dates, String startDateYearFirst, String endDateYearFirst, String colName, String site, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		

		List<List> vals = null;
		String arrayStrImages = "[";
		String arrayStrTns = "[";
	 
		//Iterator<String> i = dates.iterator();
		//String queryS = i.next();
		//String queryE = null;
		
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
		
		String pagesColumn = "num_of_pages";
		if(colName.equalsIgnoreCase("Date_loaded")) {
			pagesColumn = "pages_online";
		}else if(colName.equalsIgnoreCase("scan_ia_complete_date")) {
			pagesColumn = "scan_num_of_pages";
		}
		
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";

		if(site.equals("allFhc")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_fhc = 'T' ");
			vals = getJdbcTemplate().query("SELECT tn,  "+pagesColumn+", to_char("+colName+ ", 'yyyy/mm/dd') from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_fhc = 'T' order by to_char("+colName+ ", 'yyyy/mm/dd')", new StringX3RowMapper());
		}else if (site.equals("allPartnerLibs")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' ");
			vals = getJdbcTemplate().query("SELECT tn, "+pagesColumn+", to_char("+colName+ ", 'yyyy/mm/dd') from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' order by to_char("+colName+ ", 'yyyy/mm/dd')", new StringX3RowMapper());
		}else{
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?");
			vals = getJdbcTemplate().query("SELECT tn, "+pagesColumn+", to_char("+colName+ ", 'yyyy/mm/dd')  from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ? order by to_char("+colName+ ", 'yyyy/mm/dd')", new StringX3RowMapper(), site);
		}
		
		Iterator<List> rows = vals.iterator();
		List<String> row = null;
		if(rows.hasNext())
			row = rows.next();
		
		Iterator i = dates.iterator();
		i.next();//ok to skip first since it is covered in the query
		int tnCountTotalAll = 0;//total for all time slices
		int imgCountTotalAll = 0;//total for all time slices
		while(i.hasNext()) {
			String queryE = (String)i.next(); 
			int imgCountTotal = 0;//total per time slice
			int tnCountTotal = 0;//total per time slice

			  
			do {
				
				if(row != null && row.get(2).compareTo(queryE) < 0) {
					//matched in this time slice
					//get chart array here..
					//calculate and reduction (averaging) if needed
					String tn = (String)row.get(0);
					String pageCountStr = (String)row.get(1);
					if(pageCountStr == null)
						pageCountStr = "0";
					tnCountTotal += 1;
					int pageCount = Integer.valueOf(pageCountStr);
					imgCountTotal += pageCount;
				}else {
					break;
				}
				
				if(rows.hasNext())
					row = rows.next();
				else
					row = null;
				
			}while(row!=null);
			
			tnCountTotalAll += tnCountTotal;
			imgCountTotalAll += imgCountTotal;
			String tnCountStr = String.valueOf(tnCountTotal);	
			String imgCountStr = String.valueOf(imgCountTotal);	
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCountTotal !=1) ) {
				tnCountTotal = (int)(tnCountTotal * reduction);
				tnCountStr = String.valueOf(tnCountTotal);	
				imgCountTotal = (int)(imgCountTotal * reduction);
				imgCountStr = String.valueOf(imgCountTotal);
				reductionCount++;
			} 

			arrayStrTns += tnCountStr + ", ";
			arrayStrImages += imgCountStr + ", ";
	
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
				firstPeriodImgCount = imgCountStr;
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount
			lastPeriodImgCount = imgCountStr;
		}
		
			
		arrayStrImages = arrayStrImages.substring(0,arrayStrImages.length()-2) + "]";//remove comma and finalize array string
		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
		
		 
	 
		
		 //ret[0][0] = "Books Scanned";
	 
		 ret[arrayIndex][1] = String.valueOf(tnCountTotalAll);
		 ret[arrayIndex][2] = arrayStrTns;
		 ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);
		 
		 //ret[1][0] = "Images Scanned"; 
		 ret[arrayIndex+1][1] = String.valueOf(imgCountTotalAll);
		 ret[arrayIndex+1][2] = arrayStrImages;
		 ret[arrayIndex+1][3] = calculateSlope(firstPeriodImgCount, lastPeriodImgCount);
		 
	}

	
	public void oldrunQueryForDashboard(List<String> dates, String startDateYearFirst, String endDateYearFirst, String colName, String site, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		

		List<List> vals = null;
		String arrayStrImages = "[";
		String arrayStrTns = "[";
	 
		Iterator<String> i = dates.iterator();
		String queryS = i.next();
		String queryE = null;
		
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
		
		String pagesColumn = "num_of_pages";
		if(colName.equalsIgnoreCase("Date_loaded")) {
			pagesColumn = "pages_online";
		}else if(colName.equalsIgnoreCase("scan_ia_complete_date")) {
			pagesColumn = "scan_num_of_pages";
		}
		
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";
		while(i.hasNext()){
			queryE = i.next();
			if(site.equals("allFhc")) {
				allSql += "SELECT count(tn),  sum("+pagesColumn+") from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName + ", 'yyyy/mm/dd') < '" + queryE + "' and b.id = a.scanned_by and b.is_fhc = 'T' union all ";
			}else if (site.equals("allPartnerLibs")) {
				allSql += "SELECT count(tn),  sum("+pagesColumn+") from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName + ", 'yyyy/mm/dd') < '" + queryE + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' union all ";
			}else{
				allSql += "SELECT count(tn),  sum("+pagesColumn+") from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName + ", 'yyyy/mm/dd') < '" + queryE + "'  and scanned_by = '" + site + "' union all ";
			}
			queryS = queryE;
		
		
		}

		allSql = allSql.substring(0, allSql.length() - 10);
		vals = getJdbcTemplate().query(allSql, new StringX2RowMapper());
		for(List<String> row: vals) {
			//get chart array here..
		
			//calculate and reduction (averaging) if needed
			String tnCountStr = (String)row.get(0);
			String imgCountStr = (String)row.get(1);
			if(imgCountStr == null)
				imgCountStr = "0";
		 
			int tnCount =  Integer.valueOf(tnCountStr);
			int imageCount =  Integer.valueOf(imgCountStr);
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCount != 1) ) {
				tnCount = (int)(tnCount * reduction);
				imageCount = (int)(imageCount * reduction);
				tnCountStr = String.valueOf(tnCount);
				imgCountStr = String.valueOf(imageCount);
				reductionCount++;
			} 
		
			arrayStrTns += tnCountStr + ", ";
			arrayStrImages += imgCountStr + ", ";
	
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
				firstPeriodImgCount = imgCountStr;
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount
			lastPeriodImgCount = imgCountStr;
		}
		
			
		arrayStrImages = arrayStrImages.substring(0,arrayStrImages.length()-2) + "]";//remove comma and finalize array string
		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
		
		
		//get total counts
		if(site.equals("allFhc")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_fhc = 'T' ");
			vals = getJdbcTemplate().query("SELECT count(tn),  sum("+pagesColumn+") from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_fhc = 'T' ", new StringX2RowMapper());
		}else if (site.equals("allPartnerLibs")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' ");
			vals = getJdbcTemplate().query("SELECT count(tn),  sum("+pagesColumn+") from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' ", new StringX2RowMapper());
		}else{
	//System.out.println("SELECT count(tn),  sum(num_of_pages) from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?");
			vals = getJdbcTemplate().query("SELECT count(tn),  sum("+pagesColumn+") from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?", new StringX2RowMapper(), site);
		}
	
		 
	
	 
		
		 //ret[0][0] = "Books Scanned";
	 
		 ret[arrayIndex][1] = (String) vals.get(0).get(0);
		 ret[arrayIndex][2] = arrayStrTns;
		 ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);
		 
		 //ret[1][0] = "Images Scanned"; 
		 ret[arrayIndex+1][1] = (String) vals.get(0).get(1);
		 if(ret[arrayIndex+1][1] == null)
			 ret[arrayIndex+1][1] = "0";
		 ret[arrayIndex+1][2] = arrayStrImages;
		 ret[arrayIndex+1][3] = calculateSlope(firstPeriodImgCount, lastPeriodImgCount);
		 
		 
		 
	}

	
	public void runQueryForQualityDashboard(List<String> dates, String startDateYearFirst, String endDateYearFirst,   String site, String filterStr, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		

		List<List> vals = null;
		String arrayStrImages = "[";
		String arrayStrTns = "[";
	  
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
		
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";
		if(site.equals("allFhc")) {
			vals = getJdbcTemplate().query("SELECT a.tn,  to_char(problem_date, 'yyyy/mm/dd') from tf_notes a, book b, site c where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and " + filterStr + " and  a.tn = b.tn and    b.scanned_by=c.id and c.is_fhc = 'T' order by  to_char(problem_date, 'yyyy/mm/dd') ", new StringX2RowMapper());
			
		}else if (site.equals("allPartnerLibs")) {
 
			vals = getJdbcTemplate().query("SELECT a.tn,  to_char(problem_date, 'yyyy/mm/dd') from tf_notes a, book b, site c where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and "+ filterStr +"  and  a.tn = b.tn and    b.scanned_by=c.id and c.is_partner_institution = 'T' order by  to_char(problem_date, 'yyyy/mm/dd') ", new StringX2RowMapper());
			
		}else{
		 	vals = getJdbcTemplate().query("SELECT a.tn, to_char(problem_date, 'yyyy/mm/dd') from tf_notes a, book b where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and "+filterStr+" and  a.tn = b.tn and b.scanned_by=? order by  to_char(problem_date, 'yyyy/mm/dd')", new StringX2RowMapper(), site);
		//System.out.println("SELECT count(a.tn) from tf_notes a, book b where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and "+filterStr+" and  a.tn = b.tn and b.scanned_by=? ");
			
		}
		Iterator<List> rows = vals.iterator();
		List<String> row = null;
		if(rows.hasNext())
			row = rows.next();
		
		Iterator i = dates.iterator();
		i.next();//ok to skip first since it is covered in the query
		int tnCountTotalAll = 0;
		while(i.hasNext()) {
			String queryE = (String)i.next(); 
			int tnCountTotal = 0;

			String tnCountStr = null;
			do {
				
				if(row != null && row.get(1).compareTo(queryE) < 0) {
					//matched in this time slice
					//get chart array here..
					//calculate and reduction (averaging) if needed
					String tn = (String)row.get(0);
					tnCountTotal += 1;
				}else {
					break;
				}
				
				if(rows.hasNext())
					row = rows.next();
				else
					row = null;
				
			}while(rows.hasNext());
			
			tnCountTotalAll += tnCountTotal;
			tnCountStr = String.valueOf(tnCountTotal);	
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCountTotal !=1) ) {
				tnCountTotal = (int)(tnCountTotal * reduction);
				tnCountStr = String.valueOf(tnCountTotal);	
				reductionCount++;
			} 
			arrayStrTns += tnCountStr + ", ";
			 
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount 
		}
		

		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
		
		 //ret[0][0] = "Books failed";
	 
		 ret[arrayIndex][1] = String.valueOf(tnCountTotalAll);
		 ret[arrayIndex][2] = arrayStrTns;
		 ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);
		 
	}

	public void oldrunQueryForQualityDashboard(List<String> dates, String startDateYearFirst, String endDateYearFirst,   String site, String filterStr, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		

		List<List> vals = null;
		String arrayStrImages = "[";
		String arrayStrTns = "[";
	 
		Iterator<String> i = dates.iterator();
		String queryS = i.next();
		String queryE = null;
		
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
		
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";
		while(i.hasNext()){
			queryE = i.next();
			if(site.equals("allFhc")) {
				allSql += "SELECT count(a.tn) from tf_notes a, book b,  site c where to_char(problem_date, 'yyyy/mm/dd') >= '" + queryS + "' and to_char(problem_date, 'yyyy/mm/dd') < '" + queryE + "' and " + filterStr + " and  a.tn = b.tn and    b.scanned_by=c.id and c.is_fhc = 'T' union all ";
			}else if (site.equals("allPartnerLibs")) {

				allSql += "SELECT count(a.tn) from tf_notes a, book b, site c where to_char(problem_date, 'yyyy/mm/dd') >= '" + queryS + "' and to_char(problem_date, 'yyyy/mm/dd') < '" + queryE + "' and " + filterStr + " and  a.tn = b.tn and b.scanned_by=c.id and c.is_partner_institution = 'T' union all ";
			}else{
 
				allSql += "SELECT count(a.tn) from tf_notes a, book b   where to_char(problem_date, 'yyyy/mm/dd') >= '" + queryS + "' and to_char(problem_date, 'yyyy/mm/dd') < '" + queryE + "' and " + filterStr +" and  a.tn = b.tn and   b.scanned_by =  '" + site + "' union all ";
			}
			queryS = queryE;
		}
		 
		allSql = allSql.substring(0, allSql.length() - 10);
		vals = getJdbcTemplate().query(allSql, new StringX1RowMapper());
		for(List<String> row: vals) {
			//get chart array here..
			
			//calculate and reduction (averaging) if needed
			String tnCountStr = (String)row.get(0);
		  
		 
			int tnCount =  Integer.valueOf(tnCountStr);
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCount !=1) ) {
				tnCount = (int)(tnCount * reduction);
				tnCountStr = String.valueOf(tnCount);	
				reductionCount++;
			} 
		
			arrayStrTns += tnCountStr + ", ";
		 
	
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
			 
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount
			 
		}
		
		
		
		
		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
		
		
		//get total counts
		if(site.equals("allFhc")) {
			vals = getJdbcTemplate().query("SELECT count(a.tn) from tf_notes a, book b, site c where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and " + filterStr + " and  a.tn = b.tn and    b.scanned_by=c.id and c.is_fhc = 'T' ", new StringX1RowMapper());
			
		}else if (site.equals("allPartnerLibs")) {
 
			vals = getJdbcTemplate().query("SELECT count(a.tn) from tf_notes a, book b, site c where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and "+ filterStr +"  and  a.tn = b.tn and    b.scanned_by=c.id and c.is_partner_institution = 'T' ", new StringX1RowMapper());
			
		}else{
		 	vals = getJdbcTemplate().query("SELECT count(a.tn) from tf_notes a, book b where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and "+filterStr+" and  a.tn = b.tn and b.scanned_by=? ", new StringX1RowMapper(), site);
		//System.out.println("SELECT count(a.tn) from tf_notes a, book b where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and "+filterStr+" and  a.tn = b.tn and b.scanned_by=? ");
			
		}
	
		 
	
	 
		
		 //ret[0][0] = "Books failed";
	 
		 ret[arrayIndex][1] = (String) vals.get(0).get(0);
		 ret[arrayIndex][2] = arrayStrTns;
		 ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);
		 
	}
	
	public void runQueryForQualityRepublishedDashboard(List<String> dates, String startDateYearFirst, String endDateYearFirst,   String site, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		
		List<List> vals = null;

		String arrayStrTns = "[";
	  		
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
		
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";

		//get total counts
		if(site.equals("allFhc")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_fhc = 'T' ");
			vals = getJdbcTemplate().query("SELECT tn, to_char(date_republished, 'yyyy/mm/dd') from book a, site b where to_char(date_republished, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(date_republished, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_fhc = 'T' order by to_char(date_republished, 'yyyy/mm/dd') ", new StringX2RowMapper());
		}else if (site.equals("allPartnerLibs")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' ");
			vals = getJdbcTemplate().query("SELECT tn, to_char(date_republished, 'yyyy/mm/dd') from book a, site b where to_char(date_republished, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(date_republished, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' order by to_char(date_republished, 'yyyy/mm/dd') ", new StringX2RowMapper());
		}else{
	//System.out.println("SELECT count(tn),  sum(num_of_pages) from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?");
			vals = getJdbcTemplate().query("SELECT tn, to_char(date_republished, 'yyyy/mm/dd') from book where to_char(date_republished, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(date_republished, 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ? order by to_char(date_republished, 'yyyy/mm/dd') ", new StringX2RowMapper(), site);
		}
		
		Iterator<List> rows = vals.iterator();
		List<String> row = null;
		if(rows.hasNext())
			row = rows.next();
		
		Iterator i = dates.iterator();
		i.next();//ok to skip first since it is covered in the query
		int tnCountTotalAll = 0;
		while(i.hasNext()) {
			String queryE = (String)i.next();
			int tnCountTotal = 0;

			String tnCountStr = null;
			//for(List<String> row: vals) {
			do {
				
				if(row != null && row.get(1).compareTo(queryE) < 0) {
					//matched in this time slice
					//get chart array here..
					//calculate and reduction (averaging) if needed
					String tn = (String)row.get(0);
					tnCountTotal += 1;
		
				}else {
					break;
				}
				
				if(rows.hasNext())
					row = rows.next();
				else
					row = null;
				
			}while(rows.hasNext());
			
			tnCountTotalAll += tnCountTotal;
			tnCountStr = String.valueOf(tnCountTotal);	
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCountTotal !=1) ) {
				tnCountTotal = (int)(tnCountTotal * reduction);
				tnCountStr = String.valueOf(tnCountTotal);	
				reductionCount++;
			} 
			arrayStrTns += tnCountStr + ", ";
			 
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount 
		}

		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
		
		ret[arrayIndex][1] = String.valueOf(tnCountTotalAll);
		ret[arrayIndex][2] = arrayStrTns;
		ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);
	}


	public void oldrunQueryForQualityRepublishedDashboard(List<String> dates, String startDateYearFirst, String endDateYearFirst,   String site, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		
		List<List> vals = null;

		String arrayStrTns = "[";
	 
		Iterator<String> i = dates.iterator();
		String queryS = i.next();
		String queryE = null;
		
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
		
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";
		while(i.hasNext()){
			queryE = i.next();
			if(site.equals("allFhc")) {
				//System.out.println("SELECT count(tn) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName + ", 'yyyy/mm/dd') < '" + queryE + "' and b.id = a.scanned_by and b.is_fhc = 'T' ");
				allSql += "SELECT count(tn) from book a, site b where to_char(date_republished, 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(date_republished, 'yyyy/mm/dd') < '" + queryE + "' and b.id = a.scanned_by and b.is_fhc = 'T' union all ";
			}else if (site.equals("allPartnerLibs")) {
				//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName + ", 'yyyy/mm/dd') < '" + queryE + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' union all ");
				allSql += "SELECT count(tn) from book a, site b where to_char(date_republished, 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(date_republished, 'yyyy/mm/dd') < '" + queryE + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' union all ";
			}else{
				allSql += "SELECT count(tn) from book where to_char(date_republished, 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(date_republished, 'yyyy/mm/dd') < '" + queryE + "'  and scanned_by = '" + site + "' union all ";
				//System.out.println("SELECT count(tn),  sum(num_of_pages) from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName + ", 'yyyy/mm/dd') < '" + queryE + "'  and scanned_by = ?");
			}
			queryS = queryE;	
		}
		
		allSql = allSql.substring(0, allSql.length() - 10);
		vals = getJdbcTemplate().query(allSql, new StringX1RowMapper());
		for(List<String> row: vals) {
			//get chart array here..
			
			//calculate and reduction (averaging) if needed
			String tnCountStr = (String)row.get(0);
		  
		 
			int tnCount =  Integer.valueOf(tnCountStr);
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCount !=1) ) {
				tnCount = (int)(tnCount * reduction);
				tnCountStr = String.valueOf(tnCount);	
				reductionCount++;
			} 
		
			arrayStrTns += tnCountStr + ", ";
		 
	
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
			 
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount
			
		}
		
		
		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
		
		

		//get total counts
		if(site.equals("allFhc")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_fhc = 'T' ");
			vals = getJdbcTemplate().query("SELECT count(tn) from book a, site b where to_char(date_republished, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(date_republished, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_fhc = 'T' ", new StringX1RowMapper());
		}else if (site.equals("allPartnerLibs")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' ");
			vals = getJdbcTemplate().query("SELECT count(tn) from book a, site b where to_char(date_republished, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(date_republished, 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' ", new StringX1RowMapper());
		}else{
	//System.out.println("SELECT count(tn),  sum(num_of_pages) from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?");
			vals = getJdbcTemplate().query("SELECT count(tn) from book where to_char(date_republished, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(date_republished, 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?", new StringX1RowMapper(), site);
		}
	
		 
	
	 
			 
		 ret[arrayIndex][1] = (String) vals.get(0).get(0);
		 ret[arrayIndex][2] = arrayStrTns;
		 ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);
	}

	public void runQueryForAuditor(List<String> dates, String startDateYearFirst, String endDateYearFirst, String colName, String colName2, String site, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		

		List<List> vals = null;
		String arrayStrImages = "[";
		String arrayStrTns = "[";
	  
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
		
		String pagesColumn = "num_of_pages";
		if(colName.equalsIgnoreCase("Date_loaded")) {
			pagesColumn = "pages_online";
		}else if(colName.equalsIgnoreCase("scan_ia_complete_date")) {
			pagesColumn = "scan_num_of_pages";
		}
		
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";
		if (site.equals("all")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' ");
			vals = getJdbcTemplate().query("SELECT tn, "+pagesColumn+",  to_char(" + colName + ", 'yyyy/mm/dd') from book a where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' order by  to_char(" + colName + ", 'yyyy/mm/dd')", new StringX3RowMapper());
		}else{
	//System.out.println("SELECT count(tn),  sum(num_of_pages) from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?");
			vals = getJdbcTemplate().query("SELECT tn, "+pagesColumn+",  to_char(" + colName + ", 'yyyy/mm/dd') from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ? order by  to_char(" + colName + ", 'yyyy/mm/dd')", new StringX3RowMapper(), site);
		}

		Iterator<List> rows = vals.iterator();
		List<String> row = null;
		if(rows.hasNext())
			row = rows.next();
		
		Iterator i = dates.iterator();
		i.next();//ok to skip first since it is covered in the query
		int tnCountTotalAll = 0;//total for all time slices
		int imgCountTotalAll = 0;//total for all time slices
		while(i.hasNext()) {
			String queryE = (String)i.next(); 
			int imgCountTotal = 0;//total per time slice
			int tnCountTotal = 0;//total per time slice

			  
			do {
				
				if(row != null && row.get(2).compareTo(queryE) < 0) {
					//matched in this time slice
					//get chart array here..
					//calculate and reduction (averaging) if needed
					String tn = (String)row.get(0);
					String pageCountStr = (String)row.get(1);
					if(pageCountStr == null)
						pageCountStr = "0";
					tnCountTotal += 1;
					int pageCount = Integer.valueOf(pageCountStr);
					imgCountTotal += pageCount;
				}else {
					break;
				}
				
				if(rows.hasNext())
					row = rows.next();
				else
					row = null;
				
			}while(row!=null);
			
			tnCountTotalAll += tnCountTotal;
			imgCountTotalAll += imgCountTotal;
			String tnCountStr = String.valueOf(tnCountTotal);	
			String imgCountStr = String.valueOf(imgCountTotal);	
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCountTotal !=1) ) {
				tnCountTotal = (int)(tnCountTotal * reduction);
				tnCountStr = String.valueOf(tnCountTotal);	
				imgCountTotal = (int)(imgCountTotal * reduction);
				imgCountStr = String.valueOf(imgCountTotal);
				reductionCount++;
			} 

			arrayStrTns += tnCountStr + ", ";
			arrayStrImages += imgCountStr + ", ";
	
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
				firstPeriodImgCount = imgCountStr;
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount
			lastPeriodImgCount = imgCountStr;
		}
		
		
			
		arrayStrImages = arrayStrImages.substring(0,arrayStrImages.length()-2) + "]";//remove comma and finalize array string
		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
		
		
		//get total counts
		if (site.equals("all")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' ");
			vals = getJdbcTemplate().query("SELECT distinct   count(distinct " + colName2 + ") from book a where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' ", new StringX1RowMapper());
		}else{
	//System.out.println("SELECT count(tn),  sum(num_of_pages) from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?");
			vals = getJdbcTemplate().query("SELECT distinct  count(distinct " + colName2 + ") from book a where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?", new StringX1RowMapper(), site);
		}
	
		
		//ret[0][0] = "Books Scanned";
		int auditorCount =  Integer.parseInt(((String) vals.get(0).get(0)));//total distinct auditor count for all time slices
		if(auditorCount == 0)
			ret[arrayIndex][1] = "0";
		else
			ret[arrayIndex][1] = String.valueOf(tnCountTotalAll/auditorCount);
		ret[arrayIndex][2] = arrayStrTns;
		ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);

		//ret[1][0] = "Images Scanned"; 
		if(auditorCount == 0)
			ret[arrayIndex+1][1] = "0";
		else
			ret[arrayIndex+1][1] = String.valueOf(imgCountTotalAll/auditorCount);
		//if(ret[arrayIndex+1][1] == null)
		//	ret[arrayIndex+1][1] = "0";
		ret[arrayIndex+1][2] = arrayStrImages;
		ret[arrayIndex+1][3] = calculateSlope(firstPeriodImgCount, lastPeriodImgCount);



	}
	

	public void oldrunQueryForAuditor(List<String> dates, String startDateYearFirst, String endDateYearFirst, String colName, String colName2, String site, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		

		List<List> vals = null;
		String arrayStrImages = "[";
		String arrayStrTns = "[";
	 
		Iterator<String> i = dates.iterator();
		String queryS = i.next();
		String queryE = null;
		
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
		
		String pagesColumn = "num_of_pages";
		if(colName.equalsIgnoreCase("Date_loaded")) {
			pagesColumn = "pages_online";
		}else if(colName.equalsIgnoreCase("scan_ia_complete_date")) {
			pagesColumn = "scan_num_of_pages";
		}
		
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";
		while(i.hasNext()){
			queryE = i.next();
			if (site.equals("all")) {
				allSql += "SELECT count(tn),  sum("+pagesColumn+") from book a  where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName + ", 'yyyy/mm/dd') < '" + queryE + "' union all ";
			}else{
				allSql += "SELECT count(tn),  sum("+pagesColumn+") from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName + ", 'yyyy/mm/dd') < '" + queryE + "'  and scanned_by = '" + site + "' union all ";
			}
			queryS = queryE;
		
		
		}

		allSql = allSql.substring(0, allSql.length() - 10);
		vals = getJdbcTemplate().query(allSql, new StringX2RowMapper());
		for(List<String> row: vals) {
			//get chart array here..
		
			//calculate and reduction (averaging) if needed
			String tnCountStr = (String)row.get(0);
			String imgCountStr = (String)row.get(1);
			if(imgCountStr == null)
				imgCountStr = "0";
		 
			int tnCount =  Integer.valueOf(tnCountStr);
			int imageCount =  Integer.valueOf(imgCountStr);
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCount != 1) ) {
				tnCount = (int)(tnCount * reduction);
				imageCount = (int)(imageCount * reduction);
				tnCountStr = String.valueOf(tnCount);
				imgCountStr = String.valueOf(imageCount);
				reductionCount++;
			} 
		
			arrayStrTns += tnCountStr + ", ";
			arrayStrImages += imgCountStr + ", ";
	
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
				firstPeriodImgCount = imgCountStr;
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount
			lastPeriodImgCount = imgCountStr;
		}
		
			
		arrayStrImages = arrayStrImages.substring(0,arrayStrImages.length()-2) + "]";//remove comma and finalize array string
		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
		
		
		//get total counts
		if (site.equals("all")) {
			//System.out.println("SELECT count(tn),  sum(num_of_pages) from book a, site b where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' and b.id = a.scanned_by and b.is_partner_institution = 'T' ");
			vals = getJdbcTemplate().query("SELECT count(tn), sum("+pagesColumn+"), 0 from book a where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' "
								+ " union all SELECT distinct 0, 0, count(distinct " + colName2 + ") from book a where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "' ", new StringX3RowMapper());
		}else{
	//System.out.println("SELECT count(tn),  sum(num_of_pages) from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?");
			vals = getJdbcTemplate().query("SELECT count(tn), sum("+pagesColumn+"), 0  from book where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?"
							+ " union all SELECT distinct 0, 0, count(distinct " + colName2 + ") from book a where to_char(" + colName + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName + ", 'yyyy/mm/dd') <= '" + endDateYearFirst + "'  and scanned_by = ?", new StringX3RowMapper(), site, site);
		}
	
		
		//ret[0][0] = "Books Scanned";
		int auditorCount =  Integer.parseInt(((String) vals.get(1).get(2)));
		if(auditorCount == 0)
			ret[arrayIndex][1] = "0";
		else
			ret[arrayIndex][1] = String.valueOf(Integer.parseInt( (String) vals.get(0).get(0))/auditorCount);
		ret[arrayIndex][2] = arrayStrTns;
		ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);

		//ret[1][0] = "Images Scanned"; 
		String pageCount =  (String)vals.get(0).get(1);
		if(pageCount == null)
			pageCount = "0";
		if(auditorCount == 0)
			ret[arrayIndex+1][1] = "0";
		else
			ret[arrayIndex+1][1] = String.valueOf(Integer.parseInt( pageCount)/auditorCount);
		//if(ret[arrayIndex+1][1] == null)
		//	ret[arrayIndex+1][1] = "0";
		ret[arrayIndex+1][2] = arrayStrImages;
		ret[arrayIndex+1][3] = calculateSlope(firstPeriodImgCount, lastPeriodImgCount);



	}
	

	public void runQueryForTopDashboard(int daysBetween, List<String> dates, String startDateYearFirst, String endDateYearFirst,  String site, int extraDays, double reduction, String[][] ret) {
		
		List<List> vals = null;
		//get total counts
		String allSql = "";
		if(site.equals("all")) {
			allSql = "SELECT count(a.tn) num, a.problem_reason from tf_notes a, book b where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') < '" + endDateYearFirst + "' and a.tn = b.tn group by a.problem_reason order by num desc ";
		}else{
			allSql = "SELECT count(a.tn) num, a.problem_reason from tf_notes a, book b   where to_char(problem_date, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and to_char(problem_date, 'yyyy/mm/dd') < '" + endDateYearFirst + "' and a.tn = b.tn and   b.scanned_by =  '" + site + "' group by a.problem_reason order by num desc ";
		}
	
		vals = getJdbcTemplate().query(allSql, new StringX2RowMapper());
	 
		//get top 5 reasons with counts
		 double total1 = 0;
		 String reasonStr1 = "";
		 double total2 = 0;
		 String reasonStr2 = "";
		 double total3 = 0;
		 String reasonStr3 = "";
		 double total4 = 0;
		 String reasonStr4 = "";
		 double total5 = 0;
		 String reasonStr5 = "";
		 
		 try {
			 //try in case there are less than 5 issue types
			 total1 = Double.parseDouble((String) vals.get(0).get(0));
			 reasonStr1 = (String) vals.get(0).get(1);
			 total2 = Double.parseDouble((String) vals.get(1).get(0));
			 reasonStr2 = (String) vals.get(1).get(1);
			 total3 = Double.parseDouble((String) vals.get(2).get(0));
			 reasonStr3 = (String) vals.get(2).get(1);
			 total4 = Double.parseDouble((String) vals.get(3).get(0));
			 reasonStr4 = (String) vals.get(3).get(1);
			 total5 = Double.parseDouble((String) vals.get(4).get(0));
			 reasonStr5 = (String) vals.get(4).get(1);
		 }catch(Exception e) {
			 //do nothing
		 }
		 double total = 0;
		 for(List row : vals) {
			 total += Double.parseDouble((String)row.get(0));
		 } 
		 
		 double percent1 = total1/total;
		 double percent2 = total2/total;
		 double percent3 = total3/total;
		 double percent4 = total4/total;
		 double percent5 = total5/total;
		 if(total == 0) {
			 percent1 = 0;
			 percent2 = 0;
			 percent3 = 0;
			 percent4 = 0;
			 percent5 = 0;
		 }
		 ret[0][0] = reasonStr1;
		 ret[0][1] = (String) String.format("%.1f%%", percent1*100);//avg
		 ret[1][0] = reasonStr2;
		 ret[1][1] = (String) String.format("%.1f%%", percent2*100);//avg
		 ret[2][0] = reasonStr3;
		 ret[2][1] = (String) String.format("%.1f%%", percent3*100);//avg
		 ret[3][0] = reasonStr4;
		 ret[3][1] = (String) String.format("%.1f%%", percent4*100);//avg
		 ret[4][0] = reasonStr5;
		 ret[4][1] = (String) String.format("%.1f%%", percent5*100);//avg
				 
		String arrayStrTns = "[";
	 
		Iterator<String> i = dates.iterator();
		String queryS = i.next();
		String queryE = null;
		
		String firstPeriodCount = null;
		String lastPeriodCount = null;

		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		allSql = "";
		for(int x = 0; x< 5; x++) {
 
			while(i.hasNext()){
				queryE = i.next();
				if(site.equals("all")) {
					if(ret[x][0] == null || ret[x][0].equals(""))
						allSql += "SELECT count(a.tn) num  from tf_notes a, book b where to_char(problem_date, 'yyyy/mm/dd') >= '" + queryS + "' and to_char(problem_date, 'yyyy/mm/dd') < '" + queryE + "' and a.tn = b.tn and a.problem_Reason is null union all ";
					else
						allSql += "SELECT count(a.tn) num  from tf_notes a, book b where to_char(problem_date, 'yyyy/mm/dd') >= '" + queryS + "' and to_char(problem_date, 'yyyy/mm/dd') < '" + queryE + "' and a.tn = b.tn and a.problem_Reason = '" + ret[x][0] + "' union all ";
				}else{
					if(ret[x][0] == null || ret[x][0].equals(""))
						allSql += "SELECT count(a.tn) num from tf_notes a, book b where to_char(problem_date, 'yyyy/mm/dd') >= '" + queryS + "' and to_char(problem_date, 'yyyy/mm/dd') < '" + queryE + "' and a.tn = b.tn and   b.scanned_by =  '" + site + "'  and a.problem_Reason is null union all ";
					else
						allSql += "SELECT count(a.tn) num from tf_notes a, book b where to_char(problem_date, 'yyyy/mm/dd') >= '" + queryS + "' and to_char(problem_date, 'yyyy/mm/dd') < '" + queryE + "' and a.tn = b.tn and   b.scanned_by =  '" + site + "'  and a.problem_Reason = '" + ret[x][0] + "' union all ";
					
				}
				queryS = queryE;
			}
			i = dates.iterator();
			queryS = i.next();
			queryE = null;
		}
		allSql = allSql.substring(0, allSql.length() - 10);
	 
		vals = getJdbcTemplate().query(allSql, new StringX1RowMapper());
		int reductionCountOrig = reductionCount;
		Iterator iter = vals.iterator();
		for(int x = 0 ; x < 5; x++) {
			for(int y = 0 ; y < 10; y++) { //ten date range queries
				//get chart array here..
				List<String> row = (List<String>) iter.next();
				//calculate and reduction (averaging) if needed
				String tnCountStr = (String)row.get(0);
			 
				int tnCount =  Integer.valueOf(tnCountStr);
				//if tnCount is 1, then don't bother averaging, or it averages to 0
				if((reductionCount <= extraDays) && (tnCount !=1) ) {
					tnCount = (int)(tnCount * reduction);
					tnCountStr = String.valueOf(tnCount);	
					reductionCount++;
				} 
			
				arrayStrTns += tnCountStr + ", ";
			 
				if(isFirstrun)
				{
					firstPeriodCount = tnCountStr;
				}
				isFirstrun = false;
				
				lastPeriodCount = tnCountStr;//just always set it each time arount 
			}
			arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
			ret[x][2] = arrayStrTns;
			ret[x][3] = calculateSlope(firstPeriodCount, lastPeriodCount);
			reductionCount = reductionCountOrig;//reset to orig for next loop
			arrayStrTns = "[";//restart string
					
		}

	}

	public void runQueryForAgedDashboard(int daysBetween, List<String> dates, String startDateYearFirst, String endDateYearFirst, String table, String XcolName1, String colName2,  String site, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		
		List<List> vals = null;
		String arrayStrTns = "[";
	  
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;

		String pagesColumn = "num_of_pages";
	   if(colName2.equalsIgnoreCase("sent_to_scan")) {
			//metadata
			pagesColumn = null;//unknown
		}else if(colName2.equalsIgnoreCase("scan_ia_complete_date")) {
			//scan
			pagesColumn = "scan_num_of_pages";
		}else if(colName2.equalsIgnoreCase("date_released")) {
			//ocr processing
			pagesColumn = "num_of_pages";
		}else if(colName2.equalsIgnoreCase("date_loaded")) {
			//publish load and total
			pagesColumn = "pages_online";
		}
			
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";
		if(site.equals("all")) {
			if(colName2.equalsIgnoreCase("sent_to_scan")) {
				//metadata
				vals = getJdbcTemplate().query("SELECT titleno, 0, to_char("+colName2+ ", 'yyyy/mm/dd') from bookmetadata a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + endDateYearFirst + "' order by "+colName2, new StringX3RowMapper());
			}else if(colName2.equalsIgnoreCase("scan_ia_complete_date") || colName2.equalsIgnoreCase("date_released") || colName2.equalsIgnoreCase("date_loaded")) {
				//scan, ocr processing, publish load, and total
				vals = getJdbcTemplate().query("SELECT tn, "+pagesColumn+", to_char("+colName2+ ", 'yyyy/mm/dd')  from book a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + endDateYearFirst + "' order by "+colName2, new StringX3RowMapper());
			}
		}else{
 
			if(colName2.equalsIgnoreCase("sent_to_scan")) {
				//metadata
				vals = getJdbcTemplate().query("SELECT titleno, 0 , to_char("+colName2+ ", 'yyyy/mm/dd') from bookmetadata a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + endDateYearFirst + "'  and a.requesting_location = ? order by "+colName2, new StringX3RowMapper(), site);
			}else if(colName2.equalsIgnoreCase("scan_ia_complete_date") || colName2.equalsIgnoreCase("date_released") || colName2.equalsIgnoreCase("date_loaded")) {
				//scan, ocr processing, publish load, and total
				vals = getJdbcTemplate().query("SELECT tn, "+pagesColumn+", to_char("+colName2+ ", 'yyyy/mm/dd')  from book a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + endDateYearFirst + "'  and a.scanned_by = ? order by "+colName2, new StringX3RowMapper(), site);
			}
		}
		Iterator<List> rows = vals.iterator();
		List<String> row = null;
		if(rows.hasNext())
			row = rows.next();
		
		Iterator i = dates.iterator();
		i.next();//ok to skip first since it is covered in the query
		int tnCountTotalAll = 0;
		while(i.hasNext()) {
			String queryE = (String)i.next(); 
			int pageCountTotal = 0;
			int tnCountTotal = 0;

			String tnCountStr = null;
			//for(List<String> row: vals) {
			do {
				
				if(row != null && row.get(2).compareTo(queryE) < 0) {
					//matched in this time slice
					//get chart array here..
					//calculate and reduction (averaging) if needed
					String tn = (String)row.get(0);
					String pageCountStr = (String)row.get(1);
					if(pageCountStr == null)
						pageCountStr = "0";
					tnCountTotal += 1;
					int pageCount = Integer.valueOf(pageCountStr);
					pageCountTotal += pageCount;
				}else {
					break;
				}
				
				if(rows.hasNext())
					row = rows.next();
				else
					row = null;
				
			}while(rows!=null);
			
			tnCountTotalAll += tnCountTotal;
			tnCountStr = String.valueOf(tnCountTotal);	
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCountTotal !=1) ) {
				tnCountTotal = (int)(tnCountTotal * reduction);
				tnCountStr = String.valueOf(tnCountTotal);	
				reductionCount++;
			} 
			arrayStrTns += tnCountStr + ", ";
			 
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount 
		}
		
		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string
 
 	 
		 double avg = 0;
		 if(tnCountTotalAll != 0)
			 avg = ((double)daysBetween)/((double)tnCountTotalAll);
		 	 avg = avg*24*60;//get minutes
		 ret[arrayIndex][1] = (String) String.format("%.3f", avg);//avg
		 ret[arrayIndex][2] = arrayStrTns;
		 ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);
		 
	}

	public void oldrunQueryForAgedDashboard(int daysBetween, List<String> dates, String startDateYearFirst, String endDateYearFirst, String table, String XcolName1, String colName2,  String site, int extraDays, double reduction, String[][] ret, int arrayIndex) {
		
		List<List> vals = null;
		String arrayStrTns = "[";
	 
		Iterator<String> i = dates.iterator();
		String queryS = i.next();
		String queryE = null;
		
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;

		String pagesColumn = "num_of_pages";
	   if(colName2.equalsIgnoreCase("sent_to_scan")) {
			//metadata
			pagesColumn = null;//unknown
		}else if(colName2.equalsIgnoreCase("scan_ia_complete_date")) {
			//scan
			pagesColumn = "scan_num_of_pages";
		}else if(colName2.equalsIgnoreCase("date_released")) {
			//ocr processing
			pagesColumn = "num_of_pages";
		}else if(colName2.equalsIgnoreCase("date_loaded")) {
			//publish load and total
			pagesColumn = "pages_online";
		}
			
		boolean isFirstrun = true;
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";
		while(i.hasNext()){
			queryE = i.next();
			
				
			if(site.equals("all")) {
					
				if(colName2.equalsIgnoreCase("sent_to_scan")) {
					//metadata
					allSql += "SELECT count(titleno), 0 from bookmetadata a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + queryE + "' union all ";
				}else if(colName2.equalsIgnoreCase("scan_ia_complete_date") || colName2.equalsIgnoreCase("date_released") || colName2.equalsIgnoreCase("date_loaded")) {
					//scan, ocr processing, publish load, and total
					allSql += "SELECT count(tn), sum("+pagesColumn+") from book a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + queryE + "' union all ";
				}
			}else{
				if(colName2.equalsIgnoreCase("sent_to_scan")) {
					//metadata
					allSql += "SELECT count(titleno), 0 from bookmetadata a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + queryE + "'  and a.requesting_location = '" + site + "' union all ";
				}else if(colName2.equalsIgnoreCase("scan_ia_complete_date") || colName2.equalsIgnoreCase("date_released") || colName2.equalsIgnoreCase("date_loaded")) {
					//scan, ocr processing, publish load, and total
					allSql += "SELECT count(tn), sum("+pagesColumn+") from book a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + queryE + "'  and a.scanned_by = '" + site + "' union all ";
				}
			}
			queryS = queryE;
			
		
		}
		allSql = allSql.substring(0, allSql.length() - 10);
		vals = getJdbcTemplate().query(allSql, new StringX2RowMapper());
		for(List<String> row: vals) {
			//get chart array here..
			
			//calculate and reduction (averaging) if needed
			String tnCountStr = (String)row.get(0);
		 
			int tnCount =  Integer.valueOf(tnCountStr);
			//if tnCount is 1, then don't bother averaging, or it averages to 0
			if((reductionCount <= extraDays) && (tnCount !=1) ) {
				tnCount = (int)(tnCount * reduction);
				tnCountStr = String.valueOf(tnCount);	
				reductionCount++;
			} 
		
			arrayStrTns += tnCountStr + ", ";
		 
			if(isFirstrun)
			{
				firstPeriodTNCount = tnCountStr;
			}
			isFirstrun = false;
			
			lastPeriodTNCount = tnCountStr;//just always set it each time arount 
		}
		
		
		
		arrayStrTns = arrayStrTns.substring(0,arrayStrTns.length()-2) + "]";//remove comma and finalize array string

		//get total counts
		if(site.equals("all")) {
			if(colName2.equalsIgnoreCase("sent_to_scan")) {
				//metadata
				vals = getJdbcTemplate().query("SELECT count(titleno), 0 from bookmetadata a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + endDateYearFirst + "' ", new StringX2RowMapper());
			}else if(colName2.equalsIgnoreCase("scan_ia_complete_date") || colName2.equalsIgnoreCase("date_released") || colName2.equalsIgnoreCase("date_loaded")) {
				//scan, ocr processing, publish load, and total
				vals = getJdbcTemplate().query("SELECT count(tn), sum("+pagesColumn+") from book a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + endDateYearFirst + "' ", new StringX2RowMapper());
			}
		}else{
 
			if(colName2.equalsIgnoreCase("sent_to_scan")) {
				//metadata
				vals = getJdbcTemplate().query("SELECT count(titleno), 0 from bookmetadata a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + endDateYearFirst + "'  and a.requesting_location = ?", new StringX2RowMapper(), site);
			}else if(colName2.equalsIgnoreCase("scan_ia_complete_date") || colName2.equalsIgnoreCase("date_released") || colName2.equalsIgnoreCase("date_loaded")) {
				//scan, ocr processing, publish load, and total
				vals = getJdbcTemplate().query("SELECT count(tn), sum("+pagesColumn+") from book a where to_char(" + colName2 + ", 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(" + colName2 + ", 'yyyy/mm/dd') < '" + endDateYearFirst + "'  and a.scanned_by = ?", new StringX2RowMapper(), site);
			}
		}
	
	
		 //ret[0][0] = "";
	 
		 String totalStr = (String) vals.get(0).get(0);
		 int total = Integer.valueOf(totalStr);
		 double avg = ((double)total)/((double)daysBetween);
		 ret[arrayIndex][1] = (String) String.format("%.3f", avg);//avg
		 ret[arrayIndex][2] = arrayStrTns;
		 ret[arrayIndex][3] = calculateSlope(firstPeriodTNCount, lastPeriodTNCount);
		 
	}
	

	public void runQueryForGoals(List<String> dates, String startDateYearFirst, String endDateYearFirst, String site, String[][] ret,  int arrayIndex) {
		
 

		List<List> vals = null;
		String arrayStrLabels = "[";
		String arrayStrGoals = "[";
		String arrayStrActuals = "[";
	  
		
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
 
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";

		//get goals
		if (site.equals("all")) {
			allSql += "SELECT goal_images_yearly, year from site_goal  where year >= '" + startDateYearFirst.substring(0, 4) + "' and year <= '" + endDateYearFirst.substring(0, 4) + "' and site = 'All Sites' order by year"; 
		}else{
			allSql += "SELECT goal_images_yearly, year from site_goal  where   year  >= '" + startDateYearFirst.substring(0, 4) + "' and year <= '" + endDateYearFirst.substring(0, 4) + "' and site = '" + site + "' order by year";
		}
		 
		vals = getJdbcTemplate().query(allSql, new StringX2RowMapper());
		int lastIndex = vals.size();
		if(vals.size() == 0) {
			 
			arrayStrGoals = "[0]";
		}else {
			int yearStart = Integer.parseInt(startDateYearFirst.substring(0, 4));
			int yearEnd = Integer.parseInt(endDateYearFirst.substring(0, 4));
			int mStart = Integer.parseInt(startDateYearFirst.substring(5, 7));
			int mEnd = Integer.parseInt(endDateYearFirst.substring(5, 7));
			for(int y = yearStart; y<=yearEnd; y++) {
				boolean foundYear = false;
				for(List<String> row: vals) {
	 
					String goalYear = (String)row.get(0);
					String year = (String)row.get(1);
				
					int goalMonthly = Integer.parseInt(goalYear)/12;
	
					if(y == Integer.parseInt(year)) {
						if(y == yearStart) {
							int endMonth = 12;//end month in first year only
							if(yearStart == yearEnd) {
								endMonth = mEnd;
							}
							for(int x = mStart; x <= endMonth ; x++) {
								arrayStrGoals += goalMonthly + ", ";
							}
						}else if(y == yearEnd) {
							for(int x = 1; x <= mEnd ; x++) {
								arrayStrGoals += goalMonthly + ", ";
							}
						}else {
							//not first or last
							for(int x = 1; x <= 12 ; x++) {
								arrayStrGoals += goalMonthly + ", ";
							}
						}	
						foundYear = true;
						break;
					}
				}	
				if(foundYear == false) {
					if(y == yearStart) {
						int endMonth = 12;//end month in first year only
						if(yearStart == yearEnd) {
							endMonth = mEnd;
						}
						for(int x = mStart; x <= endMonth ; x++) {
							arrayStrGoals += "0" + ", ";
						}
					}else if(y == yearEnd) {
						for(int x = 1; x <= mEnd ; x++) {
							arrayStrGoals += "0" + ", ";
						}
					}else {
						//not first or last
						for(int x = 1; x <= 12 ; x++) {
							arrayStrGoals += "0" + ", ";
						}
					}
				}
			}
			arrayStrGoals = arrayStrGoals.substring(0,arrayStrGoals.length()-2) + "]";//remove comma and finalize array string
		}
	
		//actual results
		allSql = "";
		startDateYearFirst = dates.get(0);
		endDateYearFirst = dates.get(dates.size()-1);
		if (site.equals("all")) {
			allSql += "SELECT tn, pages_online, to_char(date_loaded, 'yyyy/mm/dd') from book a  where to_char(date_loaded, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(date_loaded, 'yyyy/mm/dd') < '" + endDateYearFirst + "' order by date_loaded ";
		}else{
			allSql += "SELECT tn, pages_online, to_char(date_loaded, 'yyyy/mm/dd') from book a  where to_char(date_loaded, 'yyyy/mm/dd') >= '" + startDateYearFirst + "' and  to_char(date_loaded, 'yyyy/mm/dd') < '" + endDateYearFirst + "' and scanned_by = '" + site + "' order by date_loaded ";
		}
		vals = getJdbcTemplate().query(allSql, new StringX3RowMapper());
		Iterator<List> rows = vals.iterator();
		List<String> row = null;
		if(rows.hasNext())
			row = rows.next();
		
		Iterator i = dates.iterator();
		i.next();//ok to skip first since it is covered in the query
		int tnCountTotalAll = 0;
		while(i.hasNext()) {
			String queryE = (String)i.next(); 
			int pageCountTotal = 0;
			int tnCountTotal = 0;

			String tnCountStr = null;
			//for(List<String> row: vals) {
			do {
				
				if(row != null && row.get(2).compareTo(queryE) < 0) {
					//matched in this time slice
					//get chart array here..
					//calculate and reduction (averaging) if needed
					String tn = (String)row.get(0);
					String pageCountStr = (String)row.get(1);
					if(pageCountStr == null)
						pageCountStr = "0";
					tnCountTotal += 1;
					int pageCount = Integer.valueOf(pageCountStr);
					pageCountTotal += pageCount;
				}else {
					break;
				}
				
				if(rows.hasNext())
					row = rows.next();
				else
					row = null;
				
			}while(rows.hasNext());
			
			tnCountTotalAll += tnCountTotal;
			tnCountStr = String.valueOf(tnCountTotal);	
  
			arrayStrActuals += pageCountTotal + ", ";
		}
		

		
		arrayStrActuals = arrayStrActuals.substring(0,arrayStrActuals.length()-2) + "]";//remove comma and finalize array string
		
		
		for(String d : dates) {
			String monthLong = "";
			
			String mm = d.substring(5, 7);
			if(mm.equals("01"))
				mm = "Jan";
			else if (mm.equals("02"))
				mm = "Feb";
			else if (mm.equals("03"))
				mm = "Mar";
			else if (mm.equals("04"))
				mm = "Apr";
			else if (mm.equals("05"))
				mm = "May";
			else if (mm.equals("06"))
				mm = "Jun";
			else if (mm.equals("07"))
				mm = "Jul";
			else if (mm.equals("08"))
				mm = "Aug";
			else if (mm.equals("09"))
				mm = "Sep";
			else if (mm.equals("10"))
				mm = "Oct";
			else if (mm.equals("11"))
				mm = "Nov";
			else if (mm.equals("12"))
				mm = "Dec";
			arrayStrLabels += "\"" + mm + "-" + d.substring(0,4) + "\", ";
		}

		
		arrayStrLabels = arrayStrLabels.substring(0,arrayStrLabels.length()-14) + "]";//remove comma and finalize array string
		
		
		ret[0][0] = arrayStrLabels;
		ret[0][1] = arrayStrGoals;
		ret[0][2] = arrayStrActuals;		 
	}
	

	public void oldrunQueryForGoals(List<String> dates, String startDateYearFirst, String endDateYearFirst, String site, String[][] ret, int arrayIndex) {
		

		List<List> vals = null;
		String arrayStrLabels = "[";
		String arrayStrGoals = "[";
		String arrayStrActuals = "[";
	 
		Iterator<String> i = dates.iterator();
		String queryS = i.next();
		String queryE = null;
		
		String firstPeriodTNCount = null;
		String lastPeriodTNCount = null;
		String firstPeriodImgCount = null;
		String lastPeriodImgCount = null;
 
		//generate string array for chart
		int reductionCount = 1;
		String allSql = "";

		//get goals
		if (site.equals("all")) {
			allSql += "SELECT goal_images_yearly, year from site_goal  where year >= '" + startDateYearFirst.substring(0, 4) + "' and year <= '" + endDateYearFirst.substring(0, 4) + "' and site = 'All Sites' order by year"; 
		}else{
			allSql += "SELECT goal_images_yearly, year from site_goal  where   year  >= '" + startDateYearFirst.substring(0, 4) + "' and year <= '" + endDateYearFirst.substring(0, 4) + "' and site = '" + site + "' order by year";
		}
		 
		vals = getJdbcTemplate().query(allSql, new StringX2RowMapper());
		int lastIndex = vals.size();
		if(vals.size() == 0) {
			 
			arrayStrGoals = "[0]";
		}else {
			int yearStart = Integer.parseInt(startDateYearFirst.substring(0, 4));
			int yearEnd = Integer.parseInt(endDateYearFirst.substring(0, 4));
			int mStart = Integer.parseInt(startDateYearFirst.substring(5, 7));
			int mEnd = Integer.parseInt(endDateYearFirst.substring(5, 7));
			for(int y = yearStart; y<=yearEnd; y++) {
				boolean foundYear = false;
				for(List<String> row: vals) {
	 
					String goalYear = (String)row.get(0);
					String year = (String)row.get(1);
				
					int goalMonthly = Integer.parseInt(goalYear)/12;
	
					if(y == Integer.parseInt(year)) {
						if(y == yearStart) {
							int endMonth = 12;//end month in first year only
							if(yearStart == yearEnd) {
								endMonth = mEnd;
							}
							for(int x = mStart; x <= endMonth ; x++) {
								arrayStrGoals += goalMonthly + ", ";
							}
						}else if(y == yearEnd) {
							for(int x = 1; x <= mEnd ; x++) {
								arrayStrGoals += goalMonthly + ", ";
							}
						}else {
							//not first or last
							for(int x = 1; x <= 12 ; x++) {
								arrayStrGoals += goalMonthly + ", ";
							}
						}	
						foundYear = true;
						break;
					}
				}	
				if(foundYear == false) {
					if(y == yearStart) {
						int endMonth = 12;//end month in first year only
						if(yearStart == yearEnd) {
							endMonth = mEnd;
						}
						for(int x = mStart; x <= endMonth ; x++) {
							arrayStrGoals += "0" + ", ";
						}
					}else if(y == yearEnd) {
						for(int x = 1; x <= mEnd ; x++) {
							arrayStrGoals += "0" + ", ";
						}
					}else {
						//not first or last
						for(int x = 1; x <= 12 ; x++) {
							arrayStrGoals += "0" + ", ";
						}
					}
				}
			}
			arrayStrGoals = arrayStrGoals.substring(0,arrayStrGoals.length()-2) + "]";//remove comma and finalize array string
		}
	
		//actual results
		allSql = "";
		while(i.hasNext()){
			queryE = i.next();
			if (site.equals("all")) {
				allSql += "SELECT count(tn),  sum(pages_online) from book a  where to_char(date_loaded, 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(date_loaded, 'yyyy/mm/dd') < '" + queryE + "' union all ";
			}else{
				allSql += "SELECT count(tn),  sum(pages_online) from book a  where to_char(date_loaded, 'yyyy/mm/dd') >= '" + queryS + "' and  to_char(date_loaded, 'yyyy/mm/dd') < '" + queryE + "' and scanned_by = '" + site + "' union all ";
			}
			queryS = queryE;
		}

		allSql = allSql.substring(0, allSql.length() - 10);
		vals = getJdbcTemplate().query(allSql, new StringX2RowMapper());
		for(List<String> row: vals) {
			//get chart array here..
		
			//calculate and reduction (averaging) if needed
			String tnCountStr = (String)row.get(0);
			String imgCountStr = (String)row.get(1);
			if(imgCountStr == null)
				imgCountStr = "0";
		 
			arrayStrActuals += imgCountStr + ", ";
		}
		
		arrayStrActuals = arrayStrActuals.substring(0,arrayStrActuals.length()-2) + "]";//remove comma and finalize array string
		
		
		for(String d : dates) {
			String monthLong = "";
			
			String mm = d.substring(5, 7);
			if(mm.equals("01"))
				mm = "Jan";
			else if (mm.equals("02"))
				mm = "Feb";
			else if (mm.equals("03"))
				mm = "Mar";
			else if (mm.equals("04"))
				mm = "Apr";
			else if (mm.equals("05"))
				mm = "May";
			else if (mm.equals("06"))
				mm = "Jun";
			else if (mm.equals("07"))
				mm = "Jul";
			else if (mm.equals("08"))
				mm = "Aug";
			else if (mm.equals("09"))
				mm = "Sep";
			else if (mm.equals("10"))
				mm = "Oct";
			else if (mm.equals("11"))
				mm = "Nov";
			else if (mm.equals("12"))
				mm = "Dec";
			arrayStrLabels += "\"" + mm + "-" + d.substring(0,4) + "\", ";
		}

		
		arrayStrLabels = arrayStrLabels.substring(0,arrayStrLabels.length()-14) + "]";//remove comma and finalize array string
		
		
		ret[0][0] = arrayStrLabels;
		ret[0][1] = arrayStrGoals;
		ret[0][2] = arrayStrActuals;		 
	}
	
	
	////end dashboard/////
	///misc
	@Override 
	public List<String> getAllBookColumnNames(){
		String[] cols = { 
				"TITLE", 
				"AUTHOR", 
				"PROPERTY_RIGHT", 
				"PUBLICATION_TYPE", 
				"PRIORITY_ITEM", 
				"WITHDRAWN", 
				"DIGITAL_COPY_ONLY", 
				"MEDIA_TYPE", 
				"METADATA_COMPLETE", 
				"BATCH_CLASS", 
				"LANGUAGE", 
				"REMARKS_FROM_SCAN_CENTER", 
				"REMARKS_ABOUT_BOOK", 
				"RECORD_NUMBER", 
				"REQUESTING_LOCATION", 
				"OWNING_INSTITUTION", 
				"SCANNED_BY", 
				"SCAN_OPERATOR", 
				"SCAN_MACHINE_ID", 
				"SCAN_METADATA_COMPLETE", 
				"LOCATION", 
				"SCAN_START_DATE", 
				"SCAN_COMPLETE_DATE",
				"SCAN_IMAGE_AUDITOR", 
				"SCAN_IA_START_DATE", 
				"SCAN_IA_COMPLETE_DATE", 
				"FILES_SENT_TO_OREM", 
				"SCAN_NUM_OF_PAGES", 
				"NUM_OF_PAGES", 
				"FILES_RECEIVED_BY_OREM", 
				"IMAGE_AUDIT", 
				"IA_START_DATE", 
				"IA_COMPLETE_DATE", 
				"OCR_by", 
				"OCR_start_date", 
				"OCR_complete_date", 
				"PDFREVIEW_BY", 
				"PDFREVIEW_START_DATE", 
				"PDF_READY", 
				"DATE_RELEASED", 
				"COMPRESSION_CODE", 
				"LOADED_BY", 
				"DATE_LOADED", 
				"COLLECTION", 
				"DNP", 
				"DNP_DELETED_OFF_LINE", 
				"TN_CHANGE_HISTORY", 
				"PDF_OREM_ARCHIVED_DATE", 
				"PDF_OREM_DRIVE_SERIAL_#", 
				"PDF_OREM_DRIVE_NAME", 
				"PDF_COPY2_ARCHIVED_DATE", 
				"PDF_COPY2_DRIVE_SERIAL_#", 
				"PDF_COPY2_DRIVE_NAME", 
				"TIFF_OREM_ARCHIVED_DATE", 
				"TIFF_OREM_DRIVE_SERIAL_#", 
				"TIFF_OREM_DRIVE_NAME", 
				"TIFF_COPY2_ARCHIVED_DATE", 
				"TIFF_COPY2_DRIVE_SERIAL_#", 
				"TIFF_COPY2_DRIVE_NAME", 
				"PDF_SENT_TO_LOAD", 
				"SITE", 
				"URL", 
				"PID", 
				"PAGES_ONLINE", 
				"SUBJECT", 
				"FILMNO", 
				"PAGES_PHYSICAL_DESCRIPTION", 
				"SUMMARY", 
				"DGSNO", 
				"DATE_ORIGINAL", 
				"PUBLISHER_ORIGINAL", 
				"FILENAME", 
				"PDF_DOWNLOAD_BY", 
				"PDF_DOWNLOAD_DATE"};
		
		List<String> sList = Arrays.asList(cols);
		
		return sList;
	}
	
	@Override 
	public List<List> getAllTnsAndColumn(String col, String tnList){
		if(tnList == null)
			return null;
		
		List<List> vals;
		
		if(col == null || col.equals("---"))
			col = "'' as x";
		
		String inClause = generateInClause("tn", tnList);
		vals = getJdbcTemplate().query("SELECT book.TN, " + col + " from book where " + inClause, new StringX2RowMapper() );
		 
		return  vals;
		
	}
	
	public void saveUpdatedColumnValue(String tnList, String columnName, String newValue){
		String inClause = generateInClause("tn", tnList);
		String sql1 = "UPDATE book SET " + columnName + " = '" + newValue + "' where " + inClause;
		try {
			getJdbcTemplate().update(sql1);
		}catch(Exception e) {
			//try as date
			sql1 = "UPDATE book SET " + columnName + " = to_date('" + newValue + "', 'MM/DD/YYYY') where " + inClause;
			getJdbcTemplate().update(sql1);
		}
	}
	
	
	public String markStartedOcr(List<String> tnList, String principal) {
		String allTnListStr = generateQuotedListString(tnList);
		String inClause = generateInClause("tn", allTnListStr);
		if(tnList == null)
			return null;
		
		List<List> vals;
		vals = getJdbcTemplate().query("SELECT book.TN from book where ocr_start_date is not null and " + inClause, new StringX1RowMapper() );
		String msg = "";
		String notInClause = "";
		if(vals != null && vals.size() != 0) {
			msg = generateQuotedListStringFromList(vals);
			notInClause = " and tn not in (" + msg + ")";
		}
		 
		String sql1 = "UPDATE book SET ocr_by =  (select name from users where lower(id) = '" + principal.toLowerCase() + "'), ocr_start_date = current_timestamp where " + inClause + notInClause;
		getJdbcTemplate().update(sql1);
		
		if(!msg.equals("")) {
			return  msg;
		}else {
			return null;
		}
	}
	
	public String markCompleteOcr(List<String> tnList) {
		String allTnListStr = generateQuotedListString(tnList);
		String inClause = generateInClause("tn", allTnListStr);
		
		if(tnList == null)
			return null;
		
		List<List> vals;
		vals = getJdbcTemplate().query("SELECT book.TN from book where ocr_complete_date is not null and " + inClause, new StringX1RowMapper() );
		String msg = "";
		String notInClause = "";
		if(vals != null && vals.size() != 0) {
			msg = generateQuotedListStringFromList(vals);
			notInClause = " and tn not in (" + msg + ")";
		}
		
		String sql1 = "UPDATE book SET ocr_complete_date = current_timestamp where " + inClause + notInClause;
		getJdbcTemplate().update(sql1);

		if(!msg.equals("")) {
			return msg;
		}else {
			return null;
		}
	}
	
	
	public String markCompletePdfDownload(List<String> tnList, String principal) {
		String allTnListStr = generateQuotedListString(tnList);
		String inClause = generateInClause("tn", allTnListStr);
		
		if(tnList == null)
			return null;
		
		List<List> vals;
		vals = getJdbcTemplate().query("SELECT book.TN from book where pdf_download_date is not null and " + inClause, new StringX1RowMapper() );
		String msg = "";
		String notInClause = "";
		if(vals != null && vals.size() != 0) {
			msg = generateQuotedListStringFromList(vals);
			notInClause = " and tn not in (" + msg + ")";
		}
		
		String sql1 = "UPDATE book SET pdf_download_by = (select name from users where lower(id) = '" + principal.toLowerCase() + "'), pdf_download_date = current_timestamp where " + inClause + notInClause;
		getJdbcTemplate().update(sql1);	

		if(!msg.equals("")) {
			return  msg;
		}else {
			return null;
		}
	}
	
	///end misc
	private static class StringRowMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String tn = rs.getString(1);
			return tn;
		}
	}

	//x columns of data
	private static class StringXRowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			List retList = new ArrayList(colCount);
			
			for(int x = 1; x<=colCount; x++) {
				String s = rs.getString(x);
				retList.add(s);
			}
			 
			return retList;
		}
	}

	//1 columns of data
	private static class StringX1RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			List retList = new ArrayList(1);
			retList.add(s1);
			return retList;
		}
	}
	
	//2 columns of data
	private static class StringX2RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			List retList = new ArrayList(2);
			retList.add(s1);
			retList.add(s2);
			return retList;
		}
	}
	

	//3 columns of data
	private static class StringX3RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			List retList = new ArrayList(3);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			return retList;
		}
	}
	

	//4 columns of data
	private static class StringX4RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			List retList = new ArrayList(4);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			return retList;
		}
	}

	//5 columns of data
	private static class StringX5RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			List retList = new ArrayList(5);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			return retList;
		}
	}

	//6 columns of data
	private static class StringX6RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			List retList = new ArrayList(6);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			return retList;
		}
	}

	//7 columns of data
	private static class StringX7RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			List retList = new ArrayList(7);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			return retList;
		}
	}

	//8 columns of data
	private static class StringX8RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			 
			List retList = new ArrayList(8);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
 
			return retList;
		}
	}
	

	//9 columns of data
	private static class StringX9RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			 
			List retList = new ArrayList(9);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
		 
			return retList;
		}
	}

	//10 columns of data
	private static class StringX10RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
						 
			List retList = new ArrayList(10);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
		 
			return retList;
		}
	}



	//11 columns of data
	private static class StringX11RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			 
			List retList = new ArrayList(11);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
		 
			return retList;
		}
	}


	//12 columns of data
	private static class StringX12RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			String s12 = rs.getString(12);
			 
			List retList = new ArrayList(12);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
			retList.add(s12);
		 
			return retList;
		}
	}


	//13 columns of data
	private static class StringX13RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			String s12 = rs.getString(12);
			String s13 = rs.getString(13);
			 
			List retList = new ArrayList(13);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
			retList.add(s12);
			retList.add(s13);
		 
			return retList;
		}
	}



	//14 columns of data
	private static class StringX14RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			String s12 = rs.getString(12);
			String s13 = rs.getString(13);
			String s14 = rs.getString(14);
			 
			List retList = new ArrayList(13);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
			retList.add(s12);
			retList.add(s13);
			retList.add(s14);
		 
			return retList;
		}
	}

	
	//16 columns of data
	private static class StringX16RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			String s12 = rs.getString(12);
			String s13 = rs.getString(13);
			String s14 = rs.getString(14);
			String s15 = rs.getString(15);
			String s16 = rs.getString(16);
			List retList = new ArrayList(16);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
			retList.add(s12);
			retList.add(s13);
			retList.add(s14);
			retList.add(s15);
			retList.add(s16);
			return retList;
		}
	}
	 
		//18 columns of data
		private static class StringX18RowMapper implements RowMapper<List> {
			@Override
			public List mapRow(ResultSet rs, int rowNum) throws SQLException {
				String s1 = rs.getString(1);
				String s2 = rs.getString(2);
				String s3 = rs.getString(3);
				String s4 = rs.getString(4);
				String s5 = rs.getString(5);
				String s6 = rs.getString(6);
				String s7 = rs.getString(7);
				String s8 = rs.getString(8);
				String s9 = rs.getString(9);
				String s10 = rs.getString(10);
				String s11 = rs.getString(11);
				String s12 = rs.getString(12);
				String s13 = rs.getString(13);
				String s14 = rs.getString(14);
				String s15 = rs.getString(15);
				String s16 = rs.getString(16);
				String s17 = rs.getString(17);
				String s18 = rs.getString(18);
				List retList = new ArrayList(18);
				retList.add(s1);
				retList.add(s2);
				retList.add(s3);
				retList.add(s4);
				retList.add(s5);
				retList.add(s6);
				retList.add(s7);
				retList.add(s8);
				retList.add(s9);
				retList.add(s10);
				retList.add(s11);
				retList.add(s12);
				retList.add(s13);
				retList.add(s14);
				retList.add(s15);
				retList.add(s16);
				retList.add(s17);
				retList.add(s18);
				return retList;
			}
		}
	 
		 
		//18 columns of data
		private static class StringX19RowMapper implements RowMapper<List> {
			@Override
			public List mapRow(ResultSet rs, int rowNum) throws SQLException {
				String s1 = rs.getString(1);
				String s2 = rs.getString(2);
				String s3 = rs.getString(3);
				String s4 = rs.getString(4);
				String s5 = rs.getString(5);
				String s6 = rs.getString(6);
				String s7 = rs.getString(7);
				String s8 = rs.getString(8);
				String s9 = rs.getString(9);
				String s10 = rs.getString(10);
				String s11 = rs.getString(11);
				String s12 = rs.getString(12);
				String s13 = rs.getString(13);
				String s14 = rs.getString(14);
				String s15 = rs.getString(15);
				String s16 = rs.getString(16);
				String s17 = rs.getString(17);
				String s18 = rs.getString(18);
				String s19 = rs.getString(19);
				List retList = new ArrayList(19);
				retList.add(s1);
				retList.add(s2);
				retList.add(s3);
				retList.add(s4);
				retList.add(s5);
				retList.add(s6);
				retList.add(s7);
				retList.add(s8);
				retList.add(s9);
				retList.add(s10);
				retList.add(s11);
				retList.add(s12);
				retList.add(s13);
				retList.add(s14);
				retList.add(s15);
				retList.add(s16);
				retList.add(s17);
				retList.add(s18);
				retList.add(s19);
				return retList;
			}
		}
	 
	 
}