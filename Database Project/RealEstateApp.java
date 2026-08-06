import java.sql.*;
import java.util.Scanner;

public class RealEstateApp {

    private static final String DB_URL = "jdbc:sqlite:realestate.db";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

      
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found on the classpath.");
            System.out.println("Make sure sqlite-jdbc.jar is present and included with -cp.");
            return;
        }

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

            System.out.println("\nBUYER");
            System.out.println("9. List Buyers");
            System.out.println("10. Add Buyer");
            System.out.println("11. Update Buyer");
            System.out.println("12. Delete Buyer");

            System.out.println("\nSELLER");
            System.out.println("13. List Sellers");
            System.out.println("14. Add Seller");
            System.out.println("15. Update Seller");
            System.out.println("16. Delete Seller");

            System.out.println("\nSALE");
            System.out.println("17. Record Sale");
            System.out.println("18. List Sales");

            System.out.println("\nSEARCH / REPORTS");
            System.out.println("20. Search Properties by City");
            System.out.println("21. Search Properties by Max Price");
            System.out.println("22. Search Properties by School District");
            System.out.println("23. Average Sale Price");
            System.out.println("24. Most Expensive Property");

            System.out.println("\n19. Exit");

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
                    listBuyers();
                    break;

                case "10":
                    insertBuyer();
                    break;

                case "11":
                    updateBuyer();
                    break;

                case "12":
                    deleteBuyer();
                    break;

                case "13":
                    listSellers();
                    break;

                case "14":
                    insertSeller();
                    break;

                case "15":
                    updateSeller();
                    break;

                case "16":
                    deleteSeller();
                    break;

                case "17":
                    recordSale();
                    break;

                case "18":
                    listSales();
                    break;

                case "20":
                    searchCity();
                    break;

                case "21":
                    searchPrice();
                    break;

                case "22":
                    searchSchoolDistrict();
                    break;

                case "23":
                    averageSales();
                    break;

                case "24":
                    mostExpensiveProperty();
                    break;

                case "19":
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
                            "listing_agent INTEGER" +
                            ");");


            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS agent(" +
                            "agent_id INTEGER PRIMARY KEY," +
                            "first_name TEXT," +
                            "last_name TEXT," +
                            "phone TEXT," +
                            "email TEXT" +
                            ");");


            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS buyer(" +
                            "buyer_id INTEGER PRIMARY KEY," +
                            "first_name TEXT," +
                            "last_name TEXT," +
                            "phone TEXT" +
                            ");");


            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS seller(" +
                            "seller_id INTEGER PRIMARY KEY," +
                            "first_name TEXT," +
                            "last_name TEXT," +
                            "phone TEXT" +
                            ");");


            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS sale(" +
                            "sale_id INTEGER PRIMARY KEY," +
                            "property_id INTEGER," +
                            "buyer_id INTEGER," +
                            "seller_id INTEGER," +
                            "selling_agent INTEGER," +
                            "buyers_agent INTEGER," +
                            "sale_price REAL," +
                            "sale_date TEXT" +
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

            System.out.printf("%-5s %-20s %-15s %-30s\n", "ID", "Name", "Phone", "Email");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-15s %-30s\n",

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

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO agent(agent_id, first_name, last_name, phone, email) "
                + " VALUES(?,?,?,?,?)";

        executeUpdate(sql, id, firstName, lastName, phone, email);
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

            System.out.printf("%-5s %-15s %-15s %-15s\n", "ID", "First Name", "Last Name", "Phone");

            while (rs.next()) {

                System.out.printf("%-5d %-15s %-15s %-15s\n",

                        rs.getInt("buyer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Insert Buyer
    private static void insertBuyer() {

        System.out.print("Buyer ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        String sql = "INSERT INTO buyer(buyer_id,first_name,last_name,phone)"
                + " VALUES(?,?,?,?)";

        executeUpdate(sql, id, firstName, lastName, phone);
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

            System.out.printf("%-5s %-15s %-15s %-15s\n", "ID", "First Name", "Last Name", "Phone");

            while (rs.next()) {

                System.out.printf("%-5d %-20s %-20s %-15s\n",

                        rs.getInt("seller_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Insert Seller
    private static void insertSeller() {

        System.out.print("Seller ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        String sql = "INSERT INTO seller(seller_id,first_name,last_name,phone)"
                + " VALUES(?,?,?,?)";

        executeUpdate(sql, id, firstName, lastName, phone);
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

        System.out.print("Seller ID: ");
        int sellerId = Integer.parseInt(scanner.nextLine());

        System.out.print("Selling Agent ID: ");
        int sellingAgent = Integer.parseInt(scanner.nextLine());

        System.out.print("Buyer's Agent ID: ");
        int buyersAgent = Integer.parseInt(scanner.nextLine());

        System.out.print("Sale Price: ");
        double salePrice = Double.parseDouble(scanner.nextLine());

        System.out.print("Sale Date: ");
        String saleDate = scanner.nextLine();

        String sql = "INSERT INTO sale(" +
                "sale_id, property_id, buyer_id, seller_id, selling_agent, buyers_agent, sale_price, sale_date)" +
                " VALUES(?,?,?,?,?,?,?,?)";

        executeUpdate(sql,
                saleId,
                propertyId,
                buyerId,
                sellerId,
                sellingAgent,
                buyersAgent,
                salePrice,
                saleDate);
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
