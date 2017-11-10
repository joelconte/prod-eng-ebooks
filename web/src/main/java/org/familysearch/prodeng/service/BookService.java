/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.service;
 
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import org.familysearch.prodeng.model.Book;
import org.familysearch.prodeng.model.BookMetadata;
import org.familysearch.prodeng.model.NonBook;
import org.familysearch.prodeng.model.Problem;
import org.familysearch.prodeng.model.Search;
import org.familysearch.prodeng.model.Site;
import org.familysearch.prodeng.model.User;
 

 
public interface BookService {

	/**
	 * Persists an instance of {@code Book} to the Database.
	 * @param book
	 * @throws ConstraintViolationException
	 */
	public void createBook(Book book) throws ConstraintViolationException; 

	public String generateQuotedListString(List<String> l);
	public String generateQuotedListStringFromArray(String[] l);
	
	public List<String> getAllTns();
	public List<String> getBooksByWildcard(String searchBy);
	public List<List> getScanScanReadyTnsInfo(String location);
	public List<List> getScanScanInProgressTnsInfo(String location);
	public List<List> getScanAuditReadyTnsInfo(String location);
	public List<List> getScanAuditReadyTnsInfo2(String location);
	public List<List> getScanProcessedReadyForOremTnsInfo(String location);
	public List<List> getScanProblemTnsInfo(String location);
	
	public List<List> getProcessTitleCheckTnsInfo(String location);
	public List<List> getProcessWaitingForFilesTnsInfo(String location);
	public List<List> getProcessTitleCheckInProgressTnsInfo(String location);
	public List<List> getProcessOcrReadyTnsInfo(String location);
	public List<List> getProcessOcrInProgressTnsInfo(String location);
	public List<List> getProcessPdfDownloadTnsInfo(String location);
	public List<List> getProcessPdfTnsInfo(String location);
	public List<List> getProcessPdfInProgressTnsInfo(String location);
	public List<List> getProcessProblemTnsInfo(String location);
	
	public List<List> getCatalogProblemTnsInfo();
	public List<List> getAdminProblemTnsInfo(String location);
	public List<List> getAdminPdfDateNoReleaseDateTnsInfo();
	public List<List> getAdminReceivedNotesTnsInfo();
	public List<List> getAdminReceivedImagesWithoutMatchingBooksTnsInfo();
	public List<List> getAdminReceivedImagesWithDateAlreadyTnsInfo();
	public List<List> getAdminTiffsBackupTnsInfo();
	public List<List> getAdminTiffArchivingWithoutMatchingBooksTnsInfo();
	public List<List> getAdminTiffArchivingWithMatchingDateTnsInfo();
	public List<List> getAdminPdfBackupTnsInfo();
	public List<List> getAdminPdfArchivingWithoutMatchingBooksTnsInfo();
	public List<List> getAdminPdfArchivingWithMatchingDateTnsInfo();
	public List<List> getAdminPdfArchiveCopy2TnsInfo();
	public List<List> getAdminPdfArchivingCopy2WithoutMatchingBooksTnsInfo();
	public List<List> getAdminPdfArchivingCopy2WithMatchingDateTnsInfo();
	public List<List> getAdminBooksLoadedOnlinTnsInfo();
	public List<List> getAdminLoadingEntryWithoutMatchingBooksTnsInfo();
	public List<List> getAdminLoadingEntryWithMatchingDateTnsInfo();
	public List<List> getAdminReleasedBooksTnsInfo();
	public List<List> getAdminReleaseEntryWithoutMatchingBooksTnsInfo();
	public List<List> getAdminReleaseEntryWithMatchingDateTnsInfo();
	public List<List> getAdminReleaseEntryBatchClassTnsInfo();
	
	public List<List> getSearchTnsListAllColumns(String tnList);
	public List<List> getSearchTnsList(String tnList);
	public List<List> getSearchSecondaryIdsList(String tnList);
	public List<List> getSearchUrlsList(String tnList);
	public List<List> getSearchPidsList(String tnList);

