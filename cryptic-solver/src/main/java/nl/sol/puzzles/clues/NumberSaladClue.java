package nl.sol.puzzles.clues;

import nl.sol.puzzles.grid.base.Operator;
import nl.sol.puzzles.path.Path;
import nl.sol.puzzles.path.PathReducer;

public class NumberSaladClue  extends AbstractClue<Operator, Integer> {
    private final int length;
    private final int target;

    public NumberSaladClue(int length, int target) {
        this.length = length;
        this.target = target;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public boolean isSolution(Path<Operator> path, PathReducer<Operator, Integer> pathReducer) {
        var solution = pathReducer.reduce(path).orElseThrow(() -> new IllegalArgumentException("Path cannot be reduced: " + path));
        return this.target == solution; 
    }

    @Override
    public String toString() {
        return "NumberSaladClue [length=" + length + ", target=" + target + ", answers=" + this.getPossibleAnswers() + "]"; 
    }

   

    
}
