package nl.sol.puzzles;

import java.util.Map;

import nl.sol.puzzles.clues.Clue;
import nl.sol.puzzles.grid.Grid;
import nl.sol.puzzles.path.PathField;
import nl.sol.puzzles.path.PathReducer;

public class Puzzle<T,O> {
    final private Grid<T> grid;
    final private PathReducer<T, O> pathReducer;
    private final Map<Integer, ? extends Clue<T, O>> clues;

    public Puzzle(Grid<T> grid, PathReducer<T, O> pathReducer, Map<Integer, ? extends Clue<T, O>> clues) {
        this.grid = grid;
        this.pathReducer = pathReducer;
        this.clues = clues;
    }

    public int getMaxUnsolvedClueLength() {
        return clues.values().stream().filter(c -> !c.isSolved()).mapToInt(Clue::getLength).max().orElse(Integer.MIN_VALUE); 
    }

    public Map<Integer, ? extends Clue<T, O>> solve() {
        PathField<T> pathField = new PathField<>(this.grid);
        while(pathField.getLength() <= getMaxUnsolvedClueLength()) {
            pathField.extendAndRemoveInvalidAnswers(PathField.convertReducerToValidator(this.pathReducer));
            var cluesToCheck = clues.values().stream().filter(c -> !c.isSolved() && c.getLength() == pathField.getLength()).toList();
            for(var clue : cluesToCheck) {
                var paths = pathField.getPaths();
                for(var path : paths) {
                    if(clue.isSolution(path, this.pathReducer)) {
                        clue.addPossibleAnswer(path);
                    }
                }
            }
        }
        return this.clues;
    }
    

}
