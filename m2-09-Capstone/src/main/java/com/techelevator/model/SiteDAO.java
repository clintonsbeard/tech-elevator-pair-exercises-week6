package com.techelevator.model;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface SiteDAO {

	public Site mapRowToSite(SqlRowSet results);
	
	public List<Site> getAllAvailableSites();
	
}