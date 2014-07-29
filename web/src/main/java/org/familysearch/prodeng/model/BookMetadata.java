/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Book object that maps to DB table BOOKMETADATA
 */
public class BookMetadata implements Serializable {

	private static final long serialVersionUID = 1L;

    private String title;
    private String author;
    private String subject;
    private String titleno= "";//have some checks for empty tn
    private String oclcNumber;
    private String isbnIssn;
    private String callno;
    private String partnerLibCallno;
    private String filmno;
    private String pages;
    private String summary;
    private String dgsno;
    private String language;
    private String owningInstitution;
    private String requestingLocation;    
    private String scanningLocation;    
    private String recordNumber;  
    private Timestamp dateOriginal;
    private String publisherOriginal;
    private String filename;
    
    private Timestamp dateAdded;
    private String metadataAdder;
    private Timestamp checkComplete;
    private String checker;
    private Timestamp sentToScan;
    private String sender;
    
    //since html form will only set values that are on a particular form, need to keep track of changed data
    private	boolean	titleSet	=	false;    
    private	boolean	authorSet	=	false;
    private	boolean	subjectSet	=	false;
    private	boolean	titlenoSet	=	false;
    private	boolean	oclcNumberSet	=	false;
    private	boolean	isbnIssnSet	=	false;
    private	boolean	callnoSet	=	false;
    private	boolean	partnerLibCallnoSet	=	false;
    private	boolean	filmnoSet	=	false;
    private	boolean	pagesSet	=	false;
    private	boolean	summarySet	=	false;
    private	boolean	dgsnoSet	=	false;
    private	boolean	languageSet	=	false;
    private boolean owningInstitutionSet = false;
    private	boolean	requestingLocationSet	=	false;
    private	boolean	scanningLocationSet	=	false;
    private	boolean	recordNumberSet	=	false;
    private boolean dateOriginalSet	=	false;
    private boolean publisherOriginalSet	=	false;
    private	boolean	dateAddedSet	=	false;
    private	boolean	metadataAdderSet	=	false;
    private	boolean	checkCompleteSet	=	false;
    private	boolean	checkerSet	=	false;
    private	boolean	sentToScanSet	=	false;
    private	boolean	senderSet	=	false;
    private boolean filenameSet = false; 
    
    
 
    //////////////get methods////////////
    
    
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public String getSubject() {
		return subject;
	}
	public String getTitleno() {
		return titleno;
	}
	public String getOclcNumber() {
		return oclcNumber;
	}
	public String getIsbnIssn() {
		return isbnIssn;
	}
 
	public String getCallno() {
		return callno;
	}
	public String getPartnerLibCallno() {
		return partnerLibCallno;
	}
	public String getFilmno() {
		return filmno;
	}
	public String getPages() {
		return pages;
	}
	public String getSummary() {
		return summary;
	}
	public String getDgsno() {
		return dgsno;
	}
	public String getLanguage() {
		return language;
	}
	public String getOwningInstitution() {
		return owningInstitution;
	}
	public String getRequestingLocation() {
		return requestingLocation;
	} 
	public String getScanningLocation() {
		return scanningLocation;
	} 
	public String getRecordNumber() {
		return recordNumber;
	} 
	
	public Timestamp getDateOriginal() {
		return dateOriginal;
	}	
	public String getPublisherOriginal() {
		return publisherOriginal;
	} 
	
	public Timestamp getDateAdded() {
		return dateAdded;
	}
	public String getMetadataAdder() {
		return metadataAdder;
	}
	public Timestamp getCheckComplete() {
		return checkComplete;
	}
	public String getChecker() {
		return checker;
	}
	public Timestamp getSentToScan() {
		return sentToScan;
	}
	public String getSender() {
		return sender;
	}
	public String getFilename() {
		return filename;
	}

	
	
	///////////////set methods///////////////
	
	
 
