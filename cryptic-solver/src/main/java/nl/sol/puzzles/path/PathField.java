package nl.sol.puzzles.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.sol.puzzles.Validator;
import nl.sol.puzzles.grid.Grid;
import nl.sol.puzzles.grid.Gridcell;

public class PathField<T> {
    private ArrayList<Path<T>> paths = new ArrayList<>();
    private int length = 1;

    public PathField(Grid<T> grid) {
        for (Gridcell<T> cell : grid) {
            paths.add(new Path<>(cell));
        }
        this.length = 1;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<Path<T>> getPaths() {
        return paths;
    }

    public void extend() {
        ArrayList<Path<T>> newPaths = new ArrayList<>();
        for (Path<T> path : paths) {
            if(path.canPathContinue())
            {
                var neighbours = path.getPathContinuation();
                for (Gridcell<T> neighbour : neighbours) {
                    newPaths.add(path.extend(neighbour));
                }
            }
        }
        this.paths = newPaths;
        this.length++;
    }

    public void removeInvalidAnswers(Validator<T> validator) {
        removeInvalidAnswers(List.of(validator));
    }

    public void removeInvalidAnswers(Collection<Validator<T>> validators) {
        ArrayList<Path<T>> validPaths = new ArrayList<>();
        for (Path<T> path : paths) {
            boolean isValid = true;
            for (var validator : validators) {
                if (!validator.test(path)) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                validPaths.add(path);
            }
        }
        this.paths = validPaths;
    }

    public static <T> Validator<T> convertReducerToValidator(PathReducer<T, ?> reducer) {
        return path -> reducer.reduce(path).isPresent();
    }

    public void extendAndRemoveInvalidAnswers(List<Validator<T>> validators) {
        extend();
        removeInvalidAnswers(validators);
    }

    public void extendAndRemoveInvalidAnswers(Validator<T> validator) {
        extend();
        removeInvalidAnswers(validator);
    }
}
