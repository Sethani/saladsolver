package nl.sol.puzzles;

import java.util.function.Predicate;

import nl.sol.puzzles.path.Path;

@FunctionalInterface
public interface Validator<T> extends Predicate<Path<T>> {

}
