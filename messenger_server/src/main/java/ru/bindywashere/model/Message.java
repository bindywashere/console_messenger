package ru.bindywashere.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Message {
    private long id;
    private String nickname;
    private String content;
    private Instant timestamp;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

    public Message(String nickname, String content) {
        this.nickname = nickname;
        this.content = content;
        this.timestamp = Instant.now();
    }

    public Message(long id, String nickname, String content, Instant timestamp) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getContent() {
        return content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFormattedTime() {
        LocalDateTime dateTime = LocalDateTime.ofInstant((timestamp), ZoneId.systemDefault());

        return dateTime.format(FORMATTER);
    }

    @Override
    public String toString() {
        return "[ " + getFormattedTime() + " ] " + content;
    }
}

