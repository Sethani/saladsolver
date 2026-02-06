package nl.sol.puzzles.grid.base;

public class Operator {
    private final Operation operation;
    private final int value;

   public static Operator parse(String s) {
        if (s == null || s.length() < 2) throw new IllegalArgumentException("Bad operator: " + s);
        Operation op = Operation.fromSymbol(s.charAt(0));
        int value = Integer.parseInt(s.substring(1));
        return new Operator(op, value);
}

    public Operator(Operation operation, int value) {
        this.operation = operation;
        this.value = value;
    }

    
    public Operation getOperation() {
        return operation;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + operation.getSymbol() + value;
    }
}
