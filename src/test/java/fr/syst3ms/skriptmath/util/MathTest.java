package fr.syst3ms.skriptmath.util;

import ch.njol.skript.Skript;
import fr.syst3ms.skriptmath.util.evaluation.*;
import org.junit.Test;

import java.util.Collections;

import static fr.syst3ms.skriptmath.util.Algorithms.*;
import static org.junit.Assert.assertEquals;

public class MathTest {
    static {
        registerOperator(">>", RightBitShift.class, Associativity.LEFT, 4);
        registerOperator("<<", LeftBitShift.class, Associativity.LEFT, 4);
        registerOperator(">>>", UnsignedRightBitShift.class, Associativity.LEFT, 4);
        registerOperator("+", Sum.class, Associativity.LEFT, 3);
        registerOperator("-", Difference.class, Associativity.LEFT, 3);
        registerOperator("*", Product.class, Associativity.LEFT, 2);
        registerOperator("/", Division.class, Associativity.LEFT, 2);
        registerOperator("%", Modulo.class, Associativity.LEFT, 2);
        registerOperator("^", Power.class, Associativity.RIGHT, 1);
        registerConstant("pi", Math.PI);
        registerConstant("e", Math.E);
        registerConstant("nan", Double.NaN);
        registerConstant("Infinity", Double.POSITIVE_INFINITY);
        registerConstant("phi", MathUtils.PHI);
    }

    @Test
    public void sigma() throws Exception {
        assertEquals(55, MathUtils.sigma("x", 1L, 10L).intValue());
        assertEquals(1023.0d, MathUtils.sigma("2^x/2", 1L, 10L).doubleValue(), 0.001);
        assertEquals(24.358924454916741049d, MathUtils.sigma("(1+1/x)^x", 1L, 10L).doubleValue(), Skript.EPSILON);
    }

    @Test
    public void chainedProduct() throws Exception {
        assertEquals(Math.round(MathUtils.gamma(11)), MathUtils.chainedProduct("x", 1L, 10L).intValue());
        assertEquals(1.0d / 3628800.0d, MathUtils.chainedProduct("1/x", 1L, 10L).doubleValue(), 1e-5d);
        assertEquals((Math.exp(55.0d) / 3628800.0d) / 1e17d, (MathUtils.chainedProduct("e^x/x", 1L, 10L).doubleValue() / 1e17d), 0.0001);
    }

    @Test
    public void gamma() throws Exception {
        assertEquals(3628800, Math.round(MathUtils.gamma(11)));
        assertEquals(1.77245d, MathUtils.gamma(0.5), 0.0001);
        assertEquals(1.56746d, MathUtils.gamma(Math.E), 0.0001);
    }


    @Test
    public void indefiniteDerivative() throws Exception {
        assertEquals(Constant.ZERO, MathUtils.indefiniteDerivative(Constant.getConstant(Math.random())));
        assertEquals(Constant.ONE, MathUtils.indefiniteDerivative(new Unknown("x", false)));
        assertEquals(Constant.TWO, MathUtils.indefiniteDerivative(new Product(Constant.TWO, new Unknown("x", false))).simplify());
        MathTerm expected = new Product(Constant.getConstant(3), new Unknown("x", false).getSquared()); // x^3 -> 3x^2
        assertEquals(expected, MathUtils.indefiniteDerivative(new Power(new Unknown("x", false), Constant.getConstant(3))).simplify());
        MathTerm param = new Power(Constant.E, new Difference(Constant.getConstant(5), new Unknown("x", false)));
        assertEquals(param.getNegative(), MathUtils.indefiniteDerivative(param).simplify());
    }

    @Test
    public void asString() throws Exception {
        assertEquals("5 + 8 * 5", parseMathExpression("5 + 8 * 5", Collections.emptyList(), false).getTerm().asString());
        assertEquals("5 / x / x ^ 2", parseMathExpression("5 / x / x^2", Collections.singletonList("x"), false).getTerm().asString());
        assertEquals("8(5 + 2)", parseMathExpression("8 * (5 +2)", Collections.emptyList(), false).getTerm().asString());
    }

    @Test
    public void simplify() throws Exception {
        assertEquals(Constant.ONE, parseMathExpression("(x + x) / (x * 2)", Collections.singletonList("x"), true).getTerm());
        assertEquals(Constant.ZERO, parseMathExpression("x((x-x) / 2x^2)", Collections.singletonList("x"), true).getTerm());
        MathTerm expected = new Difference(new Product(Constant.TWO, new Unknown("x", false).getSquared()), new Unknown("x", false));
        assertEquals(expected, parseMathExpression("x(2x-1)", Collections.singletonList("x"), true).getTerm());
        assertEquals(new Division(new Unknown("x", false).getSquared(), Constant.TWO), parseMathExpression("x(x/2)", Collections.singletonList("x"), true).getTerm());
    }
}