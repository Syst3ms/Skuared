package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class UnsignedRightBitShift extends DoubleOperandTerm {
	private MathTerm first, second;

	public UnsignedRightBitShift(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::ushr;
	}
}
