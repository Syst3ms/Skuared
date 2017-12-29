package fr.syst3ms.skuared.util;

import ch.njol.skript.Skript;
import fr.syst3ms.skuared.util.evaluation.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

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
        assertEquals(Constant.getConstant(Math.E), parseMathExpression("e", Collections.emptyList(), false).getFirst());
        MathTerm expected = new Product(Constant.getConstant(5), new Sum(Constant.TWO, Constant.getConstant(3)));
        assertEquals(expected, parseMathExpression("5(2 + 3)", Collections.emptyList(), false).getFirst());
        MathTerm expected2 = new Product(new Sum(Constant.getConstant(5), Constant.TWO), new Sum(Constant.getConstant(3), Constant.getConstant(4)));
        assertEquals(expected2, parseMathExpression("(5 + 2)(3 + 4)", Collections.emptyList(), false).getFirst());
        MathTerm expected3 = new Product(new Power(new Unknown("x"), Constant.TWO), new Power(new Unknown("x"), Constant.TWO.getNegative()));
        assertEquals(expected3, parseMathExpression("(x^2)(x^-2)", Collections.singletonList("x"), false).getFirst());
        MathTerm expected4 = new Difference(new Product(Constant.TWO, new Unknown("x").getSquared()), new Power(new Unknown("y"), Constant.getConstant(3)));
        assertEquals(expected4, parseMathExpression("2x^2-y^3", Arrays.asList("x", "y"), false).getFirst());
        MathTerm expected5 = new Sum(
            new Sum(
                new Sum(
                    new Product(new Unknown("a"), new Power(new Unknown("x"), Constant.getConstant(3))),
                    new Product(new Unknown("b"), new Unknown("x").getSquared())
                ),
                new Product(new Unknown("c"), new Unknown("x"))
            ),
            new Unknown("d")
        );
        assertEquals(expected5, parseMathExpression("a*x^3+b*x^2+c*x+d", Arrays.asList("x", "a", "b", "c", "d"), false).getFirst());
    }

    @Test
    public void evaluateTest() throws Exception {
        assertEquals(21.0, evaluate("5 + 2 * 8", Collections.emptyMap()).doubleValue(), 0.0);
        assertEquals(56.0, evaluate("8(5 + 2)", Collections.emptyMap()).doubleValue(), 0.0);
        assertEquals(56.0, evaluate("(5 + 2) * 8", Collections.emptyMap()).doubleValue(), 0.0);
        assertEquals(19.7392088021, evaluate("2pi^2", Collections.emptyMap()).doubleValue(), Skript.EPSILON);
        assertEquals(MathUtils.PHI, evaluate("phi", Collections.emptyMap()).doubleValue(), Skript.EPSILON);
        assertEquals(8.0, evaluate("2(x+2)", Algorithms.getXMap(2.0)).doubleValue(), Skript.EPSILON);
        assertEquals(77.8802336483881, evaluate("2^(x*pi)", Algorithms.getXMap(2.0)).doubleValue(), Skript.EPSILON);
        assertEquals(
            -19.0,
            evaluate("2x^2-y^3", MapBuilder.builder("x", 2.0).add("y", 3.0).build()).doubleValue(),
            0.0
        );
    }

}