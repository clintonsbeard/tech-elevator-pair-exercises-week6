package com.techelevator.model;

public class Site {

	private int siteId;
	private int siteNumber;
	private int maxOccupancy;
	private String accessible;
	private String maxRvLength;
	private String utilities;
	private String cost;
	
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	
	public int getSiteNumber() {
		return siteNumber;
	}
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	
	public String getAccessible() {
		return accessible;
	}
	public void setAccessible(String accessible) {
		this.accessible = accessible;
	}
	
	public String getMaxRvLength() {
		int maxRvLengthInt = Integer.parseInt(maxRvLength);
		if (maxRvLengthInt <= 0) {
			return "N/A";
		}
		return maxRvLength;
	}
	public void setMaxRvLength(String maxRvLength) {
		this.maxRvLength = maxRvLength;
	}
	
	public String getUtilities() {
		return utilities;
	}
	public void setUtilities(String utilities) {
		this.utilities = utilities;
	}
	
	public int getCost() {
		double doubleCost = Double.parseDouble(cost);
		int intCost = (int) doubleCost;
		return intCost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	
}