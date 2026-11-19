# Video talking points (say these in your own words, own voice)

1. Intro: name, student number, module, "Part 3 - Store Data and Display Task Report".
2. Run the app: send a message, disregard one, store two. Show messages.json appearing and restart to show it reloading.
3. Stored Messages menu: demo 2a to 2f (senders/recipients, longest, search ID, search recipient, delete by hash, report).
4. Code walk-through (explain the logic and flow - this earns marks):
   - Message: plain data class, also the JSON object.
   - MessageStore: the five arrays + parallel arrays; index i in each describes the same message.
   - addMessage -> createMessageHash (first 2 ID digits : message number : FIRST+LAST word) -> register() puts text in the right flag array.
   - Longest message: loop, keep the longest.
   - Search ID / recipient: loop or indexOf on the parallel arrays.
   - Delete: indexOf(hash), work out the position in its flag array, remove from every array.
   - MessageStorage: Gson toJson to save, fromJson + TypeToken to load; say where it was learned (Gson User Guide).
5. Constructs used: variables/scope, data types, classes/methods, operators, decisions, loops, arrays.
6. Unit tests: run them, point at assertEquals and the test data, then show the green GitHub Actions run.
7. Mention sources used and any AI assistance honestly.
