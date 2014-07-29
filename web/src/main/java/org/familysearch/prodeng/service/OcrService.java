/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.service;
 
import org.familysearch.prodeng.model.BookMetadata;

 
public interface OcrService {
	public void insertOcrBookMetadata(BookMetadata b);
 
	public void deleteOcrBookMetadata(BookMetadata b); 
	public void deleteOcrBookMetadata(String titleno);
}
