package fr.syst3ms.skuared.util;

import fr.syst3ms.skuared.util.evaluation.*;
import org.junit.Test;

import static fr.syst3ms.skuared.util.Algorithms.registerConstant;
import static fr.syst3ms.skuared.util.Algorithms.registerOperator;
import static org.junit.Assert.assertEquals;

public class MathTest {

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
    public void sigma() throws Exception {
        assertEquals(55, MathUtils.sigma("x", 1L, 10L).intValue());
        assertEquals(1023.0d, MathUtils.sigma("2^x/2", 1L, 10L).doubleValue(), 0.001);
        assertEquals(24, MathUtils.sigma("(1+1/x)^x", 1L, 10L).intValue());
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

}