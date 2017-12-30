package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class Power extends DoubleOperandTerm {

	public Power(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::pow;
	}

	@Override
	String getAsString(Class<? extends DoubleOperandTerm> calling) {
		return null;
	}

	@Override
	public MathTerm simplifyOperation() {
		if (second == Constant.ONE) {
			return first;
		} else if (second == Constant.ZERO) {
			return first == Constant.ZERO ? Constant.NAN : Constant.ONE;
		} else if (second == Constant.getConstant(0.5)) {
			return first.getSquareRoot();
		} else if (second == Constant.getConstant(-1)) {
			return first.getReciprocal();
		} else if ((first instanceof Sum || first instanceof Difference) && second == Constant.TWO) {
			DoubleOperandTerm f = (DoubleOperandTerm) first;
			MathTerm a = f.getFirst();
			MathTerm b = f.getSecond();
			if (first instanceof Sum) {
				return new Sum(
						new Sum(a.getSquared(), new Product(Constant.TWO, new Product(a, b))),
						b.getSquared()
				).simplify();
			} else {
				return new Sum(
						new Difference(a.getSquared(), new Product(Constant.TWO, new Product(a, b))),
						b.getSquared()
				).simplify();
			}
		}
		return this;
	}

	@Override
	public String asString() {
		return null;
	}
}
