package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private Project mapRowToProject(SqlRowSet results) {
		Project project = new Project();
		project.setId(results.getLong("project_id"));
		project.setName(results.getString("name"));
		if (results.getDate("from_date") != null) {
			project.setStartDate(results.getDate("from_date").toLocalDate());
		}
		if (results.getDate("to_date") != null) {
			project.setEndDate(results.getDate("to_date").toLocalDate());
		}
		return project;
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		List<Project> projects = new ArrayList<>();
		String sqlAllProjects= "SELECT project_id, name, from_date, to_date FROM project " + 
							   "WHERE NOW() > from_date AND NOW() < to_date " + 
							   "OR from_date IS NULL AND to_date IS NULL " + 
							   "OR NOW() > from_date AND to_date IS NULL " + 
							   "OR NOW() < to_date AND from_date IS NULL";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllProjects);
		while (results.next()) {
			Project p = mapRowToProject(results);
			projects.add(p);
		}
		return projects;
	}
	
	// BRING IN DATES

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String removeEmployeeFromProject = "DELETE FROM project_employee " +
				  					       "WHERE project_id = ? AND employee_id = ?";
		jdbcTemplate.update(removeEmployeeFromProject, projectId, employeeId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String addEmployeeToProject = "INSERT INTO project_employee (project_id, employee_id) " +
				 					  "VALUES (?, ?)";
		jdbcTemplate.update(addEmployeeToProject, projectId, employeeId);
	}

}