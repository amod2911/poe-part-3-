package com.prog5121.chatapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageStorage.java
 * Student: Muhammed Uwais Amod | Student number: ST10537025
 * Module: PROG5121 - Programming 1A | Part 3
 *
 * Writes stored messages to a JSON file and reads them back into a list.
 *
 * Attribution:
 *  - Reading/writing a List of objects with Gson (toJson, fromJson and TypeToken) follows the
 *    approach in the Gson User Guide: Google (n.d.) Gson User Guide. Available at:
 *    https://github.com/google/gson/blob/main/UserGuide.md (Accessed: 19 November 2026).
 *  - Files.readString / Files.writeString follow the Java SE 17 API documentation:
 *    Oracle (2021) Files (Java SE 17 & JDK 17). Available at:
 *    https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/Files.html
 *    (Accessed: 19 November 2026).
 */
public class MessageStorage {

    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public MessageStorage(String fileName) {
        this.file = Paths.get(fileName);
    }

    /** Writes the given messages to the JSON file (overwrites the old content). */
    public void save(List<Message> messages) throws IOException {
        Files.writeString(file, gson.toJson(messages), StandardCharsets.UTF_8);
    }

    /** Reads the JSON file into a list of messages. Returns an empty list if there is no file. */
    public List<Message> load() throws IOException, JsonParseException {
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        String json = Files.readString(file, StandardCharsets.UTF_8);
        Type listType = new TypeToken<List<Message>>() { }.getType();
        List<Message> messages = gson.fromJson(json, listType);
        return messages == null ? new ArrayList<>() : messages;
    }
}
