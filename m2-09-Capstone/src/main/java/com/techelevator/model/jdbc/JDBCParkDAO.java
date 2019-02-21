package com.techelevator.model.jdbc;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	@Override
	public Park mapRowToPark(SqlRowSet results) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Park> getAllAvailableParks() {
		// TODO Auto-generated method stub
		return null;
	}
	
}