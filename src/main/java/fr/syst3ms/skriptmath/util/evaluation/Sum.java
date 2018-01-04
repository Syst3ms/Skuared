package fr.syst3ms.skriptmath.util.evaluation;

import fr.syst3ms.skriptmath.util.MathUtils;

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
		} else if (first.equals(second)) {
			return new Product(first, Constant.TWO);
		} else if (first.equals(second.getNegative())) {
			return Constant.ZERO;
		} else if (first instanceof Division && second instanceof Division) {
			Division f = (Division) first;
			Division s = (Division) second;
			if (f.getSecond().equals(s.getSecond())) {
				return new Division(new Product(f.getFirst(), s.getFirst()), f.getSecond()).simplify();
			}
		}
		return this;
	}

	@Override
	public String asString() {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Sum.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Sum.class, true) : second.asString();
		return String.format("%s + %s", f, s);
	}

	@Override
	public MathTerm getNegative() {
		return new Difference(first.getNegative(), second);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DoubleOperandTerm)) {
			return false;
		} else if (this == obj) {
			return true;
		} else {
			DoubleOperandTerm o = (DoubleOperandTerm) obj;
			return first.equals(o.first) && second.equals(o.second) || first.equals(o.second) && second.equals(o.first);
		}
	}
}
