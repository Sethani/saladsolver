package nl.sol.puzzles.grid.base;

public enum Operation {
    ADD('+'),
    SUBTRACT('-'),
    MULTIPLY('*'),
    DIVIDE('/');

    private final char symbol;

    Operation(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static Operation fromSymbol(char c) {
    for (Operation op : values()) {
        if (op.symbol == c) return op;
    }
    throw new IllegalArgumentException("Unknown op symbol: " + c);
}

}