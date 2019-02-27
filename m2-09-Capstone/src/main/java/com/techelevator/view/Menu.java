package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

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
	private int[] siteNumbers;
	
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
	
	public int displayAllParks() {
		System.out.println("\nSelect a Park for Further Details");
		List<Park> allParks = parkDAO.getAllAvailableParks();
		if (allParks.size() > 0) {
			for (int i = 0; i < allParks.size(); i++) {
				int parkNumber = allParks.get(i).getParkId();
				System.out.println(parkNumber + ") " + allParks.get(i).getName());
			}
			System.out.println("Q) " + "quit");
		}
		return allParks.size();
	}
	
	public String getParkChoiceFromUser() {
		System.out.print("Please select an option and press enter: ");
		String parkChoice = in.nextLine();
		return parkChoice;
	}
	
	public void displayInvalidChoiceError() {
		System.out.println("Error: Invalid choice.  Please select an option from the list.");
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
		System.out.println("\n" + chosenPark.getName() + " National Park Campgrounds\n");
		List<Campground> allCampgrounds = campgroundDAO.getAllCampgroundsByPark(userChoice);
		if (allCampgrounds.size() > 0) {
			System.out.printf("%-5s%-35s%-12s%-12s%-3s\n", "", "Name", "Open", "Close", "Daily Fee");
			for (int i = 0; i < allCampgrounds.size(); i++) {
				int campgroundNumber = allCampgrounds.get(i).getCampgroundId();
				System.out.printf("#%-4s%-35s%-12s%-12s$%-3.2f\n", campgroundNumber, allCampgrounds.get(i).getName(),
								  format.convertMonthToString(allCampgrounds.get(i).getOpenMonth()),
								  format.convertMonthToString(allCampgrounds.get(i).getCloseMonth()),
								  allCampgrounds.get(i).getDailyFee());
			}
			System.out.print("\nSelect a Command");
		}
		else {
			System.out.println("Error: No campgrounds found in system.");
		}
		
	}
	
	public boolean getCampgroundChoice(int userChoice) {
		boolean cancelCheck = false;
		while (true) {
			System.out.println("\n" + chosenPark.getName() + " National Park Campgrounds\n");
			List<Campground> allCampgrounds = campgroundDAO.getAllCampgroundsByPark(userChoice);
			int[] campgroundNumbers = new int[allCampgrounds.size()];
			if (allCampgrounds.size() > 0) {
				System.out.printf("%-5s%-35s%-12s%-12s%-3s\n", "", "Name", "Open", "Close", "Daily Fee");
				for (int i = 0; i < allCampgrounds.size(); i++) {
					int campgroundNumber = allCampgrounds.get(i).getCampgroundId();
					System.out.printf("#%-4s%-35s%-12s%-12s$%-3.2f\n", campgroundNumber, allCampgrounds.get(i).getName(),
									  format.convertMonthToString(allCampgrounds.get(i).getOpenMonth()),
									  format.convertMonthToString(allCampgrounds.get(i).getCloseMonth()),
									  allCampgrounds.get(i).getDailyFee());
					campgroundNumbers[i] = allCampgrounds.get(i).getCampgroundId();
				}
			}
			else {
				System.out.println("Error: No campgrounds found in system.");
			}
			boolean validCheck = true;
			System.out.print("\nWhich campground (enter 0 to cancel)? ");
			boolean inputMismatch = false;
			try {
				campgroundChoice = in.nextInt();
				in.nextLine();
			}
			catch (InputMismatchException e) {
				in.next();
				System.out.println("Error: Invalid choice.  Please select from the options listed above.");
				inputMismatch = true;
				validCheck = false;
			}
			boolean numberCheck = false;
			for (int id : campgroundNumbers) {
		        if (id == campgroundChoice) {
		        	numberCheck = true;
		        }
		        else {
		        	validCheck = false;
		        }
		    }
			if (numberCheck == true && inputMismatch == false) {
				validCheck = true;
			}
			if (numberCheck == false && inputMismatch == false) {
				System.out.println("Error: Invalid choice.  Please select from the options listed above.");
				validCheck = false;
			}
			if (campgroundChoice == 0 && inputMismatch == false) {
				validCheck = false;
				cancelCheck = true;
				return cancelCheck;
			}
			if (validCheck == true && inputMismatch == false) {
				return cancelCheck;
			}
		}
	}
	
	public void searchForSites() {
		Date arrivalDateParsed = null;
		Date departureDateParsed = null;
		LocalDate arrivalLocalDate = null;
		LocalDate departureLocalDate = null;
		boolean arrivalParseCheck = false;
		boolean departureParseCheck = false;
		siteInput:
		while (true) {
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
						arrivalParseCheck = true;
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
						departureParseCheck = true;
						break;
					}
				}
			} catch (ParseException e) {
				System.out.println("Please enter the date in the correct format (MM/dd/yyyy).\n");
			}
			if (arrivalParseCheck && departureParseCheck) {
				String formattedArrivalDate = dateFormatOutput.format(arrivalDateParsed);
				String formattedDepartureDate = dateFormatOutput.format(departureDateParsed);
				Period periodBetween = Period.between(arrivalLocalDate, departureLocalDate);
				daysBetween = periodBetween.getDays();
				if (departureLocalDate.isBefore(arrivalLocalDate) || departureLocalDate.isEqual(arrivalLocalDate)) {
					System.out.println("Error: Departure date is before or on arrival date.  Please select a valid date range.\n");
				}
				else {
					listAllAvailableSites(formattedArrivalDate, formattedDepartureDate);
					break siteInput;
				}
			}
		}
	}
	
	public void listAllAvailableSites(String arrivalDate, String departureDate) {
		List<Site> sites = siteDAO.getAllAvailableSites(campgroundChoice, arrivalDate, departureDate);
		siteNumbers = new int[sites.size()];
		if (sites.size() > 0) {
			System.out.println("\nResults Matching Your Search Criteria");
			System.out.printf("%-12s%-12s%-18s%-18s%-14s%-10s\n", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utilities", "Cost");
			for (int i = 0; i < sites.size(); i++) {
				System.out.printf("%-12s%-12s%-18s%-18s%-14s$%-10s\n", sites.get(i).getSiteNumber(), sites.get(i).getMaxOccupancy(),
																	   format.capitalizeFirstLetter(sites.get(i).getAccessible()),
																	   sites.get(i).getMaxRvLength(),
																	   format.capitalizeFirstLetter(sites.get(i).getUtilities()), 
																	   (daysBetween * sites.get(i).getCost()));
				siteNumbers[i] = sites.get(i).getSiteNumber();
			}
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
					break;
				}
				else {
					System.out.println("Error: Invalid choice.  Please select from the options listed above.\n");
				}
			}
		}
	}
	
	public boolean makeReservation() {
		boolean reservationCheck = false;
		int siteChoice;
		reservation:
		while (true) {
			siteCheck:
			while (true) {
				boolean numberCheck = false;
				System.out.print("Which site should be reserved (enter 0 to cancel)? ");
				siteChoice = in.nextInt();
				in.nextLine();
				if (siteChoice == 0) {
					break reservation;
				}
				for (int id : siteNumbers) {
			        if (id == siteChoice) {
			        	numberCheck = true;
			        }
			    }
				if (numberCheck == false) {
					System.out.println("Error: Invalid choice.  Please select from the options listed above.\n");
				}
				else {
					break siteCheck;
				}
			}
			System.out.print("What name should the reservation be made under? ");
			String reservationUnderName = in.nextLine();
			
			reservation = new Reservation();
			int preReservationCheck = reservation.getReservationId();
			reservationDAO.bookReservation(reservation, siteChoice, reservationUnderName, arrivalDate, departureDate);
			
			System.out.println("\nThe reservation has been made and the confirmation id is " + reservation.getReservationId() + ".");
			int postReservationCheck = reservation.getReservationId();
			if (postReservationCheck > preReservationCheck) {
				reservationCheck = true;
				break;
			}
			else {
				System.out.println("Error: Reservation not made.");
				reservationCheck = false;
			}
		}
		return reservationCheck;
	}
	
	public void formatParkDesc(String parkDesc) {
		StringBuilder sb = new StringBuilder(parkDesc);
	    int i = 0;
	    while ((i = sb.indexOf(" ", i + 80)) != -1) {
	        sb.replace(i, i + 1, "\n");
	    }
	    System.out.println(sb.toString());
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
		}
		catch(Exception e) {
			System.out.println("Error: Invalid choice.  Please select an option from the list.");
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

}