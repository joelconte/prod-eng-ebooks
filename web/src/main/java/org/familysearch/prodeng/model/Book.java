/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.lds.stack.security.web.validate.jsr303.SafeHtml;

/**
 * Book object that maps to DB table BOOK
 */
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

    private String tn = "";
    
    @NotBlank(message="{manageBook.namerequired}")
    @Size(min = 0, max = 200, message="{manageBook.sizebetween}")
    private String title;
    
    @SafeHtml
    @NotBlank(message="{manageBook.datarequired}")
    private String secondaryIdentifier = "";
    private String oclcNumber;
    private String isbnIssn;
    private String author;
    private String propertyRight;
    private String publicationType;
    private String filename;
    private String scanningSiteNotes;
    private String callNumber;
    private String partnerLibCallNumber;
    private String priorityItem;
    private String withdrawn;
    private String digitalCopyOnly;
    private String mediaType;
    private Timestamp metadataComplete;
    private String batchClass;
    private String language;
    private String remarksFromScanCenter;
    private String remarksAboutBook;
    private String recordNumber;
    private String requestingLocation;
    private String owningInstitution;
    private String scannedBy;
    private String scanOperator;
    private String scanMachineId;
    private Timestamp scanMetadataComplete;
    private String location;
    private Timestamp scanStartDate;
    private Timestamp scanCompleteDate;
    private String scanImageAuditor;
    private Timestamp scanIaStartDate;
    private Timestamp scanIaCompleteDate;
    private Timestamp filesSentToOrem;
    private String scanNumOfPages;
    private String numOfPages;
    private Timestamp filesReceivedByOrem;
    private String imageAudit;
    private Timestamp iaStartDate;
    private Timestamp iaCompleteDate;
    private String ocrBy;
    private Timestamp ocrStartDate;
    private Timestamp ocrCompleteDate;
    private String pdfDownloadBy;
    private Timestamp pdfDownloadDate;
    private String pdfreviewBy;
    private Timestamp pdfreviewStartDate;
    private Timestamp pdfReady;
    private Timestamp dateReleased;
    private String compressionCode;
    private String loadedBy;
    private Timestamp dateLoaded;
    private String collection;
    private String dnp;
    private String dnpDeletedOffLine;
    private String tnChangeHistory;
    private Timestamp pdfOremArchivedDate;
    private String pdfOremDriveSerialNumber;
    private String pdfOremDriveName;
    private Timestamp pdfCopy2ArchivedDate;
    private String pdfCopy2DriveSerialNumber;
    private String pdfCopy2DriveName;
    private Timestamp tiffOremArchivedDate;
    private String tiffOremDriveSerialNumber;
    private String tiffOremDriveName;
    private Timestamp tiffCopy2ArchivedDate;
    private String tiffCopy2DriveSerialNumber;
    private String tiffCopy2DriveName;
    private Timestamp pdfSentToLoad;
    private String site;
    private String url;
    private String pid;
    private String pagesOnline;
    private String subject;
    private String filmno; 
    private String pagesPhysicalDescription; 
    private String summary;
    private String dgsno;
    private Timestamp dateOriginal;
    private String publisherOriginal;
    private String fhcTitle;
    private String fhcTn;
    private Timestamp dateRepublished;
    
    //since html form will only set values that are on a particular form, need to keep track of changed data
    private boolean tnSet = false;
    private boolean secondaryIdentifierSet = false;
    private boolean oclcNumberSet = false;
    private boolean isbnIssnSet = false;
    private boolean titleSet = false;
    private boolean authorSet = false;
    private boolean propertyRightSet = false; 
    private boolean publicationTypeSet = false; 
    private boolean filenameSet = false; 
    private boolean scanningSiteNotesSet = false;
    private boolean callNumberSet = false;
    private boolean partnerLibCallNumberSet = false;
    private boolean priorityItemSet = false;
    private boolean withdrawnSet = false;
    private boolean digitalCopyOnlySet = false;
    private boolean mediaTypeSet = false;
    private boolean metadataCompleteSet = false;
    private boolean batchClassSet = false;
    private boolean languageSet = false;
    private boolean remarksFromScanCenterSet = false;
    private boolean remarksAboutBookSet = false;
    private boolean recordNumberSet = false;
    private boolean requestingLocationSet = false;
    private boolean owningInstitutionSet = false;
    private boolean scannedBySet = false;
    private boolean scanOperatorSet = false;
    private boolean scanMachineIdSet = false;
    private boolean scanMetadataCompleteSet = false;
    private boolean locationSet = false;
    private boolean scanStartDateSet = false;
    private boolean scanCompleteDateSet = false;
    private boolean scanImageAuditorSet = false;
    private boolean scanIaStartDateSet = false;
    private boolean scanIaCompleteDateSet = false;
    private boolean filesSentToOremSet = false;
    private boolean scanNumOfPagesSet = false;
    private boolean numOfPagesSet = false;
    private boolean filesReceivedByOremSet = false;
    private boolean imageAuditSet = false;
    private boolean iaStartDateSet = false;
    private boolean iaCompleteDateSet = false;
    private boolean ocrBySet = false;
    private boolean ocrStartDateSet = false;
    private boolean ocrCompleteDateSet = false;
    private boolean pdfDownloadBySet = false;
    private boolean pdfDownloadDateSet = false;
    private boolean pdfreviewBySet = false;
    private boolean pdfreviewStartDateSet = false;
    private boolean pdfReadySet = false;
    private boolean dateReleasedSet = false;
    private boolean compressionCodeSet = false;
    private boolean loadedBySet = false;
    private boolean dateLoadedSet = false;
    private boolean collectionSet = false;
    private boolean dnpSet = false;
    private boolean dnpDeletedOffLineSet = false;
    private boolean tnChangeHistorySet = false;
    private boolean pdfOremArchivedDateSet = false;
    private boolean pdfOremDriveSerialNumberSet = false;
    private boolean pdfOremDriveNameSet = false;
    private boolean pdfCopy2ArchivedDateSet = false;
    private boolean pdfCopy2DriveSerialNumberSet = false;
    private boolean pdfCopy2DriveNameSet = false;
    private boolean tiffOremArchivedDateSet = false;
    private boolean tiffOremDriveSerialNumberSet = false;
    private boolean tiffOremDriveNameSet = false;
    private boolean tiffCopy2ArchivedDateSet = false;
    private boolean tiffCopy2DriveSerialNumberSet = false;
    private boolean tiffCopy2DriveNameSet = false;
    private boolean pdfSentToLoadSet = false;
    private boolean siteSet = false;
    private boolean urlSet = false;
    private boolean pidSet = false;
    private boolean pagesOnlineSet = false;
    private boolean subjectSet = false;
    private boolean filmnoSet = false;
    private boolean pagesPhysicalDescriptionSet = false;
    private boolean summarySet = false;
    private boolean dgsnoSet = false;
    private boolean dateOriginalSet = false;
    private boolean publisherOriginalSet = false;
    private boolean fhcTitleSet = false;
    private boolean fhcTnSet = false;
    private boolean dateRepublishedSet = false;
    
    
 
    //////////////get methods////////////
	public String getTn() {
		return tn;
	}
	public String getSecondaryIdentifier() {
		return secondaryIdentifier;
	}
	public String getOclcNumber() {
		return oclcNumber;
	}
	public String getIsbnIssn() {
		return isbnIssn;
	}
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public String getPropertyRight() {
		return propertyRight;
	}
	public String getPublicationType() {
		return publicationType;
	}
	public String getFilename() {
		return filename;
	}
	public String getBatchClass() {
		return batchClass;
	}
	public String getCallNumber() {
		return callNumber;
	}
	public String getPartnerLibCallNumber() {
		return partnerLibCallNumber;
	}
	public String getCollection() {
		return collection;
	}
	public String getCompressionCode() {
		return compressionCode;
	}
	public Timestamp getDateLoaded() {
		return dateLoaded;
	}
	public Timestamp getDateReleased() {
		return dateReleased;
	}
	public String getDigitalCopyOnly() {
		return digitalCopyOnly;
	}
	public String getDnp() {
		return dnp;
	}
	public String getDnpDeletedOffLine() {
		return dnpDeletedOffLine;
	}
	public Timestamp getFilesReceivedByOrem() {
		return filesReceivedByOrem;
	}
	public Timestamp getFilesSentToOrem() {
		return filesSentToOrem;
	}
	public Timestamp getIaCompleteDate() {
		return iaCompleteDate;
	}
	public Timestamp getIaStartDate() {
		return iaStartDate;
	}
	public String getImageAudit() {
		return imageAudit;
	}
	public String getOcrBy() {
		return ocrBy;
	}
	public Timestamp getOcrStartDate() {
		return ocrStartDate;
	}
	public Timestamp getOcrCompleteDate() {
		return ocrCompleteDate;
	}
	public String getPdfDownloadBy() {
		return pdfDownloadBy;
	}
	public Timestamp getPdfDownloadDate() {
		return pdfDownloadDate;
	}
	public String getPdfreviewBy() {
		return pdfreviewBy;
	}
	public Timestamp getPdfreviewStartDate() {
		return pdfreviewStartDate;
	}
	public String getLanguage() {
		return language;
	}
	public String getLoadedBy() {
		return loadedBy;
	}
	public String getLocation() {
		return location;
	}
	public String getMediaType() {
		return mediaType;
	}
	public Timestamp getMetadataComplete() {
		return metadataComplete;
	}
	public String getNumOfPages() {
		return numOfPages;
	}
	public String getPagesOnline() {
		return pagesOnline;
	}
	public Timestamp getPdfCopy2ArchivedDate() {
		return pdfCopy2ArchivedDate;
	}
	public String getPdfCopy2DriveName() {
		return pdfCopy2DriveName;
	}
	public String getPdfCopy2DriveSerialNumber() {
		return pdfCopy2DriveSerialNumber;
	}
	public Timestamp getPdfOremArchivedDate() {
		return pdfOremArchivedDate;
	}
	public String getPdfOremDriveName() {
		return pdfOremDriveName;
	}
	public String getPdfOremDriveSerialNumber() {
		return pdfOremDriveSerialNumber;
	}
	public Timestamp getPdfReady() {
		return pdfReady;
	}
	public Timestamp getPdfSentToLoad() {
		return pdfSentToLoad;
	}
	public String getPid() {
		return pid;
	}
	public String getPriorityItem() {
		return priorityItem;
	}
	public String getRemarksAboutBook() {
		return remarksAboutBook;
	}
	public String getRemarksFromScanCenter() {
		return remarksFromScanCenter;
	}
	public String getUrl() {
		return url;
	}
	public Timestamp getScanCompleteDate() {
		return scanCompleteDate;
	}
	public Timestamp getScanIaCompleteDate() {
		return scanIaCompleteDate;
	}
	public Timestamp getScanIaStartDate() {
		return scanIaStartDate;
	}
	public String getScanImageAuditor() {
		return scanImageAuditor;
	}
	public String getScanMachineId() {
		return scanMachineId;
	}
	public Timestamp getScanMetadataComplete() {
		return scanMetadataComplete;
	}
	public String getRecordNumber() {
		return recordNumber;
	}
	public String getRequestingLocation() {
		return requestingLocation;
	}
	public String getOwningInstitution() {
		return owningInstitution;
	}
	public String getScannedBy() {
		return scannedBy;
	}
	public String getScanningSiteNotes() {
		return scanningSiteNotes;
	}
	public String getScanNumOfPages() {
		return scanNumOfPages;
	}
	public String getScanOperator() {
		return scanOperator;
	}
	public Timestamp getScanStartDate() {
		return scanStartDate;
	} 
	public String getSite() {
		return site;
	}
	public Timestamp getTiffCopy2ArchivedDate() {
		return tiffCopy2ArchivedDate;
	}
	public String getTiffCopy2DriveName() {
		return tiffCopy2DriveName;
	}
	public String getTiffCopy2DriveSerialNumber() {
		return tiffCopy2DriveSerialNumber;
	}
	public Timestamp getTiffOremArchivedDate() {
		return tiffOremArchivedDate;
	}
	public String getTiffOremDriveName() {
		return tiffOremDriveName;
	}
	public String getTiffOremDriveSerialNumber() {
		return tiffOremDriveSerialNumber;
	}
	public String getTnChangeHistory() {
		return tnChangeHistory;
	}
	public String getWithdrawn() {
		return withdrawn;
	}
	public String getSubject() {
		return subject ;
	}
	public String getFilmno() {
		return filmno;
	}
	public String getPagesPhysicalDescription() {
		return pagesPhysicalDescription;
	}
	public String getSummary() {
		return summary;
	}
	public String getDgsno() {
		return dgsno;
	}
	public Timestamp getDateOriginal() {
		return dateOriginal;
	}
	public String getPublisherOriginal() {
		return publisherOriginal;
	}
	public String getFhcTitle() {
		return fhcTitle;
	}
	public String getFhcTn() {
		return fhcTn;
	}
	public Timestamp getDateRepublished() {
		return dateRepublished;
	}
	
	///////////////set methods///////////////
	
	
	public void setTn(String tn) {
		this.tn = tn;
		this.tnSet = true;
	}
	public void setSecondaryIdentifier(String secondaryIdentifier) {
		this.secondaryIdentifier = secondaryIdentifier;
		this.secondaryIdentifierSet = true;
	}
	public void setOclcNumber(String oclcNumber) {
		this.oclcNumber = oclcNumber;
		this.oclcNumberSet = true;
	}	
	public void setIsbnIssn(String isbnIssn) {
		this.isbnIssn = isbnIssn;
		this.isbnIssnSet = true;
	}
	public void setTitle(String title) {
		this.title = title;
		this.titleSet = true;
	}

	public void setPropertyRight(String propertyRight) {
		this.propertyRight = propertyRight;
		this.propertyRightSet = true;
	}
	
	public void setPublicationType(String publicationType) {
		this.publicationType = publicationType;
		this.publicationTypeSet = true;
	}

	public void setFilename(String filename) {
		this.filename = filename;
		this.filenameSet = true;
	}
	
	public void setAuthor(String author) {
		this.author = author;
		this.authorSet = true;
	}
	
	public void setBatchClass(String batchClass) {
		this.batchClass = batchClass;
		this.batchClassSet = true;
	}
	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
		this.callNumberSet = true;
	}
	public void setPartnerLibCallNumber(String partnerLibCallNumber) {
		this.partnerLibCallNumber = partnerLibCallNumber;
		this.partnerLibCallNumberSet = true;
	}
	public void setCollection(String collection) {
		this.collection = collection;
		this.collectionSet = true;
	}
	public void setCompressionCode(String compressionCode) {
		this.compressionCode = compressionCode;
		this.compressionCodeSet = true;
	}
	public void setDateLoaded(Timestamp dateLoaded) {
		this.dateLoaded = dateLoaded;
		this.dateLoadedSet = true;
	}
	public void setDateReleased(Timestamp dateReleased) {
		this.dateReleased = dateReleased;
		this.dateReleasedSet = true;
	}
	public void setDigitalCopyOnly(String digitalCopyOnly) {
		this.digitalCopyOnly = digitalCopyOnly;
		this.digitalCopyOnlySet = true;
	}
	public void setDnp(String dnp) {
		this.dnp = dnp;
		this.dnpSet = true;
	}
	public void setDnpDeletedOffLine(String dnpDeletedOffLine) {
		this.dnpDeletedOffLine = dnpDeletedOffLine;
		this.dnpDeletedOffLineSet = true;
	}
	public void setFilesReceivedByOrem(Timestamp filesReceivedByOrem) {
		this.filesReceivedByOrem = filesReceivedByOrem;
		this.filesReceivedByOremSet = true;
	}
	public void setFilesSentToOrem(Timestamp filesSentToOrem) {
		this.filesSentToOrem = filesSentToOrem;
		this.filesSentToOremSet = true;
	}
	public void setIaCompleteDate(Timestamp iaCompleteDate) {
		this.iaCompleteDate = iaCompleteDate;
		this.iaCompleteDateSet = true;
	}
	public void setIaStartDate(Timestamp iaStartDate) {
		this.iaStartDate = iaStartDate;
		this.iaStartDateSet = true;
	}
	public void setImageAudit(String imageAudit) {
		this.imageAudit = imageAudit;
		this.imageAuditSet = true;
	}
	public void setOcrBy(String ocrBy) {
		this.ocrBy = ocrBy;
		this.ocrBySet = true;
	}
	public void setOcrStartDate(Timestamp ocrStartDate) {
		this.ocrStartDate = ocrStartDate;
		this.ocrStartDateSet = true;
	}
	public void setOcrCompleteDate(Timestamp ocrCompleteDate) {
		this.ocrCompleteDate = ocrCompleteDate;
		this.ocrCompleteDateSet = true;
	}
	public void setPdfDownloadBy(String pdfDownloadBy) {
		this.pdfDownloadBy = pdfDownloadBy;
		this.pdfDownloadBySet = true;
	}
	public void setPdfDownloadDate(Timestamp pdfDownloadDate) {
		this.pdfDownloadDate = pdfDownloadDate;
		this.pdfDownloadDateSet = true;
	}
	public void setPdfreviewBy(String pdfreviewBy) {
		this.pdfreviewBy = pdfreviewBy;
		this.pdfreviewBySet = true;
	}
	public void setPdfreviewStartDate(Timestamp pdfreviewStartDate) {
		this.pdfreviewStartDate = pdfreviewStartDate;
		this.pdfreviewStartDateSet = true;
	}
	public void setLanguage(String language) {
		this.language = language;
		this.languageSet = true;
	}
	public void setLoadedBy(String loadedBy) {
		this.loadedBy = loadedBy;
		this.loadedBySet = true;
	}
	public void setLocation(String location) {
		this.location = location;
		this.locationSet = true;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
		this.mediaTypeSet = true;
	}
	public void setMetadataComplete(Timestamp metadataComplete) {
		this.metadataComplete = metadataComplete;
		this.metadataCompleteSet = true;
	}
	public void setNumOfPages(String numOfPages) {
		this.numOfPages = numOfPages;
		this.numOfPagesSet = true;
	}
	public void setPagesOnline(String pagesOnline) {
		this.pagesOnline = pagesOnline;
		this.pagesOnlineSet = true;
	}
	public void setPdfCopy2ArchivedDate(Timestamp pdfCopy2ArchivedDate) {
		this.pdfCopy2ArchivedDate = pdfCopy2ArchivedDate;
		this.pdfCopy2ArchivedDateSet = true;
	}
	public void setPdfCopy2DriveName(String pdfCopy2DriveName) {
		this.pdfCopy2DriveName = pdfCopy2DriveName;
		this.pdfCopy2DriveNameSet = true;
	}
	public void setPdfCopy2DriveSerialNumber(String pdfCopy2DriveSerialNumber) {
		this.pdfCopy2DriveSerialNumber = pdfCopy2DriveSerialNumber;
		this.pdfCopy2DriveSerialNumberSet = true;
	}
	public void setPdfOremArchivedDate(Timestamp pdfOremArchivedDate) {
		this.pdfOremArchivedDate = pdfOremArchivedDate;
		this.pdfOremArchivedDateSet = true;
	}
	public void setPdfOremDriveName(String pdfOremDriveName) {
		this.pdfOremDriveName = pdfOremDriveName;
		this.pdfOremDriveNameSet = true;
	}
	public void setPdfOremDriveSerialNumber(String pdfOremDriveSerialNumber) {
		this.pdfOremDriveSerialNumber = pdfOremDriveSerialNumber;
		this.pdfOremDriveSerialNumberSet = true;
	}
	public void setPdfReady(Timestamp pdfReady) {
		this.pdfReady = pdfReady;
		this.pdfReadySet = true;
	}
	public void setPdfSentToLoad(Timestamp pdfSentToLoad) {
		this.pdfSentToLoad = pdfSentToLoad;
		this.pdfSentToLoadSet = true;
	}
	public void setPid(String pid) {
		this.pid = pid;
		this.pidSet = true;
	}
	public void setPriorityItem(String priorityItem) {
		this.priorityItem = priorityItem;
		this.priorityItemSet = true;
	}
	public void setRemarksAboutBook(String remarksAboutBook) {
		this.remarksAboutBook = remarksAboutBook;
		this.remarksAboutBookSet = true;
	}
	public void setRemarksFromScanCenter(String remarksFromScanCenter) {
		this.remarksFromScanCenter = remarksFromScanCenter;
		this.remarksFromScanCenterSet = true;
	}
	public void setUrl(String url) {
		this.url = url;
		this.urlSet = true;
	}
	public void setScanCompleteDate(Timestamp scanCompleteDate) {
		this.scanCompleteDate = scanCompleteDate;
		this.scanCompleteDateSet = true;
	}
	public void setScanIaCompleteDate(Timestamp scanIaCompleteDate) {
		this.scanIaCompleteDate = scanIaCompleteDate;
		this.scanIaCompleteDateSet = true;
	}
	public void setScanIaStartDate(Timestamp scanIaStartDate) {
		this.scanIaStartDate = scanIaStartDate;
		this.scanIaStartDateSet = true;
	}
	public void setScanImageAuditor(String scanImageAuditor) {
		this.scanImageAuditor = scanImageAuditor;
		this.scanImageAuditorSet = true;
	}
	public void setScanMachineId(String scanMachineId) {
		this.scanMachineId = scanMachineId;
		this.scanMachineIdSet = true;
	}
	public void setScanMetadataComplete(Timestamp scanMetadataComplete) {
		this.scanMetadataComplete = scanMetadataComplete;
		this.scanMetadataCompleteSet = true;
	}
	public void setRecordNumber(String recordNumber) {
		this.recordNumber = recordNumber;
		this.recordNumberSet = true;
	}
	public void setRequestingLocation(String requestingLocation) {
		this.requestingLocation = requestingLocation;
		this.requestingLocationSet = true;
	}
	public void setOwningInstitution(String owningInstitution) {
		this.owningInstitution = owningInstitution;
		this.owningInstitutionSet = true;
	}
	public void setScannedBy(String scannedBy) {
		this.scannedBy = scannedBy;
		this.scannedBySet = true;
	}
	public void setScanningSiteNotes(String scanningSiteNotes) {
		this.scanningSiteNotes = scanningSiteNotes;
		this.scanningSiteNotesSet = true;
	}
	public void setScanNumOfPages(String scanNumOfPages) {
		this.scanNumOfPages = scanNumOfPages;
		this.scanNumOfPagesSet = true;
	}
	public void setScanOperator(String scanOperator) {
		this.scanOperator = scanOperator;
		this.scanOperatorSet = true;
	}
	public void setScanStartDate(Timestamp scanStartDate) {
		this.scanStartDate = scanStartDate;
		this.scanStartDateSet = true;
	}
	public void setSite(String site) {
		this.site = site;
		this.siteSet = true;
	}
	public void setTiffCopy2ArchivedDate(Timestamp tiffCopy2ArchivedDate) {
		this.tiffCopy2ArchivedDate = tiffCopy2ArchivedDate;
		this.tiffCopy2ArchivedDateSet = true;
	}
	public void setTiffCopy2DriveName(String tiffCopy2DriveName) {
		this.tiffCopy2DriveName = tiffCopy2DriveName;
		this.tiffCopy2DriveNameSet = true;
	}
	public void setTiffCopy2DriveSerialNumber(String tiffCopy2DriveSerialNumber) {
		this.tiffCopy2DriveSerialNumber = tiffCopy2DriveSerialNumber;
		this.tiffCopy2DriveSerialNumberSet = true;
	}
	public void setTiffOremArchivedDate(Timestamp tiffOremArchivedDate) {
		this.tiffOremArchivedDate = tiffOremArchivedDate;
		this.tiffOremArchivedDateSet = true;
	}
	public void setTiffOremDriveName(String tiffOremDriveName) {
		this.tiffOremDriveName = tiffOremDriveName;
		this.tiffOremDriveNameSet = true;
	}
	public void setTiffOremDriveSerialNumber(String tiffOremDriveSerialNumber) {
		this.tiffOremDriveSerialNumber = tiffOremDriveSerialNumber;
		this.tiffOremDriveSerialNumberSet = true;
	} 
	public void setTnChangeHistory(String tnChangeHistory) {
		this.tnChangeHistory = tnChangeHistory;
		this.tnChangeHistorySet = true;
	}
	public void setWithdrawn(String withdrawn) {
		this.withdrawn = withdrawn;
		this.withdrawnSet = true;
	}
	public void setSubject (String subject) {
		this.subject = subject;
		this.subjectSet = true;
	}
	public void setFilmno (String filmno ) {
		this.filmno = filmno;
		this.filmnoSet = true;
	}
	public void setPagesPhysicalDescription (String pagesPhysicalDescription ) {
		this.pagesPhysicalDescription = pagesPhysicalDescription;
		this.pagesPhysicalDescriptionSet = true;
	}
	public void setSummary (String summary) {
		this.summary = summary;
		this.summarySet = true;
	}
	public void setDgsno (String dgsno ) {
		this.dgsno = dgsno;
		this.dgsnoSet = true;
	}
	public void setDateOriginal (Timestamp dateOriginal) {
		this.dateOriginal = dateOriginal;
		this.dateOriginalSet = true;
	}
	public void setPublisherOriginal (String publisherOriginal ) {
		this.publisherOriginal = publisherOriginal;
		this.publisherOriginalSet = true;
	}
	public void setFhcTitle (String fhcTitle ) {
		this.fhcTitle = fhcTitle;
		this.fhcTitleSet = true;
	}
	public void setFhcTn (String fhcTn ) {
		this.fhcTn = fhcTn;
		this.fhcTnSet = true;
	}
	public void setDateRepublished (Timestamp dateRepublished ) {
		this.dateRepublished = dateRepublished;
		this.dateRepublishedSet = true;
	}
	

	
	
	/////////////methods to check if update/set //////////////

	 
	public boolean isTnSet() {
		return tnSet;
	}
	public boolean isSecondaryIdentifierSet() {
		return secondaryIdentifierSet;
	}
	public boolean isOclcNumberSet() {
		return oclcNumberSet;
	}
	public boolean isIsbnIssnSet() {
		return isbnIssnSet;
	}
	public boolean isTitleSet() {
		return titleSet;
	}
	public boolean isAuthorSet() {
		return authorSet;
	}
	public boolean isPropertyRightSet() {
		return propertyRightSet;
	}
	public boolean isPublicationTypeSet() {
		return publicationTypeSet;
	}
	public boolean isFilenameSet() {
		return filenameSet;
	}
	public boolean isBatchClassSet() {
		return batchClassSet;
	}
	public boolean isCallNumberSet() {
		return callNumberSet;
	}
	public boolean isPartnerLibCallNumberSet() {
		return partnerLibCallNumberSet;
	}
	public boolean isCollectionSet() {
		return collectionSet;
	}
	public boolean isCompressionCodeSet() {
		return compressionCodeSet;
	}
	public boolean isDateLoadedSet() {
		return dateLoadedSet;
	}
	public boolean isDateReleasedSet() {
		return dateReleasedSet;
	}
	public boolean isDigitalCopyOnlySet() {
		return digitalCopyOnlySet;
	}
	public boolean isDnpSet() {
		return dnpSet;
	}
	public boolean isDnpDeletedOffLineSet() {
		return dnpDeletedOffLineSet;
	}
	public boolean isFilesReceivedByOremSet() {
		return filesReceivedByOremSet;
	}
	public boolean isFilesSentToOremSet() {
		return filesSentToOremSet;
	}
	public boolean isIaCompleteDateSet() {
		return iaCompleteDateSet;
	}
	public boolean isIaStartDateSet() {
		return iaStartDateSet;
	}
	public boolean isImageAuditSet() {
		return imageAuditSet;
	}
	public boolean isOcrBySet() {
		return ocrBySet;
	}
	public boolean isOcrStartDateSet() {
		return ocrStartDateSet;
	}
	public boolean isOcrCompleteDateSet() {
		return ocrCompleteDateSet;
	}
	public boolean isPdfDownloadBySet() {
		return pdfDownloadBySet;
	}
	public boolean isPdfDownloadDateSet() {
		return pdfDownloadDateSet;
	}
	public boolean isPdfreviewBySet() {
		return pdfreviewBySet;
	}
	public boolean isPdfreviewStartDateSet() {
		return pdfreviewStartDateSet;
	}
	public boolean isLanguageSet() {
		return languageSet;
	}
	public boolean isLoadedBySet() {
		return loadedBySet;
	}
	public boolean isLocationSet() {
		return locationSet;
	}
	public boolean isMediaTypeSet() {
		return mediaTypeSet;
	}
	public boolean isMetaDataCompleteSet() {
		return metadataCompleteSet;
	}
	public boolean isNumOfPagesSet() {
		return numOfPagesSet;
	}
	public boolean isPagesOnlineSet() {
		return pagesOnlineSet;
	}
	public boolean isPdfCopy2ArchivedDateSet() {
		return pdfCopy2ArchivedDateSet;
	}
	public boolean isPdfCopy2DriveNameSet() {
		return pdfCopy2DriveNameSet;
	}
	public boolean isPdfCopy2DriveSerialNumberSet() {
		return pdfCopy2DriveSerialNumberSet;
	}
	public boolean isPdfOremArchivedDateSet() {
		return pdfOremArchivedDateSet;
	}
	public boolean isPdfOremDriveNameSet() {
		return pdfOremDriveNameSet;
	}
	public boolean isPdfOremDriveSerialNumberSet() {
		return pdfOremDriveSerialNumberSet;
	}
	public boolean isPdfReadySet() {
		return pdfReadySet;
	}
	public boolean isPdfSentToLoadSet() {
		return pdfSentToLoadSet;
	}
	public boolean isPidSet() {
		return pidSet;
	}
	public boolean isPriorityItemSet() {
		return priorityItemSet;
	}
	public boolean isRemarksAboutBookSet() {
		return remarksAboutBookSet;
	}
	public boolean isRemarksFromScanCenterSet() {
		return remarksFromScanCenterSet;
	}
	public boolean isUrlSet() {
		return urlSet;
	}
	public boolean isScanCompleteDateSet() {
		return scanCompleteDateSet;
	}
	public boolean isScanIaCompleteDateSet() {
		return scanIaCompleteDateSet;
	}
	public boolean isScanIaStartDateSet() {
		return scanIaStartDateSet;
	}
	public boolean isScanImageAuditorSet() {
		return scanImageAuditorSet;
	}
	public boolean isScanMachineIdSet() {
		return scanMachineIdSet;
	}
	public boolean isScanMetadataCompleteSet() {
		return scanMetadataCompleteSet;
	}
	public boolean isRecordNumberSet() {
		return recordNumberSet;
	}
	public boolean isRequestingLocationSet() {
		return requestingLocationSet;
	}
	public boolean isOwningInstitutionSet() {
		return owningInstitutionSet;
	}
	public boolean isScannedBySet() {
		return scannedBySet;
	}
	public boolean isScanningSiteNotesSet() {
		return scanningSiteNotesSet;
	}
	public boolean isScanNumOfPagesSet() {
		return scanNumOfPagesSet;
	}
	public boolean isScanOperatorSet() {
		return scanOperatorSet;
	}
	public boolean isScanStartDateSet() {
		return scanStartDateSet;
	} 
	public boolean isSiteSet() {
		return siteSet;
	}
	public boolean isTiffCopy2ArchivedDateSet() {
		return tiffCopy2ArchivedDateSet;
	}
	public boolean isTiffCopy2DriveNameSet() {
		return tiffCopy2DriveNameSet;
	}
	public boolean isTiffCopy2DriveSerialNumberSet() {
		return tiffCopy2DriveSerialNumberSet;
	}
	public boolean isTiffOremArchivedDateSet() {
		return tiffOremArchivedDateSet;
	}
	public boolean isTiffOremDriveNameSet() {
		return tiffOremDriveNameSet;
	}
	public boolean isTiffOremDriveSerialNumberSet() {
		return tiffOremDriveSerialNumberSet;
	}
	public boolean isTnChangeHistorySet() {
		return tnChangeHistorySet;
	}
	public boolean isWithdrawnSet() {
		return withdrawnSet;
	}
	public boolean isSubjectSet() {
		return subjectSet;
	}
	public boolean isFilmnoSet() {
		return filmnoSet;
	}
	public boolean isPagesPhysicalDescriptionSet() {
		return pagesPhysicalDescriptionSet;
	}
	public boolean isSummarySet() {
		return summarySet;
	}
	public boolean isDgsnoSet() {
		return dgsnoSet;
	}
	public boolean isDateOriginalSet() {
		return dateOriginalSet;
	}
	public boolean isPublisherOriginalSet() {
		return publisherOriginalSet;
	}
	public boolean isFhcTitleSet() {
		return fhcTitleSet;
	}
	public boolean isFhcTnSet() {
		return fhcTnSet;
	}
	public boolean isDateRepublishedSet() {
		return dateRepublishedSet;
	}
}