package fr.syst3ms.skuared.util.evaluation;

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
}
