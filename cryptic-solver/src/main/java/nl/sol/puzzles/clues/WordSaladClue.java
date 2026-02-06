package nl.sol.puzzles.clues;

import java.util.List;

import nl.sol.puzzles.path.Path;
import nl.sol.puzzles.path.PathReducer;

public class WordSaladClue extends AbstractClue<Character, String> {
    private final List<Integer> lengths;
    private final String target;

    public WordSaladClue(int length, String target) {
        this.lengths = List.of(length);
        this.target = target;
    }

    public WordSaladClue(List<Integer> lengths, String target) {
        this.lengths = lengths;
        this.target = target;
    }

    @Override
    public int getLength() {
        return this.lengths.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String toString() {
        return "WordSaladClue [length=" + this.getLength() + ", target=" + target + ", answers=" + this.getPossibleAnswers() + "]";
    }

    @Override
    public boolean isSolution(Path<Character> path, PathReducer<Character, String> pathReducer) {
        var solution = pathReducer.reduce(path).orElseThrow(() -> new IllegalArgumentException("Path cannot be reduced: " + path));
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
