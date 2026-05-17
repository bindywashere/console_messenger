package ru.bindywashere.db;

import ru.bindywashere.model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private final DatabaseManager dbManager;

    public MessageDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public long saveMessage(Message message) throws SQLException {
        String sql = """
            INSERT INTO messages (nickname, content, timestamp)
            VALUES (?, ?, ?)
            """;

        try (PreparedStatement pstmt = dbManager.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, message.getNickname());
            pstmt.setString(2, message.getContent());
            pstmt.setLong(3, message.getTimestamp());

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    message.setId(id);
                    return id;
                }
                throw new SQLException("couldn't get ID after INSERT");
            }
        }
    }

    public List<Message> showHistory() throws SQLException {
        String sql = "SELECT id, nickname, content, timestamp FROM messages ORDER BY timestamp ASC";

        List<Message> list = new ArrayList<>();
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Message mapRow(ResultSet rs) throws SQLException {
        return new Message(rs.getLong("id"), rs.getString("nickname"), rs.getString("content"), rs.getLong("timestamp"));
    }
}
