package nl.sol.puzzles.path;

import java.util.Optional;

import nl.sol.puzzles.grid.Gridcell;

@FunctionalInterface
public interface PathReducer<T, O> {
    Optional<O> reduce(Iterable<Gridcell<T>> path);
}