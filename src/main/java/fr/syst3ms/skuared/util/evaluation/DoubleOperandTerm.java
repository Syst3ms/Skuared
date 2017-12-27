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

	@Override
	public Number compute(Map<String, Number> unknowns) {
		return getFunction().apply(first.compute(unknowns), second.compute(unknowns));
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
	public MathTerm simplify() {
		first = first.simplify();
		second = second.simplify();
		if (!first.hasUnknown() && !second.hasUnknown()) {
			return Constant.getConstant(compute(null));
		}
		return this;
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
