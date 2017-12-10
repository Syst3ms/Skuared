package fr.syst3ms.skuared.util;

import ch.njol.skript.Skript;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static fr.syst3ms.skuared.util.Algorithms.*;
import static org.junit.Assert.assertEquals;

public class AlgorithmsTest {

    static {
        registerOperator(">>", MathUtils::shr, Associativity.LEFT, 4);
        registerOperator("<<", MathUtils::shl, Associativity.LEFT, 4);
        registerOperator(">>>", MathUtils::ushr, Associativity.LEFT, 4);
        registerOperator("+", MathUtils::plus, Associativity.LEFT, 3);
        registerOperator("-", MathUtils::minus, Associativity.LEFT, 3);
        registerOperator("*", MathUtils::times, Associativity.LEFT, 2);
        registerOperator("/", MathUtils::divide, Associativity.LEFT, 2);
        registerOperator("%", MathUtils::mod, Associativity.LEFT, 2);
        registerOperator("^", MathUtils::pow, Associativity.RIGHT, 1);
        registerConstant("pi", Math.PI);
        registerConstant("e", Math.E);
        registerConstant("nan", Double.NaN);
        registerConstant("Infinity", Double.POSITIVE_INFINITY);
        registerConstant("phi", MathUtils.PHI);
    }

    @Test
    public void shuntingYardTest() throws Exception {
        assertEquals(Collections.singletonList(StringUtils.toString(Math.E)), shuntingYard("e"));
        assertEquals(Arrays.asList("5", "2", "3", "+", "*"), shuntingYard("5(2 + 3)"));
        assertEquals(Arrays.asList("5", "2", "+", "3", "4", "+", "*"), shuntingYard("(5 + 2)(3 + 4)"));
    }

    @Test
    public void evaluateTest() throws Exception {
        assertEquals(21.0, evaluate("5 + 2 * 8").doubleValue(), 0.0);
        assertEquals(56.0, evaluate("8(5 + 2)").doubleValue(), 0.0);
        assertEquals(56.0, evaluate("(5 + 2) * 8").doubleValue(), 0.0);
        assertEquals(19.7392088021, evaluate("2pi^2").doubleValue(), Skript.EPSILON);
        assertEquals(MathUtils.PHI, evaluate("phi").doubleValue(), Skript.EPSILON);
    }

}