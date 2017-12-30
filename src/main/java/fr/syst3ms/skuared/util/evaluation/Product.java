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
	String getAsString(Class<? extends DoubleOperandTerm> calling) {
		return null;
	}

	@Override
	public MathTerm simplifyOperation() {
		if (first == Constant.ONE) {
			return second;
		} else if (second == Constant.ONE) {
			return first;
		} else if (first == Constant.ZERO || second == Constant.ZERO) {
			return Constant.ZERO;
		} else if (first.equals(second)) {
			return first.getSquared();
		} else if (second.equals(first.getReciprocal())) {
			return Constant.ONE;
		} else if (first instanceof Constant && (second instanceof Sum || second instanceof Difference)) {
			DoubleOperandTerm s = (DoubleOperandTerm) second;
			if (second instanceof Sum) {
				return new Sum(new Product(first, s.getFirst()), new Product(first, s.getSecond())).simplify();
			} else {
				return new Difference(new Product(first, s.getFirst()), new Product(first, s.getSecond())).simplify();
			}
		} else if ((first instanceof Sum || first instanceof Difference) && first instanceof Constant) {
			DoubleOperandTerm f = (DoubleOperandTerm) first;
			return new Sum(new Product(second, f.getFirst()), new Product(second, f.getSecond())).simplify();
		} else if ((first instanceof Sum || first instanceof Difference) && (second instanceof Sum || second instanceof Difference)) {
			DoubleOperandTerm f = (DoubleOperandTerm) first;
			DoubleOperandTerm s = (DoubleOperandTerm) second;
			MathTerm a = f.getFirst();
			MathTerm b = f.getSecond();
			MathTerm c = s.getFirst();
			MathTerm d = s.getSecond();
			if (first instanceof Sum && second instanceof Sum) {
				return new Sum(
						new Sum(new Product(a, c), new Product(a, d)),
						new Sum(new Product(b, c), new Product(b, d))
				).simplify();
			} else if (first instanceof Sum && second instanceof Difference) {
				return new Sum(
						new Difference(new Product(a, c), new Product(a, d)),
						new Difference(new Product(b, c), new Product(b, d))
				).simplify();
			} else if (first instanceof Difference && second instanceof Sum) {
				return new Difference(
						new Sum(new Product(a, c), new Product(a, d)),
						new Sum(new Product(b, c), new Product(b, d))
				).simplify();
			} else {
				return new Difference(
						new Difference(new Product(a, c), new Product(a, d)),
						new Difference(new Product(b, c), new Product(b, d))
				).simplify();
			}
		} else if (first instanceof Division && second instanceof Division) {
			Division f = (Division) first;
			Division s = (Division) second;
			return new Division(new Product(f.getFirst(), s.getFirst()), new Product(f.getSecond(), s.getSecond())).simplify();
		}
		return this;
	}

	@Override
	public String asString() {
		return "(" + first + " * " + second + ")";
	}
}
