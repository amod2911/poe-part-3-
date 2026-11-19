package com.prog5121.chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * MessageStoreTest.java
 * Student: Muhammed Uwais Amod | Student number: ST10537025
 * Module: PROG5121 - Programming 1A | Part 3 - unit tests (assertEquals)
 */
class MessageStoreTest {

    private static final String M1 = "Did you get the cake?";
    private static final String M2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String M3 = "Yohoooo, I am at your gate.";
    private static final String M4 = "It is dinner time!";
    private static final String M5 = "Ok, I am leaving without you.";

    private MessageStore store;

    /** Test data from the brief (Test Data Message 1 - 5) used to populate the arrays. */
    @BeforeEach
    void setUp() {
        store = new MessageStore();
        store.addMessage("1000000001", "+27834557896", M1, MessageStore.SENT);
        store.addMessage("2000000002", "+27838884567", M2, MessageStore.STORED);
        store.addMessage("3000000003", "+27834484567", M3, MessageStore.DISREGARD);
        store.addMessage("0838884567", "0838884567", M4, MessageStore.SENT);
        store.addMessage("5000000005", "+27838884567", M5, MessageStore.STORED);
    }

    @Test
    void sentMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(M1, M4), store.getSentMessages());
    }

    @Test
    void displayTheLongestMessage() {
        assertEquals(M2, store.getLongestStoredMessage());
    }

    @Test
    void searchForMessageID() {
        assertEquals(M4, store.searchByMessageID("0838884567"));
    }

    @Test
    void searchAllMessagesForARecipient() {
        assertEquals(Arrays.asList(M2, M5), store.searchByRecipient("+27838884567"));
    }

    @Test
    void deleteAMessageUsingTheMessageHash() {
        String hash = store.getMessageHashes().get(1); // Test Message 2
        assertEquals("Message: \"" + M2 + "\" successfully deleted.", store.deleteByHash(hash));
        assertEquals(Collections.singletonList(M5), store.getStoredMessages());
    }

    @Test
    void deleteWithUnknownHashDeletesNothing() {
        assertEquals("No message with hash XX:9:NONE was found.", store.deleteByHash("XX:9:NONE"));
        assertEquals(2, store.getStoredMessages().size());
    }

    @Test
    void displayReport() {
        String expected =
                "Message Hash: 10:0:DIDCAKE\nMessage ID: 1000000001\nRecipient: +27834557896\nMessage: " + M1 + "\n\n"
              + "Message Hash: 08:3:ITTIME\nMessage ID: 0838884567\nRecipient: 0838884567\nMessage: " + M4 + "\n\n";
        assertEquals(expected, store.generateReport(MessageStore.SENT));
    }

    @Test
    void messageHashHasCorrectFormat() {
        assertEquals("20:1:WHERETIME", store.getMessageHashes().get(1));
    }

    @Test
    void disregardedMessagesArrayCorrectlyPopulated() {
        assertEquals(Collections.singletonList(M3), store.getDisregardedMessages());
    }

    @Test
    void storedMessagesAreWrittenToAndReadFromJson(@TempDir Path dir) throws IOException {
        Path jsonFile = dir.resolve("messages.json");
        MessageStorage storage = new MessageStorage(jsonFile.toString());
        storage.save(store.getMessagesByFlag(MessageStore.STORED));

        MessageStore reloaded = new MessageStore();
        for (Message m : storage.load()) {
            reloaded.addLoadedMessage(m);
        }
        List<String> expected = Arrays.asList(M2, M5);
        assertEquals(expected, reloaded.getStoredMessages());
        assertEquals(store.getMessageHashes().get(1), reloaded.getMessageHashes().get(0));
    }

    @Test
    void searchForUnknownMessageIDReturnsNull() {
        assertNull(store.searchByMessageID("9999999999"));
    }
}
