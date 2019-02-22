package com.techelevator.model;

public class Park {

	private int parkId;
	private String name;
	private String location;
	private String estDate;
	private int area;
	private int annualVisitors;
	private String parkDesc;
	
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getEstDate() {
		return estDate;
	}
	public void setEstDate(String estDate) {
		this.estDate = estDate;
	}
	
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	
	public int getAnnualVisitors() {
		return annualVisitors;
	}
	public void setAnnualVisitors(int annualVisitors) {
		this.annualVisitors = annualVisitors;
	}
	
	public String getParkDesc() {
		return parkDesc;
	}
	public void setParkDesc(String parkDesc) {
		this.parkDesc = parkDesc;
	}
	
}