package fr.syst3ms.skuared.util;

import fr.syst3ms.skuared.util.evaluation.DoubleOperandTerm;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Operator<T, U, R> {
    private final String symbol;
    private final int precedence;
    private final Associativity associativity;
    private final Class<? extends DoubleOperandTerm> operation;

    Operator(@NotNull String symbol, int precedence, @NotNull Associativity associativity, Class<? extends DoubleOperandTerm> operation) {
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

    Class<? extends DoubleOperandTerm> getOperation() {
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
