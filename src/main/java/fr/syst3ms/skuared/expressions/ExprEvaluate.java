package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.ExprParse;
import ch.njol.skript.expressions.ExprParseError;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.ReflectionUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class ExprEvaluate extends SimpleExpression<Number> {
	private Expression<String> expr;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		expr = (Expression<String>) exprs[0];
		return true;
	}

	@Nullable
	@Override
	protected Number[] get(Event e) {
		String ex = expr.getSingle(e);
		if (ex == null) {
			ReflectionUtils.setStaticField(ExprParse.class, "lastError","The evaluated expression is undefined");
			return null;
		}
		return new Number[]{Algorithms.evaluateRpn(ex)};
	}

	@NotNull
	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@NotNull
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "evaluate " + expr.toString(e, debug);
	}
}
