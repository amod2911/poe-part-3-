package com.prog5121.chatapp;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main.java
 * Student: Muhammed Uwais Amod | Student number: ST10537025
 * Module: PROG5121 - Programming 1A | Part 3 - Store Data and Display Task Report
 *
 * QuickChat console application.
 * Main menu: 1) Send Message  2) Show recently sent messages  3) Quit  4) Stored Messages
 *
 * NOTE: call your Part 1 login / registration before showMainMenu() in your own project.
 */
public class Main {

    private static final String JSON_FILE = "messages.json";
    private static final int MAX_LENGTH = 250;

    private static final Scanner input = new Scanner(System.in);
    private static final MessageStore store = new MessageStore();
    private static final MessageStorage storage = new MessageStorage(JSON_FILE);

    public static void main(String[] args) {
        loadStoredMessages();
        boolean running = true;
        while (running) {
            System.out.println("\n===== QuickChat Menu =====");
            System.out.println("1) Send Message");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            switch (ask("Choose an option: ")) {
                case "1": sendMessage(); break;
                case "2": showSentMessages(); break;
                case "3": running = false; System.out.println("Goodbye!"); break;
                case "4": storedMessagesMenu(); break;
                default: System.out.println("Invalid option, please try again.");
            }
        }
    }

    // ---------------------------------------------------------- JSON file

    /** Reads messages.json into the stored-messages arrays when the app starts. */
    private static void loadStoredMessages() {
        try {
            int loaded = 0;
            for (Message m : storage.load()) {
                if (store.addLoadedMessage(m)) {
                    loaded++;
                }
            }
            System.out.println(loaded + " stored message(s) loaded from " + JSON_FILE + ".");
        } catch (IOException | JsonParseException e) {
            System.out.println("Could not read " + JSON_FILE + ": " + e.getMessage());
        }
    }

    private static void saveStoredMessages() {
        try {
            storage.save(store.getMessagesByFlag(MessageStore.STORED));
        } catch (IOException e) {
            System.out.println("Could not write " + JSON_FILE + ": " + e.getMessage());
        }
    }

    // ------------------------------------------------------ menu option 1

    private static void sendMessage() {
        String recipient;
        do {
            recipient = ask("Recipient cell number (international format, e.g. +27834557896): ");
            if (!recipient.matches("^\\+\\d{9,13}$")) {
                System.out.println("Invalid number. It must start with + and contain 9 to 13 digits.");
                recipient = null;
            }
        } while (recipient == null);

        String text;
        do {
            text = ask("Enter your message (max " + MAX_LENGTH + " characters): ");
            if (text.isEmpty()) {
                System.out.println("The message cannot be empty.");
            } else if (text.length() > MAX_LENGTH) {
                System.out.println("Message is " + (text.length() - MAX_LENGTH)
                        + " characters too long. Please shorten it.");
                text = "";
            }
        } while (text.isEmpty());

        String flag = null;
        while (flag == null) {
            System.out.println("1) Send   2) Disregard   3) Store");
            switch (ask("What would you like to do with this message? ")) {
                case "1": flag = MessageStore.SENT; break;
                case "2": flag = MessageStore.DISREGARD; break;
                case "3": flag = MessageStore.STORED; break;
                default: System.out.println("Please enter 1, 2 or 3.");
            }
        }

        String hash = store.addMessage(generateMessageID(), recipient, text, flag);
        if (flag.equals(MessageStore.STORED)) {
            saveStoredMessages();
        }
        System.out.println("Message " + flag.toLowerCase() + ". Message hash: " + hash);
    }

    /** Random 10 digit message ID that is not already in use. */
    private static String generateMessageID() {
        String id;
        do {
            id = String.format("%010d", ThreadLocalRandom.current().nextLong(10_000_000_000L));
        } while (store.idExists(id));
        return id;
    }

    // ------------------------------------------------------ menu option 2

    private static void showSentMessages() {
        String report = store.generateReport(MessageStore.SENT);
        System.out.println(report.isEmpty() ? "No messages have been sent yet."
                : "\n--- Sent Messages ---\n" + report);
    }

    // ------------------------------------------------------ menu option 4

    private static void storedMessagesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== Stored Messages =====");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages stored for a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display stored messages report");
            System.out.println("0) Back to main menu");
            switch (ask("Choose an option: ")) {
                case "1":
                    System.out.println(store.displaySendersAndRecipients());
                    break;
                case "2":
                    String longest = store.getLongestStoredMessage();
                    System.out.println(longest == null ? "There are no stored messages."
                            : "Longest stored message: " + longest);
                    break;
                case "3":
                    String id = ask("Enter the message ID: ");
                    String text = store.searchByMessageID(id);
                    System.out.println(text == null ? "No message found with that ID."
                            : "Recipient: " + store.getRecipientByMessageID(id) + "\nMessage: " + text);
                    break;
                case "4":
                    List<String> found = store.searchByRecipient(ask("Enter the recipient number: "));
                    if (found.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        found.forEach(m -> System.out.println("- " + m));
                    }
                    break;
                case "5":
                    System.out.println(store.deleteByHash(ask("Enter the message hash: ").toUpperCase()));
                    saveStoredMessages();
                    break;
                case "6":
                    String report = store.generateReport(MessageStore.STORED);
                    System.out.println(report.isEmpty() ? "There are no stored messages."
                            : "\n--- Stored Messages Report ---\n" + report);
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static String ask(String prompt) {
        System.out.print(prompt);
        try {
            return input.nextLine().trim();
        } catch (NoSuchElementException e) {
            // input stream closed (e.g. Ctrl+D): leave the program cleanly
            System.out.println("\nInput closed. Goodbye!");
            System.exit(0);
            return "";
        }
    }
}
