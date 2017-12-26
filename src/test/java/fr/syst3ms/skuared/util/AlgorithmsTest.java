package fr.syst3ms.skuared.util;

import ch.njol.skript.Skript;
import fr.syst3ms.skuared.util.evaluation.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static fr.syst3ms.skuared.util.Algorithms.*;
import static org.junit.Assert.assertEquals;

public class AlgorithmsTest {

    static {
        Algorithms.registerOperator(">>", RightBitShift.class, Associativity.LEFT, 4);
        Algorithms.registerOperator("<<", LeftBitShift.class, Associativity.LEFT, 4);
        Algorithms.registerOperator(">>>", UnsignedRightBitShift.class, Associativity.LEFT, 4);
        Algorithms.registerOperator("+", Addition.class, Associativity.LEFT, 3);
        Algorithms.registerOperator("-", Substraction.class, Associativity.LEFT, 3);
        Algorithms.registerOperator("*", Product.class, Associativity.LEFT, 2);
        Algorithms.registerOperator("/", Division.class, Associativity.LEFT, 2);
        Algorithms.registerOperator("%", Modulo.class, Associativity.LEFT, 2);
        Algorithms.registerOperator("^", Exponentiation.class, Associativity.RIGHT, 1);
        Algorithms.registerConstant("pi", Math.PI);
        Algorithms.registerConstant("e", Math.E);
        Algorithms.registerConstant("nan", Double.NaN);
        Algorithms.registerConstant("Infinity", Double.POSITIVE_INFINITY);
        Algorithms.registerConstant("phi", MathUtils.PHI);
    }

    @Test
    public void shuntingYardTest() throws Exception {
        assertEquals(Collections.singletonList(StringUtils.toString(Math.E)), shuntingYard("e", null));
        assertEquals(Arrays.asList("5", "2", "3", "+", "*"), shuntingYard("5(2 + 3)", null));
        assertEquals(Arrays.asList("5", "2", "+", "3", "4", "+", "*"), shuntingYard("(5 + 2)(3 + 4)", null));
    }

    @Test
    public void evaluateTest() throws Exception {
        assertEquals(21.0, evaluate("5 + 2 * 8", null).doubleValue(), 0.0);
        assertEquals(56.0, evaluate("8(5 + 2)", null).doubleValue(), 0.0);
        assertEquals(56.0, evaluate("(5 + 2) * 8", null).doubleValue(), 0.0);
        assertEquals(19.7392088021, evaluate("2pi^2", null).doubleValue(), Skript.EPSILON);
        assertEquals(MathUtils.PHI, evaluate("phi", null).doubleValue(), Skript.EPSILON);
        assertEquals(8.0, evaluate("2(x+2)", new HashMap<String, Number>(){{put("x", 2.0);}}).doubleValue(), Skript.EPSILON);
        assertEquals(77.8802336483881, evaluate("2^(x*pi)", new HashMap<String, Number>(){{put("x", 2.0);}}).doubleValue(), Skript.EPSILON);
    }

}