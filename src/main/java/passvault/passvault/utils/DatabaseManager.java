package passvault.passvault.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:passwords.db";

    public DatabaseManager() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // We don't need to create any tables here anymore
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public void createUserTable(String username) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + sanitizeTableName(username) + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "website TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "UNIQUE(website, username))";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void savePassword(String userTableName, String website, String username, String password) throws SQLException {
        String checkSql = "SELECT * FROM " + sanitizeTableName(userTableName) + " WHERE website = ? AND username = ?";
        String updateSql = "UPDATE " + sanitizeTableName(userTableName) + " SET password = ? WHERE website = ? AND username = ?";
        String insertSql = "INSERT INTO " + sanitizeTableName(userTableName) + " (website, username, password) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            checkStmt.setString(1, website);
            checkStmt.setString(2, username);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Entry exists, update password
                    updateStmt.setString(1, password);
                    updateStmt.setString(2, website);
                    updateStmt.setString(3, username);
                    updateStmt.executeUpdate();
                } else {
                    // New entry, insert
                    insertStmt.setString(1, website);
                    insertStmt.setString(2, username);
                    insertStmt.setString(3, password);
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    public boolean entryExists(String userTableName, String website, String username, String password) throws SQLException {
        String sql = "SELECT * FROM " + sanitizeTableName(userTableName) + " WHERE website = ? AND username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, website);
            pstmt.setString(2, username);
            pstmt.setString(3, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<String> getPasswords(String userTableName) throws SQLException {
        List<String> passwords = new ArrayList<>();
        String sql = "SELECT * FROM " + sanitizeTableName(userTableName);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String entry = String.format("Website: %s | Username: %s | Password: %s",
                        rs.getString("website"),
                        rs.getString("username"),
                        rs.getString("password"));
                passwords.add(entry);
            }
        }
        return passwords;
    }

    private String sanitizeTableName(String tableName) {
        // Remove any non-alphanumeric characters to prevent SQL injection
        return tableName.replaceAll("[^a-zA-Z0-9]", "");
    }
}