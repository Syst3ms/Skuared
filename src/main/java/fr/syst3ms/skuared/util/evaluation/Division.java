package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class Division extends DoubleOperandTerm {

	public Division(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::divide;
	}

	@Override
	String getAsString(Class<? extends DoubleOperandTerm> calling) {
		return null;
	}

	@Override
	public MathTerm simplify() {
		if (!first.hasUnknown() && !second.hasUnknown()) {
			return Constant.getConstant(compute(null));
		} else if (second == Constant.ONE) {
			return first;
		} else if (second == Constant.ZERO) {
			return Constant.NAN;
		} else if (first.equals(second)) {
			return Constant.ONE;
		} else if (first == Constant.ZERO) {
			return Constant.ZERO;
		}
		return this;
	}

	@Override
	public String asString() {
		return "(" + first + " / " + second + ")";
	}
}