	public List<String> getAllSitesIncludingInactive();
	public List<String> getAllSites();
	public List<String> getAllScanSites();
	public List<String> getAllScanSitesIncludingInactive();
	public List<String> getAllOcrSites();
	public List<String> getAllOcrSitesIncludingInactive();
	public List<String> getAllIaScanSites();
	public List<String> getAllPropertyRights();
	public List<String> getAllPublicationTypes();
	public List<List> getAllBatchClasses();
	
	public Book getBook(String tn);
	public Book getBookBySecondaryIdentifier(String id);
	public Book getBookForProcess(String tn);
	public Book prepareBookForScanToProcessUpdate(Book b, Book oldBook, String tn);
	public boolean isTransitionScanToProcess(Book b, Book oldBook, String tn);
 
	public void insertBatch(String tableName, String[] columns, int[] colTypes, List<List<String>> rows);

	
	public void deleteReceivedImagesEntry();
	public void updateReceivedImages();
	public void deleteTiffArchivingCopy1Entry();
	public void updateTiffArchivingCopy1();
	public void deletePdfArchivingCopy1Entry();
	public void updatePdfArchivingCopy1();
	public void deletePdfArchivingCopy2Entry();
	public void updatePdfArchivingCopy2();
	public String updateLoadingEntry();
	public void deleteLoadingEntry();
	public void updateReleaseEntry();
	public void deleteReleaseEntry();
	
	public List<String> parseExcelDataCol1(String tnData);
	public List<List<String>>  parseExcelData(String tnData, int colCount);
	
	public void updateBook(String userId, Book book);
	
	public void doBookAudit(String userId, String tn, String sql);
	
	//use this when tn also is updated
	public void updateBook(String userId, Book book, String oldTn);
	public void updateBooksFilesReceived(String tnList);
	public void updateBooksSkipScan(String tnList);
	public void updateBooksSkipScanAndProcess(String tnList);
	public void deleteBook(String tn);
	
	////user admin start////
	public List<String> getAllUserIds();
	public User getUser(String userId);
	public String getUserName(String userId, HttpSession session);
	public void createUser(User user);
	public void updateUser(User user);
	public void deleteUser(String userId);
	
	//use this when tn also is updated
	public void updateUser(User user, String oldUserId);
	public void deleteAuthorities(String userId);
	public void insertAuthority(String userId, String auth);
	public List<String> getAllAuthorities();
	////user admin end////
	
	////search start////
	public List<String> getAllSearchIds();
	public void createSearch(Search search);
	public Search getSearch(String searchId);
	public void updateSearch(Search search);
	
	//use this when id also is updated
	public void updateSearch(Search search, String oldSearchId);
	public void deleteSearch(String searchId);
	public List<List<String>> runQuery(String queryText);
	public String checkQuery(String queryText);
	////search end////

	////lang admin start////
	public List<String> getAllLanguageIds();
	public List<List>  getAllLanguageIdsAsRows();
	public List<List>  getAllLanguages();
	public void createLanguage(String id);
	public void deleteLanguages(String idList);
	////lang admin end////
	
	////site admin start////
	public List<String>  getAllSiteIds(); //for showing in a table of rows even though 1 column'
	public List<String>  getAllSiteIdsIncludingInactive();
	public void createSite(Site site);
	public String updateSite(Site site, String oldId);

	public void deleteSite(String id);
	public Site getSite(String id);
	public String getListTNsUsingSite(String id);
	public String getListProblemsUsingSite(String id);
	public String getListMetadataUsingSite(String id);
	public String getListUsersUsingSite(String id);
	
	public List<List> getSiteGoals(String site); //all if null
	public void deleteSiteGoals(String id);
	public String getDuplicateSiteYearGoals(String tnList);
	public void deleteSelectedSiteGoals(String dupSiteYearList);
	////site admin end////
	
