import java.sql.*;

public class DbCheck {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://aws-1-ap-northeast-2.pooler.supabase.com:5432/postgres";
        String user = "postgres.rxfdeaizxpybdpffxbxq";
        String pass = "Dleodus3380^^";
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM order_items LIMIT 1");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.println(i + ": " + rsmd.getColumnName(i) + " (" + rsmd.getColumnTypeName(i) + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
