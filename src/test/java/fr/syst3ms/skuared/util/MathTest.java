package fr.syst3ms.skuared.util;

import org.junit.Test;

import java.math.BigDecimal;

import static fr.syst3ms.skuared.util.Algorithms.registerConstant;
import static fr.syst3ms.skuared.util.Algorithms.registerOperator;
import static org.junit.Assert.assertEquals;

/**
 * Created by ARTHUR on 09/12/2017.
 */
public class MathTest {

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
    public void sigma() throws Exception {
        assertEquals(BigDecimal.valueOf(55L), MathUtils.sigma("x", 1L, 10L));
        assertEquals(BigDecimal.valueOf(1023.0D), MathUtils.sigma("2^x/2", 1L, 10L));
        assertEquals(24, MathUtils.sigma("(1+1/x)^x", 1L, 10L).intValue());
    }

    @Test
    public void chainedProduct() throws Exception {
        assertEquals(Math.round(MathUtils.gamma(11)), MathUtils.chainedProduct("x", 1L, 10L).intValue());
        assertEquals(1.0d / 3628800.0d, MathUtils.chainedProduct("1/x", 1L, 10L).doubleValue(), 1e-5d);
        assertEquals((Math.exp(55.0d) / 3628800.0d) / 1e17d, MathUtils.chainedProduct("e^x/x", 1L, 10L).doubleValue() / 1e17d, 0.0001);
    }

    @Test
    public void gamma() throws Exception {
        assertEquals(3628800, Math.round(MathUtils.gamma(11)));
        assertEquals(1.77245d, MathUtils.gamma(0.5), 0.0001);
        assertEquals(1.56746d, MathUtils.gamma(Math.E), 0.0001);
    }

}