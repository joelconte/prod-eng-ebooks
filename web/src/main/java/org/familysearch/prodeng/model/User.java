/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Book object that maps to DB table users and others
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

    private String userId = "";
    private String name = "";
    private String entryPage = "";
    private String primaryLocation = "";
    private String email = "";
    private String sendScanNotice = "F";
    private List<String> authorities = null;
    private Timestamp lastLoggedIn = null;
    private Timestamp dateAdded = null;
    private Timestamp dateUpdated = null;
    
    //since html form will only set values that are on a particular form, need to keep track of changed data
    private boolean userIdSet = false; 
    private boolean nameSet = false;
    private boolean primaryLocationSet = false;
    private boolean entryPageSet = false;
    private boolean emailSet = false;
    private boolean sendScanNoticeSet = false;
    private boolean authoritiesSet = false;
   
    
 
    //////////////get methods////////////
	public String getUserId() {
		return userId;
	}
	public String getName() {
		return name;
	}
	public String getEntryPage() {
		return entryPage;
	}
	public String getPrimaryLocation() {
		return primaryLocation;
	}
	public String getEmail() {
		return email;
	}
	public String getSendScanNotice() {
		return sendScanNotice;
	}
	public Timestamp getLastLoggedIn() {
		return lastLoggedIn;
	}
	public Timestamp getDateAdded() {
		return dateAdded;
	}
	public Timestamp getDateUpdated() {
		return dateUpdated;
	}
	public List<String> getAuthorities() {
		return authorities;
	}
 
	
	
	///////////////set methods///////////////

	
	public void setUserId(String userId) {
		this.userId = userId;
		this.userIdSet = true;
	}

	public void setName(String name) {
		this.name = name;
		this.nameSet = true;
	}
	public void setPrimaryLocation(String loc) {
		this.primaryLocation = loc;
		this.primaryLocationSet = true;
	}
	public void setEmail(String email) {
		this.email = email;
		this.emailSet = true;
	}
	public void setSendScanNotice(String sendScanNotice) {
		this.sendScanNotice = sendScanNotice;
		this.sendScanNoticeSet = true;
	}
	public void setEntryPage(String page) {
		this.entryPage = page;
		this.entryPageSet = true;
	}
	public void setLastLoggedIn(Timestamp t) {
		this.lastLoggedIn = t;
	}

	public void setDateAdded(Timestamp t) {
		this.dateAdded = t;
	}
	public void setDateUpdated(Timestamp t) {
		this.dateUpdated = t;
	}
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
		this.authoritiesSet = true;
	}
 
	
	
	/////////////methods to check if update/set //////////////

	 
	public boolean isUserIdSet() {
		return userIdSet;
	}

	public boolean isNameSet() {
		return nameSet;
	}
	public boolean isEntryPageSet() {
		return entryPageSet;
	} 
	public boolean isPrimaryLocationSet() {
		return primaryLocationSet;
	} 
	public boolean isEmailSet() {
		return emailSet;
	} 
	public boolean isSendScanNoticeSet() {
		return sendScanNoticeSet;
	} 
	public boolean isAuthoritiesSet() {
		return authoritiesSet;
	} 



}
