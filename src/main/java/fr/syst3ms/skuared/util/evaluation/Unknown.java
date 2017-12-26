package fr.syst3ms.skuared.util.evaluation;

import java.util.Map;

public class Unknown implements MathTerm {
	private String name;

	public Unknown(String name) {
		this.name = name;
	}

	@Override
	public Number compute(Map<String, Number> unknowns) {
		return unknowns.get(name);
	}

	@Override
	public boolean hasUnknown() {
		return true;
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
	public String toString() {
		return name;
	}
}
