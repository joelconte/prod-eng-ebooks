/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Book object that maps to DB table BOOK
 */
public class NonBook implements Serializable {

	private static final long serialVersionUID = 1L;

	//document number
    private String dn = "";
    
    private String title;
     
    private String author;
    private String filename;
    private String mediaType;
    private String language;
    private String remarksFromScanCenter;
    private String remarksAboutBook;
    private String requestingLocation;
    private String owningInstitution;
    private String scannedBy;
    private String scanOperator;
    private String scanMachineId; 
    private Timestamp scanStartDate;
    private Timestamp scanCompleteDate;
    private String scanImageAuditor;
    private Timestamp scanIaStartDate;
    private Timestamp scanIaCompleteDate; 
    private String scanNumOfPages;
             
    
    //since html form will only set values that are on a particular form, need to keep track of changed data
    private boolean dnSet = false;
    private boolean titleSet = false;
    private boolean authorSet = false;
    private boolean filenameSet = false; 
    private boolean mediaTypeSet = false; 
    private boolean languageSet = false;
    private boolean remarksFromScanCenterSet = false;
    private boolean remarksAboutBookSet = false; 
    private boolean requestingLocationSet = false;
    private boolean owningInstitutionSet = false;
    private boolean scannedBySet = false;
    private boolean scanOperatorSet = false;
    private boolean scanMachineIdSet = false; 
    private boolean scanStartDateSet = false;
    private boolean scanCompleteDateSet = false;
    private boolean scanImageAuditorSet = false;
    private boolean scanIaStartDateSet = false;
    private boolean scanIaCompleteDateSet = false; 
    private boolean scanNumOfPagesSet = false; 
    
    
 
    //////////////get methods////////////
	public String getDn() {
		return dn;
	} 
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	 
	public String getFilename() {
		return filename;
	}
	  
	public String getLanguage() {
		return language;
	}
	 
	public String getMediaType() {
		return mediaType;
	}
  
	public String getRemarksAboutBook() {
		return remarksAboutBook;
	}
	public String getRemarksFromScanCenter() {
		return remarksFromScanCenter;
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
 
	public String getRequestingLocation() {
		return requestingLocation;
	}
	public String getOwningInstitution() {
		return owningInstitution;
	}
	public String getScannedBy() {
		return scannedBy;
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
 
	
	///////////////set methods///////////////
	
	
	public void setDn(String dn) {
		this.dn = dn;
		this.dnSet = true;
	} 
	public void setTitle(String title) {
		this.title = title;
		this.titleSet = true;
	}
 
	public void setFilename(String filename) {
		this.filename = filename;
		this.filenameSet = true;
	}
	
	public void setAuthor(String author) {
		this.author = author;
		this.authorSet = true;
	}
	 
	public void setLanguage(String language) {
		this.language = language;
		this.languageSet = true;
	} 
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
		this.mediaTypeSet = true;
	} 
	public void setRemarksAboutBook(String remarksAboutBook) {
		this.remarksAboutBook = remarksAboutBook;
		this.remarksAboutBookSet = true;
	}
	public void setRemarksFromScanCenter(String remarksFromScanCenter) {
		this.remarksFromScanCenter = remarksFromScanCenter;
		this.remarksFromScanCenterSet = true;
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
	
	/////////////methods to check if update/set //////////////

	 
	public boolean isDnSet() {
		return dnSet;
	} 
	public boolean isTitleSet() {
		return titleSet;
	}
	public boolean isAuthorSet() {
		return authorSet;
	} 
	public boolean isFilenameSet() {
		return filenameSet;
	} 
	 
	public boolean isLanguageSet() {
		return languageSet;
	} 
	public boolean isMediaTypeSet() {
		return mediaTypeSet;
	} 
	public boolean isRemarksAboutBookSet() {
		return remarksAboutBookSet;
	}
	public boolean isRemarksFromScanCenterSet() {
		return remarksFromScanCenterSet;
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
	public boolean isRequestingLocationSet() {
		return requestingLocationSet;
	}
	public boolean isOwningInstitutionSet() {
		return owningInstitutionSet;
	}
	public boolean isScannedBySet() {
		return scannedBySet;
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
}