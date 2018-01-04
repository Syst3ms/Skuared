package fr.syst3ms.skriptmath.util.evaluation;

import fr.syst3ms.skriptmath.util.MathUtils;

import java.util.function.BinaryOperator;

public class UnsignedRightBitShift extends DoubleOperandTerm {

	public UnsignedRightBitShift(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::ushr;
	}

	@Override
	protected String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond) {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(UnsignedRightBitShift.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(UnsignedRightBitShift.class, true) : second.asString();
		if ((calling == getClass() || calling == LeftBitShift.class || calling == RightBitShift.class) && !isSecond) {
			return String.format("%s >>> %s", f, s);
		} else {
			return String.format("(%s >>> %s)", f, s);
		}
	}

	@Override
	protected MathTerm simplifyOperation() {
		return new Division(first, new Power(Constant.TWO, second)).simplify();
	}

	@Override
	public String asString() {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(UnsignedRightBitShift.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(UnsignedRightBitShift.class, true) : second.asString();
		return String.format("%s >>> %s", f, s);
	}

	@Override
	public MathTerm getNegative() {
		return new Product(Constant.getConstant(-1), this).simplify();
	}
}
