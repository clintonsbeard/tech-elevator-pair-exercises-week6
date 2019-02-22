package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.CampgroundCLI;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;

public class Menu {

	private PrintWriter out;
	private Scanner in;
	private Park park;
	private DataSource dataSource;
	private ParkDAO parkDAO;
	
	public Menu(InputStream input, OutputStream output, DataSource dataSource) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
		this.dataSource = dataSource;
		
		parkDAO = new JDBCParkDAO(dataSource);
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
	
	public void displayAllParks() {
		System.out.println("View Parks Interface");
		System.out.println("Select a Park for Further Details");
		
		List<Park> allParks = parkDAO.getAllAvailableParks();
		listAllParks(allParks);
		
		displayParkInformation(getParkChoiceFromUser());
	}
	
	public int getParkChoiceFromUser() {
		int parkChoice = in.nextInt();
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
	
	public void displayParkInformation(int parkChoice) {
		Park chosenPark = parkDAO.getParkDescription(parkChoice);
		System.out.println(chosenPark.getName());
	}
	
}