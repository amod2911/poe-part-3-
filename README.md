# QuickChat - PROG5121 Part 3 (Store Data and Display Task Report)

Student: Muhammed Uwais Amod | Student number: ST10537025

## Run in NetBeans
1. File > Open Project > select this folder (Maven project, JDK 17+).
2. Right-click project > Run (main class `com.prog5121.chatapp.Main`).
3. Right-click project > Test File / Test Project to run the JUnit tests.

## Structure
- `Message` - data class (also the JSON object)
- `MessageStore` - the arrays + search / delete / report logic
- `MessageStorage` - writes and reads `messages.json` (Gson)
- `Main` - console menu (1 Send, 2 Show sent, 3 Quit, 4 Stored Messages)
- `src/test/.../MessageStoreTest` - unit tests using the test data from the brief
- `.github/workflows/TestJava.yml` - runs the tests automatically on GitHub

## References (Harvard)
Anthropic (2026) Claude [Large language model]. Available at: https://claude.ai (Accessed: 19 November 2026).

Google (n.d.) Gson User Guide. Available at: https://github.com/google/gson/blob/main/UserGuide.md (Accessed: 19 November 2026).

Oracle (2021) Files (Java SE 17 & JDK 17). Available at: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/Files.html (Accessed: 19 November 2026).
## Testing and automation

In NetBeans, right-click the project and choose Test to run the JUnit tests. They cover message arrays, longest-message lookup, ID and recipient searches, deleting by hash, reports, and JSON storage and loading.

GitHub Actions runs `mvn -B test` with JDK 17 when changes are pushed or a pull request is opened against `main` or `master`.
