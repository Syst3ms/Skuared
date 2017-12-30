package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

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
	String getAsString(Class<? extends DoubleOperandTerm> calling) {
		return null;
	}

	@Override
	public MathTerm simplifyOperation() {
		return this;
	}

	@Override
	public String asString() {
		return "(" + first + " % " + second + ")";
	}
}
