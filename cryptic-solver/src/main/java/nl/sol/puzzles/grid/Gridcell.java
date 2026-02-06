package nl.sol.puzzles.grid;

import java.util.HashSet;
import java.util.Set;

public class Gridcell<T> {
    final private T value;
    final private Set<Gridcell<T>> neighbours = new HashSet<>();

    public Gridcell(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public Set<Gridcell<T>> getNeighbours() {
        return neighbours;
    }

    public Set<Gridcell<T>> getUnvisitedNeighbours(Set<Gridcell<T>> visited) {
        Set<Gridcell<T>> unvisited = new HashSet<>();
        for (Gridcell<T> n : neighbours) {
            if (!visited.contains(n)) unvisited.add(n);
        }
        return unvisited;
    }

    public void addNeighbour(Gridcell<T> gridcell) {
        neighbours.add(gridcell);
    }


}
