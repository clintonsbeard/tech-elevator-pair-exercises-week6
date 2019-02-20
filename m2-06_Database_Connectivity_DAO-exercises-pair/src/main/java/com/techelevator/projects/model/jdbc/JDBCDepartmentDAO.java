package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private Department mapRowToDepartment(SqlRowSet results) {
		Department department = new Department();
		department.setId(results.getLong("department_id"));;
		department.setName(results.getString("name"));
		
		return department;
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> departments = new ArrayList<>();
		
		String sqlAllDepartments = "SELECT department_id, name FROM department ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllDepartments);

		while (results.next()) {
			Department d = mapRowToDepartment(results);
			departments.add(d);
		}
		return departments;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		
		List<Department> departments = new ArrayList<>();
		
		String sqlDepartmentByName = "SELECT department_id, name FROM department " +
									 "WHERE name ILIKE ?"; 
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlDepartmentByName, "%" + nameSearch + "%");
		
		while (results.next()) {
			Department d = mapRowToDepartment(results);
			departments.add(d);
		}
		return departments;
	}

	
	private long getNextDepartmentId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_department_id')");
		if(nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new department");
		}
	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		
		String sqlUpdateDepartment = "UPDATE department SET name = ? " +
									 "WHERE department_id = ? ";
		
		jdbcTemplate.update(sqlUpdateDepartment, updatedDepartment.getName(), updatedDepartment.getId());
	}

	@Override
	public Department createDepartment(Department newDepartment) {
		String sqlCreateDepartment = "INSERT INTO department(department_id, name) " + "VALUES(?, ?)";
		newDepartment.setId(getNextDepartmentId());
		jdbcTemplate.update(sqlCreateDepartment, newDepartment.getId(), newDepartment.getName());
		
		return newDepartment;
	}

	@Override
	public Department getDepartmentById(Long id) {
		Department department = null;
		String sqlFindDepartmentById = "SELECT department_id, name " +
							   		   "FROM department " +
							   		   "WHERE department_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindDepartmentById, id);
		if(results.next()) {
			department = mapRowToDepartment(results);
		}
		return department;
	}

}