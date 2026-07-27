import java.sql.*;
import java.util.Scanner;

public class RealEstateApp {

    private static final String DB_URL = "jdbc:sqlite:realestate.db";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        initializeDatabase();

        while (true) {

            System.out.println("\n========== REAL ESTATE DATABASE ==========");
            System.out.println("PROPERTY");
            System.out.println("1. List Properties");
            System.out.println("2. Add Property");
            System.out.println("3. Update Property");
            System.out.println("4. Delete Property");

            System.out.println("\nAGENT");
            System.out.println("5. List Agents");
            System.out.println("6. Add Agent");
            System.out.println("7. Update Agent");
            System.out.println("8. Delete Agent");

            System.out.println("\n9. Exit");

            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    listProperties();
                    break;

                case "2":
                    insertProperty();
                    break;

                case "3":
                    updateProperty();
                    break;

                case "4":
                    deleteProperty();
                    break;

                case "5":
                    listAgents();
                    break;

                case "6":
                    insertAgent();
                    break;

                case "7":
                    updateAgent();
                    break;

                case "8":
                    deleteAgent();
                    break;

                case "9":
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid Option.");
            }
        }
    }

    // Create Database
    private static void initializeDatabase() {

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS property(" +
                            "property_id INTEGER PRIMARY KEY," +
                            "address TEXT," +
                            "city TEXT," +
                            "school_district TEXT," +
                            "bedrooms INTEGER," +
                            "bathrooms INTEGER," +
                            "price REAL," +
                            "pool TEXT," +
                            "seller_id INTEGER," +
                            "listing_agent INTEGER," +
                            ");");

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS agent(" +
                            "agent_id INTEGER PRIMARY KEY," +
                            "first_name TEXT," +
                            "last_name TEXT," +
                            "phone TEXT," +
                            "email TEXT" +
                            ");");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Generic Update Method
    private static void executeUpdate(String sql, Object... params) {

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            int rows = pstmt.executeUpdate();

            System.out.println(rows + " row(s) affected.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // List Properties
    private static void listProperties() {

        String sql = "SELECT * FROM property";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-20s %-15s %-12s %-8s %-8s %-8s %-8s %-8s %-8s\n",
                    "ID", "Address", "City", "School District", "Beds", "Baths", "Price", "Pool", "Seller ID",
                    "Listing Agent");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-15s %-12s %-8d %-8d $%-8.2f %-8s %-8d %-8d\n",

                        rs.getInt("property_id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("school_district"),
                        rs.getInt("bedrooms"),
                        rs.getInt("bathrooms"),
                        rs.getDouble("price"),
                        rs.getString("pool"),
                        rs.getInt("seller_id"),
                        rs.getInt("listing_agent"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Insert Property
    private static void insertProperty() {

        System.out.print("Property ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("City: ");
        String city = scanner.nextLine();

        System.out.print("School District: ");
        String schoolDistrict = scanner.nextLine();

        System.out.print("Bedrooms: ");
        int bedrooms = Integer.parseInt(scanner.nextLine());

        System.out.print("Bathrooms: ");
        int bathrooms = Integer.parseInt(scanner.nextLine());

        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Pool (Yes/No): ");
        String pool = scanner.nextLine();

        System.out.print("Seller ID: ");
        int sellerId = Integer.parseInt(scanner.nextLine());

        System.out.print("Listing Agent ID: ");
        int listingAgentId = Integer.parseInt(scanner.nextLine());

        String sql = "INSERT INTO property(property_id,address,city,school_district,bedrooms,bathrooms,price,pool,seller_id,listing_agent)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?)";

        executeUpdate(sql, id, address, city, schoolDistrict, bedrooms, bathrooms, price, pool, sellerId,
                listingAgentId);
    }

    // Update Property
    private static void updateProperty() {

        System.out.print("Enter Property ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("New Price: ");
        double price = Double.parseDouble(scanner.nextLine());

        String sql = "UPDATE property SET price=? WHERE property_id=?";

        executeUpdate(sql, price, id);

    }

    // Delete Property
    private static void deleteProperty() {

        System.out.print("Enter Property ID: ");

        int id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM property WHERE property_id=?";

        executeUpdate(sql, id);

    }

    // List Agent
    private static void listAgents() {

        String sql = "SELECT * FROM agent";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-20s %-15s %-12s\n", "ID", "Name", "Phone", "Email");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-15s %-12s\n",

                        rs.getInt("agent_id"),
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Insert Agent
    private static void insertAgent() {

        System.out.print("Agent ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO agent(agent_id,name,phone,email)"
                + " VALUES(?,?,?,?)";

        executeUpdate(sql, id, name, phone, email);
    }

    // Update Agent
    private static void updateAgent() {

        System.out.print("Enter Agent ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("New Phone: ");
        String phone = scanner.nextLine();

        String sql = "UPDATE agent SET phone=? WHERE agent_id=?";

        executeUpdate(sql, phone, id);

    }

    // Delete Agent
    private static void deleteAgent() {

        System.out.print("Enter Agent ID: ");

        int id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM agent WHERE agent_id=?";

        executeUpdate(sql, id);

    }

    // List Buyers
    private static void listBuyers() {

        String sql = "SELECT * FROM buyer";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-20s %-15s %-12s\n", "ID", "Name", "Phone", "Email");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-15s %-12s\n",

                        rs.getInt("buyer_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Insert Buyer
    private static void insertBuyer() {

        System.out.print("Buyer ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO buyer(buyer_id,name,phone,email)"
                + " VALUES(?,?,?,?)";

        executeUpdate(sql, id, name, phone, email);
    }

    // Update Buyer
    private static void updateBuyer() {

        System.out.print("Enter Buyer ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("New Phone: ");
        String phone = scanner.nextLine();

        String sql = "UPDATE buyer SET phone=? WHERE buyer_id=?";

        executeUpdate(sql, phone, id);

    }

    // Delete Buyer
    private static void deleteBuyer() {

        System.out.print("Enter Buyer ID: ");

        int id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM buyer WHERE buyer_id=?";

        executeUpdate(sql, id);

    }

    // List Sellers
    private static void listSellers() {

        String sql = "SELECT * FROM seller";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-20s %-15s %-12s\n", "ID", "Name", "Phone", "Email");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-15s %-12s\n",

                        rs.getInt("seller_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Insert Seller
    private static void insertSeller() {

        System.out.print("Seller ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO seller(seller_id,name,phone,email)"
                + " VALUES(?,?,?,?)";

        executeUpdate(sql, id, name, phone, email);
    }

    // Update Seller
    private static void updateSeller() {

        System.out.print("Enter Seller ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("New Phone: ");
        String phone = scanner.nextLine();

        String sql = "UPDATE seller SET phone=? WHERE seller_id=?";

        executeUpdate(sql, phone, id);

    }

    // Delete Seller
    private static void deleteSeller() {

        System.out.print("Enter Seller ID: ");

        int id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM seller WHERE seller_id=?";

        executeUpdate(sql, id);

    }

    // Record Sale
    private static void recordSale() {

        System.out.print("Sale ID: ");
        int saleId = Integer.parseInt(scanner.nextLine());

        System.out.print("Property ID: ");
        int propertyId = Integer.parseInt(scanner.nextLine());

        System.out.print("Buyer ID: ");
        int buyerId = Integer.parseInt(scanner.nextLine());

        System.out.print("Sale Price: ");
        double salePrice = Double.parseDouble(scanner.nextLine());

        String sql = "INSERT INTO sale(sale_id,property_id,buyer_id,sale_price)"
                + " VALUES(?,?,?,?)";

        executeUpdate(sql, saleId, propertyId, buyerId, salePrice);
    }

    // List Sales
    private static void listSales() {

        String sql = "SELECT * FROM sale";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-5s %-12s %-10s %-10s\n", "Sale ID", "Property ID", "Buyer ID", "Sale Price");

            while (rs.next()) {

                System.out.printf("%-5d %-12d %-10d $%-10.2f\n",

                        rs.getInt("sale_id"),
                        rs.getInt("property_id"),
                        rs.getInt("buyer_id"),
                        rs.getDouble("sale_price"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Search City
    private static void searchCity() {

        System.out.print("Enter City: ");
        String city = scanner.nextLine();

        String sql = "SELECT * FROM property WHERE city=?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, city);

            ResultSet rs = pstmt.executeQuery();

            System.out.printf("%-5s %-20s %-15s %-12s %-8s %-8s %-8s %-8s %-8s %-8s\n",
                    "ID", "Address", "City", "School District", "Beds", "Baths", "Price", "Pool", "Seller ID",
                    "Listing Agent");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-15s %-12s %-8d %-8d $%-8.2f %-8s %-8d %-8d\n",

                        rs.getInt("property_id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("school_district"),
                        rs.getInt("bedrooms"),
                        rs.getInt("bathrooms"),
                        rs.getDouble("price"),
                        rs.getString("pool"),
                        rs.getInt("seller_id"),
                        rs.getInt("listing_agent"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Search Price
    private static void searchPrice() {

        System.out.print("Enter Maximum Price: ");
        double maxPrice = Double.parseDouble(scanner.nextLine());

        String sql = "SELECT * FROM property WHERE price<=?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, maxPrice);

            ResultSet rs = pstmt.executeQuery();

            System.out.printf("%-5s %-20s %-15s %-12s %-8s %-8s %-8s %-8s %-8s %-8s\n",
                    "ID", "Address", "City", "School District", "Beds", "Baths", "Price", "Pool", "Seller ID",
                    "Listing Agent");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-15s %-12s %-8d %-8d $%-8.2f %-8s %-8d %-8d\n",

                        rs.getInt("property_id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("school_district"),
                        rs.getInt("bedrooms"),
                        rs.getInt("bathrooms"),
                        rs.getDouble("price"),
                        rs.getString("pool"),
                        rs.getInt("seller_id"),
                        rs.getInt("listing_agent"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Search School District
    private static void searchSchoolDistrict() {

        System.out.print("Enter School District: ");
        String schoolDistrict = scanner.nextLine();

        String sql = "SELECT * FROM property WHERE school_district=?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, schoolDistrict);

            ResultSet rs = pstmt.executeQuery();

            System.out.printf("%-5s %-20s %-15s %-12s %-8s %-8s %-8s %-8s %-8s %-8s\n",
                    "ID", "Address", "City", "School District", "Beds", "Baths", "Price", "Pool", "Seller ID",
                    "Listing Agent");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-15s %-12s %-8d %-8d $%-8.2f %-8s %-8d %-8d\n",

                        rs.getInt("property_id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("school_district"),
                        rs.getInt("bedrooms"),
                        rs.getInt("bathrooms"),
                        rs.getDouble("price"),
                        rs.getString("pool"),
                        rs.getInt("seller_id"),
                        rs.getInt("listing_agent"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Average Sales
    private static void averageSales() {

        String sql = "SELECT AVG(sale_price) AS avg_sale_price FROM sale";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.printf("Average Sale Price: $%.2f\n", rs.getDouble("avg_sale_price"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Most Expensive Property
    private static void mostExpensiveProperty() {

        String sql = "SELECT * FROM property ORDER BY price DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.printf(
                        "Most Expensive Property:\nID: %d\nAddress: %s\nCity: %s\nSchool District: %s\nBedrooms: %d\nBathrooms: %d\nPrice: $%.2f\nPool: %s\nSeller ID: %d\nListing Agent: %d\n",
                        rs.getInt("property_id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("school_district"),
                        rs.getInt("bedrooms"),
                        rs.getInt("bathrooms"),
                        rs.getDouble("price"),
                        rs.getString("pool"),
                        rs.getInt("seller_id"),
                        rs.getInt("listing_agent"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}