	////elder kern metadata admin start////
	public List<List>  getAllNtfBlankWoPrefix();
	public String getDuplicateTnsInTblNtfBlank(String tnList);
	public int getMaxIdTblWoPrefix();
	public void deleteTblWoPrefixRows(String list);
	public List<List>  getNtfBlankAndBook();
	public List<List>  getNtfBlankAndMetadata();
	public void process02TiffsData();
	public List<List> get02TiffsNotInTrackingForm();
	public List<List> getComparedSearchBackup();
	public List<List> getListToNullTitleCheckPlusReview();
	public void updateListToNullTitleCheck();
	public List<String> getAllDriveNames();
	public List<List> getQueryReprocess(String drive);
	public void doCopy(List<List> rows, String drive, String driveLetter);
	////elder kern metadata admin end////
	
	////problems start////
	public List<String>  getBookProblemPns(String tn);
	public List<List>  getBookProblems(String tn);
	public List<List>  getBookOpenProblems(String tn);
	public List<List>  getBookClosedProblems(String tn);
	public void createProblem(Problem problem);
	public Problem getProblem(String tn, String pn);
	public Problem getNewProblem(String tn, String userId);
	public void updateProblem(Problem problem);
	public String getProblemReasonInUseList(String problemReasonList);

 
	public List<String> getAllStatuses();
	public List<List> getAllProblemReasons();
	public List<String> getAllProblemReasons2();
	public void deleteProblemReasons(String list);
	////problems end////
	
	///metadata start///
 
	public List<List> getMetadataNewBooksAlreadyInTrackingFormDatabaseTnsInfo();
	public List<List> getMetadataSendToScanTnsInfo();
	public List<List> getMetadataUpdateTnsInfo();
	public List<List> getMetadataUpdateTnsInfo(String tnList);
	
	public  Object[]  getReportForSendToScanSelectedMetadata(List<String> allTnList);
	public  Object[]  getReportForSendToScanSelectedMetadataAll();
 
	public List<List> getDuplicateTnsInBook();
	public void deleteAllNewMetadata();
	public void deleteAllUpdateMetadata();
	public void deleteSelectedMetadata(String tnList);
	public void deleteSelectedMetadataForUpdate(String tnList);
	public void deleteMetadata(String tn);
	public void checkCompleteSelectedMetadata(String tnList, String checker);
	public void checkCompleteAllMetadata(String checker);
	public String sendToScanSelectedMetadata(List<String> tnList, String sender);
	public String sendToScanAllMetadata(String sender);
	public String sendToDoUpdateSelectedMetadata(String userId, List<String> tnList, String sender);
	public String sendToDoUpdateAllMetadata(String userId, String sender);
	public String sendToScanSelectedInternetArchiveMetadata(List<String> tnList, String sender);
	public String sendToScanAllInternetArchiveMetadata(String sender);
	public void migrateInternetArchiveMetadataToBookInsert( String tnList );
	public void migrateInternetArchiveMetadataToBookUpdate( String tnList );
	public void autoUpdateCopyrightSerialEtc(String tnList);
	public void autoUpdateSkipStepsEtc(Book b, String skipTo);
	public String getDuplicateTnsInMetadata(String tnList);
	public String getDuplicateTnsInBook(String tnList);
	public List<String> getDuplicateTnsInBookList(String tnList);
	public BookMetadata getBookMetadata(String tn);
	public BookMetadata getBookMetadataFromBookTable(String tn);//decided to get from BOOK tab instead so metadata table can be trimmed etc..
	public List<String> getAllTnsMetadata();
	 
	public List<String> getMetadataCompleteAndSent(String userId);
 
	public void updateBookMetadata(BookMetadata book);
	//use this when tn also is updated
	public void updateBatchMetatdataUpdates(String userId, String tableName, String[] columns, List<List> rows);
	public void updateBookMetadata(BookMetadata book, String oldTn);
 
