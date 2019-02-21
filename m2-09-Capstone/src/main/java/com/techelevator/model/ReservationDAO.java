package com.techelevator.model;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface ReservationDAO {

	public Reservation mapRowToReservation(SqlRowSet results);
	
	public List<Reservation> getAllAvailableReservations(String startDate, String endDate);
	
	public int bookReservation(String reservationUnderName, String startDate, String endDate);
	
}