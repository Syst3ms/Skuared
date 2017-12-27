package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class Difference extends DoubleOperandTerm {

	public Difference(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::minus;
	}

	@Override
	public String toString() {
		return "(" + first.toString() + " - " + second.toString() + ")";
	}

	@Override
	public MathTerm simplify() {
		MathTerm sup = super.simplify();
		if (sup != this) {
			return sup;
		} else if (first.equals(second)) {
			return Constant.ZERO;
		}
		return this;
	}
}
