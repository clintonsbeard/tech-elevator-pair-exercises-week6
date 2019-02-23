package com.techelevator.view;

import java.text.NumberFormat;
import java.time.Month;
import java.util.Locale;

public class FormatData {
	
	public String formatOutputDate(String date) {
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8);
		String newDateFormat = month + "/" + day + "/" + year;
		return newDateFormat;
	}
	
	public String formatInputDate(String date) {
		String month = date.substring(0, 2);
		String day = date.substring(3, 5);
		String year = date.substring(6);
		String newDateFormat = year + "-" + month + "-" + day;
		return newDateFormat;
	}
	
	public String formatArea(int area) {
		String formattedArea = NumberFormat.getNumberInstance(Locale.US).format(area);
		formattedArea += " sq km";
		return formattedArea;
	}
	
	public String formatAnnualVisitors(int annualVisitors) {
		String formattedAnnualVisitors = NumberFormat.getNumberInstance(Locale.US).format(annualVisitors);
		return formattedAnnualVisitors;
	}
	
	public String convertMonthToString(int monthNumber) {
		String monthString = Month.of(monthNumber).name().toLowerCase();
		char firstLetter = monthString.toUpperCase().charAt(0);
		String firstLetterString = String.valueOf(firstLetter);
		String restOfWord = monthString.substring(1);
		String fixedMonth = firstLetterString + restOfWord;
		return fixedMonth;
	}
	
	public String capitalizeFirstLetter(String wordInput) {
		char firstLetter = wordInput.toUpperCase().charAt(0);
		String restOfWord = wordInput.substring(1);
		String fixedWord = firstLetter + restOfWord;
		return fixedWord;
	}
	
}