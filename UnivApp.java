package univdb;

import java.sql.*
import java.util.Scanner;

public class UnivApp {
	
	// Database URL (create a local file named university.db)
	private static final String DB_URL = "jdbc:sqlite:university.db";
	private static final Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		initializeDatabase();
		
		while (true) {
			System.out.println("\n===DEPARTMENT DATABASE MENU ===");
			System.out.println("1. List All Departments");
			System.out.println("2. Insert Department");
			System.out.println("3. Update Department");
			System.out.println("4. Delete Department");
			System.out.println("5. Exit");
			System.out.println("Select an option: ");
			
			String choice = scanner.nextLine().trim();
			
			switch (choice) {
				case "1": listDepartments(); break;
				case "2": insertDepartment(); break;
				case "3": updateDepartment(); break;
				case "4": deleteDepartment(); break;
				case "5":
					System.out.println("Exiting application.");
					return;
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}
	
	// --- Database Initialization ---
	private static void initializeDatabase() {
		try (Connection conn = DriverManager.getConnection(DB_URL);
			Statement stmt = conn.createStatement()) {
			
			stmt.execute("CREATE TABLE IF NOT EXISTS dept (" +
					"dept_name TEXT PRIMARY KEY, " + 
					"building TEXT, " +
					"budget REAL" +
					");");
			
		} catch (SQLException e) {
			System.err.println("DB Initialization Error: " + e.getMessage());
		}
	}
	
	// ---Helper Method for Modifications (Insert/Update/Delete) ---
	private static void executeUpdate(String sql, Object...objects params) {
		try (Connection conn = DriverManager.getConnection(DB_URL);
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			int rowsAffected = pstmt.executeUpdate();
			System.err.println("Success! Rows affected: " + rowsAffected);
		} catch (SQLExecption e) {
			System.err.println("Database Error: " + e.getMessage());
		}
	}
	
	// --- CRUD Operations ---
	private static void listDepartments() {
		String query = "SELECT * FROM dept";
		System.out.println("\n--- DEPARTMENTS ---");
		System.out.printf("%-20s %-20s %-10s\n", "Dept Name", " Building", "Budget");
		System.out.println("----------------------------------------------------------");
		
		try (Connection conn = DriverManager.getConnection(DB_URL);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			
			while (rs.next()) {
				System.out.printf("%-20s %-20s %-10.2f\n,
						rs.getString("dept_name"),
						rs.getString("building"),
						rs.getDouble("budget"));
			}
		} catch (SQLException e) {
			Syst
		}
	}
}