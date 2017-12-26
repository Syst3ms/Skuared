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
}
