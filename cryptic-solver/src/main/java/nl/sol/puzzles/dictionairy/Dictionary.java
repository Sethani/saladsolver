package nl.sol.puzzles.dictionairy;

import java.util.List;
import java.util.stream.Stream;

public interface Dictionary {
    boolean containsWord(String word);

    /** May be empty if word exists but no defs loaded */
    List<String> definitions(String word);
    Stream<String> wordsOfLength(int n);
}