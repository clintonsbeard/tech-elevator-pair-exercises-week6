package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.view.FormatData;



public class FormatDataTest {

	private FormatData target;
	
	@Before
	public void setup(){
		target = new FormatData();
	}
	
	@Test
	public void correct_date_displayed_when_entered_incorrectly_by_user(){
		//Arrange
		String before = "2019-03-03";
		//Act
		String actual = target.formatOutputDate(before);
		//Assert
		Assert.assertEquals("03/03/2019", actual);
	}
	
	@Test
	public void correct_date_displayed_when_entered_with_dashes_by_user(){
		//Arrange
		String before = "03-03-2019";
		//Act
		String actual = target.formatInputDate(before);
		//Assert
		Assert.assertEquals("2019-03-03", actual);
	}
	
	@Test
	public void area_formatted_correctly() {
				//Arrange
				int before = 2001;
				//Act
				String actual = target.formatArea(before);
				//Assert
				Assert.assertEquals("2,001 sq km", actual); 
	}
	
	@Test
	public void annual_visitors_formatted_correctly() {
				//Arrange
				int before = 1000000;
				//Act
				String actual = target.formatAnnualVisitors(before);
				//Assert
				Assert.assertEquals("1,000,000", actual); 
	}
	@Test
	public void converts_month_to_string_correctly() {
				//Arrange
				int before = 03;
				//Act
				String actual = target.convertMonthToString(before);
				//Assert
				Assert.assertEquals("March", actual); 
	}
	@Test
	public void first_letter_gets_capitalized() {
				//Arrange
				String before = "yes";
				//Act
				String actual = target.capitalizeFirstLetter(before);
				//Assert
				Assert.assertEquals("Yes", actual); 
	}
	
}
