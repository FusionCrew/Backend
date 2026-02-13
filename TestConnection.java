import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://db.rxfdeaizxpybdpffxbxq.supabase.co:5432/postgres?sslmode=require";
        String user = "postgres";
        String password = "Dleodus3380^^";

        System.out.println("=== Supabase Connection Test ===");
        System.out.println("Connecting to: " + url);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Connection Successful!");

            // Test query to verify database is accessible
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT current_database(), version()")) {
                if (rs.next()) {
                    System.out.println("📊 Database: " + rs.getString(1));
                    System.out.println("🔧 PostgreSQL Version: " + rs.getString(2).split(" ")[0] + " "
                            + rs.getString(2).split(" ")[1]);
                }
            }

            // Check if tables exist
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(
                            "SELECT table_name FROM information_schema.tables " +
                                    "WHERE table_schema = 'public' ORDER BY table_name")) {
                System.out.println("\n📋 Tables in database:");
                boolean hasTables = false;
                while (rs.next()) {
                    System.out.println("  - " + rs.getString(1));
                    hasTables = true;
                }
                if (!hasTables) {
                    System.out.println("  (No tables found)");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Connection Failed!");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
