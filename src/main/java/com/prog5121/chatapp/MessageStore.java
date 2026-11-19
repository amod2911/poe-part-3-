package com.prog5121.chatapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MessageStore.java
 * Student: Muhammed Uwais Amod | Student number: ST10537025
 * Module: PROG5121 - Programming 1A | Part 3
 *
 * Holds all of the arrays required by Part 3 and the operations that work on them.
 *
 * Arrays (ArrayLists are used so the arrays can grow as the user adds messages):
 *  - sentMessages          : text of every message that was sent
 *  - disregardedMessages   : text of every message that was disregarded
 *  - storedMessages        : text of every stored message
 *  - storedRecipients      : recipient of each stored message (parallel to storedMessages)
 *  - messageHashes         : hash of every message
 *  - messageIDs            : ID of every message
 *
 * messageIDs, messageHashes, messageRecipients, messageTexts and messageFlags are
 * parallel arrays: index i in each of them describes the same message.
 * No data is hard-coded here; every array is populated by addMessage() (user input,
 * unit-test data) or addLoadedMessage() (messages read from the JSON file).
 */
public class MessageStore {

    public static final String SENT = "Sent";
    public static final String STORED = "Stored";
    public static final String DISREGARD = "Disregard";

    // Required arrays
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> disregardedMessages = new ArrayList<>();
    private final ArrayList<String> storedMessages = new ArrayList<>();
    private final ArrayList<String> storedRecipients = new ArrayList<>();
    private final ArrayList<String> messageHashes = new ArrayList<>();
    private final ArrayList<String> messageIDs = new ArrayList<>();

    // Parallel arrays that describe every message (used for searching / reports)
    private final ArrayList<String> messageRecipients = new ArrayList<>();
    private final ArrayList<String> messageTexts = new ArrayList<>();
    private final ArrayList<String> messageFlags = new ArrayList<>();
    private final ArrayList<Integer> messageNumbers = new ArrayList<>();

    private int nextMessageNumber = 0;

    // ---------------------------------------------------------------- adding

    /**
     * Creates the hash for a new message, adds it to the arrays and returns the hash.
     */
    public String addMessage(String id, String recipient, String text, String flag) {
        String hash = createMessageHash(id, nextMessageNumber, text);
        register(nextMessageNumber, id, hash, recipient, text, flag);
        nextMessageNumber++;
        return hash;
    }

    /**
     * Adds a message that was read from the JSON file (keeps its saved hash and number).
     * Returns false if a message with the same ID is already in the arrays.
     */
    public boolean addLoadedMessage(Message m) {
        if (messageIDs.contains(m.getMessageID())) {
            return false;
        }
        register(m.getMessageNumber(), m.getMessageID(), m.getMessageHash(),
                 m.getRecipient(), m.getMessageText(), m.getFlag());
        nextMessageNumber = Math.max(nextMessageNumber, m.getMessageNumber() + 1);
        return true;
    }

    private void register(int number, String id, String hash, String recipient,
                          String text, String flag) {
        switch (flag) {
            case SENT:
                sentMessages.add(text);
                break;
            case DISREGARD:
                disregardedMessages.add(text);
                break;
            case STORED:
                storedMessages.add(text);
                storedRecipients.add(recipient);
                break;
            default:
                throw new IllegalArgumentException("Unknown flag: " + flag);
        }
        messageNumbers.add(number);
        messageIDs.add(id);
        messageHashes.add(hash);
        messageRecipients.add(recipient);
        messageTexts.add(text);
        messageFlags.add(flag);
    }

    /**
     * Message hash format: first two digits of the ID : message number : FIRSTWORDLASTWORD
     * e.g. "Did you get the cake?" with ID 1000000001 as message 0 -> 10:0:DIDCAKE
     */
    public static String createMessageHash(String id, int messageNumber, String text) {
        String[] words = text.trim().split("\\s+");
        String first = words[0].replaceAll("[^A-Za-z0-9]", "");
        String last = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        String prefix = id.length() >= 2 ? id.substring(0, 2) : id;
        return (prefix + ":" + messageNumber + ":" + first + last).toUpperCase();
    }

    // ------------------------------------------------------------ Part 3 (2a)

