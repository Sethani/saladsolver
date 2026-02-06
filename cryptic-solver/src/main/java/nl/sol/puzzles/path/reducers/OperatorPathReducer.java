package nl.sol.puzzles.path.reducers;

import java.util.Iterator;
import java.util.Optional;

import nl.sol.puzzles.grid.Gridcell;
import nl.sol.puzzles.grid.base.Operation;
import nl.sol.puzzles.grid.base.Operator;
import nl.sol.puzzles.path.PathReducer;

public final class OperatorPathReducer implements PathReducer<Operator, Integer> {

    @Override
    public Optional<Integer> reduce(Iterable<Gridcell<Operator>> path) {
        Iterator<Gridcell<Operator>> it = path.iterator();
        if (!it.hasNext()) return Optional.empty();

        Operator first = it.next().getValue();
        Operation op = first.getOperation();

        if (op == Operation.MULTIPLY || op == Operation.DIVIDE) {
            return Optional.empty();
        }

        int acc = (op == Operation.ADD)
                ? first.getValue()
                : -first.getValue(); // SUBTRACT at start means "0 - value"

        while (it.hasNext()) {
            Operator cur = it.next().getValue();
            switch (cur.getOperation()) {
                case ADD -> acc += cur.getValue();
                case SUBTRACT -> acc -= cur.getValue();
                case MULTIPLY -> acc *= cur.getValue();
                case DIVIDE -> { return Optional.empty(); }
            }
        }

        return Optional.of(acc);
    }
}