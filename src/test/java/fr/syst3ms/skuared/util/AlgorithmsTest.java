package fr.syst3ms.skuared.util;

import ch.njol.skript.Skript;
import fr.syst3ms.skuared.util.evaluation.*;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

import static fr.syst3ms.skuared.util.Algorithms.*;
import static org.junit.Assert.assertEquals;

public class AlgorithmsTest {

    static {
        Algorithms.registerOperator(">>", RightBitShift.class, Associativity.LEFT, 4);
        Algorithms.registerOperator("<<", LeftBitShift.class, Associativity.LEFT, 4);
        Algorithms.registerOperator(">>>", UnsignedRightBitShift.class, Associativity.LEFT, 4);
        Algorithms.registerOperator("+", Sum.class, Associativity.LEFT, 3);
        Algorithms.registerOperator("-", Difference.class, Associativity.LEFT, 3);
        Algorithms.registerOperator("*", Product.class, Associativity.LEFT, 2);
        Algorithms.registerOperator("/", Division.class, Associativity.LEFT, 2);
        Algorithms.registerOperator("%", Modulo.class, Associativity.LEFT, 2);
        Algorithms.registerOperator("^", Power.class, Associativity.RIGHT, 1);
        Algorithms.registerConstant("pi", Math.PI);
        Algorithms.registerConstant("e", Math.E);
        Algorithms.registerConstant("nan", Double.NaN);
        Algorithms.registerConstant("Infinity", Double.POSITIVE_INFINITY);
        Algorithms.registerConstant("phi", MathUtils.PHI);
    }

    @Test
    public void shuntingYardTest() throws Exception {
        assertEquals(new Constant(Math.E), shuntingYard("e", Collections.emptyList()));
        Product expected = new Product(new Constant(5), new Sum(new Constant(2), new Constant(3)));
        assertEquals(expected, shuntingYard("5(2 + 3)", Collections.emptyList()));
        Product expected2 = new Product(new Sum(new Constant(5), new Constant(2)), new Sum(new Constant(3), new Constant(4)));
        assertEquals(expected2, shuntingYard("(5 + 2)(3 + 4)", Collections.emptyList()));
        Sum expected3 = new Sum(new Power(new Unknown("x"), new Constant(2)), new Power(new Unknown("x"), new Constant(-2)));
        assertEquals(expected3, shuntingYard("(x^2)(x^-2)", Collections.singletonList("x")));
    }

    @Test
    public void evaluateTest() throws Exception {
        assertEquals(21.0, evaluate("5 + 2 * 8", Collections.emptyMap()).doubleValue(), 0.0);
        assertEquals(56.0, evaluate("8(5 + 2)", Collections.emptyMap()).doubleValue(), 0.0);
        assertEquals(56.0, evaluate("(5 + 2) * 8", Collections.emptyMap()).doubleValue(), 0.0);
        assertEquals(19.7392088021, evaluate("2pi^2", Collections.emptyMap()).doubleValue(), Skript.EPSILON);
        assertEquals(MathUtils.PHI, evaluate("phi", Collections.emptyMap()).doubleValue(), Skript.EPSILON);
        assertEquals(8.0, evaluate("2(x+2)", new HashMap<String, Number>(){{put("x", 2.0);}}).doubleValue(), Skript.EPSILON);
        assertEquals(77.8802336483881, evaluate("2^(x*pi)", new HashMap<String, Number>(){{put("x", 2.0);}}).doubleValue(), Skript.EPSILON);
    }

}