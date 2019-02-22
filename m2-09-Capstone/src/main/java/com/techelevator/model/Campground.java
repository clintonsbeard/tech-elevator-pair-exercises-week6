package com.techelevator.model;

public class Campground {

	private int campgroundId;
	private String name;
	private int openMonth;
	private int closeMonth;
	private double dailyFee;
	
	public int getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getOpenMonth() {
		return openMonth;
	}
	public void setOpenMonth(int openMonth) {
		this.openMonth = openMonth;
	}
	
	public int getCloseMonth() {
		return closeMonth;
	}
	public void setCloseMonth(int closeMonth) {
		this.closeMonth = closeMonth;
	}
	
	public double getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(double dailyFee) {
		this.dailyFee = dailyFee;
	}
	
}