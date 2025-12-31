package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton class for managing database connections.
 * Part of the DAO pattern - handles SQLite database connection.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:student_app.db";

    // Private constructor - prevents external instantiation (Singleton pattern)
    private DatabaseConnection() throws SQLException {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(DB_URL);
            initializeTables();
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }
    }

    /**
     * Gets the singleton instance of DatabaseConnection.
     * Thread-safe implementation.
     */
    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.connection == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Returns the database connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Initializes database tables if they don't exist.
     */
    private void initializeTables() throws SQLException {
        String createStudentsTable = """
            CREATE TABLE IF NOT EXISTS students (
                student_id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                age INTEGER NOT NULL,
                course TEXT NOT NULL,
                email TEXT
            )""";

        String createAttendanceTable = """
            CREATE TABLE IF NOT EXISTS attendance (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_id TEXT NOT NULL,
                date TEXT NOT NULL,
                status TEXT NOT NULL,
                FOREIGN KEY (student_id) REFERENCES students(student_id)
            )""";

        String createPaymentsTable = """
            CREATE TABLE IF NOT EXISTS payments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_id TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                description TEXT,
                FOREIGN KEY (student_id) REFERENCES students(student_id)
            )""";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createStudentsTable);
            stmt.execute(createAttendanceTable);
            stmt.execute(createPaymentsTable);
            
            // Migration: Add enrollment_status column if it doesn't exist
            migrateEnrollmentStatus();
        }
    }

    /**
     * Migrates database schema to add enrollment_status column.
     * Handles existing databases gracefully.
     */
    private void migrateEnrollmentStatus() {
        try {
            // Check if column exists by trying to query it
            try (Statement stmt = connection.createStatement()) {
                stmt.executeQuery("SELECT enrollment_status FROM students LIMIT 1");
            }
        } catch (SQLException e) {
            // Column doesn't exist, add it
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("ALTER TABLE students ADD COLUMN enrollment_status TEXT DEFAULT 'ENROLLED'");
                System.out.println("Database migration: Added enrollment_status column");
            } catch (SQLException migrationException) {
                System.err.println("Failed to migrate enrollment_status column: " + migrationException.getMessage());
            }
        }
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
