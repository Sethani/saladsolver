package nl.sol.puzzles.grid;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Grid<T> implements Iterable<Gridcell<T>> {
    final private Gridcell<T>[] gridcells;

    @SuppressWarnings("unchecked")
    public Grid(int[][] gridLayout, T[] cells) throws UnmatchedSizeException {
        if(gridLayout.length != cells.length) {
            throw new UnmatchedSizeException("Grid layout size does not match number of cells");
        }

        this.gridcells = List.of(cells).stream()
                .map(Gridcell<T>::new)
                .toArray(Gridcell[]::new);
        
        for(int row = 0; row < gridLayout.length; row++) {
            for(int col = 0; col < gridLayout[row].length; col++) {
                int n = gridLayout[row][col];
                if (n < 0 || n >= gridcells.length) {
                    throw new UnmatchedSizeException("Invalid neighbour index " + n + " for cell " + row);
                }
                gridcells[row].addNeighbour(gridcells[n]);
            }
        }
    }

    public Gridcell<T> getCell(int index) throws IndexOutOfBoundsException {
        if(index < 0 || index >= gridcells.length) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for grid of size " + gridcells.length);
        }
        return this.gridcells[index];
    }

    @Override
    public Iterator<Gridcell<T>> iterator() {
        return Arrays.asList(this.gridcells).iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<gridcells.length; i++) {
            sb.append("Cell ").append(i).append(": ").append(gridcells[i].getValue()).append(" Neighbours: ");
            for(Gridcell<T> n : gridcells[i].getNeighbours()) {
                sb.append(n.getValue()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
