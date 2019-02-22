package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation = new Reservation();
		reservation.setReservationId(results.getInt("reservation_id"));;
		reservation.setSiteId(results.getInt("name"));
		reservation.setFromDate(results.getDate("from_date").toLocalDate());
		reservation.setToDate(results.getDate("to_date").toLocalDate());
		
		return reservation;
	}

	@Override
	public List<Reservation> getAllAvailableReservations(String startDate, String endDate) {
		List<Reservation> reservations = new ArrayList<>();
		
		String sqlAllReservations = "SELECT r.reservation_id, r.name, r.from_date, r.to_date FROM reservation r" +
									"JOIN site s ON s.site_id = r.site_id";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllReservations);

		while (results.next()) {
			Reservation r = mapRowToReservation(results);
			reservations.add(r);
		}
		return reservations;
	}

	@Override
	public int bookReservation(String reservationUnderName, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return 0;
	}

}