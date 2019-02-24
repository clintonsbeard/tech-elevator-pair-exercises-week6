package com.techelevator.model;

public interface ReservationDAO {
	
	public void bookReservation(Reservation reservation, int siteChoice, String reservationUnderName, String startDate, String endDate);
	
}