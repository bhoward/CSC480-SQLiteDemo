package edu.depauw.csc480.jdbc;

import java.sql.*;
import java.util.Scanner;

public class PreparedFindMajors {
	public static void main(String[] args) {
		System.out.print("Enter a department name: ");
		Scanner sc = new Scanner(System.in);
		String major = sc.next();
		sc.close();

		String qry = "select sname, gradyear from student, dept where did = majorid and dname = ?";

		String url = "jdbc:sqlite:db/student.db";
		// String url = "jdbc:postgresql://localhost:5432/sciore";
		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(qry)) {
			pstmt.setString(1, major);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("Here are the " + major + " majors");
			System.out.println("Name\tGradYear");
			while (rs.next()) {
				String sname = rs.getString("sname");
				int gradyear = rs.getInt("gradyear");
				System.out.println(sname + "\t" + gradyear);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}