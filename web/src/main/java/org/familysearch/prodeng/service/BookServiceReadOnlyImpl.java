/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.familysearch.prodeng.service;
//same as BookServiceImpl, but this one has a "select" only datasource userid in database.
//so i think there is only one method that id currently used...

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.validation.Validator;

import org.familysearch.prodeng.model.SqlTimestampPropertyEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
//not import org.springframework.beans.factory.parsing.Problem;

@Service("bookServiceReadOnly")
public class BookServiceReadOnlyImpl extends NamedParameterJdbcDaoSupport implements BookServiceReadOnly {
 
	private DataSource ds;
	private SimpleJdbcInsert bookModelJdbcInsert; 
	private Validator validator; 
	private SqlTimestampPropertyEditor tsConvert = new SqlTimestampPropertyEditor("M/dd/yyyy h:mm a");
	private List<String> badWords = new ArrayList<String>();
	{
		badWords.add("insert");
		badWords.add("update");
		badWords.add("delete");
		badWords.add("drop");
		badWords.add("alter");
	}
	
	BookServiceReadOnlyImpl() {
		//In case cglib is being used.
	}

	@Inject
	public BookServiceReadOnlyImpl(Validator validator, DataSource dataSourceQuery ) {
		this.ds = dataSourceQuery;
		this.validator = validator;
		setDataSource(dataSourceQuery);//required to get jdbctemplate from jdbcDaoSupport..
	}
	 
	@Override 
	public List<List<String>> runQuery(String queryText){
		String lowerQueryText = queryText.toLowerCase();
		if(lowerQueryText.contains("update") || lowerQueryText.contains("insert") || lowerQueryText.contains("delete") || lowerQueryText.contains("alter")) {
			//allow many
			String[] commands = queryText.split(";end");

			for(int x = 0; x < commands.length; x++) {
				String command = commands[x];
				if(command.endsWith(";"))
					command = command.substring(0, command.length()-1);
				
				if(command.equals("\r\n") == false){
					getJdbcTemplate().update(command);
				}
			}
		}else {
			
			int ind = queryText.indexOf(";");
			if(ind != -1)
				queryText = queryText.substring(0, ind );
			
			List<List<String>> sList = getJdbcTemplate().query(queryText, new ExtractorWithColumnHeaders());
			
			return sList;
		}
		return null;
	}
	 
	private static class ExtractorWithColumnHeaders implements ResultSetExtractor<List<List<String>>> {
		  @Override
		  public List<List<String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
			  
			  List<List<String>> all = new ArrayList();
			  ResultSetMetaData rsmd = rs.getMetaData();
			  int colCount = rsmd.getColumnCount();
			  List retList = new ArrayList(colCount);

			  
			  for(int x = 1; x<=colCount; x++) {
				  String s = rsmd.getColumnName(x);
				  retList.add(s);
			  }
			  all.add(retList);//add column headers
			  
			  while(rs.next()) {
				  retList = new ArrayList(colCount);
				  for(int x = 1; x<=colCount; x++) {
					  String s = rs.getString(x);
					  retList.add(s);
				  }
				  all.add(retList);//add each row of data
			  }
				 
