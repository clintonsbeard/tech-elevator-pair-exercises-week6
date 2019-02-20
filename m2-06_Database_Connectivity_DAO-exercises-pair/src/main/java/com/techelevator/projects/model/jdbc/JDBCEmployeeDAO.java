package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private Employee mapRowToEmployee(SqlRowSet results) {
		Employee employee = new Employee();
		employee.setId(results.getLong("employee_id"));
		employee.setDepartmentId(results.getLong("department_id"));
		employee.setFirstName(results.getString("first_name"));
		employee.setLastName(results.getString("last_name"));
	
		return employee;
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		
		List<Employee> employees = new ArrayList<>();
		
		String sqlAllEmployees = "SELECT employee_id, department_id, first_name, last_name FROM employee";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllEmployees);

		while (results.next()) {
			Employee e = mapRowToEmployee(results);
			employees.add(e);
		}
		return employees;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		
		List<Employee> employees = new ArrayList<>();
		
		String sqlEmployeeByFirstLastName = "SELECT employee_id, department_id, first_name, last_name FROM employee " +
									    "WHERE first_name ILIKE ? OR last_name ILIKE ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlEmployeeByFirstLastName, "%" + firstNameSearch + "%", "%" + lastNameSearch + "%");
		
		while (results.next()) {
			Employee e = mapRowToEmployee(results);
			employees.add(e);
		}
		return employees;
	}
	
	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		
		List<Employee> employees = new ArrayList<>();
		
		String sqlEmployeeByDepartmentID = "SELECT employee_id, department_id, first_name, last_name FROM employee " +
									       "WHERE department_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlEmployeeByDepartmentID, id);
		
		while (results.next()) {
			Employee e = mapRowToEmployee(results);
			employees.add(e);
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		
		List<Employee> employees = new ArrayList<>();
		
		String sqlEmployeeByDepartmentID = "SELECT e.employee_id, e.department_id, e.first_name, e.last_name FROM employee e " +
										   "LEFT JOIN project_employee pe ON e.employee_id = pe.employee_id " +
										   "WHERE pe.employee_id IS NULL";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlEmployeeByDepartmentID);
		
		while (results.next()) {
			Employee e = mapRowToEmployee(results);
			employees.add(e);
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		
		List<Employee> employees = new ArrayList<>();
		
		String sqlGetEmployeesByProjectId = "SELECT e.employee_id, e.department_id, e.first_name, e.last_name FROM employee e " +
										    "JOIN project_employee pe ON e.employee_id = pe.employee_id " +
										    "WHERE pe.project_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeesByProjectId, projectId);
		
		while (results.next()) {
			Employee e = mapRowToEmployee(results);
			employees.add(e);
		}
		return employees;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		
		String sqlChangeEmployeeDepartment = "UPDATE employee SET department_id = ? " +
				 							 "WHERE employee_id = ?";

		jdbcTemplate.update(sqlChangeEmployeeDepartment, departmentId, employeeId);
	}

}