	public List<List> getInternetArchiveMetadataSendToScanTnsInfo();
	public String getDuplicateTnsInInternetArchiveMetadata(String tnList);
 
	public void deleteInternetArchiveAllNewMetadata();
	public void deleteInternetArchiveSelectedMetadata(String tnList);
	public void deleteInternetArchiveMetadata(String tn);
	public void deleteSelectedInternetArchiveMetadata(String tnList);
	public List<List> getIAMetadataNewBooksAlreadyInTrackingFormDatabaseTnsInfo();
	public void deleteAllInternetArchiveNewMetadata();
	///metadata end///
	
	////viewingreport////
	public void insertBookViewingStats(String year, String month, String totalViews, String numUniqueViews);
	public boolean getDuplicatesInViewingReport(String year, String month);
	public List<List> getViewingReports( );
	public void deleteSelectedViewingReports(List<String> yearList, List<String> monthList);
	public List<List> getViewingStats5Years(int currentYear);
	public List<List> getPast12MonthViews();
	//public List<List> getViewingStats5YearsX(int currentYear);
	////viewingreport end////
		
	///reports///
	public List<List> getStatsFinal(String year, String month,  String scannedBy);
	public List<List> getStatsTn( String year, String month, String scannedBy);
	public List<List> getStatsUrlList( String year, String month, String scannedBy);
	public List<List> getStatsTitleList( String year, String month, String scannedBy);
	public List<List> getStatsGenealogyToday( String year, String month, String scannedBy);
	public List<List> getStatsScannedByReport( String year, String month, String scannedBy);
	public List<List> getStatsMonthlyUrls( String year, String month, String scannedBy);
	public List<List> getStatsScannedByTns();
	public List<List> getStatsTnsThey(String year, String month, String scannedBy );
	public List<List> getStatsTnsWe( String year, String month, String scannedBy);
	public List<List> getStatsCountPerMonth( String year, String month, String scannedBy);
	public List<String> getAllYears();
	
	///reports end///
	///locking start///
	
	public String getBookLock(String tn, String operator);
	public String checkAndReleaseBookLock(String tn, String operator);
	public List<List> getAllBookLocks();
	public void deleteBookLocks(String lockList);
	public void unlockOldLocks();
	///locking end///
    ////start dashboard/////
	public String[][] getDashboardAverages(String startDate, String endDate, String site);
	public String[][] getDashboardQualityAverages(String startDate, String endDate, String site);
	public String[][] getDashboardAuditor( String startDate, String endDate, String site);
	public String[][] getDashboardTop5( String startDate, String endDate, String site);
	public String[][] getDashboardAgedAverages(String startDate, String endDate, String site);
	public String[][] getDashboardTurnaroundAverages(String startDate, String endDate, String site);
	public String[][] getDashboardGoalData( String startDate, String endDate, String site);
	public String[][] XgetDashboardByMonthDataScanProcessPublish( String startDate, String endDate, String site, int daysDiff);
	public List<List> getDashboardOpenIssues();
	public List<List> getGoalsAndActuals(String year, int endMonthInt, String endDate, String site);
	public String[][] getDashboarDataYTDScanPublish( String startDate, String endDate, String site, int daysDiff);
	public String[] getDashboarDataTotalYTDScanPublish(String startDate, String endDate, String fomStartDate, String fomStartDateCurrentMonth, String fomEndDate, String site);
	
	////end dashboard/////
	//xml medatdata//
	public boolean queryXmlMetadata( String tn, String[][] mdValues, String[][] recordValues);
	public boolean queryXmlCatalogingData( String secondaryId, String[][] mdValues);
	public boolean queryGetXmlIACountTotal(String[][] mdValues);
	//xml medatdata end//
	//misc
	public List<String> getAllBookColumnNames();
	public List<List> getAllTnsAndColumn(String col, String tnList);
	public void saveUpdatedColumnValue(String userId, List<String> tnList1, String tnList, String columnName, String value);
	public String markStartedOcr(List<String> tnList, String principal);
	public String markCompleteOcr(List<String> tnList);
	public String markCompletePdfDownload(List<String> tnList, String principal);
	