    /** 2a. Sender and recipient of every stored message. */
    public String displaySendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append("Sender: You | Recipient: ").append(storedRecipients.get(i)).append("\n");
        }
        return sb.toString();
    }

    /** 2b. The longest stored message (null if there are no stored messages). */
    public String getLongestStoredMessage() {
        String longest = null;
        for (String message : storedMessages) {
            if (longest == null || message.length() > longest.length()) {
                longest = message;
            }
        }
        return longest;
    }

    /** 2c. Search by message ID. Returns the message text, or null if not found. */
    public String searchByMessageID(String id) {
        int index = messageIDs.indexOf(id);
        return index == -1 ? null : messageTexts.get(index);
    }

    /** 2c. Recipient that belongs to a message ID (null if not found). */
    public String getRecipientByMessageID(String id) {
        int index = messageIDs.indexOf(id);
        return index == -1 ? null : messageRecipients.get(index);
    }

    /** 2d. All sent or stored messages for one recipient (disregarded ones are skipped). */
    public List<String> searchByRecipient(String recipient) {
        List<String> found = new ArrayList<>();
        for (int i = 0; i < messageRecipients.size(); i++) {
            boolean wanted = messageFlags.get(i).equals(STORED) || messageFlags.get(i).equals(SENT);
            if (wanted && messageRecipients.get(i).equals(recipient)) {
                found.add(messageTexts.get(i));
            }
        }
        return found;
    }

    /** 2e. Deletes a message using its hash and returns a confirmation sentence. */
    public String deleteByHash(String hash) {
        int index = messageHashes.indexOf(hash);
        if (index == -1) {
            return "No message with hash " + hash + " was found.";
        }
        String text = messageTexts.get(index);
        removeAt(index);
        return "Message: \"" + text + "\" successfully deleted.";
    }

    /** Removes message i from the parallel arrays and from its flag array. */
    private void removeAt(int index) {
        String flag = messageFlags.get(index);

        // position of the message inside its own flag array = number of earlier messages with that flag
        int local = 0;
        for (int i = 0; i < index; i++) {
            if (messageFlags.get(i).equals(flag)) {
                local++;
            }
        }
        switch (flag) {
            case SENT:
                sentMessages.remove(local);
                break;
            case DISREGARD:
                disregardedMessages.remove(local);
                break;
            default:
                storedMessages.remove(local);
                storedRecipients.remove(local);
                break;
        }
        messageNumbers.remove(index);
        messageIDs.remove(index);
        messageHashes.remove(index);
        messageRecipients.remove(index);
        messageTexts.remove(index);
        messageFlags.remove(index);
    }

    /** 2f. Report listing the full details of all messages with the given flag. */
    public String generateReport(String flag) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messageFlags.size(); i++) {
            if (messageFlags.get(i).equals(flag)) {
                sb.append("Message Hash: ").append(messageHashes.get(i)).append("\n");
                sb.append("Message ID: ").append(messageIDs.get(i)).append("\n");
                sb.append("Recipient: ").append(messageRecipients.get(i)).append("\n");
                sb.append("Message: ").append(messageTexts.get(i)).append("\n\n");
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------- helpers

    /** Builds Message objects for every message with the given flag (used for the JSON file). */
    public List<Message> getMessagesByFlag(String flag) {
        List<Message> list = new ArrayList<>();
        for (int i = 0; i < messageFlags.size(); i++) {
            if (messageFlags.get(i).equals(flag)) {
                list.add(new Message(messageNumbers.get(i), messageIDs.get(i), messageHashes.get(i),
                        messageRecipients.get(i), messageTexts.get(i), flag));
            }
        }
        return list;
    }

    public boolean idExists(String id) { return messageIDs.contains(id); }

    public List<String> getSentMessages()        { return Collections.unmodifiableList(sentMessages); }
    public List<String> getDisregardedMessages() { return Collections.unmodifiableList(disregardedMessages); }
    public List<String> getStoredMessages()      { return Collections.unmodifiableList(storedMessages); }
    public List<String> getStoredRecipients()    { return Collections.unmodifiableList(storedRecipients); }
    public List<String> getMessageHashes()       { return Collections.unmodifiableList(messageHashes); }
    public List<String> getMessageIDs()          { return Collections.unmodifiableList(messageIDs); }
}
