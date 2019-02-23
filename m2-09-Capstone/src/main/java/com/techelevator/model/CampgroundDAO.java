package com.techelevator.model;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface CampgroundDAO {

	public Campground mapRowToCampground(SqlRowSet results);
	
	public List<Campground> getAllCampgroundsByPark(int userChoice);
	
}