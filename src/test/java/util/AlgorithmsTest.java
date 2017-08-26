package util;

import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.Associativity;
import fr.syst3ms.skuared.util.MathUtils;
import fr.syst3ms.skuared.util.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AlgorithmsTest {
    @Test
    public void shuntingYard() throws Exception {
        Algorithms.registerOperator(">>", MathUtils::shr, Associativity.LEFT, 4);
        Algorithms.registerOperator("<<", MathUtils::shl, Associativity.LEFT, 4);
        Algorithms.registerOperator(">>>", MathUtils::ushr, Associativity.LEFT, 4);
        Algorithms.registerOperator("+", MathUtils::plus, Associativity.LEFT, 3);
        Algorithms.registerOperator("-", MathUtils::minus, Associativity.LEFT, 3);
        Algorithms.registerOperator("*", MathUtils::times, Associativity.LEFT, 2);
        Algorithms.registerOperator("/", MathUtils::divide, Associativity.LEFT, 2);
        Algorithms.registerOperator("%", MathUtils::mod, Associativity.LEFT, 2);
        Algorithms.registerOperator("^", MathUtils::pow, Associativity.RIGHT, 1);
        Algorithms.registerConstant("pi", Math.PI);
        Algorithms.registerConstant("e", Math.E);
        Algorithms.registerConstant("nan", Double.NaN);
        Algorithms.registerConstant("Infinity", Double.POSITIVE_INFINITY);
        assertEquals(Collections.singletonList(StringUtils.toString(Math.E)), Algorithms.shuntingYard("e"));
        assertEquals(Arrays.asList("5", "2", "3", "+", "*"), Algorithms.shuntingYard("5(2 + 3)"));
        assertEquals(Arrays.asList("5", "2", "+", "3", "4", "+", "*"), Algorithms.shuntingYard("(5 + 2)(3 + 4)"));
    }

    @Test
    public void evaluate() throws Exception {
        Algorithms.registerOperator(">>", MathUtils::shr, Associativity.LEFT, 4);
        Algorithms.registerOperator("<<", MathUtils::shl, Associativity.LEFT, 4);
        Algorithms.registerOperator(">>>", MathUtils::ushr, Associativity.LEFT, 4);
        Algorithms.registerOperator("+", MathUtils::plus, Associativity.LEFT, 3);
        Algorithms.registerOperator("-", MathUtils::minus, Associativity.LEFT, 3);
        Algorithms.registerOperator("*", MathUtils::times, Associativity.LEFT, 2);
        Algorithms.registerOperator("/", MathUtils::divide, Associativity.LEFT, 2);
        Algorithms.registerOperator("%", MathUtils::mod, Associativity.LEFT, 2);
        Algorithms.registerOperator("^", MathUtils::pow, Associativity.RIGHT, 1);
        Algorithms.registerConstant("pi", Math.PI);
        Algorithms.registerConstant("e", Math.E);
        Algorithms.registerConstant("nan", Double.NaN);
        Algorithms.registerConstant("Infinity", Double.POSITIVE_INFINITY);
        assertEquals(21.0, Algorithms.evaluate("5 + 2 * 8").doubleValue(), 0.0);
        assertEquals(56.0, Algorithms.evaluate("8(5 + 2)").doubleValue(), 0.0);
        assertEquals(56.0, Algorithms.evaluate("(5 + 2) * 8").doubleValue(), 0.0);
    }

}