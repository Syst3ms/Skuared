package fr.syst3ms.skuared.util.evaluation;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MathExpression {
	private List<String> tokens;
	@Nullable
	private MathTerm term;
	private List<String> unknowns;

	private Map<String, Number> unknownData;

	public MathExpression(List<String> tokens, @Nullable MathTerm term, List<String> unknowns) {
		this.tokens = tokens;
		this.term = term;
		this.unknowns = unknowns;
	}

	public List<String> getTokens() {
		return tokens;
	}

	@Nullable
	public MathTerm getTerm() {
		return term;
	}

	public List<String> getUnknowns() {
		return unknowns;
	}

	public void map(String unknown, Number value) {
		if (unknowns.contains(unknown)) {
			unknownData.put(unknown, value);
		}
	}

	public Number evaluate() {
		if (term == null) {
			return null;
		} else {
			return term.compute(unknownData);
		}
	}
}
