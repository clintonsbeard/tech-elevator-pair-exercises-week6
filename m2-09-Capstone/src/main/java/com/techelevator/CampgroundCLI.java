package com.techelevator;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.view.Menu;

public class CampgroundCLI {

	private static final String PARK_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String PARK_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String PARK_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	private static final String[] PARK_OPTIONS = new String[] { PARK_OPTION_VIEW_CAMPGROUNDS,
																PARK_OPTION_SEARCH_FOR_RESERVATION,
																PARK_OPTION_RETURN_TO_PREVIOUS_SCREEN };
	
	private static final String CAMPGROUND_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION = "Search For Available Reservations";
	private static final String CAMPGROUND_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	private static final String[] CAMPGROUND_OPTIONS = new String[] { CAMPGROUND_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION,
																	  CAMPGROUND_OPTION_RETURN_TO_PREVIOUS_SCREEN };
	
	private Menu menu;
	
	private String parkChoiceString;
	private int parkChoice;
	private boolean validCheck;
	private boolean reservationCheck;
	
	public static void main(String[] args) {
		CampgroundCLI application = new CampgroundCLI();
		application.run();
	}

	public CampgroundCLI() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		this.menu = new Menu(System.in, System.out, dataSource);
	}
	
	public void run() {
		program:
		while (true) {
			parks:
			while (true) {
				validCheck = true;
				int parksSize = menu.displayAllParks();
				parkChoiceString = menu.getParkChoiceFromUser();
				if (parkChoiceString.equalsIgnoreCase("Q")) {
					break program;
				}
				try {
					parkChoice = Integer.parseInt(parkChoiceString);
					if (parkChoice > parksSize || parkChoice <= 0) {
						menu.displayInvalidChoiceError();
						validCheck = false;
					}
				}
				catch (NumberFormatException e) {
					menu.displayInvalidChoiceError();
					validCheck = false;
				}
				if (validCheck == true) {
					break parks;
				}
			}
			menu:
			while (true) {
	 			menu.displayParkInformation(parkChoice);
	 			submenu:
	 			while (true) {
					String parkOptionsChoice = (String)menu.getChoiceFromOptions(PARK_OPTIONS);
					if(parkOptionsChoice.equals(PARK_OPTION_VIEW_CAMPGROUNDS)) {
						menu.displayAllCampgroundsByPark(parkChoice);
						String campgroundOptionsChoice = (String)menu.getChoiceFromOptions(CAMPGROUND_OPTIONS);
						if(campgroundOptionsChoice.equals(CAMPGROUND_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
							boolean cancelCheck = menu.getCampgroundChoice(parkChoice);
							if (cancelCheck) {
								break submenu;
							}
							else {
								menu.searchForSites();
								reservationCheck = menu.makeReservation();
								if (reservationCheck) {
									break menu;
								}
							}
						}
						else if(campgroundOptionsChoice.equals(CAMPGROUND_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
							break submenu;
						}
					}
					else if(parkOptionsChoice.equals(PARK_OPTION_SEARCH_FOR_RESERVATION)) {
						while (true) {
							boolean cancelCheck = menu.getCampgroundChoice(parkChoice);
							if (cancelCheck) {
								break submenu;
							}
							else {
								menu.searchForSites();
								reservationCheck = menu.makeReservation();
								if (reservationCheck) {
									break submenu;
								}
								break menu;
							}
						}
					}
					else if(parkOptionsChoice.equals(PARK_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
						break menu;
					}
	 			}
			}
		}
	}
}