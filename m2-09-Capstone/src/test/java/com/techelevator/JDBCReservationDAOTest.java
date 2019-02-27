package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;




public class JDBCReservationDAOTest {
	private static SingleConnectionDataSource dataSource;
	private ReservationDAO dao; 
	private JdbcTemplate jdbcTemplate;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setup() {
		dao = new JDBCReservationDAO(dataSource);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void reservation_can_be_found_by_id_after_being_created() {
		Reservation reservation = new Reservation();
		int siteChoice = 3;
		String reservationUnderName = "Test Name";
		String startDate = "2019-03-03";
		String endDate = "2019-03-07";
		
		int before = reservation.getReservationId();
		dao.bookReservation(reservation, siteChoice, reservationUnderName, startDate, endDate);
	    int actual = reservation.getReservationId();
		
		assertNotNull(reservation.getReservationId());
		assertEquals(actual, reservation.getReservationId());
	}
}
