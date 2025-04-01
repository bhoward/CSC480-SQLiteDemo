package edu.depauw.csc480.projectv2.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import edu.depauw.csc480.projectv2.model.Course;
import edu.depauw.csc480.projectv2.model.Dept;
import edu.depauw.csc480.projectv2.model.Enroll;
import edu.depauw.csc480.projectv2.model.Section;
import edu.depauw.csc480.projectv2.model.Student;

/**
 * This class mediates access to the student database, hiding the lower-level
 * DAO objects from the client. Based on Sciore, Section 9.1.
 * 
 * @author bhoward
 */
public class DatabaseManager {
	private Connection conn;
	private DeptDAO deptDAO;
	private StudentDAO studentDAO;
	private CourseDAO courseDAO;
	private SectionDAO sectionDAO;
	private EnrollDAO enrollDAO;

	private final String url = "jdbc:sqlite:db/student.db";
	// private final String url = "jdbc:postgresql://localhost:5432/sciore";

	public DatabaseManager() {
		try {
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(false);
			create(conn);
		} catch (SQLException e) {
			throw new RuntimeException("cannot connect to database", e);
		}

		deptDAO = new DeptDAO(conn, this);
		studentDAO = new StudentDAO(conn, this);
		courseDAO = new CourseDAO(conn, this);
		sectionDAO = new SectionDAO(conn, this);
		enrollDAO = new EnrollDAO(conn, this);
	}

	/**
	 * Initialize the tables in a newly created database
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private void create(Connection conn) throws SQLException {
		DeptDAO.create(conn);
		StudentDAO.create(conn);
		CourseDAO.create(conn);
		SectionDAO.create(conn);
		EnrollDAO.create(conn);
		conn.commit();
	}

	// ***************************************************************
	// Data retrieval functions -- find a model object given its key

	public Dept findDept(int dId) {
		return deptDAO.find(dId);
	}

	public Student findStudent(int sId) {
		return studentDAO.find(sId);
	}

	public Course findCourse(int cId) {
		return courseDAO.find(cId);
	}

	public Section findSection(int sectId) {
		return sectionDAO.find(sectId);
	}

	public Enroll findEnroll(int eId) {
		return enrollDAO.find(eId);
	}

	public Dept findDeptByName(String dName) {
		return deptDAO.findByName(dName);
	}

	public Student findStudentByName(String sName) {
		return studentDAO.findByName(sName);
	}

	public Course findCourseByTitle(String title) {
		return courseDAO.findByTitle(title);
	}

	// ***************************************************************
	// Data retrieval functions -- get collections of objects
	
	public Collection<Student> getStudents() {
		return studentDAO.getAll();
	}
	
	public Collection<Section> getSections() {
		return sectionDAO.getAll();
	}

	// ***************************************************************
	// Data insertion functions -- create new model object from attributes

	public Dept insertDept(int dId, String dName) {
		return deptDAO.insert(dId, dName);
	}

	public Student insertStudent(int sId, String sName, Dept major, int gradYear) {
		return studentDAO.insert(sId, sName, major, gradYear);
	}

	public Course insertCourse(int cId, String title, Dept dept) {
		return courseDAO.insert(cId, title, dept);
	}

	public Section insertSection(int sectId, Course course, String prof, int yearOffered) {
		return sectionDAO.insert(sectId, course, prof, yearOffered);
	}

	public Enroll insertEnroll(int eId, Student student, Section section, String grade) {
		return enrollDAO.insert(eId, student, section, grade);
	}

	// ***************************************************************
	// Utility functions

	/**
	 * Commit changes since last call to commit
	 */
	public void commit() {
		try {
			conn.commit();
		} catch (SQLException e) {
			throw new RuntimeException("cannot commit database", e);
		}
	}

	/**
	 * Abort changes since last call to commit, then close connection
	 */
	public void cleanup() {
		try {
			conn.rollback();
			conn.close();
		} catch (SQLException e) {
			System.out.println("fatal error: cannot cleanup connection");
		}
	}

	/**
	 * Close connection and shutdown database
	 */
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException("cannot close database connection", e);
		}
	}

	/**
	 * Clear out all data from database (but leave empty tables). Note that the
	 * order is the reverse in which the tables were created, because of referential
	 * integrity constraints.
	 */
	public void clearTables() {
		try {
			enrollDAO.clear();
			sectionDAO.clear();
			courseDAO.clear();
			studentDAO.clear();
			deptDAO.clear();
		} catch (SQLException e) {
			throw new RuntimeException("cannot clear tables", e);
		}
	}
}
