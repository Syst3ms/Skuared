package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class Product extends DoubleOperandTerm {

	public Product(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::times;
	}

	@Override
	public MathTerm simplify() {
		MathTerm sup = super.simplify();
		if (sup != this) {
			return sup;
		} else if (first.equals(second)) {
			return new Power(first, Constant.TWO);
		} else if (first == Constant.ZERO || second == Constant.ZERO) {
			return Constant.ZERO;
		} else if (first == Constant.ONE || second == Constant.ONE) {
			return first == Constant.ONE ? second : first;
		}
		return this;
	}

	@Override
	public String toString() {
		return "(" + getFirst().toString() + " * " + getSecond().toString() + ")";
	}
}
