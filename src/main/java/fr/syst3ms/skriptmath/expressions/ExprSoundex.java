package fr.syst3ms.skriptmath.expressions;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.syst3ms.skriptmath.util.Algorithms;

public class ExprSoundex extends SimplePropertyExpression<String, String> {

	static {
		PropertyExpression.register(ExprSoundex.class, String.class, "soundex [code]", "string");
	}

	@Override
	protected String getPropertyName() {
		return "soundex code";
	}

	@Override
	public String convert(String s) {
		return Algorithms.soundex(s);
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
}
