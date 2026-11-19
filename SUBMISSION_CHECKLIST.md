# Part 3 submission checklist (ST10537025)

## A. Get the code running (about 20 min)
1. Unzip, then NetBeans > File > Open Project > QuickChat. Let Maven download dependencies (needs internet).
2. Clean and Build. Run the project and send / store / disregard a message.
3. Right-click project > Test. All tests must pass.
4. Merge with Part 1/2 (see steps below) or keep it as its own repo folder if your lecturer allows.

## B. Merge with your Part 1 and 2 code
- Copy the 4 main classes + test + `.github/workflows/TestJava.yml` into your repo.
- Change the `package` line in every file to your package.
- Add Gson + JUnit 5 + surefire to your pom.xml (copy from the supplied pom.xml).
- Rename my `Message` class if you already have one.
- Main.java: call your Part 1 login at the top of main().
- Main.java: match the recipient regex, MAX_LENGTH and generateMessageID() to your Part 2 rules.
- MessageStore.createMessageHash(): match your Part 2 hash format. If it changes, update the expected hashes in MessageStoreTest.

## C. GitHub (minimum 6 commits for this part or -5%)
Commit as you really add each piece, then push, e.g.:
1. Add Message class and pom dependencies
2. Add MessageStore arrays
3. Add search, longest message and report methods
4. Add delete by hash
5. Add MessageStorage (JSON read/write)
6. Add Stored Messages menu
7. Add unit tests
8. Add TestJava.yml workflow
Then open GitHub > Actions and confirm the run is green. Screenshot it.

## D. Video (own voice, no AI voice, unlisted on YouTube)
See VIDEO_NOTES.md.

## E. Submit on Arc
- Zip of the project (delete the `target` folder first)
- YouTube unlisted link
- GitHub repo link
- AI-use declaration if IIE requires one
- Check that references are consistent Harvard (see README)
