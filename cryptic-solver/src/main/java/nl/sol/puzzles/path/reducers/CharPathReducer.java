package nl.sol.puzzles.path.reducers;

import java.util.Optional;

import nl.sol.puzzles.grid.Gridcell;
import nl.sol.puzzles.path.PathReducer;

public final class CharPathReducer implements PathReducer<Character, String> {
    @Override
    public Optional<String> reduce(Iterable<Gridcell<Character>> path) {
        StringBuilder sb = new StringBuilder();
        for (Gridcell<Character> c : path) sb.append(c.getValue());
        return Optional.of(sb.toString());
    }
}
