package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Campground mapRowToCampground(SqlRowSet results) {
		Campground campground = new Campground();
		campground.setCampgroundId(results.getInt("campground_id"));;
		campground.setName(results.getString("name"));
		campground.setOpenMonth(results.getInt("open_from_mm"));
		campground.setCloseMonth(results.getInt("close_to_mm"));
		campground.setDailyFee(results.getInt("daily_fee"));
		
		return campground;
	}

	@Override
	public List<Campground> getAllAvailableCampgrounds() {
		List<Campground> campgrounds = new ArrayList<>();
		
		String sqlAllCampgrounds = "SELECT campground_id, name, open_from_mm, close_to_mm, daily_fee FROM campground";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllCampgrounds);

		while (results.next()) {
			Campground c = mapRowToCampground(results);
			campgrounds.add(c);
		}
		return campgrounds;
	}

}