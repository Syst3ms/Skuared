package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class Power extends DoubleOperandTerm {
	private MathTerm first, second;

	public Power(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::pow;
	}

	@Override
	public String toString() {
		return "(" + getFirst().toString() + " ^ " + getSecond().toString() + ")";
	}
}
