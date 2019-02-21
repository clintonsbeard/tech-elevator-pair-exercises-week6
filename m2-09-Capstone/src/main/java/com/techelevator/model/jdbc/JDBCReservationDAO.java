package com.techelevator.model.jdbc;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	@Override
	public Reservation mapRowToReservation(SqlRowSet results) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> getAllAvailableReservations(String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int bookReservation(String reservationUnderName, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return 0;
	}

}