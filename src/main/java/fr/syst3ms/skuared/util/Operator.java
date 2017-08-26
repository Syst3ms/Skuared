package fr.syst3ms.skuared.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

@SuppressWarnings("unused")
public class Operator<T, U, R> {
    @NotNull
    private final String symbol;
    private final int precedence;
    @NotNull
    private final Associativity associativity;
    @NotNull
    private final BiFunction<T, U, R> operation;

    Operator(@NotNull String symbol, int precedence, @NotNull Associativity associativity, @NotNull BiFunction<T, U, R> operation) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.associativity = associativity;
        this.operation = operation;
    }

    int getPrecedence() {
        return precedence;
    }

    @NotNull
    Associativity getAssociativity() {
        return associativity;
    }

    @NotNull
    BiFunction<T, U, R> getOperation() {
        return operation;
    }

    @NotNull
    public String getSymbol() {
        return symbol;
    }

    @NotNull
    @Override
    public String toString() {
        return "Symbol : " + symbol + ", precedence : " + precedence + ", associativity : " + associativity;
    }

}
