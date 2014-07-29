/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.service;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.validation.Validator;

import org.familysearch.prodeng.model.BookMetadata;
import org.familysearch.prodeng.model.SqlTimestampPropertyEditor;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

@Service("ocrService")
public class OcrServiceImpl extends NamedParameterJdbcDaoSupport implements OcrService {

 
	private SimpleJdbcInsert ocrModelJdbcInsert;
	private Validator validator; 
	private SqlTimestampPropertyEditor tsConvert = new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a");
	private String ocrTable;
	private MessageSource messageSource;
	
	OcrServiceImpl() {
		//In case cglib is being used.
	}

	@Inject
	public OcrServiceImpl(Validator validator, DataSource dataSource2 ) {
		
		Properties p = new Properties();
		try {
			InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("messages.properties");
			p.load(propStream);
			ocrTable = p.getProperty("ocrDb.tableName");
			
		}catch(Exception e) {
			ocrTable = "paultest";
		}
		this.validator = validator;
		setDataSource(dataSource2);//required to get jdbctemplate from jdbcDaoSupport..
	}
 
	
	@Override
	public void insertOcrBookMetadata(BookMetadata b) {
		//kofax is gone...remove
		if(true)
			return;
		
		
		
		List<String> alreadyExist = getOcrTn(b.getTitleno());
		if(alreadyExist != null && alreadyExist.size() > 0 ) {
			//delete old row first
			deleteOcrBookMetadata(b);
		}
		
		String sql = "insert into " + ocrTable;
		String colList  = "";
		String valList = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		//always insert id
		colList += "field02, ";
		valList += ":field02, ";
		params.put("field02", b.getTitleno());
		
		if (b.isTitleSet()) {
			colList += "field01, ";
			valList += ":field01, ";
			params.put("field01", b.getTitle());
		}

		if (b.isPagesSet()) {
			colList += "field03, ";
			valList += ":field03, ";
			params.put("field03", b.getPages());
		}
 
		if (b.isAuthorSet()) {
			colList += "field04, ";
			valList += ":field04, ";
			params.put("field04", b.getAuthor());
		}

		if (b.isSubjectSet()) {
			colList += "field05, ";
			valList += ":field05, ";
			params.put("field05", b.getSubject());
		}
		
		//added getPartnerLibCallno
		//if (b.isCallnoSet()) {
			//if fhl book then use kofax field06 else use field10
			//if(b.getRequestingLocation().equals("Family History Library") || b.getRequestingLocation().equals("FHL"))
			//{
				colList += "field06, ";
				valList += ":field06, ";
				params.put("field06", b.getCallno());
		//	}else {
				colList += "field10, ";
				valList += ":field10, ";
				params.put("field10", b.getPartnerLibCallno());
			//}
		//}
	 

		if (b.isSummarySet()) {
			colList += "field07, ";
			valList += ":field07, ";
			params.put("field07", b.getSummary());
		}
 

		if (b.isFilmnoSet()) {
			colList += "field08, ";
			valList += ":field08, ";
			params.put("field08", b.getFilmno());
		}
 
		if (b.isDgsnoSet()) {
			colList += "field09, ";
			valList += ":field09, ";
			params.put("field09", b.getDgsno());
		} 
		
		/* Jeri note: since multiple languages, don't populate
		if (b.isLanguageSet()) {
			colList += "field16, ";
			valList += ":field16, ";
			params.put("field16", b.getLanguage());
		}
		*/
		 
		if (b.isDateOriginalSet()) {
			colList += "field11, ";
			valList += ":field11, ";
			params.put("field11", b.getDateOriginal());
		}
		if (b.isPublisherOriginalSet()) {
			colList += "field12, ";
			valList += ":field12, ";
			params.put("field12", b.getPublisherOriginal());
		}
 
		//trim comma
		colList = colList.substring(0, colList.length() - 2);
		valList = valList.substring(0, valList.length() - 2);
		
		sql += " (" + colList + ") values (" + valList + ")";
		getNamedParameterJdbcTemplate().update(sql, params);	
	} 
	

	@Override
	public void deleteOcrBookMetadata(BookMetadata b) {
		//kofax is gone...remove
		if(true)
			return;
				
		String sql = "DELETE FROM " + ocrTable + " where field02 = '" + b.getTitleno() + "'" ;
	    getJdbcTemplate().update(sql);
	}
	@Override
	public void deleteOcrBookMetadata(String titleno) {
		//kofax is gone...remove
		if(true)
			return;
				
		String sql = "DELETE FROM " + ocrTable + " where field02 = '" + titleno + "'" ;
	    getJdbcTemplate().update(sql);
	}
	
	public List<String> getOcrTn(String titleno) {
		//kofax is gone...remove
		if(true)
			return null;
				
		List<String> tnList = getJdbcTemplate().query("select field02 from " + ocrTable + " where field02 = '" + titleno + "'" , new StringRowMapper());
		return tnList;
	}
	 

	private static class StringRowMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String tn = rs.getString(1);
			return tn;
		}
	}


}