package fr.syst3ms.skuared.util.evaluation;

import java.util.Map;
import java.util.function.BinaryOperator;

public abstract class DoubleOperandTerm implements MathTerm {
	protected MathTerm first, second;

	protected DoubleOperandTerm(MathTerm first, MathTerm second) {
		this.first = first;
		this.second = second;
	}

	abstract BinaryOperator<Number> getFunction();

	protected abstract String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond);

	protected abstract MathTerm simplifyOperation();

	@Override
	public Number compute(Map<String, ? extends Number> unknowns) {
		if (first == null || second == null)
			return null;
		Number f = first.compute(unknowns);
		Number s = second.compute(unknowns);
		if (f == null || s == null)
			return null;
		return getFunction().apply(f, s);
	}

	public MathTerm getSecond() {
		return second;
	}

	public void setSecond(MathTerm second) {
		this.second = second;
	}

	public MathTerm getFirst() {
		return first;
	}

	public void setFirst(MathTerm first) {
		this.first = first;
	}

	@Override
	public boolean hasUnknown() {
		return first.hasUnknown() || second.hasUnknown();
	}

	@Override
	public String toString() {
		return asString();
	}

	@Override
	public int termCount() {
		return first.termCount() + second.termCount();
	}

	@Override
	public MathTerm simplify() {
		first = first.simplify();
		second = second.simplify();
		if (!first.hasUnknown() && !second.hasUnknown()) {
			return Constant.getConstant(compute(null));
		}
		MathTerm simp = simplifyOperation();
		return simp;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DoubleOperandTerm)) {
			return false;
		} else if (this == obj) {
			return true;
		} else {
			DoubleOperandTerm o = (DoubleOperandTerm) obj;
			return first.equals(o.first) && second.equals(o.second);
		}
	}
}
