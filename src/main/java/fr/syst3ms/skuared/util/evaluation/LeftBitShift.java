package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class LeftBitShift extends DoubleOperandTerm {

	public LeftBitShift(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::shl;
	}

	@Override
	protected String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond) {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(LeftBitShift.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(LeftBitShift.class, true) : second.asString();
		if ((calling == getClass() || calling == RightBitShift.class || calling == UnsignedRightBitShift.class) && !isSecond) {
			return String.format("%s << %s", f, s);
		} else {
			return String.format("(%s << %s)", f, s);
		}
	}

	@Override
	public MathTerm simplifyOperation() {
		return new Product(first, new Power(Constant.TWO, second)).simplify();
	}

	@Override
	public String asString() {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(LeftBitShift.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(LeftBitShift.class, true) : second.asString();
		return String.format("%s << %s", f, s);
	}

	@Override
	public MathTerm getNegative() {
		return new Product(Constant.getConstant(-1), this).simplify();
	}
}
