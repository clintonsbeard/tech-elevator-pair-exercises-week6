package com.techelevator.model.jdbc;

import java.util.ArrayList;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Park mapRowToPark(SqlRowSet results) {
		Park park = new Park();
		park.setParkId(results.getInt("park_id"));;
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstDate(results.getString("establish_date"));
		park.setArea(results.getInt("area"));
		park.setAnnualVisitors(results.getInt("visitors"));
		park.setParkDesc(results.getString("description"));
		
		return park;
	}

	@Override
	public List<Park> getAllAvailableParks() {
		List<Park> parks = new ArrayList<>();
		String sqlAllParks = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllParks);
		while (results.next()) {
			Park p = mapRowToPark(results);
			parks.add(p);
		}
		return parks;
	}

	@Override
	public Park getParkDescription(int parkChoice) {
		String sqlAllParks = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park " +
							 "WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllParks, parkChoice);
		Park park = null;
		if (results.next()) {
			park = mapRowToPark(results);
		}
		return park;
	}
	
}