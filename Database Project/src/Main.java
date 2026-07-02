import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/school";
        String username = "root";
        String password = "yourPassword";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Statement stmt = conn.createStatement();

// ResultSet rs = stmt.executeQuery("SELECT * FROM students");

//while (rs.next()) {
//    System.out.println(rs.getString("name"));
//}