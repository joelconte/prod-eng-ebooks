/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.service;
 
import java.util.List;
 

 
public interface BookServiceReadOnly {
 
	public List<List<String>> runQuery(String queryText);
	 
}