	List<List<Object>> stringsToTypes(int[] colType, List<List<String>> rows);
	//misc end
 
	
	public Set<String> doBibcheck(String inClause);

    public List<String> getInternetArchiveWorkingBookById(String identifier);
	public Set<String> getIneternetArchiveBooksInProcess(String inClause);
	public void insertInternetArchiveSearchedBooks(List<List<String>> rows, String ownerUserId);
	
	public List<List> getInternetArchiveWorkingBooksStateSelectBooks(String userId);
	public List<List> getInternetArchiveWorkingBooksStateVerifyBooks(String userId);
	public List<List> getInternetArchiveWorkingBooksStatePreDownloadBooks(String userId);
	public List<List> getInternetArchiveWorkingBooksStateDownloadNotStartedBooks(String userId);
    public List<List> getInternetArchiveWorkingBooksStateAnyDownloadBooks(String userId);
    public List<List> getInternetArchiveWorkingBooksStateAnyDownloadBooksExceptComplete(String userId);
    public List<List> getInternetArchiveWorkingBooksStateInsertTfdb(String userId);
	  
	public String updateInternetArchiveWorkingBook(String bookId, String addToFs, String oclc, String tn, String dnp, String volume, String user);
	public String updateInternetArchiveWorkingBook(String bookId, String addToFs, String user);
	public String updateInternetArchiveWorkingBookToState(String bookId, String state);
	
	public void updateInternetArchiveWorkingBooksChangeStateVerifyBooks(String userId, String site);
	public void updateInternetArchiveWorkingBooksChangeStatePreDownloadBooks(String userId);
	public void updateInternetArchiveWorkingBooksChangeStateDownloadNotStartedBooks(String userId);
	public void updateInternetArchiveWorkingBooksChangeStateDownloadX(String bookId, String[] fromStates, String toState, String folder);
	public void updateInternetArchiveWorkingBooksChangeStateDownloadNotStartedBooksFromAnyDownloadingState(String userId);
	public void updateInternetArchiveWorkingBooksChangeStateDownloadNotStartedBooksFromAnyDownloadingStateExceptComplete(String userId);
	public void updateInternetArchiveWorkingBooksChangeStateReadyInsertTfdbBooks(String userId);
	public void updateInternetArchiveWorkingBooksChangeStateCompleteBooks(String userId, String driveName, String driveNumber, InternetArchiveService iaService); 
	public void updateInternetArchiveWorkingBooksError(String bookId, String err);
	public void updateInternetArchiveWorkingBooksStateDownloadNotStartedBooksErrorMsg(String msg);
	
	public void deleteInternetArchiveWorkingBooksAnyDownloadingState();
    public void deleteInternetArchiveWorkingBooks(List<String> identifier);
	public void deleteInternetArchiveWorkingBooksStateSelectBooks(String ownerUserId);
	public void deleteInternetArchiveWorkingBooksStateVerifyBooks(String ownerUserId);
	public void deleteInternetArchiveWorkingBooksStatePreDownloadBooks(String ownerUserId);
	public void deleteInternetArchiveWorkingBooksStateDownloadCompleteBooks(String ownerUserId);
	
	
	//start nonbook docs
	public void createNonBook(NonBook book) throws ConstraintViolationException;
	public List<String> getAllDns();
	public NonBook getNonBook(String dn);
	public List<String> getNonBooksByWildcard(String searchBy);
	public void updateNonBook(NonBook book);
	public void updateNonBook(NonBook book, String oldDn);
	public void deleteNonBook(String dn);
	//start nonbook docs
	
}
