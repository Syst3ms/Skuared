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
	protected String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond) {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Division.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Division.class, true) : second.asString();
		if (calling == Power.class || (calling == Modulo.class || calling == getClass() || calling == Product.class) && isSecond) {
			return String.format("(%s / %s)", f, s);
		} else {
			return String.format("%s / %s", f, s);
		}
	}

	@Override
	public MathTerm simplifyOperation() {
		if (second == Constant.ONE) {
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
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Division.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Division.class, true) : second.asString();
		return String.format("%s / %s", f, s);
	}
}
