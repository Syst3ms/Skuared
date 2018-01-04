package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class RightBitShift extends DoubleOperandTerm {

	public RightBitShift(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::shr;
	}

	@Override
	protected String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond) {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(RightBitShift.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(RightBitShift.class, true) : second.asString();
		if ((calling == getClass() || calling == LeftBitShift.class || calling == UnsignedRightBitShift.class) && !isSecond) {
			return String.format("%s >> %s", f, s);
		} else {
			return String.format("(%s >> %s)", f, s);
		}
	}

	@Override
	protected MathTerm simplifyOperation() {
		return this;
	}

	@Override
	public MathTerm getNegative() {
		return new Product(Constant.getConstant(-1), this).simplify();
	}

	@Override
	public String asString() {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(RightBitShift.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(RightBitShift.class, true) : second.asString();
		return String.format("%s >> %s", f, s);
	}


}
