package fr.syst3ms.skuared.util.evaluation;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class MathExpression {
	private List<String> tokens;
	@Nullable
	private MathTerm term;
	private List<String> unknowns;

	private HashMap<String, Number> unknownData;

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

}
