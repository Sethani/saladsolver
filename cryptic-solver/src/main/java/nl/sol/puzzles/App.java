package nl.sol.puzzles;

import java.util.Arrays;

import nl.sol.puzzles.clues.NumberSaladClue;
import nl.sol.puzzles.grid.Grid;
import nl.sol.puzzles.grid.GridLayoutGeneratorUtil;
import nl.sol.puzzles.grid.base.Operator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String[] operations = {"+1", "-2", "*3", "-4"};
        var grid = new Grid<>(GridLayoutGeneratorUtil.generateSquareGridLayout(2), Arrays.stream(operations).map(Operator::parse).toArray(Operator[]::new));
        var puzzle = new NumberSalad(grid, Arrays.asList(new NumberSaladClue(3, -18)));
        System.out.println(puzzle.solve());

    }
}
