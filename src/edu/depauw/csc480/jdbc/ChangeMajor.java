package edu.depauw.csc480.jdbc;

import java.sql.*;

public class ChangeMajor {
	public static void main(String[] args) {
		String url = "jdbc:sqlite:db/student.db";
		String cmd = "update STUDENT set MajorId=30 where SName='amy'";

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
			int howmany = stmt.executeUpdate(cmd);
			System.out.println(howmany + " records changed.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
