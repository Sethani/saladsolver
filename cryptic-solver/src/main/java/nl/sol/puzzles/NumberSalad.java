package nl.sol.puzzles;

import java.util.List;

import nl.sol.puzzles.clues.NumberSaladClue;
import nl.sol.puzzles.grid.Grid;
import nl.sol.puzzles.grid.base.Operator;
import nl.sol.puzzles.path.reducers.OperatorPathReducer;

public class NumberSalad extends Puzzle<Operator, Integer> {

    public NumberSalad(Grid<Operator> grid, List<NumberSaladClue> clues) {
        super(grid, new OperatorPathReducer(), clues);
    }


}
