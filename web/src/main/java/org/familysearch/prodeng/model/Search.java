/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.model;

import java.io.Serializable;
import java.sql.Timestamp;
 
public class Search implements Serializable {

	private static final long serialVersionUID = 1L;

    private String searchId = "";
    private String description = "";
    private String queryText = "";
    private String owner = "";
    private Timestamp dateUpdated = null;
    
    //since html form will only set values that are on a particular form, need to keep track of changed data
    private boolean searchIdSet = false; 
    private boolean descriptionSet = false;
    private boolean queryTextSet = false;
    private boolean ownerSet = false;
    private boolean dateUpdatedSet = false;
   
    
 
    //////////////get methods////////////
    public String getSearchId() {
		return searchId;
	}
    public String getDescription() {
		return description;
	}
    public String getQueryText() {
		return queryText;
	}
    public String getOwner() {
		return owner;
	}
    public Timestamp getDateUpdated() {
		return dateUpdated;
	}
 
	
	
	///////////////set methods///////////////
	
    public void setSearchId(String searchId) {
		this.searchId = searchId;
		this.searchIdSet = true;
	}
    public void setDescription(String description) {
		this.description = description;
		this.descriptionSet = true;
	}
    public void setQueryText(String queryText) {
		this.queryText = queryText;
		this.queryTextSet = true;
	}
    public void setOwner(String owner) {
		this.owner = owner;
		this.ownerSet = true;
	}
    public void setDateUpdated(Timestamp dateUpdated) {
		this.dateUpdated = dateUpdated;
		this.dateUpdatedSet = true;
	}
	
	
	// ///////////methods to check if update/set //////////////
	
    public boolean isSearchIdSet() {
		return searchIdSet;
	}

	public boolean isDescriptionSet() {
		return descriptionSet;
	}

	public boolean isQueryTextSet() {
		return queryTextSet;
	}

	public boolean isOwnerSet() {
		return ownerSet;
	}

	public boolean isDateUpdatedSet() {
		return dateUpdatedSet;
	}

}
