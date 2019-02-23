package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.sql.DataSource;

import com.techelevator.CampgroundCLI;
import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
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
	
	private PrintWriter out;
	private Scanner in;
	private int parkChoice;
	private Park chosenPark;
	private int campgroundChoice;
	private String arrivalDate;
	private String departureDate;
	private DataSource dataSource;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private FormatData format;
	private int daysBetween;
	
	public Menu(InputStream input, OutputStream output, DataSource dataSource) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
		this.dataSource = dataSource;
		
		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
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
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}
	
	private void displayMenuOptions(Object[] options) {
		out.println();
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i]);
		}
		out.flush();
	}
	
	public void displayMenu() {
		while (true) {
			displayAllParks();
			while (true) {
				displayParkInformation(parkChoice);
				String choice = (String)getChoiceFromOptions(PARK_OPTIONS);
				if(choice.equals(PARK_OPTION_VIEW_CAMPGROUNDS)) {
					System.out.println(chosenPark.getName() + " National Park Campgrounds\n");
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
					searchForSites();
				} else if(choice.equals(PARK_OPTION_RETURN_TO_PREVIOUS_SCREEN)) {
					break;
				}
			}
		}
	}
	
	public void displayAllParks() {
		System.out.println("View Parks Interface");
		System.out.println("Select a Park for Further Details");
		
		List<Park> allParks = parkDAO.getAllAvailableParks();
		listAllParks(allParks);
		
		getParkChoiceFromUser();
	}
	
	public int getParkChoiceFromUser() {
		System.out.print("Please select an option and press enter: ");
		parkChoice = in.nextInt();
		in.nextLine();
		return parkChoice;
	}
	
	private void listAllParks(List<Park> parks) {
		if (parks.size() > 0) {
			for (int i = 0; i < parks.size(); i++) {
				int optionNum = i + 1;
				System.out.println(optionNum + ") " + parks.get(i).getName());
			}
			System.out.println("Q) " + "quit");
		}
		System.out.flush();
	}
	
	private void displayParkInformation(int parkChoice) {
		chosenPark = parkDAO.getParkDescription(parkChoice);
		
		System.out.println();
		System.out.println(chosenPark.getName() + " National Park");
		System.out.printf("%-17s%-17s\n", "Location: ", chosenPark.getLocation());
		System.out.printf("%-17s%-17s\n", "Established: ", format.formatOutputDate(chosenPark.getEstDate()));
		System.out.printf("%-17s%-17s\n", "Area: ", format.formatArea(chosenPark.getArea()));
		System.out.printf("%-17s%-17s\n", "Annual Visitors: ", format.formatAnnualVisitors(chosenPark.getAnnualVisitors()));
		System.out.println();
		formatParkDesc(chosenPark.getParkDesc());
		
		parkCommands();
	}
	
	public void parkCommands() {
		System.out.println("\nSelect a Command");
	}
	
	public void displayAllCampgroundsByPark(int userChoice) {
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgroundsByPark(parkChoice);
		listAllCampgrounds(allCampgrounds);
	}
	
	public void listAllCampgrounds(List<Campground> campgrounds) {
		if (campgrounds.size() > 0) {
			System.out.printf("%-5s%-35s%-12s%-12s%-3s\n", "", "Name", "Open", "Close", "Daily Fee");
			for (int i = 0; i < campgrounds.size(); i++) {
				int optionNum = i + 1;
				String optionNumString = "#" + optionNum;
				System.out.printf("%-5s%-35s%-12s%-12s$%-3.2f\n", optionNumString, campgrounds.get(i).getName(),
								  format.convertMonthToString(campgrounds.get(i).getOpenMonth()),
								  format.convertMonthToString(campgrounds.get(i).getCloseMonth()),
								  campgrounds.get(i).getDailyFee());
			}
		}
		System.out.flush();
	}
	
	public void searchForSites() {
		System.out.print("\nWhich campground (enter 0 to cancel)? ");
		campgroundChoice = in.nextInt();
		in.nextLine();
		System.out.print("What is the arrival date? ");
		arrivalDate = in.nextLine();
		String arrivalDateFormat = format.formatInputDate(arrivalDate);
		System.out.print("What is the departure date? ");
		departureDate = in.nextLine();
		String departureDateFormat = format.formatInputDate(arrivalDate);
		
		LocalDate arrivalLocalDate = LocalDate.parse(format.formatInputDate(arrivalDate));
		LocalDate departureLocalDate = LocalDate.parse(format.formatInputDate(departureDate));
		Period periodBetween = Period.between(arrivalLocalDate, departureLocalDate);
		daysBetween = periodBetween.getDays();
		
		List<Site> allSites = siteDAO.getAllAvailableSites(campgroundChoice, arrivalDateFormat, departureDateFormat);
		listAllAvailableSites(allSites);
	}
	
	public void listAllAvailableSites(List<Site> sites) {
		if (sites.size() > 0) {
			System.out.printf("\n%-12s%-12s%-18s%-18s%-14s%-10s\n", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utilities", "Cost");
			for (int i = 0; i < sites.size(); i++) {
				System.out.printf("%-12s%-12s%-18s%-18s%-14s$%-10s\n", sites.get(i).getSiteNumber(), sites.get(i).getMaxOccupancy(),
																	   format.capitalizeFirstLetter(sites.get(i).getAccessible()),
																	   sites.get(i).getMaxRvLength(),
																	   format.capitalizeFirstLetter(sites.get(i).getUtilities()), 
																	   (daysBetween * sites.get(i).getCost()));
			}
		}
		System.out.flush();
	}
	
	public void formatParkDesc(String parkDesc) {
		StringBuilder sb = new StringBuilder(parkDesc);
	    int i = 0;
	    while ((i = sb.indexOf(" ", i + 80)) != -1) {
	        sb.replace(i, i + 1, "\n");
	    }
	    System.out.println(sb.toString());
	}
	
}