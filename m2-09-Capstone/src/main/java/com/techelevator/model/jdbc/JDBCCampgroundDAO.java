package com.techelevator.model.jdbc;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {

	@Override
	public Campground mapRowToCampground(SqlRowSet results) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Campground> getAllAvailableCampgrounds() {
		// TODO Auto-generated method stub
		return null;
	}

}