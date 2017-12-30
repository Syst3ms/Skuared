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
	protected String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond) {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Sum.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Sum.class, true) : second.asString();
		if (calling == LeftBitShift.class || calling == RightBitShift.class || calling == UnsignedRightBitShift.class || calling == Difference.class && !isSecond) {
			return String.format("%s + %s", f, s);
		} else {
			return String.format("(%s + %s)", f, s);
		}
	}


	@Override
	protected MathTerm simplifyOperation() {
		if (first == Constant.ZERO) {
			return second;
		} else if (second == Constant.ZERO) {
			return first;
		} else if (second instanceof Constant && ((Constant) second).isNegative()) {
			return new Difference(first, second.getNegative()).simplify();
		} else if (first.equals(second)) {
			return new Product(first, Constant.TWO);
		}
		return this;
	}

	@Override
	public String asString() {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Sum.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Sum.class, true) : second.asString();
		return String.format("%s + %s", f, s);
	}
}
