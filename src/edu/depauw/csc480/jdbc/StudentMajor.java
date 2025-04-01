package edu.depauw.csc480.jdbc;

import java.sql.*;

public class StudentMajor {
	public static void main(String[] args) {
		String url = "jdbc:sqlite:db/student.db";
		// String url = "jdbc:postgresql://localhost:5432/sciore";
		String qry = "select SName, DName from DEPT, STUDENT where MajorId = DId";

		try (Connection conn = DriverManager.getConnection(url);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(qry)) {
			System.out.println("Name\tMajor");
			while (rs.next()) {
				String sname = rs.getString("SName");
				String dname = rs.getString("DName");
				System.out.println(sname + "\t" + dname);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
