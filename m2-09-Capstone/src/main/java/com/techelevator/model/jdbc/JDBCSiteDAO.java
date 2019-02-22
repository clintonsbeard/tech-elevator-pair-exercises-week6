package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Site mapRowToSite(SqlRowSet results) {
		Site site = new Site();
		site.setSiteId(results.getInt("site_id"));;
		site.setSiteNumber(results.getInt("site_number"));
		
		return site;
	}

	@Override
	public List<Site> getAllAvailableSites() {
		List<Site> sites = new ArrayList<>();
		
		String sqlAllSites = "SELECT site_id, site_number FROM site";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllSites);

		while (results.next()) {
			Site s = mapRowToSite(results);
			sites.add(s);
		}
		return sites;
	}

}