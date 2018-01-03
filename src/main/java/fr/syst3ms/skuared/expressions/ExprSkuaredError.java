package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

@Name("Last Skuared Error")
@Description({"If something has gone wrong in nearly any of Skuared's syntaxes, will contain a descriptive error."})
@Examples({"set {_res} to eval expr \"2(3 + 5\"",
		"set {_err} to last skuared error # 'Mismatched parentheses'"})
@Since("1.1")
public class ExprSkuaredError extends SimpleExpression<String> {
	public static String lastError = null;

	static {
		Skript.registerExpression(
				ExprSkuaredError.class,
				String.class,
				ExpressionType.SIMPLE,
				"[the] last skuared error"
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		return true;
	}

	@Override
	protected String[] get(Event e) {
		return lastError == null ? null : new String[]{lastError};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "last skuared error";
	}
}
