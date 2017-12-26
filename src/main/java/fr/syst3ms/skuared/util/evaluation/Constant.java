package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.Map;

public class Constant implements MathTerm {
	private Number value;

	public Constant(Number value) {
		this.value = value;
	}

	@Override
	public Number compute(Map<String, Number> unknowns) {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof Constant && (this == obj || MathUtils.equals(value, ((Constant) obj).value));
	}

	@Override
	public String toString() {
		return value.toString();
	}
}