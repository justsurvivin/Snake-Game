import java.sql.*;

public class Connect {
    private static final String URL = "jdbc:mysql://localhost:3306/snake";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static ResultSet getScores() throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT score, tanggal FROM scores ORDER BY score DESC LIMIT 10";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    public static void insScores(int lts_score) throws SQLException {
        Connection conn = getConnection();
        String query = "INSERT INTO scores(score, tanggal) VALUES (?, now());";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, lts_score);
        pstmt.executeUpdate();
    }

    public static void delScores() throws SQLException {
        Connection conn = getConnection();
        String query = "DELETE FROM scores";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.executeUpdate();
    }
}
