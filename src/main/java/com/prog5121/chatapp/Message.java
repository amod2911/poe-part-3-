package com.prog5121.chatapp;

/**
 * Message.java
 * Student: Muhammed Uwais Amod | Student number: ST10537025
 * Module: PROG5121 - Programming 1A | Part 3
 *
 * Simple data class that represents one QuickChat message. It is also the
 * object that is written to / read from the messages.json file.
 */
public class Message {

    private final int messageNumber;
    private final String messageID;
    private final String messageHash;
    private final String recipient;
    private final String messageText;
    private final String flag; // "Sent", "Stored" or "Disregard"

    public Message(int messageNumber, String messageID, String messageHash,
                   String recipient, String messageText, String flag) {
        this.messageNumber = messageNumber;
        this.messageID = messageID;
        this.messageHash = messageHash;
        this.recipient = recipient;
        this.messageText = messageText;
        this.flag = flag;
    }

    public int getMessageNumber() { return messageNumber; }
    public String getMessageID()  { return messageID; }
    public String getMessageHash(){ return messageHash; }
    public String getRecipient()  { return recipient; }
    public String getMessageText(){ return messageText; }
    public String getFlag()       { return flag; }
}
