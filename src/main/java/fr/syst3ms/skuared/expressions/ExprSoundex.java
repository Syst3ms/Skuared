package fr.syst3ms.skuared.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.syst3ms.skuared.util.Algorithms;

@Name("Soundex")
@Description({"Gets the [Soundex](https://en.wikipedia.org/wiki/Soundex) code of a given string"})
@Examples({"set {_soundex} to soundex code of \"skript\""})
@Since("1.1")
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
