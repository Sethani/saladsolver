package nl.sol.puzzles.clues;

import java.util.ArrayList;
import java.util.Collection;

import nl.sol.puzzles.path.Path;

abstract public class AbstractClue<I,O> implements Clue<I, O> {
    
    private final Collection<Path<I>> possibleAnswerList = new ArrayList<>();

    @Override
    public Collection<Path<I>> getPossibleAnswers() {
        return this.possibleAnswerList;
    }
    
    @Override
    public boolean isSolved() {
        return !this.possibleAnswerList.isEmpty();
    }

     @Override
    public void addPossibleAnswer(Path<I> path) {
        this.possibleAnswerList.add(path);
    }
    
}
