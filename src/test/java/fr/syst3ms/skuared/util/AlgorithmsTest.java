package fr.syst3ms.skuared.util;

import fr.syst3ms.skuared.util.math.Associativity;
import fr.syst3ms.skuared.util.math.MathUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class AlgorithmsTest {
	@Test
	public void evaluateRpn() throws Exception {
		Algorithms.registerOperator(">>", MathUtils::shr, Associativity.LEFT, 4);
		Algorithms.registerOperator("<<", MathUtils::shl, Associativity.LEFT, 4);
		Algorithms.registerOperator(">>>", MathUtils::ushr, Associativity.LEFT, 4);
		Algorithms.registerOperator("+", MathUtils::plus, Associativity.LEFT, 3);
		Algorithms.registerOperator("-", MathUtils::minus, Associativity.LEFT, 3);
		Algorithms.registerOperator("*", MathUtils::times, Associativity.LEFT, 2);
		Algorithms.registerOperator("/", MathUtils::divide, Associativity.LEFT, 2);
		Algorithms.registerOperator("^", MathUtils::pow, Associativity.RIGHT, 1);
		Algorithms.registerConstant("pi", Math.PI);
		Algorithms.registerConstant("e", Math.E);
		Algorithms.registerConstant("nan", Double.NaN);
		Algorithms.registerConstant("Infinity", Double.POSITIVE_INFINITY);
		assertEquals(Double.NaN, Algorithms.evaluateRpn("pi").doubleValue(),  0.0D);
		assertEquals(192L, Algorithms.evaluateRpn("3 << 9 / 3 << 3").longValue());
	}

	@Test
	public void shuntingYard() throws Exception {
		Algorithms.registerOperator(">>", MathUtils::shr, Associativity.LEFT, 4);
		Algorithms.registerOperator("<<", MathUtils::shl, Associativity.LEFT, 4);
		Algorithms.registerOperator(">>>", MathUtils::ushr, Associativity.LEFT, 4);
		Algorithms.registerOperator("+", MathUtils::plus, Associativity.LEFT, 3);
		Algorithms.registerOperator("-", MathUtils::minus, Associativity.LEFT, 3);
		Algorithms.registerOperator("*", MathUtils::times, Associativity.LEFT, 2);
		Algorithms.registerOperator("/", MathUtils::divide, Associativity.LEFT, 2);
		Algorithms.registerOperator("^", MathUtils::pow, Associativity.RIGHT, 1);
		Algorithms.registerConstant("pi", Math.PI);
		Algorithms.registerConstant("e", Math.E);
		Algorithms.registerConstant("nan", Double.NaN);
		Algorithms.registerConstant("Infinity", Double.POSITIVE_INFINITY);
		assertArrayEquals(new Object[]{"5", "2", "+"}, Algorithms.shuntingYard("5 + 2").toArray());
		assertArrayEquals(new Object[]{"2", "5", "1", "3", "<<", "+", "*"}, Algorithms.shuntingYard("2(5 + (1 << 3))").toArray());
		assertArrayEquals(new Object[]{"3", "9", "3", "/", "<<", "3", "<<"}, Algorithms.shuntingYard("3 << 9 / 3 << 3").toArray());
	}
}