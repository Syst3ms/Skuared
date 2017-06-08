package fr.syst3ms.skuared.util;

import java.util.function.BiFunction;

public class Operator<T, U, R> {
    private final String symbol;
    private final int precedence;
    private final Associativity associativity;
    private final BiFunction<T, U, R> operation;

    Operator(String symbol, int precedence, Associativity associativity, BiFunction<T, U, R> operation) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.associativity = associativity;
        this.operation = operation;
    }

    public int getPrecedence() {
        return precedence;
    }

    public Associativity getAssociativity() {
        return associativity;
    }

    public BiFunction<T, U, R> getOperation() {
        return operation;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Symbol : " + symbol + ", precedence : " + precedence + ", associativity : " + associativity;
    }

    public enum Associativity {
        RIGHT,
        LEFT
    }
}