			  return all;
		  }
	}
	 
	///end misc
	private static class StringRowMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String tn = rs.getString(1);
			return tn;
		}
	}

	//x columns of data
	private static class StringXRowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			List retList = new ArrayList(colCount);
			
			for(int x = 1; x<=colCount; x++) {
				String s = rs.getString(x);
				retList.add(s);
			}
			 
			return retList;
		}
	}

	//1 columns of data
	private static class StringX1RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			List retList = new ArrayList(1);
			retList.add(s1);
			return retList;
		}
	}
	
	//2 columns of data
	private static class StringX2RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			List retList = new ArrayList(2);
			retList.add(s1);
			retList.add(s2);
			return retList;
		}
	}
	

	//3 columns of data
	private static class StringX3RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			List retList = new ArrayList(3);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			return retList;
		}
	}
	

	//4 columns of data
	private static class StringX4RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			List retList = new ArrayList(4);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			return retList;
		}
	}

	//5 columns of data
	private static class StringX5RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			List retList = new ArrayList(5);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			return retList;
		}
	}

	//6 columns of data
	private static class StringX6RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			List retList = new ArrayList(6);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			return retList;
		}
	}

	//7 columns of data
	private static class StringX7RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			List retList = new ArrayList(7);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			return retList;
		}
	}

	//8 columns of data
	private static class StringX8RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			 
			List retList = new ArrayList(8);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
 
			return retList;
		}
	}
	

	//9 columns of data
	private static class StringX9RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			 
			List retList = new ArrayList(9);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
		 
			return retList;
		}
	}

	//10 columns of data
	private static class StringX10RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
						 
			List retList = new ArrayList(10);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
		 
			return retList;
		}
	}



	//11 columns of data
	private static class StringX11RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			 
			List retList = new ArrayList(11);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
		 
			return retList;
		}
	}


	//12 columns of data
	private static class StringX12RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			String s12 = rs.getString(12);
			 
			List retList = new ArrayList(12);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
			retList.add(s12);
		 
			return retList;
		}
	}


	//13 columns of data
	private static class StringX13RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			String s12 = rs.getString(12);
			String s13 = rs.getString(13);
			 
			List retList = new ArrayList(13);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
			retList.add(s12);
			retList.add(s13);
		 
			return retList;
		}
	}



	//14 columns of data
	private static class StringX14RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			String s12 = rs.getString(12);
			String s13 = rs.getString(13);
			String s14 = rs.getString(14);
			 
			List retList = new ArrayList(13);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
			retList.add(s12);
			retList.add(s13);
			retList.add(s14);
		 
			return retList;
		}
	}

	
	//16 columns of data
	private static class StringX16RowMapper implements RowMapper<List> {
		@Override
		public List mapRow(ResultSet rs, int rowNum) throws SQLException {
			String s1 = rs.getString(1);
			String s2 = rs.getString(2);
			String s3 = rs.getString(3);
			String s4 = rs.getString(4);
			String s5 = rs.getString(5);
			String s6 = rs.getString(6);
			String s7 = rs.getString(7);
			String s8 = rs.getString(8);
			String s9 = rs.getString(9);
			String s10 = rs.getString(10);
			String s11 = rs.getString(11);
			String s12 = rs.getString(12);
			String s13 = rs.getString(13);
			String s14 = rs.getString(14);
			String s15 = rs.getString(15);
			String s16 = rs.getString(16);
			List retList = new ArrayList(16);
			retList.add(s1);
			retList.add(s2);
			retList.add(s3);
			retList.add(s4);
			retList.add(s5);
			retList.add(s6);
			retList.add(s7);
			retList.add(s8);
			retList.add(s9);
			retList.add(s10);
			retList.add(s11);
			retList.add(s12);
			retList.add(s13);
			retList.add(s14);
			retList.add(s15);
			retList.add(s16);
			return retList;
		}
	}
	 
		//18 columns of data
		private static class StringX18RowMapper implements RowMapper<List> {
			@Override
			public List mapRow(ResultSet rs, int rowNum) throws SQLException {
				String s1 = rs.getString(1);
				String s2 = rs.getString(2);
				String s3 = rs.getString(3);
				String s4 = rs.getString(4);
				String s5 = rs.getString(5);
				String s6 = rs.getString(6);
				String s7 = rs.getString(7);
				String s8 = rs.getString(8);
				String s9 = rs.getString(9);
				String s10 = rs.getString(10);
				String s11 = rs.getString(11);
				String s12 = rs.getString(12);
				String s13 = rs.getString(13);
				String s14 = rs.getString(14);
				String s15 = rs.getString(15);
				String s16 = rs.getString(16);
				String s17 = rs.getString(17);
				String s18 = rs.getString(18);
				List retList = new ArrayList(18);
				retList.add(s1);
				retList.add(s2);
				retList.add(s3);
				retList.add(s4);
				retList.add(s5);
				retList.add(s6);
				retList.add(s7);
				retList.add(s8);
				retList.add(s9);
				retList.add(s10);
				retList.add(s11);
				retList.add(s12);
				retList.add(s13);
				retList.add(s14);
				retList.add(s15);
				retList.add(s16);
				retList.add(s17);
				retList.add(s18);
				return retList;
			}
		}
	 
		 
		//18 columns of data
		private static class StringX19RowMapper implements RowMapper<List> {
			@Override
			public List mapRow(ResultSet rs, int rowNum) throws SQLException {
				String s1 = rs.getString(1);
				String s2 = rs.getString(2);
				String s3 = rs.getString(3);
				String s4 = rs.getString(4);
				String s5 = rs.getString(5);
				String s6 = rs.getString(6);
				String s7 = rs.getString(7);
				String s8 = rs.getString(8);
				String s9 = rs.getString(9);
				String s10 = rs.getString(10);
				String s11 = rs.getString(11);
				String s12 = rs.getString(12);
				String s13 = rs.getString(13);
				String s14 = rs.getString(14);
				String s15 = rs.getString(15);
				String s16 = rs.getString(16);
				String s17 = rs.getString(17);
				String s18 = rs.getString(18);
				String s19 = rs.getString(19);
				List retList = new ArrayList(19);
				retList.add(s1);
				retList.add(s2);
				retList.add(s3);
				retList.add(s4);
				retList.add(s5);
				retList.add(s6);
				retList.add(s7);
				retList.add(s8);
				retList.add(s9);
				retList.add(s10);
				retList.add(s11);
				retList.add(s12);
				retList.add(s13);
				retList.add(s14);
				retList.add(s15);
				retList.add(s16);
				retList.add(s17);
				retList.add(s18);
				retList.add(s19);
				return retList;
			}
		}
	 
	 
}