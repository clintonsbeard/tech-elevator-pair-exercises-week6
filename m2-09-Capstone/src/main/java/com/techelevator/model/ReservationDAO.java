package com.techelevator.model;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface ReservationDAO {

	public Reservation mapRowToReservation(SqlRowSet results);
	
	public int bookReservation(String reservationUnderName, String startDate, String endDate);
	
}