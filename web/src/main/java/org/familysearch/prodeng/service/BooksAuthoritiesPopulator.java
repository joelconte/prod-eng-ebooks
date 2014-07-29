package org.familysearch.prodeng.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.lds.stack.ldsaccount.LdsAccountDetails;
import org.lds.stack.ldsaccount.spring.populators.LdsAccountAuthoritiesPopulator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class BooksAuthoritiesPopulator extends NamedParameterJdbcDaoSupport
		implements LdsAccountAuthoritiesPopulator {

	private SimpleJdbcInsert bookModelJdbcInsert;
	public BooksAuthoritiesPopulator( ) {
		 
	}
	@Inject
	public BooksAuthoritiesPopulator(DataSource dataSource) {
		setDataSource(dataSource);// required to get jdbctemplate from
									// jdbcDaoSupport..
	}
 
	//similar to getGrantedAuthorities in spring mvc ldap AutoritiesPopulator
	@Override
	public Set<String> populateRoles(LdsAccountDetails user) {
		Set<String> roleSet = new HashSet<String>();
		roleSet.addAll(getJdbcTemplate().query("select role from user_authorities where lower(id) = '" + user.getUsername().toLowerCase() + "'", new GrantedAuthRowMapper()));
		return roleSet;
	}
	 
	// 1 columns of data
	private static class GrantedAuthRowMapper implements RowMapper  {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			 return rs.getString(1);
		}
	}
}