	public void setTitle(String title) {
		this.title = title;
		this.titleSet = true;
	}
	public void setAuthor(String author ) {
		this.author = author;
		this.authorSet = true;
	}
	public void setSubject(String subject) {
		this.subject = subject;
		this.subjectSet = true;
	}
	public void setTitleno(String titleno) {
		this.titleno = titleno;
		this.titlenoSet = true;
	}
	public void setOclcNumber (String oclcNumber) {
		this.oclcNumber = oclcNumber;
		this.oclcNumberSet = true;
	}
	public void setIsbnIssn(String isbnIssn) {
		this.isbnIssn = isbnIssn;
		this.isbnIssnSet = true;
	}
	public void setCallno(String callno) {
		this.callno = callno;
		this.callnoSet = true;
	}
	public void setPartnerLibCallno(String partnerLibCallno) {
		this.partnerLibCallno = partnerLibCallno;
		this.partnerLibCallnoSet = true;
	}
	public void setFilmno(String filmno ) {
		this.filmno = filmno;
		this.filmnoSet = true;
	}
	public void setPages(String pages ) {
		this.pages = pages;
		this.pagesSet = true;
	}
	public void setSummary(String summary ) {
		this.summary = summary;
		this.summarySet = true;
	}
	public void setDgsno(String dgsno) {
		this.dgsno = dgsno;
		this.dgsnoSet = true;
	}
	public void setLanguage(String language) {
		this.language = language;
		this.languageSet = true;
	}
	public void setOwningInstitution(String owningInstitution) {
		this.owningInstitution = owningInstitution;
		this.owningInstitutionSet = true;
	}
	public void setRequestingLocation(String requestingLocation) {
		this.requestingLocation = requestingLocation;
		this.requestingLocationSet = true;
	} 
	public void setScanningLocation(String scanningLocation) {
		this.scanningLocation = scanningLocation;
		this.scanningLocationSet = true;
	} 
	public void setRecordNumber(String recordNumber) {
		this.recordNumber = recordNumber;
		this.recordNumberSet = true;
	} 
	public void setDateOriginal(Timestamp dateOriginal) {
		this.dateOriginal = dateOriginal;
		this.dateOriginalSet = true;
	}	
	public void setPublisherOriginal(String publisherOriginal) {
		this.publisherOriginal = publisherOriginal;
		this.publisherOriginalSet = true;
	} 
	public void setFilename(String filename) {
		this.filename = filename;
		this.filenameSet = true;
	}
	
	
	
	public void setDateAdded(Timestamp dateAdded ) {
		this.dateAdded = dateAdded;
		this.dateAddedSet = true;
	}
	public void setMetadataAdder(String metadataAdder) {
		this.metadataAdder = metadataAdder;
		this.metadataAdderSet = true;
	}
	public void setCheckComplete(Timestamp checkComplete  ) {
		this.checkComplete = checkComplete;
		this.checkCompleteSet = true;
	}
	public void setChecker(String checker ) {
		this.checker = checker;
		this.checkerSet = true;
	}
	public void setSentToScan(Timestamp d  ) {
		this.sentToScan = d;
		this.sentToScanSet = true;
	}
	public void setSender(String sender) {
		this.sender = sender;
		this.senderSet = true;
	}
	
	/////////////methods to check if update/set //////////////

	 
	 
	public boolean isTitleSet() {
		return titleSet;
	}
	public boolean isAuthorSet() {
		return authorSet;
	}
	public boolean isSubjectSet() {
		return subjectSet;
	}
	public boolean isTitlenoSet() {
		return titlenoSet;
	}
	public boolean isOclcNumberSet() {
		return oclcNumberSet;
	}
	public boolean isIsbnIssnSet() {
		return isbnIssnSet;
	}
	public boolean isCallnoSet() {
		return callnoSet;
	}
	public boolean isPartnerLibCallnoSet() {
		return partnerLibCallnoSet;
	}
	public boolean isFilmnoSet() {
		return filmnoSet;
	}
	public boolean isPagesSet() {
		return pagesSet;
	}
	public boolean isSummarySet() {
		return summarySet;
	}
	public boolean isDgsnoSet() {
		return dgsnoSet;
	}
	public boolean isLanguageSet() {
		return languageSet;
	}
	public boolean isOwningInstitutionSet() {
		return owningInstitutionSet;
	}
	public boolean isRequestingLocationSet() {
		return requestingLocationSet;
	} 
	public boolean isScanningLocationSet() {
		return scanningLocationSet;
	} 
	public boolean isRecordNumberSet() {
		return recordNumberSet;
	} 
	
	public boolean isDateOriginalSet() {
		return dateOriginalSet;
	}
	public boolean isPublisherOriginalSet() {
		return publisherOriginalSet;
	}
	public boolean isFilenameSet() {
		return filenameSet;
	}
	
	
	
	public boolean isDateAddedSet() {
		return dateAddedSet;
	}
	public boolean isMetadataAdderSet() {
		return metadataAdderSet;
	}
	public boolean isCheckCompleteSet() {
		return checkCompleteSet;
	}
	public boolean isCheckerSet() {
		return checkerSet;
	}
	public boolean isSentToScanSet() {
		return sentToScanSet;
	}
	public boolean isSenderSet() {
		return senderSet;
	}



}
