package fr.syst3ms.skriptmath.util.evaluation;

import org.eclipse.jdt.annotation.Nullable;

import java.util.Map;

public class Unknown implements MathTerm {
	private String name;
	private boolean isNegative;

	public Unknown(String name, boolean isNegative) {
		this.name = name;
	}

	@Override
	@Nullable
	public Number compute(Map<String, ? extends Number> unknowns) {
		if (name == null)
			return null;
		return unknowns.get(name);
	}

	@Override
	public boolean hasUnknown() {
		return true;
	}

	@Override
	public MathTerm simplify() {
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Unknown)) {
			return false;
		} else {
			return this == obj || name.equals(((Unknown) obj).name);
		}
	}

	@Override
	public String asString() {
		return (isNegative ? "-" : "") + name;
	}

	@Override
	public int termCount() {
		return 1;
	}

	@Override
	public MathTerm getNegative() {
		return new Unknown(name, !isNegative);
	}

	@Override
	public String toString() {
		return asString();
	}
}
