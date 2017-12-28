package fr.syst3ms.skuared.util.evaluation;

import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import fr.syst3ms.skuared.util.MathUtils;

import java.util.Collections;
import java.util.function.BinaryOperator;

public class Power extends DoubleOperandTerm {

	public Power(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::pow;
	}

	@Override
	public String asString() {
		return "(" + first + " ^ " + second + ")";
	}
}
