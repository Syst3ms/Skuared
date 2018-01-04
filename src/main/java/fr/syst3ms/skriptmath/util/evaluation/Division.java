package fr.syst3ms.skriptmath.util.evaluation;

import fr.syst3ms.skriptmath.util.MathUtils;

import java.util.function.BinaryOperator;

public class Division extends DoubleOperandTerm {

	public Division(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::divide;
	}

	@Override
	protected String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond) {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Division.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Division.class, true) : second.asString();
		if (calling == Power.class || (calling == Modulo.class || calling == getClass() || calling == Product.class) && isSecond) {
			return String.format("(%s / %s)", f, s);
		} else {
			return String.format("%s / %s", f, s);
		}
	}

	@Override
	public MathTerm simplifyOperation() {
		if (second == Constant.ONE) {
			return first;
		} else if (second == Constant.ZERO) {
			return Constant.NAN;
		} else if (first.equals(second)) {
			return Constant.ONE;
		} else if (first == Constant.ZERO) {
			return Constant.ZERO;
		} else if (first instanceof Power || second instanceof Power) {
			if (first instanceof Power && second instanceof Power) {
				Power f = (Power) first;
				Power s = (Power) second;
				if (f.getSecond().equals(s.getSecond())) {
					return new Power(f.getFirst(), new Difference(f.getSecond(), s.getSecond())).simplify();
				}
			} else if (first instanceof Power) {
				Power f = (Power) first;
				if (f.getFirst().equals(second)) {
					return new Power(f.getFirst(), new Difference(f.getSecond(), Constant.ONE)).simplify();
				}
			} else if (second instanceof Power) {
				Power s = (Power) second;
				if (s.getFirst().equals(first)) {
					return new Power(s.getFirst(), new Difference(Constant.ONE, s.getSecond())).simplify();
				}
			}
		} else if (first instanceof Product && second instanceof Product) {
			Product f = (Product) first;
			Product s = (Product) second;
			MathTerm a = f.getFirst();
			MathTerm b = f.getSecond();
			MathTerm c = s.getFirst();
			MathTerm d = s.getSecond();
			MathTerm firstTry = new Division(a, d).simplify();
			if (!firstTry.equals(new Division(a, d))) {
				return new Product(firstTry, new Division(b, c));
			}
			MathTerm secondTry = new Division(b, c).simplify();
			if (!secondTry.equals(new Division(b, c))) {
				return new Product(secondTry, new Division(a, d));
			}
		} else if (first instanceof Product) {
			Product f = (Product) first;
			MathTerm a = f.getFirst();
			MathTerm b = f.getSecond();
			MathTerm c = f.getSecond();
			MathTerm firstTry = new Division(a, c).simplify();
			if (!firstTry.equals(new Division(a, c))) {
				return new Product(firstTry, b).simplify();
			}
			MathTerm secondTry = new Division(b, c).simplify();
			if (!secondTry.equals(new Division(b, c))) {
				return new Product(secondTry, a).simplify();
			}
		} else if (second instanceof Product) {
			Product s = (Product) second;
			MathTerm a = first;
			MathTerm b = s.getFirst();
			MathTerm c = s.getSecond();
			MathTerm firstTry = new Division(a, b).simplify();
			if (!firstTry.equals(new Division(a, b))) {
				return new Product(firstTry, c.getReciprocal()).simplify();
			}
			MathTerm secondTry = new Division(a, c).simplify();
			if (!secondTry.equals(new Division(a, c))) {
				return new Product(secondTry, b.getReciprocal()).simplify();
			}
		} else if (first instanceof Division && second instanceof Division) {
			Division f = (Division) first;
			Division s = (Division) second;
			MathTerm a = f.getFirst();
			MathTerm b = f.getSecond();
			MathTerm c = s.getFirst();
			MathTerm d = s.getSecond();
			return new Division(new Product(a, d), new Product(b, c)).simplify();
		}
		return this;
	}

	@Override
	public MathTerm getNegative() {
		return new Division(first.getNegative(), second);
	}

	@Override
	public String asString() {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Division.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Division.class, true) : second.asString();
		return String.format("%s / %s", f, s);
	}
}
