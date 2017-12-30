package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class Sum extends DoubleOperandTerm {

	public Sum(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::plus;
	}

	@Override
	String getAsString(Class<? extends DoubleOperandTerm> calling) {
		return null;
	}

	@Override
	protected MathTerm simplifyOperation() {
		if (first == Constant.ZERO) {
			return second;
		} else if (second == Constant.ZERO) {
			return first;
		}
		return this;
	}

	@Override
	public String asString() {
		return "(" + first + " + " + second + ")";
	}
}
