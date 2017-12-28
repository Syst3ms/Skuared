package fr.syst3ms.skuared.util.evaluation;

import org.eclipse.jdt.annotation.Nullable;

import java.util.Map;

public class Unknown extends MathTerm {
	private String name;

	public Unknown(String name) {
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
		return name;
	}
}
