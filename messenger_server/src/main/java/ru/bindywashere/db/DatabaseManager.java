package ru.bindywashere.db;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

public class DatabaseManager {
    static Dotenv dotenv = Dotenv.load();

    String dbUrl = dotenv.get("DB_URL");
    String dbUsername = dotenv.get("DB_USERNAME");
    String dbPassword = dotenv.get("DB_PASSWORD");
    private static DatabaseManager instance;
    private Connection connection;
    private DatabaseManager() throws SQLException {
        connect();
        initDatabase();
    }

    public static synchronized DatabaseManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        System.out.println("[DB] >> successfully connected w/ DB");
    }

    public void initDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS messages (
                    id SERIAL PRIMARY KEY,
                    nickname TEXT NOT NULL,
                    content TEXT NOT NULL,
                    timestamp TIMESTAMP NOT NULL DEFAULT NOW()
                )
                """);

            stmt.execute("""
                CREATE INDEX IF NOT EXISTS idx_messages_timestamp
                ON messages(timestamp)
                """);

            System.out.println("[DB] >> tables was initialized successfully");
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.out.println("[DB] >> connection is lost, reconnecting...");
            connect();
        }
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] >> connection was closed");
            }
        } catch (SQLException e) {
            System.err.println("[DB] >> an error while closing the connection: " + e.getMessage());
        }
    }
}
