package ru.bindywashere.db;

import ru.bindywashere.model.Message;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:chat.db";
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
        connection = DriverManager.getConnection(DB_URL);
        System.out.println("[DB] >> successfully connected w/ DB");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL");
            stmt.execute("PRAGMA foreign_keys=ON");
        }
    }

    public void initDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS messages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nickname TEXT NOT NULL,
                    content TEXT NOT NULL,
                    timestamp INTEGER NOT NULL
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
