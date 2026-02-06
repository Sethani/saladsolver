package nl.sol.puzzles.grid;

import java.util.ArrayList;

final public class GridLayoutGeneratorUtil {

    private GridLayoutGeneratorUtil() {

    }
    
    static public int[][] generateSquareGridLayout(int gridSize) {

        int[][] base = new int[gridSize*gridSize][];

        for(int row=0; row<gridSize; row++) {
            for(int col=0; col<gridSize; col++) {
                int index = generateIndex(row, col, gridSize);
                ArrayList<Integer> neighbours = new ArrayList<>();
                for(int dr=-1; dr<=1; dr++) {
                    for(int dc=-1; dc<=1; dc++) {
                        if(dr == 0 && dc == 0) continue; // skip self
                        int newRow = row + dr;
                        int newCol = col + dc;
                        if(newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize) {
                            int neighbourIndex = generateIndex(newRow, newCol, gridSize);
                            neighbours.add(neighbourIndex);
                        }
                    }
                }
                base[index] = neighbours.stream().mapToInt(i->i).toArray();
            }
        }
        return base;
    }

    static private int generateIndex(int row, int col, int gridSize) {
        return row * gridSize + col;
    }


}
