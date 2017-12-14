package fr.syst3ms.skuared.expressions;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.syst3ms.skuared.util.Algorithms;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

	@Override
	public String convert(@NotNull String s) {
		List<String> tokens = Algorithms.shuntingYard(s, false);
		return tokens == null ? null : Algorithms.tokensToString(tokens);
	}

	@NotNull
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
}
