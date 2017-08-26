package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.Algorithms;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class ExprLevenshteinDistance extends SimpleExpression<Number> {
	private Expression<String> first, second;
	private boolean ignoreCase;

	static {
		Skript.registerExpression(
			ExprLevenshteinDistance.class,
			Number.class,
			ExpressionType.COMBINED,
			"(levenshtein|string) distance (betwee	n|of) %string% and %string% [(1¦ignor(e|ing) case)]"
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
		first = (Expression<String>) exprs[0];
		second = (Expression<String>) exprs[1];
		ignoreCase = parseResult.mark == 1;
		return true;
	}

	@org.jetbrains.annotations.Nullable
	@Override
	protected Number[] get(Event e) {
		String f = first.getSingle(e), s = second.getSingle(e);
		if (f == null || s == null)
			return new Number[]{Double.NaN};
		return new Number[]{Algorithms.levenshtein(f, s, ignoreCase)};
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
		return "levenshtein distance between " + first.toString(e, debug) + " and " + second.toString(e, debug);
	}
}
