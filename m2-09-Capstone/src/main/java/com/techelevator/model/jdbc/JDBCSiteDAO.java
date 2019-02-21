package com.techelevator.model.jdbc;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {

	@Override
	public Site mapRowToSite(SqlRowSet results) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Site> getAllAvailableSites() {
		// TODO Auto-generated method stub
		return null;
	}

}