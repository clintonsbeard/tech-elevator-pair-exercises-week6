package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.sql.DataSource;

import com.techelevator.CampgroundCLI;
import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;

public class Menu {

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
	
	private Scanner in;
	private PrintWriter out;
	private DataSource dataSource;
	private FormatData format;
	
	private int parkChoice;
	private int campgroundChoice;
	private Park chosenPark;
	private Reservation reservation;
	
	private String arrivalDate;
	private String departureDate;
	private int daysBetween;
	
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;
	
	private List<Site> sites;
	
	SimpleDateFormat dateFormatInput = new SimpleDateFormat("MM/dd/yyyy");
	SimpleDateFormat dateFormatOutput = new SimpleDateFormat("yyyy-MM-dd");
	
	public Menu(InputStream input, OutputStream output, DataSource dataSource) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
		this.dataSource = dataSource;
		
		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
		format = new FormatData();
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("Error: Invalid choice.  Please choose and insert a number from the list.");
		}
		return choice;
	}
	
	private void displayMenuOptions(Object[] options) {
		out.println();
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i]);
		}
		out.print("\nPlease select an option and press enter: ");
		out.flush();
	}
	
	public void displayMenu() {
		while (true) {
			System.out.println();
			displayAllParks();
			while (true) {
				displayParkInformation(parkChoice);
				String choice = (String)getChoiceFromOptions(PARK_OPTIONS);
				if(choice.equals(PARK_OPTION_VIEW_CAMPGROUNDS)) {
					System.out.println("\n" + chosenPark.getName() + " National Park Campgrounds\n");
					displayAllCampgroundsByPark(parkChoice);
					while (true) {
						String choice2 = (String)getChoiceFromOptions(CAMPGROUND_OPTIONS);
						if(choice2.equals(CAMPGROUND_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
							displayAllCampgroundsByPark(parkChoice);
							searchForSites();
						} else if(choice2.equals(CAMPGROUND_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
							break;
						}
					}
				} else if(choice.equals(PARK_OPTION_SEARCH_FOR_RESERVATION)) {
					displayAllCampgroundsByPark(parkChoice);
				} else if(choice.equals(PARK_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
					break;
				}
			}
		}
	}
	
	public void displayAllParks() {
		System.out.println("Select a Park for Further Details");
		
		List<Park> allParks = parkDAO.getAllAvailableParks();
		listAllParks(allParks);
		
		getParkChoiceFromUser();
	}
	
	public void getParkChoiceFromUser() {
		while (true) {
			boolean validCheck = true;
			System.out.print("Please select an option and press enter: ");
			String parkChoiceString = in.nextLine();
			if (parkChoiceString.equalsIgnoreCase("Q")) {
				System.exit(0);
			}
			try {
				parkChoice = Integer.parseInt(parkChoiceString);
			}
			catch (NumberFormatException e) {
				System.out.println("Error: Invalid choice.  Please choose and insert a number from the list.\n");
				validCheck = false;
			}
			if (validCheck) {
				break;
			}
		}
	}
	
	private void listAllParks(List<Park> parks) {
		if (parks.size() > 0) {
			for (int i = 0; i < parks.size(); i++) {
				int parkNumber = parks.get(i).getParkId();
				System.out.println(parkNumber + ") " + parks.get(i).getName());
			}
			System.out.println("Q) " + "quit");
		}
		System.out.flush();
	}
	
	public void displayParkInformation(int parkChoice) {
		chosenPark = parkDAO.getParkDescription(parkChoice);
		
		System.out.println();
		System.out.println(chosenPark.getName() + " National Park");
		System.out.printf("%-17s%-17s\n", "Location: ", chosenPark.getLocation());
		System.out.printf("%-17s%-17s\n", "Established: ", format.formatOutputDate(chosenPark.getEstDate()));
		System.out.printf("%-17s%-17s\n", "Area: ", format.formatArea(chosenPark.getArea()));
		System.out.printf("%-17s%-17s\n", "Annual Visitors: ", format.formatAnnualVisitors(chosenPark.getAnnualVisitors()));
		System.out.println();
		formatParkDesc(chosenPark.getParkDesc());
		System.out.print("\nSelect a Command");
	}
	
	public void displayAllCampgroundsByPark(int userChoice) {
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgroundsByPark(parkChoice);
		listAllCampgrounds(allCampgrounds);
	}
	
	public void listAllCampgrounds(List<Campground> campgrounds) {
		while (true) {
			if (campgrounds.size() > 0) {
				System.out.printf("%-5s%-35s%-12s%-12s%-3s\n", "", "Name", "Open", "Close", "Daily Fee");
				for (int i = 0; i < campgrounds.size(); i++) {
					int campgroundNumber = campgrounds.get(i).getCampgroundId();
					System.out.printf("#%-4s%-35s%-12s%-12s$%-3.2f\n", campgroundNumber, campgrounds.get(i).getName(),
									  format.convertMonthToString(campgrounds.get(i).getOpenMonth()),
									  format.convertMonthToString(campgrounds.get(i).getCloseMonth()),
									  campgrounds.get(i).getDailyFee());
				}
			}
			else {
				System.out.println("Error: No campgrounds found in system.");
			}
			boolean numberCheck = false;
			while (true) {
				numberCheck = false;
				System.out.print("\nWhich campground (enter 0 to cancel)? ");
				try {
					campgroundChoice = in.nextInt();
					in.nextLine();
					numberCheck = true;
					if (campgroundChoice == 0) {
						break;
					}
				}
				catch (InputMismatchException e) {
					System.out.print("Error: Invalid choice.  Please choose from the options listed above.\n");
					campgroundChoice = 0;
					numberCheck = false;
					break;
				}
				break;
			}
			if (campgroundChoice == 0) {
				break;
			}
			if (numberCheck == true && campgroundChoice > campgrounds.size()) {
				System.out.println("Error: Invalid choice.  Please choose from the options listed above.\n");
			}
			if (numberCheck == true) {
				searchForSites();
			}
		}
	}
	
	public void searchForSites() {
		while (true) {
			Date arrivalDateParsed = null;
			Date departureDateParsed = null;
			LocalDate arrivalLocalDate = null;
			LocalDate departureLocalDate = null;
			try {
				while (true) {
					boolean arrivalCheck = true;
					System.out.print("What is the arrival date? ");
					arrivalDate = in.nextLine();
					arrivalDateParsed = dateFormatInput.parse(arrivalDate);
					try {
						arrivalLocalDate = LocalDate.parse(format.formatInputDate(arrivalDate));
					}
					catch (DateTimeParseException e) {
						System.out.println("Please enter the date in the correct format (MM/dd/yyyy).\n");
						arrivalCheck = false;
					}
					if (arrivalCheck) {
						break;
					}
				}
				while (true) {
					boolean departureCheck = true;
					System.out.print("What is the departure date? ");
					departureDate = in.nextLine();
					departureDateParsed = dateFormatInput.parse(departureDate);	
					try {
						departureLocalDate = LocalDate.parse(format.formatInputDate(departureDate));
					}
					catch (DateTimeParseException e) {
						System.out.println("Please enter the date in the correct format (MM/dd/yyyy).\n");
						departureCheck = false;
					}
					if (departureCheck) {
						break;
					}
				}
			} catch (ParseException e) {
				System.out.println("Please enter the date in the correct format (MM/dd/yyyy).\n");
				break;
			}
			String formattedArrivalDate = dateFormatOutput.format(arrivalDateParsed);
			String formattedDepartureDate = dateFormatOutput.format(departureDateParsed);
			
			Period periodBetween = Period.between(arrivalLocalDate, departureLocalDate);
			daysBetween = periodBetween.getDays();
				
			sites = siteDAO.getAllAvailableSites(campgroundChoice, formattedArrivalDate, formattedDepartureDate);
			listAllAvailableSites(sites);
		}
	}
	
	public void listAllAvailableSites(List<Site> sites) {
		if (sites.size() > 0) {
			System.out.println("\nResults Matching Your Search Criteria");
			System.out.printf("%-12s%-12s%-18s%-18s%-14s%-10s\n", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utilities", "Cost");
			for (int i = 0; i < sites.size(); i++) {
				System.out.printf("%-12s%-12s%-18s%-18s%-14s$%-10s\n", sites.get(i).getSiteNumber(), sites.get(i).getMaxOccupancy(),
																	   format.capitalizeFirstLetter(sites.get(i).getAccessible()),
																	   sites.get(i).getMaxRvLength(),
																	   format.capitalizeFirstLetter(sites.get(i).getUtilities()), 
																	   (daysBetween * sites.get(i).getCost()));
			}
			System.out.flush();
			makeReservation();
		}
		if (sites.size() <= 0) {
			while (true) {
				System.out.print("\nSorry, but no campsites are available during those dates.  Would you like to enter another date range? (Y/N) ");
				String tryAgainPrompt = in.nextLine();
				System.out.println();
				if (tryAgainPrompt.equalsIgnoreCase("Y")) {
					break;
				}
				if (tryAgainPrompt.equalsIgnoreCase("N")) {
					System.exit(0);
				}
				else {
					System.out.println("Error: Invalid choice.  Please choose from the options listed above.\n");
				}
			}
		}
	}
	
	public void formatParkDesc(String parkDesc) {
		StringBuilder sb = new StringBuilder(parkDesc);
	    int i = 0;
	    while ((i = sb.indexOf(" ", i + 80)) != -1) {
	        sb.replace(i, i + 1, "\n");
	    }
	    System.out.println(sb.toString());
	}
	
	public void makeReservation() {
		while (true) {
			System.out.print("\nWhich site should be reserved (enter 0 to cancel)? ");
			int siteChoice = in.nextInt();
			in.nextLine();
			if (siteChoice == 0) {
				break;
			}
			if (siteChoice > sites.size()) {
				System.out.println("Error: Invalid choice.  Please choose from the options listed above.\n");
			}
			System.out.print("\nWhat name should the reservation be made under? ");
			String reservationUnderName = in.nextLine();
			
			reservation = new Reservation();
			reservationDAO.bookReservation(reservation, siteChoice, reservationUnderName, arrivalDate, departureDate);
			
			System.out.println("The reservation has been made and the confirmation id is " + reservation.getReservationId());
			break;
		}
	}

}