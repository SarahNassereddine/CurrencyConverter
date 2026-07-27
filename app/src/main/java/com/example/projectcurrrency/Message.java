package com.example.projectcurrrency;

public class Message {

    private String text;
    private String senderName;
    private long timestamp;

    public Message(){}

    public Message(String text, String senderName, long timestamp) {
        this.text = text;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public String getSenderName() {
        return senderName;
    }

    public long getTimestamp() {
        return timestamp;
    }
}