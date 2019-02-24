package com.techelevator.model.jdbc;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void bookReservation(Reservation reservation, int siteChoice, String reservationUnderName, String startDate, String endDate) {
		String sqlAllSites = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) " + 
							 "VALUES (DEFAULT, ?, ?, CAST(? AS DATE), CAST(? AS DATE), CURRENT_DATE) RETURNING reservation_id";
	
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllSites, siteChoice, reservationUnderName, "'" + startDate + "'", "'" + endDate + "'");
		
		results.next();
		reservation.setReservationId(results.getInt("reservation_id"));
	}

}