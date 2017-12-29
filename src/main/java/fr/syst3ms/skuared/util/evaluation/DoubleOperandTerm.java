package fr.syst3ms.skuared.util.evaluation;

import java.util.Map;
import java.util.function.BinaryOperator;

public abstract class DoubleOperandTerm extends MathTerm {
	protected MathTerm first, second;

	protected DoubleOperandTerm(MathTerm first, MathTerm second) {
		this.first = first;
		this.second = second;
	}

	abstract BinaryOperator<Number> getFunction();

	abstract String getAsString(Class<? extends DoubleOperandTerm> calling);

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
