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

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;
import com.techelevator.projects.model.Project;

public class JDBCEmployeeDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private EmployeeDAO dao;
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
		dao = new JDBCEmployeeDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@After
	public void cleanup() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void get_all_employees_gets_1_employee() {
		List<Employee> employees = dao.getAllEmployees();
		int originalSize = employees.size();
		insertEmployee(insertDepartment("Test Department"), "John", "Wick", "2019-02-01", "M", "2019-04-19");
		employees = dao.getAllEmployees();
		Assert.assertEquals(originalSize + 1, employees.size());
	}
	
	private int insertDepartment(String name) {
		String departmentSql = "INSERT INTO department (department_id, name) " +
							   "VALUES (DEFAULT, ?) RETURNING department_id";
		SqlRowSet results = jdbcTemplate.queryForRowSet(departmentSql, name);
		results.next();
		return results.getInt(1);
	}
	
	private int insertEmployee(int departmentId, String firstName, String lastName, String birthDate, String gender, String hireDate) {
		String employeeSql = "INSERT INTO employee (employee_id, department_id, first_name, last_name, birth_date, gender, hire_date) " +
							 "VALUES (DEFAULT, ?, ?, ?, '" + birthDate + "', ?, '" + hireDate + "') RETURNING employee_id";
		SqlRowSet results = jdbcTemplate.queryForRowSet(employeeSql, departmentId, firstName, lastName, gender);
		results.next();
		return results.getInt(1);
	}
	
}