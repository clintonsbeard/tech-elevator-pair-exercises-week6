package com.techelevator.view;

import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.SiteDAO;

public class ReservationAdmin {

	private CampgroundDAO campgroundDAO;
	private ParkDAO parkDAO;
	private ReservationDAO reservationDAO;
	private SiteDAO siteDAO;

	public CampgroundDAO getCampgroundDAO() {
		return campgroundDAO;
	}

	public ParkDAO getParkDAO() {
		return parkDAO;
	}

	public ReservationDAO getReservationDAO() {
		return reservationDAO;
	}

	public SiteDAO getSiteDAO() {
		return siteDAO;
	}
	
}