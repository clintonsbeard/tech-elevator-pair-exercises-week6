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

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;
import com.techelevator.projects.model.Project;

public class JDBCDepartmentDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private DepartmentDAO dao;
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
		dao = new JDBCDepartmentDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@After
	public void cleanup() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void get_all_active_departments_gets_1_department() {
		List<Department> departments = dao.getAllDepartments();
		int originalSize = departments.size();
		insertDepartment("Test Department");
		departments = dao.getAllDepartments();
		Assert.assertEquals(originalSize + 1, departments.size());
	}
	
	private int insertDepartment(String name) {
		String departmentSql = "INSERT INTO department (department_id, name) " +
							   "VALUES (DEFAULT, ?) RETURNING department_id";
		SqlRowSet results = jdbcTemplate.queryForRowSet(departmentSql, name);
		results.next();
		return results.getInt(1);
	}
	
}