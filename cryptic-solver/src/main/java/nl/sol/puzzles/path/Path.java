package nl.sol.puzzles.path;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nl.sol.puzzles.grid.Gridcell;

public final class Path<T> implements Iterable<Gridcell<T>> {

    private final Gridcell<T>[] cells;          // keeps the actual path order
    private final Set<Gridcell<T>> visited;     // fast "have we been here?" checks
    private final List<Gridcell<T>> cellsView;  // cheap iteration without allocations

    @SuppressWarnings("unchecked")
    public Path(Gridcell<T> start) {
        if (start == null) throw new IllegalArgumentException("start cannot be null");

        this.cells = (Gridcell<T>[]) new Gridcell<?>[] { start };

        // HashSet: fastest general-purpose Set (no order guarantees, but we don't need order here)
        Set<Gridcell<T>> v = new HashSet<>(4);
        v.add(start);
        this.visited = Collections.unmodifiableSet(v);

        // Safe because cells is never mutated after construction
        this.cellsView = Collections.unmodifiableList(Arrays.asList(this.cells));
    }

    @SuppressWarnings("unchecked")
    public Path(Path<T> previous, Gridcell<T> next) {
        if (previous == null) throw new IllegalArgumentException("previous cannot be null");
        if (next == null) throw new IllegalArgumentException("next cannot be null");

        Gridcell<T>[] newCells = (Gridcell<T>[]) new Gridcell<?>[previous.cells.length + 1];
        System.arraycopy(previous.cells, 0, newCells, 0, previous.cells.length);
        newCells[newCells.length - 1] = next;
        this.cells = newCells;

        Set<Gridcell<T>> v = new HashSet<>(previous.visited.size() + 1);
        v.addAll(previous.visited);
        v.add(next);
        this.visited = Collections.unmodifiableSet(v);

        this.cellsView = Collections.unmodifiableList(Arrays.asList(this.cells));
    }

    public Path<T> extend(Gridcell<T> next) {
        return new Path<>(this, next);
    }

    /** Always returns the actual last cell in this path (O(1)). */
    public Gridcell<T> getLastCell() {
        return cells[cells.length - 1];
    }

    public int length() {
        return cells.length;
    }

    /**
     * Fast "visited" view for neighbour filtering / containment checks.
     * Note: Set order is not guaranteed (HashSet).
     */
    public Set<Gridcell<T>> getVisited() {
        return visited;
    }

    /**
     * If you truly need the path cells in order, iterate over Path itself.
     * (This avoids copying.) If you need a defensive copy, use getPathCopy().
     */
    public List<Gridcell<T>> asList() {
        return cellsView;
    }

    /** Defensive copy (costs O(n)). Use only if you must expose an independent array. */
    public Gridcell<T>[] getPathCopy() {
        return Arrays.copyOf(cells, cells.length);
    }

    public Set<Gridcell<T>> getPathContinuation() {
        // avoids allocating Arrays.asList(cells) each call
        return getLastCell().getUnvisitedNeighbours(visited);
    }

    public boolean canPathContinue() {
        return !getPathContinuation().isEmpty();
    }

    @Override
    public Iterator<Gridcell<T>> iterator() {
        return cellsView.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path(length=").append(cells.length).append("): ");
        for (Gridcell<T> cell : cells) {
            sb.append(cell.getValue()).append(" -> ");
        }
        if (cells.length > 0) {
            sb.setLength(sb.length() - 4); // remove last arrow
        }
        return sb.toString();
    }
}