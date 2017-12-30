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
	String getAsString(Class<? extends DoubleOperandTerm> calling) {
		return null;
	}

	@Override
	public MathTerm simplifyOperation() {
		if (first == Constant.ZERO) {
			return second.getNegative();
		} else if (second == Constant.ZERO) {
			return first;
		} else if (second instanceof Constant && ((Constant) second).isNegative()) {
			return new Sum(first, second.getNegative());
		} else if (second instanceof Sum) {
			Sum s = (Sum) second;
			return new Difference(first, new Difference(s.getFirst(), s.getSecond())).simplify();
		} else if (second instanceof Difference) {
			Difference s = (Difference) second;
			return new Difference(first, new Sum(s.getFirst(), s.getSecond())).simplify();
		}
		return this;
	}

	@Override
	public String asString() {
		return "(" + first + " - " + second + ")";
	}
}
