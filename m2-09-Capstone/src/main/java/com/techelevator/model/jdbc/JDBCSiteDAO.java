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
		site.setSiteNumber(results.getInt("site_number"));
		site.setMaxOccupancy(results.getInt("max_occupancy"));
		site.setAccessible(results.getString("accessible"));
		site.setMaxRvLength(results.getString("max_rv_length"));
		site.setUtilities(results.getString("utilities"));
		site.setCost(results.getString("daily_fee"));
		
		return site;
	}

	@Override
	public List<Site> getAllAvailableSites(int campgroundChoice, String arrivalDate, String departureDate) {
		List<Site> sites = new ArrayList<>();
		
		String sqlAllSites = "SELECT DISTINCT s.site_number, s.max_occupancy, s.accessible, s.max_rv_length, s.utilities, c.daily_fee FROM site s " +
							 "JOIN campground c ON c.campground_id = s.campground_id " +
							 "WHERE s.campground_id = ? " +
							 "AND s.site_id NOT IN " +
							 "(SELECT s.site_id FROM site " +
							 "JOIN reservation r ON r.site_id = s.site_id " +
							 "WHERE (CAST(? AS DATE) > r.from_date AND CAST(? AS DATE) < r.to_date) " +
							 "OR (CAST(? AS DATE) > r.from_date AND CAST(? AS DATE) < r.to_date)) " +
							 "ORDER BY daily_fee " +
							 "LIMIT 5";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllSites, campgroundChoice, "'" + arrivalDate + "'", "'" + departureDate + "'", "'" + departureDate + "'", "'" + arrivalDate + "'");

		while (results.next()) {
			Site s = mapRowToSite(results);
			sites.add(s);
		}
		return sites;
	}

}