package com.techelevator.model;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface ParkDAO {

	public Park mapRowToPark(SqlRowSet results);
	
	public Park getParkDescription(int parkChoice);
	
	public List<Park> getAllAvailableParks();
	
}