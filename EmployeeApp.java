import java.sql.*;
import java.util.Scanner;

public class EmployeeApp {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Ganesh@1911";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Employee Management System ---");
            System.out.println("1. Add Employee (Create)");
            System.out.println("2. View All Employees (Read)");
            System.out.println("3. Update Employee Salary (Update)");
            System.out.println("4. Delete Employee (Delete)");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: addEmployee(scanner); break;
                case 2: viewEmployees(); break;
                case 3: updateEmployee(scanner); break;
                case 4: deleteEmployee(scanner); break;
                case 5: System.out.println("Exiting..."); return;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void addEmployee(Scanner scanner) {
        System.out.print("Enter Employee ID (e.g., EMP001): ");
        String empId = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Position: ");
        String position = scanner.nextLine();
        System.out.print("Enter Salary: ");
        double salary = scanner.nextDouble();

        String sql = "INSERT INTO employees (employee_id, name, position, salary) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empId);
            pstmt.setString(2, name);
            pstmt.setString(3, position);
            pstmt.setDouble(4, salary);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee added successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
        }
    }

    private static void viewEmployees() {
        String sql = "SELECT employee_id, name, position, salary FROM employees";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- Current Employees ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("ID: %s, Name: %s, Position: %s, Salary: %.2f\n",
                    rs.getString("employee_id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getDouble("salary"));
            }
            if (!found) {
                System.out.println("No employees found in the database.");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employees: " + e.getMessage());
        }
    }

    private static void updateEmployee(Scanner scanner) {
        System.out.print("Enter Employee ID to update: ");
        String empId = scanner.nextLine();
        System.out.print("Enter new Salary: ");
        double newSalary = scanner.nextDouble();

        String sql = "UPDATE employees SET salary = ? WHERE employee_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newSalary);
            pstmt.setString(2, empId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee salary updated successfully!");
            } else {
                System.out.println("Employee ID not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
        }
    }

    private static void deleteEmployee(Scanner scanner) {
        System.out.print("Enter Employee ID to delete: ");
        String empId = scanner.nextLine();

        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Employee ID not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }
}