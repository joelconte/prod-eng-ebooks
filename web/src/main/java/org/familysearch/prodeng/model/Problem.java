/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.model;

import java.io.Serializable;
import java.sql.Timestamp;

 
public class Problem implements Serializable {

	private static final long serialVersionUID = 1L;

    private String pn = "";
    private String tn = "";
    private String status = "";
    private String problemReason = "";
    private String problemText = "";
    private Timestamp problemDate = null;
    private String problemInitials = "";
    private String solutionText = "";
    private Timestamp solutionDate = null;
    private String solutionInitials = ""; 
    
    //since html form will only set values that are on a particular form, need to keep track of changed data
    private boolean pnSet = false; 
    private boolean tnSet = false;
    private boolean statusSet = false;
    private boolean problemReasonSet = false;
    private boolean problemTextSet = false;
    private boolean problemDateSet = false;
    private boolean problemInitialsSet = false;
    private boolean solutionTextSet = false;
    private boolean solutionDateSet = false;
    private boolean solutionInitialsSet = false;
   
    
 
    //////////////get methods////////////
	public String getTn() {
		return tn;
	}
	public String getPn() {
		return pn;
	}
	public String getStatus() {
		return status;
	}
	public String getProblemReason() {
		return problemReason;
	}
	public String getProblemText() {
		return problemText;
	}
	public Timestamp getProblemDate() {
		return problemDate;
	}
	public String getProblemInitials() {
		return problemInitials;
	} 
	public String getSolutionText() {
		return solutionText;
	}
	public Timestamp getSolutionDate() {
		return solutionDate;
	}
	public String getSolutionInitials() {
		return solutionInitials;
	} 
	
	
	///////////////set methods///////////////
	
	
	public void setTn(String tn) {
		this.tn = tn;
		this.tnSet = true;
	}

	public void setPn(String pn) {
		this.pn = pn;
		this.pnSet = true;
	}
	
	public void setStatus(String status) {
		this.status = status;
		this.statusSet = true;
	}
	public void setProblemReason(String problemReason) {
		this.problemReason = problemReason;
		this.problemReasonSet = true;
	}

	public void setProblemText(String problemText) {
		this.problemText = problemText;
		this.problemTextSet = true;
	}
	public void setProblemDate(Timestamp problemDate) {
		this.problemDate = problemDate;
		this.problemDateSet = true;
	}

	public void setProblemInitials(String initials) {
		this.problemInitials = initials;
		this.problemInitialsSet = true;
	} 
	
	public void setSolutionText(String solutionText) {
		this.solutionText = solutionText;
		this.solutionTextSet = true;
	}
	public void setSolutionDate(Timestamp solutionDate) {
		this.solutionDate = solutionDate;
		this.solutionDateSet = true;
	}

	public void setSolutionInitials(String initials) {
		this.solutionInitials = initials;
		this.solutionInitialsSet = true;
	} 
	
	
	/////////////methods to check if update/set //////////////

	 
	public boolean isTnSet() {
		return tnSet;
	}
	public boolean isPnSet() {
		return pnSet;
	}
	public boolean isStatusSet() {
		return statusSet;
	}
	public boolean isProblemReasonSet() {
		return problemReasonSet;
	}
	public boolean isProblemTextSet() {
		return problemTextSet;
	}
	public boolean isProblemDateSet() {
		return problemDateSet;
	}
	public boolean isProblemInitialsSet() {
		return problemInitialsSet;
	} 
	public boolean isSolutionTextSet() {
		return solutionTextSet;
	}
	public boolean isSolutionDateSet() {
		return solutionDateSet;
	}
	public boolean isSolutionInitialsSet() {
		return solutionInitialsSet;
	} 


}
