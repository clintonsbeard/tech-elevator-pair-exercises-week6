package com.techelevator.projects.model.jdbc;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private ProjectDAO dao;
	private JdbcTemplate jdbcTemplate;
	
	@BeforeClass
	public static void createDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeConnection() {
		dataSource.destroy();
	}
	
	@Before
	public void setupTest() {
		dao = new JDBCProjectDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		/* Create an employee to use */
			/* Write an insert SQL statement */
			/* Use JdbcTemplate to do the insert RETURNING the id */
	}
	
	@After
	public void cleanup() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void get_all_active_projects_gets_1_project() {
		/* Arrange (Setup) */
		List<Project> projects = dao.getAllActiveProjects();
		int originalSize = projects.size();
			/* Create a new project that is active */
		insertProject("Test", "2019-02-01", "2019-04-19");
		/* Act (Do the test, usually one line) */
			/* Get all active projects */
		projects = dao.getAllActiveProjects();
		/* Assert */
			/* Verify that my project is in the list */
		Assert.assertEquals(originalSize + 1, projects.size());
	}
	
	/* Others */
		/* Does it not return a non-active project */
		/* Does it return a project of each active type (null to_date, null from_date, etc.) */
		/* What edge cases exist? */
	
	/* To test add employee to project */
		/* Arrange: Done in the Before */
		/* 1. TEST: Call the add method of the Project DEO using the employee ID generated in the Before */
		/* 2. ASSERT: Create a SELECT FROM project_employee, probably as a private method, to get projects by employee using jdbcTemplate to run the query */
		/* 3. Assert that the number of projects returned by my select is equal to 1 (not 0, not 2+) */
	
	private int insertProject(String name, String fromDate, String toDate) {
		String projectSql = "INSERT INTO project (project_id, name, from_date, to_date) " +
				"VALUES (DEFAULT, ?, '" + fromDate + "', '" + toDate + "') RETURNING project_id";
		SqlRowSet results = jdbcTemplate.queryForRowSet(projectSql, name);
		results.next();
		return results.getInt(1);
	}
	
}