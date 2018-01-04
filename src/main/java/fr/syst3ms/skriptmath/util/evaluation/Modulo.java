package fr.syst3ms.skriptmath.util.evaluation;

import fr.syst3ms.skriptmath.util.MathUtils;

import java.util.function.BinaryOperator;

public class Modulo extends DoubleOperandTerm {

	public Modulo(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::mod;
	}

	@Override
	protected String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond) {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Modulo.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Modulo.class, true) : second.asString();
		if (calling == Power.class || (calling == Division.class || calling == getClass()) && isSecond) {
			return String.format("(%s %% %s)", f, s);
		} else {
			return String.format("%s %% %s", f, s);
		}
	}

	@Override
	public MathTerm simplifyOperation() {
		return this;
	}

	@Override
	public String asString() {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Modulo.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Modulo.class, true) : second.asString();
		return String.format("%s %% %s", f, s);
	}

	@Override
	public MathTerm getNegative() {
		return new Product(Constant.getConstant(-1), this).simplify();
	}
}
