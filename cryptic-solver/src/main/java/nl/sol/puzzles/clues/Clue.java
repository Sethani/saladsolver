package nl.sol.puzzles.clues;

import java.util.Collection;

import nl.sol.puzzles.path.Path;
import nl.sol.puzzles.path.PathReducer;


 public interface Clue<I,O> {

    public int getLength();
    public boolean isSolution(Path<I> path, PathReducer<I, O> pathReducer);
    public boolean isSolved();
    public void addPossibleAnswer(Path<I> path);
    public Collection<Path<I>> getPossibleAnswers();
}
