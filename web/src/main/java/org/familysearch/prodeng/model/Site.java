/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.model;

import java.io.Serializable;
import java.util.List;

/**
 * Book object that maps to DB table sites and others
 */
public class Site implements Serializable {

	private static final long serialVersionUID = 1L;

    private String siteId = "";
    private String publishName = "";
    private String location = "";
    private String contact = "";
    private String numberOfOperators = "";
    private String isFhc = "F";
    private String isPartnerInstitution = "F";
    private String isScanSite = "F";
    private String isProcessSite = "F";
    private String isPhysicalBookSite = "F"; 
    private String isInactiveSite = "F"; 
    private List<List<String>> goals = null;
      
    private boolean siteIdSet = false;
    private boolean publishNameSet = false;
    private boolean locationSet = false;
    private boolean contactSet = false;
    private boolean numberOfOperatorsSet = false;
    private boolean isFhcSet = false;
    private boolean isPartnerInstitutionSet = false;
    private boolean isScanSiteSet = false;
    private boolean isProcessSiteSet = false;
    private boolean isPhysicalBookSiteSet = false; 
    private boolean isInactiveSiteSet = false; 
    private boolean isGoalsSet = false;
     
 
    
    //////////////get methods////////////
	public String getSiteId() {
		return siteId;
	}
 
	public String getPublishName() {
		return publishName;
	}
	public String getLocation() {
		return location;
	}
	public String getContact() {
		return contact;
	}
	public String getNumberOfOperators() {
		return numberOfOperators;
	}
	public String getIsFhc() {
		return isFhc;
	}
	public String getIsPartnerInstitution() {
		return isPartnerInstitution;
	}
	public String getIsScanSite() {
		return isScanSite;
	}
	public String getIsPhysicalBookSite() {
		return isPhysicalBookSite;
	} 
	public String getIsProcessSite() {
		return isProcessSite;
	}
	public String getIsInactiveSite() {
		return isInactiveSite;
	}
	public List<List<String>> getGoals() {
		return goals;
	}
	
	
	///////////////set methods///////////////

	
	public void setSiteId(String siteId) {
		this.siteId = siteId;
		this.siteIdSet = true;
	}
 

	public void setPublishName(String publishName) {
		this.publishName = publishName;
		this.publishNameSet = true;
	}


	public void setLocation(String location) {
		this.location = location;
		this.locationSet = true;
	}
	public void setContact(String contact) {
		this.contact = contact;
		this.contactSet = true;
	}
	public void setNumberOfOperators(String numberOfOperators) {
		this.numberOfOperators = numberOfOperators;
		this.numberOfOperatorsSet = true;
	}
	public void setIsFhc(String isFhc) {
		this.isFhc = isFhc;
		this.isFhcSet = true;
	}

	public void setIsPartnerInstitution(String isPartnerInstitution) {
		this.isPartnerInstitution = isPartnerInstitution;
		this.isPartnerInstitutionSet = true;
	}
	
	public void setIsScanSite(String isScanSite) {
		this.isScanSite = isScanSite;
		this.isScanSiteSet = true;
	}
	
	public void setIsPhysicalBookSite(String isPhysicalBookSite) {
		this.isPhysicalBookSite = isPhysicalBookSite;
		this.isPhysicalBookSiteSet = true;
	} 
	
	public void setIsProcessSite(String isProcessSite) {
		this.isProcessSite = isProcessSite;
		this.isProcessSiteSet = true;
	}
	
	public void setIsInactiveSite(String isInactiveSite) {
		this.isInactiveSite = isInactiveSite;
		this.isInactiveSiteSet = true;
	}

	public void setGoals( List<List<String>> goals) {
		this.goals = goals;
		this.isGoalsSet = true;
	}

	
	/////////////methods to check if update/set //////////////

	public boolean isSiteIdSet() {
		return siteIdSet;
	}
	public boolean isPublishNameSet() {
		return publishNameSet;
	}
	public boolean isLocationSet() {
		return locationSet;
	}
	public boolean isContactSet() {
		return contactSet;
	}
	public boolean isNumberOfOperatorsSet() {
		return numberOfOperatorsSet;
	}
	public boolean isIsFhcSet() {
		return isFhcSet;
	}
	public boolean isIsPartnerInstitutionSet() {
		return isPartnerInstitutionSet;
	}
	public boolean isIsScanSiteSet() {
		return isScanSiteSet;
	}
	public boolean isIsPhysicalBookSiteSet() {
		return isPhysicalBookSiteSet;
	} 
	public boolean isIsProcessSiteSet() {
		return isProcessSiteSet;
	}
	public boolean isIsInactiveSiteSet() {
		return isInactiveSiteSet;
	}
	public boolean isIsGoalsSet() {
		return isGoalsSet;
	}



}
