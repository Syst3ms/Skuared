package fr.syst3ms.skuared.expressions;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.syst3ms.skuared.util.Algorithms;
import org.jetbrains.annotations.NotNull;

public class ExprRpn extends SimplePropertyExpression<String, String> {

	static {
		PropertyExpression.register(
			ExprRpn.class,
			String.class,
			"(rpn|(reverse polish|postfix) [notation])",
			"string"
		);
	}

	@NotNull
	@Override
	protected String getPropertyName() {
		return "rpn";
	}

	@NotNull
	@Override
	public String convert(@NotNull String s) {
		return Algorithms.tokensToString(s);
	}

	@NotNull
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
}
